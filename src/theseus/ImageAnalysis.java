package theseus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.util.ArrayList;
import java.util.Iterator;

import theseus_data.PixelPosition;
import theseus_data.StartEnd;
import theseus_data.Structure;

public class ImageAnalysis
{
	// Datenfelder
	public static int imageWidth;
	public static int imageHeight;
	private int pixelStruktur[][];

	// Konstruktor
	public ImageAnalysis()
	{
	}

	// Erstellt die Pixelstruktur
	public void initStruktur(BufferedImage bImage)
	{
		// intitialisiert die pixelStruktur
		pixelStruktur = new int[bImage.getWidth()][bImage.getHeight()];

		// initialisiert imageWidth und imageHeight
		imageWidth = bImage.getWidth();
		imageHeight = bImage.getHeight();
		// Schreibt Wand oder Nicht-Wand in ein Array
		for (int y = bImage.getMinY(); y < bImage.getHeight(); y++)
		{
			for (int x = bImage.getMinX(); x < bImage.getWidth(); x++)
			{
				if (bImage.getRGB(x, y) < -1)
				{
					pixelStruktur[x][y] = 1;
				} else
				{
					pixelStruktur[x][y] = 0;
				}
			}
		}

		Main.structure = new Structure();
	}

	//Wandelt das Farbbild in ein Grauwert-Bild um
	public BufferedImage setGrey(BufferedImage bImage)
	{
		// Erstellt neues BufferedImage mit anderem "Farbraum"
		BufferedImage gImage = new BufferedImage(bImage.getWidth(), bImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);  
		Graphics g = gImage.getGraphics();  
		// Malt das alte Bild in das Neue
		g.drawImage(bImage, 0, 0, null);  
		g.dispose(); 
		
		return gImage;
	}

	// Färbt das Bild nach dem Array
	public BufferedImage prio(BufferedImage bImage, int[][] bin)
	{
		int rgb;
		float red;
		Color color;
		
		bImage = Main.analysis.setGrey(Main.bImage);

		// Pixelweises Durchlaufen des Bildes
		for (int y = bImage.getMinY(); y <= bImage.getHeight() - 1; y++)
		{
			for (int x = bImage.getMinX(); x <= bImage.getWidth() - 1; x++)
			{
				rgb = bImage.getRGB(x, y);
				color = new Color(rgb);
				red = color.getRed();

				// Färbt Pixel nach dem Array
				if (red >= bin[0][1] && red < bin[0][1] + 32)
				{
					bImage.setRGB(x, y, Color.black.getRGB());
				}
				if (red >= bin[1][1] && red < bin[1][1] + 32)
				{
					bImage.setRGB(x, y, Color.black.getRGB());
				}
				if (red >= bin[2][1] && red < bin[2][1] + 32)
				{
					bImage.setRGB(x, y, Color.black.getRGB());
				}
				if (red >= bin[3][1] && red < bin[3][1] + 32)
				{
					bImage.setRGB(x, y, Color.black.getRGB());
				}
				if (red >= bin[4][1] && red < bin[4][1] + 32)
				{
					bImage.setRGB(x, y, Color.black.getRGB());
				}
				if (red >= bin[5][1] && red < bin[5][1] + 32)
				{
					bImage.setRGB(x, y, Color.black.getRGB());
				}
				if (red >= bin[6][1] && red < bin[6][1] + 32)
				{
					bImage.setRGB(x, y, Color.black.getRGB());
				}
				if (red >= bin[7][1] && red < bin[7][1] + 32)
				{
					bImage.setRGB(x, y, Color.white.getRGB());
				}
			}
		}
		
		return bImage;
	}

	// Gibt ein sortiertes Array der Farbwerte nach absoluter Häufigkeit zurück
	public int[][] histo(BufferedImage bImage)
	{
		int[][] bin = new int[8][2];
		int rgb;
		int red;
		Color color;

		// Geht pixelweise durch das Bild und sortiert Häufigkeit der Farbwerte
		// in das Array
		for (int y = bImage.getMinY(); y <= bImage.getHeight() - 1; y++)
		{
			for (int x = bImage.getMinX(); x <= bImage.getWidth() - 1; x++)
			{
				// Erstellt neues Color Objekt
				rgb = bImage.getRGB(x, y);
				color = new Color(rgb);
				red = color.getRed();

				// Erhöht Index in jedem Topf, wenn Pixel entsprechenden Farbwert hat
				if (red >= 0 && red < 32)
				{
					bin[0][0]++;
				} else if (red >= 32 && red < 64)
				{
					bin[1][0]++;
				} else if (red >= 64 && red < 96)
				{
					bin[2][0]++;
				} else if (red >= 96 && red < 128)
				{
					bin[3][0]++;
				} else if (red >= 128 && red < 160)
				{
					bin[4][0]++;
				} else if (red >= 160 && red < 192)
				{
					bin[5][0]++;
				} else if (red >= 192 && red < 224)
				{
					bin[6][0]++;
				} else if (red >= 224 && red < 256)
				{
					bin[7][0]++;
				} else
				{
				}
			}
		}

		// Schreibt die Farbwerte in das Array
		bin = createBins(bin);

		// Sortiert das Array
		bin = bubble(bin);

		return bin;
	}

