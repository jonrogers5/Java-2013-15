import java.util.*;
import javax.swing.JOptionPane;
import java.io.*;
import java.net.*;

/**
 * This is a class containing a thread used when calling cabs.
 * Purpose: Assigns calls to patrolling taxis using a first-in-first-out structure.  Updates every 3 seconds
 */

public class DisControl implements Runnable{

	private Queue<Request> requests = new LinkedList<Request>();
	Controller cont;
	
	/**
	 * Constructor: assigns reference to cont
	 * @param c -Incomming controller reference.
	 */
	public DisControl(Controller c){
		cont = c;
	}
	/**
	 * This method displays the dispatch GUI and contains an infinate loop that iterates every 3 seconds until
	 * system shutdown.
	 * Purpose: Assigns existing requests to taxis.
	 */
	public void run(){
		new DispatchUI(this);
		boolean message = false;
		Request curReq = null;
		TaxiThread taxi;
		int area;
		try{
			while (true){
				Thread.sleep(3000);
				
					if (!requests.isEmpty() && cont.hasCabs()){
						message = false;
						curReq = requests.element();
						area = DetArea.getArea(curReq.getOrig());
						try{
							Socket toTaxi = new Socket(cont.getIPAddress(area), 8000 + area);
							DataOutputStream dos = new DataOutputStream(toTaxi.getOutputStream());
							DataInputStream dis = new DataInputStream(toTaxi.getInputStream());
						
							dos.writeBoolean(true);
							int status = dis.readInt();
					
						
						
							if (status == 0){
								Location orig = curReq.getOrig();
								orig.setID(area);
								Location dest = curReq.getDest();
								dest.setID(area);
								dos.write(orig.printAsBytes());
								dos.write(dest.printAsBytes());
								toTaxi.close();
								requests.remove();
							}
							else{
								toTaxi.close();
								Thread.sleep(500);
								findATaxi(curReq);
							}
						}catch (Exception e){findATaxi(curReq);}
					}
					else if(!requests.isEmpty() && !cont.hasCabs() && message == false){
						message = true;
						JOptionPane.showMessageDialog(null,"There are no cabs in the system!","No available cabs!", JOptionPane.INFORMATION_MESSAGE);
					}
				
			}
		}catch (Exception e){}
	}
	
	/**
	 * Finds and assigns a taxi to a request if there is no active taxi in the request's
	 * area or if that taxi is not available for requests. 
	 * Method is called within infinate loop in run method.
	 * @param curReq -Awaiting request.
	 * @param taxi
	 */
	private void findATaxi(Request curReq){
		for (int z = 1;z <= (DetArea.dimension * DetArea.dimension);z++){
			try{
				Socket toTaxi = new Socket(cont.getIPAddress(z), 8000 + z);
				DataOutputStream dos = new DataOutputStream(toTaxi.getOutputStream());
				DataInputStream dis = new DataInputStream(toTaxi.getInputStream());
						
				dos.writeBoolean(true);
				int status = dis.readInt();
				if (status == 0){
					Location orig = curReq.getOrig();
					orig.setID(z);
					Location dest = curReq.getDest();
					dest.setID(z);
					dos.write(orig.printAsBytes());
					dos.write(dest.printAsBytes());
					toTaxi.close();
					requests.remove();
					break;
				}
			}
			catch (Exception e){}
		}
	}
	
	/**
	 * Adds a new request to the queue.
	 * @param o -Request origination
	 * @param d -Request destination
	 */
	public void addRequest(Location o, Location d){
		if (cont.hasCabs())
			requests.add(new Request(o,d));
		else
			JOptionPane.showMessageDialog(null,"A call requires at least 1 active taxi!","Can't call!", JOptionPane.INFORMATION_MESSAGE);
	}
}