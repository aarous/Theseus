package theseus_data;

import java.awt.Cursor;
import java.awt.Frame;

import javax.swing.JOptionPane;

import theseus.Main;

/**
 * Die Testklasse zum vereinfachten und schnellem Testen des Programms
 * @author Julian Sakowski
 *
 */
public class Test {

	/**
	 * Konstruktor
	 */
	public Test() {

	}

	/**
	 * startet den Test - vereinfachte ÷ffnung eines Bildes, Schwarzweiﬂumwandlung und 2D-Repr‰sentation
	 */
	public void startTest() {
		// L‰dt Bild aus /res mit Endung .jpg
		String path = JOptionPane.showInputDialog(null,
				"Gib einfach nur den Bildnamen ein:", "Achtung",
				JOptionPane.PLAIN_MESSAGE);
		Main.window.loadImage("res/" + path + ".jpg");

		// klickt den Berechnen-Button
		Main.window.window.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		// Setzt das Bild auf SW
		Main.bImage = Main.analysis.prio(Main.bImage,
				Main.analysis.histo(Main.bImage));
		Main.analysis.setWhite(Main.bImage);
		Main.window.updateImageIcon();
		Main.window.applyMenu(2);
		Main.window.window.setExtendedState(Frame.MAXIMIZED_BOTH);
		Main.window.window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		Main.window.window.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		Main.analysis.initStruktur(Main.bImage);
		Main.window.updateImageIcon();
		Main.structure.continue2D();
		Main.window.window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
}
