package brick.tests;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import brick.marcas.Camara;
import brick.marcas.Detector;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.NXTCam;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.RConsole;
import lejos.nxt.comm.USB;


/***
 * Imprime en consola las coordenadas del cubo comida. Sirve para probar calibracion.
 * Hay que ejecutar nxjconsole despues de que inicia el programa.
 * 
 * @author ludo
 *
 */
public class BTCamTest {


	public static void main(String[] args){	
		

		
		LCD.drawString("Iniciando con...", 0, 0);
		NXTConnection connection = USB.waitForConnection(); 
		LCD.drawString("Conexion iniciada.", 0, 0);
		DataOutputStream dos = connection.openDataOutputStream();
		
		Camara c = new Camara(dos);
		Detector d = new Detector(dos);
		try {
			dos.writeChars("Conexion Iniciada\n");
			dos.flush();
			while(!Button.ESCAPE.isPressed()){
				d.detectar(c.getObjs());
				Thread.sleep(300);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}



