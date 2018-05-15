package strategy.tile_engine.units;

import strategy.tile_engine.DamageType;
import strategy.tile_engine.Unit;
import strategy.tile_engine.UnitStats;

public class Mage extends Unit {

	public Mage(int x, int y) {
		super(x, y);
		this.name = "Mage";
		this.stats = new UnitStats(BaseStats.mage);
		this.damageType = DamageType.MAGICAL;
		this.texture = "testunit";
		this.currentHitpoints = this.stats.hitpoints;
	}
}
