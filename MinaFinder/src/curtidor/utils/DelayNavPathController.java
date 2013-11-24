package curtidor.utils;

import java.util.ArrayList;
import java.util.Collection;

import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.ArcMoveController;
import lejos.robotics.navigation.MoveController;
import lejos.robotics.navigation.PathController;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.RotateMoveController;
import lejos.robotics.navigation.WayPoint;
import lejos.robotics.navigation.WayPointListener;
import lejos.robotics.pathfinding.PathFinder;
import lejos.util.Delay;
import brick.utils.Matematicas;

/**
 * This class can cause the robot to follow a route - a sequence of  {@link  lejos.robotics.navigation.WayPoint }
 * ;
 * The way points are stored in a queue (actually, a Collection).
 * This  class uses  an inner class running its own thread to issue movement commands to its
 * {@link lejos.robotics.navigation.MoveController},
 * which can be either a  {@link lejos.robotics.navigation.DifferentialPilot}
 * or {@link lejos.robotics.navigation.SteeringPilot}.
 * It also uses a {@link lejos.robotics.localization.PoseProvider}
 * to keep its pose updated, and calls its {@link lejos.robotics.navigation.WayPointListener}
 * when a way point is reached.
 * 
 * @author Roger Glassey
 */
public class DelayNavPathController implements PathController
{

	/** 
	 * Establece la nueva ruta sin comenzar a recorrerla
	 * @param wayPoints
	 */
	public void setRoute(ArrayList<WayPoint> wayPoints){
		synchronized (_route) {
			_route = wayPoints;
		
		
			if(listeners != null && !_route.isEmpty()) 
			{ 
				for(WayPointListener l : listeners)
					l.nextWaypoint(_route.get(0));
			}
		}
	}

	public ArrayList<WayPoint> getRoute() {
		synchronized (_route) {
			return _route;
		}
	}

	/**
	 * Can use any pilot that implements the ArcMoveController interface. 
	 * @param pilot
	 */
	public DelayNavPathController(SyncDiffPilot pilot )
	{
		this(pilot,null);
	}

	/**
	 * Creates a PathController using a custom poseProvider, rather than the default tachometer pose
	 * provider.
	 * @param pilot
	 * @param poseProvider
	 */
	public DelayNavPathController(SyncDiffPilot  pilot, PoseProvider poseProvider )
	{
		_pilot = pilot;
		if(poseProvider == null)
			this.poseProvider = new OdometryPoseProvider(_pilot);
		else
			this.poseProvider = poseProvider;

		_radius = (_pilot instanceof ArcMoveController ? ((ArcMoveController) _pilot).getMinRadius() : 0);
		_nav = new Nav();
		_nav.setDaemon(true);
		_nav.start();
	}  

	/**
	 * Creates a PathController which will navigate to a point via goTo() using a PathFinder to provide 
	 * assistance to create a path. 
	 * @param pilot
	 * @param poseProvider
	 * @param pathFinder
	 */
	public DelayNavPathController(SyncDiffPilot pilot, PoseProvider poseProvider, PathFinder pathFinder) {
		this(pilot, poseProvider);
		setPathFinder(pathFinder);
	}

	public void setPathFinder(PathFinder pathFinder) {
		this.pathFinder = pathFinder; 
		final PathController pc = this; // Grab reference to NavPathController object for inner class
		pathFinder.addListener(new WayPointListener() {

			public void nextWaypoint(WayPoint wp) {
				pc.addWayPoint(wp);
			}

			public void pathComplete() {
				// Nothing to do. PathController keeps waiting for more Waypoints in queue.
			}
		});	  
	}

	public PathFinder getPathFinder() {
		return pathFinder;
	}

	/** returns <code> false </code> if the the final waypoint has been reached or interrupt() has been called
	 */
	public boolean isGoing()
	{
		return _keepGoing;
	}

	public void followRoute(Collection<WayPoint>aRoute, boolean immediateReturn )
	{
		synchronized (_route) {
			_route = (ArrayList<WayPoint>) aRoute;
		}
		_keepGoing = true;
		System.out.println("Keepgoing true");
		if(immediateReturn)return;
		while(_keepGoing) Thread.yield();
	}

