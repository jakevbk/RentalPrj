package project2;

/********************************************************
 * Project 2
 * @author Thanh Tran & Jacob VanBronkhorst
 *
 * A project for programming renting console and game system.
 * The project needs to create
 * and implement methods and properties for the system to work.
 *
 ********************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class ReturnedOnDialog extends JDialog implements ActionListener {
	/**
	 * text field
	 */
	private JTextField txtDate;

	/**
	 * Ok option button
	 */
	private JButton okButton;

	/**
	 * Cancel option button
	 */
	private JButton cancelButton;

	/**
	 * an int representing the option OK or CANCEL
	 */
	private int closeStatus;

	/**
	 * an object type Rental
	 */
	private Rental unit;

	/**
	 * an integer represents for OK
	 */
	static final int OK = 0;

	/**
	 * an integer represents for Cancel
	 */
	static final int CANCEL = 1;

	/*********************************************************
	 Instantiate a Custom Dialog as 'modal' and wait for the
	 user to provide data and click on a button.
	 @param parent reference to the JFrame application
	 @param unit an instantiated object to be filled with data
	 *********************************************************/

	public ReturnedOnDialog(JFrame parent, Rental unit) {
		// call parent and create a 'modal' dialog
		super(parent, true);

		this.unit = unit;
		setTitle("Returned dialog box");
		closeStatus = CANCEL;
		setSize(300,100);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

		txtDate = new JTextField(dateFormat.format(unit.
				getDueBack().getTime()),30);

		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(1,2));
		textPanel.add(new JLabel("Returned Date: "));
		textPanel.add(txtDate);

		getContentPane().add(textPanel, BorderLayout.CENTER);

		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);

		setVisible (true);
	}

	/**************************************************************
	 Respond to either button clicks
	 @param e the action event that was just fired
	 @throws IllegalArgumentException if return date before rent on date
	 **************************************************************/
	public void actionPerformed(ActionEvent e) {

		JButton button = (JButton) e.getSource();


		// if OK clicked the fill the object
		if (button == okButton) {
			// save the information in the object
			closeStatus = OK;
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			df.setLenient(false);
			GregorianCalendar gTemp = new GregorianCalendar();

			Date d = null;
			try {
				d = df.parse(txtDate.getText());
				gTemp.setTime(d);
				unit.setActualDateReturned(gTemp);

				if(unit.actualDateReturned.before(unit.getRentedOn())){
					throw new IllegalArgumentException();
				}

			} catch (ParseException e1) {

				JOptionPane.showMessageDialog(null,"Date must be formatted properly MM/dd/yyyy");
				closeStatus = CANCEL;
			}
			catch (IllegalArgumentException e2) {

				JOptionPane.showMessageDialog(null,"Can't return before rented try again");
				closeStatus = CANCEL;
			}

		}

		// make the dialog disappear
		if(closeStatus == OK) {
			dispose();
		}

		if(button == cancelButton){
			dispose();
		}
	}

	/**************************************************************
	 Return a String to let the caller know which button
	 was clicked
	 @return an int representing the option OK or CANCEL
	 **************************************************************/
	public int getCloseStatus(){
		return closeStatus;
	}

}
