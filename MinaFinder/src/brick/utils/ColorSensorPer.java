package brick.utils;

import java.io.Reader;

import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;

/***
 * Sensor de color personalizado que permite emular un correcto comportamiento de 
 * getLigthValue similar al de los sensores de luz, donde se normaliza el valor
 * entre 0 y 100 en base a la calibracion (en la clase LeJOS no funciona bien).
 * @author ludo
 *
 */
public class ColorSensorPer extends ColorSensor {

	private int _zero = 0;
	private int _hundred = 1023;

	public ColorSensorPer(SensorPort port, int color) {
		super(port, color);
	}

	@Override
	public int getLightValue() {
		if(_hundred == _zero) return 0;
		return 100*(getNormalizedLightValue()- _zero)/(_hundred - _zero); 
	}

	@Override
	public void setLow(int low) {
		_zero = low;
	}

	@Override
	public void setHigh(int high) {
		_hundred = high;
	}
	
	
	

}
