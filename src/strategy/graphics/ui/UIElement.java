package strategy.graphics.ui;

import strategy.graphics.RenderQueue;

public interface UIElement {
	@Deprecated
	public void addToQueue (RenderQueue r);
	public boolean isCameraDependant ();
	public void show();
	public void hide();
}
