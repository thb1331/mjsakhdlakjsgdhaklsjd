package strategy.tile_engine.units;

import strategy.tile_engine.DamageType;
import strategy.tile_engine.Unit;
import strategy.tile_engine.UnitStats;

public class Archer extends Unit {

	public Archer(int x, int y) {
		super(x, y);
		this.name = "Archer";
		this.stats = new UnitStats(BaseStats.archer);
		this.damageType = DamageType.PHYSICAL;
		this.texture = "testunit";
		this.currentHitpoints = this.stats.hitpoints;
	}
}
