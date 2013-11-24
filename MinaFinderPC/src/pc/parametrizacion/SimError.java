package pc.parametrizacion;

import java.io.File;
import java.util.List;

import lejos.robotics.navigation.Pose;
import brick.particulas.Command;
import brick.particulas.MCLPoseProvider;
import brick.particulas.TruePosCmd;
import brick.particulas.mapa.IEEEMarcasMap;
import brick.particulas.mapa.MapaMarcasLineas;
import brick.particulas.mmodel.GaussianMModel;
import brick.particulas.smodel.MarcaSModel;
import brick.tests.CmdUtils;
import brick.utils.Constantes;

public class SimError {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MapaMarcasLineas map = new IEEEMarcasMap();
		Pose initialPose = new Pose(300,200 , 0);
		Constantes.configure(200,1f/200*.874f, .02f, .11f, 1103);
		MCLPoseProvider mcl = new MCLPoseProvider(
				null, Constantes.NUMPART, initialPose, 
				new MarcaSModel(),
				new GaussianMModel());
//		mcl.setPose(initialPose);
		List<Command> cmds = CmdUtils.procesarCorrida(new File("./corridasEvol/corrida12"));
		double errorCorrida = 0;
		int count = 0;
		for(Command c : cmds){
			if(c instanceof TruePosCmd){
				Pose pose = mcl.getPose();

				Pose t = ((TruePosCmd)c).getPose();
				double dist = Math.sqrt(Math.pow(pose.getX() - t.getX(), 2)
						+ Math.pow(pose.getY() - t.getY(), 2));
				assert(!Double.isNaN(dist));
				errorCorrida += dist;
				count++;
			} else {
				c.execute(mcl);
//				System.out.println(c.toString());
			}
			System.out.println(c.toString());
		}
		
		System.out.println("Error promedio " + (errorCorrida / count));
	}

}
