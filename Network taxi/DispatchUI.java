import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
/**
 * GUI for Dispatcher (DisControl)
 * Contains inputs and controls for regestering a taxi request.
 */
public class DispatchUI extends WindowAdapter implements ActionListener, WindowListener{
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

		dispatch.addWindowListener(this);
		
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
	/**
	 * Shuts down taxi dispatcher and controller if there are no active cabs or if all cabs are patrolling.
	 */
	public void windowClosing(WindowEvent e){
		if (JOptionPane.showConfirmDialog(null, "Confirm Exit!!!","Exit Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
			if (!discont.cont.hasCabs())
				System.exit(0);
			else
				JOptionPane.showMessageDialog(null, "Connot close because the entire system is not on patrol","Can't Close", JOptionPane.INFORMATION_MESSAGE);
		}
	}

}
