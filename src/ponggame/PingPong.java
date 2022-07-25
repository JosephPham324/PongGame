package ponggame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Pham Nhat Quang
 */
public class PingPong extends javax.swing.JFrame implements KeyListener {

    /**
     * Size of ball
     */
    public static final int _BALL_SIZE = 20;

    /**
     * Height of racket
     */
    public static final int _RACKET_HEIGHT = 50;

    /**
     * Width of racket
     */
    public static final int _RACKET_WIDTH = 6;

    /**
     * Height of panel
     */
    public static final int _PANEL_HEIGHT = 400;

    /**
     * Width of panel
     */
    public static final int _PANEL_WIDTH = 800;

    /**
     * Padding size for racket
     */
    public static final int _PADDING_RACKET = 10;

    /**
     * Speed of racket
     */
    public static final int _RACKET_SPEED = _PANEL_HEIGHT / 24;

    /**
     * Value to indicate red racket
     */
    public static final int _RED_RACKET = 0;

    /**
     * Value to indicate green racket
     */
    public static final int _GREEN_RACKET = 1;

    /**
     * Keys pressed
     */
    public ArrayList<Integer> keys;
    Thread gameThread; //Thread of game
    int time; //Track time of game in seconds
    Clock timer; //Clock for game

    //Variables for red player
    int redPoint; //Score
    int xRed, yRed; //Position
    JLabel red; //To display racket
    int redDir; //Direction racket is going (vertical)

    //Variables for green player
    int greenPoint; //Score
    int xGreen, yGreen; //Position
    JLabel green; //To display racket
    int greenDir; //Direction racket is going (vertical)

    //Variables for the ball
    int xBall, yBall; //Position
    int xDir, yDir; //Direction (horizontal and vertical)
    int ballSpeed; //Speed of ball
    JLabel ball; //To display the ball

    /**
     * Reset game
     */
    public void reset() {
        xRed = 0 + _RACKET_WIDTH + _PADDING_RACKET;
        xGreen = _PANEL_WIDTH - _RACKET_WIDTH - _PADDING_RACKET - 12;
        yRed = yGreen = (_PANEL_HEIGHT) / 2 - _RACKET_HEIGHT;

        xBall = (_PANEL_WIDTH) / 2 - _BALL_SIZE;
        yBall = (_PANEL_HEIGHT - _BALL_SIZE) / 2 - _BALL_SIZE;
        ballSpeed = 15;
        time = 0;
        keys = new ArrayList<>();
    }

    /**
     * Reset points
     */
    private void resetPoint() {
        redPoint = 0;
        greenPoint = 0;
    }

    /**
     * Initialize game panel (panel to draw objects like rackets and ball)
     */
    void initScene() {

        red = new JLabel();
        red.setOpaque(true);
        red.setBackground(Color.red);
        red.setBounds(xRed, yRed, _RACKET_WIDTH, _RACKET_HEIGHT);
        redDir = 0;

        green = new JLabel();
        green.setOpaque(true);
        green.setBackground(Color.green);
        green.setBounds(xGreen, yRed, _RACKET_WIDTH, _RACKET_HEIGHT);
        greenDir = 0;

        ball = new JLabel();
        ball.setOpaque(true);
        ball.setBackground(Color.white);
        ball.setBounds(xBall, yBall, _BALL_SIZE, _BALL_SIZE);
        ball.setIcon(new ImageIcon(getClass().getResource("/Images/sun-48189.png")));

        //Min + (int)(Math.random() * ((Max - Min) + 1))
        int random = -1 + (int) (Math.random() * ((1 - -1) + 1));
        System.out.println(random);
        xDir = (random == 0) ? 1 : random;
        yDir = (random == 0) ? -1 : random;
        this.gamePanel.add(red);
        this.gamePanel.add(green);
        this.gamePanel.add(ball);
        this.gamePanel.repaint();
    }

    /**
     * Check if a racket can move in the current direction, if yes, return the
     * new y coordinate, else return the current coordinate
     *
     * @param racket The racket to check
     * @return Y coordinate of racket(vertical position)
     */
    public int racketMovement(int racket) {
        switch (racket) {
            case _RED_RACKET:
                if (yRed + (redDir * _RACKET_SPEED) > _PANEL_HEIGHT + _RACKET_SPEED) {
                    return yRed;
                } else if (yRed + (redDir * _RACKET_SPEED) < 0) {
                    return yRed;
                } else {
                    return yRed += (redDir * _RACKET_SPEED);
                }
            case _GREEN_RACKET:
                if (yGreen + (greenDir * _RACKET_SPEED) > _PANEL_HEIGHT + _RACKET_SPEED) {
                    return yGreen;
                } else if (yGreen + (greenDir * _RACKET_SPEED) < 0) {
                    return yGreen;
                } else {
                    return yGreen += (greenDir * _RACKET_SPEED);
                }
        }
        return 0;
    }

