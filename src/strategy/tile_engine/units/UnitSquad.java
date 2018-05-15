package strategy.tile_engine.units;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import strategy.tile_engine.DamageType;
import strategy.tile_engine.Team;
import strategy.tile_engine.Unit;
import strategy.tile_engine.UnitStats;

public class UnitSquad extends Unit{
	protected List<Unit> units;
	protected List<Integer> unitIDs;
	public UnitSquad (int x, int y) {
		super(x, y);
		units = new ArrayList<Unit>();
		unitIDs = new ArrayList<Integer>();
		this.name = "Unit Squad";
		this.texture = "temp unitsquad";
		this.stats = new UnitStats();
		this.stats.range = 1;
		this.stats.movespeed = 0;
		this.isDead = false;
	}
	public void addUnits (List<Unit> units) {
		this.units.addAll(units);
		for (Unit u : units) {
			unitIDs.add(u.unitID);
		}
		updateMovespeed();
	}
	public void addUnit (Unit unit) {
		units.add(unit);
		unitIDs.add(unit.unitID);
		updateMovespeed();
	}
	public List<Unit> getUnits () {
		return units;
	}
	public void updateUnits (List<Unit> units) {
		this.units.clear();
		List<Unit> includedUnits = new LinkedList<Unit>();
		for (Unit u : units) {
			if (unitIDs.contains(u.unitID)) {
				includedUnits.add(u);
			}
		}
		addUnits(includedUnits);
		if (includedUnits.size() == 0) {
			isDead = true;
		}
	}
	public void heal (int healing) {
		for (Unit u : units) {
			u.damage(healing, DamageType.HEALING);
		}
	}
	public void updateMovespeed () {
		int minSpeed = -1;
		for (Unit u : units) {
			if (u.stats.movespeed < minSpeed || minSpeed == -1) {
				minSpeed = u.stats.movespeed;
			}
		}
		stats.movespeed = minSpeed;
	}
	@Override
	public UnitSquad getUnitSquad() {
		return this;
	}
	@Override
	public void query () {
		if (team == Team.BLUE) {
			System.out.println("Blue Unit Squad");
		} else if (team == Team.RED){
			System.out.println("Red Unit Squad");
		}
		System.out.printf("Speed: %d\n", stats.movespeed);
		System.out.println("Contained units:");
		for (Unit u : units) {
			u.query();
		}
		System.out.println("End Unit Squad\n");
	}
}
