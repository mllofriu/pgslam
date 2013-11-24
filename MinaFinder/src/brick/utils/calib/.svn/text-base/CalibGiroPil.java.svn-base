package brick.utils.calib;

import brick.utils.Robot;
import lejos.nxt.Button;

/***
 * Clase que permite calibrar el trackwidth para ajustar los giros.
 * Si agrando el trackwidth el robot gira mas
 * Se debe ejecutar luego de calibrar el radio de las ruedas (Ver CalibDistPil.java)
 * @author ludo
 *
 */
public class CalibGiroPil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Robot r = new Robot();
		
		Button.ENTER.waitForPress();

		
		r.getPilot().rotate(360);
	}

}
