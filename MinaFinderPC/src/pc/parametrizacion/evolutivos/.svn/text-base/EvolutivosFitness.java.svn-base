package pc.parametrizacion.evolutivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lejos.robotics.navigation.Pose;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

import pc.logger.Logger;
import pc.parametrizacion.Parametrizacion;
import brick.particulas.Command;
import brick.particulas.MCLPoseProvider;
import brick.particulas.TruePosCmd;
import brick.particulas.mmodel.GaussianMModel;
import brick.particulas.smodel.MarcaSModel;
import brick.utils.Constantes;

public class EvolutivosFitness extends FitnessFunction {

	private static final double MAX_FIT = 100000;
	private static final int EJECS_POR_CORRIDA = 5;
	private List<List<Command>> corridas;
	private Map<Parametrizacion, Double> calculated;
	private Pose initialPose;
	private Logger debug;

	public EvolutivosFitness( File folder, Pose initialPose)
	{
		// Guardo los parametros
		this.initialPose = initialPose;

		// Guardo las corridas
		corridas = new LinkedList<List<Command>>();

		calculated = new HashMap<Parametrizacion, Double>();

		for(File f : folder.listFiles()){
			if(f.isFile()){
				corridas.add(procesarCorrida(f));
			}
		}
		
		debug = null;
	}

	private List<Command> procesarCorrida(File f) {
		List<Command> cmds = new ArrayList<Command>();
		try {
			BufferedReader in
			= new BufferedReader(new FileReader(f));
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

	/**
	 * Obtiene la parametrizacion del individuo.
	 * Para cada corrida
	 * 	Calcula EJECS_POR_CORRIDA errores promedio de medicion
	 * 	Toma la media de esos errores y lo guarda en una lista de errores (promedio) medios por corrida
	 * Toma la media de esa lista
	 */
	public double evaluate( IChromosome a_subject )
	{
		Parametrizacion p = Parametrizacion.fromChrom(a_subject);

		// No uso el diccionario cuando quiero debuggear
		if(calculated.containsKey(p) && debug == null){
//			System.out.println("hit");
			return calculated.get(p);
		}
			


		Constantes.configure(Constantes.NUMPART, 1f / Constantes.NUMPART * p.getMinVarW(),
				p.getRuidoDesp(), p.getRuidoAng(), p.getCovSensado());
		MCLPoseProvider mcl = new MCLPoseProvider(
				null, Constantes.NUMPART, initialPose, 
				new MarcaSModel(),
				new GaussianMModel());
		mcl.setDebug(false);

		
//		System.out.println("Computing fitness");
//		long time = System.currentTimeMillis();
		List<Float> errors = new ArrayList<Float>();
		int numCorrida = 0;
		for(List<Command> corrida : corridas){
			List<Float> errorsCorrida = new ArrayList<Float>();
			for(int i = 0; i < EJECS_POR_CORRIDA; i++){
				mcl.setPose(initialPose);
				float errorCorrida = 0;
				int count = 0;
				for(Command c : corrida){

					if(c instanceof TruePosCmd){
						Pose pose = mcl.getPose();

						Pose t = ((TruePosCmd)c).getPose();
						double dist = Math.sqrt(Math.pow(pose.getX() - t.getX(), 2)
								+ Math.pow(pose.getY() - t.getY(), 2));
						errorCorrida += dist;
						count++;
					} else {
						c.execute(mcl);
					}
				}
				// inserto ordenado
				int pos = 0;
				float error = errorCorrida/count;
				while(pos < errorsCorrida.size() && errorsCorrida.get(pos) < error)
					pos++;
				errorsCorrida.add(pos,error);
			}
			
			float errormedia = errorsCorrida.get(EJECS_POR_CORRIDA / 2 + 1);
			// Si puedo, loggeo el error medio para la corrida
			if (debug != null){
				debug.addLog("mediaCorrida "+numCorrida+" "+errormedia);
				numCorrida++;
			}
			
			int pos = 0;
			while(pos < errors.size() && errors.get(pos) < errormedia)
				pos++;
			errors.add(pos,errormedia);
		}
		
//		System.out.println("Time " + (System.currentTimeMillis() - time));

//		double errorprom = errorCorrida/count; 
		int index = corridas.size() / 2 == 0 ? corridas.size() / 2 : corridas.size() / 2 + 1;
		float errorMediaFinal = errors.get(index);
//		System.out.println(errormedia);

		double fitness = errorMediaFinal != 0 ? 1. / errorMediaFinal : MAX_FIT;

		calculated.put(p, fitness);

		return fitness;
	}



	public static void main (String[] args){
//		MapaMarcasLineas map = new IEEEMarcasMap();
//		Pose initialPose = new Pose(300,200 , 0);
//		EvolutivosFitness fit = new EvolutivosFitness(
//				new File("./corridasVision/"), map, initialPose, Constantes.NUMPART);
//		
//		System.out.println(1./fit.evaluate(null));

	}

	public void setDebugin(Logger log) {
		debug = log;
	}

}