	/**
	 * This method will navigate to a point. If a PathFinder was used in the constructor, it will rely
	 * on it to calculate a series of waypoints to get to the destination.
	 */
	public void goTo(WayPoint destination, boolean immediateReturn)
	{
		// Check if using PathFinder:
		if(pathFinder == null) 
			addWayPoint(destination);
		else
			pathFinder.startPathFinding(poseProvider.getPose(), destination);

		if(!immediateReturn){
			while(_keepGoing)Thread.yield();
		}
	}

	/**
	 * This method will navigate to a point. If a PathFinder was used in the constructor, it will rely
	 * on it to calculate a series of waypoints to get to the destination.
	 */
	public void goTo(WayPoint destination) {

		goTo(destination, false);
		// TODO: It would be helpful to return boolean if it got to destination successfully.
	}

	/**
	 * This method will navigate to a point. If a PathFinder was used in the constructor, it will rely
	 * on it to calculate a series of waypoints to get to the destination.
	 */
	public void goTo(double x, double y) {
		goTo(new WayPoint(x, y));
	}

	public void addListener(WayPointListener aListener)
	{
		if(listeners == null )listeners = new ArrayList<WayPointListener>();
		listeners.add(aListener);
	}

	public void addTargetListener(WayPointListener targetListener)
	{
		if(targetListeners == null )targetListeners = new ArrayList<WayPointListener>();
		targetListeners.add(targetListener);
	}

	/**
	 * Returns a reference to the MoveController.
	 * The Navigator pose will be automatically updated as a result of methods
	 * executed on the MoveController.
	 * @return reference to the MoveController
	 */
	public MoveController getMoveController(){ return _pilot;}

	public void addWayPoint(WayPoint aWayPoint)
	{
		synchronized (_route) {
			_route.add(aWayPoint);
			_keepGoing = true;
			System.out.println("Keepgoing true");
		}
	} 
	
	@Override
	public void interrupt() {
		interrupt(true);
	}

	public void interrupt(boolean waitOnStop)
	{
		_keepGoing = false;
		_pilot.stop(waitOnStop);
		System.out.println("Nav interrupted");
	}

	public void resume()
	{
		synchronized (_route) {
			if(_route.size() > 0 ) _keepGoing = true;
			System.out.println("Keepgoing true");
		}
	}

	public void flushQueue()
	{
		synchronized (_route) {
			_keepGoing = false;
			System.out.println("Keepgoing false");
			_pilot.stop();
			for(int i = _route.size()-1 ; i > 0; i++)_route.remove(i);
		}
	}

	/**
	 * Returns the waypoint to which the robot is moving
	 * @return the waypoint to which the robot is moving
	 */
	public WayPoint getWayPoint()
	{
		synchronized (_route) {
			if(_route.size() <= 0 ) return null;
			return _route.get(0);
		}
	}

	public void setPoseProvider(PoseProvider aProvider)
	{
		poseProvider = aProvider;
	}

	public PoseProvider getPoseProvider()
	{
		return poseProvider;
	}

	public void waitForDestinationReached() {
		while (_keepGoing) Thread.yield();
	}

	/**
	 * This inner class runs the thread that processes the waypoint queue
	 */
	protected  class Nav extends Thread
	{
		boolean more = true;

