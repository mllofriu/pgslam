package brick.pruebas;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.Delay;
import brick.utils.Robot;

public class TDist {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Robot r = new Robot();
		
		while(!Button.ESCAPE.isPressed()){
			LCD.clear();
			LCD.drawString("Izq " + r.getDistIzq().getDistance() + " " + 
					"Der " + r.getDistDer().getDistance(), 0, 0);
			Delay.msDelay(300);
		}
	}

}
