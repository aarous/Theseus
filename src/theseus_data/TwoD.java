package theseus_data;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import theseus.Main;

/**
 * Erstellt die 2D-Repräsentation und ermöglicht eine Nachbearbeitung der abstrakten Datenstruktur
 * @author Julian Sakowski
 *
 */
public class TwoD
{
	/**frame erstellen name "2D-Repräsentation"*/
	JFrame frame = new JFrame("2D-Repräsentation");
	/**erstellt ein Panel für die Buttons*/
	JPanel panel = new JPanel();
	/**erstellt eine Menubar*/
	JMenuBar menu = new JMenuBar();

	/**Anzahl & Dimensionen*/
	/**Datenstrukturbreite*/
	int x;
	/**Datenstrukturhöhe*/
	int y;
	/**Maße der Buttonkanten*/
	int d = 10;
	/**Die Datenstruktur*/
	int data[][];
	/**Die Maßdimension*/
	Dimension dim = new Dimension(d, d);

	/**Alle MyJButtons*/
	MyJButton[][] buttons;
	/**Das Layout*/
	GridLayout layout;

	/**Zwischenspeicher der X-Koordinate*/
	int changeX = 0;
	/**Zwischenspeicher der Y-Koordinate*/
	int changeY = 0;

	/**
	 * Konstruktor
	 * @param extrafenster soll die 2 Repräsentation in einem neuen Fenster erstellt werden?
	 * @param setX Breite
	 * @param setY Höhe
	 * @param data Datenstruktur
	 */
	public TwoD(boolean extrafenster, int setX, int setY, int[][] data)
	{
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		// nicht in einem Extrafenster
		if (extrafenster == false)
		{
			Canvas canvas = new Canvas();
			frame = Main.window.getWindow();
			frame.add(canvas);
			Main.window.getContentPane().removeAll();
		}

		x = setX;
		y = setY;

		buttons = new MyJButton[x][y];

		try
		{
			layout = new GridLayout(y, x);
		} 
		catch (Exception e)
		{
			System.out.println("Exception: " + e
					+ " - die Größe der abstrakten Datenstruktur beträgt 0!");
			return;
		}

		this.data = data;

		init2D();
	}

	/**
	 * initialisiert die 2D-Repräsentation
	 */
	public void init2D()
	{
		// Frame erstellen
		frame.setSize(d * x, d * y);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout());

		frame.setJMenuBar(menu);
		JMenuItem goItem = new JMenuItem("Spiel starten");
		menu.add(goItem);
		goItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				menu.setVisible(false);
				Main.window.initGame();
			}
		});

		panel.setLayout(layout);
		
		//Buttons konfigurieren und an das Panel hängen
		createButtons();

		frame.add(panel);
		frame.setVisible(true);

		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	/**
	 * Konfiguriert die Buttons in dem MyJButton-Array
	 */
	private void createButtons()
	{
		// Buttons erstellen
		Main.thread.setStop(true);
		int count = 0;
		int amount = Main.getButtonStorage().size();
		// Array durchlaufen
		for (int i = 0; i < y; i++)
		{
			for (int j = 0; j < x; j++)
			{	
				//Hole Buttons aus dem Vorrate, solange welche vorhanden sind - sonst erstelle neue
				if (count < amount)
				{
					buttons[j][i] = Main.getButtonStorage().get(count);
					count++;
					buttons[j][i].setX(j);
					buttons[j][i].setY(i);
				} else
				{
					buttons[j][i] = new MyJButton();
					buttons[j][i].setX(j);
					buttons[j][i].setY(i);
				}
				// Button einfärben
				if (data[j][i] == 1)
				{
					buttons[j][i].getButton().setBackground(Color.black);
				} else if (data[j][i] == 0)
				{
					buttons[j][i].getButton().setBackground(Color.white);
				} else
				{
					buttons[j][i].getButton().setBackground(Color.red);
				}
				if (buttons[j][i].getButton().getBackground() == Color.red)
				{
					// NO ACTION LISTENER
				} else
				{
					//ActionListener initialisieren
					buttons[j][i].actionListener();
				}
				// Button an das Panel hängen
				panel.add(buttons[j][i].getButton());
			}
		}
	}
}
