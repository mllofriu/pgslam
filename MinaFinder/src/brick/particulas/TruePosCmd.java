package brick.particulas;

import java.util.StringTokenizer;

import lejos.robotics.navigation.Pose;

/***
 * No es un comando real, pero se lo toma como comando para uniformidad
 * @author ludo
 *
 */
public class TruePosCmd extends Command {

	private float x;
	private float y;
	
	public TruePosCmd(float x, float y){
		this.x = x;
		this.y = y;
	}
	@Override
	public void execute(MCLPoseProvider mcl) {
		// No hace nada
	}
	
	public String toString(){
		return "truepos,"+x+","+y;
	}
	
	public static TruePosCmd fromString(String str){
		StringTokenizer stok = new StringTokenizer(str, ",");
		if(stok.nextToken().equals("truepos")){
			float x = Float.parseFloat(stok.nextToken());
			float y = Float.parseFloat(stok.nextToken());
			return new TruePosCmd(x, y);
		} else
			return null;
		
	}
	public Pose getPose() {
		return new Pose(x,y, 0);
	}

}
