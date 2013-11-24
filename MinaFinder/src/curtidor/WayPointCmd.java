package curtidor;

import java.util.StringTokenizer;

import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;
import brick.particulas.Command;
import brick.particulas.MCLPoseProvider;
import curtidor.utils.PoseStr;

public class WayPointCmd extends Command {

	private WayPoint wp;

	public WayPointCmd(WayPoint wp) {
		this.wp = wp;
	}

	@Override
	public void execute(MCLPoseProvider mcl) {

	}

	@Override
	public String toString() {
		return "wayPoint,"+new PoseStr(wp.getPose()).toString();
	}

	public static Command fromString(String str) {
		StringTokenizer stok = new StringTokenizer(str, ",");
		if(stok.nextToken().equals("wayPoint")){
			Pose p = PoseStr.fromStr(stok);
			return new WayPointCmd(new WayPoint(p));
		} else
			return null;
		
	}

	public WayPoint getWP() {
		return wp;
	}

}
