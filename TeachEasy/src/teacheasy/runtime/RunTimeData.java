/*
 * Alistair Jewers
 * 
 * Copyright (c) 2015 Sofia Software Solutions. All Rights Reserved.
 * 
 */
package teacheasy.runtime;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.prefs.Preferences;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import learneasy.trackProgress.ProgressTracker;
import teacheasy.data.*;
import teacheasy.main.LearnEasyClient;
import teacheasy.render.Renderer;
import teacheasy.xml.*;
import teacheasy.xml.util.XMLNotification;
import teacheasy.xml.util.XMLNotification.Level;

/**
 * This class encapsulates the current state of the application and all its
 * data. The methods in this class are called when events occur in the top level
 * class which incorporates the GUI.
 * 
 * @author amj523
 * @version 1.0 20 Feb 2015
 */
public class RunTimeData {
    /* Parent Reference */
    LearnEasyClient parent;

    /* */
    private Group group;
    private Rectangle2D bounds;
    private Stage dialogStage;

    /* Class level variables */
    private int pageCount;
    private int currentPage;
    private boolean lessonOpen;
    private boolean hideDialog = false;
    private boolean pageDirection;
    /* Current Lesson */
    private Lesson lesson;

    private ProgressTracker progressTracker;

    /* XML Handler */
    private XMLHandler xmlHandler;

    /* Renderer */
    private Renderer renderer;
    
    /* home page*/
    private Preferences preference;
    
  

    /** Constructor method */
    public RunTimeData(Group nGroup, Rectangle2D nBounds,
            LearnEasyClient nParent) {
        /* Set the class level variables */
        this.group = nGroup;
        this.bounds = nBounds;
        this.parent = nParent;

        /* Instantiate class level variables */
        this.pageCount = 0;
        this.currentPage = 0;
        this.lessonOpen = false;
        
        pageDirection = false;
        
        /* Load the preference */
        setPreference();
        
        /* Instantiate an empty lesson */
        this.lesson = new Lesson();

        /* Instantiate the xml handler */
        this.xmlHandler = new XMLHandler();

        /* Instantiate the renderer */
        renderer = new Renderer(group, bounds);

        redraw(group, bounds);
    }

    /** Get the current page */
    public Page getCurrentPage() {
        return lesson.pages.get(currentPage);
    }

    /** Get the number of current page */
    public int getCurrentPageNumber() {
        return currentPage;
    }

    /** Set the current page number */
    public void setCurrentPage(int nPage) {
        if (nPage >= pageCount) {
            this.currentPage = pageCount - 1;
        } else if (nPage < 0) {
            this.currentPage = 0;
        } else {
            this.currentPage = nPage;
        }
    }

    /** Move to the next page */
    public void nextPage() {
        if (currentPage < pageCount - 1) {
            collatePageMarks();
            progressTracker.setVisitedPages(currentPage);
            currentPage++;
            redraw(group, bounds);
            if(progressTracker.pageStatus(currentPage)){
                /* grey out the available marks */
                renderer.answerBoxHandler.DisableAllAnswerBoxes();
                renderer.multipleChoiceHandler.DisableAllMultipleChoices();
            }
        }
    }

    public boolean checkPageCompleted() {
        /* Check to see if user has attempted all questions */
        if ((attemptedAllAvailableMarks()) || (hideDialog)) {
            return true;
        } else {

            /* Display dialog box */
            displayWarning();

            return false;
        }
    }

 

    /**
     * A method to count the marks on a page and pass them to progress tracking
     * class
     */
    private void collatePageMarks() {
        int pageMarks = 0;

        pageMarks += renderer.answerBoxHandler.currentPageMarks();
        pageMarks += renderer.multipleChoiceHandler.totalPageMarks();

       // progressTracker.collateMarks(pageMarks);
        progressTracker.inidividualPageMarks(pageMarks, currentPage);
    }

