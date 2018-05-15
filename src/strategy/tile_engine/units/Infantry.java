package strategy.tile_engine.units;

import strategy.tile_engine.DamageType;
import strategy.tile_engine.Unit;
import strategy.tile_engine.UnitStats;

public class Infantry extends Unit {

	public Infantry(int x, int y) {
		super(x, y);
		this.name = "Infantry";
		this.stats = new UnitStats(BaseStats.infantry);
		this.damageType = DamageType.PHYSICAL;
		this.texture = "testunit";
		this.currentHitpoints = this.stats.hitpoints;
	}
}
