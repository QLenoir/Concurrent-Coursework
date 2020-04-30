/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cm3113cwstartingpoint;

/**
 *
 * @author quent
 */
public class Updatable {
    
    private final Donation donation;
    private final long timeViewed;
    private Player player;
    
    public Updatable(Donation donation){
        this.donation = donation;
        this.timeViewed = 0;
        this.player = null;
    }
    
    public Updatable(Player player,long timeViewed){
        this.donation = null;
        this.player = player;
        this.timeViewed = timeViewed;
    }
    
    
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
