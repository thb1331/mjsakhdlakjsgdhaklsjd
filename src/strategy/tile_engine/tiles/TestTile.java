package strategy.tile_engine.tiles;

import strategy.tile_engine.Tile;

public class TestTile extends Tile{
	public TestTile (int x, int y) {
		this.x = x;
		this.y = y;
		slow = 0;
		texture = "test";
	}
	public String getName () {
		return "TestTile";
	}
}
