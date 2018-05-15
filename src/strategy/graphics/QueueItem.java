package strategy.graphics;

public class QueueItem {
	// defines a render queue item which can be passed around
	// rather than messy texture and float[] combos
	public String texture;
	public float x;
	public float y;
	public float z;
	public QueueItem (String texture, float x, float y, float z) {
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	@Override
	public void finalize () {
		//System.out.println("Garbage collecting");
	}
}
