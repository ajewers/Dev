/*
 * Lewis Thresh
 * 
 * Copyright (c) 2015 Sofia Software Solutions. All Rights Reserved.
 * 
 */

package teacheasy.debug;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.*;

/**
 * This class is a dummy GUI for people to play with
 * to get to grips with JavaFX.
 * 
 * @author Lewis Thresh
 * @version 1.0 18 April 2015
 */

public class TE_GUI extends Application{

	Text text, botText, propText;
	
	/**
     * Override the start method inside the JavaFX Application
     * class. This is called to start the program.
     */
    @Override
    public void start(Stage primaryStage) {
    	
        /* Get the screen size */
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        /* Instantiate the scene and group */
        Group masterGroup = new Group();       
        Scene scene = new Scene(masterGroup, 500, 500);
	
        /* Instantiate content of Group */
	    GridPane masterGrid = new GridPane();
        HBox bottemBar = new HBox();
        MenuBar menuBar = new MenuBar();
        GridPane innerGrid = new GridPane();
        HBox topBar = new HBox();
        HBox centerPanel = new HBox();
        Group contentPanel = new Group();
        VBox propertiesPanel = new VBox();
        
        
        /* Content sizes and constraints */

        masterGroup.minHeight(200);
        masterGroup.maxHeight(200);
		masterGroup.minWidth(200);
		masterGroup.maxWidth(200);

		
		/* Set masterGrid row sizes */
		RowConstraints masterRow1 = new RowConstraints();
		masterRow1.setMaxHeight(100);
        masterGrid.getRowConstraints().add(masterRow1);
        
        /* Resize contentPane and propertiesPane to vertically fit screen */
    	RowConstraints masterRow2 = new RowConstraints();
		masterRow2.setFillHeight(true);
		masterRow2.setVgrow(Priority.ALWAYS);
        masterGrid.getRowConstraints().add(masterRow2);
        
    	RowConstraints masterRow3 = new RowConstraints();
		masterRow3.setMaxHeight(50);
        masterGrid.getRowConstraints().add(masterRow3);
        
        
        /* Set innerGrid row sizes */
        RowConstraints innerRow1 = new RowConstraints();
        innerRow1.setPrefHeight(50);
        innerGrid.getRowConstraints().add(innerRow1);
        
        /* Resize contentPane & propertiesPanel vertically*/
    	RowConstraints innerRow2 = new RowConstraints();
    	innerRow2.setFillHeight(true);
    	innerRow2.setVgrow(Priority.ALWAYS);
    	innerGrid.getRowConstraints().add(innerRow2);
        
    	/* innerGrid re-sizable width */
    	ColumnConstraints innerColumn = new ColumnConstraints();
    	innerColumn.setFillWidth(true);
    	innerColumn.setHgrow(Priority.ALWAYS);
    	innerGrid.getColumnConstraints().add(innerColumn);

    	
        /* centerPanel width (Content Pane and Properties Pane) */
        propertiesPanel.setMaxWidth(1000);

        
        /* Setup Menubar */
        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuHelp = new Menu("Help");
        menuBar.setPrefWidth(10000);
        menuBar.getMenus().addAll(menuFile, menuEdit, menuHelp);
        
        /* Set layout colours */
        masterGrid.setStyle("-fx-background-color: white;");
        bottemBar.setStyle("-fx-background-color: blue;");
        topBar.setStyle("-fx-background-color: green;");
        centerPanel.setStyle("-fx-background-color: black;");
        contentPanel.setStyle("-fx-background-color: pink;");
        propertiesPanel.setStyle("-fx-background-color: yellow;");
        
        /* DUMMY CONTENT */
        
        /* Text */
        text = new Text(1, 1,"topBar");
        text.setFont(Font.font("Verdana", 20));
        text.setId("text");
        
        botText = new Text(1, 1,"bottemBar");
        botText.setFont(Font.font("Verdana", 20));
        botText.setId("botText");
        
        propText = new Text(1, 1,"propText");
        propText.setFont(Font.font("Verdana", 20));
        propText.setId("botText");
        
        /* Page */
        
        Rectangle r = new Rectangle();
        r.setWidth(800);
        r.setHeight(500);
        r.setFill(Color.WHITE);
        r.setEffect(new DropShadow());
        
        /* Add content to panes */
        masterGroup.getChildren().addAll(masterGrid);
        masterGrid.add(menuBar, 0, 0);
        masterGrid.add(innerGrid, 0, 1);
        masterGrid.add(bottemBar, 0, 2);
       
        
        
        /* Content of innerGrid */
        innerGrid.add(topBar, 0, 0);
        innerGrid.add(centerPanel, 0, 1);
        
        /* Content of topBar */
        topBar.getChildren().addAll(text);
         
        /* Content of centerPanel */ 
        centerPanel.getChildren().addAll(contentPanel,propertiesPanel);
        centerPanel.setAlignment(Pos.CENTER);
        
        
        
        /* Content of propertiesPanel */
        propertiesPanel.getChildren().addAll(propText);
        
        /* Content of contentPane */
        contentPanel.getChildren().addAll(r);
         
        /* Content of bottemBar */
        bottemBar.getChildren().addAll(botText);
        
        /* Setup the window */
        primaryStage.setTitle("TE_GUI");
        primaryStage.setScene(scene);
        
        /* Show the window */
		primaryStage.show();
		
    }
	public static void main(String[] args) {
		launch(args);
	}
	
}