		@Override
		public void run()
		{ 
			while (more)
			{
				//				System.out.println("keep going?");
				while (_keepGoing)// && _route != null && _route.size()>0)
				{
					//					System.out.println("Geting route");
					synchronized (_route) {
						_destination = _route.get(0);
					}
					//					System.out.println("Geting pose");
					_pose = poseProvider.getPose();
					if(_pose.distanceTo(_destination.getPose().getLocation()) > 100 ||
							(_destination instanceof WayPointOrden)){
						if(_pose.distanceTo(_destination.getPose().getLocation()) > 100){
//							System.out.println("My pose " + new PoseStr(_pose));
//							System.out.println("Destination " + _destination.x + "," + _destination.y);
							float destinationRelativeBearing = _pose.relativeBearing(_destination);
							if(!_keepGoing) break;
							
							//						System.out.println("doblando");
//							System.out.println("Rotando " + destinationRelativeBearing);
							((RotateMoveController) _pilot).rotate(destinationRelativeBearing, true);
							
							while (_pilot.isMoving() && _keepGoing)
							{
								//							System.out.println("yield mientras termino de doblar");
								Thread.yield();
							}
							
//							System.out.println("Se rotaron " + _pilot.getMovement().getAngleTurned());
							
							// Espera para que los movimientos se registren
							if(_keepGoing) Delay.msDelay(200);
							
							

							if(!_keepGoing) break;

							//						System.out.println("Obteniendo pose 2");
							_pose = poseProvider.getPose();
							if (_radius == 0 && _keepGoing)
							{
								//							System.out.println("Avanzando");
								float distance = _pose.distanceTo(_destination);
								_pilot.travel(distance, true);

								while (_pilot.isMoving() && _keepGoing)
								{
									Thread.yield();
								}
								
								// Espera para que los movimientos se registren
								if(_keepGoing) Delay.msDelay(200);

								if(!_keepGoing) break;

								//							System.out.println("Obteniendo pose 3");
								//								_pose = poseProvider.getPose();

							}
						}

						//							 Heading required
						// Hack, si el heading error es dif a -1, es que hay heading req
						if (_keepGoing && _destination.getMaxHeadingError() != -1) {
							_pose = poseProvider.getPose();


							float ang = Matematicas.angleDiffSig((_pose.getHeading()+360)%360,
									(float)(_destination.getHeading() + 360)%360);
//							System.out.println("Orients " + ((_pose.getHeading()+360)%360) + " " +
//									((_destination.getHeading() + 360)%360) + " " + ang);
							((RotateMoveController) _pilot).rotate(ang , true);
							while (_pilot.isMoving() && _keepGoing)
							{
								Thread.yield();
							}
//							System.out.println("En final se rotaron " + _pilot.getMovement().getAngleTurned());
							// Espera para que los movimientos se registren
							if(_keepGoing) Delay.msDelay(200);
						}


						if (!_keepGoing){
							//							System.out.println("break luego de pose 3");
							break;
						}

						//					if(listeners != null)
						//					{ 
						//						for(WayPointListener l : listeners)
						//							l.nextWaypoint(new WayPoint(poseProvider.getPose()));
						//					}
						//
						//					if(targetListeners != null)
						//					{ 
						//						for(WayPointListener l : targetListeners)
						//							l.nextWaypoint(_destination);
						//					}
					}
					synchronized (_route) {
						if (_keepGoing && 0 < _route.size()) {
							//						System.out.println("remove route");
							_route.remove(0);
						}	

					}
					if(listeners != null)
					{ 
						synchronized (_route) {
						for(WayPointListener l : listeners)
							if(l instanceof WayPointListenerInicio)
								((WayPointListenerInicio)l).wayPointComplete(_destination);
								if(_route.size() > 0)
									for(WayPointListener l : listeners)
										l.nextWaypoint(_route.get(0));
											//									else // Hack para procesar algunos waypoints de caminos de un waypoint
											//										for(WayPointListener l : listeners)
						}
					}
					synchronized (_route) {
						_keepGoing = _keepGoing && 0 < _route.size();
						System.out.println("Keepgoing " + new Boolean(_keepGoing).toString());
					}
											//											System.out.println("delay dentro while keepgoing");
											Delay.msDelay(200);
				} // end while keepGoing
//				System.out.println("Delay antes de otra iter nav");
				Delay.msDelay(200);
			}  // end while more
		}  // end run

	} // end Nav class

	protected Nav _nav ;
	protected ArrayList<WayPoint> _route  = new ArrayList<WayPoint>() ;
	protected ArrayList<WayPointListener> listeners;
	protected ArrayList<WayPointListener> targetListeners;
	protected boolean _keepGoing = false;
	protected SyncDiffPilot _pilot;
	protected PoseProvider poseProvider;
	protected PathFinder pathFinder = null;
	protected Pose _pose = new Pose();
	protected WayPoint _destination;
	protected double _radius;
	

}
