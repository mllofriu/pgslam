package brick.pruebas;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroSensor;

public class SegWay {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GyroSensor gyro = new GyroSensor(SensorPort.S1);
		
		while(true){
			
			LCD.drawInt(gyro.readValue(), 0, 0);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
