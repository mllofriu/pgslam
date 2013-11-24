package pc.cubrimiento;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.ImageTerminal;

public class EvolCubrimiento {

	private static final int maxLines = 11905;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		JavaPlot plotter = new JavaPlot();
		plotter.setTitle("Evolución del Cubrimiento");
		PlotStyle myPlotStyle = new PlotStyle();
		myPlotStyle.setStyle(Style.LINES);
		myPlotStyle.setLineWidth(1);


		int[][][] distsSlam = new int[5][maxLines][2];
		int[][][] distsOdom = new int[5][maxLines][2];

		File carpeta = new File(".");

		int i = 0;
		for(File f : carpeta.listFiles(new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				return arg0.getName().endsWith(".cub") && arg0.getName().startsWith("slam");
			}
		})){
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			int j = 0;
			int cubri = 0;
			while(line != null){
				cubri = Integer.parseInt(line);
				distsSlam[i][j][0] = j;
				distsSlam[i][j][1] = cubri;
				line = br.readLine();
				j++;
			}

			for(int k = j; k < distsSlam[i].length; k++){
				distsSlam[i][k][0] = k;
				distsSlam[i][k][1] = cubri;
			}

			i++;
		}

		i = 0;
		for(File f : carpeta.listFiles(new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				return arg0.getName().endsWith(".cub") && arg0.getName().startsWith("odom");
			}
		})){
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			int j = 0;
			int cubri = 0;
			while(line != null){
				cubri = Integer.parseInt(line);
				distsOdom[i][j][0] = j;
				distsOdom[i][j][1] = cubri;
				line = br.readLine();
				j++;
			}

			for(int k = j; k < distsOdom[i].length; k++){
				distsOdom[i][k][0] = k;
				distsOdom[i][k][1] = cubri;
			}

			i++;
		}

		int[][] distsSlamSuma = new int[maxLines][2];
		int[][] distsOdomSuma = new int[maxLines][2];

		for(i = 0; i < maxLines; i++){
			distsSlamSuma[i][0] = i;
			distsSlamSuma[i][1] = 0;
			for(int j = 0; j < distsSlam.length; j++)
				distsSlamSuma[i][1] += distsSlam[j][i][1];

		}

		for(i = 0; i < maxLines; i++){
			distsOdomSuma[i][0] = i;
			distsOdomSuma[i][1] = 0;
			for(int j = 0; j < distsOdom.length; j++)
				distsOdomSuma[i][1] += distsOdom[j][i][1];
		}

		DataSetPlot slamSet = new DataSetPlot(distsSlamSuma);
		slamSet.setPlotStyle(myPlotStyle);
		slamSet.setTitle("Slam");
		plotter.addPlot(slamSet);

		DataSetPlot odomSet = new DataSetPlot(distsOdomSuma);
		odomSet.setPlotStyle(myPlotStyle);
		odomSet.setTitle("Odometría");
		plotter.addPlot(odomSet);

		ImageTerminal png = new ImageTerminal();
		File file = new File("plot.png");
		try {
			file.createNewFile();
			png.processOutput(new FileInputStream(file));
		} catch (FileNotFoundException ex) {
			System.err.print(ex);
		} catch (IOException ex) {
			System.err.print(ex);
		}
		plotter.setTerminal(png);

		//		i = 1;
		//		for(int[][] data : distsSlam){
		//			DataSetPlot set = new DataSetPlot(data);
		//			set.setPlotStyle(myPlotStyle);
		//			set.setTitle("Slam"+i++);
		//			plotter.addPlot(set);
		//		}
		//
		//		i = 1;
		//		for(int[][] data : distsOdom){
		//			DataSetPlot set = new DataSetPlot(data);
		//			set.setPlotStyle(myPlotStyle);
		//			set.setTitle("Odom"+i++);
		//			plotter.addPlot(set);
		//		}

		plotter.plot();
		
		try {
	        ImageIO.write(png.getImage(), "png", file);
	    } catch (IOException ex) {
	        System.err.print(ex);
	    }
	}
}