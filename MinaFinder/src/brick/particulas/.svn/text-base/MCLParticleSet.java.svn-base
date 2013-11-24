package brick.particulas;


import lejos.geom.Point;
import lejos.geom.Rectangle;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.Pose;
import brick.particulas.smodel.MarcaMeassure;
import brick.particulas.smodel.Meassure;
import brick.utils.Constantes;
import brick.utils.Pgrand;

/**
 * Represents a particle set for the particle filtering algorithm.
 *
 * @author Lawrie Griffiths
 *
 */
public class MCLParticleSet {
	private static final int GC_PERIOD = 20;
	// Static variables
	public static int maxIterations = 1000;
	// Instance variables
	private int numParticles;
	private MCLParticle[] particles;
	private MCLParticle[] moveParts;
	private float maxWeight;
	private int border = 10;	// The minimum distance from the edge of the map
	private Pgrand random = new Pgrand(); // to generate a particle.
	private Rectangle boundingRect;
	private static boolean debug = false;
	private int _iterations;

	private static int[] cantPart;

	public MCLParticleSet()
	{
		this(Constantes.NUMPART, new Pose(0,0,0), 20, 20);
	}

	/**
	 * Generates a circular cloud of particles centered on initialPose with random
	 * normal radius  and angle, and random normal heading.
	 * @param map the map
	 * @param numParticles the number of particles
	 * @param initialPose  the center of the cloud
	 * @param radiusNoise standard deviation of the normal of the distance from center
	 * @param headingNoise standard deviation of heading
	 * @param sModel 
	 * @param mModel 
	 */
	public MCLParticleSet(int numParticles, Pose initialPose,
			float radiusNoise, float headingNoise)
	{
		this.numParticles = numParticles;
		border = 0;
		moveParts = new MCLParticle[numParticles];
		for (int i = 0; i < numParticles; i++)
			moveParts[i] = null;
		particles = new MCLParticle[numParticles];
		for (int i = 0; i < numParticles; i++)
		{
			float rad = radiusNoise * (float) random.getRand().nextGaussian();
			float theta = (float) (2 * Math.PI * random.getRand().nextDouble());
			float x = initialPose.getX() + rad * (float) Math.cos(theta);
			float y = initialPose.getY() + rad * (float) Math.sin(theta);
			//			float x = initialPose.getX() + rad;
			//			float y = initialPose.getY() + rad;
			float heading = initialPose.getHeading() + headingNoise * (float) random.getRand().nextGaussian();
			particles[i] = new MCLParticle(x,y,heading);
			if(debug){
				System.out.println(" new particle set ");
			}
		}

		cantPart = new int[numParticles];

	}


	/**
	 * Return the number of particles in the set
	 *
	 * @return the number of particles
	 */
	public int numParticles() {
		return numParticles;
	}

	/**
	 * Set system out debugging on or off
	 *
	 * @param debug true to set debug, false to set it off
	 */
	public    void setDebug(boolean debug) {
		this.debug = debug;
		if(debug)System.out.println("ParticleSet Debug ON ");
	}

	/**
	 * Get a specific particle
	 *
	 * @param i the index of the particle
	 * @return the particle
	 */
	public MCLParticle getParticle(int i) {
		return particles[i];
	}


	/**
	 * Resample the set picking those with higher weights.
	 *
	 * Note that the new set has multiple instances of the particles with higher
	 * weights.
	 *
	 * @return true iff lost
	 */
	/**
	 * Resample the set picking those with higher weights.
	 *
	 * Note that the new set has multiple instances of the particles with higher
	 * weights.
	 *
	 * @return true iff lost
	 */
	public boolean resample() {
		//		normalizeWeights(particles);


		// Rename particles as oldParticles and create a new set
		if(debug)System.out.println("entro al resample");
		// Encuentro el total
		float tWeight = 0;

		for(MCLParticle p : particles){
			tWeight += p.getWeight();
		}

		if(debug)System.out.println("por sincronizar");
		synchronized(this){	
			if(tWeight == 0){
				for(int i = 0; i < numParticles; i++)
					particles[i].setWeight(1);
				// No resample
				return false;
			}

			if(debug)System.out.println("tweight calculado");
			// normalizo y encuentro maximo
			float maxW = 0;
			for(MCLParticle p : particles){
				p.setWeight(p.getWeight()/tWeight);
				if(p.getWeight() > maxW)
					maxW = p.getWeight();
			}

			// Calculo varianza de los pesos
			// Como normalize el promedio es 1/N
			float promW = 1f/numParticles;
			float varW = 0;
			for(MCLParticle p : particles){
				varW += Math.pow(p.getWeight() - promW, 2);
			}
			varW /= numParticles;
			varW = (float) Math.sqrt(varW);
			if(debug) System.out.println("Varianza Pesos " + varW);

			if(varW > Constantes.MIN_VAR_PESOS){

//				if(Constantes.online)System.gc();
				if(MCLPoseProvider.debugMemoria )System.out.println("[M] synRes " +( Runtime.getRuntime().freeMemory()));

				//			MCLParticle[] oldParticles = particles;

				// Array para guardar cantidad de copias a realizar
				for(int i = 0; i < numParticles; i++)
					cantPart[i] = 0;

				// Ruleta
				float beta = 0;
				int index = random.getRand().nextInt(numParticles);
				for(int i = 0; i < numParticles; i++){
					beta += random.getRand().nextFloat() * 2 * maxW;
					while (beta > particles[index].getWeight()){
						beta -= particles[index].getWeight();
						index = (index + 1) % numParticles;
					}
					cantPart[index] = cantPart[index] + 1;
				}

				// Paso solo las que se van a copiar
				//			particles = new MCLParticle[numParticles];
				index = 0;
				for(int i = 0; i < numParticles; i++){
					if(cantPart[i] > 0){
						// Reutilizo la ultima
						moveParts[index] = particles[i];
						index += cantPart[i];
					}
					particles[i] = null;
				}

				MCLParticle[] tmp = particles;
				particles = moveParts;
				moveParts = tmp;

				// Hago las copias
				MCLParticle anterior = null;
				for(int i = 0; i < numParticles; i++){
					if(particles[i] == null)
						particles[i] = new MCLParticle(anterior);
					else
						anterior = particles[i];
				}


				return true;
			}
		}

		return false;
	}
	
