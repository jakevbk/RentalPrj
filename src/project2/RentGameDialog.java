package project2;

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
    private int closeStatus;

    private JTextField txtRenterName;
    private JTextField txtDateRentedOn;
    private JTextField txtDateDueDate;
    private JTextField txtGameTitle;
    private JComboBox<ConsoleTypes> comBoxConsoleType;
    private JButton okButton;
    private JButton cancelButton;

    private Console console;
    private Game game;

    public static final int OK = 0;
    public static final int CANCEL = 1;

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
                //   Do some thing good here, what that is, I am not sure.
                System.out.print("error here");
            }

            if(txtRenterName.getText().isEmpty())
                JOptionPane.showMessageDialog(null,"Who's renting?");
            else
                game.setNameOfRenter(txtRenterName.getText());

            if(txtGameTitle.getText().isEmpty())
                JOptionPane.showMessageDialog(null,"What game?");
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
        dispose();
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

