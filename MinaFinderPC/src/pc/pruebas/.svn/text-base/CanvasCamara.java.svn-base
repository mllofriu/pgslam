package pc.pruebas;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import brick.marcas.ObjCamara;
import brick.marcas.ObservMarca;

public class CanvasCamara extends JComponent {

	private static final float MIN_PROB = -70;
	private List<ObjCamara> objs = null;
	private List<ObservMarca> marcas = null;
	private Color[] colors = {Color.orange, Color.blue, Color.gray};

	public void setObjsCam(List<ObjCamara> objs){
		this.objs = objs;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setTransform(new AffineTransform(3, 0, 0, 3,0,0));
		// draw entire component white
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());



//		if (objs != null){
//			for(ObjCamara obj : objs){
//				//				g.drawRect((int)obj.getR().getX(),(int) obj.getR().getY(),
//				//						(int) obj.getR().getWidth(),(int) obj.getR().getHeight());
//				g.setColor(colors[obj.getColor()]);
//				g2d.draw(obj.getR());
//			}
//		}

		if (marcas != null){
			for(ObservMarca marca : marcas){
				//				g.drawRect((int)obj.getR().getX(),(int) obj.getR().getY(),
				//						(int) obj.getR().getWidth(),(int) obj.getR().getHeight());
				if(marca.getProb() > MIN_PROB){
					for(ObjCamara obj : marca.getObjs()){
						//				g.drawRect((int)obj.getR().getX(),(int) obj.getR().getY(),
						//						(int) obj.getR().getWidth(),(int) obj.getR().getHeight());
						g.setColor(colors[obj.getColor()]);
						g2d.draw(obj.getR());
					}
					
					g.setColor(Color.red);
					g2d.draw(marca.getR());
					String idS = new Integer(marca.getId()).toString();
					String distS = new Float(marca.getDistancia()).toString();
					String prob = new Float(marca.getProb()).toString();
					String infoS = idS + " " + distS + " " + prob;
					char[] info = infoS.toCharArray(); 
					g2d.setFont(new Font("Arial",0, 10));
					g2d.drawChars(info, 0, info.length,
							(int) (marca.getR().x - 10), (int)(marca.getR().y - 10));
				}
			}
		}
	}

	public void setMarcas(List<ObservMarca> marcas) {
		this.marcas = marcas;

	}

	public void emtpyObjsMarcas() {
		this.marcas = new ArrayList<ObservMarca>();
		this.objs = new ArrayList<ObjCamara>();
	}
}