    /**
     * Informs user that not all questions have been attempted and gives them
     * the option of either continuing with the lesson or completing the
     * questions
     */
    private void displayWarning() {

        /* Create buttons */
        Button yesButton = new Button("Yes");
        yesButton.setId("yes");
        yesButton.setOnAction(new ButtonEventHandler());

        Button noButton = new Button("No");
        noButton.setId("no");
        noButton.setOnAction(new ButtonEventHandler());

        /* Check box giving user the option to never see warning again */
        final CheckBox dialogCheck = new CheckBox("Don't show me this again");
        dialogCheck.setId("dialog check box");
        dialogCheck.setIndeterminate(false);
        dialogCheck.setSelected(false);
        /* What to do if the check box is selected */
        dialogCheck.selectedProperty().addListener(
                new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                            Boolean old_val, Boolean new_val) {
                        if (dialogCheck.isSelected()) {
                            hideDialog = true;
                        }
                    }
                });

        /* Create the dialog box and draw on screen */
        dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage
                .setScene(new Scene(
                        VBoxBuilder
                                .create()
                                .children(
                                        new Text(
                                                "You haven't attempted every mark available on this page yet!"
                                                        + "\nAre you sure you want to continue to the next page without"
                                                        + " attempting them? \nYou won't be able to attempt them later."),
                                        yesButton, noButton, dialogCheck)
                                .alignment(Pos.CENTER).padding(new Insets(5))
                                .build()));
        dialogStage.show();
    }

    /**
     * Button Event Handler Class
     */
    public class ButtonEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            /* Get the button that was pressed */
            Button button = (Button) e.getSource();

            /* Get the id of the button pressed */
            String id = button.getId();

            /* Act according to id */
            /* If user wants to continue, go to next page */
            if (id.equals("yes")) {
            	System.out.println("yes");
            	
                dialogStage.close();
                
                if (isPageDirection()) {
                	nextPage();
                } else {
                	prevPage();
                }
                
                parent.updateUI();
            }
            /* If user wants to attempt questions close the dialog */
            else if (id.equals("no")) {
                dialogStage.close();
            }
        }
    }

    /** Checks if all marks available on the current page have been attempted */
    private boolean attemptedAllAvailableMarks() {

        if ((!renderer.answerBoxHandler.allBoxesDisabled())
                || (!renderer.multipleChoiceHandler
                        .allMultipleChoicesDisabled())) {
            return false;
        } else {
            return true;
        }
    }

    /** Move to the previous page */
    public void prevPage() {

        if (currentPage > 0) {
        	progressTracker.setVisitedPages(currentPage);
            currentPage--;
            redraw(group, bounds);

            if (progressTracker.pageStatus(currentPage)) {
                /* grey out the available marks */
                renderer.answerBoxHandler.DisableAllAnswerBoxes();
                renderer.multipleChoiceHandler.DisableAllMultipleChoices();
            }

        }
    }

    /** Check if there is a next page */
    public boolean isNextPage() {
        if (currentPage < pageCount - 1) {
            return true;
        } else {
            return false;
        }
    }

    /** Check if there is a previous page */
    public boolean isPrevPage() {
        if (currentPage > 0) {
            return true;
        } else {
            return false;
        }
    }

    /** Get the page count */
    public int getPageCount() {
        return pageCount;
    }

    /** Set the page count */
    public void setPageCount(int nPageCount) {
        this.pageCount = nPageCount;
    }

    /** Check if there is an open lesson */
    public boolean isLessonOpen() {
        return lessonOpen;
    }

    /** Set the lesson open flag */
    public void setLessonOpen(boolean flag) {
        lessonOpen = flag;
    }

    /** Get the current lesson object */
    public Lesson getLesson() {
        return lesson;
    }

    /** Open a lesson file */
    public boolean openLesson() {
        /* Create a file chooser */
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("XML Files", "*.xml"));
        fileChooser.setInitialDirectory(new File(System
                .getProperty("user.home")));

        /* Set the initial directory to the recent read path */
       /* if (xmlHandler.getRecentReadPath() != null) {
            fileChooser.setInitialDirectory(new File(new File(xmlHandler
                    .getRecentReadPath()).getParent()));
        }*/
        
        
        /* Set the initial directory to the recent read path */
        fileChooser.setInitialDirectory(new File(preference.get("recentlyOpened", "hi")));
        
       

        /* Get the file to open */
        File file = fileChooser.showOpenDialog(new Stage());

        /* Check that the file is not null */
        if (file == null) {
            return false;
        }

        /* Set the recent read Path */
        xmlHandler.setRecentReadPath(file.getAbsolutePath());

        /* Parse the file */
        ArrayList<XMLNotification> errorList = xmlHandler.parseXML2(file
                .getAbsolutePath());

        /* If any errors were found during parsing, do not load the lesson */
        if (errorList.size() > 0) {
            for (int i = 0; i < errorList.size(); i++) {
                System.out.println(errorList.get(i));
            }
        }

        if (XMLNotification.countLevel(errorList, Level.ERROR) > 0) {
            return false;
        }

        /* Get the lesson data */
        lesson = xmlHandler.getLesson();
        lesson.debugPrint();

        /* Open the lesson */
        setPageCount(lesson.pages.size());
        setCurrentPage(0);
        setLessonOpen(true);
        
        /*This needs moving somewhere more appropriate!*/
        progressTracker = new ProgressTracker(pageCount);
        
        redraw(group, bounds);
        return true;
    }

    /** Close the current lesson */
    public void closeLesson() {
        /* Clear the page */
        renderer.clearPage();

        /* Set the lesson open flag to false */
        setLessonOpen(false);

        /* Set the lesson to an empty lesson */
        lesson = new Lesson();

        /* Set the page count back to zero */
        setPageCount(0);

        /* Set the current page back to zero */
        setCurrentPage(0);

        redraw(group, bounds);
    }

    /** Redraw the content */
    public void redraw(Group group, Rectangle2D bounds) {
        if (isLessonOpen()) {
            /* Render the current page */
            renderer.renderPage(lesson.pages.get(currentPage));
        } else {
            /* Render the no lesson loaded screen */
            renderer.renderUnLoaded();
        }
    }

	public boolean isPageDirection() {
		return pageDirection;
	}

	public void setPageDirection(boolean pageDirection) {
		this.pageDirection = pageDirection;
	}
	
	private void setPreference(){
		 /* Instantiate preferences */
        preference = Preferences.userRoot().node(this.getClass().getName());
        
        /* String to store the path of the recently opened lesson*/
        String recentlyOpened = "recentlyOpened";
        
        /*Set actual path*/
        preference.put(recentlyOpened, "//userfs/dbb503/w2k/workspace/Dev/TeachEasy");
	}
}
