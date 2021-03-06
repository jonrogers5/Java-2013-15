import java.awt.*;
import javax.swing.JOptionPane;
import java.net.*;
import java.io.*;
/**
 * This class contains a thread that controls a specific taxi.  It also contains the
 * essential data unique to that taxi such as its id and assigned patrol area.
 * A connetion to the controller is required via network.
 * Controller must be started before this class will work.
 * This class contains code that displays data on the TaxiUI GUI
 * The following .java files are essential: Location, Taxi, DetArea, and TaxiUI.
 * 
 */
public class TaxiThread implements Runnable{

	 int id;
	
	 private Location orig, dest, curloc,aorig;
	
	 private boolean stop;
	 private Controller cont;
	 private TaxiUI ui;
	 private int status;
	 private String IP;

	 DataOutputStream dos;
	 DataInputStream dis;

	 Socket toController;

	 byte data[] = new byte[4];
	
	/**
	 * Gets status of the taxi.
	 * @return status integer.
	 */
	public int getStatus(){
		return status;
	}
	/**
	* Sets up the controller's IP address and initializes the data array to build a blank location
	*@param id id number and assigned patrol area for the taxi 
	*@param IP Controller's IP address.
	*/
	public TaxiThread(int id, String IP){
		if (IP == null)
			this.IP = "localhost";
		else
			this.IP = IP;

		if (DetArea.isInRange(id))
			this.id = id;
		else{
			JOptionPane.showMessageDialog(null,"Areas can only be from 1 to 16","Area out of bounds!", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}

		data[1] = (byte)0;
		data[0] = (byte)0;
		data[2] = (byte)0;
		data[3] = (byte)0;
	}
	/**
	 * Thread run method.  Allows multiple taxis to run at time same time.
	 * Moves the taxi form location to location.
	 * Runs until taxi is shut down
	 * Taxi moves to new locations avery 10 seconds and waits 40 seconds during passenger
	 * exchanges.
	 */
	public void run(){
		
		aorig = DetArea.getLocation(id);
		curloc = Location.toLocation(data);
		status = 0;
		stop = false;


		//Connectt to controller
		for (int z = 0;z<50;z++){
			try{
			
				toController = new Socket(IP, 8000);
				dos = new DataOutputStream(toController.getOutputStream());
				dis = new DataInputStream(toController.getInputStream());
				dos.writeInt(id);
				if (!dis.readBoolean()){
					JOptionPane.showMessageDialog(null,"Cannot start taxi because there is a taxi already servicing this area.","Can't start taxi!", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				}
				else
					while (!curloc.isEqualTo(aorig)){
						dos.write(data);
						dos.write(aorig.printAsBytes());
						dis.read(data);
						curloc = Location.toLocation(data);
					}
				break;

			}
			catch (Exception e){
				e.printStackTrace();
				if (z == 49){
					System.out.println("Unable to connect!");
					System.exit(0);
				}
			}
		}
		ui = new TaxiUI(this);
		displayData();
		int timeout = 0;
		try{
			while (!stop){
				while (!stop){
					if (status == 0){
						if (status != 0)
							break;
						for (int z = 0;z < DetArea.areasize - 1;z++){
							Thread.sleep(10000);
							if (stop){
								System.exit(0);
							}
							if (status != 0){
								displayData();
								break;
							}
							data[0] = (byte)id;
							data[1]	= (byte)curloc.ns;
							data[2] = (byte)(curloc.ew - 1);
							data[3] = (byte)timeout;
							
							dos.write(curloc.printAsBytes());
							dos.write(data);
							dis.read(data);
							curloc = Location.toLocation(data);
							displayData();
						}
						if (status != 0)
							break;
						for (int z = 0;z < DetArea.areasize - 1;z++){
							Thread.sleep(10000);
							if (stop){
								System.exit(0);
							}
							if (status != 0){
								displayData();
								break;
							}
							data[0] = (byte)id;
							data[1]	= (byte)(curloc.ns+1);
							data[2] = (byte)curloc.ew;
							data[3] = (byte)timeout;
							
							dos.write(curloc.printAsBytes());
							dos.write(data);
							dis.read(data);
							curloc = Location.toLocation(data);
							displayData();
						}
						if (status != 0){
							displayData();
							break;
						}
						for (int z = 0;z < DetArea.areasize - 1;z++){
							Thread.sleep(10000);
							if (stop){
								System.exit(0);
							}
							if (status != 0){
								displayData();
								break;
							}
							data[0] = (byte)id;
							data[1]	= (byte)curloc.ns;
							data[2] = (byte)(curloc.ew + 1);
							data[3] = (byte)timeout;
							
							dos.write(curloc.printAsBytes());
							dos.write(data);
							dis.read(data);
							curloc = Location.toLocation(data);
							displayData();
						}
						if (status != 0)
							break;
						for (int z = 0;z < DetArea.areasize - 1;z++){
							Thread.sleep(10000);
							if (stop){
								System.exit(0);
							}
							if (status != 0){
								displayData();
								break;
							}
							data[0] = (byte)id;
							data[1]	= (byte)(curloc.ns-1);
							data[2] = (byte)curloc.ew;
							data[3] = (byte)timeout;
							
							dos.write(curloc.printAsBytes());
							dos.write(data);
							dis.read(data);
							curloc = Location.toLocation(data);
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
							System.exit(0);
						}
					}
				}
			}
		}catch (Exception e){}
		ui.closeUI();
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
	 * Handle deadlocks.
	 * Purpose: Moves taxi to appropriate location. 
	 * @param dloc -desired location to move taxi.
	 */
	private void passCommute(Location dloc){
		int curstat = status; 
		int timeout = 0;
		try{
			//Computes horizontal right motion
			for (int z = curloc.ns;z < dloc.ns;z++){
				Thread.sleep(10000);
				if ((status == 0 && stop) || (curstat != status)){
					displayData();
					return;
				}
				if (curloc.timeout < 3){
					data[0] = (byte)id;
					data[1]	= (byte)(z+1);
					data[2] = (byte)curloc.ew;
					data[3] = (byte)timeout;
					
					dos.write(curloc.printAsBytes());
					dos.write(data);
					dis.read(data);
					curloc = Location.toLocation(data);
				}
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
		
				if (curloc.timeout < 3){
					data[0] = (byte)id;
					data[1]	= (byte)(z-1);
					data[2] = (byte)curloc.ew;
					data[3] = (byte)timeout;
					
					dos.write(curloc.printAsBytes());
					dos.write(data);
					dis.read(data);
					curloc = Location.toLocation(data);
				}
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
				
				if (curloc.timeout < 3){
					data[0] = (byte)id;
					data[1]	= (byte)curloc.ns;
					data[2] = (byte)(z+1);
					data[3] = (byte)timeout;
					
					dos.write(curloc.printAsBytes());
					dos.write(data);
					dis.read(data);
					curloc = Location.toLocation(data);
				}
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
				
				if (curloc.timeout < 3){
					data[0] = (byte)id;
					data[1]	= (byte)curloc.ns;
					data[2] = (byte)(z-1);
					data[3] = (byte)timeout;
					
					dos.write(curloc.printAsBytes());
					dos.write(data);
					dis.read(data);
					curloc = Location.toLocation(data);
				}
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
					data[0] = (byte)id;
					data[1]	= (byte)curloc.ns;
					data[2] = (byte)(curloc.ew+1);
					data[3] = (byte)timeout;
					
					dos.write(curloc.printAsBytes());
					dos.write(data);
					dis.read(data);
					curloc = Location.toLocation(data);
					displayData();
				}
				if (curloc.timeout < 2)
					break;
				
				if ((status == 0 && stop) || (curstat != status)){
					displayData();
					return;
				}
				
				
				if (curloc.ns > 1){
					data[0] = (byte)id;
					data[1]	= (byte)(curloc.ns-1);
					data[2] = (byte)curloc.ew;
					data[3] = (byte)timeout;
					
					dos.write(curloc.printAsBytes());
					dos.write(data);
					dis.read(data);
					curloc = Location.toLocation(data);
					displayData();
				}
				if (curloc.timeout < 2)
					break;
				
				if ((status == 0 && stop) || (curstat != status)){
					displayData();
					return;
				}
				
				
				if (curloc.ew > 1){
					data[0] = (byte)id;
					data[1]	= (byte)curloc.ns;
					data[2] = (byte)(curloc.ew-1);
					data[3] = (byte)timeout;
					
					dos.write(curloc.printAsBytes());
					dos.write(data);
					dis.read(data);
					curloc = Location.toLocation(data);
					displayData();
				}
				if (curloc.timeout < 2)
					break;
				
				if ((status == 0 && stop) || (curstat != status)){
					displayData();
					return;
				}
				
				
				if (curloc.ns < DetArea.gridsize){
					data[0] = (byte)id;
					data[1]	= (byte)curloc.ns;
					data[2] = (byte)(curloc.ew-1);
					data[3] = (byte)timeout;
					
					dos.write(curloc.printAsBytes());
					dos.write(data);
					dis.read(data);
					curloc = Location.toLocation(data);
					displayData();
				}
			}
		}catch (Exception e){}
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
				if (status == 0)
					ui.printData(id, a, status, "", "",curloc.toStr());
				else
					ui.printData(id, a, status, orig.toStr(),dest.toStr(),curloc.toStr());
			}
		});
	}
}
