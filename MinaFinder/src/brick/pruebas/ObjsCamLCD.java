package brick.pruebas;

import java.awt.Rectangle;
import java.util.List;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import brick.marcas.ObjCamara;
import brick.utils.Robot;

public class ObjsCamLCD {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Robot rob = new Robot();
		rob.initCam();
		
		while(!Button.ESCAPE.isPressed()){
			List<ObjCamara> objs = rob.getCam().getObjs();
			LCD.clear();
			int i = 0;
			for(ObjCamara oc : objs){
				Rectangle r = oc.getR();
				LCD.drawString(r.x + "," + r.y + "," + r.height + "," +
						r.width + ")" + " " + oc.getColor(), 0, i++);
			}
		}
	}

}
