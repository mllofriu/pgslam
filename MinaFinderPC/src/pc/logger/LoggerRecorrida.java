package pc.logger;

import java.util.StringTokenizer;

import pc.comm.Comunication;
import brick.utils.Observable;
import brick.utils.Observer;


public class LoggerRecorrida extends Logger implements Observer{

	public LoggerRecorrida(String filename) {
		super(filename);
	}

	@Override
	public void update(Observable observable, Object arg) {
		StringTokenizer args = new StringTokenizer((String) arg, " ");
		Integer id = Integer.parseInt(args.nextToken());
		
		switch(id){
		case Comunication.IDUPDATE:
			addLog("update,"+args.nextToken());
			break;
		case Comunication.IDMOVSTART:
			addLog("movstart,"+args.nextToken());
			break;
		case Comunication.IDMOVEND:
			addLog("movend,"+args.nextToken());
			break;
		}

	}

}
