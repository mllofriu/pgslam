package pc.logger;

import java.util.Scanner;

import pc.comm.Comunication;
import pc.comm.DoraIntegrator;
import pc.particulas.SocketCliCom;
import brick.comm.Mensaje;
import brick.particulas.Command;
import brick.particulas.TruePosCmd;
import brick.particulas.UpdateCmdMarcas;
import brick.utils.Observable;
import brick.utils.Observer;

public class LogCmds extends Logger implements Observer {
	protected static final long RUNTIME = 10 * 60 * 1000;
	private DoraIntegrator di;
	private Comunication com;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Ingrese el nombre del archivo donde grabar los comandos: ");
		Scanner sc = new Scanner(System.in);
		String filename = sc.next();
//		String filename = "corrida1.txt";
	
		new LogCmds(filename).run();
		
		
	}

	private void run() {
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					Thread.sleep(RUNTIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				endLog();
					
				System.exit(0);
			}
			
		}).start();
		
		while(true)
			com.recibir();
	}


	public LogCmds(String filename){
		super(filename);
		di = new DoraIntegrator();
		com = SocketCliCom.getInstance();
		com.addObserver(this);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		Mensaje mensaje = (Mensaje) arg;
		if(mensaje.getId() == Comunication.IDCOM){
			Command cmd = Command.parseCommand(mensaje.getStr());
			addLog(cmd.toString());
			if(cmd instanceof UpdateCmdMarcas)
				addLog(new TruePosCmd(di.getX(), di.getY()).toString());
//			System.out.println("LogCmds: cmd logged");
		} else if (mensaje.getId() == Comunication.IDFIN){
			endLog();
			di.end();
			System.exit(0);
		}
	}

}
