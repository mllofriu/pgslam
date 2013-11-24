package pc.particulas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;

import pc.comm.Comunication;
import pc.comm.PCCom;
import brick.comm.Mensaje;
import brick.particulas.MCLParticle;
import brick.utils.Observer;

public class HeadingCanvas extends JComponent implements Observer {
	
	private static final float XPART = 50;
	private static final float YPART = 50;
	private int DIAMETROPART = 50;
	private MCLParticle particle = null;

	public HeadingCanvas(){
		
//		particle = new MCLLineParticle(new Pose(XPART, YPART, 90));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.red);
		if(particle != null)
			drawParticle(particle, g);
	}
		
	
	private void drawParticle(MCLParticle p, Graphics g) {
		float x = p.getX();
		float y = p.getY();
		float h = p.getH();

		g.setColor(Color.red);
		g.drawOval((int)x-DIAMETROPART/2,(int) y-DIAMETROPART/2,
				DIAMETROPART, DIAMETROPART);
		//tengo q pensarlo un cacho
		
		double headRad = h / 180 * Math.PI;
		double ady = (DIAMETROPART/2)*Math.cos(headRad);
		double op = (DIAMETROPART/2)*Math.sin(headRad);
		int adyint = (int)ady;
		int opint = (int)op; 
//		System.out.println("heading-->" + p.getPose().getHeading() + " ady = " + ady + " op= " + op + " weight " + p.getWeight());
 
		g.setColor(Color.black);
		lejos.geom.Point dir = new lejos.geom.Point(adyint, opint);
		g.drawLine((int)x,(int) y,(int) (x + dir.x) ,(int) (y- dir.y));
		
	}
	

	public static void main(String[] args){
		JFrame frame = new JFrame();
		Comunication com = PCCom.getInstance();
		HeadingCanvas hc = new HeadingCanvas();
		frame.getContentPane().add(hc);
		com.addObserver(hc);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(200, 100);
	}

	@Override
	public void update(brick.utils.Observable o, Object arg) {
		Mensaje m = (Mensaje) arg;
//		System.out.println("Id en heading canvas "+id);
		if( m.getId() == Comunication.IDMEDIDAHEADING){
			// Parseo la direccion de la partícula
			particle = new MCLParticle(XPART, YPART,
					Float.parseFloat(m.getStr()));
			repaint();
		}
	}
	
	
}
