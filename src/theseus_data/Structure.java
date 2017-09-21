package theseus_data;

import java.util.ArrayList;

import theseus.Main;

/**
 * Erstellt eine abstrakte Datenstruktur, welche das Bild in einer abstrakten Form repräsentiert.
 * Dabei werden Mauerstärke, Gangstärke und weitere Faktoren berücksichtigt.
 * @author Julian Sakowski
 *
 */
public class Structure {

	/**hilfsvariable - wird gerade an einer mauer gezählt?*/
	boolean mauer = false;
	/**die Breite der abstrakten Datenstruktur*/
	private int dataWidth = 0;
	/**die Höhe der abstrakten Datenstruktur*/
	private int dataHeight = 0;
 
	/**gibt an, ab wie viel schwarzen Pixel innerhalb eines abstrakten
	* Feldes eine Mauer in der abstrakten Datensturktur representiert werden
	* soll*/
	private int gueltig = 0;
	/**der median der ArrayList laenge*/
	private int median = 0;
	/**verringert den Median bei extrem dünnen Gängen*/
	private int modifier = 0;
	/**gibt die Prozentzahl an für eine gültige Mauer*/
	private double percent = 40;
	/**beinhaltet die Längen und Breiten der Mauern*/
	private ArrayList<Integer> laenge; 
	/**geinhaltet die Längen und Breiten der Gänge*/
	private ArrayList<Integer> gap;
	/**die abstrakte Datenstruktur*/
	private int[][] datenstruktur;
	/**Pixelstruktur nach Dilatation*/
	private int[][] dilatedPixelStruktur = Main.analysis.getPixelStruktur();;
	/**
	 * 
	 */
	private int imageWidth = Main.analysis.imageWidth;
	/**
	 * 
	 */
	private int imageHeight = Main.analysis.imageHeight;

	/**
	 * verhindert doppelte Sortierung
	 */
	private boolean sorted = false;

	/**
	 * Konstruktor
	 */
	public Structure() {
		laenge = new ArrayList<Integer>(0);
		gap = new ArrayList<Integer>(0);
		/** dilateN(0); */
		countLengths();
		countGap();
		initModifier();
		// initialisiert den Median erneut mit Berücksichtigung des Modifiers
		initMedian();
		initGueltig();
		initData();
		createData();
		/** show2DConsole(); */
	}

	/** FEHLEHERHAFT - Programmierung nicht beendet, da diese Verarbeitung für unser Projekt nicht hilfreich war
	 * private void dilate() { int counter = 0; for (int i = 0; i < imageHeight;
	 * i++) { for (int j = 0; j < imageWidth; j++) { // schwarzes Pixel suchen
	 * if (pixelstruktur[j][i] == 1) { // 4-Nachberschaft prüfen // links if (j
	 * > 0 && pixelstruktur[j - 1][i] == 0) { dilatedPixelStruktur[j - 1][i] =
	 * 1; counter++; System.out.println("links 1 - " + counter); } // oben if (i
	 * > 0 && pixelstruktur[j][i - 1] == 0) { dilatedPixelStruktur[j][i - 1] =
	 * 1; } // rechts if (j + 1 < imageWidth && pixelstruktur[j + 1][i] == 0) {
	 * dilatedPixelStruktur[j + 1][i] = 1; } // unten if (i + 1 < imageHeight &&
	 * pixelstruktur[j][i + 1] == 0) { dilatedPixelStruktur[j][i + 1] = 1; } } }
	 * }
	 * 
	 * }
	 * 
	 * private void dilateN(int n) { for (int i = 0; i < n; i++) { dilate();
	 * System.out.println("dilate"); } }
	 */
	
