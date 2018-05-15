package strategy.tile_engine.units;

import strategy.tile_engine.DamageType;
import strategy.tile_engine.Unit;
import strategy.tile_engine.UnitStats;

public class Cleric extends Unit {

	public Cleric(int x, int y) {
		super(x, y);
		this.name = "Cleric";
		this.stats = new UnitStats(BaseStats.cleric);
		this.damageType = DamageType.HEALING;
		this.texture = "testunit";
		this.currentHitpoints = this.stats.hitpoints;
	}
}
