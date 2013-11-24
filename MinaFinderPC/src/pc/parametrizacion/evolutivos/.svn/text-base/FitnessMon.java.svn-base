package pc.parametrizacion.evolutivos;

import java.util.List;

import org.jgap.Configuration;
import org.jgap.Population;
import org.jgap.audit.IEvolutionMonitor;
import org.jgap.eval.PopulationHistoryIndexed;

public class FitnessMon implements IEvolutionMonitor {

	private double lastfit;
	private int check;
	private int checkPeriod;
	private double fitnessIncrement;
	
	

	public FitnessMon(int checkPeriod, double fitnessIncrement) {
		this.fitnessIncrement = fitnessIncrement;
		this.checkPeriod = checkPeriod;
	}

	@Override
	public void event(String arg0, int arg1, Object[] arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public PopulationHistoryIndexed getPopulations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean nextCycle(Population pop, List<String> arg1) {
		System.out.print(".");
		boolean res;
		double fit =  pop.determineFittestChromosome().getFitnessValue();
		
		check = check + 1;
		
		if(check == checkPeriod){
			res = lastfit == 0 || (fit - lastfit) / lastfit > fitnessIncrement;
			lastfit = fit;
			check = 0;
		} else
			res = true;
		
//		System.out.println("Fittest " + pop.determineFittestChromosome());
		
		return res;
	}

	@Override
	public void start(Configuration arg0) {
		check = 0;
		lastfit = 0d;
	}

}

	