package strategy.tile_engine.tiles;

import strategy.tile_engine.Team;
import strategy.tile_engine.Tile;

public abstract class CapturableTile extends Tile {
	Team team;
	int hitpoints;
	int captureSpeed;
	int maxHitpoints;
	public Team getTeam () {
		return team;
	}
	public void capture (Team team) {
		if (this.team == team) {
			return;
		}
		hitpoints -= captureSpeed;
		System.out.printf("%s at (%d, %d) down to %d HP\n", 
				this.getName(), x, y, hitpoints);
		if (hitpoints <= 0) {
			this.team = team;
			resetCapture();
			String teamStr = "";
			if (team == Team.RED) {
				teamStr = "Red";
			} else {
				teamStr = "Blue";
			}
			System.out.printf("%s team has taken the village!\n", teamStr);
		}
	}
	public int getHP () {
		return hitpoints;
	}
	public void resetCapture () {
		hitpoints = maxHitpoints;
	}
	@Override
	public CapturableTile getCapturableTile () {
		return this;
	}
}
