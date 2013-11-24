package pc.pruebas;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ObjCamViewer extends JFrame  {

	public static final int CAM_WIDTH = 176;
	public static final int CAM_HEIGHT = 144;
	private static final boolean enLinea = true;
	private static final String LOGTOREAD = "marcaChanfle.log";
	private static final String LOG_TOWRITE = "log.log";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ObjCamViewer();
	}

	private boolean actualizar = true;
	private CanvasCamara canvas;
	BufferedWriter log = null;
	private Reader reader;

	public ObjCamViewer(){
		// Loggin file
		try {
			FileWriter fstream = new FileWriter(LOG_TOWRITE);
		   log = new BufferedWriter(fstream);
//			System.out.println(f.getAbsolutePath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			 public void windowClosing( WindowEvent e ) {  
				 try {
					log.flush();
					log.close();
					System.out.println("Chau");
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			 }
				 
		});
		getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));

		canvas = new CanvasCamara();
		canvas.setSize(new Dimension(CAM_WIDTH,CAM_HEIGHT));
		getContentPane().add(canvas);

		JPanel cmds = new JPanel();
		cmds.setLayout(new BoxLayout(cmds, BoxLayout.X_AXIS));

		JCheckBox act = new JCheckBox("Actualizar");
		act.setSelected(actualizar);
		act.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				reader.setActualizar(((JCheckBox) ae.getSource()).isSelected());
			}
		});
		cmds.add(act);

		getContentPane().add(cmds);

		if(enLinea){
			reader = new RobotReader(log, canvas, actualizar);			
		} else {
			try {
				FileReader fread = new FileReader(LOGTOREAD);
				BufferedReader logReader = new BufferedReader(fread);
				reader = new LogReader(logReader, canvas, actualizar);
//				reader = new TrainSetReader(logReader, canvas, actualizar);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		reader.start();	
			
			
			 
				

		setSize(new Dimension(800, 600));
		setVisible(true);
	}

	

	

}
