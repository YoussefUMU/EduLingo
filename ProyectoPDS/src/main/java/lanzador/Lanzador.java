package lanzador;

import java.awt.EventQueue;

import javax.swing.UIManager;

import vista.*;

// Clase Lanzador, se encarga de lanzar el programa desde la ventana de login 

public class Lanzador {
	public static void main(final String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Estilo inicial
		            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

					VentanaLogin ventana = new VentanaLogin();
					ventana.setVisible(true);
				} catch (Exception e) {				
					e.printStackTrace();
				}
			}
		});
	}
}