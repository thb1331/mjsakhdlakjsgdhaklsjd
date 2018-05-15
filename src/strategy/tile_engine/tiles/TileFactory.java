package strategy.tile_engine.tiles;

import strategy.tile_engine.Tile;

public class TileFactory {
	public Tile makeTile (String tileName, int x, int y) {
		if (tileName.equalsIgnoreCase("TestTile")) {
			return new TestTile(x, y);
		} else if (tileName.equalsIgnoreCase("GrassTile")) {
			return new GrassTile(x, y);
		} else {
			return null;
		}
	}
}
