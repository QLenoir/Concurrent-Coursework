package uk.ac.rgu.cousework;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

/**
 * Designed with aid if NetBeans GUI builder 
 * @author DAVID
 */
public class GUI extends javax.swing.JFrame {

    private final Game game;
    private Viewer testViewer;
    private final Executor viewerPool;
    DecimalFormat round = new DecimalFormat("0.00");
    private final static int MAX_VIEWER = 50; //Max number of viewver threads to handle
    
    public GUI() {
        initComponents();

        this.game = new Game(this,"");
        this.jComboBox1.setModel(new DefaultComboBoxModel(game.getPlayersNames().toArray()));
        
        
        this.viewerPool = Executors.newFixedThreadPool(MAX_VIEWER); //Creates the thread pool
        this.createClockTimer(); //Instanciate the timer that displays the curent time
        this.totalViewerstask(); //Instanciate the timer that retrieves the total number of viewers
        this.fillTableTask(); //Instance the timer that update the table every 1/10th of a second
    }

    /**
     * Method to create a timer that displays the current time of the system
     */
    private void createClockTimer() {
        java.util.Timer timer = new java.util.Timer(true); // the Timer
        java.util.TimerTask task = new java.util.TimerTask(){ // the Task
            @Override public void run(){

                java.awt.EventQueue.invokeLater(
                        new Runnable() {
                            @Override public void run() {
                                getTextFieldTime().setText(getTime());
                            }}); // this is thread-safe as sets clock via the EDT
            }
        };
        timer.scheduleAtFixedRate(task, 0,1000);
    }
    
    /**
     * Method to create a timer that display the time since the game is running
     */
    private void startGameTimer() {
        java.util.Timer timer = new java.util.Timer(true); // the Timer
        long start = System.currentTimeMillis();
        DateFormat simple = new SimpleDateFormat("mm:ss"); 
        java.util.TimerTask task = new java.util.TimerTask(){ // the Task
            @Override public void run(){
                    Date result = new Date(System.currentTimeMillis() - start); 
                    java.awt.EventQueue.invokeLater(
                        new Runnable() {
                            @Override public void run() {  
                                if(game.isRunning()){    
                                getTextFieldGameRunningTime().setText(simple.format(result));
                                }else{
                                    cancel();
                                }
                            }}); // this is thread-safe as sets clock via the EDT
            }
        };
        timer.scheduleAtFixedRate(task, 0,1000);    }

    /**
     * Retrieve current time of System
     * @return A String with the date and time of System
     */
    public String getTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
    
    /**
     * Method that creates a timer to update the information table every 1/10th of a second
     */
    private void fillTableTask() {
        java.util.Timer timer = new java.util.Timer(true); // the Timer
        java.util.TimerTask task = new java.util.TimerTask(){ // the Task
            @Override public void run(){

                java.awt.EventQueue.invokeLater(
                        new Runnable() {
                            @Override public void run() {
                               fillTable();
                            }}); // this is thread-safe as sets clock via the EDT
            }
        };
        timer.scheduleAtFixedRate(task, 0,100);
    }
    
    /**
     * Method that creates a timer to update the number of viewers every 1/10th of a second
     */
    private void totalViewerstask() {
        java.util.Timer timer = new java.util.Timer(true); // the Timer
        java.util.TimerTask task = new java.util.TimerTask(){ // the Task
            @Override public void run(){
                int sum = 0;
                sum = game.getPlayers().stream().map((player) -> player.getNumViewers()).reduce(sum, Integer::sum);
                String total = String.valueOf(sum);
                java.awt.EventQueue.invokeLater(
                        new Runnable() {
                            @Override public void run() {
                               textFieldNumberViewers.setText(total);
                            }}); // this is thread-safe as sets clock via the EDT
            }
        };
        timer.scheduleAtFixedRate(task, 0,100);
    }
    
