package brick.comm;

import java.util.StringTokenizer;

import lejos.robotics.navigation.Move;

public class MoveSerializable extends Move {

	public MoveSerializable(float distance, float angle, boolean isMoving) {
		super(distance, angle, isMoving);
	}
	
	public MoveSerializable(Move move) {
		this(move.getDistanceTraveled(), move.getAngleTurned(), move.isMoving());
	}

	public String toString(){
		return getDistanceTraveled() + "," + getAngleTurned() + "," + isMoving();
	}
	
	public static MoveSerializable fromString(String move){
		StringTokenizer tok = new StringTokenizer(move, ",");
		return fromString(tok);
	}

	public static MoveSerializable fromString(StringTokenizer stok) {
		return new MoveSerializable(
				Float.parseFloat(stok.nextToken()),
				Float.parseFloat(stok.nextToken()),
				Boolean.parseBoolean(stok.nextToken()));
	}

	
}
