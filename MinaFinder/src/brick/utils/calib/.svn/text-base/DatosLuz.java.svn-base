package brick.utils.calib;

import brick.utils.Robot;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;

public class DatosLuz {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Button.ESCAPE.addButtonListener(new ButtonListener() {
//			@Override
//			public void buttonReleased(Button b) {
//				System.exit(0);
//			}
//
//			@Override
//			public void buttonPressed(Button b) {
//			}
//		});
		
		Robot r = new Robot();
		
		r.getPilot().forward();
		while (!Button.ENTER.isPressed()){
//			LCD.clear();
//			LCD.drawString(r.luzIzq.getNormalizedLightValue()+ " " + 
//					r.luzIzq.getLightValue() + " " +r.luzDer.getNormalizedLightValue() + " " +
//					r.luzDer.getLightValue(), 0, 0);
			if(r.luzIzq.getLightValue() < 90)
				System.out.println(r.luzIzq.getNormalizedLightValue()+ " " + 
						r.luzIzq.getLightValue() + " " +r.luzDer.getNormalizedLightValue() + " " +
						r.luzDer.getLightValue());
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		r.stop();
		
		Button.waitForPress();
	}

}
