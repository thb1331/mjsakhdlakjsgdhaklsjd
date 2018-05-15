package strategy.graphics.ui;

import strategy.graphics.QueueItem;
import strategy.graphics.RenderQueue;
import strategy.tile_engine.Square;

public abstract class SquareOverlay {
	int x;
	int y;
	boolean isShowing;
	String texture;
	public SquareOverlay (Square s) {
		this.x = s.x;
		this.y = s.y;
		isShowing = true;
	}
	public SquareOverlay (int x, int y) {
		this.x = x;
		this.y = y;
		isShowing = true;
	}
	public void show () {
		isShowing = true;
	}
	public void hide () {
		isShowing = false;
	}
	public abstract String getTexture();
	public void addToQueue (RenderQueue r) {
		r.addElement(getTexture(), x * 2, y *2 , 1.0f);
	}
	public float getRealX() {
		return x * 2.0f;
	}
	public float getRealY() {
		return y * 2.0f;
	}
}