	// Ersetzt alle roten Pixel durch weiße
	public void setWhite(BufferedImage bImage)
	{
		ArrayList<PixelPosition> positions = StartEnd.getPositions();
		
		// iteriere das Array und setze die Punkte auf die Farbe weiß
		Iterator<PixelPosition> it = positions.iterator();
		while (it.hasNext())
		{
			PixelPosition element = it.next();
			bImage.setRGB(element.getX(), element.getY(), Color.white.getRGB());
		}

	}
	
	// Säubert das Bild mit dem Median Filter
	public BufferedImage medianFilter(BufferedImage bImage)
	{
		// Vorher muss setGrey ausgeführt werden
		int index = 0;
		int[] sq = new int[25];

		for (int y = bImage.getMinY() + 2; y <= bImage.getHeight() - 3; y++)
		{
			for (int x = bImage.getMinX() + 2; x <= bImage.getWidth() - 3; x++)
			{
				// Läuft durch ein 3x3 Quadrat
				for (int i = x - 2; i <= x + 2; i++)
				{
					for (int j = y - 2; j <= y + 2; j++)
					{
						// Schreib RGB-Werte des Kerns in das Array
						sq[index] = bImage.getRGB(i, j);
						index++;
					}
				}

				// Soertiert das Array und findet den Median
				java.util.Arrays.sort(sq);
				int median = findMedian(sq);
				
				// Setzt den neuen Farbwert in Mittelpunkt
				bImage.setRGB(x, y, median);
				index = 0;
			}
		}
		
		return bImage;
	}
	
	// Säubert das Bild mit dem Mittelwert Filter (3x3 Filterkern)
	public BufferedImage middleFilter(BufferedImage bImage)
	{
		// Vorher muss setGrey ausgeführt werden 
		int index = 0;
		int middle = 0;
		int[] sq = new int[9];

		for (int y = bImage.getMinY() + 1; y <= bImage.getHeight() - 2; y++)
		{
			for (int x = bImage.getMinX() + 1; x <= bImage.getWidth() - 2; x++)
			{
				// Läuft durch ein 3x3 Quadrat
				for (int i = x - 1; i <= x + 1; i++)
				{
					for (int j = y - 1; j <= y + 1; j++)
					{
						// Schreib RGB-Wert des Quadrats in das Array
						sq[index] = bImage.getRGB(i, j);
						index++;
					}
				}

				// Soertiert das Array 
				java.util.Arrays.sort(sq);
				
				// Berechnet den Mittelwert
				for(int i = 0; i < sq.length; i++)
				{
					middle = middle + sq[i];
				}
				middle = middle/9;
				
				// Setzt den neuen Farbwert ein
				bImage.setRGB(x, y, middle);
				index = 0;
			}
		}
		
		return bImage;
	}

	// Berechnet den Median eines Arrays
	private int findMedian(int[] sq)
	{
		int median;
		median = sq[sq.length / 2];
		return median;
	}

	// Schreibt die Farbwertgruppen in das Array
	private int[][] createBins(int[][] bin)
	{
		int x = 0;

		for (int i = 0; i < bin.length; i++)
		{
			bin[i][1] = x;
			x = x + 32;
		}

		return bin;
	}

	// Sortiert das Array mit Bubblesort
	private int[][] bubble(int[][] bin)
	{
		int help;

		for (int l = bin.length; l > 1; l--)
		{
			for (int i = 0; i < l - 1; i++)
			{
				if (bin[i][0] > bin[i + 1][0])
				{
					// Die erste Zeile des Arrays sortieren
					help = bin[i][0];
					bin[i][0] = bin[i + 1][0];
					bin[i + 1][0] = help;

					// Die zweite Zeile entsprechend der ersten anordnen (nicht sortieren!)
					help = bin[i][1];
					bin[i][1] = bin[i + 1][1];
					bin[i + 1][1] = help;
				}
			}
		}

		return bin;
	}

	// Gibt die Pixelstruktur zurück
	public int[][] getPixelStruktur()
	{
		return pixelStruktur;
	}

}
