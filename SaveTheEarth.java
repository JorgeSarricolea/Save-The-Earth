import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SaveTheEarth extends JFrame {
    private JLabel earthLabel;
    private List<JLabel> meteorList;
    private int destroyedCount;
    private int crashedCount;
    private JLabel countLabel;
    private JLayeredPane layeredPane;
    private boolean gameOver;

    public SaveTheEarth() {
        setTitle("Save the Earth!");
        setSize(879, 485);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        meteorList = new ArrayList<>();
        destroyedCount = 0;
        crashedCount = 0;
        gameOver = false;

        // Timer to create meteors at regular intervals
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    createMeteor();
                }
            }
        });
        timer.start();

        // Mouse listener to check for collisions when the mouse is clicked
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                checkCollision(e.getX(), e.getY());
            }
        });

        // Add the image of the earth to the background
        earthLabel = new JLabel(new ImageIcon("assets/earth.jpg"));
        earthLabel.setBounds(0, 0, 879, 485);
        layeredPane.add(earthLabel, JLayeredPane.DEFAULT_LAYER);

        // Sets the Earth image overlay order
        layeredPane.setLayer(earthLabel, 0);

        // Create and add the JLabel to display the count in the top left corner
        countLabel = new JLabel("Destroyed: 0 | Crashed: 0");
        countLabel.setForeground(Color.WHITE);
        countLabel.setBounds(10, 10, 300, 20);
        layeredPane.add(countLabel, JLayeredPane.PALETTE_LAYER);
    }

    // Method to create a new meteor
    private void createMeteor() {
        Random rand = new Random();
        int x = rand.nextInt(800);

        JLabel meteorLabel = new JLabel(new ImageIcon("assets/meteor.png"));
        meteorLabel.setBounds(x, 0, 100, 100);
        layeredPane.add(meteorLabel, JLayeredPane.PALETTE_LAYER);
        meteorList.add(meteorLabel);

        // Thread to move the meteor downward
        Thread moveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int y = 0;
                while (y < 300 && !gameOver) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    y += 5;
                    meteorLabel.setBounds(x, y, 100, 100);
                }

                if (!gameOver && meteorList.contains(meteorLabel)) {
                    crashedCount++;
                    remove(meteorLabel);
                    meteorList.remove(meteorLabel);
                    updateCountLabel();

                    if (crashedCount == 3) {
                        showGameOver();
                    }
                }
            }
        });
        moveThread.start();
    }

    // Method to check for collisions with the mouse click
    private void checkCollision(int mouseX, int mouseY) {
        List<JLabel> meteorsToRemove = new ArrayList<>();

        for (JLabel meteorLabel : meteorList) {
            Rectangle meteorBounds = meteorLabel.getBounds();
            if (meteorBounds.contains(mouseX, mouseY)) {
                destroyedCount++;
                meteorsToRemove.add(meteorLabel);
            }
        }

        for (JLabel meteorLabel : meteorsToRemove) {
            layeredPane.remove(meteorLabel);
            meteorList.remove(meteorLabel);
        }

        updateCountLabel();
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    // Method to update the count label
    private void updateCountLabel() {
        countLabel.setText("Destroyed: " + destroyedCount + " | Crashed: " + crashedCount);
    }

    // Method to display the "Game Over" message and handle the user's choice
    private void showGameOver() {
        int choice = JOptionPane.showOptionDialog(
                this,
                "Game Over",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Play Again"},
                "Play Again");

        if (choice == 0) {
            resetGame();
        } else {
            // Handle other choices if needed
        }
    }

    // Method to reset the game state
    private void resetGame() {
        destroyedCount = 0;
        crashedCount = 0;
        updateCountLabel();
        gameOver = false;

        // Eliminate all remaining meteorites
        for (JLabel meteorLabel : meteorList) {
            layeredPane.remove(meteorLabel);
        }
        meteorList.clear();

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SaveTheEarth().setVisible(true);
            }
        });
    }
}
