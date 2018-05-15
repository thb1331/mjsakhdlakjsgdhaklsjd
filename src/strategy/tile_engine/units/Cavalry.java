package strategy.tile_engine.units;

import strategy.tile_engine.DamageType;
import strategy.tile_engine.Unit;
import strategy.tile_engine.UnitStats;

public class Cavalry extends Unit{

	public Cavalry(int x, int y) {
		super(x, y);
		this.name = "Cavalry";
		this.stats = new UnitStats(BaseStats.cavalry);
		this.damageType = DamageType.PHYSICAL;
		this.texture = "testunit";
		this.currentHitpoints = this.stats.hitpoints;
	}
}
