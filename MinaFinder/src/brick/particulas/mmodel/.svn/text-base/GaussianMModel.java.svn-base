package brick.particulas.mmodel;

import lejos.geom.Point;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.Pose;
import brick.utils.Constantes;
import brick.utils.Pgrand;

public class GaussianMModel implements MotionModel{
	private static Pgrand rand;

	
	public GaussianMModel(){
		 rand = new Pgrand();
	}

	@Override
	public Pose applyMove(Move move, Pose origPose) {
		float ym = (move.getDistanceTraveled() * ((float) Math.sin(Math.toRadians(origPose.getHeading()))));
		float xm = (move.getDistanceTraveled() * ((float) Math.cos(Math.toRadians(origPose.getHeading()))));

		Pose pose = new Pose();
		pose.setLocation(new Point((float) (origPose.getX() + xm + (Constantes.distanceNoiseFactor * xm * rand.getRand().nextGaussian())),
				(float) (origPose.getY() + ym + (Constantes.distanceNoiseFactor * ym * rand.getRand().nextGaussian()))));
		float h = (float) (origPose.getHeading() + move.getAngleTurned()
				+ (Constantes.angleNoiseFactor  * move.getAngleTurned() * rand.getRand().nextGaussian()));
		if(h > 180)
			h -= 360;
		else if (h < -180)
			h += 360;
		
		pose.setHeading(h);
//		pose.setHeading((float) (pose.getHeading() /*+ 0.5f*/) % 360);
		
		return pose;
	}

	@Override
	public String toString() {
		return Constantes.distanceNoiseFactor + "," + Constantes.angleNoiseFactor;
	} 
	
	
	

}
