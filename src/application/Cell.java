package application;

public class Cell extends Position {
	
	Tile tile;
	
	public static final Cell emptyCell = null;
	Cell(int x, int y){
		super(x,y);
		tile = null;
	}
	public void putTile(Tile tile) {
		this.tile = tile;
	}
	public boolean empty() {
		if(tile == null)
			return true;
		else
			return false;
	}
}
