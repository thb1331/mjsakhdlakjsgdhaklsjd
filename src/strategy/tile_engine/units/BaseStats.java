package strategy.tile_engine.units;

import strategy.tile_engine.UnitStats;

public class BaseStats {
	public static UnitStats testUnit = new UnitStats(20, 11, 4, 6, 6, 1);
	public static UnitStats archer = new UnitStats(18, 14, 6, 4, 5, 2);
	public static UnitStats cavalry = new UnitStats(22, 12, 7, 2, 6, 1);
	public static UnitStats infantry = new UnitStats(20, 16, 6, 6, 5, 1);
	public static UnitStats mage = new UnitStats(18, 0, 5, 12, 5, 2);
	public static UnitStats cleric = new UnitStats(16, 0, 4, 8, 5, 2);
	
}
