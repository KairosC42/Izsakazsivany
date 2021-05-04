package com.csapat.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PauseMenu extends JPanel implements ChangeListener, KeyListener {
    static final int VOLUME_MIN = 0;
    static final int VOLUME_MAX = 100;
    private final JPanel container;
    Renderer.IntWrapper value;

    static JSlider volume;

    public PauseMenu(Renderer.IntWrapper volumeLevel) {
        container = new JPanel();
        JLabel pauseText = new JLabel();
        pauseText.setText("<html><body>The game is currently paused. <br> You can set the volume with this slider! ></body></html>");
        pauseText.setVisible(true);
        this.add(pauseText);
        volume = new JSlider(JSlider.CENTER, VOLUME_MIN, VOLUME_MAX, volumeLevel.getValue());
        volume.addChangeListener(this);
        volume.setMajorTickSpacing(10);
        volume.setMinorTickSpacing(1);
        volume.setPaintTicks(true);
        volume.setPaintLabels(true);
        volume.setSize(200, 50);
        volume.setBounds(200,200, 200, 50);
        volume.setEnabled(true);
        volume.setPreferredSize(new Dimension(200, 50));
        value = volumeLevel;
        //this should be a shallow copy
       // container.setBackground(Color.GRAY);
       // container.add(volume);
        volume.addKeyListener(this);
        this.add(volume);
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            value.setValue(source.getValue());
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar()==KeyEvent.VK_ESCAPE)
        {
            this.setVisible(false);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


}
