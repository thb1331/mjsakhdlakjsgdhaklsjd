package strategy.tile_engine;

import strategy.gamelogic.GameInit;
import strategy.tile_engine.units.UnitSquad;

public abstract class Unit extends Square{
	public UnitStats stats = new UnitStats();
	public int currentHitpoints;
	public Team team;
	public String texture; //change when multiple images per unit
	public String name;
	public DamageType damageType;
	public boolean isDead = false;
	public boolean exhausted = false;
	public float partialX = 0;
	public float partialY = 0;
	public int unitID;
	public Unit (int x, int y) {
		this.x = x;
		this.y = y;
		unitID = GameInit.getUnitID();
		//System.out.println(unitID);
	}
	public void move (int x, int y) {
		this.x = x;
		this.y = y;
	} 
	public float getRealX () {
		return (x + partialX) * 2;
	} 
	public float getRealY () {
		return (y + partialY) * 2;
	} 
	public void moveBy (int x, int y) {
		this.x += x;
		this.y += y;
	}
	public void damage (int damage, DamageType type) {
		switch (type) {
		case PHYSICAL:
			currentHitpoints = Math.max(currentHitpoints - Math.max(damage - stats.defense, 1), 0);
			break;
		case MAGICAL:
			currentHitpoints = Math.max(currentHitpoints - Math.max(damage - stats.magic, 1), 0);
			break;
		case HEALING:
			currentHitpoints = Math.min(currentHitpoints + damage, stats.hitpoints);
			break;
		}
		if (currentHitpoints == 0) {
			isDead = true;
		}
	}
	public void movePartial (float x, float y) {
		partialX = x;
		partialY = y;
	}
	public String getName() {
		return name;
	}
	public UnitSquad getUnitSquad () {
		return null;
	}
	public void query () {
		if (team == Team.RED) {
			System.out.println("Red " + getName());
		} else {
			System.out.println("Blue " + getName());
		}
		System.out.printf("Position: (%d, %d)\n", x, y);
		System.out.printf("HP: %d/%d\n", currentHitpoints, stats.hitpoints);
		System.out.printf("Atk: %d\n", stats.attack);
		System.out.printf("Def: %d\n", stats.defense);
		System.out.printf("Mag: %d\n", stats.magic);
		System.out.printf("Speed: %d\n", stats.movespeed);
		System.out.printf("Range: %d\n", stats.range);
		System.out.println("");
	}
}
