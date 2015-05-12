import java.io.*;
import java.net.*;
/**
* Starts TaxiThread (a new taxi) in a specified area
* Listens for requests form the disparcher (DisControl).
*/
public class Taxi{
	/**
	* Starts TaxiThread and communicates with the dispatcher via the network.
	* @param args args[0] Contains the taxi's assigned patrol area.  args[1] Contains the IP address to the controller.
	* If args[1] doesn't exist, the default IP address is "localhost".
	*/
	public static void main(String[] args){
		String IP = null;
		if (args.length > 1)
			IP = args[1];

		TaxiThread taxithread = new TaxiThread(Integer.parseInt(args[0]), IP);
		Thread thread = new Thread(taxithread);
		thread.start();
		
		while (true){
			try{
				ServerSocket toDispatch = new ServerSocket(8000 + taxithread.id);
				Socket incoming = toDispatch.accept();
				DataInputStream dis = new DataInputStream(incoming.getInputStream());
				DataOutputStream dos = new DataOutputStream(incoming.getOutputStream());
			
				dis.readBoolean();
				int status = taxithread.getStatus();
				dos.writeInt(status);

				byte data[] = new byte[4];
			
				if (status == 0){
					dis.read(data);
					Location orig = Location.toLocation(data);
					dis.read(data);
					Location dest = Location.toLocation(data);
					taxithread.call(orig, dest);
				}
				incoming.close();
				toDispatch.close();
			}
			catch (Exception e){}
		}
	}
}