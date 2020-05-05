package uk.ac.rgu.cousework;

/**
 * Class to represent updatable data that can be produced by Viewer thread
 * @author quent
 */
public class Updatable {
    
    //Updatable data i either a donation or the viewed time of a player
    private final Donation donation;
    private final long timeViewed;
    private final Player player;
    
    /**
     * Constructor for a donation updatable data
     * @param donation Donation object that should be processed
     */
    public Updatable(Donation donation){
        this.donation = donation;
        this.timeViewed = 0;
        this.player = null;
    }
    
    /**
     * Constructor for a viewing time for a player that should be processed
     * @param player
     * @param timeViewed 
     */
    public Updatable(Player player,long timeViewed){
        this.donation = null;
        this.player = player;
        this.timeViewed = timeViewed;
    }
    
    /**
     * Method to check if current object is a donation or not
     * @return 
     */
    public boolean isDonation(){
        return donation!=null;
    }

    public Donation getDonation() {
        return donation;
    }

    public long getTimeViewed() {
        return timeViewed;
    }

    public Player getPlayer() {
        return player;
    }
}
