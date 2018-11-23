package application;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class CustomTile extends StackPane {

	private boolean isMerged = false;
	private boolean isNew = false;
	private boolean isSuper = false;
	private int value = 1;
	public boolean isMerged() {
		return isMerged;
	}

	public void setMerged(boolean isMerged) {
		this.isMerged = isMerged;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isSuper() {
		return isSuper;
	}

	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	private Position position;
	
	CustomTile(double width, double height){
		this.getChildren().add(new Rectangle(width, height));
	}

}
