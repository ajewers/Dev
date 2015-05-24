/*
 *
 * Copyright (c) 2015 Sofia Software Solutions. All Rights Reserved.
 */
package teacheasy.mediahandler;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import teacheasy.mediahandler.image.*;

/**This class inserts an image with specified image name, size, location(x and y) and rotation degrees.
 * 
 * @Authors: Daniel Berhe & Jake Ransom
 * @Verion:  1.1 27/04/2015
 * 
 * */
public class ImageHandler {

	/* Reference to the group on which to add the image */
	private Group group;
	
	/* Array list of the images on screen */
	private List<ImageObject> images;

	public ImageHandler(Group nGroup){
		/* Set the group reference */
		this.group = nGroup;
		
		/* Initialise the image list */
		this.images = new ArrayList<ImageObject>();
	}

	/**
	 * Adds an image to the group associated with the handler
	 * 
	 * @param imageName
	 *            : name of the image as saved on disk
	 * @param locationX
	 *            : desired x location of the image
	 * @param locationY
	 *            : desired y location of the image
	 * @param widthSize
	 *            : actual image with multiplier. Eg, size 2 is twice as big as
	 *            the original image
	 * @param heightSize: actual image height multiplier
	 * @param rotationDegree
	 *            : to rotate the image in clockwise direction
	 * @param group
	 *            : group layout
	 */
	public void createImage(String imageName, double locationX,	double locationY, double widthSize, double heightSize, double rotationDegree) {
		images.add(new ImageObject(group, imageName, locationX, locationY, widthSize, heightSize, rotationDegree));
	}
	
	/**
     * Returns the width of the selected image object
     * 
     * @return Width of the image
     */
    public double getWidth(int imageId) {
    	double width = 0;
    	if(imageId < images.size() && imageId >= 0) {
    		width = images.get(imageId).getImageWidth();
    	}
    	return width;
    }
    
    /**
     * Returns the height of the selected image object
     * 
     * @return Height of the image
     */
    public double getHeight(int imageId) {
    	double height = 0;
    	if(imageId < images.size() && imageId >= 0) {
    		height = images.get(imageId).getImageHeight();
    	}
    	return height;
    }
	
}
