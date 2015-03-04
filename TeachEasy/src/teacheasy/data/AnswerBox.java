package teacheasy.data;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

public class AnswerBox {

	public TextField answerField;
	int marks;
	int awardedMarks;
	boolean retry;
	String correctAnswers;
	boolean answerIsCorrect;
	int characterLimit;
	double xStart;
	double yStart;
	Group group;

	private HBox box;
	private Button checkAnswerButton;

	public AnswerBox(double nXStart, double nYStart, int nCharacterLimit,
			boolean nRetry, String nCorrectAnswers, int nMarks, Group nGroup) {

		answerField = new TextField();
		this.marks = nMarks;
		this.correctAnswers = nCorrectAnswers;
		this.characterLimit = nCharacterLimit;
		this.xStart = nXStart;
		this.yStart = nYStart;
		this.group = nGroup;
		this.retry = nRetry;

		checkAnswerButton = new Button("Check answer");
		checkAnswerButton.setId("check answer");
		checkAnswerButton.setOnAction(new ButtonEventHandler());

		box = new HBox();

		createAnswerBox(xStart, yStart, characterLimit, nRetry);
		answerField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent key) {
				if (key.getCode().equals(KeyCode.ENTER)
						&& answerField.isEditable() == true) {
					answerIsCorrect = checkAnswer(marks);
					System.out.println("Answered " + answerIsCorrect);
					System.out.println("Awarded marks " + awardedMarks);
				}
			}
		});

		box.getChildren().addAll(answerField ,checkAnswerButton);
		group.getChildren().addAll(box);
	}

	/* Method to create an answer box */
	private void createAnswerBox(double nXStart, double nYStart,
			int nCharacterLimit, boolean nRetry) {

		answerField.relocate(nXStart, nYStart);
		answerField.addEventHandler(KeyEvent.KEY_TYPED,
				maxLength(nCharacterLimit));
		this.retry = nRetry;
	}

	/**
	 * This method limits the number of characters that can be typed in the
	 * textField to the the specified nCharacterLimit
	 * 
	 */
	public EventHandler<KeyEvent> maxLength(final Integer characterLimit) {
		return new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				TextField textField = (TextField) event.getSource();
				if (textField.getText().length() >= characterLimit) {
					event.consume();
				}
			}
		};
	}

	/**
	 * This method stores the potential answers in an array. When an answer is
	 * provided the loop compares the entered answer against the possible
	 * answers in the array. It also allows/disallows retry depending on the
	 * value of the variable retry.
	 * 
	 */
	public boolean checkAnswer(int nMarks) {
		boolean isCorrect = false;
		String[] listOfCorrectAnswers = correctAnswers.split("~");
		for (int i = 0; i < listOfCorrectAnswers.length; i++) {

			if (answerField.getText().equals(listOfCorrectAnswers[i])) {
				awardedMarks += nMarks;
				isCorrect = true;
				retry = false;
				break;
			}
		}
		answerField.setEditable(retry);
		return isCorrect;
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
			if (id.equals("check answer")) {
				answerIsCorrect = checkAnswer(marks);
				System.out.println("Answered " + answerIsCorrect);
				System.out.println("Awarded marks " + awardedMarks);
				System.out.println("Checked answer using the button ");
			}
		}
	}

}
