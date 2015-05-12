/**
 * this class is an object used by the dispatcher (DisControl) to store requests.
 * This object stores two location objects. Origination and destination.
 * Location orig, dest are instances that store the origination and destination. 
 * locations.
 */
public class Request {

	private Location orig, dest;
	/**
	 * Assigns references to the instances
	 * @param o -Incomming reference for orig.
	 * @param d -Incomming reference for dest.
	 */
	public Request(Location o, Location d){
	orig = o;
	dest = d;
	}
	
	/**
	 * Retrives origination location.
	 * @return -Returns orig
	 */
	public Location getOrig(){
		return orig;
	}
	
	/**
	 * Retrieves destination location.
	 * @return -Returns dest
	 */
	public Location getDest(){
		return dest;
	}
}
