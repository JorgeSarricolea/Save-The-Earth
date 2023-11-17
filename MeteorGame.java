import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MeteorGame extends JFrame {
    private JLabel earthLabel;
    private List<JLabel> meteorList;
    private int destroyedCount;
    private int crashedCount;
    private JLabel countLabel;
    private JLayeredPane layeredPane;

    public MeteorGame() {
        setTitle("Meteor Game");
        setSize(879, 485);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        meteorList = new ArrayList<>();
        destroyedCount = 0;
        crashedCount = 0; // Inicializa el nuevo contador

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

        // Agrega la imagen de la tierra al fondo
        earthLabel = new JLabel(new ImageIcon("tierra.jpg"));
        earthLabel.setBounds(0, 0, 879, 485);
        layeredPane.add(earthLabel, JLayeredPane.DEFAULT_LAYER);

        // Establece el orden de superposición de la imagen de la Tierra
        layeredPane.setLayer(earthLabel, 0);

        // Crea y agrega el JLabel para mostrar el conteo en la esquina superior izquierda
        countLabel = new JLabel("Destroyed: 0 | (Crashed: 0");
        countLabel.setForeground(Color.WHITE);
        countLabel.setBounds(10, 10, 300, 20);
        layeredPane.add(countLabel, JLayeredPane.PALETTE_LAYER);
    }

    private void createMeteor() {
        Random rand = new Random();
        int x = rand.nextInt(800);
        int[] y = {0}; // Array de un tamaño 1 para almacenar el valor de y

        JLabel meteorLabel = new JLabel(new ImageIcon("meteor_100x100.png"));
        meteorLabel.setBounds(x, y[0], 100, 100);
        layeredPane.add(meteorLabel, JLayeredPane.PALETTE_LAYER);
        meteorList.add(meteorLabel);

        Timer moveTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                y[0] += 5;
                meteorLabel.setBounds(x, y[0], 100, 100);

                if (y[0] > 300) {
                    // Verifica si el meteorito llega a la posición 350 en y
                    if (y[0] >= 300) {
                      crashedCount++;
                    }

                    remove(meteorLabel);
                    meteorList.remove(meteorLabel);
                    ((Timer) e.getSource()).stop();
                    updateCountLabel(); // Actualiza el JLabel del conteo
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
                updateCountLabel(); // Actualiza el JLabel del conteo
            }
        }

        for (JLabel meteorLabel : meteorsToRemove) {
            remove(meteorLabel);
            meteorList.remove(meteorLabel);
        }
    }

    private void updateCountLabel() {
        countLabel.setText("Destroyed: " + destroyedCount + " | Crashed: " + crashedCount + "");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MeteorGame().setVisible(true);
            }
        });
    }
}
