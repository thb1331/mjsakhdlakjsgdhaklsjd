package strategy.gamelogic.players;

import strategy.gamelogic.GameLogic;
import strategy.gamelogic.InputState;
import strategy.tile_engine.Team;
import strategy.tile_engine.Unit;

public class HumanPlayer implements Player {
	GameLogic logic;
	int selectedX;
	int selectedY;
	Unit selectedUnit;
	boolean isActive;
	boolean newSelect;
	Team team;
	public HumanPlayer (GameLogic logic, Team team) {
		this.logic = logic;
		this.isActive = false;
		this.newSelect = false;
		this.team = team;
	}

	@Override
	public void startTurn() {
		// TODO Auto-generated method stub
		isActive = true;
	}

	@Override
	public void doAction() {
		if (logic.isClearMovable()) {
			if (logic.getInputState() == InputState.ENDTURN) {
				logic.drawActionableSquares(selectedUnit, team);
			} else if (logic.getInputState() == InputState.START) {
				logic.drawMovableSquares(team);
			}
		}
		if (newSelect) {
			doNewSelect();
			newSelect = false;
		}
	}

	@Override
	public void endTurn() {
		// TODO Auto-generated method stub
		isActive = false;
	}

	public void selectSquare (int x, int y) {
		this.selectedX = x;
		this.selectedY = y;
		newSelect = true;
	}
	public void doNewSelect () {
		Unit testUnit = logic.getSelectedUnit(selectedX, selectedY);
		switch (logic.getInputState()) {
		case START:
			selectedUnit = testUnit;
			logic.clearMovableSquares();
			logic.selectUnit(testUnit);
			if (selectedUnit != null) {
				logic.drawMovableSquares(selectedUnit, team);
				if (canBeMoved(selectedUnit)) {
					logic.changeInputState(InputState.MOVE);
				}
			} else {
				logic.drawMovableSquares(team);
			}
			break;
		case MOVE:
			logic.clearMovableSquares();
			if (testUnit != null && testUnit != selectedUnit) {
				selectedUnit = testUnit;
				logic.selectUnit(testUnit);
				logic.drawMovableSquares(selectedUnit, team);
				if (!canBeMoved(selectedUnit)) {
					logic.changeInputState(InputState.START);
				}
			} else if (testUnit == selectedUnit) {
				logic.changeInputState(InputState.ENDTURN);
			} else if (logic.isLegalMove(selectedUnit, selectedX, selectedY)) {
				logic.doMove(selectedUnit, selectedX, selectedY);
				logic.changeInputState(InputState.ENDTURN);
			} else {
				System.out.println("Move cancelled");
				selectedUnit = null;
				logic.selectUnit(null);
				logic.changeInputState(InputState.START);
				logic.drawMovableSquares(team);
			}
			break;
		case ENDTURN:
			if (testUnit == selectedUnit) {
				selectedUnit.exhausted = true;
				selectedUnit = null;
				logic.changeInputState(InputState.START);
				logic.clearMovableSquares();
				logic.drawMovableSquares(team);
			} else if (logic.canAttack(selectedUnit, testUnit)) {
				logic.doAttack(selectedUnit, testUnit);
				selectedUnit.exhausted = true;
				selectedUnit = null;
				logic.changeInputState(InputState.START);
				logic.clearMovableSquares();
				logic.drawMovableSquares(team);
			}
			
			break;
		}
	}
	protected boolean canBeMoved (Unit u) {
		return (u.team == team && !u.exhausted);
	}
}
