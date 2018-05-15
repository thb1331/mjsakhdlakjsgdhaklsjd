package strategy.tile_engine;

public class Square {
	public int x;
	public int y;
	public int slow;
	public float getRealX () {
		return x * 2.0f;
	} public float getRealY () {
		return y * 2.0f;
	}
}
