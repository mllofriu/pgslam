package brick.pruebas;

import lejos.nxt.Button;
import brick.utils.Robot;

public class PSetAcel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Robot r = new Robot();
		r.getPilot().setAcceleration(300);
		r.getPilot().forward();
		
		Button.ESCAPE.waitForPressAndRelease();
	}

}
