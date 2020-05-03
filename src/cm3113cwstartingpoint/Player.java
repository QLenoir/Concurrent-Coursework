package cm3113cwstartingpoint;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Player object stores player name, number of active viewers, 
 * running totals of number and values of donations from viewers
 * also stores a record of all donations made to Player
 * 
 * Player class keeps running total of all submitted viewing times
 * @author DAVID
 */
public class Player{
    private final String playerName;    /* Player name */
    private int numViewers;             /* current number of viewers */
    private int numOfDonations;         /* running total number of donations */
    private double sumOfDonations;      /* running total of value of donations */
    
    private final ConcurrentHashMap<Viewer, Double> donationsFromViewers; /* record of donations */
    
    private static long allTime;    /* class-level running total of all viewing times */

    /* Player constructor */
    public Player(String name) {
        this.playerName = name;
        this.numViewers = 0;
        donationsFromViewers = new ConcurrentHashMap();
        sumOfDonations = 0;
    }
    
    /* methods for updating and accessing number of current viewers */
    public void gainOneViewer(){
        synchronized(this){
            numViewers++;
        }
    }
    public void loseOneViewer(){
        synchronized(this){
            numViewers--;
        }
    }
    public int getNumViewers() {
        synchronized(this){
            return numViewers;
        }
    }
        
    /* method for processing donation from a Viewer into Player records */
    public void addDonation(Donation donation){
        synchronized(this){
            this.numOfDonations++;
            this.sumOfDonations += donation.getAmount();
            Viewer donor = donation.getViewer();
            if(donationsFromViewers.containsKey(donor)){
                donationsFromViewers.replace(donor, donationsFromViewers.get(donor) + donation.getAmount());
            }
            else{
                donationsFromViewers.put(donor, donation.getAmount());
            }
        }
    }
    
    /* method to get total of all donations in Players record of donations */
    public double sumDonations(){
        synchronized(this){
            double sum = 0;
            sum = donationsFromViewers.values().stream().map((v) -> v).reduce(sum, (accumulator, _item) -> accumulator + _item);
            return sum;
        }
    }
    
    @Override public String toString(){
        return playerName + " received " + this.numOfDonations + " donations, worth " + sumDonations() ;
    }
    
    /* Basic getter method for player name */
    public String getPlayerName() {
        return playerName;
    }
    
    /* method to return reference to HashMap of donation records made to this Player */
    public ConcurrentHashMap<Viewer, Double> getDonationsFromViewers() {
        synchronized(this){
            return donationsFromViewers;
        }
    }

    /* methods to access running totals of number and values of donations */
    public int getNumOfDonations() {
        synchronized(this){
            return numOfDonations;
        }
    }
    public Double getSumOfDonations() {
        synchronized(this){
            return sumOfDonations;
        }
    }
    
    // class-level methods to access and increase grand total of all viewing time  for all Players */
    public static synchronized long getAllTime() {
        return allTime;
    }
    public static synchronized void addToAllTime(long allTime) {
        Player.allTime += allTime;
    }

}
