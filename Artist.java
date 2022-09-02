import java.awt.Color;
import java.util.Random;

/**
 * 
 */

/**
 * 
 * 
 * This class is the driver for the whole program, and creates a turtle using a class downloaded from here (https://sites.google.com/a/asmsa.org/java-turtle/documentation)
 * and sets the canvas size to my screen size. THe main method then calls painter which is the driving clsss for the whole program. It then calls the most significant
 * function from Paiter, paint. Paint takes the turtle as its only argument, and after paint has finished, the program hides the turtle.  
 * @author noahgans
 *
 */
public class Artist {

	/** Main method, creates turtle, makes a paiter, and passes painter the turtle
	 * @param args
	 */
	public static void main(String[] args) {
		
		Turtle turtle = new Turtle();//turtle is made 
		turtle.setCanvasSize(1280,740);//canvas set to size of screen
		
		Paint painter = new Paint(turtle);//make new painter with turtle
		painter.paint();//paint
		turtle.hide();//hide turtle after painting

		
		}
		
		
		


		
	

}
