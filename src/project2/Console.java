package project2;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Console extends Rental {

    /** Represents the type of console player, see enum type. */
    private ConsoleTypes consoleType;

    public Console() {
    }

    public Console(String nameOfRenter,
                   GregorianCalendar rentedOn,
                   GregorianCalendar dueBack,
                   GregorianCalendar actualDateReturned,
                   ConsoleTypes consoleType) {
        super(nameOfRenter, rentedOn, dueBack, actualDateReturned);
        this.consoleType = consoleType;
    }

    public ConsoleTypes getConsoleType() {
        return consoleType;
    }

    public void setConsoleType(ConsoleTypes consoleType) {
        this.consoleType = consoleType;
    }

    @Override
    public double getCost(GregorianCalendar dueBack) {

        GregorianCalendar gTemp = new GregorianCalendar();
        double cost = 5;
        gTemp = (GregorianCalendar) dueBack.clone();     //  gTemp = dueBack; does not work!!
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

    @Override
    public String toString() {
        return "Console{" +
                " consoleType=" + consoleType + " " + super.toString() +
                '}';
    }
}


