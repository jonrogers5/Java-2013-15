import javax.swing.JOptionPane;
/**
 * This is a class consisting of only static methods and variables.
 * Purpose: Used to setup the demsions of the grid and areas.
 * It is also used to determine the area of a specific taxi and to compute the area limits.
 * dimension -Instance variable that stores the square root of the number of areas
 * the grid can hold.
 * gridsize -x & y size of grid, where x = y.
 * areasize -x & y size of area, where x = y.
 */
public class DetArea {

	static int dimension;
	static int gridsize;
	static int areasize;
	
	/**
	 * Sets size (dimsensions) of grid and areas.  gs must be divisible by as. 
	 * @param as -desired area size
	 * @param gs -desired grid size
	 * @return -Returns false if if gs is not divisible by as.  Otherwise returns true.
	 */
	public static boolean setDem(int as, int gs){
		if(as < 0 || gs < 0)
			JOptionPane.showMessageDialog(null,"Values must be grater than 0!","Out of bounds", JOptionPane.INFORMATION_MESSAGE);
		else if (gs % as == 0){
			dimension = gs / as;
			gridsize = gs;
			areasize = as;
			JOptionPane.showMessageDialog(null,"Dimensions have been set Gridsize = " + Integer.toString(gridsize) + " Areasize = " + Integer.toString(areasize) ,"Successful", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		else
			JOptionPane.showMessageDialog(null,"Grid size must be divisible by area size","Out of bounds", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}
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
	 * Called by SystemUI when starting a new taxi to determine if the desired
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
