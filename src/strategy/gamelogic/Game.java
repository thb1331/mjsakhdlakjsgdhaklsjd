package strategy.gamelogic;

import java.util.LinkedList;
import java.util.List;

import strategy.graphics.animations.UnitMoveAnimation;
import strategy.tile_engine.Board;
import strategy.tile_engine.Team;
import strategy.tile_engine.Tile;
import strategy.tile_engine.Unit;
import strategy.tile_engine.units.UnitSquad;

public class Game {
	Board microBoard;
	Board macroBoard;
	boolean isMacro;
	Team currentTurn;
	GameState state;
	Unit selectedUnit;
	int selectedX;
	int selectedY;
	boolean newSelect;
	boolean isEndMove;
	boolean needMovable;
	InputState inputState;
	Team microBoardTurn;
	List<Battle> battles;
	Battle currentBattle;
	int microX;
	int microY;
	public Game (int microX, int microY, int macroX, int macroY) {
		microBoard = new Board(microX, microY);
		macroBoard = new Board(macroX, macroY);
		state = GameState.INPUT;
		selectedUnit = null;
		selectedX = 0;
		selectedY = 0;
		newSelect = false;
		isEndMove = false;
		inputState = InputState.START;
		needMovable = true;
		currentTurn = Team.NONE;
		battles = new LinkedList<Battle>();
		this.microX = microX;
		this.microY = microY;
		currentBattle = null;
		isMacro = true;
	}
	public void addUnit (Unit u, boolean macro) {
		if (macro) {
			macroBoard.addUnit(u);
		} else {
			microBoard.addUnit(u);
		}
	}
	public void fillTiles (Tile t, boolean macro) {
		if (macro) {
			macroBoard.fill(t.getName());
		} else {
			microBoard.fill(t.getName());
		}
	}
	public Board getCurrentBoard () {
		if (isMacro) {
			return macroBoard;
		} else {
			return microBoard;
		}
	}
	public Team getCurrentTeam () {
		if (isMacro) {
			return currentTurn;
		} else {
			return microBoardTurn;
		}
	}
	public boolean isTurnOver () {
		Board testBoard = getCurrentBoard();
		boolean availableUnits = false;
		for (Unit u : testBoard.getUnits()) {
			if (isMacro) {
				if (u.team == currentTurn) {
					availableUnits = true;
				}
			} else {
				if (u.team == microBoardTurn) {
					availableUnits = true;
				}
			}
		}
		return availableUnits;
	}
	public void printNewTurn (Team team) {
		if (team == Team.BLUE) {
			System.out.println("Blue player's turn");
		} else if (team == Team.RED) {
			System.out.println("Red player's turn");
		}
	}
	public void nextTurn () {
		if (isMacro) {
			if (battles.size() > 0) {
				Battle b = battles.remove(0);
				microBoard = b.getMicroBoard(microX, microY);
				currentBattle = b;
				microBoardTurn = currentTurn;
				System.out.println("New battle");
				printNewTurn(microBoardTurn);
				isMacro = false;
			} else {
				swapTurn();
			}
		} else {
			swapTurn();
		}
	}
	public void addBattle (Battle b) {
		battles.add(b);
	}
	public void swapTurn () {
		if (isMacro) {
			macroBoard.doCapture(currentTurn);
			if (currentTurn == Team.BLUE) {
				currentTurn = Team.RED;
			} else {
				currentTurn = Team.BLUE;
			}
			macroBoard.refreshUnits(currentTurn);
			macroBoard.refreshVillages(currentTurn);
		} else {
			if (microBoardTurn == Team.BLUE) {
				microBoardTurn = Team.RED;
			} else {
				microBoardTurn = Team.BLUE;
			}
			microBoard.refreshUnits(microBoardTurn);
		}
		printNewTurn(getCurrentTurn());
	}
	public Team getCurrentTurn () {
		if (isMacro) {
			return currentTurn;
		} else {
			return microBoardTurn;
		}
	}
	public void finaliseBattle () {
		for (UnitSquad squad : currentBattle.squads) {
			squad.updateUnits(microBoard.getUnits());
			if (squad.isDead) {
				macroBoard.removeUnit(squad);
			}
		}
		isMacro = true;
	}
}
