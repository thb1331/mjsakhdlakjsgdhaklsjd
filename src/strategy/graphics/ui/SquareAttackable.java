package strategy.graphics.ui;

import strategy.tile_engine.Square;

public class SquareAttackable extends SquareOverlay implements UIElement {
	//String texture = "squareattackable";
	public SquareAttackable(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	public SquareAttackable (Square s) {
		super(s);
	}

	@Override
	public boolean isCameraDependant() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getTexture() {
		// TODO Auto-generated method stub
		return "squareattackable";
	}

}
