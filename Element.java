import java.awt.Color;


/**
 * This class holds all information for an element, and has a few getter methods for certain attributes. 
 * @author noahgans
 *
 */
public class Element {
	
	double elementLocationX;//elements x location
	double elementLocationY;//elements y location
	Color elementBaseColor;//elements base color
	int elementTexture;//elements pen width
	double elementLength;//avrage size of feature
	double elementComplexity;//number of features
	


	public Element(double elementLocationX, double elementLocationY, Color elementBaseColor2, double elementLength2, double elementComplexity2, int elementTexture) {
		this.elementLocationX = elementLocationX;
		this.elementLocationY = elementLocationY;
		this.elementBaseColor = elementBaseColor2;
		this.elementLength = elementLength2 / elementComplexity2;// takes total length and divides by number of features. 
		this.elementComplexity = elementComplexity2;
		this.elementTexture = elementTexture;
		
		
		// TODO Auto-generated constructor stub
	
	}
	
	/**
	 * Getter for elementComplexity
	 * @return
	 */
	public double getElementComplexity() {
		return this.elementComplexity;
	}
	
	/**
	 * Getter for elementBaseColor
	 * @return
	 */
	public Color getElementBaseColor() {
		return this.elementBaseColor;
	}

	/**
	 * Getter for length
	 * @return
	 */
	public double getlength() {
		return elementLength;
		
	}

	/**
	 * Getter for lengthWidth
	 * @return
	 */
	public double getwidth() {
		
		return this.elementTexture;
		// TODO Auto-generated method stub
		
	}
	 

}


