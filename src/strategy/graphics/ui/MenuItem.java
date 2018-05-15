package strategy.graphics.ui;

import strategy.graphics.RenderQueue;
import strategy.graphics.Renderer;

public class MenuItem implements Selectable {
	
	protected UIFunction selectFunction;
	protected boolean isShowing;
	protected float x;
	protected float y;
	protected float scaleX;
	protected float scaleY;
	protected String baseTexture;
	protected String selectedTexture;
	protected String hoverTexture;
	protected String currentTexture;
	@Override
	public void addToQueue(RenderQueue r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCameraDependant() {
		return false;
	}

	@Override
	public void show() {
		isShowing = true;
	}

	@Override
	public void hide() {
		isShowing = false;
	}

	@Override
	public void render(Renderer r) {
		r.draw(currentTexture, x, y, scaleX, scaleY);
		// TODO add text
	}

	@Override
	public void updateScale(int newWindowWidth, int newWindowHeight) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void setFunction(UIFunction function) {
		this.selectFunction = function;
	}

	@Override
	public void onHoverOn() {
		currentTexture = hoverTexture;
	}

	@Override
	public void onHoverOff() {
		currentTexture = baseTexture;
	}

	@Override
	public float getRealX() {
		return x;
	}

	@Override
	public float getRealY() {
		return y;
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onSelect() {
		selectFunction.function();
	}
	
	public MenuItem (float x, float y) {
		setPosition(x, y);
	}
	public MenuItem setText (String text) {
		return this;
	}
	public MenuItem setScale (int windowWidth, int windowHeight) {
		return this;
	}
	public MenuItem setTextPadding (float xPadding, float yPadding) {
		return this;
	}
	public MenuItem setDimensions (int width, int height) {
		return this;
	}
	public MenuItem setTextAlign (TextAlign alignment) {
		return this;
	}
	public MenuItem setScaleType (boolean scaleWithWindow) {
		return this;
	}
	public MenuItem setTextures (String base, String hover, String select) {
		baseTexture = base;
		hoverTexture = hover;
		selectedTexture = select;
		return this;
	}
} enum TextAlign {
	LEFT, MIDDLE, RIGHT
}
