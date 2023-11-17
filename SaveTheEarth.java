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

    public SaveTheEarth() {
        setTitle("Save the Earth!");
        setSize(879, 485);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        meteorList = new ArrayList<>();
        destroyedCount = 0;
        crashedCount = 0;

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createMeteor();
            }
        });
        timer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                checkCollision(e.getX(), e.getY());
            }
        });

        // Add the image of the earth to the background
        earthLabel = new JLabel(new ImageIcon("earth.jpg"));
        earthLabel.setBounds(0, 0, 879, 485);
        layeredPane.add(earthLabel, JLayeredPane.DEFAULT_LAYER);

        // Sets the Earth image overlay order
        layeredPane.setLayer(earthLabel, 0);

        // Create and add the JLabel to display the count in the top left corner
        countLabel = new JLabel("Destroyed: 0 | (Crashed: 0");
        countLabel.setForeground(Color.WHITE);
        countLabel.setBounds(10, 10, 300, 20);
        layeredPane.add(countLabel, JLayeredPane.PALETTE_LAYER);
    }

    private void createMeteor() {
      Random rand = new Random();
      int x = rand.nextInt(800);
      int[] y = {0}; // Array of size 1 to store the value of y

      JLabel meteorLabel = new JLabel(new ImageIcon("assets/meteor.png"));
      meteorLabel.setBounds(x, y[0], 100, 100);
      layeredPane.add(meteorLabel, JLayeredPane.PALETTE_LAYER);
      meteorList.add(meteorLabel);

      Timer moveTimer = new Timer(50, new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              y[0] += 5;
              meteorLabel.setBounds(x, y[0], 100, 100);

              if (y[0] > 300) {
                  // Check if the meteorite reaches position 300 in y
                  if (y[0] >= 300) {
                      // Check if the meteorite is still in the list before incrementing crashedCount
                      if (meteorList.contains(meteorLabel)) {
                          crashedCount++;
                      }
                  }

                  remove(meteorLabel);
                  meteorList.remove(meteorLabel);
                  ((Timer) e.getSource()).stop();
                  updateCountLabel(); // Update the JLabel of the content
              }
          }
      });
      moveTimer.start();
    }

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
          layeredPane.remove(meteorLabel);  // Remove the meteor directly from the JLayeredPane
          meteorList.remove(meteorLabel);
      }

      updateCountLabel(); // Update the tag after removing the meteorites
      layeredPane.revalidate(); // Ensures the interface is updated correctly
      layeredPane.repaint(); // Repaint the interface to reflect the changes
    }

    private void updateCountLabel() {
        countLabel.setText("Destroyed: " + destroyedCount + " | Crashed: " + crashedCount + "");
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
