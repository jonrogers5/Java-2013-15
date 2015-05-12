import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
/**
 *This class displays the system GUI. 
 *This GUI appears when the system starts.
 * boolean canchange determines if the area and grid dimensions an be change. Initally
 * set to true.
 * boolean disclosed is set to false when the when DisControl starts to prevent duplicate
 * DisControl's from being opened.
 * JFrame system -Main frame for the system GUI
 * JButton dem is used to set custom dimensions for the area and grid.  Custom
 * dimensions must be set before any taxi(s) enter service.  The default dimensions
 * are: areasize = 10x10 and gridsize = 40x40
 * JButton start starts a new patrolling taxi in the area specified.
 * JButton dis starts DisControl, the dispatch thread.
 * JTextField dems -Text box used to insert a custom gridsize.
 * JTextField area -Text box used to insert a custom areasize.
 * JTextField sarea -Text box used to specify the patrol area of a new taxi.
 * -The following text boxes display data of a taxi selected from a list.
 * id -id number of the selected taxi.
 * area -Area taxi is currently travelling in.
 * stat -Taxi status. Either set to Responding,Transport, or Patrol
 * loc -Taxi's current location.
 * orig -Taxi's origination.  Used in flag and requests only.
 * dest -Taxi's destination.  Used in flag and transport only.
 * -Elements used to display a list of all active taxis.
 * JList -List of active taxis by id.
 * DefaultListModel dlm -Tool used to add and remove taxis from the list.
 * Controller c -Reference allowing communication with the controller.
 */
public class SystemUI extends WindowAdapter implements ActionListener, ListSelectionListener, WindowListener{
	
	//This veriable determines if the demensions can be changed
	private boolean canchange = true;
	private boolean disclosed = true;
	//Declare bottons and boxes
	private JFrame system = new JFrame();
	
	private JButton dem = new JButton("Set Dimensions");
	private JButton start = new JButton("Start Taxi");
	private JButton dis = new JButton("Start Dispatcher");
	
	private JTextField dems = new JTextField();
	private JTextField areasize = new JTextField();
	private JTextField sarea = new JTextField();
	
	private JTextField id = new JTextField();
	private JTextField area = new JTextField();
	private JTextField stat = new JTextField();
	private JTextField loc = new JTextField();
	private JTextField orig = new JTextField();
	private JTextField dest = new JTextField();
	
	private DefaultListModel dlm = new DefaultListModel();
	private JList cabs = new JList(dlm);
	
	
	//Cab controller
	
