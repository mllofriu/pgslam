package brick.distancia;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import lejos.robotics.navigation.Pose;
import brick.marcas.Detector;
import brick.marcas.ObservMarca;
import brick.utils.Robot;
import curtidor.utils.SyncDiffPilot;

/***
 * Clase dedicada a obtener muestras de marcas y su distancia medida por odometria corregida utilizando lineas negras equiespaciadas a LARGO_CUADRADO de distancia
 * Al finalizar la obtencion se estima un polinomio de interpolacion y se guardan sus coeficientes en el archivo de propiedades
 * 
 * @author ludo
 *
 */
public class DatDistPro implements Runnable{

	private static final double DIVISOR = 100;
	private static final double NORM_LIGTH = 1;
	private static final int CANT_MEDIDAS = 3;
	private static final int UMBRAL_NEGRO = 18;
	private static final int CANT_CUADRADOS = 3;
	private static final long ESPERA_MARCAS = 5000;
	private static final float LARGO_CUADRADO = 200;
	private static final int MULTI_VUELTA = 1;
	private static final double MULTI_IDA = 0.5;
	private static final int CANTMUESTRAS = 1000;
	private static final float MIN_X = 350;
	private static final float MAX_X = 1000;
	private Robot r;
	private boolean stop = false;
	private DataOutputStream dos = null;
	private EstPolDist estimador;
	
	public DatDistPro(){
		r = new Robot();
		estimador = new EstPolDist();

		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
				if (dos != null)
					try {
						dos.flush();
						dos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				System.exit(0);
			}

			@Override
			public void buttonPressed(Button b) {
			}
		});
		
		Button.LEFT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
				stop = !stop;
			}

			@Override
			public void buttonPressed(Button b) {
			}
		});

