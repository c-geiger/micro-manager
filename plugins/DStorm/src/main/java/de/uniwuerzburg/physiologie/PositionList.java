package de.uniwuerzburg.physiologie;

import org.micromanager.Studio;

public class PositionList {
String [] directionArray;
public void setDirectionArray(String[] directionArray) {
	this.directionArray = directionArray;
}



public void setFrameArray(int[] frameArray) {
	this.frameArray = frameArray;
}



public void setPositionArray(String[] positionArray) {
	this.positionArray = positionArray;
}



int []frameArray;
String[] positionArray;



public PositionList(String [] directionArray , int [] frameArray , String [] positionArray) {
	this.directionArray = directionArray;
	this.frameArray = frameArray;
	this.positionArray=positionArray;
}
}