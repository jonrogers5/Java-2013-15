import java.awt.*;
import javax.swing.JOptionPane;
/**
 * This class contains a thread that controls a specific taxi.  It also contains the
 * essential data unique to that taxi.
 * Each taxi contains its own taxiThread.
 * int id -The unique id number assigned to the taxi.  It is als the taxi's assigned
 * patrol area
 * Location variables:
 * orig -Origination location used only during flags and requests.  During a flag 
 * orig = the location taxi was at when flaged.
 * dest -Destination location used only during flags and requests.  Final destination
 * taxi is sent to before resuming its patrol.
 * curloc -Location in which taxi currently resides.
 * aorig -Origination location of the Taxi's patrol area.  Used when taxi first enters 
 * service and returning from the patrol area after a transport.
 * Control and GUI instances:
 * boolean stop -Set to true to stop the infinate loop when the taxi goes out of 
 * service.
 * Controller cont -Reference to controller to allow communication.
 * TaxiUI ui - Reference to taxi GUI.  Used to display data.
 * SystemUI sui - Reference to system GUI.  Used to display data.
 * int stat 
 */
public class TaxiThread implements Runnable{

	int id;
	
	Location orig, dest, curloc,aorig;
	
	private boolean stop;
	private Controller cont;
	private TaxiUI ui;
	private SystemUI sui;
	private int status;
	
	/**
	 * Constructor -Assigns instance variables.
	 * @param s -Incomming reference to SystemUI.
	 * @param c -Incomming reference to Controller.
	 * @param i -Assigned patrol area and id.
	 */
	public TaxiThread(SystemUI s,Controller c, int i){
		cont = c;
		stop = false;
		id = i;
		curloc = DetArea.getLocation(i);
		aorig = curloc;
		sui = s;
		status = 0;
	}
	
