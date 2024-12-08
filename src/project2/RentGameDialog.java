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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RentGameDialog extends JDialog implements ActionListener {

    /**
     * an int representing the option OK or CANCEL
     */
    private int closeStatus;

    /**
     * Text field for renter name
     */
    private JTextField txtRenterName;

    /**
     * Text field for date rented on
     */
    private JTextField txtDateRentedOn;

    /**
     * text field for due date
     */
    private JTextField txtDateDueDate;

    /**
     * text field for game name
     */
    private JTextField txtGameTitle;

    /**
     * Box for console type
     */
    private JComboBox<ConsoleTypes> comBoxConsoleType;

    /**
     * Ok option button
     */
    private JButton okButton;

    /**
     * Cancel option button
     */
    private JButton cancelButton;

    /**
     * an object type Console
     */
    private Console console;

    /**
     * an object type Game
     */
    private Game game;

    /**
     * an integer represents Ok
     */
    public static final int OK = 0;

    /**
     * an integer represents Cancel
     */
    public static final int CANCEL = 1;

    /*********************************************************
     Instantiate a Custom Dialog as 'modal' and wait for the
     user to provide data and click on a button.
     @param parent reference to the JFrame application
     @param game an instantiated object to be filled with data
     *********************************************************/
    public RentGameDialog(JFrame parent, Game game) {
        // call parent and create a 'modal' dialog
        super(parent, true);
        this.game = game;

        setTitle("Game dialog box");
        closeStatus = CANCEL;
        setSize(500,250);

        // prevent user from closing window
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        txtRenterName = new JTextField("Roger",30);
        txtDateRentedOn = new JTextField(25);
        txtDateDueDate = new JTextField(25);
        txtGameTitle = new JTextField(25);
        comBoxConsoleType = new JComboBox<>(ConsoleTypes.values());

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter= new SimpleDateFormat("MM/dd/yyyy");
        String dateNow = formatter.format(currentDate.getTime());
        currentDate.add(Calendar.DATE, 1);
        String datetomorrow = formatter.format(currentDate.getTime());

        txtDateRentedOn.setText(dateNow);
        txtDateDueDate.setText(datetomorrow);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(5,2));

        textPanel.add(new JLabel("Name of Renter: "));
        textPanel.add(txtRenterName);
        textPanel.add(new JLabel("Date Rented On "));
        textPanel.add(txtDateRentedOn);
        textPanel.add(new JLabel("Date Due (est.): "));
        textPanel.add(txtDateDueDate);
        textPanel.add(new JLabel("What game did you choose?"));
        textPanel.add(txtGameTitle);
        textPanel.add(new JLabel("ConsoleType"));
        textPanel.add(comBoxConsoleType);

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
     @throws IllegalArgumentException if there errors
     **************************************************************/
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();


        // if OK clicked the fill the object
        if (button == okButton) {
            closeStatus = OK;
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            Date d1 = null;
            Date d2 = null;
            try {
                GregorianCalendar gregTemp = new GregorianCalendar();
                d1 = df.parse(txtDateRentedOn.getText());
                gregTemp.setTime(d1);
                game.setRentedOn(gregTemp);



                gregTemp = new GregorianCalendar();
                d2 = df.parse(txtDateDueDate.getText());
                gregTemp.setTime(d2);
                game.setDueBack(gregTemp);



            } catch (ParseException e1) {
                System.out.print("error here");
                throw new IllegalArgumentException();

            }

            if(txtRenterName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Who's renting?");
                closeStatus = CANCEL;
            }
            else
                game.setNameOfRenter(txtRenterName.getText());

            if(txtGameTitle.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "What game?");
                closeStatus = CANCEL;
            }
            else
                game.setNameGame(txtGameTitle.getText());

            Console temp = new Console();
            temp.setConsoleType((ConsoleTypes)comBoxConsoleType.getSelectedItem());
            if((ConsoleTypes)comBoxConsoleType.getSelectedItem() == ConsoleTypes.NoSelection)
                temp.setConsoleType(null);
            if (temp.getConsoleType() != null) {

                GregorianCalendar gregTemp = new GregorianCalendar();
                gregTemp.setTime(d1);
                temp.setRentedOn(gregTemp);
                gregTemp.setTime(d2);
                temp.setDueBack(gregTemp);

                game.setConsole(temp);
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
