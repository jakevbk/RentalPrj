package project2;

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

    public Game() {
    }

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

    public String getNameGame() {
        return nameGame;
    }

    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    public Console getConsole() {
        return console;
    }

    public void setConsole(Console console) {
        this.console = console;
    }


    @Override
    public double getCost(GregorianCalendar dueBack) {

        GregorianCalendar gTemp = new GregorianCalendar();
        double cost = 3;
        //        Date d = dueBack.getTime();
        //        gTemp.setTime(d);

        gTemp = (GregorianCalendar) dueBack.clone();     //  gTemp = dueBack;  does not work!!
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

    @Override
    public String toString() {
        return "Game{" +
                "name='" + nameGame + '\'' +
                ", player=" + console + super.toString() +
                '}';
    }
}
