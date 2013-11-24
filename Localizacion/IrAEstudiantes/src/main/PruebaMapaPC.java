package main;
import java.awt.FlowLayout;
import java.util.Collection;

import javax.swing.JFrame;

import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.FourWayGridMesh;
import lejos.robotics.pathfinding.NodePathFinder;
import navegador.mapa.MapaIEEE;

/**
 * Clase que genera una ventana donde es posible ver el mapa, grid y ruta generados utilizando MapCanvas.
 * @author ludo
 *
 */
public class PruebaMapaPC {


	private static final int NUMPART = 100;
	private static final int BORDER = 10;

	public static void main(String[] args) throws DestinationUnreachableException, InterruptedException {
		LineMap map = MapaIEEE.getIEEEMap();
		FourWayGridMesh mesh = new FourWayGridMesh(map, 200, (float) main.Robot.TRACK_WIDTH/2);
		NodePathFinder nPathFinder = new NodePathFinder(new AstarSearchAlgorithm(), mesh);
		Pose initialPose = new Pose(300+main.Robot.TRACK_WIDTH,main.Robot.TRACK_WIDTH, 0);
		Collection<WayPoint> route = nPathFinder.findRoute(
				initialPose,
				new WayPoint(1500 - main.Robot.TRACK_WIDTH,2400 - main.Robot.TRACK_WIDTH));

		
		JFrame mainFrame = new JFrame("Mapa");
		mainFrame.getContentPane().setLayout(new FlowLayout());
		MapCanvasNoParticles canvas = new MapCanvasNoParticles(map, mesh, route);
		mainFrame.getContentPane().add(canvas);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);

	}

}
