package brick.tests;

import curtidor.behaviors.VisionBh;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import brick.utils.Robot;

public class TVision {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Robot r = new Robot();
		Behavior vision = new VisionBh(r, null);
		
		Behavior[] comps = {vision};

		Arbitrator arb = new Arbitrator(comps);
		arb.start();
	}

}
