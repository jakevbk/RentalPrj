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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Game extends Rental {

    /** Represents the name of the game */
    private String nameGame;

    /**
     * Represents the player the person rented to play the game,
     * null if no console was rented.
     */
    private Console console;

    /**
     * A constructor for Game class
     */
    public Game() {
    }

    /**
     * A constructor that initialize the rental information
     * @param nameOfRenter name of renter
     * @param rentedOn rent on date
     * @param dueBack due back date
     * @param actualDateReturned actual date return
     * @param nameGame name of the game
     * @param console console type
     */
    public Game(String nameOfRenter,
                GregorianCalendar rentedOn,
                GregorianCalendar dueBack,
                GregorianCalendar actualDateReturned,
                String nameGame,
                Console console) {
        super(nameOfRenter, rentedOn, dueBack, actualDateReturned);
        this.nameGame = nameGame;
        this.console = console;
    }

    /**
     * A method to get the game name
     * @return nameGame name of the game
     */
    public String getNameGame() {
        return nameGame;
    }

    /**
     * A method to set the game name
     * @param nameGame name of the game
     */
    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    /**
     * A method to get the console type
     * @return console
     */
    public Console getConsole() {
        return console;
    }

    /**
     * A method to set the console type
     * @param console
     */
    public void setConsole(Console console) {
        this.console = console;
    }

    /**
     * A method to calculate the cost for due back date
     * @param dueBack due back date
     * @return cost estimate for return date
     */
    @Override
    public double getCost(GregorianCalendar dueBack) {

        GregorianCalendar gTemp = new GregorianCalendar();
        double cost = 3;
        //        Date d = dueBack.getTime();
        //        gTemp.setTime(d);

        gTemp = (GregorianCalendar) dueBack.clone();
        // gTemp.add(Calendar.DATE, -1);             // this subtracts one day from gTemp

        // these lines will help with debugging
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        System.out.println(formatter.format(gTemp.getTime()));
        System.out.println(formatter.format(rentedOn.getTime()));
        System.out.println(formatter.format(dueBack.getTime()));

        // more code here
        for(int days = 0; days< 7; days++){
            gTemp.add(Calendar.DATE, -1);
        }

        while(gTemp.after(rentedOn)){
            cost += .50;

            gTemp.add(Calendar.DATE, -1);
        }
        if(getConsole() == null){
            cost = cost;
        }
        if (getConsole() != null ) {
            this.console.setRentedOn(rentedOn);
            cost += this.console.getCost(dueBack);
        }
        //This is the extra credit where you can captialize name
        //must also have in Console.java getCost()
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
        return "Game{" +
                "name='" + nameGame + '\'' +
                ", player=" + console + super.toString() +
                '}';
    }
}
