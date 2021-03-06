/*
 * Alistair Jewers
 * 
 * Copyright (c) 2015 Sofia Software Solutions. All Rights Reserved.
 * 
 */
package teacheasy.data;

import teacheasy.xml.XMLElement;

/**
 * This class encapsulates a single object 
 * on a page.
 * 
 * @version 	1.0 05 Feb 2015
 * @author 		Alistair Jewers
 */
public abstract class PageObject {
	
	/** Enumeration of the various page object types */
	public static enum PageObjectType {
		TEXT,
		IMAGE,
		GRAPHIC,
		AUDIO,
		VIDEO,
		ANSWER_BOX,
		MULTIPLE_CHOICE,
	}
	
	/** The type of this page object */
	private PageObjectType type;
	
	/** Relative X and Y position, measured from top left */
	private float xStart;
	private float yStart;
	
	/**
	 * Constructor Method
	 * 
	 * @param nType - The type of this page object
	 * @param nXStart - Relative X position
	 * @param nYStart - Relative Y position
	 */
	public PageObject(PageObjectType nType, float nXStart, float nYStart) {
		this.type = nType;
		this.xStart = nXStart;
		this.yStart = nYStart;
	}
	
	/** Get the xstart position */
	public float getXStart() {
	    return xStart;
	}
	
	/** Get the ystart position */
	public float getYStart() {
	    return yStart;
	}
	
	/** Set the xstart position */
    public void setXStart(float nXStart) {
        this.xStart = nXStart;
    }
    
    /** Set the ystart position */
    public void setYStart(float nYStart) {
        this.yStart = nYStart;
    }
	
	/** Get the type of this page object */
	public PageObjectType getType() {
	    return type;
	}
	
	/** Prints information about the object to the console */
	public void debugPrint() {
	    System.out.print(type + ": \n" + "xStart " + xStart + ", yStart " + yStart);
	}
}
