package pc.simulacion;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;

import lejos.robotics.navigation.Pose;
import pc.logger.Logger;
import pc.particulas.ParticleFrame;
import brick.particulas.ApplyMoveCmd;
import brick.particulas.Command;
import brick.particulas.MCLPoseProvider;
import brick.particulas.UpdateCmdMarcas;
import brick.particulas.mapa.IEEEMarcasMap;
import brick.particulas.mapa.Marca;
import brick.particulas.mmodel.GaussianMModel;
import brick.particulas.smodel.LineOrientSModel;
import brick.tests.CmdUtils;

public class GenDatosVisio {

	private static final int NUMPART = 10;
	private static final int PERIOD_GEN_CMD = 3;
	private LinkedList<List<Command>> corridas;
	private Pose initialPose;
	private MCLPoseProvider mcl;
	private IEEEMarcasMap map;
	private ParticleFrame frame;
	private Logger datosVisioLog;

	public GenDatosVisio(File folder) {
		map = new IEEEMarcasMap();
		initialPose = new Pose(300,200 , 0);
		mcl = new MCLPoseProvider(
				null,  NUMPART, initialPose, new LineOrientSModel(), new GaussianMModel());

		corridas = new LinkedList<List<Command>>();

		for(File f : folder.listFiles()){
			if(f.isFile()){
				corridas.add(CmdUtils.procesarCorrida(f));
			}
		}

		frame = new ParticleFrame(map, mcl.getParticles(), null);

		datosVisioLog = new Logger("corrAng");

		ejecutarCorridas(corridas);

		datosVisioLog.endLog();
	}

	private void ejecutarCorridas(LinkedList<List<Command>> corridas) {
		for(List<Command> corrida : corridas){
			mcl.setPose(initialPose);
			frame.setParticles(mcl.getParticles());

			int count = 1;
			for(Command c : corrida){
				c.execute(mcl);

				if (c instanceof ApplyMoveCmd)
					datosVisioLog.addLog(c.toString());
				//					System.out.println(c);

				if (count % PERIOD_GEN_CMD == 0){
					// Obtengo la pos del robot
					Pose robotPose = mcl.getPose();
					// Me quedo con la mas cercana
//					Iterator<Marca> mIter = map.getMarcas().iterator();
//					Marca closest = mIter.next();
					Marca closest = map.getMarcas()[0];
					int idClosest = 1;
					for (Marca m : map.getMarcas()){
//					while(mIter.hasNext()){
//						Marca tmp = mIter.next();
						if(robotPose.distanceTo(m.getMedia()) < 
								robotPose.distanceTo(closest.getMedia())){
							closest = m;
							idClosest = 1;
						}
					}
					// TODO: Agregar ruido

					// Genero un comando update
					float orientation = robotPose.getHeading();
					float anguloAmarca = (float) this.calcularAngulo(robotPose.getX(), robotPose.getY(), (float)closest.getMedia().getX(), (float)closest.getMedia().getY(), robotPose.getHeading());
					float dist = robotPose.distanceTo(closest.getMedia());
					datosVisioLog.addLog(new UpdateCmdMarcas(dist,idClosest,anguloAmarca).toString());
					//					System.out.println(new UpdateCmdMarcas(orientation,dist , closest.getId()));
				}

				count ++;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	private double calcularAngulo(float xPart, float yPart, float xMarc, float yMarc, float angPart) {
		double alfa =0;
		//divido en 8 casos
		if (xPart < xMarc && yPart == yMarc){
			alfa = angPart;
		}else if(xPart == xMarc && yPart < yMarc ){
			alfa = angPart - 90;			
		}else if (xPart > xMarc && yPart == yMarc){
			alfa = angPart - 180;
		}else if (xPart == xMarc && yPart > yMarc){
			alfa = angPart - 270;
		}else if (xPart < xMarc && yPart < yMarc){
			//cuadrante I
			alfa = Math.toDegrees(Math.atan( ((yMarc - yPart)/(xMarc - xPart)))); 
			alfa = angPart - alfa;
		}else if(xPart > xMarc && yPart < yMarc){
			//cuadrante II
			alfa = 180 - Math.toDegrees(Math.atan( ((yMarc - yPart)/(xPart - xMarc)))); 
			alfa = angPart - alfa;
		}else if (xPart > xMarc && yPart > yMarc){
			//cuadrante III
			alfa = 180 + Math.toDegrees(Math.atan( ((yPart - yMarc)/(xPart - xMarc)))); 
			alfa = angPart - alfa;
		}else if (xPart  < xMarc && yPart > xPart){
			//cuadrante IV
			alfa = 360 - Math.toDegrees(Math.atan( ((yPart - yMarc)/(xMarc - xPart)))); 
			alfa = angPart - alfa;

		}
		
		//negativo indica q la marca esta a la izquierda, positivo indica a la derecha
		return alfa;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Directorio con corridas");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//
		// disable the "All files" option.
		//
		chooser.setAcceptAllFileFilterUsed(false);
		//    
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
			new GenDatosVisio(chooser.getSelectedFile());
		} else {
			System.out.println("Debe elegir un directorio con corridas");
		}
	}



}
