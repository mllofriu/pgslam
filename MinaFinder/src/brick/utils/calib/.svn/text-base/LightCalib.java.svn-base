package brick.utils.calib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import brick.utils.Robot;


import lejos.nxt.Button;
import lejos.nxt.LCD;

/**
 * Clase para calibrar los sensores de luz
 * Aparentemente, la calibración no queda grabada, por lo que hay 
 * que obtener los valores a mano y manejarse (setHigh setLow)
 * @author ludo
 *
 */
public class LightCalib {

	public static void main(String[] args)
	{
		
		
		// Write properties file.
		Properties prop = new Properties();
		
		try {
			File f = new File("roMINA.properties");
			if (!f.exists()){
				f.createNewFile();
				prop.setProperty("IzqLow", "" + 0);
				prop.setProperty("DerLow", "" + 0);
				prop.setProperty("IzqHi", "" + 0);
				prop.setProperty("DerHi", "" + 0);
				FileOutputStream fos = new FileOutputStream(f);
				prop.store(fos, null);
				fos.close();
			}
				
			prop.load(new FileInputStream(f));
		} catch (IOException e) {
		}
		
		Robot robot = new Robot();
		
		// Colocar el robot sobre el negro - ambos sensores
		LCD.drawString("Sobre negro", 0, 0);
		Button.ENTER.waitForPress();
		LCD.drawString("Izq low " + robot.luzIzq.getNormalizedLightValue(), 0, 1);
		prop.setProperty("IzqLow", "" + robot.luzIzq.getNormalizedLightValue());
		LCD.drawString("Der low " + robot.luzDer.getNormalizedLightValue(), 0, 2);
		prop.setProperty("DerLow", "" + robot.luzDer.getNormalizedLightValue());
		LCD.clear(0);
		
		// Colocar el robot sobre el negro - ambos sensores
		LCD.drawString("Sobre blanco", 0, 0);
		Button.ENTER.waitForPress();
		LCD.drawString("Izq hi " + robot.luzIzq.getNormalizedLightValue(), 0, 3);
		prop.setProperty("IzqHi", "" + robot.luzIzq.getNormalizedLightValue());
		LCD.drawString("Der hi " + robot.luzDer.getNormalizedLightValue(), 0, 4);
		prop.setProperty("DerHi", "" + robot.luzDer.getNormalizedLightValue());
		
		Button.ENTER.waitForPress();

				
		try {
			FileOutputStream fos = new FileOutputStream(new File("roMINA.properties"));
			prop.store(fos, null);
			fos.close();
		} catch (IOException e) {
		}
		
		
	}
}
