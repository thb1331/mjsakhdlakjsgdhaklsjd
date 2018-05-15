package strategy.tile_engine;

import strategy.tile_engine.units.Archer;
import strategy.tile_engine.units.Cavalry;
import strategy.tile_engine.units.Cleric;
import strategy.tile_engine.units.Infantry;
import strategy.tile_engine.units.Mage;
import strategy.tile_engine.units.TestUnit;

public class Villager {
	public int strength;
	public int toughness;
	public int intelligence;
	public int constitution;
	public boolean isMagic;
	public Villager (int s, int t, int i, int c, boolean isMagic) {
		strength = s;
		toughness = t;
		intelligence = i;
		constitution = c;
		this.isMagic = isMagic;
	}
	
	public Unit getUnit (int x, int y, String unitName) {
		Unit u;
		if (unitName.equalsIgnoreCase("Archer")) {
			u = new Archer(x, y);
		} else if (unitName.equalsIgnoreCase("Cavalry")) {
			u = new Cavalry(x, y);
		} else if (unitName.equalsIgnoreCase("Cleric")) {
			u = new Cleric(x, y);
		} else if (unitName.equalsIgnoreCase("Mage")) {
			u = new Mage(x, y);
		} else if (unitName.equalsIgnoreCase("Infantry")) {
			u = new Infantry(x, y);
		} else {
			u = new TestUnit(x, y);
		}
		
		u.stats.hitpoints += constitution;
		u.stats.attack += strength;
		u.stats.defense += toughness;
		u.stats.magic += intelligence;
		
		return u;
	}
}
