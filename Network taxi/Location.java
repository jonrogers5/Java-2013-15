/**
 * This class is an object containing the necessary data for a location
 * int ew and ns are the coordinates for east-west (ew) streets and north-south (ns) streets.
 * int id is the id of the taxi owning the location. 
 */
public class Location {
	
	int ew, ns, id, timeout;
	
	/**
	 * Constructor: Assigns instances.  Sets timeout to 0.
	 * @param e -ew coordinate.
	 * @param n -ns coordinate.
	 * @param i -owner's id
	 */
	public Location(int e, int n, int i){
	ew = e;
	ns = n;
	id = i;
	timeout = 0;
	}
	public Location(int e, int n, int i, int t){
	ew = e;
	ns = n;
	id = i;
	timeout = t;
	}
	/**
	 * Constructor: Assigns instances except id.  This constrcter is only used during a 
	 * request, where an id cannot be assigned. 
	 * Sets timeout to 0.
	 * @param e -ew coordinate.
	 * @param n -ns coordinate.
	 */
	public Location (int e, int n){
		ew = e;
		ns = n;
		timeout = 0;
	}
	/**
	 * Determines if this location equals a specified location.
	 * @param l -Specified location.
	 * @return -Returns true if equal.
	 */
	public boolean isEqualTo(Location l){
		return (l.ew == ew) && (l.ns == ns);
	}
	/**
	 * increases timeout by 1.
	 * Called when a taxi cannot be granted ownership to a requested location.
	 */
	public void incTimeout(){
		if (timeout < 3)
			timeout++;
	}
	/**
	 * Returns location in String form.
	 * @return Location in String form.
	 */
	public String toStr(){
		return "NS = " + Integer.toString(ns) + "EW = " + Integer.toString(ew); 
	}
	/**
	 * Assigns id to location (request) when being assigned a taxi.
	 * @param i -Incomming id.
	 */
	public void setID(int i){
		id = i;
	}
	/**
	* Converts location into a byte array 
	* Used to send a location to another host
	* @return location as byte array.
	*/
	public byte[] printAsBytes(){
		byte b[] = new byte[4];
		b[0] = (byte)id;
		b[1] = (byte)ns;
		b[2] = (byte)ew;
		b[3] = (byte)timeout;
		return b;
	}
	/**
	* Converts location in the form of a byte array into a standard location.
	* Used when receiving a location (byte array) from the network.
	* @param location Incoming byte array containing location data.
	* @return Location data as location object.
	*/
	public static Location toLocation(byte location[]){
		return new Location((int)location[2], (int)location[1], (int)location[0], (int)location[3]);
	}
}