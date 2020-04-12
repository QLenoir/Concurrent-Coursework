package cm3113cwstartingpoint;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Game class stores lists of Players, Viewers, Donations and viewing times
 * @author DAVID
 */
public class Game {
    /* Collections stored within Game object */
    private List<Player> players;
    private List<Viewer> viewers;
    private List<Donation> donations;
    private ConcurrentHashMap<Player,ArrayList<Long>> times;
    DecimalFormat round = new DecimalFormat("0.00");
    
    /* Properties of the Game */
    private String name;
    private boolean running;
    private double totalDonationsRecordedByGame;
    private long totalViewingTimeRecordedByGame;
    private int totalNumberViewing;
    
    private Random random;
    private GUI gui;
    
    /* Game construtor */
    public Game(GUI gui, String name){
        this.gui = gui;
        this.name =name;
        this.players = Collections.synchronizedList(new ArrayList());
        this.viewers = Collections.synchronizedList(new ArrayList());
        this.donations = Collections.synchronizedList(new ArrayList());
        this.times = new ConcurrentHashMap();
        this.totalDonationsRecordedByGame = 0;
        this.random = new Random();
        initialise();
    }
    
    /* Method to assign players to Game, called by constructor */
    public void initialise(){
        players.add(new Player("Scarlett"));
        players.add(new Player("David"));
        players.add(new Player("Thor"));
        players.add(new Player("Cleo"));
        System.out.println("Game" + players);
        for(Player p: players) times.put(p, new ArrayList());
        running = true;       
    }
    
    /* User by viewers when switching Players they are viewing */
    public Player getRandomPlayer(){
        return players.get(random.nextInt(players.size()));
    }
    
    /* processes a donation that comes from a Viewer */
    public void processDonation(Donation donation){
        synchronized(this){
            totalDonationsRecordedByGame += donation.getAmount();
            donations.add(donation);
            donation.getPlayer().addDonation(donation);
        }
    }
    
    /* processes a record of time spent viewing a Player that comes from a Viewer */
    public void recordViewingTime(Player p, long time){
        synchronized(this){
            totalViewingTimeRecordedByGame += time;
            times.get(p).add(time);       
        }
    }
       
    /* Basic methods for accessing Game properties */
    public List<Player> getPlayers() {
        return players;
    }
    
    public ArrayList<String> getPlayersNames() {
        ArrayList<String> names = new ArrayList();
        for(Player p: players) names.add(p.getPlayerName());
        return names;
    }
    
    public List<Viewer> getViewers() {
        synchronized(this){
            return viewers;
        }
    }
       
    public boolean isRunning() {
        return running;
    }
    
    public void stopGame(){
        this.running = false;
    }
    
    public void startGame(){
        this.running = true;
    }

    public List<Donation> getDonations() {
        synchronized(this){
            return donations;
        }
        
    }

    public double getTotalDonationsRecordedByGame() {
        synchronized(this){
            return totalDonationsRecordedByGame;
        }
    }

    public long getTotalViewingTimeRecordedByGame() {
        synchronized(this){
            return totalViewingTimeRecordedByGame;
        }
    }
    
    public ConcurrentHashMap<Player, ArrayList<Long>> getTimes() {
        synchronized(this){
            return times;
        }
    }

    public int getTotalNumberViewing() {
        synchronized(this){
            return totalNumberViewing;
        }
    }

    public void viewerJoins(Viewer v) {
        synchronized(this){
            totalNumberViewing++;
            viewers.add(v);
        }
    }
    
    public void viewerLeaves(Viewer v) {
        synchronized(this){
            totalNumberViewing--;
            viewers.remove(v);
        }
    }
    
    /* Method that checks for consistency of viewing time data 
     * Totals of recorded running total in Game, total of times in HashMap, and 
     * total of times sent by Viewers should all agree if all data is processed safely
    */
    public void countTimes(){
        String timeResults = "";
        long sumOfPlayerTotalTimes = 0;
        for(Player p: times.keySet()){
            long totalTimeForPlayer = 0;
            for(Long time: times.get(p)){
                totalTimeForPlayer += time;
                sumOfPlayerTotalTimes += time;
            }
            timeResults += p + " and had " + totalTimeForPlayer/1000000 + "ms of views" +"\n";           
        }
        
        timeResults += "\n Total viewing times recorded by Players: " 
                + sumOfPlayerTotalTimes/1000000 + "ms \n" +
            "Total viewing times recorded by Game:    " 
                + totalViewingTimeRecordedByGame/1000000 + "ms\n" +
            "Total viewing times recorded by Viewers: " 
                + Viewer.getTimeViewers()/1000000 + "ms\n";
        System.out.println(timeResults);
        gui.updateReport(timeResults);
    }
    
    /* Method that checks for consistency of donations 
     * Totals of recorded donations in Game, total of donations held by Player, and
     * total of donations sent by Viewers should all agree if all data is processed safely
    */
    public void checkDonations(){
        String report = "";
        /* Donations counted by the Game class */
        double totalOfDonations = 0;
        for(Donation d: donations){
            totalOfDonations += d.getAmount();
        }
        report = "Donations counted by Game:    Number = " 
                + donations.size() + " Value = " 
                + round.format(totalOfDonations) + "\n";
        
        /* Sum of Donations counted by the Players */
        int numberDonationsToPlayers = 0;
        double totalOfDonationsToPlayers = 0;
        for(Player p: players){
           numberDonationsToPlayers += p.getNumOfDonations();
           totalOfDonationsToPlayers += p.sumDonations();
        }
        report += "Donations counted by Players: Number = " 
                + numberDonationsToPlayers + " Value = " 
                + round.format(totalOfDonationsToPlayers) + "\n";
        
        /* Sum of Donations sent by the Viewer class */
        report += "Donations counted by Viewers: Number = " 
                + Viewer.getTotalNumberOfDonations() + " Value = " 
                + round.format(Viewer.getTotalValueOfDonations())+ "\n\n"; 
        
        System.out.println(report);
        gui.updateReport(report);
    }
    
}
