package brick.angulo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import brick.marcas.Camara;
import brick.marcas.Detector;
import brick.marcas.ObjCamara;
import brick.marcas.ObservMarca;
import brick.utils.Robot;

public class DatosAng {

	private static final int CANT_MEDIDAS = 10;
	private static final int MAX_INTENTOS = 40;
	private static final float MIN_PROB = -40;

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		Robot r = new Robot();
		Camara c = r.getCam();
		Detector d = r.getDetect();
		EstPolAng estimador = new EstPolAng();


		for(int ang = 75; ang <= 120; ang += 5){
			LCD.drawString("Angulo: " + ang, 0, 0);
			Button.waitForPress();

			ObservMarca m = null;
			for(int intento = 0; intento < MAX_INTENTOS && m == null; intento++){
				m = encontrarMarca(c, d);
			}
			if(m != null){
				for(int medida = 0; medida < CANT_MEDIDAS; medida++){
					Sound.beep();
					m.setAngulo(ang - 90);
					estimador.agregarDato(m);
					do{
						m = encontrarMarca(c, d);
					} while(m == null);
				}

				Sound.beepSequence();
			} else {
				Sound.buzz();
			}
		}


		double[] terms = estimador.calcPoly();

		// Write properties file.
		Properties prop = new Properties();
		File f = new File("roMINA.properties");
		try {
			prop.load(new FileInputStream(f));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String coefs = "";
		for(double t : terms)
			coefs += t + ",";

		// Quito la ultima coma
		coefs = coefs.substring(0, coefs.length() - 1);
		prop.setProperty("coefsAng",  coefs);

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			prop.store(fos, null);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Thread.sleep(2000);

		Sound.beepSequenceUp();
		System.exit(0);
	}

	private static ObservMarca encontrarMarca(Camara c, Detector d) throws InterruptedException {
		Thread.sleep(50);
		List<ObjCamara> objs = c.getObjs();
		Thread.sleep(50);
		objs.addAll(c.getObjs());
		return d.detectarUna(objs, MIN_PROB);
	}

}
