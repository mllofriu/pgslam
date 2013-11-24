package pc.particulas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JFrame;

import lejos.geom.Line;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;
import lejos.robotics.pathfinding.FourWayGridMesh;
import pc.comm.DoraIntegrator;
import brick.particulas.MCLParticle;
import brick.particulas.MCLParticleSet;
import brick.particulas.MCLPoseProvider;
import brick.particulas.mapa.IEEERecorridaMap;
import brick.particulas.mapa.MapaIEEE;
import brick.particulas.mapa.MapaMarcasLineas;
import brick.particulas.mapa.Marca;
import brick.utils.Constantes;
import brick.utils.DetectorDist;

public class MarcasMapCanvas extends JComponent {

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

	private MapaMarcasLineas map;
	private FourWayGridMesh mesh = null;
	private Collection<WayPoint> route;

	private MCLParticleSet particles;

	private DoraIntegrator dora = null;

	private Pose truePose;


	private int idMarca = -1;
	private float distMarca;
	private int countShowMarca;

	private WayPoint nextWp = null;

	private boolean[][] cubierto = null;

	private LinkedList<Line> validRect;


	public Pose getTruePose() {
		return truePose;
	}

	public void setTruePose(Pose truePose) {
		this.truePose = truePose;
	}

	public MarcasMapCanvas(MapaMarcasLineas map, FourWayGridMesh mesh, Collection<WayPoint> route){
		this.map = map;
		//		this.mesh = mesh;
		this.mesh = null;
		this.route = route;

		double scale = (DIMENSION_PANTALLA*SCALE/MapaIEEE.getLargerSide());
		this.DIAMETRO = (int) (50d * scale); 
		
		validRect = new LinkedList<Line>();
		int bound = DetectorDist.MAXDIST * 10 - Constantes.ANCHOCOBERTURA / 2;
		validRect.add(new Line(bound, 200, 2000-bound, 200));
		validRect.add(new Line(bound, 200, bound, 2400 - bound));
		validRect.add(new Line(2000-bound, 200, 2000-bound, 2400 - bound));
		validRect.add(new Line(bound, 2400-bound, 2000 - bound, 2400 - bound));
	}

	public MarcasMapCanvas(MapaMarcasLineas map, FourWayGridMesh mesh, Collection<WayPoint> route,
			DoraIntegrator dora) {
		this(map,mesh, route);
		this.dora  = dora;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);	

		// draw entire component white
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(Color.gray);
		if(cubierto != null){
			for (int i = 0; i < cubierto.length; i++)
				for(int j = 0; j < cubierto[i].length; j++)
					if(cubierto[i][j]){
						Point p = t(new lejos.geom.Point(
								Constantes.PRECISION_GRILLA * i, Constantes.PRECISION_GRILLA * j));
						double scale = (DIMENSION_PANTALLA*SCALE/MapaIEEE.getLargerSide());
						g.fillRect(p.x,p.y,
								(int)(Constantes.PRECISION_GRILLA*scale),
								(int)(Constantes.PRECISION_GRILLA*scale));
					}
						
			// Dibujar lineas de cuadrado valido
			g.setColor(Color.blue);
			// Dibujo cada linea
			
			for(Line l : validRect){
				// Obtengo coordenadas de linea
				Point p1 = t(l.getP1());
				Point p2 = t(l.getP2());
				g.drawLine(p1.x, p1.y, p2.x, p2.y);			
			}
		}
		
		g.setColor(Color.blue);
		// Dibujo cada linea
		for(Line l : map.getLines()){
			// Obtengo coordenadas de linea
			Point p1 = t(l.getP1());
			Point p2 = t(l.getP2());
			g.drawLine(p1.x, p1.y, p2.x, p2.y);			
		}

		g.setColor(Color.gray);
		int id = 1;
		for(Marca m : map.getMarcas()){
			Point t = t(m.getMedia());
			dibujarCuad(g, t, 26);  //ese 26 quien es?

			g.drawString(Integer.toString(id), t.x,t.y );
			id++;
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
		//		if(nextWp != null){
		//			Point p1 = t(new lejos.geom.Point(nextWp.x, nextWp.y));
		//			dibujarCruz(g, p1, 40);
		////			g.drawOval(p1.x, p1.y, 2, 2);
		//		}
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
			Point p = t(new lejos.geom.Point(dora.getX(), dora.getY())); // Cosecha no tomado en cuenta
			dibujarCruz(g, p, 50);
		}

		if (truePose != null){
			g.setColor(Color.green);
			Point p = t(new lejos.geom.Point(truePose.getX(), truePose.getY()));
			dibujarCruz(g, p, 50);
		}

