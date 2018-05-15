package strategy.graphics.ui;

import strategy.graphics.Renderer;

public interface Selectable extends UIElement {
	public void render (Renderer r);
	public void updateScale (int newWindowWidth, int newWindowHeight);
	public void setPosition (float x, float y);
	public void setFunction (UIFunction function);
	public void onHoverOn ();
	public void onHoverOff ();
	public float getRealX ();
	public float getRealY ();
	public float getWidth ();
	public float getHeight ();
	public void onSelect ();
	
}
