package uk.ac.rgu.cousework;


import java.util.ArrayList;
import java.util.Random;

/**
 * Viewer class extends Thread to run in parallel to Game
 * Viewer sends updates of donations made to players, 
 * and time spent watching each player. 
 * @author DAVID
 */
public class Viewer extends Thread{
    private final String id;            // id for viewer
    
    private final Game game;                  // reference to Game associated with Viewer
    private Player viewedPlayer;        // Player that Viwere is currently watching
    private long startedViewingPlayer;  // stores nanosecond level start of viewing time of current Player 
    
    private final Random random;              
    private final int numberActions;          // number of switches between Players that Viewer can make
    private final int intervalBetweenActions; // time in milliseconds between switches
    
    /* class-level data tracking totals for all Viewers */
    private static long totalViewingTimeRecordedByViewers;
    private volatile static ArrayList<Donation> donationsMade = new ArrayList();  
   
    
    /* Viewer constructor */
    public Viewer(String id, Game f, int actions, int interval){
        this.id = id;
        this.game = f;
        this.numberActions = actions;
        this.intervalBetweenActions = interval;
        this.viewedPlayer = game.getRandomPlayer();        
        this.random = new Random();
        this.viewedPlayer = null; switchPlayer();
        this.startedViewingPlayer = System.nanoTime();
    }

    /* Task for Viewer object, in a loop
     * to perform specified number of switches between Players
     * randomly donation around 10% of the time */
    @Override public void run(){
        game.viewerJoins(this);
        int actionsRemaining = this.numberActions;
        while(game.isRunning() && actionsRemaining > 0){
            try {
                Thread.sleep(this.intervalBetweenActions);
                /* randomly (10% of time) donate a ranom amount (0.01 to 5.00  to current Player*/
                if(Math.random() < 0.1){
                    donate(0.01 + 1.0*random.nextInt(500)/100.0);
                }
                switchPlayer();
                actionsRemaining--;
            } catch (InterruptedException ex) {System.out.println("Viewer " + id + " run method exception");}            
        }
        game.viewerLeaves(this);
        switchPlayer(null);   
    }
    
    /* method to process a donation for a Player */
    public void donate(double amount){
        Donation donation = new Donation(this, viewedPlayer, amount);
        this.game.add(new Updatable(donation)); //Add the updatable data to the process queue
        Viewer.donationsMade.add(donation);
    }
    
    /* Method used to switch between Players */
    public void switchPlayer(Player newPlayerToWatch){
        if(viewedPlayer != null){ /* will be null when Viewer first starts */
            long timeViewed = System.nanoTime() - startedViewingPlayer;
            game.add(new Updatable(viewedPlayer, timeViewed)); //Add the updatable data to the process queue
            synchronized(Viewer.class){
                Viewer.totalViewingTimeRecordedByViewers += timeViewed;
            }
            this.viewedPlayer.loseOneViewer();
            Player.addToAllTime(timeViewed);
        }        
        this.viewedPlayer = newPlayerToWatch;
        if(viewedPlayer != null){ /* will be null here if Viewer is finished viewing */
            this.viewedPlayer.gainOneViewer();
            this.startedViewingPlayer = System.nanoTime();
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try{
                        game.getGui().addToDonationHistory(this +" went to " + viewedPlayer.getPlayerName() + "'s stream");//Update the donation hisotry with the new player that is being watched
                    }catch(NullPointerException e){};     
                }
            });
        }    
    }
    
    /* convenient method for switching to random Player,calls general method above */
    public void switchPlayer(){
        switchPlayer(game.getRandomPlayer());
    }
    

        


    /* class-level methods for accessing totals of viewing time, 
     * and number and value of all Viewers */   
    public static long getTimeViewers() {
        return Viewer.totalViewingTimeRecordedByViewers;
    }   
    public static int getTotalNumberOfDonations(){
        return donationsMade.size();
    }   
    public static double getTotalValueOfDonations(){
        double total = 0;
        total = donationsMade.stream().map((donation) -> donation.getAmount()).reduce(total, (accumulator, _item) -> accumulator + _item);
        return total;
    }
    
}
