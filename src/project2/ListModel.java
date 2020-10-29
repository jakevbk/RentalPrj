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

import javafx.util.converter.LocalDateStringConverter;


import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.lang.management.GarbageCollectorMXBean;
import java.net.SocketOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ListModel extends AbstractTableModel {

    /**
     * An arraylist that holds all the rentals
     */
    private ArrayList<Rental> listOfRentals;

    /**
     * holds only the rentals that are to be displayed
     */
    private ArrayList<Rental> fileredListRentals;

    /**
     *  current screen being displayed
     *  */
    private ScreenDisplay display = ScreenDisplay.CurrentRentalStatus;

    /**
     * holds the column names for the current rentals
     */
    private String[] columnNamesCurrentRentals = {"Renter\'s Name", "Est. Cost",
            "Rented On", "Due Date ", "Console", "Name of the Game"};

    /**
     * holds the column names for the rented out
     */
    private String[] columnNamesforRented = {"Renter\'s Name", "Rented On Date",
            "Due Date",
            "Actual date returned ", "Est. Cost", " Real Cost"};

    /**
     *  type DateFormat object to format all the dates
     */
    private DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    /**
     *  A constructor that instantiate the arraylist and update current rental screen
     */
    public ListModel() {
        display = ScreenDisplay.CurrentRentalStatus;
        listOfRentals = new ArrayList<>();
        fileredListRentals = new ArrayList<>();
        UpdateScreen();
        createList();
    }

    /**
     * A method that update the current display status on screen
     * @param selected an object of ScreenDisplay type
     */
    public void setDisplay(ScreenDisplay selected) {
        display = selected;
        UpdateScreen();
    }

    /**
     * A method that helps to update the current rental status
     * and current status with
     * lambda and stream function
     * @throws RuntimeException if there no chosen case
     */
    private void UpdateScreen() {
        ArrayList<String> ogName = new ArrayList<>();
        for (int i = 0; i < listOfRentals.size() - 1; ++i){
            if(listOfRentals.get(i).getCost(listOfRentals.get(i).getDueBack())>50)
                ogName.add(listOfRentals.get(i).getNameOfRenter());
        }
        switch (display) {

            case CurrentRentalStatus:
                fileredListRentals = (ArrayList<Rental>) listOfRentals.stream()
                        .filter(n -> n.actualDateReturned == null) // filter by items that have not been returned

                        //uncapitalize names of the rentals when return to current rental screen
                        .map(n -> { if(n.getCost(n.getDueBack()) > 50) {
                            n.setNameOfRenter(n.getNameOfRenter().toLowerCase());
                            n.setNameOfRenter(Character.toUpperCase(n.getNameOfRenter().charAt(0))+n.getNameOfRenter().substring(1));
                        }
                            return n;
                        })
                        .collect(Collectors.toList()); // collect rental names to list

                // Note: This uses Lambda function
                Collections.sort(fileredListRentals, (n1, n2) -> n1.getNameOfRenter().compareTo(n2.nameOfRenter));
                break;

            case RetendItems:
                fileredListRentals = (ArrayList<Rental>) listOfRentals.stream()
                        .filter(n -> n.getActualDateReturned() != null) // filter by items that have been returned
                        //uncapitalize the name of renter
                        .map(n -> { if(n.getCost(n.getDueBack()) > 50) {
                            n.setNameOfRenter(n.getNameOfRenter().toLowerCase());
                            n.setNameOfRenter(Character.toUpperCase(n.getNameOfRenter().charAt(0))+n.getNameOfRenter().substring(1));
                        }
                            return n;
                        })
                        .collect(Collectors.toList());

                Collections.sort(fileredListRentals, new Comparator<Rental>() {
                    /**
                     *  An override method that compare names of the renters
                     * @param n1 an object type rental
                     * @param n2 an object type rental
                     * @return 0 if both names are the same
                     *        < 0 if this name is lexicographically less than the other name
                     *        > 0 if this name is lexicographically greater than the other name
                     */
                    @Override
                    public int compare(Rental n1, Rental n2) {
                        return n1.getNameOfRenter().compareTo(n2.nameOfRenter);
                    }
                });

                break;


            // sort by dueBack,
            // sort game first then console
            case SortByGameConsole:
                fileredListRentals = (ArrayList<Rental>) listOfRentals.stream()
                        .filter(n -> n.actualDateReturned == null)
                        .collect(Collectors.toList());

//                // Your code goes here.

                Collections.sort(fileredListRentals, new Comparator<Rental>() {
                    public int compare(Rental o1, Rental o2) {
                        if(o1 instanceof Game && o2 instanceof Game){
                            return o1.getDueBack().compareTo(o2.getDueBack());
                        }
                        return 0;
                    }
                });

                Collections.sort(fileredListRentals, new Comparator<Rental>() {
                    public int compare(Rental o1, Rental o2) {
                        if(o1 instanceof Console && o2 instanceof Console){
                            return o1.getDueBack().compareTo(o2.getDueBack());
                        }
                        return 0;
                    }
                });

                fileredListRentals.stream() // set the renter name to all uppercase
                        .map(arg -> { if(arg.getCost(arg.getDueBack()) > 50) {
                            arg.setNameOfRenter(arg.getNameOfRenter().toUpperCase());
                        }
                            return arg;
                        })
                        .collect(Collectors.toList());


                break;

            default:
                throw new RuntimeException("upDate is in undefined state: " + display);
        }
        fireTableStructureChanged();
    }

    /**
     * A method to get column names
     * @param col number of column
     * @return columnNamesCurrentRentals[col] an array type String with #col number of elements
     *          columnNamesforRented[col] an array type String with #col number of elements
     */
    @Override
    public String getColumnName(int col) {
        switch (display) {
            case CurrentRentalStatus:
                return columnNamesCurrentRentals[col];
            case RetendItems:
                return columnNamesforRented[col];
            case SortByGameConsole:
                return columnNamesCurrentRentals[col];
        }
        throw new RuntimeException("Undefined state for Col Names: " + display);
    }

    /**
     * A method co count the column on each screen
     * @return columnNamesCurrentRentals.length size of the array
     *         columnNamesforRented.length  size of the array
     * @throws IllegalArgumentException if there no case reached
     */
    @Override
    public int getColumnCount() {
        switch (display) {
            case CurrentRentalStatus:
                return columnNamesCurrentRentals.length;
            case RetendItems:
                return columnNamesforRented.length;
            case SortByGameConsole:
                return columnNamesCurrentRentals.length;

        }
        throw new IllegalArgumentException();
    }

    /**
     * A method to get the number of row
     * @return fileredListRentals.size() size of the filter array
     */
    @Override
    public int getRowCount() {
        return fileredListRentals.size();
    }

    /**
     * A method to get the value of certain position
     * @param row number of row
     * @param col number of column
     * @return currentParkScreen(row, col) value in current rental status
     *         rentedOutScreen(row, col) value in returned screen
     * @throws IllegalArgumentException if there no case reached
     */
    @Override
    public Object getValueAt(int row, int col) {
        switch (display) {
            case CurrentRentalStatus:
                return currentParkScreen(row, col);
            case RetendItems:
                return rentedOutScreen(row, col);
            case SortByGameConsole:
                return currentParkScreen(row, col);

        }
        throw new IllegalArgumentException();
    }

    /**
     * A method to get the value in current rental status
     * @param row number of row
     * @param col number of column
     * @return case 0: name of the renter
     *         case 1: due back cost
     *         case 2: rent on date
     *         case 3: "-" if due back equals to null
     *                  due back date
     *         case 4: console types if it is console
     *                 "" if it is game
     *         case 5: game name
     * @throws RuntimeException if no case reached
     */
    private Object currentParkScreen(int row, int col) {
        switch (col) {
            case 0:
                return (fileredListRentals.get(row).nameOfRenter);

            case 1:
                return (fileredListRentals.get(row).getCost(fileredListRentals.
                        get(row).dueBack));

            case 2:
                return (formatter.format(fileredListRentals.get(row).rentedOn.getTime()));

            case 3:
                if (fileredListRentals.get(row).dueBack == null)
                    return "-";

                return (formatter.format(fileredListRentals.get(row).dueBack.getTime()));

            case 4:
                if (fileredListRentals.get(row) instanceof Console)
                    return (((Console) fileredListRentals.get(row)).getConsoleType());
                else {
                    if (fileredListRentals.get(row) instanceof Game)
                        if (((Game) fileredListRentals.get(row)).getConsole() != null)
                            return ((Game) fileredListRentals.get(row)).getConsole().getConsoleType();
                        else
                            return "";
                }

            case 5:
                if (fileredListRentals.get(row) instanceof Game)
                    return (((Game) fileredListRentals.get(row)).getNameGame());
                else
                    return "";
            default:
                throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }


    /**
     *  A method to get value in the returned screen
     * @param row number of row
     * @param col number of column
     * @return case 0: name of renter
     *         case 1: rented on date
     *         case 2: due back date
     *         case 3: actual date return
     *         case 4: cost on due back date
     *         case 5: cost on actual date return
     * @throws RuntimeException if no case reached
     */
    private Object rentedOutScreen(int row, int col) {
        switch (col) {
            case 0:
                return (fileredListRentals.get(row).nameOfRenter);

            case 1:
                return (formatter.format(fileredListRentals.get(row).rentedOn.
                        getTime()));

            case 2:
                return (formatter.format(fileredListRentals.get(row).dueBack.
                        getTime()));

            case 3:
                return (formatter.format(fileredListRentals.get(row).
                        actualDateReturned.getTime()));

            case 4:
                return (fileredListRentals.
                        get(row).getCost(fileredListRentals.get(row).dueBack));

            case 5:
                return (fileredListRentals.
                        get(row).getCost(fileredListRentals.get(row).
                        actualDateReturned
                ));

            default:
                throw new RuntimeException("Row,col out of range: " + row + " " + col);
        }
    }

    /**
     *  A method to add a new rental
     * @param a an object type Rental
     */
    public void add(Rental a) {
        listOfRentals.add(a);
        UpdateScreen();
    }

    /**
     * A method to get the specific rental information from the array
     * @param i index for the array
     * @return value at specific index
     */
    public Rental get(int i) {
        return fileredListRentals.get(i);
    }

    /**
     * A method to update screen
     * @param index index
     * @param unit an object type Rental
     */
    public void upDate(int index, Rental unit) {
        UpdateScreen();
    }

    /**
     * A method to save the data into a filename
     * @param filename filename to save data on
     * @throws RuntimeException if it can not be saved
     */
    public void saveDatabase(String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            System.out.println(listOfRentals.toString());
            os.writeObject(listOfRentals);
            os.close();
        } catch (IOException ex) {
            throw new RuntimeException("Saving problem! " + display);
        }
    }

    /**
     * A method to load data from the file name
     *
     * @param filename a file of data
     * @throws RuntimeException if it can not load data
     */
    public void loadDatabase(String filename) {
        listOfRentals.clear();

        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream is = new ObjectInputStream(fis);

            listOfRentals = (ArrayList<Rental>) is.readObject();
            UpdateScreen();
            is.close();
        } catch (Exception ex) {
            throw new RuntimeException("Loading problem: " + display);

        }
    }

    /**
     * A method to show how to save data into the file
     * @param filename a file to save data
     * @return true if the data can be saved without error
     *         false if any errors happen
     * @throws IllegalArgumentException if theres no file name
     */
    public boolean saveAsText(String filename) {
        if (filename == null || filename.equals("")){
            throw new IllegalArgumentException();
        }
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new FileWriter(filename)));
            int size = fileredListRentals.size();
            out.println(listOfRentals.size());
            for (int i = 0; i < listOfRentals.size(); i++) {
                Rental Unit = listOfRentals.get(i);
                out.println(Unit.getClass().getName());
                out.println(Unit.getNameOfRenter());
                out.println(formatter.format(Unit.rentedOn.getTime()));
                out.println(formatter.format(Unit.dueBack.getTime()));

                if(Unit instanceof Game){
                    if(((Game) Unit).getConsole() != null) {
                        out.println(((Game) Unit).getConsole().getConsoleType());
                        out.println(((Game) Unit).getNameGame());
                    }
                    else{
                        out.println("NoSelection");
                        out.println(((Game) Unit).getNameGame());
                    }
                }

                else if(Unit instanceof Console){
                    out.println(((Console) Unit).getConsoleType());
                    out.println("no game");
                }


                if(Unit.getActualDateReturned() == null){
                    out.println(Unit.actualDateReturned);
                }
                else{
                    out.println(formatter.format(Unit.actualDateReturned.getTime()));
                }

                // more code here

            }
            out.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * A method to load data fromm text
     * @param filename a file of data
     * @throws RuntimeException if the data can not be loaded
     */
    public void loadFromText(String filename) {
        listOfRentals.clear();
        try {
            Scanner scanner = new Scanner(new File(filename));
            int count = Integer.parseInt(scanner.nextLine().trim());
            System.out.println(count);

            for (int i = 0; i < count; i++) {
                GregorianCalendar use1 = new GregorianCalendar();
                GregorianCalendar use2 = new GregorianCalendar();
                GregorianCalendar use3 = new GregorianCalendar();

                String type = scanner.nextLine().trim().toLowerCase();
                String userName = scanner.nextLine();
                Date d1 = formatter.parse(scanner.nextLine().trim());
                use1.setTime(d1);
                Date d2 = formatter.parse(scanner.nextLine());
                use2.setTime(d2);
                ConsoleTypes console = ConsoleTypes.valueOf((scanner.nextLine().trim()));
                String game = (scanner.nextLine());
                String returned = (scanner.nextLine());
                Date d3;

                if(type.equalsIgnoreCase("project2.Game")) {
                    Console temp = new Console();
                    temp.setConsoleType(console);

                    Game unit = new Game();
                    unit.setNameOfRenter(userName);
                    unit.setRentedOn(use1);
                    unit.setDueBack(use2);
                    if(temp.getConsoleType() != ConsoleTypes.NoSelection) {
                        unit.setConsole(temp);
                    }
                    else{
                        unit.setConsole(null);
                    }
                    if(game.contains("no game")) {
                        unit.setNameGame(null);
                    }
                    else{
                        unit.setNameGame(game);
                    }

                    if (returned.equalsIgnoreCase("null")){
                        unit.setActualDateReturned(null);
                    }
                    else {
                        d3 = formatter.parse(returned);
                        use3.setTime(d3);
                        unit.setActualDateReturned(use3);
                    }
                    listOfRentals.add(unit);
                }
                else{
                    Console unit = new Console();

                    unit.setNameOfRenter(userName);
                    unit.setRentedOn(use1);
                    unit.setDueBack(use2);
                    unit.setConsoleType(console);

                    if (returned.equalsIgnoreCase("null")){
                        unit.setActualDateReturned(null);
                    }
                    else {
                        d3 = formatter.parse(returned);
                        use3.setTime(d3);
                        unit.setActualDateReturned(use3);
                    }
                    listOfRentals.add(unit);
                }

            }
            scanner.close();

        } catch (Exception ex) {
            throw new RuntimeException("Loading text file problem" + display);
        }
        UpdateScreen();
    }

    /**
     * A method used to test out the code
     * @throws RuntimeException if there errors in testing
     */
    // used by instructor to test your code.  Please do not change
    public void createList() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        GregorianCalendar g1 = new GregorianCalendar();
        GregorianCalendar g2 = new GregorianCalendar();
        GregorianCalendar g3 = new GregorianCalendar();
        GregorianCalendar g4 = new GregorianCalendar();
        GregorianCalendar g5 = new GregorianCalendar();
        GregorianCalendar g6 = new GregorianCalendar();
        GregorianCalendar g7 = new GregorianCalendar();
        GregorianCalendar g8 = new GregorianCalendar();

        try {
            Date d1 = df.parse("1/20/2020");
            g1.setTime(d1);
            Date d2 = df.parse("12/22/2020");
            g2.setTime(d2);
            Date d3 = df.parse("12/20/2019");
            g3.setTime(d3);
            Date d4 = df.parse("7/20/2020");
            g4.setTime(d4);
            Date d5 = df.parse("1/20/2010");
            g5.setTime(d5);
            Date d6 = df.parse("9/29/2020");
            g6.setTime(d6);
            Date d7 = df.parse("7/25/2020");
            g7.setTime(d7);
            Date d8 = df.parse("7/29/2020");
            g8.setTime(d8);

            Console console1 = new Console("Person1", g4, g6, null, ConsoleTypes.PlayStation4);
            Console console2 = new Console("Person2", g5, g3, null, ConsoleTypes.PlayStation4);
            Console console3 = new Console("Person5", g4, g8, null, ConsoleTypes.SegaGenesisMini);
            Console console4 = new Console("Person6", g4, g7, null, ConsoleTypes.SegaGenesisMini);
            Console console5 = new Console("Person1", g5, g4, g3, ConsoleTypes.XBoxOneS);

            Game game1 = new Game("Person1", g3, g2, null, "title1",
                    new Console("Person1", g3, g2, null, ConsoleTypes.PlayStation4));
            Game game2 = new Game("Person1", g3, g1, null, "title2",
                    new Console("Person1", g3, g1, null, ConsoleTypes.PlayStation4));
            Game game3 = new Game("Person1", g5, g3, null, "title2",
                    new Console("Person1", g5, g3, null, ConsoleTypes.SegaGenesisMini));
            Game game4 = new Game("Person7", g4, g8, null, "title2", null);
            Game game5 = new Game("Person3", g3, g1, g1, "title2",
                    new Console("Person3", g3, g1, g1, ConsoleTypes.XBoxOneS));
            Game game6 = new Game("Person6", g4, g7, null, "title1",
                    new Console("Person6", g4, g7, null, ConsoleTypes.NintendoSwich));
            Game game7 = new Game("Person5", g4, g8, null, "title1",
                    new Console("Person5", g4, g8, null, ConsoleTypes.NintendoSwich));

            add(game1);
            add(game4);
            add(game5);
            add(game2);
            add(game3);
            add(game6);
            add(game7);

            add(console1);
            add(console2);
            add(console5);
            add(console3);
            add(console4);

        } catch (ParseException e) {
            throw new RuntimeException("Error in testing, creation of list");
        }
    }
}

  
  
