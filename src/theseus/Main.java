package theseus;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import theseus_data.MyJButton;
import theseus_data.StartEnd;
import theseus_data.Structure;
import theseus_data.Test;
import theseus_data.TwoD;
import theseus_data.MyThread;

public class Main
{
	// Erzeugt Datenfelder
	public static MyThread thread = new MyThread();
	public static Window window = new Window();
	public static ImageAnalysis analysis;
	public static Structure structure;
	public static TwoD twoD;
	public static Test test = new Test();
	public static BufferedImage bImage;
	public static StartEnd startEnd;

	public static ArrayList<MyJButton> buttonStorage = new ArrayList<MyJButton>(0);

	// Main-Methode
	public static void main(String[] args)
	{
	}

	// Gibt ArrayList mit Buttons zurück
	public static ArrayList<MyJButton> getButtonStorage()
	{
		return buttonStorage;
	}

}