    /* uses data in Players list from Game to fill JTable on the GUI */
    public void fillTable() {
        List<Player> players = game.getPlayers();
        Object[][] tableData = new Object[players.size() + 1][4];
        int totalViewers = 0, totalNumDonations = 0, row = 0;
        double totalValDonations = 0.0;
        for (Player p : game.getPlayers()) {
            String name = p.getPlayerName();
            int nViewers = p.getNumViewers(), nDonations = p.getNumOfDonations();
            double vDonations = p.getSumOfDonations();
            tableData[row][0] = p.getPlayerName();
            tableData[row][1] = nViewers;
            tableData[row][2] = nDonations;
            tableData[row][3] = round.format(vDonations);
            totalViewers += nViewers;
            totalNumDonations += nDonations;
            totalValDonations += vDonations;
            row++;
        }
        tableData[row][0] = "TOTAL";
        tableData[row][1] = totalViewers;
        tableData[row][2] = totalNumDonations;
        tableData[row][3] = round.format(totalValDonations);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                tableData, new String[]{"Player", "Viewering", "# Donations", "£ Donations"}
        ));
    }

    public void addToDonationHistory(String s) {
        this.textAreaDonationHistory.append("\n" + s);
    }

    public void updateViewerNumber() {
        this.textFieldNumberViewers.setText("" + game.getViewers().size());
    }
    
    public void updateReport(String str) {
        this.textAreaResults.append("" + str);
    }

    public JTextField getTextFieldTime(){
        return this.textFieldTime;
    }
    
    public JTextField getTextFieldGameRunningTime(){
        return this.textFieldGameRunningTime;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaResults = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        textFieldGameName = new javax.swing.JTextField();
        buttonStartGame = new javax.swing.JButton();
        buttonStopGame = new javax.swing.JButton();
        buttonProcessResults = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        textAreaDonationHistory = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        textFieldTime = new javax.swing.JTextField();
        textFieldGameRunningTime = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        buttonStartOneViewer = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        spinnerDonation = new javax.swing.JSpinner();
        buttonMakeDonation = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        textFieldNumberViewers = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        buttonStartViewers = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        spinnerNumberViewerThreads = new javax.swing.JSpinner();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        spinnerViewerRate = new javax.swing.JSpinner();
        jLabel16 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        spinnerViewerActions = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Game Management GUI");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        textAreaResults.setColumns(20);
        textAreaResults.setRows(5);
        jScrollPane1.setViewportView(textAreaResults);

        jLabel3.setText("Results");

        jLabel4.setText("Game name");

        buttonStartGame.setBackground(new java.awt.Color(102, 255, 102));
        buttonStartGame.setText("Start Game");
        buttonStartGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartGameActionPerformed(evt);
            }
        });

        buttonStopGame.setBackground(new java.awt.Color(255, 102, 102));
        buttonStopGame.setText("Stop Game");
        buttonStopGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStopGameActionPerformed(evt);
            }
        });

        buttonProcessResults.setBackground(new java.awt.Color(51, 51, 255));
        buttonProcessResults.setText("Process Results");
        buttonProcessResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonProcessResultsActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Player", "Viewing", "# Donations", "£ Donations"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable1);

        textAreaDonationHistory.setColumns(20);
        textAreaDonationHistory.setRows(5);
        jScrollPane4.setViewportView(textAreaDonationHistory);

        jLabel10.setText("Donation History");

        jLabel11.setText("Current Time");

        jLabel12.setText("Game RunTime");

        textFieldTime.setText("12:07:23");

        textFieldGameRunningTime.setText("03:45");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textFieldGameName, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(buttonStartGame)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(buttonStopGame)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(buttonProcessResults))
                                .addComponent(jLabel3)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10)
                            .addComponent(jScrollPane4)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel11))
                                .addGap(16, 16, 16)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(textFieldTime, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(textFieldGameRunningTime))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(textFieldGameName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(textFieldTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonStartGame)
                    .addComponent(buttonStopGame)
                    .addComponent(buttonProcessResults)
                    .addComponent(jLabel12)
                    .addComponent(textFieldGameRunningTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("Single Viewer");

        jLabel2.setText("Select Player to View");

        buttonStartOneViewer.setText("Start Viewer");
        buttonStartOneViewer.setEnabled(false);
        buttonStartOneViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartOneViewerActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Player A", "Player B", "Player C", "Player D" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel9.setText("Amount to donate to selected payer");

        spinnerDonation.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));

        buttonMakeDonation.setText("Make Donation");
        buttonMakeDonation.setEnabled(false);
        buttonMakeDonation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMakeDonationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(spinnerDonation, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonStartOneViewer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(buttonMakeDonation, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(buttonStartOneViewer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(spinnerDonation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonMakeDonation)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel13.setText("Number of Viewers");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(textFieldNumberViewers)
                .addGap(61, 61, 61))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(textFieldNumberViewers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setText("Multiple Viewers");

        buttonStartViewers.setText("Add Multiple Viewers");
        buttonStartViewers.setEnabled(false);
        buttonStartViewers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartViewersActionPerformed(evt);
            }
        });

        jLabel14.setText("Number of Viewer Threads to Start");

        spinnerNumberViewerThreads.setModel(new javax.swing.SpinnerNumberModel(10, 1, 1000, 1));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(buttonStartViewers)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(spinnerNumberViewerThreads, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(buttonStartViewers))
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(spinnerNumberViewerThreads, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setText("Set Viewer Behaviour");

        jLabel15.setText("Interval between Viewer Actions");

        spinnerViewerRate.setModel(new javax.swing.SpinnerNumberModel(1, 0, 100, 1));

        jLabel16.setText("ms");

        jCheckBox1.setText("Unlimited Actions");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel17.setText("Number of Actions Per Viewer");

        spinnerViewerActions.setModel(new javax.swing.SpinnerNumberModel(100, 10, 1000, 10));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(jLabel5)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spinnerViewerActions, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(spinnerViewerRate, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(spinnerViewerRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox1)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(spinnerViewerActions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 5, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonStartOneViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartOneViewerActionPerformed
        int viewerActionInterval = (Integer) this.spinnerViewerRate.getValue();
        testViewer = new Viewer("Test" , game, 1, viewerActionInterval);
        this.buttonMakeDonation.setEnabled(true);
        viewerPool.execute(testViewer);
    }//GEN-LAST:event_buttonStartOneViewerActionPerformed

    private void buttonMakeDonationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMakeDonationActionPerformed
        int who = this.jComboBox1.getSelectedIndex();
        testViewer.switchPlayer(game.getPlayers().get(who));       
        double donationAmount = (Integer) this.spinnerDonation.getValue();
        testViewer.donate(donationAmount);
    }//GEN-LAST:event_buttonMakeDonationActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        int who = this.jComboBox1.getSelectedIndex();
        testViewer.switchPlayer(game.getPlayers().get(who));
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void buttonStartViewersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartViewersActionPerformed
        int numViewers = (Integer) this.spinnerNumberViewerThreads.getValue();
        int viewerActionInterval = (Integer) this.spinnerViewerRate.getValue();
        Viewer v;
        for (int i = 0; i < numViewers; i++) {
            if(this.jCheckBox1.isSelected()){
                v = new Viewer("V" + i, game, Integer.MAX_VALUE, viewerActionInterval);
            }
            else{
                int numViewerActions = (Integer) this.spinnerViewerActions.getValue();
                v = new Viewer("V" + i, game, numViewerActions, viewerActionInterval);
            }           
            viewerPool.execute(v); //Adds new viewer to thread pool
        }
    }//GEN-LAST:event_buttonStartViewersActionPerformed

    private void buttonStartGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStartGameActionPerformed
        game.startGame();
        this.buttonStartOneViewer.setEnabled(true);
        this.buttonStartViewers.setEnabled(true);
        this.startGameTimer();
        UpdateConsumer update = new UpdateConsumer(game);
        update.start();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                addToDonationHistory("Game started at :" + textFieldTime.getText());
            }
        });
       
    }//GEN-LAST:event_buttonStartGameActionPerformed

    private void buttonStopGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStopGameActionPerformed
        game.stopGame();
        this.buttonStartOneViewer.setEnabled(false);
        this.buttonStartViewers.setEnabled(false);
        String result = game.getSummaryOfDonations();
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                addToDonationHistory("Game stopped at :" + textFieldTime.getText());
                addToDonationHistory(result);
            }
        });
        System.out.println("Game stopped. Number of viewers should reduce to 0");
    }//GEN-LAST:event_buttonStopGameActionPerformed

    private void buttonProcessResultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonProcessResultsActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                textAreaResults.append("Summary of game " + textFieldGameName.getText() +" :\n");
            }
        });
        game.countTimes();
        game.checkDonations();
    }//GEN-LAST:event_buttonProcessResultsActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if(this.jCheckBox1.isSelected()){
            this.jLabel17.setEnabled(false);
            this.spinnerViewerActions.setEnabled(false);
        }
        else{
            this.jLabel17.setEnabled(true);
            this.spinnerViewerActions.setEnabled(true);
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonMakeDonation;
    private javax.swing.JButton buttonProcessResults;
    private javax.swing.JButton buttonStartGame;
    private javax.swing.JButton buttonStartOneViewer;
    private javax.swing.JButton buttonStartViewers;
    private javax.swing.JButton buttonStopGame;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JSpinner spinnerDonation;
    private javax.swing.JSpinner spinnerNumberViewerThreads;
    private javax.swing.JSpinner spinnerViewerActions;
    private javax.swing.JSpinner spinnerViewerRate;
    private javax.swing.JTextArea textAreaDonationHistory;
    private javax.swing.JTextArea textAreaResults;
    private javax.swing.JTextField textFieldGameName;
    private javax.swing.JTextField textFieldGameRunningTime;
    private javax.swing.JTextField textFieldNumberViewers;
    private javax.swing.JTextField textFieldTime;
    // End of variables declaration//GEN-END:variables
}
