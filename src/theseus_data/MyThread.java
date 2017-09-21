package theseus_data;

import java.awt.Dimension;

import javax.swing.JButton;

import theseus.Main;

/**
 * Erstellt ab Programmstart 90000 Buttons, um später Ladezeiten zu vermeiden
 * @author Julian Sakowski
 *
 */
public class MyThread implements Runnable {

	/**Ist für Threadbeendigung verantwortlich, falls true*/
	private boolean stop = false;
	
	/**lokale Datenfelder - initialisiert Buttongröße*/
	int d = 10;
	Dimension dim = new Dimension(d, d);
	
	/**
	 * Konstruktor
	 */
	public MyThread() {
		Thread thread;
		thread = new Thread(this, "My thread");
		thread.start();
	}

	/**
	 * Methoden und Befehle, die der Thread abarbeiten soll
	 */
	public void run() {
			int count = 0;
			//erstellt 90000 Buttons und speichert diese im ButtonStorage
			while (count < 90000 && stop == false) {
				for (int i = 0; i < 10; i++){
					MyJButton b = new MyJButton();
					b.getButton().setPreferredSize(dim);
					b.getButton().setBorderPainted(false);
					Main.buttonStorage.add(b);
					count++;
					b = null;
				}				
			}
		System.out.println("buttons done");
	}
	
	/**
	 * @param bo initialisiert Datenfeld stop
	 */
	public void setStop(boolean bo){
		this.stop = bo;
	}
}
