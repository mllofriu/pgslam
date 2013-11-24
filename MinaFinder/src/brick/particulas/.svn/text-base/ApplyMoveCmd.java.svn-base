package brick.particulas;

import java.io.Serializable;
import java.util.StringTokenizer;

import brick.comm.MoveSerializable;
import lejos.robotics.navigation.Move;

public class ApplyMoveCmd extends Command{
	
	private Move m;

	public ApplyMoveCmd(Move m){
		this.m = m;
	}

	@Override
	public void execute(MCLPoseProvider mcl) {
		mcl.applyMove(m);
	}

	@Override
	public String toString() {
		return "applymove,"+new MoveSerializable(m).toString();
	}

	public static Command fromString(String strLine) {
		StringTokenizer stok = new StringTokenizer(strLine, ",");
		if(stok.nextToken().equals("applymove")){
			return new ApplyMoveCmd(MoveSerializable.fromString(stok));
		} else
			return null;
	}
	
	
	
	

}
