import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainMenuPanel extends JPanel {
    private final JTextField usernameField;
    private final JButton startButton;
    private final JButton resetScoresButton;
    private final Image backgroundImage;
    private final JLabel titleLabel;
    private final JLabel usernameLabel;
    private final JTextArea highScoresArea;

    public MainMenuPanel(GameFrame gameFrame) {
        setPreferredSize(new Dimension(1200, 600));
        setLayout(null);
        SoundUtils.playBackgroundMusic("gamebackground.wav");

        // Load background image
        backgroundImage = new ImageIcon("background.png").getImage();

        // Title label
        titleLabel = new JLabel("Kuş Vurmaca");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 72));
        titleLabel.setBounds(0, 90, 1200, 80);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(0, 102, 204));
        add(titleLabel);

        // Username label
        usernameLabel = new JLabel("Kullanıcı Adı:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameLabel.setBounds(450, 230, 300, 30);
        usernameLabel.setForeground(Color.BLACK);
        add(usernameLabel);

        // Username field
        usernameField = new JTextField();
        usernameField.setBounds(450, 260, 300, 40);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 18));
        usernameField.setHorizontalAlignment(JTextField.CENTER);
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        add(usernameField);

        // Start button
        startButton = new JButton("Başla");
        startButton.setBounds(525, 310, 150, 40);
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setBackground(new Color(0, 153, 0));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createEmptyBorder());
        add(startButton);

        // Reset Scores button
        resetScoresButton = new JButton("Skorları Sıfırla");
        resetScoresButton.setBounds(475, 535, 250, 40);
        resetScoresButton.setFont(new Font("Arial", Font.BOLD, 18));
        resetScoresButton.setBackground(new Color(255, 69, 0)); // Kırmızı renkte
        resetScoresButton.setForeground(Color.WHITE);
        resetScoresButton.setFocusPainted(false);
        resetScoresButton.setBorder(BorderFactory.createEmptyBorder());
        add(resetScoresButton);

        // High Scores Area
        highScoresArea = new JTextArea();
        highScoresArea.setBounds(450, 370, 300, 150);
        highScoresArea.setFont(new Font("Arial", Font.PLAIN, 16));
        highScoresArea.setEditable(false);
        highScoresArea.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        add(highScoresArea);

        // Load and display high scores
        loadHighScores();

        // Button actions
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                if (!username.isEmpty()) {
                    gameFrame.startGame(username);
                } else {
                    JOptionPane.showMessageDialog(MainMenuPanel.this, "Kullanıcı adınızı giriniz!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        resetScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(MainMenuPanel.this, "Skorları sıfırlamak istediğinizden emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    resetHighScores();
                    loadHighScores();
                }
            }
        });
    }

    private void loadHighScores() {
        ArrayList<HighScores.ScoreEntry> topScores = HighScores.getTopScores("scores.txt", 5);
        StringBuilder highScoresText = new StringBuilder("En İyi Skorlar:\n");
        for (HighScores.ScoreEntry score : topScores) {
            highScoresText.append(score).append(" sn\n");
        }
        highScoresArea.setText(highScoresText.toString());
    }

    private void resetHighScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt", false))) {
            // Writing an empty file to clear the scores
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw a translucent overlay for better readability
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setColor(new Color(255, 255, 255));
        g2d.fillRect(400, 210, 400, 380);
        g2d.dispose();
    }
}
