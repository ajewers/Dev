/*
 * Sam Raeburn
 * 
 * Copyright (c) 2015 Sofia Software Solutions. All Rights Reserved.
 */
package teacheasy.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import teacheasy.data.lessondata.*;
import wavemedia.graphic.GraphicType;
import teacheasy.data.*;
import teacheasy.data.MultipleChoiceObject.MultiChoiceType;
import teacheasy.xml.*;

import teacheasy.xml.util.XMLNotification;

/**
 * This class tests the XML Parser and Writer using
 * a JUnit automated test system.
 * 
 * @author  Sam Raeburn
 * @version 1.0 01 Mar 2015
 *
 */
public class Iteration1XMLTest {
	private static XMLHandler handler;
	private static XMLParser parser;
	private static XMLWriter writer;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		parser = new XMLParser();
		writer = new XMLWriter();
	}
	
	//The following tests are for the parser

	/** Parsing file that doesn't exist returns error */
	@Test
	public void checkParserWithNonexistentXML() {
		//Parse the XML file
		ArrayList<XMLNotification> errorList = parser.parse("iDontExist.xml");
		System.out.println("non existent file test:" + errorList);
		assertTrue(errorList.size() != 0);
	}
	
	/** 
	 * Check that no error is returned if there is no audio,  
	 * video or image data in a parsed XML file.
	 */
	@Test
	public void checkParserWithMissingDataXML() {
		ArrayList<XMLNotification> errorList;
		errorList = parser.parse("testXML/missingImageAudioVideoData.xml");
		System.out.println("missing media test:" + errorList);
		assertTrue(errorList.size() == 0);
	}
	
	/**
	 * Check that the parser doesn't crash if an XML file
	 * has image with no file or location.
	 */
	@Test
	public void checkParserWithNoImageLocation() {
		ArrayList<XMLNotification> errorList;
		errorList = parser.parse("testXML/missingImageLocation.xml");
		System.out.println("image location test:" + errorList);
		//Check that there IS an error in the list
		assertTrue(errorList.size() != 0);
	}
	
	/** Check how the parser handles missing default settings*/
	@Test
	public void checkParserDefaults() {
		ArrayList<XMLNotification> errorList;
		errorList = parser.parse("testXML/missingDefaults.xml");
		System.out.println("missing defaults test:" + errorList);
		assertTrue(errorList.size() == 0);
	}
	
	/** Check how parser handles missing document info section */
	@Test
	public void checkMissingDocumentInfo() {
		ArrayList<XMLNotification> errorList;
		errorList = parser.parse("testXML/missingDocumentInfo.xml");
		System.out.println("missing document info test:" + errorList);
		assertTrue(errorList.size() != 0);
	}
	
	/** Check how parser handles an xml file which does not define locations */
	@Test
	public void checkNoLocations() {
		ArrayList<XMLNotification> errorList;
		errorList = parser.parse("testXML/noLocations.xml");
		System.out.println("no locations test:" + errorList);
		assertTrue(errorList.size() != 0);
	}
	
	/** Check pass boundary */
	@Test
	public void checkPassBoundary() {
		ArrayList<XMLNotification> errorList;
		errorList = parser.parse("testXML/passBoundaryTest.xml");
		System.out.println("Pass boundary test:" + errorList);
		assertTrue(errorList.size() != 0);
	}
	
	/** Check if error if a colour is given in wrong format (not hex) */
	@Test
	public void checkColourFormatting() {
		ArrayList<XMLNotification> errorList;
		errorList = parser.parse("testXML/colourFormatting.xml");
		System.out.println("Colour formatting test: " + errorList);
		assertTrue(errorList.size() != 0);
	}
	
	/** Check if locations can be greater than 1 or negative */
	@Test
	public void checkLocationToobig() {
		ArrayList<XMLNotification> errorList;
		errorList = parser.parse("testXML/incorrectLocation.xml");
		System.out.println("Location greater than 1 and negative test:" + errorList);
		assertTrue(errorList.size() != 0);
	}
	
	/** Check if for graphic x/yend can be smaller than x/ystart */
	@Test
	public void checkGraphicLocation() {
		ArrayList<XMLNotification> errorList;
		errorList = parser.parse("testXML/graphicsLocation.xml");
		System.out.println("Check graphics locations:" + errorList);
		assertTrue(errorList.size() != 0);
	}
	
	/** Check data is correctly stored once it has been parsed */
	@Test
	public void checkDataIsCorrectlyStored(){
		parser.parse("/testXML/testXML.xml");
		Lesson lesson = parser.getLesson();
		Page page = lesson.pages.get(0);
		for (int i = 0; i < page.pageObjects.size(); i++) {
			PageObject pageObject = page.pageObjects.get(i);
			switch (pageObject.getType()) {
			case TEXT:
				TextObject text = (TextObject)pageObject;
				//Check positions
				assertTrue(text.getXStart() == 0.6f);
				assertTrue(text.getYStart() == 0.2f);
				//Check font 
				assertTrue(text.getFont().equals("arial"));
				break;
			case IMAGE:
				ImageObject image = (ImageObject)pageObject;
				//Check positions
				assertTrue(image.getXStart() == 0.5f);
				assertTrue(image.getYStart() == 0.5f);
				//Check source file
				assertTrue(image.getSourcefile().equals("C:\\Users\\sadjlsd\\imagefile.jpg"));
				//Check rotation
				assertTrue(image.getRotation() == 0.0f);
				break;
			case AUDIO:
				AudioObject audio = (AudioObject)pageObject;
				//Check positions
				assertTrue(audio.getXStart() == 0.5f);
				assertTrue(audio.getYStart() == 0.1f);
				//Check source file
				assertTrue(audio.getSourcefile().equals("gavel.wav"));
				break;
			case VIDEO:
				VideoObject video = (VideoObject)pageObject;
				//Check positions
				assertTrue(video.getXStart() == 0.1f);
				assertTrue(video.getYStart() == 0.1f);
				//Check source file and screenshot
				assertTrue(video.getSourcefile().equals("avengers.mkv"));
				break;
			case GRAPHIC:
				GraphicObject graphics = (GraphicObject)pageObject;
				//Check positions
				assertTrue(graphics.getXStart() == 0.5f);
				assertTrue(graphics.getXEnd() == 0.7f);
				assertTrue(graphics.getYStart() == 0.5f);
				assertTrue(graphics.getYEnd() == 0.7f);
				//Check other parameters
				assertTrue(graphics.getGraphicType() == GraphicType.OVAL);
				assertTrue(graphics.getOutlineThickness() == 0.5f);
				assertTrue(graphics.getGraphicColour().equals("#11223344"));
				break;
			case ANSWER_BOX:
				AnswerBoxObject answerbox = (AnswerBoxObject)pageObject;
				//Check positions
				assertTrue(answerbox.getXStart() == 0.1f);
				assertTrue(answerbox.getYStart() == 0.1f);
				assertTrue(answerbox.getCorrectAnswers().equals("springs~spring"));
				assertTrue(answerbox.isRetry() == false);
				assertTrue(answerbox.getMarks() == 5);
				break;
			case MULTIPLE_CHOICE:
				MultipleChoiceObject multipleChoice = (MultipleChoiceObject)pageObject;
				//Check positions
				assertTrue(multipleChoice.getXStart() == 0.2f);
				assertTrue(multipleChoice.getYStart() == 0.2f);
				assertTrue(multipleChoice.getMultiChoiceType() == MultiChoiceType.CHECKBOX);
				/* THIS CODE IS NO LONGER VALID */
				/*assertTrue(multipleChoice.incorrectAnswers.get(0).equals("Fish"));
				assertTrue(multipleChoice.correctAnswers.get(0).equals("Bear"));
				assertTrue(multipleChoice.correctAnswers.get(1).equals("Zebra"));
				assertTrue(multipleChoice.incorrectAnswers.get(1).equals("Komodo Dragon"));*/
				break;				
			default:
				break;
			}
		}
	}
	
	/** Check to see if defaults are stored correctly */
	@Test
	public void checkDefaultStoredCorrectly() {
		ArrayList<XMLNotification> errorList = parser.parse("testXML/testingDefaults.xml");
		Lesson lesson = parser.getLesson();
		Page page = lesson.pages.get(0);
		for (int i = 0; i < page.pageObjects.size(); i++) {
			PageObject pageObject = page.pageObjects.get(i);
			
			assertTrue(page.getPageColour().equals("#ff00ff00"));
			
			switch (pageObject.getType()) {
			case TEXT:
				TextObject text = (TextObject)pageObject;
				//Check defaults have been correctly recognised
				assertTrue(text.getFont().equals("arial"));
				assertTrue(text.getFontSize() == 24);
				assertTrue(text.getColor().equals("#ffffffff"));
				
			default:
				break;
			}
		}
	}
	
	/** Check to see that the writer creates a new XML document */
	@Test
	public void checkWriterCreatesXML() {
		parser.parse("testXML/testXML.xml");
		Lesson lesson = parser.getLesson();
		writer.writeXML(lesson, "testXML/writerTestXML.xml");
		File file = new File("testXML/writerTestXML.xml");
		assertTrue(file.exists());
		file.delete();
		assertFalse(file.exists());
	}
	
	/**
	 * Check to see that the XML file created by the writer contains
	 * the correct data, since it has already been shown that the parser
	 * behaves as wished this can simply be done using the parser
	 */
	@Test
	public void checkWriterXMLIsCorrect() {
		parser.parse("testXML/testXML.xml");
		Lesson lesson = parser.getLesson();
		writer.writeXML(lesson, "testXML/writerTestCorrectXML.xml");
		File file = new File("testXML/writerTestXML.xml");
		ArrayList<XMLNotification> errorList = parser.parse("testXML/writerTestCorrectXML.xml");
		file.delete();
		assertTrue(errorList.size() == 0);
	}
}

