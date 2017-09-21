package theseus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import theseus_Game.TheseusGame;
import theseus_data.StartEnd;

public class Window
{
	String FPS = "Theseus";
	public static JFrame window;
	JMenuItem openitem;
	JMenuItem startitem;
	JMenuItem calculateitem;
	JMenuItem quickstart;
	JMenuItem medianitem;
	JMenuItem middleitem;
	JMenuItem continueitem;
	JMenuItem testitem;
	JLabel label;
	static JMenuBar menubar;
	Container contentPane;
	ImageIcon imageIcon;
	
	// Konstruktor
	public Window()
	{
		// Erzeugt Hilfsobjekte zur Bilderkennung
		Main.analysis = new ImageAnalysis();

		// Erzeugt das gesamte Fenster samt Funktionen
		initWindow();
		initMenu();
		initAction();
		initImagePanel();
		// initLoadImageWindow();
		applyMenu(0);
		window.setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	// Erzeuge das Fenster
	private void initWindow()
	{
		// Erstellt den Frame mit Einstellungen
		window = new JFrame(FPS);
		contentPane = window.getContentPane();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setVisible(true);
	}

	// Startet das Spiel
	public void initGame()
	{
		menubar.setVisible(false);
		contentPane.removeAll();
		TheseusGame.getInstance();
	}

	// Erzeuge die Menüzeile samt Einträge
	private void initMenu()
	{
		// Erzeugt die Menüleiste
		menubar = new JMenuBar();
		window.setJMenuBar(menubar);

		// Erzeugt einzelne Menüeinträge
		startitem = new JMenuItem("Spiel starten");
		calculateitem = new JMenuItem("Berechnen");
		openitem = new JMenuItem("Neues Bild");
		// testitem = new JMenuItem("TEST");
		quickstart = new JMenuItem("Schnellstart");
		medianitem = new JMenuItem("Median Filter");
		middleitem = new JMenuItem("Mittelwert Filter");
		continueitem = new JMenuItem("Weiter");
	}

	// Zeigt verschiedene Menüs an
	public void applyMenu(int i)
	{
		switch (i)
		{
			case 0:
				// Versteckt nicht benötigte Menüelemente
				openitem.setVisible(false);
				startitem.setVisible(false);
				calculateitem.setVisible(false);
				quickstart.setVisible(false);
				medianitem.setVisible(false);
				middleitem.setVisible(false);
				continueitem.setVisible(false);

				menubar.add(openitem);
				openitem.setVisible(true);

				// Fenster auf Vollbild setzen
				window.setExtendedState(Frame.MAXIMIZED_BOTH);
			break;
			case 1:
				// Versteckt nicht benötigte Menüelemente
				openitem.setVisible(false);
				startitem.setVisible(false);
				calculateitem.setVisible(false);
				quickstart.setVisible(false);
				medianitem.setVisible(false);
				middleitem.setVisible(false);
				continueitem.setVisible(false);

				menubar.add(calculateitem);
				calculateitem.setVisible(true);

				menubar.add(quickstart);
				quickstart.setVisible(true);

				// Fenster auf Vollbild setzen
				window.setExtendedState(Frame.MAXIMIZED_BOTH);
			break;
			case 2:
				// Versteckt nicht benötigte Menüelemente
				openitem.setVisible(false);
				startitem.setVisible(false);
				calculateitem.setVisible(false);
				quickstart.setVisible(false);
				medianitem.setVisible(false);
				middleitem.setVisible(false);
				continueitem.setVisible(false);

				menubar.add(continueitem);
				continueitem.setVisible(true);

				menubar.add(medianitem);
				medianitem.setVisible(true);

				menubar.add(middleitem);
				middleitem.setVisible(true);

				// Fenster auf Vollbild setzen
				window.setExtendedState(Frame.MAXIMIZED_BOTH);
			break;

			default:
			break;
		}

	}

	// Initiiere die ActionListener!
	private void initAction()
	{
		// ActionListener fuer den "Bild Laden"-Button
		openitem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				initLoadImageWindow();

			}
		});

		// Item zum Testen bei Programmierung
		/**
		 * testitem.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { Main.test.startTest(); } });
		 */

		// ActionListener fuer den "Berechnen"-Button
		calculateitem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				window.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				// Setzt das Bild auf SW
				Main.bImage = Main.analysis.prio(Main.bImage,
						Main.analysis.histo(Main.bImage));
				Main.analysis.setWhite(Main.bImage);
				updateImageIcon();
				applyMenu(2);
				window.setExtendedState(Frame.MAXIMIZED_BOTH);
				window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});

		// ActionListener fuer den "MedianFilter"-Button
		medianitem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				window.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				Main.bImage = Main.analysis.medianFilter(Main.bImage);
				updateImageIcon();
				window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			}
		});

		// ActionListener fuer den "Mittelwert Filter"-Button
		middleitem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				window.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				Main.bImage = Main.analysis.middleFilter(Main.bImage);
				updateImageIcon();
				window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});

		// ActionListener fuer den "Weiter"-Button
		continueitem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				window.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				Main.analysis.initStruktur(Main.bImage);
				updateImageIcon();
				Main.structure.continue2D();
				window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});

		// ActionListener fuer den "Schnellstart"-Button
		quickstart.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				window.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				Main.analysis.setWhite(Main.bImage);
				Main.bImage = Main.analysis.prio(Main.bImage,
						Main.analysis.histo(Main.bImage));
				Main.analysis.medianFilter(Main.bImage);
				Main.analysis.initStruktur(Main.bImage);
				Main.structure.continue2D();
				initGame();
				menubar.setVisible(false);
				window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
	}

	// Initiere das Startbild bei Programmstart
	private void initImagePanel()
	{
		// Hier wird der Startbildschirm erstellt und in das Fenster geladen
		try
		{
			// Erstmals wird eine Datei in das bImage geschrieben
			Main.bImage = ImageIO.read(new File("res/intro2.png"));

		} 
		catch (IOException ex)
		{
		}

		// Das Bild wird in Image Icon geladen
		imageIcon = new ImageIcon(Main.bImage);
		// ImageIcon wird in Label angezeigt
		label = new JLabel(imageIcon);
		// Label wird in Fenster geladen
		contentPane.add(label);
	}
	
	// Initialisiere ein Eingabefenster für den Bildpfad
	private void initLoadImageWindow()
	{
		// Erstellt einen InputDialog
		String input = JOptionPane.showInputDialog(null,
				"Geben Sie einen Blidpfad ein:", "Bild laden",
				JOptionPane.PLAIN_MESSAGE);
		loadImage(input);
		updateImageIcon();
	}

	// Lädt ein Bild in die Variable bImage
	public void loadImage(String path)
	{
		// Wenn in dem Path (oder dem Eingabefeld nicht drin steht, wird ein
		// Fehler geworfen, wenn man die Eingabeaufforderung ohne Eingabe
		// schließt
		if (path != null)
		{
			try
			{
				// Erstellt ein BufferedImage aus einer Datei des Pfades
				Main.bImage = ImageIO.read(new File(path));
				Main.startEnd = new StartEnd();
				updateImageIcon();
				applyMenu(1);
			} 
			catch (IOException ex)
			{
				// falscher Bildpfad
				JOptionPane.showMessageDialog(
								null,
								"Der angegebene Bildpfad oder das Bild existiert nicht!",
								"Kein Bild gefunden!",
								JOptionPane.WARNING_MESSAGE);
			}
		}
		
	}

	// Lädt das bImage in das ImageIcon
	public void updateImageIcon()
	{
		// Setzt neues Bild in das ImageIcon
		imageIcon.setImage(Main.bImage);

		// UI Reset und Bildzuweisung
		label.updateUI();
		label.setIcon(imageIcon);
	}

	// Gibt den Frame zurück
	public static JFrame getWindow()
	{
		return window;
	}

	// Gibt den Container zurück
	public Container getContentPane()
	{
		return contentPane;
	}

	// Setzt die FramesPerSeconds
	public void setFPS(String fPS)
	{
		this.FPS = fPS;
		window.setTitle(FPS);
	}

	// Gibt die FramesPerSeconds zurück
	public String getFPS()
	{
		return FPS;
	}

}
