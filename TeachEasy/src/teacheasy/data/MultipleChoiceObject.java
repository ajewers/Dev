/*
 * Alistair Jewers
 * 
 * Copyright (c) 2015 Sofia Software Solutions. All Rights Reserved.
 * 
 */
package teacheasy.data;

import java.util.ArrayList;
import java.util.List;

import teacheasy.data.multichoice.Answer;

/**
 * This class encapsulates the data for
 * a multiple choice object.
 * 
 * @version     1.0 13 Feb 2015
 * @author      Alistair Jewers
 */
public class MultipleChoiceObject extends PageObject {
    /** Enumeration of the possible orientations */
    public static enum Orientation {
        VERTICAL,
        HORIZONTAL;
        
        public static Orientation check(String str) {
            try {
                return valueOf(str);
            } catch (Exception ex) {
                return VERTICAL;
            }
        }
    }
    
    /** Enumeration of the possible multiple choice types */
    public static enum MultiChoiceType {
        CHECKBOX,
        RADIO,
        DROPDOWNLIST;
        
        public static MultiChoiceType check(String str) {
            try {
                return valueOf(str);
            } catch (Exception ex) {
                return CHECKBOX;
            }
        }
    }
    
    private ArrayList<Answer> answers;
    
    private Orientation orientation;
    private MultiChoiceType multiChoiceType;
    private int marks;
    private boolean retry;
    
    /** Constructor method */
    public MultipleChoiceObject(float nXStart, float nYStart, 
                                Orientation nOrientation,
                                MultiChoiceType nType,
                                int nMarks, boolean nRetry) {
        
        /* Call super constructor */
        super(PageObjectType.MULTIPLE_CHOICE, nXStart, nYStart);
        
        /* Instantiate answer lists */
        answers = new ArrayList<Answer>();
        
        /* Instantiate class level variables */
        this.orientation = nOrientation;
        this.multiChoiceType = nType;
        this.marks = nMarks;
        this.retry = nRetry;
    }

    /* Get and set methods */
    
    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation nOrientation) {
        this.orientation = nOrientation;
    }

    public MultiChoiceType getMultiChoiceType() {
        return multiChoiceType;
    }

    public void setType(MultiChoiceType nType) {
        this.multiChoiceType = nType;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int nMarks) {
        this.marks = nMarks;
    }

    public boolean isRetry() {
        return retry;
    }

    public void setRetry(boolean nRetry) {
        this.retry = nRetry;
    }
    
    public ArrayList<Answer> getAnswers() {
        return answers;
    }
    
    public void clearAnswers() {
        answers.clear();
    }
    
    public void addAnswer(Answer answer) {
        answers.add(answer);
    }
    
    public void removeAnswer(Answer answer) {
        if(answers.contains(answer)) {
            answers.remove(answer);
        }
    }
    
    /** Prints information about the object to the console */
    public void debugPrint() {
        super.debugPrint();
        
        System.out.println(", Orientation " + orientation + 
                           ", Type " + multiChoiceType + 
                           ", Marks " + marks + 
                           ", Retry " + retry + ".");
        
        for(int i = 0; i < answers.size(); i++) {
            System.out.println("\tAnswer: " + answers.get(i).getText() + ". Correct: " + answers.get(i).isCorrect());
        }
        
        System.out.println("");
    }
}
