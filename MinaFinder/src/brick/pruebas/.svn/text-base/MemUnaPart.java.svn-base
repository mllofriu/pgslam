package brick.pruebas;

import lejos.nxt.Button;
import lejos.robotics.navigation.Pose;
import brick.particulas.MCLParticle;
import brick.particulas.mmodel.GaussianMModel;
import brick.particulas.mmodel.MotionModel;
import brick.particulas.smodel.MarcaSModel;
import brick.particulas.smodel.SensorModel;
import brick.utils.Constantes;

public class MemUnaPart {

	private static final int CANTPARTS = 200;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SensorModel sm = new MarcaSModel();
		MotionModel mm = new GaussianMModel();
		MCLParticle.setmModel(mm);
		MCLParticle.setsModel(sm);
		System.out.println(Runtime.getRuntime().freeMemory());

		MCLParticle[] parts = new MCLParticle[CANTPARTS];
		System.out.println(Runtime.getRuntime().freeMemory());
		for(int i = 0; i < CANTPARTS; i++)
			parts[i] = new MCLParticle(0,0,0);

		System.out.println(Runtime.getRuntime().freeMemory());
		
		Button.ENTER.waitForPress();
		
	}

}
