package project2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RentGameDialog extends JDialog implements ActionListener {
    private int closeStatus;

    public static final int OK = 0;
    public static final int CANCEL = 1;

    public RentGameDialog(JFrame parent, Game game) {
    }

    public void actionPerformed(ActionEvent e) {
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

