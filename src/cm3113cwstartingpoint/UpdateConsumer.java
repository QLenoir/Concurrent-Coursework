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
    
    private final Game game;
    
    public UpdateConsumer(Game game){    
        this.game = game;
    }
    
    @Override public void run(){
        while(game.isRunning() || game.getQueueLength() != 0){
            if(game.getQueueLength()== 0) continue;
            Updatable u = game.remove();
            if(u.isDonation()){
               game.processDonation(u.getDonation());
            } else {
                game.recordViewingTime(u.getPlayer(), u.getTimeViewed());
            }
            pauseFor(1);
        }
    }
    
    public static void pauseFor(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {/* ignore */}
    }
}
