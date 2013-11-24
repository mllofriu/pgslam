package brick;

import lejos.nxt.Button;
import lejos.robotics.subsumption.Behavior;

public class BotonBh implements Behavior {

	@Override
	public boolean takeControl() {
		return Button.ENTER.isPressed();
	}

	@Override
	public void action() {

	}

	@Override
	public void suppress() {

	}

}
