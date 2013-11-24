package curtidor.utils;

import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;

public class WayPointOrden extends WayPoint {

	public enum Orden {desactivarUltra, activarUltra, none};
	private Orden orden;
	
	public WayPointOrden(Pose p) {
		super(p);
		setOrden(Orden.none);
	}

	public WayPointOrden(Pose p, Orden o){
		super(p);
		setOrden(o);
		headingRequired = false;
	}
	
	public WayPointOrden(Pose p, Orden o, boolean headingRequired){
		super(p);
		setOrden(o);
		this.headingRequired = headingRequired;
		if(headingRequired) this.maxHeadingError = 10;
	}

	public Orden getOrden() {
		return orden;
	}

	public void setOrden(Orden orden) {
		this.orden = orden;
	}
}
