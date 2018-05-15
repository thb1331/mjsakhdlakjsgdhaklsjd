package strategy.graphics.ui;

import strategy.tile_engine.Square;

public class SquareMovable extends SquareOverlay implements UIElement{
	// shows a square that can be moved to
	// probably should extend Square
	// shows range limits
	//String texture = "squaremovable";
	public SquareMovable (Square s) {
		super (s);
	}
	public SquareMovable (int x, int y) {
		super (x, y);
	}
	public boolean isCameraDependant() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public String getTexture() {
		// TODO Auto-generated method stub
		return "squaremovable";
	}

}
