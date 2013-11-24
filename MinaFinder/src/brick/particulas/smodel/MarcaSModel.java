package brick.particulas.smodel;

import lejos.geom.Point;
import lejos.robotics.navigation.Pose;
import lejos.util.Matrix;
import brick.particulas.mapa.MapaMarcasLineas;
import brick.particulas.mapa.Marca;
import brick.utils.Constantes;
import brick.utils.Matematicas;
import brick.utils.MatrixFloat;

public class MarcaSModel implements SensorModel{

	private static final float MINDIST = 300;
	private int lastId;
	private int idRuido;
	
	public MarcaSModel(){
		lastId = -1;
		idRuido = -1;
	}

	@Override
	public float getLikelihood(Meassure m, Pose pose, Object map) {
		MarcaMeassure mm = (MarcaMeassure)m;
		Marca[] mapMarcas = (Marca[])map;
		
		// Marca
//		Marca marca = mapMarcas.getMarca(mm.getIdMarca());
//		float distToMarca = pose.distanceTo(marca.getMedia());
//		float diffDist = distToMarca - mm.getDist();
//		
//		
//		float prob = Matematicas.g(0, varDist, diffDist);

		// Datos de trabajo
		Marca marca = mapMarcas[mm.getIdMarca()-1];
		float dist = mm.getDist();
		float ang = mm.getAngle();
		
		// Punto sensado
		float dir = Matematicas.toRad((ang + pose.getHeading() + 360) % 360);
		Point pMeasured = new Point((float)(pose.getX() + dist * Math.cos(dir)),
				(float) (pose.getY() + dist * Math.sin(dir))); 
		
		// Convolucion entre modelo sensado y gaussiana de marca
		float prob = (float) Matematicas.gMulti(new Point(marca.getX(), marca.getY()),
				Constantes.covMesureMatrix.plus(marca.getCov()), pMeasured);
		
		if(Float.isNaN(prob))
			return 0;
		else
			return prob;
	}

	@Override
	public boolean acceptable(Meassure m) {
		if (!(m instanceof MarcaMeassure))
			return false;
		
		MarcaMeassure mm = (MarcaMeassure) m;
		int id = mm.getIdMarca();
		boolean res = mm.getDist() > MINDIST /*&&
						( id == lastId || id == idRuido)*/;
		
//		// Si id == idRuido, la nueva id es idRuido
//		if(id != lastId && id == idRuido)
//			lastId = id;
//		// Si no, hay una nueva idRuido y no hay id
//		else if (id != lastId && id != idRuido){
//			lastId = -1;
//			idRuido = id;
//		// Si id == lastId no hay idRuido
//		} else
//			idRuido = -1;
		
		return res;
	}

}
