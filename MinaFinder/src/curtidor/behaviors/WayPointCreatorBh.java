package curtidor.behaviors;

import java.util.ArrayList;

import brick.particulas.mapa.IEEEMarcasMap;
import brick.particulas.mapa.IEEERecorridaMap;
import brick.particulas.mapa.MapaIEEE;

import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;
import lejos.robotics.subsumption.Behavior;
import curtidor.utils.DelayNavPathController;
import curtidor.utils.WayPointGen;

public class WayPointCreatorBh implements Behavior {

	private DelayNavPathController nav;

	public WayPointCreatorBh(DelayNavPathController nav) {
		this.nav = nav;
	}

	@Override
	public boolean takeControl() {
		boolean res;
//		synchronized (nav) {
			res = nav.getWayPoint() == null;
//		}
		return res;
	}

	@Override
	public void action() {
		System.out.println("Action create way");
		// Creo waypoints dependiendo en la pos del robot
		Pose p = nav.getPoseProvider().getPose();
		WayPoint lejos;
		if(p.getX() > MapaIEEE.getAncho()/2)
			lejos = new WayPoint(-MapaIEEE.getAncho()/2, p.getY());
		else
			lejos = new WayPoint(MapaIEEE.getAncho()/2 * 3, p.getY());

//		ArrayList<WayPoint> wayp = new ArrayList<WayPoint>();
//		wayp.add(lejos);

		ArrayList<WayPoint> wayp = WayPointGen.brakeWaypoint(p, lejos);
		
		synchronized (nav) {
			nav.setRoute(wayp);
		}

		// Reestablezco parada por pared
//		WallStopBh.setEnable(true);

		System.out.println("Waypoints creados");
	}

	@Override
	public void suppress() {
		System.out.println("Supress Creat");
	}

}
