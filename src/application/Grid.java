package application;

import java.util.ArrayList;

public class Grid {
	private int size;
	private ArrayList<ArrayList<Cell>> cells = new ArrayList<ArrayList<Cell>>();

	Grid(int size) {
		this.size = size;
		for (int x = 0; x < size; x++) {
			cells.add(new ArrayList<Cell>());
			for (int y = 0; y < size; y++) {
				cells.get(x).add(new Cell(x,y));
			}
		}
	}
	public ArrayList<ArrayList<Cell>> getCells(){
		return cells;
	}
	public Cell getCell(Position pos) {
		if(withinBounds(pos)) {
			return cells.get(pos.getX()).get(pos.getY());
		}
		else return null;
	}
	public Cell randomAvailableCell() {
		ArrayList<Cell> cells = availableCells();
		if (cells.size() > 0) {
			int length = (int) Math.floor((double) Math.random() * cells.size());
			return cells.get(length);
		} else
			return null;
	}

	public ArrayList<Cell> availableCells() {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		eachCell(new CellOperation() {
			@Override
			public void callback(int x, int y, Cell cell) {
				if(cell.empty())
				cells.add(cell);
			}
		});
		return cells;
	}

	public boolean cellsAvailable() {
		return (this.availableCells().size() > 0);
	}

	public boolean cellAvailable(Cell cell) {
		return !cellOccupied(cell);
	}

	public boolean cellOccupied(Cell cell) {
		if (cellContent(cell) == null)
			return false;
		else
			return true;
	}

	public Tile cellContent(Position cell) {
		if (withinBounds(cell)) {
			return this.getCell(cell).tile;
		} else {
			return null;
		}
	}
	public void insertTile(Tile tile) {
		getCell(tile).putTile(tile);
	}
	public void removeTile(Tile tile) {
		getCell(tile).putTile(null);
	}
	public void eachCell(CellOperation Oper) {
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				Oper.callback(x, y, this.cells.get(x).get(y));
			}
		}
	}

	public boolean withinBounds(Position pos) {
		return pos.getX() >= 0 && pos.getX() < size && pos.getY() >= 0 && pos.getY() < size;
	}
}