//		Button.ENTER.waitForPress();

		//		RConsole.open();

		new Thread(this).start();

		// Inicializo posicion mas o menos, se actualiza sola cuando pasa la primera linea
		r.getPp().setPose(new Pose(1100, 0, 180));
		// Recorro los cuadrados para adelante y atras
		SyncDiffPilot p = r.getPilot();
		while(true){
//			r.mDer.forward();
//			r.mIzq.forward();
//			p.forward();
			while(r.getPp().getPose().getX() > MIN_X){
//				System.out.println(r.getPp().getPose().getX());
				LCD.drawString("Dist: "+r.getPp().getPose().getX(), 0, 1);
				if(medirDer(r.luzDer) > UMBRAL_NEGRO){
					if (stop){
						if(p.isMoving())
							p.stop();
						else
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					} else {
//						r.mDer.forward();
//						r.mIzq.forward();
//						if(!p.isMoving())
						p.forward();
						// TODO: averiguar por que no retorna la luz entre 0 y 100
						int light = (int) (r.luzIzq.getLightValue() / NORM_LIGTH);
						r.mDer.setSpeed((int) ((1+ (light - 50) / DIVISOR) * Robot.TRAVELSPEED * MULTI_IDA));
						r.mIzq.setSpeed((int) ((1 - (light - 50) / DIVISOR) * Robot.TRAVELSPEED* MULTI_IDA));
//						System.out.println(r.getPp().getPose().getX());
					}					
				} else {
				
					while(medirDer(r.luzDer) <= UMBRAL_NEGRO){
						if (stop){
							if(p.isMoving())
								p.stop();
							else
								try {
									Thread.sleep(200);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						} else {
	//						r.mDer.forward();
	//						r.mIzq.forward();
//							if(!p.isMoving())
								p.forward();
							r.mDer.setSpeed((int) (Robot.TRAVELSPEED*MULTI_IDA));
							r.mIzq.setSpeed((int) (Robot.TRAVELSPEED*MULTI_IDA));
//							Sound.beep();
						}
					}	
					// Cuando salgo del negro conozco mi posicion, la normalizo
					normalizarPos(r);
				}
				
				
			}
			
			// Atras
			// Voy hasta la linea negra9
			
			p.setTravelSpeed(Robot.TRAVELSPEED);
			p.travel(Robot.TRACK);
			p.rotate(160);
			p.rotateLeft();
			while(r.luzDer.getLightValue() > UMBRAL_NEGRO);
			
			p.stop();
			
			
			p.forward();
			while(r.getPp().getPose().getX() < MAX_X){
				LCD.drawString("Dist: "+r.getPp().getPose().getX(), 0, 1);

				if(medirDer(r.luzIzq) > UMBRAL_NEGRO){
					// TODO: averiguar por que no retorna la luz entre 0 y 100
					int light = (int) (r.luzDer.getLightValue() / NORM_LIGTH);
//					System.out.println(light);
					r.mDer.setSpeed((int) ((1 + (light - 50) / DIVISOR / 2) * Robot.TRAVELSPEED * MULTI_VUELTA));
					r.mIzq.setSpeed((int) ((1 - (light - 50) / DIVISOR / 2) * Robot.TRAVELSPEED * MULTI_VUELTA));
				} else {
					while( medirDer(r.luzIzq) <= UMBRAL_NEGRO){
						r.mDer.setSpeed(Robot.TRAVELSPEED * MULTI_VUELTA);
						r.mIzq.setSpeed(Robot.TRAVELSPEED * MULTI_VUELTA);
//						Sound.beep();
					}
				}
			}
//			Sound.beepSequenceUp();
			
			// Voy hasta la linea, negra
			p.setTravelSpeed(Robot.TRAVELSPEED);
			p.travel(Robot.TRACK );
			p.rotate(-160);
			p.rotateRight();
			while(medirDer(r.luzIzq) >= UMBRAL_NEGRO);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(medirDer(r.luzIzq) < UMBRAL_NEGRO);
			
			p.stop();
			
			// Pequeño arco para corregir, hacia la izquierda y ladeado del centro
			p.arc(r.TRACK * 3, 10);
		}

	}

	private void normalizarPos(Robot r) {
		Pose p = r.getPp().getPose();
		int mul = Math.round(p.getX() / LARGO_CUADRADO);
		p.setLocation(mul * LARGO_CUADRADO, p.getY());
		r.getPp().setPose(p);
	}

	private int medirDer(LightSensor luzDer) {
		double avg = 0;
		for(int i = 0; i < CANT_MEDIDAS; i++)
			avg += luzDer.getLightValue() / (double)CANT_MEDIDAS;
//		System.out.println((int) avg);
		return (int) avg;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new DatDistPro();
	}

	@Override
	public void run() {
//		Detector d = new Detector();
//		Camara c = new Camara();
		Detector d = r.getDetect();
//		dos = null;
//		try {
//			File f = new File("marcas.txt");
//			if (!f.exists())
//				f.createNewFile();
//			
//			dos = new DataOutputStream(new FileOutputStream(f));
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		//		NXTConnection connection = USB.waitForConnection(); 
		//		LCD.drawString("Conexion iniciada.", 0, 0);
		//		DataOutputStream dos = connection.openDataOutputStream();

		// Espero a que se estabilize la distance
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		while(estimador.getCantDatos() < CANTMUESTRAS){
			List<ObservMarca> ms = d.detectar();
//			Sound.beep();
			if(!ms.isEmpty()){
				
				// Detengo el robot
				stop = true;
				// Duermo esperando estabilizar
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// Tomo el tiempo para despertarme en ESPERAMARCAS ms
				long time = System.currentTimeMillis();
				do {
					// La medida que me hizo detenerme se descarta
					ms = d.detectar();
					
					if(!ms.isEmpty()){
//						Sound.beepSequence();
						ObservMarca m = ms.get(0);
						m.setDistancia((int) r.getPp().getPose().getX());
						estimador.agregarDato(m);
//						try {
//							dos.writeChars(m.toString() + "\n");
//							cantMuestras++;
							LCD.drawString("Datos: "+estimador.getCantDatos(), 0, 0);
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						
//						RConsole.println(m.toString());
					}
					
				} while(System.currentTimeMillis() - time < ESPERA_MARCAS);
				
				stop = false;
			} 
//			else {
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				Sound.beep();
//			}

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
		prop.setProperty("coefsDist",  coefs);
		
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
		
		
//		try {
//			dos.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		Sound.beepSequenceUp();
		System.exit(0);
	}


}
