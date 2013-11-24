package pc.pruebas;

import pc.comm.PCCom;
import brick.utils.Observable;
import brick.utils.Observer;

public class TEscucha implements Observer{

	public TEscucha(){
		PCCom com = PCCom.getInstance();
		com.addObserver(this);
		
		while(true){
			com.recibir();
		}
	}
	
	public static void main(String [] args)
	{
		new TEscucha();
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println((String) arg);
	}
}
