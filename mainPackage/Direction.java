package mainPackage;

/**
 * used to ease dealing with motion in game
 * 
 * @author Richard Yang
 * @version May 26, 2016
 * @author Period: 3
 * @author Assignment: AdventuresOfKidco
 *
 * @author Sources: None
 */
public class Direction {
	public boolean op;
	public boolean on;
	public double accel;

	/**
	 * Constructor that sets the speed of the object
	 * 
	 * @param acc
	 *            the speed of the object
	 */
	public Direction(int acc) {
		accel = acc;
	}

}