	Controller c;	
	/**
	 * Consructor -Assigns a reference to Controller c.
	 * Adds objects (JButtons, etc) to system GUI.
	 * Assigns listeners to buttons.
	 */
	public SystemUI(){
		c = new Controller(this);
		//Adds ojects to frame
		
		JPanel btns = new JPanel();
		JPanel tbs = new JPanel();
		JPanel controls = new JPanel();
		JPanel entries = new JPanel();
		
		controls.setLayout(new BorderLayout());
		controls.add(entries, BorderLayout.NORTH);
		controls.add(btns, BorderLayout.SOUTH);
		
		system.getContentPane().setLayout(new BorderLayout());
		
		system.getContentPane().add(controls, BorderLayout.SOUTH);
		system.getContentPane().add(tbs, BorderLayout.NORTH);
		system.getContentPane().add(cabs, BorderLayout.CENTER);
		
		btns.setLayout(new GridLayout(1,3));
		entries.setLayout(new GridLayout(3,1));
		tbs.setLayout(new GridLayout(6,2));
		
		
		btns.add(dis);
		btns.add(start);
		btns.add(dem);
		
		entries.add(new JLabel("Area"));
		entries.add(sarea);
		entries.add(new JLabel("Set dimensions (YxY)"));
		entries.add(dems);
		entries.add(new JLabel("Set Area Size"));
		entries.add(areasize);
		
		
		tbs.add(new JLabel("Taxi ID"));
		tbs.add(id);
		tbs.add(new JLabel("Taxi Area"));
		tbs.add(area);
		tbs.add(new JLabel("Taxi Status"));
		tbs.add(stat);
		tbs.add(new JLabel("Taxi Location"));
		tbs.add(loc);
		tbs.add(new JLabel("Origination"));
		tbs.add(orig);
		tbs.add(new JLabel("Destination"));
		tbs.add(dest);
		
		system.addWindowListener(this);
		
		system.setTitle("Taxi System");
		system.setSize(500,500);
		system.setVisible(true);
		system.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//Listener setup
		
		dem.addActionListener(this);
		dis.addActionListener(this);
		start.addActionListener(this);
	}
	/**
	 * Called by a taxi (TaxiThread) or the list (cabs) listener to display data except   
	 * the location of a selected taxi.
	 * @param i -Taxi id and assigned patrol area.
	 * @param a -Area in which taxi is currently present.
	 * @param s -Taxi's status. 0 = patrol; 1 = transport; 2 = responding.
	 * @param o -Origination location as String.
	 * @param d -Destination location as String.
	 */
	public void printData(int i, int a,int s, String o, String d){
		if (cabs.getSelectedValue() != null)
			if (cabs.getSelectedValue().toString().equals(Integer.toString(i))){
				id.setText(Integer.toString(i));
				area.setText(Integer.toString(a));
				orig.setText(o);
				dest.setText(d);
		
				if (s == 0)
					stat.setText("Patrol");
				else if (s == 1)
					stat.setText("Transport");
				else
					stat.setText("Responding");
			}
	}
	/**
	 * Called by a taxi (axiThread) or the list (cabs) listener to display the current 
	 * location of the selected taxi.
	 * @param location -Location to be displayed.
	 * @param id -taxi's id number.
	 */
	public void printLoc(String location, int id){
		if (cabs.getSelectedValue() != null)
			if (cabs.getSelectedValue().toString().equals(Integer.toString(id)))
				loc.setText(location);
	}
	/**
	 * Called when a taxi id in this list is selected.  Displays location of taxi.  In this
	 * case an id is not needed .
	 * @param location -Location to be displayed.
	 */
	private void printLoc(String location){
		loc.setText(location);
	}
	/**
	 * This method removes a specified taxi id from the list.  Called when a taxi goes out
	 * of servicee.
	 * @param i -id of taxi to remove.
	 */
	public void removeCabIndex(int i){
		dlm.removeElement(Integer.toString(i));
	}
	/**
	 * This listener is for the buttons.  Calls appropriate methods and sets appropriate
	 * parameters based on which button is clicked.
	 * If dis is clicked, a dispatch thread (DisControl) starts.
	 * If dem is clicked, custiom area and grid dimension are set if appropriate test boxes
	 * contain integers.
	 * If start is clicked, a new taxi enters service in the specified area.
	 */
	public void actionPerformed(ActionEvent e){
		boolean taxiadded;
		try{
			if (e.getSource() == dem){
				if (canchange){
					if(!dems.getText().equals("") && !areasize.getText().equals(""))
						canchange = DetArea.setDem(Integer.parseInt(areasize.getText()), Integer.parseInt(dems.getText()));
					
				}
				else
					JOptionPane.showMessageDialog(null,"Dimensions already set.  Cannot change.","Dimensions already set", JOptionPane.INFORMATION_MESSAGE);
			}
			else if (e.getSource() == start){
				if (canchange){
					canchange = DetArea.setDem(10, 40);
				}
			
				if (!sarea.getText().equals("")){
					if (c.getTaxi(Integer.parseInt(sarea.getText())) == null){
						taxiadded = c.addTaxi(new TaxiThread(this,c,Integer.parseInt(sarea.getText())));
						if (taxiadded)
							dlm.addElement(sarea.getText());
					}
					else
						JOptionPane.showMessageDialog(null,"Cannot start taxi.  A taxi already servs this area.","Cannot Start Taxi", JOptionPane.INFORMATION_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(null,"Patrol area required!"," Patrol Area Required", JOptionPane.INFORMATION_MESSAGE);
			
			}
			else if (e.getSource() == dis){
				if (disclosed){
					disclosed = false;
					DisControl dc =	new DisControl(c);
					Thread dt = new Thread(dc);
					dt.start();
				}
				else
					JOptionPane.showMessageDialog(null,"Dispatcher already open!","Cannot Start Multiple dispatchers", JOptionPane.INFORMATION_MESSAGE);
			}
		}catch (Exception ex){
			JOptionPane.showMessageDialog(null,"Text boxes only except integers!","Integers only!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/**
	 * If a new item in the list is selected, this method displays the data for the new item.
	 */
	public void valueChanged(ListSelectionEvent e){
		if (!e.getValueIsAdjusting()){
			TaxiThread tth = c.getTaxi(Integer.parseInt(cabs.getSelectedValue().toString()));
			if (tth != null){
				printLoc(tth.curloc.toStr());
				
				if (tth.getStatus() == 0)
					printData(tth.id,DetArea.getArea(tth.curloc), 0, "","");
				else
					printData(tth.id,DetArea.getArea(tth.curloc), tth.getStatus(), tth.orig.toStr(),tth.orig.toStr());
			}
		}
	}
	/**
	 * Shuts down taxi system if there are no active cabs or if all cabs are patrolling.
	 */
	public void windowClosing(WindowEvent e){
		if (JOptionPane.showConfirmDialog(null, "Confirm Exit!!!","Exit Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
			if (c.canExit())
				System.exit(0);
			else
				JOptionPane.showMessageDialog(null, "Connot close because the entire system is not on patrol","Can't Close", JOptionPane.INFORMATION_MESSAGE);
		}
	}

}
