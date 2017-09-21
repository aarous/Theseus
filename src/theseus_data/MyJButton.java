package theseus_data;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import theseus.Main;

/**
 * Verwaltet alle vorhandenen Buttons - beinhält auch dessen Position
 * @author Julian Sakowski
 * 
 */
public class MyJButton
{

	/**Position: X-Koordiante*/
	private int x = 0;
	/**Position: Y-Koordiante*/
	private int y = 0;

	/**Verknüpfung mit einem Button*/
	private JButton button = null;

	/**
	 * Konstruktor
	 */
	public MyJButton()
	{
		button = new JButton("");
	}

	/**
	 * Fügt dem verknüpften Button einen ActionListener hinzu
	 */
	public void actionListener()
	{
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{	
				// Ist der Button weiß, setze diesen auf schwarz
				if (button.getBackground().getRGB() == -1)
				{
					button.setBackground(Color.black);
					Main.structure.getDatenstruktur()[x][y] = 1;
				// Ist der Button weiß, setze diesen auf schwarz
				} else
				{
					button.setBackground(Color.white);
					Main.structure.getDatenstruktur()[x][y] = 0;
				}
			}
		});
	}

	/**
	 * @return JButton gibt den verknüpften Button zurück
	 */
	public JButton getButton()
	{
		return button;
	}

	/**
	 * @return int gibt Datenfeld x zurück
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @return int gibt Datenfeld y zurück
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * @param x initialisiert das Datenfeld x
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * @param y initialisiert das Datenfeld y
	 */
	public void setY(int y)
	{
		this.y = y;
	}
}
