import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.Line;



/** Paint is where all the bread and butter is in the program. It's busy and methods that work closely with Principles of Design. To start, there are two types of 
 * attributes for this class, static and dynamic ones. The static attributes are consistent through a whole artwork piece production while the dynamic ones change
 * with each element added to the piece. An element is a conglomeration of color, texture(width of the line), lines, and shapes. These are derived from the elements
 * of art which is common in art theory. After a conglomeration, a new one made be made which is done so through making space(starting a new element elsewhere on
 * the canvas). The dynamic attributes hold the attributes of each element until space is made, and then they are reset, and the old ones are saved to a data 
 * structure. paint, the main function of class, runs through and adds parts and therefore elements to the canvas until it has hit a set number of components. 
 * 
 * @author noahgans
 *
 */
public class Paint {

	
	//Static Attributes
	Random rand = new Random();//A random generator for the class
	Turtle draw;//The turtle that will be drawing labeled draw
	TransitionFuntion trans;//Transition function which represents the chances of moving from one action to another (Markov Chain)
	PrinciplesOfDesign prin;//This class contains rules for the different states which make up elements of the art work.  

	//Dynamic attributes. These change as new elements are added to a piece 
	double drawLocationX;//this is the x start point of the most recent element
	double drawLocationY;//y component 
	Color elementBaseColor = null;//color of the element
	int elementTexture = -1;//texture of the 
	double elementLength;// avrage size of line
	double elementComplexity;// number of features(lines and shapes)
	int quad;

	
	/**
	 * The constructor is pretty basic. It assigns the static attributes, creates a new transition function, and new PrinciplesOfDesign class. 
	 * @param cat
	 */
	public Paint(Turtle turtle) {
		this.draw = turtle;//set turtle
		this.trans = new TransitionFuntion();//new class
		this.prin = new PrinciplesOfDesign();//new class
		
	}
	/**Paint is the workhorse of this class and of the program in total. Two things are going to be defined here to understand how it functions: Elements and 
	 * Components. Components are the substructure of an element. There are five different types of components: color, texture(width of the line), lines, shape, and
	 * space. These are derived from art theory from the elements of art. There are more elements of art, but the others were far more difficult to implement. Paint
	 * loops through 700 integrations of components. After the component of space is reached, a new element is made, so an element is a conglomeration of color,
	 * texture, lines, and shapes, and is separated from other elements by space(). These components have a natural higherachy to them which is represented in 
	 * the transition function of color, texture, line, shape, and space from lowest to highest respectively. There cannot be a line without its color or texture
	 * (width) being defined, there cannot be a shape without line formed, and there cannot be space without shape or line created. 
	 * 
	 *  Paint starts with selecting a start location to create the first element, and does not choose to put it in the coordinate quad that has been designated
	 *  for the element that creates Emphasis. This is describes in PrinciplesOfDesign. After a starting location is selected and the turtle has been moved to that
	 *  starting location, then the initial state is set to 0 (color's state number), and the color function is called which selects a color for the new element. 
	 *  After this, the function iterates 700 times(selected because it usually led to the canvas getting somewhat filled) through components and everytime the 
	 *  space state is reached it makes a new element somewhere else on the canvas. The probability of going to another component(state) is only a factor of
	 *  the state that it is currently in. Look at transition function for more info on this process. After the 700 iterations are done, it calls for the final 
	 *  element to be made. 
	 * 
	 */
	public void paint() {
		
		
		//This loop ensures that the fist starting point is not in the quad that has been designated to the emphasis element. 
		while (true) {//Continue to loop
			this.drawLocationX = selectStartx();//generate a starting x and y
			this.drawLocationY = selectStarty();//and set to the dynamic attributes
			if (checkQuad(this.drawLocationX, this.drawLocationY) != this.prin.getEmphasis()) {//if it is not in the quad designated for the emphasis element
				break;//break out of looping
			}
		}

		this.draw.up();//pick up the pen
		this.draw.setPosition(this.drawLocationX, this.drawLocationY);//go to starting point
		this.draw.down();//put down pen
		int currState = 0;//set current state to 0(color)
		color(false);//choose a color the boolean is only true when needing to generate he emphasis element. 

		
		//this block of code controls the number of structural  components(lines and shapes) so that if the unity value is 3, all elements will be of a random size
		// between 1-10. This creates unity in size of the different elements. 
		int elementSize = 0;//this value holds the desired size of elements if unity is across size. 
		if (this.prin.getUnity() == 3) {//if unity is for size Unity == 3
			elementSize = (int) (Math.random() * 10);//select random size for the elements. 
		}
		int elementSizeCount = 0;//this will hold a running count of how big an element is(only used if unity is across size of elements)
		
		//Iterate through different components to build elements and fill the canvas
		for (int i = 0; i < 700; i++) {

			int nextState = trans.getNextState(currState);//get the next state given the current state
			currState = nextState;//resent current state

			if (elementSize < elementSizeCount && this.prin.getUnity() == 3) {//if unity is for size and the size of the current element is larger than the set
																				// size, make a new element by calling space. 
				space();//make a new element
				elementSizeCount = 0;//reset the element size counter

			}

			//given the next state, excute the action associated with entering that state. 
			switch (currState) {
			case 0:
				color(false);//False because not the Emphasis element
				break;
			case 1:
				texture(false);//False because not the Emphasis element
				;
				break;
			case 2:
				line(false);//False because not the Emphasis element
				elementSizeCount++;//count increase because this adds a line to the canvas
				break;
			case 3:
				shape();
				elementSizeCount++;//count increase because this adds a line to the canvas
				break;
			case 4:
				if (this.prin.getUnity() != 3) {//typically if unity is not based on size, than if state 4 is entered than a new element is created, but othewise
								//the conditional above creates a new element. 
					space();
				}
				break;

			}
		}
		
		makeEmphisisElement();//Now that all regular elements have been created, the Emphasis element is made.
	}
		
	
		/**
		 * This method makes and element that is different than the component that creates unity among all the other elements. This is done by passing a True into
		 * all the component methods. Otherwise it functions very similar to paint
		 * 
		 * 
		 */
		private void makeEmphisisElement() {
			// TODO Auto-generated method stub

		int currStateEmphisis = 0;//set starting element color
		resetElement();//reset the values from the previous element 

		
		//now instead of finding a coordinate not in the Emphasis quadrant, this code finds a coordinate that is in the emphasis quadrent
		while (true) {
			this.drawLocationX = selectStartx();//generate x and y
			this.drawLocationY = selectStarty();

			if (checkQuad(this.drawLocationX, this.drawLocationY) == this.prin.getEmphasis()) {//if in the selected quadrent
				break;//continue
			}
		}
		this.draw.up();
		this.draw.setPosition(this.drawLocationX, this.drawLocationY);//set the pen to the seleceted starting position
		this.draw.down();
		color(true);//get starting color not limited by unity becase of the true passed in

		for (int i = 0; i < 100; i++) {//iterate through 100 components

			int nextState = trans.getNextState(currStateEmphisis);//get the next state
			currStateEmphisis = nextState;//reset current state

			switch (currStateEmphisis) {//get next componet method
			case 0:
				color(true);//use true to override unity component
				break;
			case 1:
				texture(true);//use true to override unity component
				;
				break;
			case 2:
				line(true);//use true to override unity component
				break;
			case 3:
				shape();//use true to override unity component
				break;
			case 4://space is not used because the final element is emphasized more if it is larger. 
				break;
			}
		}
		

	}

