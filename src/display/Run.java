package display;

import org.opencv.core.Core;

public class Run
{
    public static void main(String[] args)
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new Scene();
    }
}