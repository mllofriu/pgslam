package brick.marcas;

import java.awt.Rectangle;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lejos.nxt.SensorPort;
import brick.utils.NXTCam;
//import lejos.nxt.addon.NXTCam;

public class Camara {

	public static final int TAM_PANTALLA = 176;
	public static final int FRAMESSEC = 30;
	private NXTCam camera;

	public Camara(){
		initCam();
		
//		LCD.drawString("Iniciando con...", 0, 0);
//		NXTConnection connection = USB.waitForConnection(); 
//		LCD.drawString("Conexion iniciada.", 0, 0);
//		DataOutputStream dos = connection.openDataOutputStream();
	}
	
	private void initCam() {
		camera = new NXTCam(SensorPort.S3);
//		camera.sendCommand('R');
//		try {
//			Thread.sleep(200);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		camera.sendCommand('A'); // sort objects by size
		camera.sendCommand('E'); // start tracking
//		camera.sendCommand('M'); // disable sleep mode 
	}

	public Camara(DataOutputStream dos){
		camera = new NXTCam(SensorPort.S3);
		camera.sendCommand('A'); // sort objects by size
		camera.sendCommand('E'); // start tracking
		
	}
	
	public List<ObjCamara> getObjs(){
		List<ObjCamara> objs = new ArrayList<ObjCamara>();
		
		boolean exito = camera.readAll();
		if(!exito)
			initCam();
		
		int numObjects = camera.getNumberOfObjects();
		if(numObjects <= 8){
			for(int i = 0; i < camera.getNumberOfObjects(); i++){
				Rectangle r = camera.getRectangle(i);
				int color = camera.getObjectColor(i);
				objs.add(new ObjCamara(r, color));
//				if(dos != null)
//					try {
//						dos.writeChars("Cubo en (" + r.x + "," + r.y + "," + r.height + "," +
//								r.width + ")" + " de color " + color + "|");
//					} catch (IOException e) {
//					}
			}
//			if(dos != null)
//				try {
//					dos.writeChars("\n");
//				} catch (IOException e) {
//				}
		}
		
		return objs;
	}
}
