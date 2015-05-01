/*
 * Copyright (C) 2015 Sofia Software Solutions
 */
package teacheasy.mediahandler;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import teacheasy.mediahandler.answerbox.AnswerBox;

/**
 * Class to draw answer boxes with the specified x & y location
 * 
 * @author Daniel Berhe and Jake Ransom
 * @version 1.0
 * 
 */
public class AnswerBoxHandler {

	private List<AnswerBox> answerBox;
	private Group group;

	public AnswerBoxHandler(Group nGroup) {
		this.answerBox = new ArrayList<AnswerBox>();
		this.group = nGroup;
	}

	/** Creates a new answer box */
	public void createAnswerBox(double xStart, double yStart,
			int characterLimit, boolean retry, String correctAnswers, int marks, boolean isNumerical) {
		answerBox.add(new AnswerBox(xStart, yStart, characterLimit, retry,
				correctAnswers,marks,isNumerical, group));
	}
	
	/** Checks to see if all questions on the current page have been attempted */
	public boolean AllAnswerBoxQuestionsAttempted() {
		for (int i=0; i < answerBox.size(); i++) {
			if (!answerBox.get(i).buttonPressed) {
				return false;
			}
		}
		return true;
	}
	
	/**Greys out all of the answer boxes on a page */
	public void DisableAllAnswerBoxes() {
		for (int i=0; i < answerBox.size(); i++) {
			answerBox.get(i).answerField.setDisable(true);
		}
	}

}
