package theseus_data;

/**
 * Repräsentiert ein Bildpixel mit Koordinaten und einem Labelindex
 * @author Julian Sakowski
 *
 */
public class PixelPosition {
	/**Beinhaltet X-Koordinate des Pixels*/
	int x;
	/**Beinhaltet Y-Koordinate des Pixels*/
	int y;
	
	/**Labelindex*/
	int label;
	
	/**
	 * @param x initialisiert die X-Koordinate des Pixels
	 * @param y initialisiert die Y-Koordinate des Pixels
	 * @param label initialisiert den Labelindex
	 */
	public PixelPosition(int x, int y, int label){
		this.x = x;
		this.y = y;
		this.label = label;
	}

	/**
	 * @param label initialisiert den Labelindex
	 */
	public void setLabel(int label) {
		this.label = label;
	}

	/**
	 * @return int gibt die X-Koordinate zurück
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return int gibt die Y-Koordinate zurück
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return int gibt den Labelindex zurück
	 */
	public int getLabel() {
		return label;
	}
}