package brick.particulas;

import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.Pose;
import brick.particulas.mapa.Marca;
import brick.particulas.mmodel.MotionModel;
import brick.particulas.smodel.Meassure;
import brick.particulas.smodel.SensorModel;
import brick.utils.Constantes;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * Represents a particle for the particle filtering algorithm. The state of the
 * particle is the pose, which represents a possible pose of the robot.
 * 
 * The weight for a particle is set by taking a set of theoretical range readings using a
 * map of the environment, and comparing these ranges with those taken by the
 * robot. The weight represents the relative probability that the robot has this
 * pose. Weights are from 0 to 1.
 * 
 * @author Lawrie Griffiths
 * 
 */
public class MCLParticle {
	
	private float x, y, h;
	private float weight = 1;

	private static SensorModel sModel;
	private static MotionModel mModel;
	private Marca marcas[];
	
	

	/**
	 * Create a particle with a specific pose
	 * 
	 * @param pose the pose
	 * @param sModel 
	 */
	public MCLParticle(float x, float y, float h) {
		this.x = x;
		this.y = y;
		this.h = h;

		this.marcas = new Marca[Constantes.cantMarcas];

		for (int i=0; i<Constantes.cantMarcas;i++){
			//recordar que queda guardado con el identificador idMarca-1
			marcas[i] = null;
		}
		
//		marcas[1] = new Marca(1300, 500);
//		marcas[2] = new Marca(1300, 500);
//		marcas[3] = new Marca(1300, 500);
//		marcas[4] = new Marca(1300, 500);
//		marcas[5] = new Marca(1300, 500);
//		marcas[0] = new Marca(1300, 500);
	}

	public MCLParticle(MCLParticle p) {
		this.x = p.x;
		this.y = p.y;
		this.h = p.h;
		this.marcas = new Marca[Constantes.cantMarcas];
		this.setMarcas(p.marcas);
	}

	public MCLParticle(byte[] d) {
		weight = Float.intBitsToFloat(byteArrayToInt(d[0],d[1],d[2],d[3]));
		x = Float.intBitsToFloat(byteArrayToInt(d[4],d[5],d[6],d[7]));
		y = Float.intBitsToFloat(byteArrayToInt(d[8],d[9],d[10],d[11]));
		h = Float.intBitsToFloat(byteArrayToInt(d[12],d[13],d[14],d[15]));
		
		int cantMarcas = byteArrayToInt(d[16],d[17],d[18],d[19]);
		int leidas = 20;
		this.marcas = new Marca[Constantes.cantMarcas];
		for (int i=0; i<Constantes.cantMarcas;i++){
			//recordar que queda guardado con el identificador idMarca-1
			marcas[i] = null;
		}
		for(int i = 0; i < cantMarcas; i++){
			int num = byteArrayToInt(d[leidas],d[leidas+1],d[leidas+2],d[leidas+3]);
			leidas += 4;
			byte[] bytesMarca = new byte[2 * 4];
			System.arraycopy(d, leidas, bytesMarca, 0, bytesMarca.length);
			marcas[num] = new Marca(bytesMarca);
			leidas += bytesMarca.length;
		}
	}

	public static final int byteArrayToInt(byte b1, byte b2, byte b3, byte b4) {
		return (b1 << 24)
				+ ((b2 & 0xFF) << 16)
				+ ((b3 & 0xFF) << 8)
				+ (b4 & 0xFF);
	}
	/**
	 * Set the weight for this particle
	 * 
	 * @param weight the weight of this particle
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * Return the weight of this particle
	 * 
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}

	public void calculateWeight(Meassure m) {
		weight = sModel.getLikelihood(m, new Pose(x,y,h), marcas);
		assert(! Float.isNaN(weight));
	}

	public void applyMove(Move move){
		Pose pose = mModel.applyMove(move, new Pose(x,y,h));
		x = pose.getX();
		y = pose.getY();
		h = pose.getHeading();
	}

	public void updateMarca(int id, double dist, double ang){

		float marcax = (float) (x + dist*Math.cos(Math.toRadians(h + ang)));
		float marcay = (float) (y + dist*Math.sin(Math.toRadians(h + ang)));

		Marca m = marcas[id-1];

		if (m==null){
			//la marca no existe, la doy de alta
			marcas[id-1]= new Marca(marcax, marcay);

		}else{
			m.update(marcax, marcay, Constantes.covMesureMatrix);
		}
	}

	public Marca[] getMarcas(){
		return this.marcas;
	}

	/**
	 * Hace una copia limpia de el array de marcas que le pasan por referencia
	 * @param marcas
	 */

	public void setMarcas(Marca[] marcas){
		for (int i = 0;i<marcas.length;i++)
		{
			Marca marca = marcas[i];
			if (marca != null){
				this.marcas[i]= new Marca(marca);
			}else{
				this.marcas[i] = null;
			}
		}
	}

	public byte[] dump() {
		int copiados = 0;
		int cantMarcas = 0;
		int i = 0;
		while(i < marcas.length){
			if(marcas[i] != null)
				cantMarcas++;
			i++;
		}

//		System.out.println("Cant Marcas " + cantMarcas);
		// 4 Floats para la particula, uno para cant marcas y 2 float (x,y) y un int (num) por marca
		byte[] res = new byte[4*4 + 4 + cantMarcas * 4 * 3];
		int w = Float.floatToIntBits(getWeight());
//		int w = 0;
		int x = Float.floatToIntBits(getX());
		int y = Float.floatToIntBits(getY());
		int h = Float.floatToIntBits(getH());
		byte[] buffer = {
				(byte)(w >>> 24),(byte)(w >>> 16),(byte)(w >>> 8),(byte)w,
				(byte)(x >>> 24),(byte)(x >>> 16),(byte)(x >>> 8),(byte)x,
				(byte)(y >>> 24),(byte)(y >>> 16),(byte)(y >>> 8),(byte)y,
				(byte)(h >>> 24),(byte)(h >>> 16),(byte)(h >>> 8),(byte)h};
		System.arraycopy(buffer, 0, res, 0, buffer.length);
		copiados += buffer.length;
		
//		for(byte b : buffer)
//			System.out.print(b + " ");
				
		
		byte[] cantMarcasBytes = {(byte)(cantMarcas >>> 24),(byte)(cantMarcas >>> 16),
				(byte)(cantMarcas >>> 8),(byte)cantMarcas};
		System.arraycopy(cantMarcasBytes, 0, res, copiados, cantMarcasBytes.length);
		copiados += cantMarcasBytes.length;
		
//		for(byte b : cantMarcasBytes)
//			System.out.print(b + " ");
				
//		System.out.println();
		
		for(i = 0; i < marcas.length; i++){
			if(marcas[i] != null){
				byte[] mBytes = marcas[i].dump(i);
				System.arraycopy(mBytes, 0, res, copiados, mBytes.length);
				copiados += mBytes.length;
			}
		}
		return res;
	}
	
	public static SensorModel getsModel() {
		return sModel;
	}

	public static void setsModel(SensorModel sModel) {
		MCLParticle.sModel = sModel;
	}

	public static MotionModel getmModel() {
		return mModel;
	}

	public static void setmModel(MotionModel mModel) {
		MCLParticle.mModel = mModel;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getH() {
		return h;
	}

	public boolean hasMarca(int id) {
		return marcas[id] != null;
	}

}
