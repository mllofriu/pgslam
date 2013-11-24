package brick.pruebas;

import brick.marcas.ObservMarca;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.NXTCam;
import lejos.nxt.comm.RConsole;

public class TDistHeight {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NXTCam camera = new NXTCam(SensorPort.S4);
		camera.sendCommand('A'); // sort objects by size
		camera.sendCommand('E'); // start tracking

		RConsole.openUSB(0);
		Buscador b = new Buscador();
		
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

		
		while(true){
			ObservMarca m = b.buscar();
			RConsole.println(m.getId() + ":" + m.getDistancia());
		}

	}

}
