package main;
import java.awt.Button;
import java.io.BufferedWriter;

import lejos.nxt.LCD;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.WayPoint;


public class PathCTest {

	/***
	 * Inicializa un robot y realiza una secuencia de tareas que implican planificación y navegación.
	 * @param args
	 */
	public static void main(String[] args)
	{
		Robot robot = new Robot();
		LCD.drawString("Robot inicializado", 0, 0);
		
		tarea1(robot);
		
		while(!lejos.nxt.Button.ENTER.isPressed());
		
		tarea2(robot);
		
		while(!lejos.nxt.Button.ENTER.isPressed());
		
		tarea3(robot);

		while(!lejos.nxt.Button.ENTER.isPressed());
		
		tarea4(robot);
		
		System.exit(0);
	}
	
	/**
	 * Ir a un punto que no hay lineas entre medio
	 * @param robot
	 */
	private static void tarea1(Robot robot){
		robot.getPathController().getPoseProvider().setPose(
				new Pose((float) (300+Robot.TRACK_WIDTH),
						(float) Robot.TRACK_WIDTH, 0));
		robot.getPathController().goTo(new WayPoint(
				300+Robot.TRACK_WIDTH, 500 + Robot.TRACK_WIDTH), false);
	}
	
	/**
	 * Ir a un punto que no hay lineas entre medio
	 * @param robot
	 */
	private static void tarea2(Robot robot){
		robot.getPathController().getPoseProvider().setPose(
				new Pose((float) (300+Robot.TRACK_WIDTH),
						(float) Robot.TRACK_WIDTH, 0));
//		robot.getPathController().goTo(new WayPoint(
//				300+Robot.TRACK_WIDTH, 500 + Robot.TRACK_WIDTH), false);
		robot.getPathController().goTo(new WayPoint(
				1500 - Robot.TRACK_WIDTH,2400 - Robot.TRACK_WIDTH), false);
	}
	
	/**
	 *  Ir a un punto que hay un agua entre medio
	 * @param robot
	 */
	private  static void tarea3(Robot robot){
		robot.getPathController().getPoseProvider().setPose(
				new Pose((float) (300+Robot.TRACK_WIDTH),
						(float) (700 + Robot.TRACK_WIDTH), 0));
		robot.getPathController().goTo(
				new WayPoint((float) (1200+Robot.TRACK_WIDTH),
						(float) (700 + Robot.TRACK_WIDTH)), false);
	}
	
	/**
	 *  Ir a un punto que hay un agua entre medio
	 * @param robot
	 */
	private  static void tarea4(Robot robot){
		robot.getPathController().getPoseProvider().setPose(
				new Pose((float) (300+Robot.TRACK_WIDTH),
						(float) (813), 0));
		robot.getPathController().goTo(
				new WayPoint((float) (1200+Robot.TRACK_WIDTH),
						(float) (700 + Robot.TRACK_WIDTH)), false);
	}
	
	
}
