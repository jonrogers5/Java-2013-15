import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
/**
 * GUI for Dispatcher (DisControl)
 * JFrame dispatch -Min frame for dispatcher
 * JButton call -Button used to send a request to dispatcher (DisControl).
 * JTextFields: sns & sew are the inputs for the origination ns and ew coordinates.
 * dns & dew are the inputs for the destination ns and ew coordinates.
 * DisControl discont -Reference to dispatcher (instance variable).
 */
public class DispatchUI implements ActionListener {
	//Declare bottons and boxes
	private JFrame dispatch = new JFrame();
	
	private JButton call = new JButton("Call");
	
	//Boxes take type text
	private JTextField sns = new JTextField();
	private JTextField dns = new JTextField();
	private JTextField sew = new JTextField();
	private JTextField dew = new JTextField();

	
	private DisControl discont;
	
	/**
	 * Displays frame and adds its components (JButtons, etc).  Also assigns discont.
	 * @param dc -Reference to be assigned to discont.
	 */
	public DispatchUI(DisControl dc){
		discont = dc;
		
		JPanel btns = new JPanel();
		
		dispatch.getContentPane().setLayout(new BorderLayout());
		
		dispatch.getContentPane().add(btns, BorderLayout.NORTH);
		
		btns.setLayout(new GridLayout(2,2));		
		
		btns.add(new JLabel("Origination ns"));
		btns.add(sns);
		btns.add(new JLabel("Destination ns"));
		btns.add(dns);
		btns.add(new JLabel("Origination ew"));
		btns.add(sew);
		btns.add(new JLabel("Destination ew"));
		btns.add(dew);
		
		dispatch.getContentPane().add(call, BorderLayout.SOUTH);
		
		dispatch.setTitle("Taxi Dispatcher");
		dispatch.setSize(500,500);
		dispatch.setVisible(true);
		dispatch.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		call.addActionListener(this);
	}
	
	/**
	 * This listener is activated by the call button.  Sends the request to the dispatcher
	 * (DisControl).
	 */
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == call){
			try{
				if (sew.getText().equals("") || sns.getText().equals("") || dew.getText().equals("") || dns.getText().equals(""))
					JOptionPane.showMessageDialog(null,"All text boxes must contain integers!","Error!", JOptionPane.INFORMATION_MESSAGE);
				else
					discont.addRequest(new Location(Integer.parseInt(sew.getText()),Integer.parseInt(sns.getText())), new Location(Integer.parseInt(dew.getText()),Integer.parseInt(dns.getText())));
			}catch (Exception ex){
				JOptionPane.showMessageDialog(null,"All text boxes must contain integers!","Error!", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

}
