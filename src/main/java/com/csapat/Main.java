package com.csapat;

import com.csapat.gui.MainWindow;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Main extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private ImageIcon image;
    private JPanel pan;

    public Main() {
        super("Izsák a Zsivány");

        image = new ImageIcon(this.getClass().getClassLoader().getResource("izsakMain.png"));
        Image scaledImage = image.getImage().getScaledInstance(900, 600, Image.SCALE_FAST);
        pan = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(image.getImage(), 0, 0, 901, 601, null);
            }
        };
        pan.setPreferredSize(new Dimension(900, 600));

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(pan, BorderLayout.CENTER);

        Component mouseClick = new MyComponent(this);
        addMouseListener((MouseListener) mouseClick);


        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


    public class MyComponent extends JComponent implements MouseListener {

        Main parent;
        MyComponent(Main parent)
        {
            this.parent=parent;
        }
        @Override
        public void mouseClicked(MouseEvent arg0) {
            MainWindow gue = new MainWindow();
            parent.setVisible(false);
            //parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }


    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }
}
