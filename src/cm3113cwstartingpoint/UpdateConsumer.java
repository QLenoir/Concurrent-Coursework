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
public class UpdateConsumer extends Thread {
    
    private Game game;
    
    public UpdateConsumer(Game game){    
        this.game = game;
    }
    
    @Override public void run(){
        while(game.isRunning()){
            if(game.getDonationQueueLength()== 0) continue;
            Donation d = game.removeDonation();
            game.processDonation(d);
            pauseFor(1);
        }
    }
    
    public static void pauseFor(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {/* ignore */}
    }
}
