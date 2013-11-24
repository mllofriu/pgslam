package brick.tests;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import brick.particulas.Command;

public class CmdUtils {

	public static List<Command> procesarCorrida(File f) {
		List<Command> cmds = new ArrayList<Command>();
		try {
//			BufferedReader in = new BufferedReader(new FileReader(f));
			DataInputStream in = new DataInputStream(new FileInputStream(f));
			String strLine;

			while ((strLine = in.readLine()) != null)   {
				cmds.add(Command.parseCommand(strLine));
			}


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cmds;
	}
}
