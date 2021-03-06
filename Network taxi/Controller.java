import java.util.*;
import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;
/**
 * Controller is the "brain" of each taxi and is the class that removes and adds taxis to the system
 * Enables dispatcher
 * Controller contains the location list which determines the locations of each taxi
 */
public class Controller{
	private List<Location> loc = new ArrayList<Location>(0);
	private List<Integer> cabs = new ArrayList<Integer>(0);
	private String IP[] = new String[16];
	
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
	 * Called by dispatcher to determine if there are active cabs in the system.
	 * @return -Returns false if the cabs list is empty.  Otherwise returns true.
	 */
	public boolean hasCabs(){
		ListIterator<Integer> li = cabs.listIterator();
		return li.hasNext();
	}
	/**
	 * Removes a specific taxi from system.  
	 * @param id -id of taxi to be romoved.
	 */
	public void removeTaxi(int id){
		ListIterator<Integer> li = cabs.listIterator();
		
		while(li.hasNext())
			if (li.next().intValue() == id){
				li.remove();
				update(null, null, id);
			}
	}
	/**
	 * Adds taxi (id number) to system
	 * @param id -id of taxi to be added
	 * @return Returns true if taxi is successfully added.  Otherwise false
	 */
	public boolean addTaxi(int id){
		if (DetArea.isInRange(id)){
			if (taxiExists(id))
				return false;
			cabs.add(id);
			return true;
		}
			JOptionPane.showMessageDialog(null,"Area out of bounds!","Cannot Start Taxi", JOptionPane.INFORMATION_MESSAGE);
		return false;
	}
	/**
	* prevents multiple taxis from being assigned to the same patrol area by checking to see if the patrol area was regestered.
	* @param id - Patrol area of taxi.
	*/
	public boolean taxiExists(int id){
		ListIterator<Integer> li = cabs.listIterator();
		
		while (li.hasNext()){
			if (li.next().intValue() == id)
				return true;
		}
		return false;
	}
	/**
	* Logs the IP address of a newly regstered taxi.
	* the IP is used by dispatcher to communicate with a specific taxi.
	* @param address IP address to be logged.
	* @param id Taxi id.
	*/
	public void setIPAddress(String address, int id){
		IP[id-1] = address.substring(1);
	}
	/**
	* Returns IP address of a specific taxi
	* Used only by dispatcher.
	* @param id Id of taxi.
	*/
	public String getIPAddress(int id){
		return IP[id-1];
	}
	/**
	* Activated when controller starts.
	* Listens to the network for incoming taxis.
	* Regesters new taxis.
	* Starts dispatcher.
	* @param args Unused.
	*/
	public static void main(String[] args){
		try{
			Controller controller = new Controller();
			DisControl dispatch = new DisControl(controller);

			Thread disThread = new Thread(dispatch);
			disThread.start();
			
			while (true){

				ServerSocket ss = new ServerSocket(8000);
				Socket incoming = ss.accept();
				
				DataInputStream dis = new DataInputStream(incoming.getInputStream());
				DataOutputStream dos = new DataOutputStream(incoming.getOutputStream());
		
				int id = dis.readInt();
				boolean accepted = controller.addTaxi(id);
				
				controller.setIPAddress(incoming.getInetAddress().toString(), id);

				dos.writeBoolean(accepted);
				
				if (accepted){

					ControllerThread ct = new ControllerThread(incoming,controller, id);
				
					Thread ctThread = new Thread(ct);
					ctThread.start();
					ss.close();
				}
			}
		}
		catch (Exception e){
			
		}
	}
}
