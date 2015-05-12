import java.io.*;
import java.net.*;

/**
* This is a thread setup by the controller for communicating with taxis viathe network.  Each taxi contains one.
* All incomming data is sent to the controller.
*/
public class ControllerThread implements Runnable{
	Socket incoming;
	int area;
	Controller controller;
	/**
	* Initializes instance variables only.
	* @param incoming Socket used to communicate with taxi.
	* @param controller Reference to controller.
	* @param area Taxi id and assigned patrol area.
	*/
	public ControllerThread(Socket incoming,Controller controller ,int area){
		this.incoming = incoming;
		this.area = area;
		this.controller = controller;
	}	
	/**
	* Communicates with taxi over the network.
	* Runs until taxi shuts down.
	*/
	public void run(){
		try{
			DataOutputStream dos = new DataOutputStream(incoming.getOutputStream());
			DataInputStream dis = new DataInputStream(incoming.getInputStream());
			Location ol, nl, returnloc;
			byte location[] = new byte[4];

			while (true){
			dis.read(location);
			ol = Location.toLocation(location);
			dis.read(location);
			nl = Location.toLocation(location);
			returnloc = controller.update(ol, nl, 0);
			dos.write(returnloc.printAsBytes());
			}
		}
		catch (Exception e){
			controller.removeTaxi(area);
		}
	}

}