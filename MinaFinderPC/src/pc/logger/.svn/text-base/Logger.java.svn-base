package pc.logger;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Logger {
	private BufferedWriter out;
	private String filename;
	
	

	public Logger(String filename) {
		try{

			this.filename = filename;
			FileWriter fstream = new FileWriter(this.filename);
			this.out = new BufferedWriter (fstream);


//			System.out.println("logging ...");



		}catch(IOException e){
//			System.out.println("cagata: " + e.getMessage());
		}
	}

	public void addLog(String datos){
		try{
			System.out.println("Logger: "+datos);
			out.write(datos);
			out.newLine();
			out.flush();
		}catch(IOException e){
			System.out.println("cagata: " + e.getMessage());
		}
	}
	
	public void endLog(){
		try {
			out.close();
			System.out.println("logging done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
