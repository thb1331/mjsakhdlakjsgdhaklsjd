package strategy.tile_engine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import strategy.graphics.Camera;
import strategy.graphics.RenderQueue;
import strategy.graphics.Renderer;
import strategy.tile_engine.tiles.CapturableTile;
import strategy.tile_engine.tiles.TestTile;
import strategy.tile_engine.tiles.TileFactory;
import strategy.tile_engine.tiles.VillageTile;

public class Board {
	protected Tile[] tiles;
	protected int width;
	protected int height;
	protected List<Unit> units;
	public boolean isWon;
	public Board (int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[width*height];
		units = new ArrayList<Unit>();
		isWon = false;
	}
	public void addTiles (RenderQueue queue) {
		for (int i = 0; i < tiles.length; i++) {
			queue.addTile(tiles[i]);
		}
	}
	public void test () {
		System.out.println("Testing board");
		fill(new TestTile(0, 0).getName());
	}
	public void setTile (Tile tile) {
		tiles[tile.x + tile.y * width] = tile;
	}
	public void fill (String tileName) {
		TileFactory tileFactory = new TileFactory();
		if (tileFactory.makeTile(tileName, 0, 0) != null) {
			for (int i = 0; i < width * height; i ++) {
				tiles[i] = tileFactory.makeTile(tileName, i % width, i / width);
			}
		} else {
			System.err.println("No tile with name " + tileName + " found");
		}
	}
	public void addUnit (Unit u) {
		units.add(u);
	}
	public void refreshUnits (Team team) {
		for (Unit u : units.stream()
				.filter(unit -> unit.team == team)
				.collect(Collectors.toList())) {
			u.exhausted = false;
		}
	}
	public boolean isUnitAtPosition (int x, int y) {
		boolean is = false;
		for (Unit u : units) {
			if (u.x == x && u.y == y) {
				is = true;
				break;
			}
		}
		return is;
	}
	public int getWidth () {
		return width;
	}
	public int getHeight () {
		return height;
	}
	public Unit getUnitAtPosition (int x, int y) {
		for (Unit u : units) {
			if (u.x == x && u.y == y) {
				return u;
			}
		}
		return null;
	}
	public void addUnits (RenderQueue r) {
		for (Unit u : units) {
			r.addUnit(u);
		}
	}
	public List<Unit> getUnits () {
		return units;
	}
	public void renderTiles (Renderer renderer, Camera camera) {
		for (Tile t : tiles) {
			renderer.draw(t.texture, camera, t.getRealX(), t.getRealY());
		}
	}
	public void renderUnits (Renderer renderer, Camera camera) {
		for (Unit u : units) {
			renderer.draw(u.texture, camera, u.getRealX(), u.getRealY());
		}
	}
	public void removeUnit (Unit u) {
		if (units.contains(u)) {
			units.remove(u);
		}
		else {
			System.err.println("Unit not found in board");
		}
	}
	public Tile getTile (int x, int y) {
		return tiles[x + y*width];
	}
	public void refreshVillages (Team team) {
		for (Tile t : tiles) {
			if (t instanceof VillageTile) {
				VillageTile v = t.getVillageTile();
				if (v.getTeam() == team) {
					v.hasRecruited = false;
				}
			}
		}
	}
	public void doCapture (Team team) {
		for (Unit u : units) {
			if (getTile(u.x, u.y) instanceof CapturableTile) {
				getTile(u.x, u.y).getCapturableTile().capture(team);
			}
		}
	}
}
