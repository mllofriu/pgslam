package brick.particulas;

import java.util.StringTokenizer;

import brick.comm.MoveSerializable;
import brick.particulas.smodel.LineOrientMeassure;

public class UpdateCmd extends Command{

	private LineOrientMeassure m;

	public UpdateCmd(boolean line, float orientation, boolean tocoizq, boolean tocoder){
		m = new LineOrientMeassure(line, orientation, tocoizq, tocoder);
	}

	@Override
	public void execute(MCLPoseProvider mcl) {
		mcl.update(m);
	}
	
	@Override
	public String toString() {
		return "update,"+m.toString();
	}

	public static UpdateCmd fromString(String strLine) {
		StringTokenizer stok = new StringTokenizer(strLine, ",");
		if(stok.nextToken().equals("update")){
			boolean line = Boolean.parseBoolean(stok.nextToken());
			float orientation = Float.parseFloat(stok.nextToken());
			boolean tocoizq = Boolean.parseBoolean(stok.nextToken());
			boolean tocoder = Boolean.parseBoolean(stok.nextToken());
			return new UpdateCmd(line, orientation, tocoizq, tocoder);
		} else
			return null;
	}

	public float getOrientation() {
		return m.getOrientation();
	}
}
