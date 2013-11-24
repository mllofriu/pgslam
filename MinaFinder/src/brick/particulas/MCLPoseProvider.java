package brick.particulas;

import java.awt.Rectangle;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.geom.Point;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import brick.particulas.mmodel.MotionModel;
import brick.particulas.smodel.Meassure;
import brick.particulas.smodel.SensorModel;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * Maintains an estimate of the robot pose using sensor data.  It uses Monte Carlo
 * Localization  (See section 8.3 of "Probabilistic Robotics" by Thrun et al. <br>
 * Uses a {@link MCLParticleSet} to represent the probability distribution  of the
 * estimated pose.
 * It uses a {@link lejos.robotics.navigation.MoveProvider} to supply odometry
 * data whenever  a movement is completed,
 * from which the {@link lejos.robotics.navigation.Pose} of each particle is updated.
 * It then uses a {@link  lejos.robotics.RangeScanner} to provide
 * {@link lejos.robotics.RangeReadings}  which are used, together with the
 * {@link lejos.robotics.mapping.RangeMap} to calculate the
 * probability weight of  each {@link MCLParticle} .
 * @author Lawrie Griffiths and Roger Glassey
 */

public class MCLPoseProvider  implements PoseProvider
{

	private static final float headingNoise = 0;//10;
	private static final float radiusNoise = 0;//50;
	private MCLParticleSet particles;
	private final float BIG_FLOAT = 1000000f;
	private int numParticles;
	private float _x, _y, _heading;
	private float minX, maxX, minY, maxY;
	private double varX, varY, varH;
	private boolean debug = false;
	static boolean debugMemoria = false;

	public MCLPoseProvider(MoveProvider mp, int numParticles,
			Pose initialPose, SensorModel sModel, MotionModel mModel){
		this.numParticles = numParticles;
		//		particles = new MCLLineParticleSet(map, numParticles, border);
		MCLParticle.setmModel(mModel);
		MCLParticle.setsModel(sModel);
		particles = new MCLParticleSet(numParticles, initialPose,
				radiusNoise, headingNoise);
		//		if (mp != null) mp.addMoveListener(this);
		
		if(debugMemoria )System.out.println("[M] init " +( Runtime.getRuntime().freeMemory()));
	}


	public MCLPoseProvider(MCLParticleSet particles) {
		this.particles = particles;
		this.numParticles = particles.numParticles();
	}


	/**
	 * Generates an  initial particle set in a circular normal distribution, centered
	 * on aPose.
	 * @param aPose - center of the cloud
	 * @param radiusNoise - standard deviation of the radius of the cloud
	 * @param headingNoise - standard deviation of the heading;
	 */
	public void setInitialPose(Pose aPose, float radiusNoise, float headingNoise)
	{
		_x = aPose.getX();
		_y = aPose.getY();
		_heading = aPose.getHeading();
		particles = new MCLParticleSet(numParticles,
				aPose, radiusNoise, headingNoise);
	}

	/**
	 * Set debugging on or off
	 * @param on true = on, false = off
	 */
	public void setDebug(boolean on) {
		debug = on;
	}

	/**
	 * set the initial pose cloud with radius noise 1 and heading noise 1
	 */
	public void setPose(Pose aPose)
	{
		setInitialPose(aPose, radiusNoise, headingNoise);
	}
	
	public void setPose(Pose aPose, float noise)
	{
		setInitialPose(aPose, noise, noise);
	}


	/**
	 * Returns the particle set
	 * @return the particle set
	 */
	public MCLParticleSet getParticles()
	{
		return particles;
	}

	/**
  		Required by MoveListener interface; does nothing
	 */
	public void moveStarted(Move event, MoveProvider mp) { 
		//		this.setChanged();
		//		this.notifyObservers();
	}


	/**
	 * Required by MoveListener interface. The pose of each particle is updated
	 * using the odometry data of the Move object.
	 * @param event the move  just completed
	 * @param mp the MoveProvider
	 */
	public void moveStopped(Move event, MoveProvider mp)
	{
		// Dibujar angulo para debbuging
		//		LCD.drawString("rcv " + event.getAngleTurned(), 0, 1);
		//		updater.moveStopped(event);	
		if(debug) System.out.println("Updater move stop "+event.getMoveType());

		particles.applyMove(event);   


		//          System.out.println("apply move ");

	}

	public void applyMove(Move m){
		if(debug) System.out.println("Updater move stop "+m.getMoveType());

		particles.applyMove(m);   

		//          System.out.println("apply move ");
		if(debugMemoria )System.out.println("[M] applym " +( Runtime.getRuntime().freeMemory()));
	}


	public boolean  update(Meassure m)
	{
		

		if (MCLParticle.getsModel().acceptable(m)){
			// if(updated) return true;
			if(debug)System.out.println("MCLPP update called ");
			return updateParts(m);
		}
			

		return false;
	}

	private boolean updateParts(Meassure m) {
		if(debug)System.out.println("MCLPP update readings called ");
		
//		if(Constantes.online)System.gc();
		particles.updateKalman(m);
		if(debug)System.out.println("Kalman updated");
		if(debugMemoria )System.out.println("[M] kal " +( Runtime.getRuntime().freeMemory()));
//		if(Constantes.online)System.gc();
		particles.calculateWeights(m);
		if(debug)System.out.println("weights calculated");

		if(debugMemoria )System.out.println("[M] calcw " +( Runtime.getRuntime().freeMemory()));
		
		if(debugMemoria )System.out.println("[M] pRes " +( Runtime.getRuntime().freeMemory()));
//		if(Constantes.online)System.gc();	
		boolean resampled = particles.resample();
		if (debug){
			if(resampled) System.out.println("Resampled");
			else System.out.println("Not resampled");
		}
		
		if(debugMemoria )System.out.println("[M] resa " +( Runtime.getRuntime().freeMemory()));
		
//		lastUpdate = System.currentTimeMillis();
		return true;
	}

	/**
	 * Returns the difference between max X and min X
	 * @return the difference between min and max X
	 */
	public float getXRange()
	{
		return getMaxX() - getMinX();
	}

	/**
	 * Return difference between max Y and min Y
	 * @return difference between max and min Y
	 */
	public float getYRange()
	{
		return getMaxY() - getMinY();
	}



	/**
	 * Returns the best best estimate of the current pose;
	 * @return the estimated pose
	 */
	public Pose getPose()
	{
		estimatePose();
		return new Pose(_x, _y, _heading);

	}

	/**
	 * Estimate pose from weighted average of the particles
	 * Calculate statistics
	 */
	private void estimatePose()
	{
		//    if (scanner == null)
		//    {
		//      return;
		//    }
		float totalWeights = 0;
		float estimatedX = 0;
		float estimatedY = 0;
		float estimatedAngleX = 0;
		float estimatedAngleY = 0;
 		varX = 0;
		varY = 0;
		varH = 0;
		minX = BIG_FLOAT;
		minY = BIG_FLOAT;
		maxX = -BIG_FLOAT;
		maxY = -BIG_FLOAT;

		synchronized (particles) {
			for (int i = 0; i < numParticles; i++)
			{
				MCLParticle p = particles.getParticle(i);
				float x = p.getX();
				float y = p.getY();
				float weight = particles.getParticle(i).getWeight();
				assert(! Float.isNaN(x));
				assert(! Float.isNaN(y));
				assert(! Float.isNaN(weight));
				estimatedX += (x * weight);
				varX += (x * x * weight);
				estimatedY += (y * weight);
				varY += (y * y * weight);
				float head = p.getH();
				estimatedAngleX += (Math.cos(Math.toRadians(head)) * weight);
				estimatedAngleY += (Math.sin(Math.toRadians(head)) * weight);
				varH += (head * head * weight);
				totalWeights += weight;

				if (x < minX)  minX = x;

				if (x > maxX)maxX = x;
				if (y < minY)minY = y;
				if (y > maxY)   maxY = y;
			}
		}
		

		estimatedX /= totalWeights;
		varX /= totalWeights;
		varX -= (estimatedX * estimatedX);
		estimatedY /= totalWeights;
		varY /= totalWeights;
		varY -= (estimatedY * estimatedY);
		estimatedAngleX /= totalWeights;
		estimatedAngleY /= totalWeights;
		float estimatedAngle = (float) Math.toDegrees(Math.atan2(estimatedAngleY,  estimatedAngleX));
		varH /= totalWeights;
		varH -= (estimatedAngle * estimatedAngle);
//		while (estimatedAngle > 180)
//		{
////			estimatedAngle -= 360;
//			estimatedAngle = 180;
//		}
//		while (estimatedAngle < -180)
//		{
////			estimatedAngle += 360;
//			estimatedAngle = -180;
//		}
		_x = estimatedX;
		_y = estimatedY;
		_heading = estimatedAngle;
	}

	/**
	 * Returns the minimum rectangle enclosing all the particles
	 * @return rectangle : the minimum rectangle enclosing all the particles
	 */
	public Rectangle getErrorRect()
	{
		return new Rectangle((int) minX, (int) minY,
				(int) (maxX - minX), (int) (maxY - minY));
	}

	/**
	 * Returns the maximum value of  X in the particle set
	 * @return   max X
	 */
	public float getMaxX()
	{
		return maxX;
	}

	/**
	 * Returns the minimum value of   X in the particle set;
	 * @return minimum X
	 */
	public float getMinX()
	{
		return minX;
	}

	/**
	 * Returns the maximum value of Y in the particle set;
	 * @return max y
	 */
	public float getMaxY()
	{
		return maxY;
	}

	/**
	 * Returns the minimum value of Y in the particle set;
	 * @return minimum Y
	 */
	public float getMinY()
	{
		return minY;
	}

	/**
	 * Returns the standard deviation of the X values in the particle set;
	 * @return sigma X
	 */
	public float getSigmaX()
	{
		return (float) Math.sqrt(varX);
	}

	/**
	 * Returns the standard deviation of the Y values in the particle set;
	 * @return sigma Y
	 */
	public float getSigmaY()
	{
		return (float) Math.sqrt(varY);
	}

	/**
	 * Returns the standard deviation of the heading values in the particle set;
	 * @return sigma heading
	 */
	public float getSigmaHeading()
	{
		return (float) Math.sqrt(varH);
	}

	/**
	 * Dump the serialized estimate of pose to a data output stream
	 * @param dos the data output stream
	 * @throws IOException
	 */
	public void dumpEstimation(DataOutputStream dos) throws IOException
	{
		dos.writeFloat(_x);
		dos.writeFloat(_y);
		dos.writeFloat(_heading);
		dos.writeFloat(minX);
		dos.writeFloat(maxX);
		dos.writeFloat(minY);
		dos.writeFloat(maxY);
		dos.writeFloat((float) varX);
		dos.writeFloat((float) varY);
		dos.writeFloat((float) varH);
		dos.flush();
	}


	public boolean hasMarca(int i) {
		return particles.hasMarca(i);
	}


	public Point getMediaMarca(int id) {
		return particles.getMediaMarca(id);
	}

}
