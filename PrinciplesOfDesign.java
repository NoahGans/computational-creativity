import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;


/**This class is the second most important class and controls how different elements look through controlling the components of them. The pricipalsOfDesign is another
 * set of rules in art theory. There are more then the ones incorporated here. There are 3 attributes and principals of this artwork: Unity, Variety, and Emphasis. 
 * Unity determines which attribute will be similar among all elements 1=color, 2=length of lines 3=number of structures(lines/shapes) 4=texture. Variety changes 
 * for each element and determines which attribute will be randomly generated for each component of an element. This can be overridden by Unity, because Unity
 * is essential for all elements. Emphasis determines which quadrant the final element will go in. The Emphasized element is random across all qualities besides
 * number of compounds which has been set to 100. 
 * 
 * @author noahgans
 *
 */
public class PrinciplesOfDesign {

	Random rand = new Random();//random for class
	int Unity = rand.nextInt(4) + 1;// one attribute to be consistent
	int variety = rand.nextInt(4) + 1;// variation in all others
	int Emphasis = rand.nextInt(4) + 1;// the quadrant that will house the last element

	ArrayList<Element> elements = new ArrayList<Element>();//a list of all elements currently on the canvas

	PrinciplesOfDesign() {
		System.out.println("Unity is of : " + Unity);
		System.out.println("Emphasis will be in Quad: " + Emphasis);

	}
	/**
	 * getter for unity
	 * @return Unity
	 */
	public int getUnity() {
		return Unity;
	}
	
	/**
	 * Getter for Emphasis
	 * @return
	 */
	public double getEmphasis() {
		return Emphasis;
	}

	/**
	 * Getter for the number of elements on canvas
	 * @return
	 */
	public int getNumElements() {
		// TODO Auto-generated method stub
		return this.elements.size();
	}

	/**
	 * adds element to elements list, and resets the variety var, because a new element is getting created
	 * @param element
	 */
	public void addElement(Element element) {
		// TODO Auto-generated method stub
		this.elements.add(element);
		this.variety = rand.nextInt(4) + 1;

	}
	
	
	
	
	
	
	
	
	
	

	/**
	 * If a new color is needed for a new element this method returns that color. All of these methods retrieving the different attributes function in 
	 * the same way. If it's the fist element, generate a random color, if unity is not designated for color generate a random color for the element or
	 * if it's he emphasis element generate a random color. This makes for all elements to have a new starting color from other elements unless unity is 
	 * across colors. If unity == 1, then the very first element will determine the color scheme of the piece. 
	 * @param emphasis
	 * @return
	 */
	public Color getNewElementColor(boolean emphasis) {
		if (elements.size() == 0 || Unity != 1 || emphasis) {//if not the very fist element, or unity is for color, or it's the last element
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();

			return new Color(r, g, b);//make random color
		} else {

			return this.elements.get((elements.size() - 1)).getElementBaseColor();//otherwise use last elements color
		}

	}

	/**
	 *  All methods with Next... take into account variety. If variety is selected for the respective attribute it will randomly generate the next component.
	 *  In this case it is color, so if variety == 1 the element will be rainbow. There will not be variety however if there is unity in the respective component. 
	 * @param elementBaseColor
	 * @return
	 */
	public Color getNextColor(Color elementBaseColor) {

		//randomly generate color if vareity is 1 and unity is not
		if (this.variety == 1 && this.Unity != 1) {
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();
			return new Color(r, g, b);
		}

		else {
			return elementBaseColor;//use the previous color
		}

	}

	/**
	 * This method works just like getNewElementColor. Lengths are from size 0 - 100. If unity is selected for 2 for length, the length is determined by taking the
	 * avrage of all previous elements lengths. This can result in a changing length because shape() is not controlled for length. 
	 * @param emphasis
	 * @return
	 */
	public double getElementLength(boolean emphasis) {
		// TODO Auto-generated method stub
		if (elements.size() == 0 || Unity != 2 || emphasis) {//if very first element and not unity for length, randomly generate length
			return (Math.random() * 100);
		} 
		else {
			int divider = elements.size();//get divider for average length
			double averageLength = 0;

			for (int i = 0; i < this.elements.size(); i++) {
				averageLength += this.elements.get(i).getlength();//sum up the average lengths for all elements
			}

			return averageLength / divider;//return the average length for all previous elements and use this
		}

	}

	
	/**
	 * This function is similar to  getNextColor. If variety is selected for length it will be random unless unity is also slesected for lenght. 
	 * @param averageLenghtOfCurrElement
	 * @return
	 */
	public double getNextLength(double averageLenghtOfCurrElement) {
		
		if (this.variety == 2 && this.Unity != 2) {//length variety and no unity in length

			return (Math.random() * 100);// generate random length
		}

		else {// if no variety return the average length of the current element
			return averageLenghtOfCurrElement;
		}

	}

	

	/**Again this method works similar to the respective ones for length and color. If it's the very fist element generate a random texture, or if unity is not
	 * set for texture. Otherwise, use the average width from all previous elements. 
	 * 
	 * @param emphasis
	 * @return
	 */
	public int getElementTexture(boolean emphasis) {
		if (elements.size() == 0 || Unity != 4 || emphasis) {//first element or no unity for texture
			return (int) (Math.random() * 10);//generate random width/texture
		} else {//otherwise use arage width
			int divider = elements.size();//divider
			double width = 0;//sum of widths

			for (int i = 0; i < this.elements.size(); i++) {
				width += this.elements.get(i).getwidth();//add all elements widths together
			}
			return (int) (width / divider);//divide by total number of elements
		}

	}
	
	
	/**
	 * This class generates random widths if variety is selected for texture for a given element. Otherwise it uses the same width as previously used.
	 * @param elementTexture
	 * @return
	 */
	public double getNextElementTexture(int elementTexture) {
		if (this.variety == 4 && this.Unity != 4) {//if variety is for texture and unity is not
			return Math.random() * 10;//use random width

		} else {
			return elementTexture;
		}
	}

}
