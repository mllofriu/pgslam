package pc.comm;

import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;
import pc.particulas.ParticleFrame;
import brick.comm.Mensaje;
import brick.utils.Observable;
import brick.utils.Observer;
import curtidor.utils.PoseStr;

public class WPUpdater implements Observer{


	private ParticleFrame frame;

	public WPUpdater(ParticleFrame frame, Comunication com) {
		com.addObserver(this);
		this.frame = frame;
	}

	@Override
	public void update(Observable o, Object arg) {
//		System.out.println((Mensaje)arg);
		Mensaje m = (Mensaje) arg;
		
		if(m.getId() == Comunication.IDWAYPOINT){ //fijarme el id
			Pose p = PoseStr.fromStr(m.getStr());
			frame.setNextWaypoint(new WayPoint(p));
		}
	}
}
