package strategy.tile_engine;

public class UnitStats {
	public int hitpoints;
	public int attack;
	public int defense;
	public int magic;
	public int movespeed;
	public int range;
	public UnitStats() {
		
	}
	public UnitStats(int hp, int a, int d, int m, int ms, int r) {
		hitpoints = hp;
		attack = a;
		defense = d;
		magic = m;
		movespeed = ms;
		range = r;
	}
	public UnitStats(UnitStats other) {
		this.hitpoints = other.hitpoints;
		this.attack = other.attack;
		this.defense = other.defense;
		this.magic = other.magic;
		this.movespeed = other.movespeed;
		this.range = other.range;
	}
}
