package strategy.gamelogic;

import java.util.ArrayList;
import java.util.List;

import strategy.tile_engine.Board;
import strategy.tile_engine.Unit;
import strategy.tile_engine.tiles.GrassTile;
import strategy.tile_engine.units.UnitSquad;

public class Battle {
	public List<UnitSquad> squads;
	public Battle () {
		squads = new ArrayList<UnitSquad>();
	}
	public void addSquad (UnitSquad u) {
		squads.add(u);
	}
	public Board getMicroBoard (int microWidth, int microHeight) {
		Board board = new Board(microWidth, microHeight);
		board.fill(new GrassTile(0, 0).getName());
		for (UnitSquad squad : squads) {
			for (Unit u : squad.getUnits()) {
				board.addUnit(u);
			}
		}
		return board;
	}
}
