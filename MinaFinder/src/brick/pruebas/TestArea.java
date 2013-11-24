package brick.pruebas;

import java.util.List;

import brick.marcas.Camara;
import brick.marcas.Detector;
import brick.marcas.ObservMarca;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;

public class TestArea {

	protected static boolean capturar;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		capturar = false;
		
		Button.ENTER.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
				System.exit(0);
			}

			@Override
			public void buttonPressed(Button b) {
				// TODO Auto-generated method stub

			}
		});
		
		Button.ESCAPE.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
				RConsole.println("Cambio");
				capturar = !capturar;
			}

			@Override
			public void buttonPressed(Button b) {
				// TODO Auto-generated method stub

			}
		});
		RConsole.openUSB(0);
		Detector d = new Detector();
		Camara c = new Camara();
		while(true){
			List<ObservMarca> marcas = d.detectar(c.getObjs());
			if (!marcas.isEmpty() && capturar)	{
				ObservMarca m = marcas.get(0);
				RConsole.println(""+m.getR().getBounds().height);
//				System.out.println("Distancia: " + (m.getR().getBounds().height * -0.46 + 84));
			}
				
		}
		
	}

}
