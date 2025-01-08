import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private MainMenuPanel mainMenuPanel;
    private GamePanel gamePanel;
    private Controls controls;

    public GameFrame() {
        setTitle("kgmDuck");
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

        gamePanel = new GamePanel(username);

        controls = new Controls(gamePanel);

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
