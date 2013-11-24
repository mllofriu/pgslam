package pc.particulas;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;
import pc.comm.Comunication;
import pc.comm.DoraIntegrator;
import brick.particulas.MCLParticleSet;
import brick.particulas.mapa.MapaMarcasLineas;
import brick.utils.Observable;
import brick.utils.Observer;

public class ParticleFrame extends JFrame implements Observer, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7459733807264180341L;
	private boolean actualizar;
	private HeadingCanvas hc;
	private MarcasMapCanvas canvas;

	public ParticleFrame(MapaMarcasLineas map, MCLParticleSet particles, DoraIntegrator dora){
		super("Particulas");
		
		//dibujo mapa
		actualizar = true;
		
		Container cPane = getContentPane();
		cPane.setLayout(new BoxLayout(cPane, BoxLayout.X_AXIS));
		canvas = new MarcasMapCanvas(map, null, null, dora);
		canvas.addMouseListener(new ActualizarML());
		cPane.add(canvas);

		JPanel info = new JPanel();
		info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS));

		hc = new HeadingCanvas();
		info.add(new JLabel("Orientacion sensada"));
		info.add(hc);

		cPane.add(info);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);

		canvas.setParticles(particles);	
		canvas.repaint();
		
		new Thread(this).start();

		
	}

	public void setCom(Comunication com){
		com.addObserver(hc);
		com.addObserver(this);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// En cada comunicacion hago repaint
//		System.out.println("ParticleFrame: llego paquete");
//		if(actualizar)
//			SwingUtilities.invokeLater(new Runnable() {
//				@Override
//				public void run() {
//					repaint();					
//				}
//			});
			
	}
	
	public class ActualizarML implements MouseListener {

		@Override
		public void mouseReleased(MouseEvent arg0) {
			actualizar = !actualizar;

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void run() {
		while(true){
			repaint();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setTruePose(Pose p){
		canvas.setTruePose(p);
	}

	public void setParticles(MCLParticleSet particles) {
		canvas.setParticles(particles);
	}

	public void setObservMarca(int id, float dist) {
		canvas.setObservMarca(id, dist);
	}

	public void setNextWaypoint(WayPoint wp) {
		canvas.setNextWaypoint(wp);
	}

	public void setCubrimiento(boolean[][] cubierto) {
		canvas.setCubierto(cubierto);
	}

}
