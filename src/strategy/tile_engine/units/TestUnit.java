package strategy.tile_engine.units;

import strategy.tile_engine.DamageType;
import strategy.tile_engine.Unit;
import strategy.tile_engine.UnitStats;

public class TestUnit extends Unit{
	public TestUnit (int x, int y) {
		super(x, y);
		this.texture = "testunit";
		this.stats = new UnitStats(BaseStats.testUnit);
		this.currentHitpoints = stats.hitpoints;
		this.damageType = DamageType.PHYSICAL;
		this.name = "Test Unit";
	}
}
