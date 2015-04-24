import java.awt.*;
import javax.swing.*;

import java.awt.event.*;

/**
 * JFrame taxi -Main frame for the taxi GUI
 * JButton flag removes taxi from patrol mode, places it in transport mode and sends it to
 * the requested destination.
 * JButton out removes taxi from system and stops its thread.
 * JTextField few -Desired east-west coordinate (flag)
 * JTextField fns -Desired north-south coordinate (flag)
 * -The following text boxes display data of the taxi.
 * id -id number of the selected taxi.
 * area -Area taxi is currently travelling in.
 * stat -Taxi status. Either set to Responding,Transport, or Patrol
 * loc -Taxi's current location.
 * orig -Taxi's origination.  Used in flag and requests only.
 * dest -Taxi's destination.  Used in flag and transport only.
 * Taxi control variables:
 * TaxiThread taxithread -Reference to taxi's TaxiThread for communication.
 */
public class TaxiUI implements ActionListener{
	private TaxiThread taxithread;
	//Declare bottons and boxes
	private JFrame taxi = new JFrame();
	
	private JButton flag = new JButton("Flag");
	private JButton out = new JButton("Go out of service");
	
	private JTextField id = new JTextField();
	private JTextField area = new JTextField();
	private JTextField stat = new JTextField();
	private JTextField loc = new JTextField();
	private JTextField orig = new JTextField();
	private JTextField dest = new JTextField();
	
	//Flag entries
	private JTextField few = new JTextField();
	private JTextField fns = new JTextField();
	
	/**
	 * Consructor -Assigns a reference to Controller c.
	 * Adds objects (JButtons, etc) to taxi GUI
	 * Assigns listeners to buttons.
	 * @param t -Incomming TaxiThread reference.
	 */
	public TaxiUI(TaxiThread t){
		taxithread = t;
		
		JPanel controls = new JPanel();
		controls.setLayout(new BorderLayout());
		
		JPanel btns = new JPanel();
		JPanel tbs = new JPanel();
		JPanel entries = new JPanel();
		
		taxi.getContentPane().setLayout(new BorderLayout());
		
		taxi.getContentPane().add(controls, BorderLayout.SOUTH);
		taxi.getContentPane().add(tbs, BorderLayout.NORTH);
		
		controls.add(entries, BorderLayout.NORTH);
		controls.add(btns, BorderLayout.SOUTH);
		
		btns.setLayout(new GridLayout(1,2));
		entries.setLayout(new GridLayout(2,1));
		tbs.setLayout(new GridLayout(6,2));
		
		entries.add(new JLabel("Flag NS"));
		entries.add(fns);
		entries.add(new JLabel("Flag EW"));
		entries.add(few);
		btns.add(flag);
		btns.add(out);
		
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
		
		flag.addActionListener(this);
		out.addActionListener(this);
		
		taxi.setTitle("Taxi #" + Integer.toString(t.id));
		taxi.setSize(300,300);
		taxi.setVisible(true);
		taxi.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	/**
	 * Listener for "out" and "Flag" buttons.
	 */
	public void actionPerformed(ActionEvent e){
		try{
			if (e.getSource() == out)
				taxithread.stopTaxi();
			else if (e.getSource() == flag)
				taxithread.flag(Integer.parseInt(few.getText()), Integer.parseInt(fns.getText()));
		}catch (Exception ex){
			JOptionPane.showMessageDialog(null,"Text boxes only except integers!","Integers only!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Called by a taxi (TaxiThread) to display data except   
	 * the location of the taxi.
	 * @param i -Taxi id and assigned patrol area.
	 * @param a -Area in which taxi is currently present.
	 * @param s -Taxi's status. 0 = patrol; 1 = transport; 2 = responding.
	 * @param o -Origination location as String.
	 * @param d -Destination location as String.
	 * @param location -Taxi's current location in string form.
	 */
	public void printData(int i, int a,int s, String o, String d, String location){
		id.setText(Integer.toString(i));
		area.setText(Integer.toString(a));
		orig.setText(o);
		dest.setText(d);
		loc.setText(location);
		
		if (s == 0)
			stat.setText("Patrol");
		else if (s == 1)
			stat.setText("Transport");
		else 
			stat.setText("Responding");
	}
	/**
	 * Closes taxi GUI when "out" is clicked.
	 */
	public void closeUI(){
		taxi.setVisible(false);
	}
}
