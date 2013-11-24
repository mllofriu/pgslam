package pc.parametrizacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.JFileChooser;

import lejos.robotics.navigation.Pose;
import pc.particulas.ParticleFrame;
import brick.particulas.Command;
import brick.particulas.MCLPoseProvider;
import brick.particulas.TruePosCmd;
import brick.particulas.UpdateCmdMarcas;
import brick.particulas.mapa.IEEEMarcasMap;
import brick.particulas.mapa.MapaMarcasLineas;
import brick.particulas.mmodel.GaussianMModel;
import brick.particulas.smodel.MarcaMeassure;
import brick.particulas.smodel.MarcaSModel;
import brick.tests.CmdUtils;
import brick.utils.Constantes;

public class Visualizador {


	public static void main(String[] args) throws IOException
	{
		//		LineMap map = MapaIEEE.getIEEEMap();
		MapaMarcasLineas map = new IEEEMarcasMap();
		Pose initialPose = new Pose(300,200 , 0);
		Constantes.configure(200,1f/200*.874f, .02f, .11f, 1103);
		MCLPoseProvider mcl = new MCLPoseProvider(
				null, 100, initialPose, new MarcaSModel(), new GaussianMModel());
		mcl.setDebug(false);
		mcl.setPose(initialPose, 0);
		ParticleFrame frame = new ParticleFrame(map, mcl.getParticles(), null);

		JFileChooser chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("."));
		//		if (!(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)) { 
		//			System.out.println("Debe elegir un directorio con corridas");
		//			System.exit(1);
		//		}

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		//		List<Command> cmds = CmdUtils.procesarCorrida(chooser.getSelectedFile());
		List<Command> cmds = CmdUtils.procesarCorrida(new File("./corridasEvols/cluster3.log"));
		//		List<Command> cmds = CmdUtils.procesarCorrida(new File("./elfede"));
		for(Command c : cmds){
			System.out.println(c.toString());
			String s = bufferedReader.readLine();
			if(!s.startsWith("n")){
				if(c instanceof TruePosCmd){
					frame.setTruePose(((TruePosCmd)c).getPose());
				} else if(c instanceof UpdateCmdMarcas){
					MarcaMeassure om = ((UpdateCmdMarcas)c).getM();
					frame.setObservMarca(om.getIdMarca(), om.getDist());
				}
				c.execute(mcl);
			}



		}
	}
}
