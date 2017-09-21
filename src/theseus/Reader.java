package theseus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader
{

	// Liest den Text aus einer Textdatei und schreibt sie in ein String
	public String readText(String url)
	{
		String ins = "";
		String zeile = null;

		try
		{
			BufferedReader in = new BufferedReader(new FileReader(url));

			while ((zeile = in.readLine()) != null)
			{
				ins = ins + zeile + "\n";
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return ins;
	}

}
