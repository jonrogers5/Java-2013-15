import java.util.*;

import javax.swing.JOptionPane;
/**
 * Controller is the "brain" of each taxi and is the class that removes and adds taxis to the system
 * Controller contains the taxi list which is used by the dispatcher
 * Controller contains the location list which determines the locations of each taxi
 * List loc -Instance variable containing the locations of all in-service taxis in a list
 * List cabs -Instance variable containing a list of all TaxiThread references in existence.
 * SysemUI system -Instance variable allowing the controller to communicate with the system GUI
 */
public class Controller {
	private List<Location> loc = new ArrayList<Location>(0);
	private List<TaxiThread> cabs = new ArrayList<TaxiThread>(0);
	private SystemUI system;
	
	/**
	 * Assigns a refeence to system
	 * @param s - incomming reference variable.
	 */
	public Controller(SystemUI s){
		system = s;
	}
	/**
	 * A syncronized method that calls the following private methods based 
	 * on the incomming parameters: 
	 * removeLocation(int id), romoveLocation(ol), and getLocation(ol,l)
	 * The method is called by removeTaxi(int id) or TaxiThread
	 * @param ol -Current location of a taxi. ol is always null when during removeLocation(id)
	 * @param l -Location a taxi requests to move to.  l is always null during removeLocation(ol).
	 * @param id -Taxi id number.  Used when removing a location with a specific id.
	 * @return -Returns the location a taxi moved to (if getLocation).  Returns null on all removes
	 */
	public synchronized Location update(Location ol, Location l, int id){
		if (ol == null && l == null){
			removeLocation(id);
			return null;
		}
		else if (l == null){
			removeLocation(ol);
			return null;
		}
		return getLocation(ol,l);
	}
	/**
	 * This method is called by update when a taxi is requesting to move to a new location.
	 * @param ol -Location taxi is currently at.
	 * @param l -Location taxi wants to move to.
	 * @return -Returns location taxi moved to to update, which updates the taxi.  if location 
	 * is not available ol is returned.
	 */
	private Location getLocation(Location ol,Location l){
		ListIterator<Location> i = loc.listIterator();
		Location curloc;
		
		while (i.hasNext()){
			curloc = i.next();
			if (curloc.isEqualTo(l)){
				ol.incTimeout();
				return ol;
			}
		} 
		removeLocation(ol);
		loc.add(l);
		return l;
	}
	/**
	 * Removes the old location of a taxi that just moved to a new location
	 * @param ol -Location to remove.
	 */
	private void removeLocation(Location ol){
		ListIterator<Location> i = loc.listIterator();
		while (i.hasNext())
			if (i.next().isEqualTo(ol)){
				i.remove();
				return;
			}
	}
	/**
	 * Removes location of a taxi going out of service
	 * @param id -taxi id
	 */
	private void removeLocation(int id){
		ListIterator<Location> i = loc.listIterator();
		while (i.hasNext())
			if (i.next().id == id){
				i.remove();
				return;
			}
	}
	/**
	 * Adds taxi to system
	 * @param t -taxi to be added
	 * @return Returns true if taxi is successfully added.  Otherwise false
	 */
	public boolean addTaxi(TaxiThread t){
		if (DetArea.isInRange(t.id)){
			cabs.add(t);
			Thread th = new Thread(t);
			th.start();
			return true;
		}
		else
			JOptionPane.showMessageDialog(null,"Area out of bounds!","Cannot Start Taxi", JOptionPane.INFORMATION_MESSAGE);
		return false;
	}
	/**
	 * Returns a taxi with a specific id if exists.
	 * @param id -id of taxi to return
	 * @return Returns a reference to specified taxi if found.  Otherwise returns null
	 */
	public TaxiThread getTaxi(int id){
		ListIterator<TaxiThread> li = cabs.listIterator();
		TaxiThread curtaxi;
		
		while (li.hasNext()){
			curtaxi = li.next();
			if (curtaxi.id == id)
				return curtaxi;
		}
		return null;
	}
	/**
	 * Removes a specific taxi from system.  Only called when "Go out of service" 
	 * buttion is clicked.
	 * @param id -id of taxi to be romoved.
	 */
	public void removeTaxi(int id){
		ListIterator<TaxiThread> li = cabs.listIterator();
		
		while(li.hasNext())
			if (li.next().id == id){
				li.remove();
				update(null, null, id);
				system.removeCabIndex(id);
			}
	}
	/**
	 * Called by SystemUI during system exit to determine if the system can shut down
	 * @return -returns false if if there are taxis that are not patrolling. 
	 * Otherwise returns true. 
	 */
	public boolean canExit(){
		ListIterator<TaxiThread> li = cabs.listIterator();
		while (li.hasNext())
			if (li.next().getStatus() != 0)
				return false;
		return true;
	}
	/**
	 * Called by dispatcher to determine if there are active cabs in the system.
	 * @return -Returns false if the cabs list is empty.  Otherwise returns true.
	 */
	public boolean hasCabs(){
		ListIterator<TaxiThread> li = cabs.listIterator();
		return li.hasNext();
	}
}
