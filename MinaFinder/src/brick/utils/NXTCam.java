package brick.utils;


import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;
import lejos.nxt.Sound;
import lejos.util.Delay;

import java.awt.*;

/*
 * WARNING: THIS CLASS IS SHARED BETWEEN THE classes AND pccomms PROJECTS.
 * DO NOT EDIT THE VERSION IN pccomms AS IT WILL BE OVERWRITTEN WHEN THE PROJECT IS BUILT.
 */

/**
 * Mindsensors NXTCam.
 * www.mindsensors.com
 * 
 * Author Lawrie Griffiths
 * 
 */
public class NXTCam extends I2CSensor {
	byte[] buf = new byte[256];
	byte[] tmpBuf = new byte[32];
	private int seenSame;
	private int cantAnt;
	private int hashAnt;
	
	/**
	 * Used by sortBy() to choose sorting criteria based on size (ordered largest to smallest).
	 */
	public static final char SIZE = 'A';
	
	/**
	 * Used by sortBy() to choose sorting criteria based on color id (ordered 0 to 7).
	 */
	public static final char COLOR = 'U';
	
	/**
	 * Used by sortBy() to choose no sorting of detected objects.
	 */
	public static final char NO_SORTING = 'X';
	
	/**
	 * Used by setTrackingMode() to choose object tracking.
	 */
	public static final char OBJECT_TRACKING = 'B';
	
	/**
	 * Used by setTrackingMode() to choose line tracking.
	 */
	public static final char LINE_TRACKING = 'L';
	private static final int MAX_SEENSAME = 100;
	
	public NXTCam(I2CPort port, int address)
	{
		super(port, address, I2CPort.LEGO_MODE, TYPE_LOWSPEED_9V);
		
		seenSame = 0;
		hashAnt = -1;
		cantAnt = 0;
	}

	public NXTCam(I2CPort port)
	{
		this(port, DEFAULT_I2C_ADDRESS);
	}
	
	/**
	 * Get the number of objects being tracked
	 * 
	 * @return number of objects (0 - 8)
	 */
	public int getNumberOfObjects() {
//		int ret = getData(0x42, buf, 1);
//		if(ret != 0) return -1;
		return (0xFF & buf[0]);
	}
	
	/**
	 * Camera sorts objects it detects according to criteria, either color, size,
	 * or no sorting at all.
	 * @param sortType Use the class constants SIZE, COLOR, or NO_SORTING.
	 */
	public void sortBy(char sortType) {
		sendCommand(sortType);
	}
	
	/**
	 * 
	 * @param enable true to enable, false to disable
	 */
	public void enableTracking(boolean enable) {
		if(enable) sendCommand('E');
		else sendCommand('D');
	}
	
	/**
	 * Choose either object or line tracking mode.
	 * @param mode Use either OBJECT_TRACKING or LINE_TRACKING
	 */
	public void setTrackingMode(char mode) {
		sendCommand(mode);
	}
	
	/**
	 * Get the color number for a tracked object
	 * 
	 * @param id the object number (starting at zero)
	 * @return the color of the object (starting at zero)
	 */
	public int getObjectColor(int id) {
//		int ret = getData(0x43 + (id * 5), buf, 1);
//		if(ret != 0) return -1;
		return (0xFF & buf[1+id * 5]);
	}
	
	/**
	 * Returns the NXTCam firmware version.
	 * @return version number as a string
	 */
	public String getFirmwareVersion() {
		sendCommand('V');
		byte mem_loc = 0x42;
		// NXTCam V1.1 appears to need a delay here otherwise it doesn't refresh the memory 
		// at location 0x42 the first time. 50 ms is not enough, 100 ms works:
		try {  
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return fetchString(mem_loc, 12);
	}
	
	/**
	 * Get the rectangle containing a tracked object
	 * 
	 * @param id the object number (starting at zero)
	 * @return the rectangle
	 */
	public Rectangle getRectangle(int id) {
//		for(int i=0;i<4;i++) buf[i] = 0;
//		getData(0x44 + (id * 5), buf, 4);
		int offset = 2 + id * 5;
		return new Rectangle(buf[offset] & 0xFF, buf[offset+1] & 0xFF,
				(buf[offset+2] & 0xFF) - (buf[offset] & 0xFF),
				(buf[offset+3] & 0xFF) - (buf[offset+1] & 0xFF));
	}
	
	/**
	 * Send a single byte command represented by a letter
	 * @param cmd the letter that identifies the command
	 */
	public void sendCommand(char cmd) {
		sendData(0x41, (byte) cmd);
	}
	
	public boolean readAll(){
		
		for(int i=0;i<buf.length;i++) buf[i] = 0;
		while(getData(0x42, tmpBuf, 1) != 0)
			Delay.usDelay(10000);
		System.arraycopy(tmpBuf, 0, buf, 0, 1);
		int cant = buf[0] * 5;
		int transm = 0;
		while(cant > 0){
			int len = Math.min(cant, 32);
			while(getData(0x43 + transm, tmpBuf, len) != 0){
				Delay.usDelay(10000);
			}
			System.arraycopy(tmpBuf, 0, buf, 1 + transm, len);
			cant -= len;
			transm += len;
		}
		
		// Genero un hash de lo visto
		int numObjs = getNumberOfObjects();
		int hash = 0;
		for(int i = 0; i < numObjs; i++)
			hash += getObjectColor(i) + getRectangle(i).width + getRectangle(i).height + getRectangle(i).x + getRectangle(i).y;
		
		// Comparo con el anterior
		if(hash == hashAnt)
			seenSame++;
		else
			seenSame = 0;
		
		hashAnt = hash;
//		System.out.println("NXTCam: numObjs " + numObjs);
		// Da uno cuando se cuelga
//		if (numObjs == cantAnt)
//			seenSame++;
//		else
//			seenSame = 0;
		
//		cantAnt = numObjs;
		
		// Si vi demasiados iguales, retorno false
		if(seenSame == MAX_SEENSAME){
//			Sound.buzz();
			System.out.println("Seen Same");
			seenSame = 0;
			return false;
			
			// Reset cam
//			sendCommand('R');
//			Delay.msDelay(200);
//			sendCommand('A'); // sort objects by size
//			sendCommand('E'); // start tracking
		}
		
		return true;
//		System.out.println("Read " + buf[0] + " objs");
	}
}


