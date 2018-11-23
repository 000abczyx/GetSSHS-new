package application;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Actuator {

	@FXML
	private Label score;
	@FXML
	private Label bestscore;
	@FXML
	private Label difference;
	@FXML
	private GridPane gridPane;
	@FXML
	private AnchorPane tileContainer;
	@FXML
	private AnchorPane scoreContainer;
	@FXML
	private VBox messageContainer;
	@FXML
	Label keepGoingButton;
	@FXML
	Label retryButton;
	
	private Text diffScore = new Text();
	private GameMeta metadata;
	Scene scene;
	private int scoreVal;
	public static HashMap<KeyCode, Integer> keyMap=new HashMap<KeyCode, Integer>();
	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	ParallelTransition totalTransition = new ParallelTransition();

	public Actuator() {
		keyMap.put(KeyCode.UP, 0);
		keyMap.put(KeyCode.RIGHT, 1);
		keyMap.put(KeyCode.DOWN, 2);
		keyMap.put(KeyCode.LEFT, 3);
		keyMap.put(KeyCode.W, 0);
		keyMap.put(KeyCode.D, 1);
		keyMap.put(KeyCode.S, 2);
		keyMap.put(KeyCode.A, 3);
	}

	public void actuate(Grid grid, GameMeta metadata) {
		this.metadata = metadata;
		clearContainer(tileContainer);               

		for (ArrayList<Cell> column : grid.getCells()) {
			for (Cell cell : column) {
				if (cell != null) {
					addTile(grid.cellContent(cell));
				}
			}
		}
		updateScore(metadata.score);
		updateBestScore(metadata.bestScore);
		
		if(metadata.terminated) {
			if(metadata.over) {
				message(false);  // U 105g
			}else if(metadata.won) {
				message(true);
			}
		}
		
		totalTransition.play();
	}

	public void continueGame() {
		clearMessage();
	}

	public void clearContainer(Pane tileContainer) {
		tileContainer.getChildren().clear();
	}

	public void updateScore(int score) {
		int difference = score - this.scoreVal;
		this.scoreVal = score;
		this.score.setText("SCORE\n" + score);
		if (difference > 0) {
			diffScore = new Text();
			diffScore.setText("+" + difference);
			diffScore.setLayoutX(246);
			diffScore.setLayoutY(89);
			TranslateTransition translation = new TranslateTransition(Duration.millis(1000), diffScore);
			translation.setByY(50);
			FadeTransition fade = new FadeTransition(Duration.millis(1000), diffScore);
			fade.setFromValue(0.8);
			fade.setToValue(0);
			fade.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					scoreContainer.getChildren().clear();
				}
			});
		//	scoreContainer.getChildren().add(diffScore);
		//	this.totalTransition.getChildren().addAll(fade, translation);

		}
	}

	public void updateBestScore(int bestScore) {
		this.bestscore.setText("BEST\n" + bestScore);
	}

	private void addTile(Tile tile) {
		if (tile == null) {
			return;
		} else {
			Position position = tile.previousPosition != null ? tile.previousPosition : tile.getPos();

			CustomTile wrapper = new CustomTile(80.0, 80.0);
			setPosition(wrapper, position);
			wrapper.setValue(tile.getValue());
			if (tile.getValue() > 2048)
				wrapper.setSuper(true);
			
			if (tile.previousPosition != null) {
				Position prev = tile.previousPosition;
				setPosition(wrapper, prev);
				TranslateTransition move = new TranslateTransition(Duration.millis(100), wrapper);
				move.setByX(toLayout(tile)[0]-toLayout(prev)[0]);
				move.setByY(toLayout(tile)[1]-toLayout(prev)[1]);
				
				totalTransition.getChildren().add(move);
				
			} else if (tile.mergedFrom != null) {
				
				wrapper.setMerged(true);
				delayAppearance(wrapper);
				for (Tile var : tile.mergedFrom) {
					addTile(var);
				}
			} else {
				wrapper.setNew(true);
				delayAppearance(wrapper);
			}
			showTile(wrapper);
			tileContainer.getChildren().add(wrapper);
		}
	}

	private void delayAppearance(CustomTile wrapper) {
		wrapper.setVisible(false);
		PauseTransition pause  = new PauseTransition(Duration.millis(100));
		pause.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				wrapper.setVisible(true);
			}
		});

		totalTransition.getChildren().add(pause);
	}

	public void message(boolean won) {
		String message = won ? "Game Won!" : "Game Over!";
		HBox hbox = (HBox) messageContainer.getChildren().get(1);
		((Text) messageContainer.getChildren().get(0)).setText(message);
		if (won) {
			hbox.getChildren().add(keepGoingButton);
		} else {
			hbox.getChildren().remove(keepGoingButton);
		}
		messageContainer.setVisible(true);
	}

	public void clearMessage() {
		messageContainer.setVisible(false);
	}

	private void setPosition(CustomTile wrapper, Position pos) {
		double[] layout = toLayout(pos);
		wrapper.setLayoutX(layout[0]);
		wrapper.setLayoutY(layout[1]);
	}
	public double[] toLayout(Position pos) {
		return(new double[] {pos.getX() * 90 + 10,pos.getY() * 90 + 10});
	}
	private void showTile(CustomTile tile) {
		HashMap<Integer, String> colorMap = new HashMap<Integer, String>();
		colorMap.put(2,"BISQUE");
		colorMap.put(4,"BURLYWOOD");
		colorMap.put(8,"TAN");
		colorMap.put(16,"CORAL");
		colorMap.put(32,"TOMATO");
		colorMap.put(64,"FIREBRICK");
		Rectangle inner = (Rectangle)tile.getChildren().get(0);
		String fill = tile.getValue() < 128 ? colorMap.get(tile.getValue()) : "YELLOW";
		inner.setFill(Paint.valueOf(fill));
		Text value = new Text(""+tile.getValue());
		tile.getChildren().add(value);
		
	}
	
	
	/*
	 * InputManager
	 */
	
	
}