	/**
	 * errechnet Länge bzw Breite der Mauern und speichert den Wert in die
	 * ArrayList laenge
	 */
	private void countLengths() {
		int count = 0;
		// System.out.println("pixelstruktur.length " + pixelstruktur.length);
		// System.out.println("pixelstruktur[imageWidth-1].length " +
		// pixelstruktur[imageWidth-1].length);
		// speichert die Mauerlängen in der horizontalen ab
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				// wird gerade an einer Mauer gezählt?
				if (mauer == true) {
					// erhöhe count, wenn Mauer 1 Pixel länger ist
					if (dilatedPixelStruktur[j][i] == 1) {
						count++;
					} else {
						mauer = false;
						laenge.add(count);
						count = 0;
					}
				} else {
					// fängt eine neue Mauer an?
					if (dilatedPixelStruktur[j][i] == 1) {
						count++;
						mauer = true;
					}
				}
			}
			// Zeile beendet - Mauer also zu ende!
			mauer = false;
			if (count != 0) {
				laenge.add(count);
				count = 0;
			}
		}
		mauer = false;
		count = 0;
		// speichert die Mauerlängen in der vertikalen ab
		for (int j = 0; j <= imageWidth - 1; j++) {
			for (int i = 0; i <= imageHeight - 1; i++) {

				// wird gerade an einer Mauer gezählt?
				if (mauer == true) {
					// erhöhe count, wenn Mauer 1 Pixel länger ist
					if (dilatedPixelStruktur[j][i] == 1) {
						count++;
					} else {
						mauer = false;
						laenge.add(count);
						count = 0;
					}
				} else {
					// fängt eine neue Mauer an?
					if (dilatedPixelStruktur[j][i] == 1) {
						count++;
						mauer = true;
					}
				}
			}
			// Spalte beendet - Mauer also zu ende!
			mauer = false;
			if (count != 0) {
				laenge.add(count);
				count = 0;
			}
		}
	}

	/**
	 * errechnet Länge bzw Breite der Gänge und speichert den Wert in die
	 * ArrayList gap
	 */
	private void countGap() {
		int count = 0;
		// speichert die Ganglängen in der horizontalen ab
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				// wird gerade an einem Gang gezählt?
				if (mauer == false) {
					// erhöhe count, wenn Gang 1 Pixel länger ist
					if (dilatedPixelStruktur[j][i] == 0) {
						count++;
					} else {
						mauer = true;
						gap.add(count);
						count = 0;
					}
				} else {
					// fängt eine neuer Gang an?
					if (dilatedPixelStruktur[j][i] == 0) {
						count++;
						mauer = false;
					}
				}
			}
			// Zeile beendet - Gang also zu ende!
			mauer = true;
			if (count != 0) {
				gap.add(count);
				count = 0;
			}
		}
		mauer = true;
		count = 0;
		// speichert die Ganglängen in der vertikalen ab
		for (int j = 0; j <= imageWidth - 1; j++) {
			for (int i = 0; i <= imageHeight - 1; i++) {

				// wird gerade an einem Gang gezählt?
				if (mauer == false) {
					// erhöhe count, wenn Gang 1 Pixel länger ist
					if (dilatedPixelStruktur[j][i] == 0) {
						count++;
					} else {
						mauer = true;
						gap.add(count);
						count = 0;
					}
				} else {
					// fängt eine neuer Gang an?
					if (dilatedPixelStruktur[j][i] == 0) {
						count++;
						mauer = false;
					}
				}
			}
			// Spalte beendet - Gang also zu ende!
			mauer = true;
			if (count != 0) {
				gap.add(count);
				count = 0;
			}
		}
	}

	/**
	 * berrechnet die durchschnittlicher Gangstärke, indem der median der
	 * ArrayList gap gewählt wird
	 */
	private void initModifier() {
		quickSort(gap, 0, gap.size() - 1);

		int medianGap = 0;
		// initialisiert den modifier
		try {
			medianGap = gap.get(gap.size() / 2);
		} catch (Exception e) {
			System.out.println(e
					+ ": Die ArrayList laenge ist leer. Das Bild ist weiß!");
		}
		System.out.println("Folgende Gangstärke wurde errechnet: " + medianGap);

		// initalisiert den Median ohne Berücksichtigung des Modifiers - zur
		// Berechnung des Modifiers
		initMedian();
		double dMedian = median;
		double dMedianGap = medianGap;
		double dMedianGapCheck = 0.9 * medianGap;
		double result = 0;
		// Gang KLEINER oder MINIMAL als Mauern
		if (dMedianGapCheck < dMedian) {
			System.out.println("Gang KLEINER  oder MINIMAL größer als Mauern");
			result = dMedian / dMedianGap * 6;
		}
		// Gang GRÖßER als Mauern
		else {
			System.out.println("Gang viel GRÖßER als Mauern");
			System.out.println("dMedianGap / dMedian " + dMedianGap / dMedian);
			result = -(dMedianGap / dMedian / 5);
			percent = dMedian / dMedianGap * 0.8 * 100;
			if (percent > 52) {
				percent = 52;
			}
		}

		System.out.println("RESULT: " + result);
		System.out.println("PERCENT: " + percent);
		modifier = (int) Math.round(result);

		System.out.println("Folgender Modifier wurde errechnet: " + modifier);
	}

	/**
	 * berrechnet die durchschnittlicher Mauerstärke, indem der median der
	 * ArrayList laenge gewählt wird
	 */
	private void initMedian() {
		if (!sorted) {
			quickSort(laenge, 0, laenge.size() - 1);
			sorted = true;
		}

		// initialisiert den median
		try {
			// Modifier darf nicht größer, gleich als der Medianwert sein
			if (laenge.get(laenge.size() / 2) <= 4) {
				median = 4;
			} else if (modifier >= laenge.get(laenge.size() / 2)) {
				median = laenge.get(laenge.size() / 2) - 1;
			} else {
				median = laenge.get(laenge.size() / 2) - modifier;
			}
		} catch (Exception e) {
			System.out.println(e
					+ ": Die ArrayList laenge ist leer. Das Bild ist weiß!");
		}
		System.out.println("Folgende Mauerstärke wurde errechnet: " + median);
	}

	/**
	 * initialisiert gueltig
	 */
	private void initGueltig() {
		double hundert = median * median;
		double einPercent = hundert / 100;
		double p = percent;
		// setzt gueltig auf percent% Schwarzfülle
		System.out.println("Gueltig: " + Math.ceil(einPercent * p));
		// Math.ceil() => Aufrunden!
		gueltig = (int) Math.ceil(einPercent * p);
	}
 
	/**
	 * initialisiert die datenstruktur[][] - Werte alle auf 0
	 */
	private void initData() {
		// Pixelstruktur auf Datenstruktur abstrahieren, hierbei auf
		// ganzzahligen Wert AUF-runden
		// Breite & Höhe teilen durch median
		double zaehlerWidth = imageWidth;
		double zaehlerHeight = imageHeight;
		double nenner = median;

		double x = Math.ceil(zaehlerWidth / nenner);
		int breite = (int) x;
		dataWidth = breite;

		double y = Math.ceil(zaehlerHeight / nenner);
		int hoehe = (int) y;
		dataHeight = hoehe;

		// abstrakte Datenstruktur wird mit abstrakter Breite und abstrakter
		// Höhe initialisiert
		datenstruktur = new int[breite][hoehe];
		System.out.println("Das Bild ist " + imageWidth + " Pixel breit und "
				+ imageHeight + " Pixel hoch.");
		System.out.println("Die Datenstruktur ist " + dataWidth
				+ " abstrakte Elemente breit und " + dataHeight
				+ " abstrakte Elemente hoch. - median: " + median);

		// datenstruktur auf 0 initialisieren
		for (int i = 0; i < hoehe; i++) {
			for (int j = 0; j < breite; j++) {
				datenstruktur[j][i] = 0;
			}
		}
	}

	/**
	 * die datenstruktur wird befüllt
	 * 1 steht für: Das Feld representiert eine Mauer
	 * 0 steht für: Das Feld representiert keine Mauer  
	 */
	private void createData() {
		int counter = 0;
		/** int square = median * median; */
		// durchlaufe die abstrakte datenstruktur
		for (int i = 0; i < dataHeight; i++) {
			for (int j = 0; j < dataWidth; j++) {

				// durchlaufe alle Pixel, welche sich in einem Feld der
				// datenstruktur befinden
				for (int k = i * median; k < i * median + median; k++) {
					for (int l = j * median; l < j * median + median; l++) {
						// try-catch, da Datenstruktur über Bildgröße
						// hinausgehen kann
						try {
							// erhöhe counter für jedes schwarze Pixel
							if (dilatedPixelStruktur[l][k] == 1) {
								counter++;
							}
						} catch (Exception e) {
							// counter++;
						}

					}
				}
				// Prüfen, ob mehr als die Hälfte des Feldes mit schwarzen
				// Pixeln gefüllt ist und fülle entsprechend die datenstruktur
				if (counter >= gueltig) {
					datenstruktur[j][i] = 1;
				}
				/** System.out.println(counter +"  "+ gueltig); */
				// counter beginnt für neues Feld wieder bei 0
				counter = 0;
			}
		}
		// Start und Endposition mit einer 2 initialisieren
		int startX = (int) (StartEnd.getStart() / 1000000) / median;
		int startY = (int) (StartEnd.getStart() % 1000000) / median;

		int endX = (int) (StartEnd.getEnd() / 1000000) / median;
		int endY = (int) (StartEnd.getEnd() % 1000000) / median;
		
		// 4-Nachbarschaft ermitteln und später auch rot färben
		try {
			datenstruktur[startX][startY] = 3;

			datenstruktur[startX][startY - 1] = 2;
			datenstruktur[startX - 1][startY] = 2;
			datenstruktur[startX + 1][startY] = 2;
			datenstruktur[startX][startY + 1] = 2;

			datenstruktur[endX][endY] = 3;

			datenstruktur[endX][endY - 1] = 2;
			datenstruktur[endX - 1][endY] = 2;
			datenstruktur[endX + 1][endY] = 2;
			datenstruktur[endX][endY + 1] = 2;
		} catch (Exception e) {
			System.out.println("Exception: " + e
					+ " - Pixel liegen außerhalb des Bildes.");
		}

		System.out.println("Die Starposition: " + "(" + endX + " / " + endY
				+ ")" + "	Die Zielposition: " + "(" + startX + " / " + startY
				+ ")");

	}

	/**
	 * QucikSort
	 * @param array Eine ArrayList übergeben
	 * @param links Startindex
	 * @param rechts Endindex
	 */
	private void quickSort(ArrayList<Integer> array, int links, int rechts) {
		if (links < rechts) {
			int teiler = teile(array, links, rechts);
			quickSort(array, links, teiler - 1);
			quickSort(array, teiler + 1, rechts);
		}

	}

	/**
	 * errechnet den Teilbereich des Arrays der erneut sortiert werden muss
	 * @param array ArrayList übergeben
	 * @param links Startindex
	 * @param rechts Endindex
	 * @return int Pivotkorrdinate
	 */
	private int teile(ArrayList<Integer> array, int links, int rechts) {
		int i = links;
		int j = rechts - 1;
		int pivot = array.get(rechts);

		do {
			// Suche links ein Element, welches größer ist als das Pivotelement
			while (array.get(i) <= pivot && i < rechts) {
				i++;
			}
			// Suche links ein Element, welches kleiner ist als das Pivotelement
			while (array.get(j) >= pivot && j > links) {
				j--;
			}
			if (i < j) {
				// Tausche Daten von index i und index j
				int a = array.get(i);
				array.set(i, array.get(j));
				array.set(j, a);
			}
		} while (i < j);

		if (array.get(i) > pivot) {
			// Tausche Daten von index i und rechts
			int a = array.get(i);
			array.set(i, array.get(rechts));
			array.set(rechts, a);
		}

		// gibt die Position des Pivotelements zurück
		return i;

	}

	/**
	 * @return int[][] gibt datenstruktur zurück
	 */
	public int[][] getDatenstruktur() {
		return datenstruktur;
	}

	/**
	 * erstellt die 2D-Repräsentation
	 */
	public void continue2D() {
		// System.out.println("ÜBERGEBE: " +dataWidth + " " + dataHeight);
		Main.twoD = new TwoD(false, dataWidth, dataHeight, datenstruktur);
	}

}
