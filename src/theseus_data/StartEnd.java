package theseus_data;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import theseus.Main;

/**
 * Ermittelt durch Algorithmen die Position der roten Start- und Zielpunkte
 * @author Julian Sakowski
 *
 */
public class StartEnd {

	/**Speichert das Bild*/
	private static final BufferedImage bImage = Main.bImage;

	/**Speichert die Bildbreite*/
	private static final int x = bImage.getWidth();
	/**Speichert die Bildhöhe*/
	private static final int y = bImage.getHeight();

	/**1. Minimale Farbtonwert für die Farbe Rot*/
	private static final float redMinHue1 = 0.917f;
	/**2. Minimale Farbtonwert für die Farbe Rot*/
	private static final float redMinHue2 = 0.0f;
	/**1. Maximale Farbtonwert für die Farbe Rot*/
	private static final float redMaxHue1 = 0.99999999f;
	/**2. Maximale Farbtonwert für die Farbe Rot*/
	private static final float redMaxHue2 = 0.042f;

	/**Minimaler Sättigungswert für die Farbe Rot*/
	private static final float redMinSat = 0.625f;
	/**Maximaler Sättigungswert für die Farbe Rot*/
	private static final float redMaxSat = 1.0f;

	/**Minimaler Helligkeitswert für die Farbe Rot*/
	private static final float redMinVal = 0.208f;
	/**Maximaler Helligkeitswert für die Farbe Rot*/
	private static final float redMaxVal = 1.0f;

	/**Geschätzte Pixelanzahl EINES Kreises*/
	private static double amount = 0;
	/**Eine Schätzung des Radius vom Startpunkt*/
	private static int radius = 0;
	/**Die maximale Distanz zwischen 2 Pixeln, ohne dass diese zu einem neuen Label gezählt werden*/
	private static double maxDistance = 0;
	/**Speichert die Koordinaten für die Startposition*/
	private static long start = 0;
	/**Speichert die Koordinaten für die Endposition*/
	private static long end = 0;

	/**Enthält alle PixelPosition-Elemente*/
	public static ArrayList<PixelPosition> positions = new ArrayList<PixelPosition>(0);

	/**
	 * Konstruktor
	 */
	public StartEnd() {
		findPositions();
		initAmount();
		initRadius();
		initMaxDistance();
		setPixelToLabels();
//		printArray();
		start = setStartEndNew(1);
		end = setStartEndNew(2);
	}	
	
	/**
	 * // Gibt das Array aus - Testweise
	private void printArray() {
		for (int i = 0; i < positions.size(); i++) {
				System.out.println("I: " + i + " X: " + positions.get(i).getX()
						+ " Y: " + positions.get(i).getY() + "LabelIndex: "
						+ positions.get(i).getLabel());	
		}
	}*/

