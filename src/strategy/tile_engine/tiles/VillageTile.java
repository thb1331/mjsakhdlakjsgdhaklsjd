package strategy.tile_engine.tiles;

import java.util.InputMismatchException;
import java.util.List;

import strategy.Utils;
import strategy.tile_engine.Team;
import strategy.tile_engine.Tile;
import strategy.tile_engine.Unit;
import strategy.tile_engine.Villager;
import strategy.tile_engine.units.Archer;
import strategy.tile_engine.units.Cavalry;
import strategy.tile_engine.units.Cleric;
import strategy.tile_engine.units.Infantry;
import strategy.tile_engine.units.Mage;

public class VillageTile extends CapturableTile {
	
	public List<Villager> villagers;
	protected final int MAGIC_UNITS = 2;
	protected final int PHYSICAL_UNITS = 3;
	public boolean hasRecruited;
	@Override
	public String getName () {
		return "VillageTile";
	}
	public VillageTile (List<Villager> villagers, Team team) {
		this.villagers = villagers;
		this.texture = "test";
		this.team = team;
		this.hitpoints = 20;
		this.maxHitpoints = 20;
		this.captureSpeed = 10;
	}
	public Unit recruit(int x, int y, Team team) {
		if (team != this.getTeam()) {
			System.out.println("You don't own this village!");
			return null;
		}
		Unit u = null;
		System.out.println("Here are the available units:");
		int i = 1;
		for (Villager v : villagers) {
			System.out.printf("%d. Const: %d, Str: %d, Tough: %d, Int: %d\n", 
					i, v.constitution, v.strength, v.toughness, 
					v.intelligence);
			i++;
		}
		System.out.println("Select a unit or enter anything else to cancel");
		int n = 0;
		try {
			n = Utils.inputInt();
		} catch (InputMismatchException e) {
			System.out.println("Cancelling");
			return null;
		}
		if (n <= 0 || n > villagers.size()) {
			System.out.println("Cancelling");
			return null;
		}
		Villager v = villagers.get(n - 1);
		if (v.isMagic) {
			System.out.println("Enter 0 or 1 for mage or cleric");
		} else {
			System.out.println("Enter 0 1 or 2 for infantry, cavalry or archer");
		}
		try {
			n = Utils.inputInt();
		} catch (InputMismatchException e) {
			System.out.println("Cancelling");
			return null;
		}
		if (n < 0 || (v.isMagic && n >= MAGIC_UNITS) || 
				(!v.isMagic && n >= PHYSICAL_UNITS)) {
			System.out.println("Cancelling");
			return null;
		}
		if (v.isMagic) {
			if (n == 0) {
				u = new Mage(x, y);
			} else {
				u = new Cleric(x, y);
			}
		} else {
			if (n == 0) {
				u = new Infantry(x, y);
			} else if (n == 1) {
				u = new Cavalry(x, y);
			} else {
				u = new Archer(x, y);
			}
		}
		villagers.remove(v);
		u.exhausted = true;
		System.out.println("Unit successfully recruited");
		return u;
	}
	@Override
	public VillageTile getVillageTile () {
		return this;
	}
}
