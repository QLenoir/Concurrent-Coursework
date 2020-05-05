package uk.ac.rgu.cousework;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class to retrieve a player viewing time
 * @author quent
 */
public class PlayerCount extends Thread{
    
    private final ConcurrentHashMap<Player,ArrayList<Long>> times;
    private final Player p;
    private String timeResults;
    private long totalTimeForPlayer = 0;
    
    public PlayerCount(Player p, ConcurrentHashMap<Player,ArrayList<Long>> times){
        this.p = p;
        this.times = times;
    } 
            
    @Override
    public void run(){
        
        times.get(p).forEach((time) -> {
            this.totalTimeForPlayer += time;
        });
            this.timeResults = p + " and had " + this.totalTimeForPlayer/1000000 + "ms of views" +"\n";     
    }
    
    /**
     * Returns individual result
     * @return A string of result for the player
     */
    public String getimeResults(){
        return this.timeResults;
    }

    /**
     * Returns individual result
     * @return the long result
     */
    public long getTotalTimeForPlayer(){
        return this.totalTimeForPlayer;
    } 
    
}
