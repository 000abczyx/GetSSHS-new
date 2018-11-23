package application;

public class Position {

	protected int x;
	protected int y;

	Position (){}
	Position(Position pos) {
		x = pos.getX();
		y = pos.getY();
	}

	Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setPos(Position pos) {
		this.x = pos.getX();
		this.y = pos.getY();
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Position getPos() {
		return this;
	}
	
	public static boolean positionsEqual(Position first, Position second) {
		return first.getX() == second.getX() && first.getY() == second.getY();
	}

}
