package brick.tests;

import java.awt.Rectangle;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import brick.BotonBh;
import brick.marcas.Camara;
import brick.marcas.Detector;
import brick.marcas.ObjCamara;
import brick.marcas.ObservMarca;
import brick.utils.NXTCam;
import brick.utils.Robot;

public class TestDetector {

	private static final int CANT_ITERS  = 2;
	private static boolean connect = false;
	private static DataOutputStream dos;

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {


		Robot rob = new Robot();
		if (connect ){
			LCD.drawString("Waiting for con...", 0, 0);
			NXTConnection connection = USB.waitForConnection();
			LCD.drawString("Conection done.", 0, 0);
			dos = connection.openDataOutputStream();
			dos.writeChars("Conexion Iniciada\n");
			dos.flush();
		}

		final NXTCam camera = new NXTCam(SensorPort.S3);
		camera.sendCommand('A'); // sort objects by size
		camera.sendCommand('E'); // start tracking
		final List<ObjCamara> objs = new ArrayList<ObjCamara>();
		final Detector det = new Detector();
//		rob.getPilot().rotateLeft();
		new Thread(new Runnable(){

			@Override
			public void run() {
				int iters = 0;
				while(!Button.ESCAPE.isPressed()){
					camera.readAll();
					int numObjects = camera.getNumberOfObjects();
					if(numObjects <= 8){

						for(int i = 0; i < camera.getNumberOfObjects(); i++){
//							Sound.beep();
							Rectangle r = camera.getRectangle(i);
							int color = camera.getObjectColor(i);
							objs.add(new ObjCamara(r, color));

						}

//						objs = det.filter(objs);

					}
					if(iters % CANT_ITERS == 0){
						for (ObjCamara oc : objs){
							Rectangle r = oc.getR();
							if(connect)
								try {
									dos.writeChars("Cubo en (" + r.x + "," + r.y + "," + r.height + "," +
											r.width + ")" + " de color " + oc.getColor() + "|");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
						if (connect){
							try {
								dos.writeChars("\n");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								dos.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						List<ObservMarca> marcas = det.detectar(objs);
						if(!marcas.isEmpty()){
							Sound.beep();
							System.out.println(marcas.get(0).getId() + " " + marcas.get(0).getProb());
						}

						iters = 0;
						objs.clear();
					}
					try {
						Thread.sleep(Math.round(1./Camara.FRAMESSEC*1000));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					iters++;
				}
			}
			
		}).start();
		
		Behavior boton = new BotonBh();
		Behavior[] comps = {boton};

		Arbitrator arb = new Arbitrator(comps);
		arb.start();
	}

}
