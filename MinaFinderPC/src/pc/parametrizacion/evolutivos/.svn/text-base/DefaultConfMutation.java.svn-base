package pc.parametrizacion.evolutivos;

import org.jgap.Configuration;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.InvalidConfigurationException;
import org.jgap.event.EventManager;
import org.jgap.impl.BestChromosomesSelector;
import org.jgap.impl.ChromosomePool;
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.GABreeder;
import org.jgap.impl.MutationOperator;
import org.jgap.impl.StockRandomGenerator;
import org.jgap.impl.WeightedRouletteSelector;

public class DefaultConfMutation extends Configuration {

	public DefaultConfMutation(String id, String name, double crossRate, int mutRate) {
	    super(id, name);
	    try {
	      setBreeder(new GABreeder());
	      setRandomGenerator(new StockRandomGenerator());
	      setEventManager(new EventManager());
	      // Elitism
	      BestChromosomesSelector bestChromsSelector = new BestChromosomesSelector(
	          this, 0.10d);
	      bestChromsSelector.setDoubletteChromosomesAllowed(true);
	      addNaturalSelector(bestChromsSelector, false);
	      // Tournament
	      WeightedRouletteSelector sel = new WeightedRouletteSelector(this);
	      addNaturalSelector(sel, false);
	      setMinimumPopSizePercent(0);
	      //
	      setSelectFromPrevGen(1.0d);
	      setKeepPopulationSizeConstant(true);
	      setFitnessEvaluator(new DefaultFitnessEvaluator());
	      setChromosomePool(new ChromosomePool());
	      addGeneticOperator(new CrossoverOperator(this, crossRate));
	      addGeneticOperator(new MutationOperator(this, mutRate));
	    }
	    catch (InvalidConfigurationException e) {
	      throw new RuntimeException(
	          "Fatal error: DefaultConfiguration class could not use its "
	          + "own stock configuration values. This should never happen. "
	          + "Please report this as a bug to the JGAP team.");
	    }
	  }
}