		if(particles != null){
			g.setColor(Color.red);
			if(drawParticles){
				for(MCLParticle p : particles.getParticles()){
					dibujarParticula(p, g, DIAMETRO, Color.red);//				System.out.println("Dibujando particula");
				}
				
				
			}
			
			g.setColor(Color.blue);
			MCLPoseProvider prov = new MCLPoseProvider(particles);
			prov.setDebug(false);
			Pose poseMCL = prov.getPose();
			double scale = (DIMENSION_PANTALLA*SCALE/MapaIEEE.getLargerSide());
			int diametro = (int) (200d * scale); 
			dibujarParticula(new MCLParticle(poseMCL.getX(), poseMCL.getY(), poseMCL.getHeading()),
					g, diametro, Color.blue);
			
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

		

		g.setColor(Color.green);
		if(nextWp != null){
			Point p1 = t(new lejos.geom.Point(nextWp.x, nextWp.y));
			dibujarCruz(g, p1, 40);
			//			g.drawOval(p1.x, p1.y, 2, 2);
		}

		if(idMarca != -1){
			dibujarObserMarca(g, distMarca, idMarca);
			countShowMarca--;
			if(countShowMarca <= 0)
				idMarca = -1;
		}

	}

	private void dibujarParticula(MCLParticle p, Graphics g, int diametro, Color color) {
		lejos.geom.Point tmp = new lejos.geom.Point(p.getX(), p.getY());
		Point pose = t(tmp);

		g.setColor(color);
		g.drawOval(pose.x-diametro/2, pose.y-diametro/2,diametro, diametro);
		//tengo q pensarlo un cacho

		double headRad = p.getH() / 180 * Math.PI;
		double ady = (diametro/2)*Math.cos(headRad);
		double op = (diametro/2)*Math.sin(headRad);
		int adyint = (int)ady;
		int opint = (int)op; 
		//					System.out.println("heading-->" + p.getPose().getHeading() + " ady = " + ady + " op= " + op + " weight " + p.getWeight());

		g.setColor(Color.black);
		//					Point dir = new lejos.geom.Point(adyint, opint);
		g.drawLine(pose.x, pose.y, pose.x + adyint, pose.y-opint);

		g.setColor(Color.orange);

		Marca[] marcas = p.getMarcas();
		for (int i=0;i<Constantes.cantMarcas;i++){
			Marca marca = marcas[i];
			//dibujo las que tengan sentido dibujar, las que tomaron algun valor
			if(marca != null){

				switch (i){
				case 0:g.setColor(Color.orange);
				break;
				case 1:g.setColor(Color.blue);
				break;
				case 2:g.setColor(Color.red);
				break;
				case 3:g.setColor(Color.magenta);
				break;
				case 4:g.setColor(Color.pink);
				break;
				case 5:g.setColor(Color.green);
				}
				dibujarCuad(g, t(marca.getMedia()), 26);

			}
		}




	}

	private lejos.geom.Point awtToLejosPoint(Point media) {
		return new lejos.geom.Point((float)(media.getX()), (float) media.getY());
	}

	private void dibujarObserMarca(Graphics g, float dist, int id) {
		g.setColor(Color.orange);
		// busco la marca en el mapa
		if(map.isMarca(idMarca)){
			Marca m = map.getMarca(idMarca);

			lejos.geom.Point p = m.getMedia();
			// La y va conjugada por el espejo - no entiendo bien por que
			Point ul =  t(new lejos.geom.Point(p.x - dist , p.y + dist));
			Point dr =  t(new lejos.geom.Point(p.x + dist , p.y - dist ));

			g.drawOval(ul.x, ul.y, Math.abs(ul.x - dr.x), Math.abs(ul.y - dr.y));
		}
	}

	private void dibujarCuad(Graphics g, Point c, int l) {
		g.drawRect(c.x - l / 2, c.y - l / 2, l, l);

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
		MapaMarcasLineas map = new IEEERecorridaMap();
		JFrame mainFrame = new JFrame("Mapa");
		mainFrame.getContentPane().setLayout(new FlowLayout());
		MarcasMapCanvas canv = new MarcasMapCanvas(map, null, null);
		mainFrame.getContentPane().add(canv);
		//		map = MapaIEEE.getIEEEMap();

		canv.setObservMarca(6, 200f);

		canv.setCubierto(new boolean[0][0]);
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

	public void setObservMarca(int id, float dist) {
		this.idMarca = id;
		this.distMarca = dist;
		this.countShowMarca = 3;
	}

	public void setNextWaypoint(WayPoint wp) {
		this.nextWp  = wp;
	}

	public void setCubierto(boolean[][] cubierto) {
		this.cubierto  = cubierto;
	}


} 
