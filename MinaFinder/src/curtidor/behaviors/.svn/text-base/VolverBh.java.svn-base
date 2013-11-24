package curtidor.behaviors;

import java.util.ArrayList;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;
import lejos.robotics.subsumption.Behavior;
import brick.utils.Constantes;
import curtidor.utils.DelayNavPathController;
import curtidor.utils.WayPointGen;
import curtidor.utils.WayPointOrden;

public class VolverBh implements Behavior {
	
	private DelayNavPathController nav;

	public VolverBh(DelayNavPathController nav){
		this.nav = nav;
	}

	@Override
	public boolean takeControl() {
		return Button.ENTER.isPressed();
	}

	@Override
	public void action() {
		Sound.beepSequence();
		
		Pose p = nav.getPoseProvider().getPose();
		WayPoint inicio = new WayPoint(Constantes.initialPose);
		// Al volver reactivo los ultrasonido
		WayPoint volver = new WayPointOrden(p, WayPointOrden.Orden.activarUltra);
		ArrayList<WayPoint> wayp = nav.getRoute();
		// Inserto al principio y al reves, para que queden los ultimos primero
		wayp.addAll(0,WayPointGen.brakeWaypoint(Constantes.initialPose,volver));
		wayp.addAll(0,WayPointGen.brakeWaypoint(p, inicio));
		nav.setRoute(wayp);
		
		WallStopBh.setEnable(false);
	}

	@Override
	public void suppress() {

	}

}