	/**
	 * Gets status of the taxi.
	 * @return status integer.
	 */
	public int getStatus(){
		return status;
	}
	/**
	 * Thread run method.  Allows multiple taxis to run at time same time.
	 * Contains infinate loops and control structures needed to move the taxi form 
	 * location to location.
	 * Infinate loops run until stop is set to true.
	 * Taxi moves to new locations avery 10 seconds and waits 40 seconds during passenger
	 * exchanges.
	 */
	public void run(){
		ui = new TaxiUI(this);
		displayData();
		
		try{
			while (!stop){
				while (!stop){
					if (status == 0){
						if (status != 0)
							break;
						for (int z = 0;z < DetArea.areasize - 1;z++){
							Thread.sleep(10000);
							if (stop){
								cont.removeTaxi(id);
								break;
							}
							if (status != 0){
								displayData();
								break;
							}
							curloc = cont.update(curloc, new Location(curloc.ew - 1, curloc.ns, id),id);
							displayData();
						}
						if (status != 0)
							break;
						for (int z = 0;z < DetArea.areasize - 1;z++){
							Thread.sleep(10000);
							if (stop){
								cont.removeTaxi(id);
								break;
							}
							if (status != 0){
								displayData();
								break;
							}
							curloc = cont.update(curloc,new Location(curloc.ew, curloc.ns + 1, id),id );
							displayData();
						}
						if (status != 0){
							displayData();
							break;
						}
						for (int z = 0;z < DetArea.areasize - 1;z++){
							Thread.sleep(10000);
							if (stop){
								cont.removeTaxi(id);
								break;
							}
							if (status != 0){
								displayData();
								break;
							}
							curloc = cont.update(curloc, new Location(curloc.ew + 1, curloc.ns, id), id);
							displayData();
						}
						if (status != 0)
							break;
						for (int z = 0;z < DetArea.areasize - 1;z++){
							Thread.sleep(10000);
							if (stop){
								cont.removeTaxi(id);
								break;
							}
							if (status != 0){
								displayData();
								break;
							}
							curloc = cont.update(curloc, new Location(curloc.ew, curloc.ns - 1, id),id);
							displayData();
						}
					}
					else
						break;
				}
				if (status != 0){
					while (!orig.isEqualTo(curloc)){
						//moves to origination
							passCommute(orig);
						}
					Thread.sleep(30000);
					status = 1;
					while (!dest.isEqualTo(curloc)){
						//Sends taxi to final destination
							passCommute(dest);
							
					}
					Thread.sleep(30000);
					status = 0;
					while (!aorig.isEqualTo(curloc)){
						passCommute(aorig);
						
						if (status != 0)
							break;
						if (stop){
							cont.removeTaxi(id);
							break;
						}
					}
				}
			}
		}catch (InterruptedException e){}
		ui.closeUI();
		cont.removeTaxi(id);
	}
	/**
	 * Called by GUI when "go out of service" is clicked.
	 * Takes cab out of service and sets stop to true. if patrolling.
	 */
	public void stopTaxi(){
		stop = true;
		if (status != 0)
			JOptionPane.showMessageDialog(null,"Taxi will go out of service when it enters patrol","Can't yet stop!", JOptionPane.INFORMATION_MESSAGE);
	}
	/**
	 * Called by GUI when "Flag" is clicked.  Purpose: Places cab in flag mode if patrolling.
	 * @param ew -Requested East-West destination.
	 * @param ns -Requested North-South destination.
	 */
	public void flag(int ew, int ns){
		if (status == 0){
			status = 1;
			orig = curloc;
			dest = new Location(ew, ns,id);
		}
		else
			JOptionPane.showMessageDialog(null,"Cannot flag taxi.","Flag Error!", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Called by dispatcher (DisControl) to place the taxi in request mode (if patrolling).
	 * @param r -Requested origination.
	 * @param d -Requested destination.
	 */
	public void call(Location r, Location d){
		if (status == 0){
			r.setID(id);
			d.setID(id);
			orig = r;
			dest = d;
			status = 2;
		}
	}
	
	/**
	 * Called by run method if cab is in return-to-patrol, respond, or transport mode.
	 * This method contains the code to handle deadlocks.
	 * Purpose: Moves taxi to appropriate location. 
	 * @param dloc -desired location to move taxi.
	 */
	private void passCommute(Location dloc){
		int curstat = status; 
		try{
			//Computes horizontal right motion
			for (int z = curloc.ns;z < dloc.ns;z++){
				Thread.sleep(10000);
				if ((status == 0 && stop) || (curstat != status)){
					displayData();
					return;
				}
				if (curloc.timeout < 3)
					curloc = cont.update(curloc,new Location(curloc.ew,z + 1 ,id),id);
				else 
					break;
				displayData();
			}
			//Computes horizontal left motion
			for (int z = curloc.ns;z > dloc.ns;z--){
				Thread.sleep(10000);
				if ((status == 0 && stop) || (curstat != status)){
					displayData();
					return;
				}
		
				if (curloc.timeout < 3)
					curloc = cont.update(curloc,new Location(curloc.ew,z - 1 ,id),id);
				else 
					break;
				displayData();
			}
				//Computes vertical up motion
			for (int z = curloc.ew;z < dloc.ew;z++){
				Thread.sleep(10000);
				if ((status == 0 && stop) || (curstat != status)){
					displayData();
					return;
				}
				
				if (curloc.timeout < 3)
					curloc = cont.update(curloc,new Location(z + 1,curloc.ns,id),id);
				else 
					break;
				displayData();
			}
			//Computes vertical down direction
			for (int z = curloc.ew;z > dloc.ew;z--){
				Thread.sleep(10000);
				if ((status == 0 && stop) || (curstat != status)){
					displayData();
					return;
				}
				
				if (curloc.timeout < 3)
					curloc = cont.update(curloc,new Location(z - 1,curloc.ns,id),id);
				else 
					break;
				displayData();
			}
				//Deadlock handler
			while (curloc.timeout > 2){
				Thread.sleep(10000);
				if ((status == 0 && stop) || (curstat != status)){
					displayData();
					return;
				}
				
				if (curloc.ew < DetArea.gridsize){
					curloc = cont.update(curloc,new Location(curloc.ew + 1,curloc.ns, id),id);
					displayData();
				}
				if (curloc.timeout < 2)
					break;
				
				if ((status == 0 && stop) || (curstat != status)){
					displayData();
					return;
				}
				
				
				if (curloc.ns > 1){
					curloc = cont.update(curloc,new Location(curloc.ew,curloc.ns - 1, id),id);
					displayData();
				}
				if (curloc.timeout < 2)
					break;
				
				if ((status == 0 && stop) || (curstat != status)){
					displayData();
					return;
				}
				
				
				if (curloc.ew > 1){
					curloc = cont.update(curloc,new Location(curloc.ew - 1,curloc.ns, id),id);
					displayData();
				}
				if (curloc.timeout < 2)
					break;
				
				if ((status == 0 && stop) || (curstat != status)){
					displayData();
					return;
				}
				
				
				if (curloc.ns < DetArea.gridsize){
					curloc = cont.update(curloc,new Location(curloc.ew - 1,curloc.ns, id),id);
					displayData();
				}
			}
		}catch (InterruptedException e){}
	}
	/**
	 * Sends taxi data (location, status, etc.) to taxi and system GUI so it can be
	 * Displayed.
	 */
	private void displayData(){
		final int a = DetArea.getArea(curloc);
		EventQueue.invokeLater(new Runnable()
		{
			public void run(){
				sui.printLoc(curloc.toStr(),id);
				if (status == 0){
					ui.printData(id, a, status, "", "",curloc.toStr());
					sui.printData(id, a, status, "", "");	
				}
				else{
					ui.printData(id, a, status, orig.toStr(),dest.toStr(),curloc.toStr());
					sui.printData(id, a, status, orig.toStr(), dest.toStr());
				}
			}
		});
	}
}