	/**Generate x value within screen width
	 * 
	 * @return
	 */
	private int selectStartx() {
		return ((int) (Math.random() * (1200 - 0 + 1))) - 600;

	}

	/**
	 * generate y value witin screen height
	 * @return
	 */
	private int selectStarty() {
		// TODO Auto-generated method stub
		return ((int) (Math.random() * (700 - 0 + 1))) - 350;
	}

	/** This method sets the pen color of the turtle. For most of these component methods they have two pathways. Either it generates a new possibility, in this case
	 * a color, or it generates on that aligns with the rest of the element. Here color() gets a new color for an element if there is no previous lines or shapes
	 * drawn. After a structure is drawn, than it will generate a color based on the previous colors of its structure. 
	 * The parameter signals that it is the unique element and does not need to follow rules of unity
	 * @param emphasis
	 */
	private void color(boolean emphasis) {
		if (this.elementBaseColor == null) {//if no color for this element, than generate a new one. 
			this.elementBaseColor = prin.getNewElementColor(emphasis);//use principles of desin to get new color
			this.draw.penColor(this.elementBaseColor);//set pen color to new color
		} else {//if there is already drawings from this element
			this.draw.penColor(prin.getNextColor(this.elementBaseColor));//figure out the next color
		}
	}

	/**
	 * Line functions like color. If there are no other lines or shapes, generate a new line, and otherwise generate the next line length based on the previous
	 * line lengths in its element.
	 * 
	 * 
	 * The parameter signals that it is the unique element and does not need to follow rules of unity
	 * @param emphasis
	 */
	private void line(boolean emphasis) {
		double length;//length of line
		double direction = (Math.random() * 360);//generate a random direction

		if (this.elementComplexity == 0) {//if no other lines or shapes
			length = this.prin.getElementLength(emphasis);//get a new length for this element
		} else {//if there is already development on this element
			length = this.prin.getNextLength(this.elementLength / this.elementComplexity);//get the next length based on the average length of the lines of the 
																								//current element

		}
		this.draw.setDirection(direction);//set the direction
		this.draw.forward(length + 10);//draw length. The +10 is there so that length 0 is not drawn.
		this.elementComplexity ++;//add 1 to element complexity
		this.elementLength += length;//add the length to the total for the element 

	}

	
	/**Shape is very different from the other component methods because it is not influenced from the unity metric. Shape basically creates a line from the 
	 * current place the pen is back to the where the first lines were made(origin of the shape) thus completing the shape. The pen location is the set to the point
	 * that connects to the origin, but not the origin. This is done so that elements are not just centered around one point, but can move an construct on themselves.
	 * 
	 */
	private void shape() {
		double tempx = this.draw.getX();//get current pen x location and set to temp
		double tempy = this.draw.getY();//get current pen y location and set to temp
		double distance = this.draw.distance(this.drawLocationX, this.drawLocationY);//find distance between current pen location and element origin
		this.draw.setPosition(this.drawLocationX, this.drawLocationY);//set position to origin, therefore completing the shape
		this.drawLocationX = tempx;//resent to temps
		this.drawLocationY = tempy;//reset to temps
		this.draw.up();//pick pen up
		this.draw.setPosition(this.drawLocationX, this.drawLocationY);//return pen to location just before element origin
		this.draw.down();//put pen down
		this.elementComplexity += 1;//add to complexity
		this.elementLength += distance;//add distance to total

	}
	/**
	 * Again texture works very similarly to color and length. If it's new texture for an element, getElementTexture to get a new texture for the element, otherwise,
	 * generate a texture depending on the previous element's textures.
	 * @param emphasis
	 */
	private void texture(boolean emphasis) {
		double width;//width of line
		if (this.elementTexture < 0) {//if this is the first texture of an element
			this.elementTexture = (int) this.prin.getElementTexture(emphasis);//get new texture
		} else {//otherwise generate texture based on previous texture
			this.elementTexture = (int) this.prin.getNextElementTexture(this.elementTexture);
		}
		
		this.draw.width(this.elementTexture);//set pen width

	}

	
	/**Space is distinguishes one element from another by creating space. This is done by selecting a new place to create an element that is not in the quad
	 * designated for the emphasis element. Space also adds the previous element to a data structure stored in the PrinciplesOfDesign class. Space then resets
	 * all the dynamic attributes for this class and finishes with reseting the location to a random spot. 
	 * 
	 */
	private void space() {

		// make and add previous element to the list
		Element element = new Element(this.drawLocationX, this.drawLocationY, this.elementBaseColor,
				this.elementLength, this.elementComplexity, this.elementTexture);
		this.prin.addElement(element);

		resetElement();//resent the attributes for the element
		
		//finds new x,y coordinates in an appropriate quad
		while (true) {
			this.drawLocationX = selectStartx();
			this.drawLocationY = selectStarty();

			if (checkQuad(this.drawLocationX, this.drawLocationY) != this.prin.getEmphasis()) {
				break;
			}
		}
		
		//move pen to the new location
		this.draw.up();
		this.draw.setPosition(this.drawLocationX, this.drawLocationY);
		this.draw.down();

	}

	/**Check quad returns the quad a set of coordinates are in. Quads are labled as they are in math
	 * 
	 * @param drawLocationX2
	 * @param drawLocationY2
	 * @return int quad
	 */
	private int checkQuad(double drawLocationX2, double drawLocationY2) {
		// TODO Auto-generated method stub
		if (drawLocationX2 > 0 && drawLocationY2 > 0) {
			return 1;
		}
		if (drawLocationX2 < 0 && drawLocationY2 > 0) {
			return 2;
		}
		if (drawLocationX2 < 0 && drawLocationY2 < 0) {
			return 3;
		}
		if (drawLocationX2 > 0 && drawLocationY2 < 0) {
			return 4;
		}
		return -1;

	}

	/**
	 * This Method resets all the dynamic elements of this class.
	 */
	private void resetElement() {
		// TODO Auto-generated method stub
		this.elementBaseColor = null;
		this.elementTexture = -1;
		this.elementComplexity = 0;
		this.elementLength = 0;

	}

}
