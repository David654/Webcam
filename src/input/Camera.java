package input;

import data.Constants;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.Scanner;

public class Camera implements Runnable
{
    private final VideoCapture videoCapture;
    public BufferedImage image;

    public Camera()
    {
        videoCapture = new VideoCapture();
        image = new BufferedImage(Constants.width, Constants.height, BufferedImage.TYPE_INT_RGB);

        videoCapture.open(0);
        setup();

        Thread thread = new Thread(this);
        thread.start();
    }

    private void setup()
    {
        videoCapture.set(Videoio.CAP_PROP_FRAME_WIDTH, 100000);
        videoCapture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 100000);
        videoCapture.set(Videoio.CAP_PROP_FPS, 1000);

        System.out.println("Resolution: " + videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH) + " x " + videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
        System.out.println("FPS: " + videoCapture.get(Videoio.CAP_PROP_FPS));
    }

    public void getImage()
    {
        Mat frame = new Mat();
        videoCapture.read(frame);
        if(videoCapture.isOpened())
        {
            if(videoCapture.read(frame))
            {
                Core.flip(frame, frame, 1);
                // Imgproc.GaussianBlur(frame, frame, new Size(41, 41), 0, 0);

                image = MatToBufferedImage(frame);
            }
        }
    }

    private BufferedImage MatToBufferedImage(Mat frame)
    {
        int type = 0;
        if(frame.channels() == 1) type = BufferedImage.TYPE_BYTE_GRAY;
        else if(frame.channels() == 3) type = BufferedImage.TYPE_3BYTE_BGR;

        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }

    private void changeCameraSettings()
    {
        Scanner sc = new Scanner(System.in);
        String cmd = sc.nextLine();
        if(cmd.contains("width="))
        {
            int value = Integer.parseInt(cmd.substring(cmd.indexOf("width=")));
            videoCapture.set(Videoio.CAP_PROP_FRAME_WIDTH, value);
        }
        else if(cmd.contains("height="))
        {
            int value = Integer.parseInt(cmd.substring(cmd.indexOf("height=")));
            videoCapture.set(Videoio.CAP_PROP_FRAME_HEIGHT, value);
        }
        else if(cmd.contains("fps="))
        {
            int value = Integer.parseInt(cmd.substring(cmd.indexOf("fps=")));
            videoCapture.set(Videoio.CAP_PROP_FPS, value);
        }
    }

    public void run()
    {
        while(true)
        {
            getImage();
        }
    }
}