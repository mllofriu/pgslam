package localizacion;
import java.awt.FlowLayout;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;

import com.sun.java.swing.plaf.windows.resources.windows;

import lejos.robotics.localization.MCLPoseProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.mapping.RangeMap;
import lejos.robotics.navigation.DestinationUnreachableException;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.FourWayGridMesh;
import lejos.robotics.pathfinding.NodePathFinder;
import main.MapCanvas;
import navegador.mapa.MapaIEEE;


public class ParticlesPCTest {


	private static final int NUMPART = 20;
	private static final int BORDER = 10;

	public static void main(String[] args) throws DestinationUnreachableException, InterruptedException {
		LineMap map = MapaIEEE.getIEEEMap();
		FourWayGridMesh mesh = new FourWayGridMesh(map, 200, (float) main.Robot.TRACK_WIDTH/2);//grafo 
		//		NodePathFinder nPathFinder = new NodePathFinder(new AstarSearchAlgorithm(), mesh);
//		Pose initialPose = new Pose(900 - main.Robot.TRACK_WIDTH/2,300 - main.Robot.TRACK_WIDTH, 90);
		Pose initialPose = new Pose(900,200 , 90);
		////		Collection<WayPoint> route = nPathFinder.findRoute(
		//				initialPose,
		//				new WayPoint(1500 - main.Robot.TRACK_WIDTH,2400 - main.Robot.TRACK_WIDTH));

		//dibujo mapa
		JFrame mainFrame = new JFrame("Mapa");
		mainFrame.getContentPane().setLayout(new FlowLayout());
		MapCanvas canvas = new MapCanvas(map, mesh, null);
		mainFrame.getContentPane().add(canvas);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);

//		SimMoveProvider simMP = new SimMoveProvider(route);
		MoveProvider mp = new Rcvmov();
		MCLLinePoseProvider mcl = new MCLLinePoseProvider(mp, null, null, map, NUMPART, BORDER, initialPose);
		canvas.setParticles(mcl.getParticles());	
		canvas.repaint();
		Thread.sleep(10000);//xq esta 
		while(true){
			canvas.repaint();
			Thread.sleep(2000);
		}
//		Thread.sleep(2000);
//		mcl.update();
//		canvas.repaint();
//		Thread.sleep(2000);

	}

}
