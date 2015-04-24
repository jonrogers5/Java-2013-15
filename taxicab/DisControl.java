import java.util.*;
import javax.swing.JOptionPane;

/**
 * This is a class containing a thread used when calling cabs.
 * Purpose: Assigns calls to patrolling taxis.  Updates every 3 seconds
 * Queue<Request> -Requests is a FIFO structure containing all taxi requests.
 * Controller cont -Referencee to controller
 */

public class DisControl implements Runnable{

	private Queue<Request> requests = new LinkedList<Request>();
	private Controller cont;
	
	/**
	 * Constructor: assigns reference to cont
	 * @param c -Incomming controller reference.
	 */
	public DisControl(Controller c){
		cont = c;
	}
	/**
	 * This method displays the dispatch GUI and contains an infinate loop that iterates until
	 * system shutdown.  Loop pauses 3 seconds on every iteration.
	 * Purpose: Assigns existing requests to taxis.
	 */
	public void run(){
		new DispatchUI(this);
		Request curReq = null;
		TaxiThread taxi;
		int area;
		try{
			while (true){
				Thread.sleep(3000);
				
					if (!requests.isEmpty()){
						curReq = requests.element();
						area = DetArea.getArea(curReq.getOrig());
					
						taxi = cont.getTaxi(area);
						if (taxi != null){
							if (taxi.getStatus() == 0){
								taxi.call(curReq.getOrig(), curReq.getDest());
								requests.remove();
							}
							else
								findATaxi(curReq);
						}
						else
							findATaxi(curReq);
						
					}
				
				Thread.sleep(10000);
			}
		}catch (InterruptedException e){}
	}
	
	/**
	 * Finds and assigns a taxi to a request if there is no active taxi in the request's
	 * area or if that taxi is not available for requests. 
	 * Method is called within infinate loop in run method.
	 * @param curReq -Awaiting request.
	 * @param taxi
	 */
	private void findATaxi(Request curReq){
		TaxiThread taxi;
		for (int z = 1;z <= (DetArea.dimension * DetArea.dimension);z++){
			taxi = cont.getTaxi(z);
			if (taxi != null)
				if (taxi.getStatus() == 0){
					taxi.call(curReq.getOrig(), curReq.getDest());
					requests.remove();
					break;
				}
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