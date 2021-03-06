import javax.swing.JOptionPane;
/**
 * This is a class consisting of only static methods and variables.
 * Purpose: Used to setup the demsions of the grid and areas.
 * It is also used to determine the area of a specific taxi and to compute the area limits.
 * It also establishes the initial patrol location of a cab.
 */
public class DetArea {

	static int dimension = 40/10;
	static int gridsize = 40;
	static int areasize = 10;

	
	
	
	/**
	 * Returns the initial patrol location of a new taxi. 
	 * @param area -Assigned patrol area of taxi and its id. 
	 * @return initial patrol location.
	 */
	public static Location getLocation(int area){
		int ew = (area -1)/dimension;
		int ns = (area - 1)% dimension;
		
		//Convert to actual location
		ns *= areasize;
		ns++;
		ew *= areasize;
		ew = gridsize - ew;
		
		return new Location(ew, ns, area);
	}
	
	/**
	 * Computes area number of a specific location. 
	 * @param l -Desired location.
	 * @return -Area number.
	 */
	public static int getArea(Location l){
		//Convert to manipulation location
		int ns = l.ns - 1;
		int ew = gridsize - l.ew;
		
		//Converts location to area
		ew = ew/areasize;
		ns = ns/areasize;
		return ((dimension * ew) + ns) + 1;
	}
	/**
	 * Called when starting a new taxi to determine if the desired
	 * area is in the bounds of the grid.
	 * @param a -Desired area. 
	 * @return -true if in bounds. Otherwise false.
	 */
	public static boolean isInRange(int a){
		if (a > 0 && a <= (dimension * dimension))
			return true;
		return false;
	}
}
