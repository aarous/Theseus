package theseus_data;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import theseus.Main;

/**
 * Verwaltet alle vorhandenen Buttons - beinh�lt auch dessen Position
 * @author Julian Sakowski
 * 
 */
public class MyJButton
{

	/**Position: X-Koordiante*/
	private int x = 0;
	/**Position: Y-Koordiante*/
	private int y = 0;

	/**Verkn�pfung mit einem Button*/
	private JButton button = null;

	/**
	 * Konstruktor
	 */
	public MyJButton()
	{
		button = new JButton("");
	}

	/**
	 * F�gt dem verkn�pften Button einen ActionListener hinzu
	 */
	public void actionListener()
	{
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{	
				// Ist der Button wei�, setze diesen auf schwarz
				if (button.getBackground().getRGB() == -1)
				{
					button.setBackground(Color.black);
					Main.structure.getDatenstruktur()[x][y] = 1;
				// Ist der Button wei�, setze diesen auf schwarz
				} else
				{
					button.setBackground(Color.white);
					Main.structure.getDatenstruktur()[x][y] = 0;
				}
			}
		});
	}

	/**
	 * @return JButton gibt den verkn�pften Button zur�ck
	 */
	public JButton getButton()
	{
		return button;
	}

	/**
	 * @return int gibt Datenfeld x zur�ck
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @return int gibt Datenfeld y zur�ck
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
