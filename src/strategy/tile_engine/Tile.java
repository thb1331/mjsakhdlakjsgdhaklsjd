package strategy.tile_engine;

import strategy.tile_engine.tiles.CapturableTile;
import strategy.tile_engine.tiles.VillageTile;

public abstract class Tile extends Square{
	public String texture;
	
	public abstract String getName ();
	public VillageTile getVillageTile () {
		return null;
	}
	public CapturableTile getCapturableTile () {
		return null;
	}
}
