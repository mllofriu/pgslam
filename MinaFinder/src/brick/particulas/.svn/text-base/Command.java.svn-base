package brick.particulas;

import java.util.StringTokenizer;

import brick.DebugCommand;
import curtidor.WayPointCmd;

public abstract class Command {

	public abstract void execute(MCLPoseProvider mcl);
	
	public abstract String toString();
	
	public static Command parseCommand(String str) {
		StringTokenizer stok = new StringTokenizer(str, ",");
		String tipo = stok.nextToken();
		if(tipo.equals("applymove")){
			return ApplyMoveCmd.fromString(str);
		} else if(tipo.equals("update")){
			return UpdateCmd.fromString(str);
		} else if(tipo.equals("updateMarcas")){
			return UpdateCmdMarcas.fromString(str);
		} else if(tipo.equals("truepos")){
			return TruePosCmd.fromString(str);
		} else if(tipo.equals("exit")){
			return ExitCommand.fromString(str);
		} else if(tipo.equals("debug")){
			return DebugCommand.fromString(str);
		} else if(tipo.equals("wayPoint")){
			return WayPointCmd.fromString(str);
		}
		
		return null;
	}
	
}
