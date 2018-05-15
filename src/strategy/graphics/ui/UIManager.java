package strategy.graphics.ui;

import java.util.LinkedList;
import java.util.List;

import strategy.graphics.Camera;
import strategy.graphics.RenderQueue;
import strategy.graphics.Renderer;
import strategy.tile_engine.Square;

public class UIManager {
	public List<SquareOverlay> movable;
	public UIManager () {
		movable = new LinkedList<SquareOverlay>();
	}
	public void addMovableSquares(List<Square> squares) {
		// adds squares to the list of moveable squares
		//System.out.println("adding movable squares!");
		for (Square s : squares) {
			//System.out.println("adding square");
			movable.add(new SquareMovable(s));
		}
	}
	public void addAttackableSquares (List<Square> squares) {
		for (Square s : squares) {
			movable.add(new SquareAttackable(s));
		}
	}
	public void addMovableSquaresToQueue (RenderQueue r) {
		// adds moveable squares to queue
		for (SquareOverlay sm : movable) {
			//System.out.println("Adding square to queue");
			//System.out.printf("(%d, %d)\n", sm.x, sm.y);
			sm.addToQueue(r);
		}
		
	}
	public void clearMovableSquares() {
		movable.clear();
	}
	public void renderMovable (Renderer renderer, Camera camera) {
		for (SquareOverlay so : movable) {
			renderer.draw(so.getTexture(), camera, so.getRealX(), so.getRealY());
		}
	}
}
