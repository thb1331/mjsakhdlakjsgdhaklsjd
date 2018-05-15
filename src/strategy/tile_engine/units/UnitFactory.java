package strategy.tile_engine.units;

import strategy.tile_engine.Unit;

public class UnitFactory {
	public Unit copyUnit (Unit u, int x, int y) {
		// returns a copy of all fields except for position
		String unitName = u.getName();
		Unit newUnit;
		if (unitName == "Test Unit") {
			newUnit = new TestUnit(x, y);
			
		}else {
			// catchall
			newUnit = new TestUnit(x, y);
		}
		newUnit.team = u.team;
		newUnit.currentHitpoints = u.currentHitpoints;
		return newUnit;
	}
}