	/**
	 * Sucht die roten Pixel
	 */
	private static void findPositions() {
		//durchlaufe alle Bildpixel
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x; j++) {
				int rgb = bImage.getRGB(j, i);
				Color color = new Color(rgb);

				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();

				float[] hsv = Color.RGBtoHSB(red, green, blue, null);
				float hue = hsv[0];
				float sat = hsv[1];
				float val = hsv[2];				

				// Auf Farbton prüfen
				if (hue >= redMinHue1 && hue <= redMaxHue1 || hue >= redMinHue2
						&& hue <= redMaxHue2) {
					// Auf Sättigung prüfen
					if (sat >= redMinSat && sat <= redMaxSat) {
						// Auf Helligkeit prüfen
						if (val >= redMinVal && val <= redMaxVal) {
							// Pixel erfüllt die Anforderungen und wird dem Array hinzugefügt
							positions.add(new PixelPosition(j, i, 0));
						}
					}
				}
			}
		}
	}

	/**
	 * initiaslisiert das amount Datenfeld
	 */
	private void initAmount() {
		amount = positions.size() / 2;
	}

	/**
	 * schätz den Radius der Start- und Zielkreise
	 */
	private void initRadius() {
		// r = Wurzel(A / Pi)
		radius = (int) Math.ceil(Math.sqrt(amount / Math.PI));
	}

	/**
	 * intitialisiert das maxDistance Datenfeld
	 */
	private void initMaxDistance() {
		//berechnet den Durchmesser + 0.5 Toleranz
		maxDistance = 2.5 * radius;
	}

	/**
	 * fügt jedem Pixel ein Labelindex zu
	 */
	private void setPixelToLabels() {
		try{
		// Label 1 beginnt mit dem ersten roten Pixel => Initialpixel
		positions.get(0).setLabel(1);
		}catch (Exception e){
			// error window
			JOptionPane.showMessageDialog(null,
			    "Bitte starten Sie das Programm erneut. In der Zeichnung konnten keine roten Kreise als Start- und Endpunkte indentifiziert werden. ",
			    "Es wurde keine Start- bzw. Endpunkt gefunden!",
			    JOptionPane.ERROR_MESSAGE);
		}
		
		// Iteriert die Pixel
		Iterator<PixelPosition> it = positions.iterator();
		while (it.hasNext()) {
			PixelPosition element = it.next();
			
			// Speicher Koordinaten des Initialpixels
			int labelX = positions.get(0).getX();
			int labelY = positions.get(0).getY();
			
			// Speicher Koordinaten des zu betrachndenen Pixels 
			int pixelX = element.getX();
			int pixelY = element.getY();
			
			// errechne die euklidische Distanz zwischen dem Initalpixel und des Betrachterpixels
			// D = Wurzel(x^2 + x^2)
			double dEuclid = Math.sqrt((labelX - pixelX) * (labelX - pixelX) + (labelY - pixelY) * (labelY - pixelY));
			
			// Ist die euklidische Distanz größer als die erlaubte Distanz setzte Betrachterpixel auf Labelindex 2
			if (dEuclid > maxDistance) {
				element.setLabel(2);
			// sonst Labelindex 1
			} else {
				element.setLabel(1);
			}
		}
	}
	
/**
 *  Erstellt Start- und Endpunkt
 * @param i Labelindex
 * @return long gibt die Koordinaten zurück
 */
private long setStartEndNew(int i) {
		
		//links
		int leftX = 0;		
		//rechts
		int rightX = 0;		
		//oben
		int topY = 0;		
		//unten
		int bottomY = 0;
		
		//initialisiert
		boolean init = false;
		
		//Durchsuche alle Pixel mit dem Label i
		for (int j = 0; j < positions.size(); j++) {
			if (positions.get(j).getLabel() == i){
				if(init == false){
					leftX = positions.get(j).getX();
					rightX = positions.get(j).getX();
					
					topY = positions.get(j).getY();
					bottomY = positions.get(j).getY();
					
					init = true;					
				}
				
				//Finde den linken, rechten, oberen und, unteren roten Punkt
				if (positions.get(j).getX() < leftX){
					leftX = positions.get(j).getX();
				}
				if (positions.get(j).getX() > rightX){
					rightX = positions.get(j).getX();
				}
				
				if (positions.get(j).getY() < bottomY){
					bottomY = positions.get(j).getY();
				}
				if (positions.get(j).getY() > topY){
					topY = positions.get(j).getY();
				}
			}
		}
		
		//Ermittel den Mittelpunkt des roten Kreises
		int x = (leftX + rightX) / 2;
		int y = (topY + bottomY) / 2;
		
		long result = x * 1000000 + y;
		
		return result;
	}

	/**
	 * @return long gibt start Datenfeld zurück
	 */
	public static long getStart() {
		return start;
	}

	/**
	 * @return long gibt end Datenfeld zurück
	 */
	public static long getEnd() {
		return end;
	}
	
	/**
	 * @return ArrayList<PixelPosition> gibt positions Datenfeld zurück
	 */
	public static ArrayList<PixelPosition> getPositions(){
		return positions;
	}
}
