package brick.particulas.smodel;

import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;
import brick.utils.Matematicas;
import brick.utils.Robot;

public class LineOrientSModel implements SensorModel{

	private float varDist;
	private float varOrient;
	
	public LineOrientSModel(){
		varDist = 87;
		varOrient = 31;
	}
	
	public LineOrientSModel(float varDist, float varOrient){
		this.varDist = varDist;
		this.varOrient = varOrient;
	}

	@Override
	public float getLikelihood(Meassure m, Pose pose, Object map) {
		LineMap lineMap = (LineMap)map;
		LineOrientMeassure lom = (LineOrientMeassure)m;
		// Si no toco, retorno
		if(!lom.isTocoizq() && !lom.isTocoder())
			return 0;

		Pose tempPose = trasladarASensor(pose, lom.isTocoizq(), lom.isTocoder());
		// Busco hacia adelante
		float dist = lineMap.range(tempPose);
		// Busco hacia atras
		tempPose.setHeading(tempPose.getHeading() + 180 % 360);
		float distAtras = lineMap.range(tempPose);	
		// Si alguno es -1 me quedo con el otro
		float distFinal;
		if(dist == -1)
			dist = 100000;
		else if(distAtras == -1)
			distAtras = 100000;

		// Si ninguno es -1 me quedo con el menor			
		distFinal = Math.min(dist, distAtras);

		//				System.out.println(dist + " " + distAtras + " " + distFinal);
		//		for(int i = 90; i <= 270; i += 90){
		//			tempPose.setHeading(i);
		//			float tempDist = map.range(tempPose);
		//			if(dist == -1 || tempDist < dist && tempDist != -1)
		//				dist = tempDist;
		//		}
		// Orientacion
		float diff1 = Math.abs(pose.getHeading() - lom.getOrientation());
		float diff2 = 360 - pose.getHeading() + lom.getOrientation();
		float diff3 = pose.getHeading() + (360 - lom.getOrientation());
		float diffOrient = Math.min(Math.min(diff1, diff2), diff3);
		//				float puntosDiff = (diffOrient * 3 + Math.abs(dist) * 20)/20;
		float puntosDiff = Matematicas.g(0, varDist, distFinal) * Matematicas.g(0, varOrient, diffOrient);

		// Calculo el peso como un inverso de una potencia de la distancia y la diferencia 
		// de orientacion
		//				weight = (float) (dist == -1 ? 0.000 : 1/Math.pow(puntosDiff, 1.3));
		return (distFinal == -1f ? 0.000f : puntosDiff);
	}
	
	private Pose trasladarASensor(Pose tempPose, boolean tocoizq,
			boolean tocoder) {
		float heading = tempPose.getHeading();
		// Sumo para quedar entre los sensores de luz
		double nuevaX = tempPose.getX() + Math.cos(toRad(heading)) * Robot.DIST_SENSORES_MEDIO;
		double nuevaY = tempPose.getY() + Math.sin(toRad(heading)) * Robot.DIST_SENSORES_MEDIO;
		// Me muevo hacia el sensor correspondiente
		float headingPerp;
		// Dependiendo que sensor toco, cambio la pose para un lado o para el otro
		if (tocoizq){
			headingPerp = (heading + 90) % 360;
		} else {
			headingPerp = (heading - 90 + 360) % 360;
		}
		nuevaX = nuevaX + Math.cos(toRad(headingPerp)) * Robot.DIST_ENTRE_SENSORES / 2;
		nuevaY = nuevaY + Math.sin(toRad(headingPerp)) * Robot.DIST_ENTRE_SENSORES / 2;
		return new Pose((float)nuevaX, (float)nuevaY, heading);
	}
	
	private double toRad(float heading) {
		return Math.PI / 180 * heading;
	}

	@Override
	public String toString() {
		return varDist + "," + varOrient;
	}

	@Override
	public boolean acceptable(Meassure m) {
		return true;
	}
	
	

}
