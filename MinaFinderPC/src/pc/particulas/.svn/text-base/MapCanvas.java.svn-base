package pc.particulas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JFrame;

import lejos.geom.Line;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;
import lejos.robotics.pathfinding.FourWayGridMesh;
import pc.comm.DoraIntegrator;
import brick.particulas.MCLParticle;
import brick.particulas.MCLParticleSet;
import brick.particulas.MCLPoseProvider;
import brick.particulas.mapa.MapaIEEE;

public class MapCanvas extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4310014214639423545L;

	private static final boolean drawParticles = true;

	private static final boolean drawVariance = false;

	private int DIAMETRO;



	private final static int DIMENSION_PANTALLA = 700;

	private final static int MAX_X = 600;
	private final static int MAX_Y = 700;

	private static final double SCALE = 0.85;

	private static final double OFFSET = 50;

	private LineMap map;
	private FourWayGridMesh mesh = null;
	private Collection<WayPoint> route;

	private MCLParticleSet particles;

	private DoraIntegrator dora = null;

	private Pose truePose;

	public Pose getTruePose() {
		return truePose;
	}

	public void setTruePose(Pose truePose) {
		this.truePose = truePose;
	}

	public MapCanvas(LineMap map, FourWayGridMesh mesh, Collection<WayPoint> route){
		this.map = map;
		//		this.mesh = mesh;
		this.mesh = null;
		this.route = route;

		double scale = (DIMENSION_PANTALLA*SCALE/MapaIEEE.getLargerSide());
		this.DIAMETRO = (int) (50d * scale); 
	}

	public MapCanvas(LineMap map, FourWayGridMesh mesh, Collection<WayPoint> route,
			DoraIntegrator dora) {
		this(map,mesh, route);
		this.dora  = dora;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		

		// draw entire component white
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.blue);
		// Dibujo cada linea
		for(Line l : map.getLines()){
			// Obtengo coordenadas de linea
			Point p1 = t(l.getP1());
			Point p2 = t(l.getP2());
			g.drawLine(p1.x, p1.y, p2.x, p2.y);			
		}

		g.setColor(Color.gray);
		if (mesh != null)
			for (lejos.robotics.pathfinding.Node n : mesh.getMesh())
				for(lejos.robotics.pathfinding.Node b : n.getNeighbors()){
					Point p1 = t(new lejos.geom.Point(n.x, n.y));
					Point p2 = t(new lejos.geom.Point(b.x, b.y));
					g.drawLine(p1.x, p1.y, p2.x, p2.y);		
				}

		g.setColor(Color.green);
		if(route != null){
			Iterator<WayPoint> it = route.iterator();
			WayPoint ant = it.next();
			while(it.hasNext()){
				WayPoint itW = it.next();
				Point p1 = t(new lejos.geom.Point(itW.x, itW.y));
				Point p2 = t(new lejos.geom.Point(ant.x, ant.y));
				g.drawLine(p1.x, p1.y, p2.x, p2.y);		
				ant = itW;
			}
		}

