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

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Console extends Rental {

    /** Represents the type of console player, see enum type. */
    private ConsoleTypes consoleType;

    /**
     * A constructor for Console class
     */
    public Console() {
    }

    /**
     * A constructor to initialize the retal information
     * @param nameOfRenter name of the renter
     * @param rentedOn rent on date
     * @param dueBack due back date
     * @param actualDateReturned actual date return
     * @param consoleType console types
     */
    public Console(String nameOfRenter,
                   GregorianCalendar rentedOn,
                   GregorianCalendar dueBack,
                   GregorianCalendar actualDateReturned,
                   ConsoleTypes consoleType) {
        super(nameOfRenter, rentedOn, dueBack, actualDateReturned);
        this.consoleType = consoleType;
    }

    /**
     * A method to get console type
     * @return console type
     */
    public ConsoleTypes getConsoleType() {
        return consoleType;
    }

    /**
     * A method to set console type
     * @param consoleType an object type ConsoleTypes
     */
    public void setConsoleType(ConsoleTypes consoleType) {
        this.consoleType = consoleType;
    }

    /**
     * A method to calculate the cost for due back date
     * @param dueBack date for return
     * @return cost estimate for due back date
     */
    @Override
    public double getCost(GregorianCalendar dueBack) {

        GregorianCalendar gTemp = new GregorianCalendar();
        double cost = 5;
        gTemp = (GregorianCalendar) dueBack.clone();
        //gTemp.add(Calendar.DATE, -1);             // this subtracts one day from gTemp

        // more code here
        for(int days = 0; days< 7; days++){
            gTemp.add(Calendar.DATE, -1);
        }

        while(gTemp.after(rentedOn)) {
            if (this.getConsoleType() == null || this.getConsoleType() == ConsoleTypes.NoSelection) {
                cost = 0;
            }
            if ((this.getConsoleType() == ConsoleTypes.SegaGenesisMini
                    || this.getConsoleType() == ConsoleTypes.NintendoSwich
                    || this.getConsoleType() == ConsoleTypes.PlayStation4Pro)) {
                cost += 1.50;
            } else if ((this.getConsoleType() == ConsoleTypes.PlayStation4
                    || this.getConsoleType() == ConsoleTypes.XBoxOneS)) {
                cost += 1.0;
            }
            gTemp.add(Calendar.DATE, -1);
        }

//This is the extra credit where you can captialize name
// must also have in Game.java getCost()
//        if(cost> 50){
//            setNameOfRenter(getNameOfRenter().toUpperCase());
//        }

        return cost;
    }

    /**
     * A method to set up the string
     * @return a string
     */
    @Override
    public String toString() {
        return "Console{" +
                " consoleType=" + consoleType + " " + super.toString() +
                '}';
    }
}


