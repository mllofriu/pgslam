package brick.pruebas;

import lejos.nxt.Button;

public class CurrTime {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		
		Button.waitForPress();
	}

}
