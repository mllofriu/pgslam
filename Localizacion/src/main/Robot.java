package main;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.CompassSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.localization.MCLPoseProvider;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.CompassPilot;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.NavPathController;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.DijkstraPathFinder;
import lejos.robotics.pathfinding.FourWayGridMesh;
import lejos.robotics.pathfinding.NodePathFinder;
import navegador.mapa.MapaIEEE;

/**
 * Clase que centraliza la configuración de los sistemas de navegación y planificación
 * @author ludo
 *
 */
public class Robot {

	public static final double WHEEL_DIAM = 57;
	public static final float TRACK_WIDTH = 122;
	private static final RegulatedMotor LEFTMOT = Motor.A;
	private static final RegulatedMotor RIGTHMOT = Motor.B;

	NavPathController pathController;
	
	@SuppressWarnings("deprecation")
	public Robot()
	{
		CompassSensor compass = new CompassSensor(SensorPort.S1);
		CompassPilot cPilot = new CompassPilot(
				compass, (float)WHEEL_DIAM,(float) TRACK_WIDTH, LEFTMOT, RIGTHMOT);
		
		// Calibro accediendo directamente al sensor
		// No es necesario calibrar siempre, comentar despues de una calibracion
//		calibrateCompass(compass);
		
		
		cPilot.setTravelSpeed(100);
		cPilot.setRotateSpeed(40);
		FourWayGridMesh fwgm = new FourWayGridMesh(MapaIEEE.getIEEEMap(),
				200, (float) Robot.TRACK_WIDTH/2);
		NodePathFinder nPathFinder = new NodePathFinder(new AstarSearchAlgorithm(), fwgm);
		pathController = new NavPathController(cPilot,	new OdometryPoseProvider(cPilot)
				,nPathFinder);
	}

	/**
	 * Método que calibra el compass. Utilizando un DifferentialPilot da dos vueltas bien lento.
	 * No es necesario llamarlo cada vez, pues los parámetros quedan grabados en el firmware.
	 * @param compass
	 */
	private void calibrateCompass(CompassSensor compass) {
		// Inicializo un differential pilot para calibrar
		DifferentialPilot dPilot = new DifferentialPilot((float)WHEEL_DIAM,(float) TRACK_WIDTH, LEFTMOT, RIGTHMOT);
		final int rotSpeed = 10;
		dPilot.setRotateSpeed(rotSpeed);
		compass.startCalibration();
		dPilot.rotate(720);
		compass.stopCalibration();
		Sound.beepSequenceUp();	
	}

	public NavPathController getPathController() {
		return pathController;
	}
}
