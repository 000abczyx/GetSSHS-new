package application;

public class Tile extends Position {
	private int value;
	
	Position previousPosition = null;
	Tile[] mergedFrom = null;
	
	Tile(Position pos , int value){
		this.value =  value;
		this.setPos(pos);
	}
	
	public void savePosition() {
		previousPosition = new Position(this); 
	}
	public void updatePosition(Position pos) {
		this.setX(pos.getX());
		this.setY(pos.getY());
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
