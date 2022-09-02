
/**
 * TransitionFution contains the 2d Array that describes the relationship between state and another state. The values ascend from 0 to 1, so that a random number
 * generated between 0-1 can easily be used to find the next states. The rows follow the same order as the columns listed. The probabilitys were chosen to create
 * Elements of fair size and uniqueness. The probability pushes the states from color and texture to line and shape, and eventually to space. Once space is reached
 * it will 100% return to color to start the next element.
 * @author noahgans
 *
 */
public class TransitionFuntion {

	double tprobs[][] = {
			{ 0, .5, .7,  1, 0}, //color
			{0, 0, .5, 1, 0},//texture
			{.25, .5, .7, 1, 0}, //line
			{.1, .2, .9, .8, 1},//shape
			{1, 0, 0, 0, 0}, //space
			
	};
	
	
	
	/**This single method returns a state given a state by generating a random number and finding the state assosiated with the probability of the random number
	 * for a given column(starting state). 
	 * 
	 * @param currState
	 * @return nextState
	 */
	public int getNextState(int currState) {
		double nextState = Math.random();//generate random number
		
		for (int i = 0; i < 5; i++) {//go through all rows
			if (nextState < tprobs[currState][i]) {//for a given state currState if the probability is greater than the random num
				return i;//return that state to enter
			}
			
		}
		return (Integer) null;

	}

}
