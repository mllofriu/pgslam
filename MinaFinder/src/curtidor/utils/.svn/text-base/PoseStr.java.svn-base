package curtidor.utils;

import java.util.StringTokenizer;

import lejos.robotics.navigation.Pose;
import lejos.util.Delay;

public class PoseStr extends Pose {

	public PoseStr(float x, float y, float h){
		super(x,y,h);
	}
	
	public PoseStr(Pose pose) {
		super(pose.getX(), pose.getY(), pose.getHeading());
	}

	public String toString(){
		return getX() +","+getY() +","+getHeading();
	}
	
	public static PoseStr fromStr(String poseStr){
//		System.out.println("Cadena :" + poseStr);
//		Delay.msDelay(10000);
		StringTokenizer stok = new StringTokenizer(poseStr, ",");
		float x = Float.parseFloat(stok.nextToken());
		float y = Float.parseFloat(stok.nextToken());
		float h = Float.parseFloat(stok.nextToken());
		
		return new PoseStr(x,y,h);
	}
	
	public static PoseStr fromStr(StringTokenizer stok){
		float x = Float.parseFloat(stok.nextToken());
		float y = Float.parseFloat(stok.nextToken());
		float h = Float.parseFloat(stok.nextToken());
		
		return new PoseStr(x,y,h);
	}
}