    /**
     * Move a racket
     *
     * @param racket Racket to move
     */
    void moveRacket(int racket) {
        switch (racket) {
            case _RED_RACKET:
                yRed = racketMovement(racket);
                red.setLocation(xRed, yRed);
                break;
            case _GREEN_RACKET:
                yGreen = racketMovement(racket);
                green.setLocation(xGreen, yGreen);
                break;
        }
    }

    /**
     * Check if a game is over
     *
     * @return
     */
    public boolean isGameOver() {
        return xBall <= 0 || xBall >= _PANEL_WIDTH;
    }

    /**
     * Check if any of the 2 racket hits the ball
     *
     * @return true if yes, false if no
     */
    public boolean willRacketHitBall() {
        int ballRadius = _BALL_SIZE / 2;

        //RED
        int bXR = xBall;
        int bYR = yBall + ballRadius;

        int rX = xRed + _RACKET_WIDTH; //Point of impact
        int rY = yRed;
        if (Math.abs(bXR - rX) <= ballRadius) {
            if (yRed - ballRadius <= bYR && bYR <= yRed + _RACKET_HEIGHT + ballRadius) {
                return true;
            }
        }
        //GREENS
        int bXG = xBall + ballRadius;
        int bYG = yBall + ballRadius;

        int gX = xGreen; //Point of impact
        int gY = yGreen;
        if (Math.abs(bXG - gX) <= ballRadius) {
            if (gY - ballRadius <= bYG && bYG <= gY + _RACKET_HEIGHT + ballRadius) {
                return true;
            }
        }
        return false;
    }

    /**
     * Perform operations for when game is over (set winner message, increase
     * point, reset variables)
     */
    public void gameOver() {
        lblMessage.setOpaque(true);
        if (xBall <= 0) {
            lblMessage.setForeground(Color.GREEN);
            lblMessage.setBackground(Color.WHITE);
            lblMessage.setText("GREEN PLAYER IS THE WINNER! PRESS SPACE OR PRESS BUTTON TO PLAY AGAIN");
            greenPoint++;
        }
        if (xBall >= _PANEL_WIDTH) {
            lblMessage.setForeground(Color.RED);
            lblMessage.setBackground(Color.WHITE);
            lblMessage.setText("RED PLAYER IS THE WINNER! PRESS SPACE OR PRESS BUTTON TO PLAY AGAIN");
            redPoint++;
        }
        setPoint();
        reset();
        timer.stop();
        gameThread.stop();
    }

    /**
     * Set text for score labels
     */
    private void setPoint() {
        ptRed.setText(redPoint + "");
        ptGreen.setText(greenPoint + "");
    }

    /**
     * Run the game
     */
    void runGame() {
        gameThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    red.setBounds(xRed, yRed, _RACKET_WIDTH, _RACKET_HEIGHT);
                    green.setBounds(xGreen, yGreen, _RACKET_WIDTH, _RACKET_HEIGHT);

                    xBall += ballSpeed * xDir;
                    yBall += ballSpeed * yDir;

                    if (isGameOver()) {
                        timer.stop();
                        JOptionPane.showMessageDialog(null, "Game over!");
                        gameOver();
                    }
                    if (willRacketHitBall()) {
                        xDir *= -1;
                    }
//                    //BALL VERTICAL
                    if (yBall < 0) {
                        yBall = 0;
                        yDir *= -1;
                    } else if (yBall > _PANEL_HEIGHT + _BALL_SIZE) {
                        yBall = _PANEL_HEIGHT + _BALL_SIZE;
                        yDir *= -1;
                    }
                    ball.setBounds(xBall, yBall, _BALL_SIZE, _BALL_SIZE);

                    for (int i = 0; i < keys.size(); i++) {
                        switch (keys.get(i)) {
                            case KeyEvent.VK_W:
                                redDir = - 1;
                                moveRacket(_RED_RACKET);
                                break;
                            case KeyEvent.VK_S:
                                redDir = 1;
                                moveRacket(_RED_RACKET);
                                break;
                            case KeyEvent.VK_UP:
                                greenDir = -1;
                                moveRacket(_GREEN_RACKET);
                                break;
                            case KeyEvent.VK_DOWN:
                                greenDir = 1;
                                moveRacket(_GREEN_RACKET);
                                break;
                        }
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PingPong.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        };
        gameThread.start();
    }

