import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;
    private Controls controls;

    public GameFrame() {
        setTitle("Kuş Vurmaca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        mainMenuPanel = new MainMenuPanel(this);
        add(mainMenuPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public void dispose() {
        super.dispose();
        SoundUtils.stopBackgroundMusic();
    }
    public void startGame(String username) {
        remove(mainMenuPanel);

        // Önce GamePanel nesnesini oluşturun
        gamePanel = new GamePanel(username);

        // Sonra Controls nesnesini oluşturun ve gamePanel'i geçirin
        controls = new Controls(gamePanel);

        // Controls nesnesini gamePanel'e ayarlayın
        gamePanel.setControls(controls);

        setLayout(new BorderLayout());
        add(gamePanel, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        validate();
        repaint();
        gamePanel.requestFocusInWindow();
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameFrame());
    }
}
