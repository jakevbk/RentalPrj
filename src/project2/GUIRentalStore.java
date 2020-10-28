package project2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

/*****************************************************************
 *
 *  Campers Reservation System
 *
 *****************************************************************/
public class GUIRentalStore extends JFrame implements ActionListener {
    private JMenuBar menus;

    private JMenu fileMenu;
    private JMenu actionMenu;

    private JMenuItem openSerItem;
    private JMenuItem exitItem;
    private JMenuItem saveSerItem;
    private JMenuItem openTextItem;
    private JMenuItem saveTextItem;
    private JMenuItem reserveConsoleItem;
    private JMenuItem reserveGameItem;
    private JMenuItem returnedOutItem;

    private JMenuItem currentRentedScn;
    private JMenuItem rentedOutItemScn;
    private JMenuItem sortConsoleGameScn;

    private JPanel panel;

    private ListModel DList;

    private JTable jTable;

    private JScrollPane scrollList;

     public GUIRentalStore(){
        //adding menu bar and menu items
        menus = new JMenuBar();
        fileMenu = new JMenu("File");
        actionMenu = new JMenu("Action");
        openSerItem = new JMenuItem("Open File");
        exitItem = new JMenuItem("Exit");
        saveSerItem = new JMenuItem("Save File");
        openTextItem = new JMenuItem("Open Text");
        saveTextItem = new JMenuItem("Save Text");
        reserveConsoleItem = new JMenuItem("Reserve a Console");
        reserveGameItem = new JMenuItem("Reserve a Game");
        returnedOutItem = new JMenuItem("Return Console or Game");

        currentRentedScn = new JMenuItem("Current Store log");
        rentedOutItemScn = new JMenuItem("Rented out screen");
        sortConsoleGameScn = new JMenuItem("Sort Consoles Games  Screen");

        fileMenu.add(openSerItem);
        fileMenu.add(saveSerItem);
        fileMenu.addSeparator();
        fileMenu.add(openTextItem);
        fileMenu.add(saveTextItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        fileMenu.addSeparator();
        fileMenu.add(currentRentedScn);
        fileMenu.add(rentedOutItemScn);
        fileMenu.add (sortConsoleGameScn);

        actionMenu.add(reserveConsoleItem);
        actionMenu.add(reserveGameItem);
        actionMenu.addSeparator();
        actionMenu.add(returnedOutItem);

        menus.add(fileMenu);
        menus.add(actionMenu);

        openSerItem.addActionListener(this);
        saveSerItem.addActionListener(this);
        openTextItem.addActionListener(this);
        saveTextItem.addActionListener(this);
        exitItem.addActionListener(this);
        reserveConsoleItem.addActionListener(this);
        reserveGameItem.addActionListener(this);
        returnedOutItem.addActionListener(this);

        currentRentedScn.addActionListener(this);
        rentedOutItemScn.addActionListener(this);
        sortConsoleGameScn.addActionListener(this);

        setJMenuBar(menus);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        DList = new ListModel();
        jTable = new JTable(DList);
        scrollList = new JScrollPane(jTable);
        panel.add(scrollList);
        add(panel);
        scrollList.setPreferredSize(new Dimension(800,300));

        setVisible(true);
        setSize(950,450);
    }

    public void actionPerformed(ActionEvent e) {
        Object comp = e.getSource();
        returnedOutItem.setEnabled(true);

        if (currentRentedScn == comp)
            DList.setDisplay(ScreenDisplay.CurrentRentalStatus);

        if (rentedOutItemScn == comp) {
            DList.setDisplay(ScreenDisplay.RetendItems);
            returnedOutItem.setEnabled(false);
        }

        if (sortConsoleGameScn == comp)
            DList.setDisplay(ScreenDisplay.SortByGameConsole);

        if (openSerItem == comp || openTextItem == comp) {
            JFileChooser chooser = new JFileChooser();
            int status = chooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                if (openSerItem == comp)
                    DList.loadDatabase(filename);
                else if (openTextItem == comp)
                    DList.loadFromText((filename));
            }
        }

        if (saveSerItem == comp || saveTextItem == comp) {
            JFileChooser chooser = new JFileChooser();
            int status = chooser.showSaveDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().getAbsolutePath();
                if (saveSerItem == e.getSource())
                    DList.saveDatabase(filename);
                else if (saveTextItem == comp)
                    DList.saveAsText(filename);
            }
        }

        if(e.getSource() == exitItem){
            System.exit(1);
        }
        if(e.getSource() == reserveConsoleItem){
            Console RV = new Console();
            RentConsoleDialog dialog = new RentConsoleDialog(this, RV);
            if(dialog.getCloseStatus() == RentConsoleDialog.OK){
                DList.add(RV);
            }
        }
        if(e.getSource() == reserveGameItem){
            Game tentOnly = new Game();
            RentGameDialog dialog = new RentGameDialog(this, tentOnly);
            if(dialog.getCloseStatus() == RentGameDialog.OK) {
                DList.add(tentOnly);
            }
        }

        if (returnedOutItem == e.getSource()) {
            int index = jTable.getSelectedRow();
            if (index != -1) {
                GregorianCalendar dat = new GregorianCalendar();

                Rental unit = DList.get(index);
                ReturnedOnDialog dialog = new ReturnedOnDialog(this, unit);

                if(dialog.getCloseStatus() == ReturnedOnDialog.OK) {
                    JOptionPane.showMessageDialog(null,
                            "  Be sure to thank " + unit.getNameOfRenter() +
                                    "\n for renting with us and the price is:  " +
                                    unit.getCost(unit.getActualDateReturned()) +
                                    " dollars");
                    DList.upDate(index, unit);
                }
            }
        }
    }

    public static void main(String[] args) {
        new GUIRentalStore();
    }
}
