package strategy.graphics;

import java.util.LinkedList;
import java.util.List;

import strategy.tile_engine.Tile;
import strategy.tile_engine.Unit;

public class RenderQueue {
	// The textureQueue of images which are to be rendered in a frame
	protected List<QueueItem> textureQueue = new LinkedList<QueueItem>();
	protected List<QueueItem> unitQueue = new LinkedList<QueueItem>();
	Runtime runtime = Runtime.getRuntime();
	public void addElement (QueueItem q) {
		textureQueue.add(q);
	} 
	public void render (Renderer r, Camera c) {
		// renders all the items and takes them off the list
		//System.out.println("Before memory: " + runtime.freeMemory());
		renderTextureQueue(r, c);
		renderUnitQueue(r, c);
		System.out.println(runtime.freeMemory());
		//System.gc();
	} 
	public void renderQueue (Renderer r, Camera c, List<QueueItem> queue) {
		// takes all items off stack and renders them
		int size = queue.size();
		while (size > 0) {
			QueueItem item = queue.remove(size - 1);
			r.draw(item.texture, c, item.x, item.y);
			size --;
		}
	}
	public void renderTextureQueue (Renderer r, Camera c) {
		for (QueueItem q : textureQueue) {
			r.draw(q.texture, c, q.x, q.y);
			q = null;
		}
		textureQueue.clear();
	}
	public void renderUnitQueue(Renderer r, Camera c) {
		for (QueueItem q : unitQueue) {
			r.draw(q.texture, c, q.x, q.y);
			q = null;
		}
		unitQueue.clear();
	}
	public void addTile (Tile t) {
		textureQueue.add(new QueueItem(t.texture, t.getRealX(), t.getRealY(), 0.0f));
	}
	public void addUnit (Unit u) {
		// will need to change
		unitQueue.add(new QueueItem(u.texture, u.getRealX(), u.getRealY(), -1f));
	}
	public void addElement (String texture, float x, float y, float z) {
		textureQueue.add(new QueueItem(texture, x, y, z));
	}
}
