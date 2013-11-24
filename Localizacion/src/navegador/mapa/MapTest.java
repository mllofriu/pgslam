package navegador.mapa;
import java.awt.FlowLayout;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;

import main.MapCanvas;


public class MapTest {

	public static void main(String[] args)
	{
		JFrame mainFrame = new JFrame("Mapa");
		mainFrame.getContentPane().setLayout(new FlowLayout());
		MapCanvas canvas = new MapCanvas(MapaIEEE.getIEEEMap(), null, null);
		mainFrame.getContentPane().add(canvas);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
}
