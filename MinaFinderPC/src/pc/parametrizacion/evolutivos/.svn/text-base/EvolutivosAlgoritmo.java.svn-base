package pc.parametrizacion.evolutivos;


import java.io.File;

import lejos.robotics.navigation.Pose;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.IntegerGene;

import pc.logger.Logger;

public class EvolutivosAlgoritmo {

	private Logger log;
	private File file;
	private int mutRate;
	private double crossRate;
	private int popSize;

	public EvolutivosAlgoritmo(File selectedFile, double crossRate,int mutRate, int popSize, Logger log) {
		this.file = selectedFile;
//		log = new Logger("evolutivos" + idProc + ".txt");
		this.log = log;
		this.mutRate = mutRate;
		this.crossRate = crossRate;
		this.popSize = popSize;
	}

	public EvolutivosAlgoritmo(File selectedFile, String id) {
		this(selectedFile, .95, 5, 50, new Logger("evolutivos" + id + ".txt"));
	}

	public double test() throws InvalidConfigurationException {
		Configuration.reset();
		Configuration gaConf = new DefaultConfMutation("", "", crossRate, mutRate);
		Gene[] sampleGenes = new Gene[ 4 ];

		sampleGenes[0] = new IntegerGene(gaConf, 0, 100 );  // distNoise
		sampleGenes[1] = new IntegerGene(gaConf, 0, 100 );  // distAngle
		sampleGenes[2] = new IntegerGene(gaConf, 0, 10000 );  // updDist
		sampleGenes[3] = new IntegerGene(gaConf, 0, 1000 );  // minVarW

		Chromosome sampleChromosome = new Chromosome(gaConf, sampleGenes );
		//			IChromosome sampleChromosome = new Chromosome(gaConf,
		//					new IntegerGene(gaConf, 0, 400 ), 4);

		gaConf.setSampleChromosome(sampleChromosome);
		gaConf.setPopulationSize(popSize);
		EvolutivosFitnessMapa fitness = new EvolutivosFitnessMapa( file, new Pose(300,200 , 0));
		gaConf.setFitnessFunction(fitness);

		Genotype population = Genotype.randomInitialGenotype( gaConf );
//		
//		TimedMonitor mon = new TimedMonitor(60);
//		population.evolve(mon);
//		log(population.getFittestChromosome());
		
//		
		FitnessMon mon = new FitnessMon(10, .10);
		population.evolve(mon);
//		for (int i = 0; i < maxEvols; i++){
//			population.evolve();
//			log(population.getFittestChromosome());
//		}

		log(population.getFittestChromosome());
		
		// Corro el fitness con debuggin para registrar el error medio de cada corrida
		System.out.println("Evaluando fitness loggeado");
		fitness.setDebugin(log);
		fitness.evaluate(population.getFittestChromosome());
		
		return 1./population.getFittestChromosome().getFitnessValue();
	}

	private void log(IChromosome fc) {
		log.addLog(fc.toString());
	}

	public static void main(String[] args) throws Exception {


		// Start with a DefaultConfiguration, which comes setup with the
		// most common settings.
		// -------------------------------------------------------------

		// Set the fitness function we want to use, which is our
		// MinimizingMakeChangeFitnessFunction that we created earlier.
		// We construct it with the target amount of change provided
		// by the user.
		// ------------------------------------------------------------
//		JFileChooser chooser = new JFileChooser(); 
//		chooser.setCurrentDirectory(new java.io.File("."));
//		chooser.setDialogTitle("Directorio con corridas");
//		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//		//
//		// disable the "All files" option.
//		//
//		chooser.setAcceptAllFileFilterUsed(false);
//		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) { 
//			System.out.println("Debe elegir un directorio con corridas");
//			System.exit(0);
//		}

		String carpeta = "./corridasEvol/";
		EvolutivosAlgoritmo alg = new EvolutivosAlgoritmo(
				/*chooser.getSelectedFile()*/new File(carpeta), args[0]);
		try {
			System.out.print("Error obtenido " + alg.test());
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
