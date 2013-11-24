package brick.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import curtidor.utils.SyncDiffPilot;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.util.Delay;
import brick.angulo.EstimadorAngulo;
import brick.distancia.EstimadorDistancia;
import brick.marcas.Camara;
import brick.marcas.Detector;

/***
 * Centraliza la inicializacion de todas las estructuras necesarias para el control
 * del robot fisico y las funciones de sensado.
 * @author ludo
 *
 */
public class Robot {

	public UltrasonicSensor getDistDer() {
		return distDer;
	}

	public UltrasonicSensor getDistIzq() {
		return distIzq;
	}

	private static final double WHEEL = 60;
	public static final double TRACK = 194;
	public static final int TRAVELSPEED = 100;
	public static final int ROTATESPEED = TRAVELSPEED/6;
	private static final double AJUSTE_IZQ = -0.25;
	public static final double DIST_SENSORES_MEDIO = 110;
	public static final double DIST_ENTRE_SENSORES = 140;
	private static final int LUZ_THRS = 35;
	private static final int ACELERATION = 1000;
	private static final int MAXACELERATION = 6000;
	public static final float DIST_CAM_CENTRO = 00;
	public static final int SPEED_BRAZO = 20;
	public LightSensor luzIzq;
	public LightSensor luzDer;
	public UltrasonicSensor distDer;
	public UltrasonicSensor distIzq;
	private SyncDiffPilot pilot;;
	public static NXTRegulatedMotor mIzq = Motor.A;
	public static NXTRegulatedMotor mDer = Motor.C;
	private static NXTRegulatedMotor brazo = Motor.B;
	private OdometryPoseProvider pp;
	private EstimadorDistancia estDist;
	private Camara cam = null;
	private Detector detector;
	DataOutputStream dos;
	//	private CompassSensor compass;
	private DataInputStream dis;
	private EstimadorAngulo estAng;
	private Thread hiloBrazo;


	public Robot(){
//		initCam();
		
		pilot = Robot.instantiatePilot();
		pilot.setTravelSpeed(TRAVELSPEED);
		pilot.setRotateSpeed(ROTATESPEED);
		pilot.setAcceleration(ACELERATION);

		hiloBrazo = new Thread(new Runnable(){
			@Override
			public void run() {
				brazo.setStallThreshold(1,12);
				brazo.setSpeed(20);
				brazo.forward();
				
				Delay.msDelay(2000);
				
				while(brazo.isMoving())
					Delay.msDelay(100);
				
				brazo.flt();
				
				Delay.msDelay(1000);
				
				brazo.stop();
				
				brazo.setStallThreshold(50, 1000);
				brazo.rotate(-70, false);
//				System.out.println(r.getBrazo().getTachoCount());
//				
				brazo.setSpeed(Robot.SPEED_BRAZO);
				brazo.resetTachoCount();
			}
		});
		hiloBrazo.start();
	
		brazo.setSpeed(SPEED_BRAZO);

		//		compass = new CompassSensor(SensorPort.S3);		

		setPp(new OdometryPoseProvider(pilot));

		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("roMINA.properties")));
		} catch (IOException e) {
		}

//		luzIzq = new LightSensor(SensorPort.S1);
//		luzDer = new LightSensor(SensorPort.S2);
		distIzq = new UltrasonicSensor(SensorPort.S1);
		distDer = new UltrasonicSensor(SensorPort.S2);

		//		// Calibro usando properties
//		luzDer.setHigh(new Integer(prop.getProperty("DerHi")).intValue());
//		luzDer.setLow(new Integer(prop.getProperty("DerLow")).intValue());
//		luzIzq.setHigh(new Integer(prop.getProperty("IzqHi")).intValue());
//		luzIzq.setLow(new Integer(prop.getProperty("IzqLow")).intValue());

		estDist = new EstimadorDistancia(prop);

		estAng = new EstimadorAngulo(prop);



		// Sleep para estabilizar voltajes motores
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//		cam = new Camara();
		////		
		//		detector = new Detector(cam);
	}

	public EstimadorAngulo getEstAng() {
		return estAng;
	}

	public void setEstAng(EstimadorAngulo estAng) {
		this.estAng = estAng;
	}

	public static SyncDiffPilot instantiatePilot() {
//		DifferentialPilot p = new DifferentialPilot(WHEEL + AJUSTE_IZQ, WHEEL, TRACK, mIzq ,mDer, false);
		SyncDiffPilot p = new SyncDiffPilot(WHEEL + AJUSTE_IZQ, WHEEL, TRACK, mIzq ,mDer, false);
		p.setMinRadius(0);
		return p;
	}

	//	public void calibrateCompass() {
	//		// Inicializo un differential pilot para calibrar
	//		final int rotSpeed = 10;
	//		pilot.setRotateSpeed(rotSpeed);
	//		compass.startCalibration();
	//		pilot.rotate(720);
	//		compass.stopCalibration();
	//		Sound.beepSequenceUp();	
	//		pilot.setRotateSpeed(ROTATESPEED);
	//	}

	//	public CompassSensor getCompass() {
	//		return compass;
	//	}
	//
	//	public void setCompass(CompassSensor compass) {
	//		this.compass = compass;
	//	}

	public SyncDiffPilot getPilot(){
		return pilot;
	}

	public OdometryPoseProvider getPp() {
		return pp;
	}

	public void setPp(OdometryPoseProvider pp) {
		this.pp = pp;
	}

	public EstimadorDistancia getEstimadorDist() {
		return estDist;
	}

	public void setEstimadorDist(EstimadorDistancia estDist) {
		this.estDist = estDist;
	}

	public Camara getCam() {
		return cam;
	}

	public void setCam(Camara cam) {
		this.cam = cam;
	}

	public Detector getDetect() {
		return detector;
	}

	public void setDetect(Detector detector) {
		this.detector = detector;
	}

	public void stop(){
		pilot.setAcceleration(MAXACELERATION);
		pilot.stop();
		pilot.setAcceleration(ACELERATION);
	}

	public int getLuzIzq(){
		return luzIzq.getLightValue();
	}

	public int getLuzDer(){
		return luzDer.getLightValue();
	}

	public boolean hayLineaIzq(){
		return getLuzIzq() < LUZ_THRS;
	}

	public boolean hayLineaDer(){
		return getLuzDer() < LUZ_THRS;
	}

	public boolean hayLinea() {
		return hayLineaDer() || hayLineaIzq();
	}

	public boolean hayLineaAmbos() {
		return hayLineaDer() && hayLineaIzq();
	}

	public void setSpeed(double speed) {
		pilot.setTravelSpeed(speed);
		pilot.setRotateSpeed(speed/8);
	}

	public void resetSpeed(){
		// Workaround a un bug, ultima velocidad es la que importa
		if(pilot.getMovement().getMoveType() == Move.MoveType.ROTATE){
			pilot.setTravelSpeed(TRAVELSPEED);
			pilot.setRotateSpeed(ROTATESPEED);
		} else {
			pilot.setRotateSpeed(ROTATESPEED);
			pilot.setTravelSpeed(TRAVELSPEED);
		}

		brazo.setSpeed(SPEED_BRAZO);


	}

	public void initCam() {
		if(cam == null){
			cam = new Camara();
			detector = new Detector(cam);
		}
	}

	public NXTRegulatedMotor getBrazo(){
		if (hiloBrazo.isAlive())
			try {
				hiloBrazo.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return brazo;
	}

	public void resetSpeedBrazo() {
		brazo.setSpeed(SPEED_BRAZO);
	}
}
