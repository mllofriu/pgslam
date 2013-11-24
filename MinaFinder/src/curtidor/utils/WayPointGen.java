package curtidor.utils;

import java.util.ArrayList;

import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;

public class WayPointGen {

	private static final float MIN_INTERRUPT = 200;

	public static ArrayList<WayPoint> brakeWaypoint(Pose orig, WayPoint w)
	{
		ArrayList<WayPoint> res = new ArrayList<WayPoint>();
		
		float dist = orig.distanceTo(w.getPose().getLocation());
		int interrupts = (int) Math.floor(dist / MIN_INTERRUPT);
		
		double diffX =  w.getX() - orig.getX();
		double diffY = w.getY() - orig.getY();
		for(int i = 1; i <= interrupts; i++){
			double x = orig.getX() + (i * MIN_INTERRUPT / dist) * diffX;
			double y = orig.getY() + (i * MIN_INTERRUPT / dist) * diffY;
			res.add(new WayPoint(x, y));
		}
		
		// Si ya agregue el ulitmo, lo borro para poner el que me pasaron
		if (interrupts * MIN_INTERRUPT >= dist)
			res.remove(res.size() - 1);
		
		res.add(w);
		
		return res;
	}
}
