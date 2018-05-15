package strategy.tile_engine.tiles;

import strategy.tile_engine.Tile;

public class GrassTile extends Tile{
	public GrassTile (int x, int y) {
		this.x = x;
		this.y = y;
		slow = 0;
		texture = "grass";
	}
	public String getName() {
		return "GrassTile";
	}
	
}
