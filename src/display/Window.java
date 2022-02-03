package display;

import data.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Window implements ComponentListener
{
    private final JFrame frame;
    private final Scene scene;

    public Window(String title, int width, int height, Scene scene)
    {
        this.scene = scene;

        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(scene);
        frame.pack();

        frame.addComponentListener(this);

        scene.start();
    }

    public void show()
    {
        frame.setVisible(true);
    }

    public void setTitle(String title)
    {
        frame.setTitle(title);
    }

    public void componentResized(ComponentEvent e)
    {
        Constants.width = frame.getWidth();
        Constants.height = frame.getHeight();
        scene.setPreferredSize(new Dimension(Constants.width, Constants.height));
    }

    public void componentMoved(ComponentEvent e) {}
    public void componentShown(ComponentEvent e) {}
    public void componentHidden(ComponentEvent e) {}
}