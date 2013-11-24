package brick;

import java.util.StringTokenizer;

import brick.particulas.Command;
import brick.particulas.MCLPoseProvider;

public class DebugCommand extends Command {

	private String msg;

	public DebugCommand(String msg) {
		this.msg = msg;
	}

	@Override
	public void execute(MCLPoseProvider mcl) {

	}

	@Override
	public String toString() {
		return "debug," + msg;
	}

	public static Command fromString(String str) {
		StringTokenizer stok = new StringTokenizer(str, ",");
		if(stok.nextToken().equals("debug"))
			return new DebugCommand(stok.nextToken());
		else
			return null;
	}

}
