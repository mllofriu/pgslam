package localizacion;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;


import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import lejos.robotics.navigation.Move;

public class OdoMovRcv {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NXTComm nxtComm;
		try{
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			NXTInfo nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH,"proy","00:16:53:10:D5:05");
			nxtComm.open(nxtInfo);
			System.out.println("Conexion establecida");
			DataInputStream dis = new DataInputStream(nxtComm.getInputStream());

			//			Logger logDatos = new Logger("poses.txt");
			//			logDatos.addLog("datos de navegacion");

			String[] moveData = new String[4];			
			Float dist,ang;
			Boolean isMoving;
			while (true){
				String data = dis.readUTF(); 
				moveData = data.split(",");
				if (moveData[0].equals("mstarted")){ 
					System.out.println("mstarted bro");
				}else if (moveData.equals("mstopped")){
					System.out.println("mstoped bro" + moveData[1] + moveData[2] + moveData[3]);
				}
				//				logDatos.addLog((float)Math.round(x*100000)/100000 + " " + (float)Math.round(y*100000)/100000 + " " + (float)Math.round(angulo*100000)/100000);
				Thread.sleep(100);		    	
			}
			//System.out.println("datos para leer " + data);
			//			logDatos.endLog();

		}catch(Exception e){

		}


	}

}


