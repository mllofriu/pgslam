package brick.particulas;

import java.util.StringTokenizer;

public class ExitCommand extends Command {

	@Override
	public void execute(MCLPoseProvider mcl) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	@Override
	public String toString() {
		return "exit";
	}

	public static Command fromString(String str) {
		StringTokenizer stok = new StringTokenizer(str, ",");
		if(stok.nextToken().equals("exit"))
			return new ExitCommand();
		else
			return null;
	}

}
