package pc.pruebas.comm;

import pc.comm.Comunication;
import brick.utils.Observable;
import brick.utils.Observer;

public class InputListener implements Observer{
	private Comunication com;
	public InputListener(Comunication com){
		com.addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		String data = (String)arg;
		System.out.println(data);
	}
}
