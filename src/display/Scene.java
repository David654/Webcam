package display;

import data.Constants;
import input.Camera;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Scene extends Canvas implements Runnable
{
    private final Window window;
    private Thread thread;
    private boolean running = false;

    private final Camera camera;

    public Scene()
    {
        this.setPreferredSize(new Dimension(Constants.width, Constants.height));

        camera = new Camera();

        window = new Window(Constants.title, Constants.width, Constants.height, this);
        window.show();
    }

    public synchronized void start()
    {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop()
    {
        try
        {
            thread.join();
            running = false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = Constants.tickRate;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1)
            {
                tick();
                delta--;
            }
            if(running)
            {
                render();
            }
            frames++;

            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                window.setTitle(Constants.title + " | " + frames + " fps");
                frames = 0;
            }
        }
        stop();
    }

    private void tick()
    {

    }

    private void render()
    {
        BufferStrategy bs = this.getBufferStrategy();

        if(bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Constants.bg);
        g2d.fillRect(0, 0, Constants.width, Constants.height);

        g2d.drawImage(camera.image, 0, 0, Constants.width, Constants.height, null);

        g2d.dispose();
        bs.show();
    }
}