//		g.drawLine(600,200 , 605, 200);
//		g.drawLine(600,200 ,605, 205);

		if(dora != null){
			g.setColor(Color.green);
			// Sumo trescientos por dora configurado solo dentro de zona navegable sin zona de arboles
			// TODO: Cambiar mapa
			Point p = t(new lejos.geom.Point(dora.getX(), dora.getY()));
			dibujarCruz(g, p, 50);
		}
		
		if (truePose != null){
			g.setColor(Color.green);
			Point p = t(new lejos.geom.Point(truePose.getX(), truePose.getY()));
			dibujarCruz(g, p, 50);
		}
			
		
		
		if(particles != null){
			g.setColor(Color.red);
			if(drawParticles)
				for(MCLParticle p : particles.getParticles()){
					//				System.out.println("Dibujando particula");
					lejos.geom.Point tmp = new lejos.geom.Point(p.getX(), p.getY());
					Point pose = t(tmp);

					g.setColor(Color.red);
					g.drawOval(pose.x-DIAMETRO/2, pose.y-DIAMETRO/2,DIAMETRO, DIAMETRO);
					//tengo q pensarlo un cacho

					double headRad = p.getH() / 180 * Math.PI;
					double ady = (DIAMETRO/2)*Math.cos(headRad);
					double op = (DIAMETRO/2)*Math.sin(headRad);
					int adyint = (int)ady;
					int opint = (int)op; 
					//					System.out.println("heading-->" + p.getPose().getHeading() + " ady = " + ady + " op= " + op + " weight " + p.getWeight());

					g.setColor(Color.black);
//					Point dir = new lejos.geom.Point(adyint, opint);
					g.drawLine(pose.x, pose.y, pose.x + adyint, pose.y-opint);



				}
			if(drawVariance){
				double xMean = 0, yMean = 0, xVar = 0, yVar = 0;
				int cantParticles = particles.getParticles().length;
				for(MCLParticle p : particles.getParticles()){
					//				System.out.println("Dibujando particula");
					lejos.geom.Point tmp = new lejos.geom.Point(p.getX(), p.getY());
					Point pose = t(tmp);
					xMean += pose.x / cantParticles;
					yMean += pose.y / cantParticles;
				}
				for(MCLParticle p : particles.getParticles()){
					//				System.out.println("Dibujando particula");
					lejos.geom.Point tmp = new lejos.geom.Point(p.getX(), p.getY());
					Point pose = t(tmp);
					xVar += Math.pow(pose.x - xMean, 2) / cantParticles;
					yMean += Math.pow(pose.y - yMean, 2) / cantParticles;
				}

				g.drawLine((int)(xMean - xVar / 2), (int)(yMean),
						(int)(xMean + xVar / 2), (int) (yMean));
				g.drawLine((int)(xMean), (int)(yMean - yVar / 2),
						(int)(xMean), (int) (yMean + xVar / 2) );
				g.drawOval((int)xMean, (int)yMean, (int)xVar/2, (int)yVar/2);
			}
		}
		
		if(particles != null){
			g.setColor(Color.blue);
			MCLPoseProvider prov = new MCLPoseProvider(particles);
			prov.setDebug(false);
			Pose pose = prov.getPose();
//			System.out.println(pose.getX() + " " + pose.getY());
			Point p = t(new lejos.geom.Point(pose.getX(), pose.getY()));
			dibujarCruz(g, p, 50);
		}

	}

	private void dibujarCruz(Graphics g, Point p, int l) {
		g.drawLine(p.x - l/2, p.y, p.x + l/2, p.y);
		g.drawLine(p.x, p.y - l/2, p.x, p.y + l/2);
	}

	private Point t(lejos.geom.Point point)
	{
		Point res = new Point();
		// Agrando un poco el lado más largo para ver las lineas exteriores

		res.x = /*MAX_X - */scale(point.x );
		res.y = MAX_Y - scale (point.y);
		return res;
	}

	private static int scale(double d){
		double scale = (DIMENSION_PANTALLA*SCALE/MapaIEEE.getLargerSide());
		return (int) (d * scale + OFFSET);
	}

	public Dimension getPreferredSize() {
		return new Dimension(DIMENSION_PANTALLA, DIMENSION_PANTALLA);
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	//	public static void main(String args[]) throws DestinationUnreachableException {
	//		LineMap map = MapaIEEE.getIEEEMap();
	//		JFrame mainFrame = new JFrame("Mapa");
	//		mainFrame.getContentPane().setLayout(new FlowLayout());
	////		mainFrame.getContentPane().add(new MapCanvas(map));
	//		map = MapaIEEE.getIEEEMap();
	//		//DijkstraPathFinder dPathFinder = new DijkstraPathFinder(map);
	////		dPathFinder.lengthenLines((float) Robot.TRACK_WIDTH);
	//		mainFrame.getContentPane().add(new MapCanvas(map));
	//		
	//		mesh = new FourWayGridMesh(map, 200, (float) Robot.TRACK_WIDTH/2);
	//		NodePathFinder nPathFinder = new NodePathFinder(new AstarSearchAlgorithm(), mesh);
	//		
	//		route = findRoute(nPathFinder,
	//				new Pose(300+Robot.TRACK_WIDTH,Robot.TRACK_WIDTH, 0),
	//				new WayPoint(1500 - Robot.TRACK_WIDTH,2400 - Robot.TRACK_WIDTH));
	//		
	//		mainFrame.pack();
	//		mainFrame.setVisible(true);
	//		
	//		
	//	}

//	public static void main(String args[]){
//		LineMap map = MapaIEEE.getIEEEMap();
//		JFrame mainFrame = new JFrame("Mapa");
//		mainFrame.getContentPane().setLayout(new FlowLayout());
//		MapCanvas canv = new MapCanvas(map, null, null);
//		mainFrame.getContentPane().add(canv);
//		map = MapaIEEE.getIEEEMap();
//		
//		MCLLineParticleSet mclps = new MCLLineParticleSet(map,5,new Pose(600,200 , 0),100,10);
//		canv.setParticles(mclps);
//
//		mainFrame.pack();
//		mainFrame.setVisible(true);
//	}
	
	public static void main(String args[]){
		LineMap map = MapaIEEE.getIEEEMap();
		JFrame mainFrame = new JFrame("Mapa");
		mainFrame.getContentPane().setLayout(new FlowLayout());
		MapCanvas canv = new MapCanvas(map, null, null);
		mainFrame.getContentPane().add(canv);
		map = MapaIEEE.getIEEEMap();
		
		
		mainFrame.pack();
		mainFrame.setVisible(true);
	}


	//	private static Collection<WayPoint> findRoute(PathFinder pf, Pose p, WayPoint wp) throws DestinationUnreachableException{
	//		return pf.findRoute(p, wp);
	//		
	//	}
	public void setParticles(MCLParticleSet mclLineParticleSet){
		this.particles = mclLineParticleSet;
	}

	public void setRoute(Collection<WayPoint> route) {
		this.route = route;
	}
} 
