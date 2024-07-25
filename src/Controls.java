import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Controls extends JPanel {
    private JSlider angleSlider;
    private JSlider speedSlider;
    private JLabel angleLabel;
    private JLabel speedLabel;
    private GamePanel gamePanel;

    public Controls(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        angleSlider = new JSlider(30, 90, 45);
        speedSlider = new JSlider(30, 100, 50);

        angleLabel = new JLabel("Angle: " + angleSlider.getValue());
        speedLabel = new JLabel("Speed: " + speedSlider.getValue());

        angleSlider.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                gamePanel.setFocusable(true);
                gamePanel.requestFocusInWindow();
            }
        });

        speedSlider.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                gamePanel.setFocusable(true);
                gamePanel.requestFocusInWindow();
            }
        });

        angleSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                angleLabel.setText("Angle: " + angleSlider.getValue());
            }
        });

        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                speedLabel.setText("Speed: " + speedSlider.getValue());
            }
        });

        setLayout(new GridLayout(2, 2));
        add(angleLabel);
        add(angleSlider);
        add(speedLabel);
        add(speedSlider);
    }

    public int getAngle() {
        return angleSlider.getValue();
    }

    public double getSpeed() {
        return speedSlider.getValue();
    }

    public void incrementAngle() {
        angleSlider.setValue(Math.min(angleSlider.getValue() + 1, angleSlider.getMaximum()));
    }

    public void decrementAngle() {
        angleSlider.setValue(Math.max(angleSlider.getValue() - 1, angleSlider.getMinimum()));
    }

    public void incrementSpeed() {
        speedSlider.setValue(Math.min(speedSlider.getValue() + 1, speedSlider.getMaximum()));
    }

    public void decrementSpeed() {
        speedSlider.setValue(Math.max(speedSlider.getValue() - 1, speedSlider.getMinimum()));
    }
}