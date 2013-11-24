package brick.pruebas;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.comm.RConsole;

public class PruebaDist {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RConsole.openUSB(0);
		
		OpticalDistanceSensor distancia = new OpticalDistanceSensor(SensorPort.S3);
		
		Button.ENTER.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
				System.exit(0);
			}
			@Override
			public void buttonPressed(Button b) {
			}
		});
		
//		new Buscador().buscar();
		while(true){
//			System.out.println("MSB: " + distancia.getDistMSB());
//			System.out.println("LSB: " + distancia.getDistLSB());
			RConsole.println("LSB: " + distancia.getDistLSB());
		}
	}

}
