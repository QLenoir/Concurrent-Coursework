package uk.ac.rgu.cousework;

/**
 * Consumer class that will process updatable data in the queue that Game is holding
 * @author quent
 */
public class UpdateConsumer extends Thread {
    
    private final Game game;
    
    public UpdateConsumer(Game game){    
        this.game = game;
    }
    
    @Override public void run(){
        while(game.isRunning() || game.getQueueLength() != 0){ //Makes sure every data will be processed, even after game stops 
            if(game.getQueueLength()== 0) continue;
            Updatable u = game.remove();
            if(u.isDonation()){ //Updatable data is either a donation or viewing time
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
