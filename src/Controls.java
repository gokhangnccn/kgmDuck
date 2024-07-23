import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Controls extends JPanel {
    private JSlider angleSlider;
    private JSlider speedSlider;
    private JLabel angleLabel;
    private JLabel speedLabel;

    public Controls() {
        angleSlider = new JSlider(30, 90, 45);
        speedSlider = new JSlider(30, 100, 50);

        angleLabel = new JLabel("Angle: " + angleSlider.getValue());
        speedLabel = new JLabel("Speed: " + speedSlider.getValue());

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
}