    @Override
    public void keyPressed(KeyEvent k) {
        if (k.getKeyCode() == KeyEvent.VK_SPACE) {
            gameOver();
            startPlaying();
        } else if (!keys.contains(k.getKeyCode())) {
            keys.add(k.getKeyCode());
        }

    }

    @Override
    public void keyReleased(KeyEvent k) {
        if (keys.contains(k.getKeyCode())) {
            keys.remove(keys.indexOf(k.getKeyCode()));
        }
    }

    /**
     * Creates new form PingPong
     */
    public PingPong() {
        initComponents();
        this.replay.setFocusable(false);
        this.setSize(new Dimension(812, 600));
        this.setResizable(false);
        this.setLocationRelativeTo(null); //Put this frame into center of the screen
        gamePanel.setSize(new Dimension(_PANEL_WIDTH, _PANEL_HEIGHT));
        resetPoint();
        startPlaying();
        this.addKeyListener(this);

    }

    private void startPlaying() {
        if (red != null) {
            gamePanel.remove(red);
        }
        if (green != null) {
            gamePanel.remove(green);
        }
        if (ball != null) {
            gamePanel.remove(ball);
        }
        this.gamePanel.repaint();
        reset();
        initScene();
        this.clock.setText("00:00:00");
        timer = new Clock(this.clock);
        this.runGame();
        timer.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gameInfo = new javax.swing.JPanel();
        lblRed = new javax.swing.JLabel();
        lblGreen = new javax.swing.JLabel();
        ptRed = new javax.swing.JLabel();
        ptGreen = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        clock = new javax.swing.JLabel();
        replay = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        lblMessage = new javax.swing.JLabel();
        gamePanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(812, 600));

        gameInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Game information"));
        gameInfo.setPreferredSize(new java.awt.Dimension(100, 100));

        lblRed.setForeground(new java.awt.Color(255, 0, 0));
        lblRed.setText("Red: ");

        lblGreen.setForeground(new java.awt.Color(51, 204, 0));
        lblGreen.setText("Green:");

        ptRed.setForeground(new java.awt.Color(255, 0, 0));
        ptRed.setText("0");

        ptGreen.setForeground(new java.awt.Color(51, 204, 0));
        ptGreen.setText("0");

        jLabel1.setText("Time");

        clock.setText("00:00:00");

        replay.setText("PLAY AGAIN");
        replay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replayActionPerformed(evt);
            }
        });

        jButton1.setText("RESET POINTS");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout gameInfoLayout = new javax.swing.GroupLayout(gameInfo);
        gameInfo.setLayout(gameInfoLayout);
        gameInfoLayout.setHorizontalGroup(
            gameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRed)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ptRed)
                .addGap(66, 66, 66)
                .addComponent(lblGreen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ptGreen)
                .addGap(52, 52, 52)
                .addGroup(gameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMessage)
                    .addGroup(gameInfoLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clock)
                        .addGap(105, 105, 105)
                        .addComponent(replay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gameInfoLayout.setVerticalGroup(
            gameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gameInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gameInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRed)
                    .addComponent(lblGreen)
                    .addComponent(ptRed)
                    .addComponent(ptGreen)
                    .addComponent(jLabel1)
                    .addComponent(clock)
                    .addComponent(replay)
                    .addComponent(jButton1))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(lblMessage)
                .addContainerGap())
        );

        gamePanel.setBackground(new java.awt.Color(255, 255, 255));
        gamePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        gamePanel.setPreferredSize(new java.awt.Dimension(800, 400));
        gamePanel.setLayout(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(gamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gameInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gameInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gamePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void replayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replayActionPerformed
        gameOver();
        startPlaying();
    }//GEN-LAST:event_replayActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        timer.reset();
        resetPoint();
        setPoint();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(PingPong.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PingPong.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PingPong.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PingPong.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PingPong().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel clock;
    private javax.swing.JPanel gameInfo;
    private javax.swing.JPanel gamePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblGreen;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblRed;
    private javax.swing.JLabel ptGreen;
    private javax.swing.JLabel ptRed;
    private javax.swing.JButton replay;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
