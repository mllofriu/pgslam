package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JFrame;

import sun.dc.pr.PathFiller;

import lejos.geom.Line;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.FourWayGridMesh;
import lejos.robotics.pathfinding.NodePathFinder;
import lejos.robotics.pathfinding.PathFinder;
import localizacion.MCLLineParticle;
import localizacion.MCLLineParticleSet;
import navegador.mapa.MapaIEEE;

public class MapCanvas extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4310014214639423545L;

	private static final boolean drawParticles = true;

	private static final boolean drawVariance = false;

	private final int DIMENSION = 800;

	private LineMap map;
	private FourWayGridMesh mesh = null;
	private Collection<WayPoint> route;

	private MCLLineParticleSet particles;

	public MapCanvas(LineMap map, FourWayGridMesh mesh, Collection<WayPoint> route){
		this.map = map;
		this.mesh = mesh;
		this.route = route;
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
		
		if(particles != null){
			g.setColor(Color.red);
			if(drawParticles)
				for(MCLLineParticle p : particles.getParticles()){
	//				System.out.println("Dibujando particula");
					Point pose = t(p.getPose().getLocation());
					g.drawOval(pose.x, pose.y, 2, 2);
				}
			if(drawVariance){
				double xMean = 0, yMean = 0, xVar = 0, yVar = 0;
				int cantParticles = particles.getParticles().length;
				for(MCLLineParticle p : particles.getParticles()){
					//				System.out.println("Dibujando particula");
					Point pose = t(p.getPose().getLocation());
					xMean += pose.x / cantParticles;
					yMean += pose.y / cantParticles;
				}
				for(MCLLineParticle p : particles.getParticles()){
					//				System.out.println("Dibujando particula");
					Point pose = t(p.getPose().getLocation());
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
				
	}

	private Point t(lejos.geom.Point point)
	{
		Point res = new Point();
		// Agrando un poco el lado más largo para ver las lineas exteriores
		double scale = (DIMENSION / (MapaIEEE.getLargerSide()*1.1));
		res.x = (int) (point.x * scale);
		res.y = (int) (point.y * scale);
		return res;
	}

	public Dimension getPreferredSize() {
		return new Dimension(DIMENSION, DIMENSION);
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
	
//	private static Collection<WayPoint> findRoute(PathFinder pf, Pose p, WayPoint wp) throws DestinationUnreachableException{
//		return pf.findRoute(p, wp);
//		
//	}
	public void setParticles(MCLLineParticleSet mclLineParticleSet){
		this.particles = mclLineParticleSet;
	}

	public void setRoute(Collection<WayPoint> route) {
		this.route = route;
	}
} 