	/**
	 * Apply a move to each particle
	 *
	 * @param move the move to apply
	 */
	public void applyMove(Move move) {
		if(debug)System.out.println("particles applyMove "+move.getMoveType());
		maxWeight = 0f;
		synchronized (this)	 {
			for (int i = 0; i < numParticles; i++) {
				particles[i].applyMove(move);
			}
		}
		
		if(debug)System.out.println("particles applyMove Exit");
	}

	/**
	 * The highest weight of any particle
	 *
	 * @return the highest weight
	 */
	public float getMaxWeight() {
		float wt = 0;
		for (int i = 0; i < particles.length; i ++ ) wt = Math.max(wt,particles[i].getWeight());
		return wt;
	}

	/**
	 * Set the maximum iterations for the resample algorithm
	 * @param max the maximum iterations
	 */
	public void setMaxIterations(int max) {
		maxIterations = max;
	}

	public byte[] dumpParticles(byte[] res){
		synchronized(this){
			//			byte [] res = new byte[4 + 4 * 4 * numParticles];
			System.arraycopy(intToByteArray(numParticles),
					0, res, 0, 4);
			// buffer donde guardo las particulas

			int copiadas = 4;
			for (int i = 0; i < numParticles(); i++) {
				byte[] pDump = getParticle(i).dump();
				System.arraycopy(pDump,
						0, res, copiadas, pDump.length);
				copiadas += pDump.length;
			}
			
			return res;
		}

	}

	public void loadParticles(byte[] d) {
		synchronized (this) {
			numParticles = byteArrayToInt(d[0], d[1], d[2], d[3]);
			particles = new MCLParticle[numParticles];
			int leidas = 4;
			for (int i = 0; i < numParticles; i++) {
				int cantMarcas = byteArrayToInt(d[leidas+16], d[leidas+17], d[leidas+18], d[leidas+19]);
				byte[] partBytes = new byte[4 * 4 + 4 + 3 * 4 * cantMarcas];
				System.arraycopy(d,	leidas, partBytes, 0, partBytes.length);
				particles[i] = new MCLParticle(partBytes);
				leidas += partBytes.length;
			}
		}
		
	}
	
	public int getIterations() {
		return _iterations;
	}



	public static final int byteArrayToInt(byte b1, byte b2, byte b3, byte b4) {
		return (b1 << 24)
				+ ((b2 & 0xFF) << 16)
				+ ((b3 & 0xFF) << 8)
				+ (b4 & 0xFF);
	}

	public static final byte[] intToByteArray(int value) {
		return new byte[] {
				(byte)(value >>> 24),
				(byte)(value >>> 16),
				(byte)(value >>> 8),
				(byte)value};	
	}	


	public MCLParticle[] getParticles() {
		synchronized (this) {
			return particles;
		}
		
	}

	public boolean calculateWeights(Meassure m) {
		for (int i = 0; i < numParticles; i++)
			particles[i].calculateWeight(m);

		return true;
	}

	public void updateKalman(Meassure m) {
		MarcaMeassure mm = (MarcaMeassure)m;
		int id = mm.getIdMarca();
		float dist = mm.getDist();
		float angle = mm.getAngle();
		for (int i = 0; i < numParticles; i++)
		{
//			if(Constantes.online) System.gc();
			particles[i].updateMarca(id,dist,angle);
		}
	}

	public boolean hasMarca(int id) {
		boolean res = false;
		for (int i = 0; i < numParticles && !res; i++)
		{
			res = particles[i].hasMarca(id);
		}
		return res;
	}

	public Point getMediaMarca(int id) {
		float x = 0;
		float y = 0;
		for (int i = 0; i < numParticles; i++)
		{
			x += particles[i].getMarcas()[id].getX() / particles.length;
			y += particles[i].getMarcas()[id].getY() / particles.length;
		}
		
		return new Point(x,y);
	}


}
