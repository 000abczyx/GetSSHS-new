package application;

import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;

import javafx.event.EventHandler;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;

public class GameManager {
	private static int size = 4;
	private int score;
	private boolean over, won, keepPlaying;
	private int startTiles = 2;
	Grid grid;
	InputManager inputManager = new InputManager();
	ScoreManager scoreManager = new ScoreManager();
	Actuator actuator;

	GameManager(Actuator actuator) {
		this.actuator = actuator;

		actuator.keepGoingButton.setOnMousePressed(new EventHandler<InputEvent>() {
			@Override
			public void handle(InputEvent arg0) {
				// cancel events??
				keepPlaying = true;
				actuator.continueGame();
			}
		});
		actuator.retryButton.setOnMousePressed(new EventHandler<InputEvent>() {
			@Override
			public void handle(InputEvent arg0) {
				actuator.continueGame();
				setup();
			}
		});

		actuator.getScene().setOnKeyReleased(new EventHandler<InputEvent>() {
			@Override
			public void handle(InputEvent event) {
				int mapped = -1;
				KeyEvent typed = (KeyEvent) event;
				boolean modifiers = typed.isAltDown() || typed.isControlDown() || typed.isMetaDown()
						|| typed.isShiftDown();
				Integer value = Actuator.keyMap.get(typed.getCode());
				if (value != null) {
					mapped = value.intValue();
				}
				if (!modifiers) {
					if (mapped > -1) {
						move(mapped);
					}
				}
				// esc ??
			}

		});

		setup();
	}

	private void setup() {
		grid = new Grid(size);

		score = 0;
		over = false;
		won = false;
		keepPlaying = false;

		addStartTiles();

		actuate();
	}

	public void restart() {
		actuator.continueGame();
		setup();
	}

	public void keepPlaying() {
		keepPlaying = true;
		actuator.continueGame();
	}

	private boolean isGameTerminated() {
		if (over || (won && !keepPlaying)) {
			return true;
		} else {
			return false;
		}
	}

	private void addStartTiles() {
		for (int i = 0; i < startTiles; i++) {
			addRandomTile();
		}
	}

	public void addRandomTile() {
		Tile tile;
		if (grid.cellsAvailable()) {
			int value = Math.random() < 0.9 ? 2 : 4;
			Position pos = this.grid.randomAvailableCell();
			if (pos != null) {
				tile = new Tile(pos, value);
				grid.insertTile(tile);
			}
		}
	}

	public void actuate() {
		if (scoreManager.get() < score) {
			scoreManager.set(score);
		}
		actuator.actuate(grid, new GameMeta(score, scoreManager.get(), over, won, isGameTerminated()));
	}

	public void prepareTiles() {
		grid.eachCell(new CellOperation() {

			@Override
			public void callback(int x, int y, Cell cell) {
				if (!cell.empty()) {
					cell.tile.mergedFrom = null;
					cell.tile.savePosition();
				}
			}
		});
	}

	public void moveTile(Tile tile, Cell cell) {
		grid.getCell(tile).putTile(null);
		grid.getCell(cell).putTile(tile);
		tile.updatePosition(cell);
	}

	public void move(int direction) {
		if (isGameTerminated())
			return;
		Cell cell;
		Tile tile;
		Position vector = getVector(direction);
		int[][] traversals = buildTraversals(vector);
		boolean moved = false;

		this.prepareTiles();

		for (int x : traversals[0]) {
			for (int y : traversals[1]) {
				cell = new Cell(x, y);
				tile = grid.cellContent(cell);

				if (tile != null) {
					Cell[] positions = findFarthestPosition(cell, vector);
					Tile next = grid.cellContent(positions[1]); // next position

					// only one merger per row traversal?
					if (next != null && next.getValue() == tile.getValue() && next.mergedFrom == null) {
						Tile merged = new Tile(positions[1], tile.getValue() * 2); // next position
						merged.mergedFrom = new Tile[] { tile, next };

						grid.insertTile(merged);
						grid.removeTile(tile);

						// converge the two tiles' positions
						tile.updatePosition(positions[1]);

						// Update the score
						score += merged.getValue();

						// The mighty 2048 tile
						if (merged.getValue() == 2048)
							won = true;
					} else {
						moveTile(tile, positions[0]);// farthest(previous) position
					}

					if (!Position.positionsEqual(cell, tile)) {
						moved = true; // the tile moved from its original position
					}
				}
			}
		}
	
		if (moved) {
			addRandomTile();

			if (!movesAvailable()) {
				this.over = true; // Game Over!
			}

			this.actuate();
		}
	}

	private static Position getVector(int direction) {
		HashMap<Integer, Position> map = new HashMap<Integer, Position>();
		map.put(0, new Position(0, -1));
		map.put(1, new Position(1, 0));
		map.put(2, new Position(0, 1));
		map.put(3, new Position(-1, 0));

		return map.get(direction);
	}

	private static int[][] buildTraversals(Position vector) {
		int[][] traversals = { new int[size], new int[size] };
		for (int pos = 0; pos < size; pos++) {
			traversals[0][pos] = pos;
			traversals[1][pos] = pos;
		}
		if (vector.getX() == 1)
			ArrayUtils.reverse(traversals[0]);
		if (vector.getY() == 1)
			ArrayUtils.reverse(traversals[1]);

		return traversals;
	}

	public Cell[] findFarthestPosition(Cell cell, Position vector) {
		Cell previous;
		do {
			previous = cell;
			cell = new Cell(previous.getX() + vector.getX(), previous.getY() + vector.getY());
		} while (grid.withinBounds(cell) && grid.cellAvailable(cell));

		return new Cell[] { previous, cell };
	}

	private boolean movesAvailable() {
		return grid.cellsAvailable() || tileMatchesAvailable();
	}

	private boolean tileMatchesAvailable() {
		Tile tile;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				tile = grid.cellContent(new Position(x, y));

				if (tile != null) {
					for (int direction = 0; direction < 4; direction++) {
						Position vector = getVector(direction);
						Tile other = grid.cellContent(new Position(x + vector.getX(), y + vector.getY()));

						if (other != null && other.getValue() == tile.getValue()) {
							return true; // These two tiles can be merged
						}
					}
				}
			}
		}

		return false;
	}

}
