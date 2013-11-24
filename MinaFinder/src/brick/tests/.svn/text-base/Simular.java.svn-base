package brick.tests;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.robotics.navigation.Pose;

import brick.particulas.Command;
import brick.particulas.MCLPoseProvider;
import brick.particulas.TruePosCmd;
import brick.particulas.UpdateCmdMarcas;
import brick.particulas.mapa.IEEEMarcasMap;
import brick.particulas.mapa.MapaMarcasLineas;
import brick.particulas.mmodel.GaussianMModel;
import brick.particulas.smodel.MarcaMeassure;
import brick.particulas.smodel.MarcaSModel;
import brick.utils.Constantes;

public class Simular {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		MapaMarcasLineas map = new IEEEMarcasMap();
		Pose initialPose = new Pose(300,200 , 0);
		MCLPoseProvider mcl = new MCLPoseProvider(
				null, Constantes.NUMPART, initialPose, 
				new MarcaSModel(),
				new GaussianMModel());
		
//		mcl.setPose(initialPose);
//		List<Command> cmds = CmdUtils.procesarCorrida(new File("vis2.log"));
		DataInputStream in = new DataInputStream(new FileInputStream(new File("vis2.log")));
		String strLine;

		double errorCorrida = 0;
		int count = 0;
		while ((strLine = in.readLine()) != null)   {
			Command c = Command.parseCommand(strLine);
			if(c instanceof TruePosCmd){
				Pose pose = mcl.getPose();

				Pose t = ((TruePosCmd)c).getPose();
				double dist = Math.sqrt(Math.pow(pose.getX() - t.getX(), 2)
						+ Math.pow(pose.getY() - t.getY(), 2));
				errorCorrida += dist;
				count++;
			} else {
				c.execute(mcl);
//				System.out.println(c.toString());
			}
		}
		
		System.out.println("Error promedio " + (errorCorrida / count));
		
		Sound.beepSequenceUp();
		
		Button.waitForPress();
	}

}
