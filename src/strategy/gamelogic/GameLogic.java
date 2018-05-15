package strategy.gamelogic;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import strategy.gamelogic.pathing.AStarPather;
import strategy.gamelogic.pathing.BruteForcePather;
import strategy.gamelogic.pathing.Node;
import strategy.gamelogic.pathing.Path;
import strategy.gamelogic.pathing.Pather;
import strategy.gamelogic.players.HumanPlayer;
import strategy.gamelogic.players.Player;
import strategy.graphics.Camera;
import strategy.graphics.Renderer;
import strategy.graphics.Window;
import strategy.graphics.animations.UnitMoveAnimation;
import strategy.graphics.ui.UIManager;
import strategy.tile_engine.Board;
import strategy.tile_engine.DamageType;
import strategy.tile_engine.Square;
import strategy.tile_engine.Team;
import strategy.tile_engine.Unit;
import strategy.tile_engine.Villager;
import strategy.tile_engine.tiles.GrassTile;
import strategy.tile_engine.tiles.TestTile;
import strategy.tile_engine.tiles.VillageTile;
import strategy.tile_engine.units.TestUnit;
import strategy.tile_engine.units.UnitSquad;

public class GameLogic {
	UIManager uimanager;
	public UnitMoveAnimation moveAnimation = new UnitMoveAnimation();
	protected double currentTime;
	protected final int MICRO_BOARD_WIDTH = 15;
	protected final int MICRO_BOARD_HEIGHT = 15;
	protected final int MACRO_BOARD_WIDTH = 12;
	protected final int MACRO_BOARD_HEIGHT = 12;
	protected final double MOVE_TIME = 1.25;
	protected boolean clearMovable;
	protected Player player1;
	protected Player player2;
	Game game;
	public GameLogic () {
		// for now only implementing micro game
		newGame();
	}
	public GameLogic (String path) {
		// implement later
	}
	public void loadGame (String path) {
		// implement later
	}
	public void attackSquad (Unit u1, Unit u2) {
		UnitSquad us1 = u1.getUnitSquad();
		UnitSquad us2 = u2.getUnitSquad();
		Battle battle = new Battle();
		battle.addSquad(us1);
		battle.addSquad(us2);
		game.addBattle(battle);
	}
	public void attack (Unit u1, Unit u2) {
		// animation code
		// simple damage code
		damageUnit(u1, u2);
		if (u2.isDead) {
			// death logic
			System.out.println("Dead");
		} else if (u2.team != u1.team && canAttack(u2, u1)) {
			damageUnit(u2, u1);
		}
	}
	public boolean canAttack (Unit u1, Unit u2) {
		// checks whether a unit can attack another unit
		//System.out.println(u2 == null);
		//System.out.println(u1 == null);
		if (((Math.abs(u1.x - u2.x) + Math.abs(u1.y - u2.y)) <= u1.stats.range)
			&& ((u1.team == u2.team && u1.damageType == DamageType.HEALING)
			|| (u1.damageType != DamageType.HEALING && u1.team != u2.team))) {
			return true;
		}
		return false;
	}
	public void damageUnit (Unit u1, Unit u2) {
		// damages a unit
		if (u1.damageType == DamageType.MAGICAL) {
			u2.damage(u1.stats.magic, DamageType.MAGICAL);
		} else if (u1.damageType == DamageType.PHYSICAL) {
			u2.damage(u1.stats.attack, DamageType.PHYSICAL);
		} else {
			u2.damage(u1.stats.magic, DamageType.HEALING);
		}
	}
	public void newMicroTurn () {
		// implements turn code for later
		game.nextTurn();
	}
	public void logicLoop (Window window, double deltaTime, double currentTime) {
		// pretty temp logic code
		/*if (game.state == GameState.ANIMATION) {
			// more code
			updateAnimation(deltaTime); 
		}else if (game.state == GameState.INPUT) {
			if (game.isEndMove) {
				doEndMove();
			}
			if (game.newSelect) {
				// need to check if position is a unit and what team it is
				if (game.selectedUnit == null) {
					// selecting a unit
					Unit testUnit = game.getCurrentBoard()
										.getUnitAtPosition(game.selectedX,
														   game.selectedY);
					game.selectedUnit = testUnit;
				} else {
					startMoveAnimation(currentTime);
				}
				game.newSelect = false;
			}if (game.selectedUnit != null && uimanager.movable.size() == 0 
					&& !moveAnimation.inAnimation && !game.isEndMove) {
				setMovableUI();
			}

		}*/
		this.currentTime = currentTime;
		if (game.state == GameState.ANIMATION) {
			updateAnimation(deltaTime);
		} else if (game.state == GameState.INPUT) {
			/*if (game.newSelect) {
				doAction(currentTime);
				game.newSelect = false;
			}*/
			getCurrentPlayer().doAction();
			if (!game.isMacro) {
				if (isWonMicro(Team.BLUE) && !game.getCurrentBoard().isWon) {
					System.out.println("Blue wins the battle");
					// do end code
					game.getCurrentBoard().isWon = true;
				}
				else if (isWonMicro(Team.RED) && !game.getCurrentBoard().isWon) {
					System.out.println("Red wins the battle");
					game.getCurrentBoard().isWon = true;
				}
				if (game.getCurrentBoard().isWon) {
					game.finaliseBattle();
					game.nextTurn();
				}
			}
			//System.out.println("In logic loop");
			doMovableSquares();
		}
	}
	public void selectSquare (int x, int y) {
		// selects the square at a given x and y value
		if (game.state == GameState.INPUT) {
			game.selectedX = x;
			game.selectedY = y;
			game.newSelect = true;
		}
	}
	public Board getCurrentBoard () {
		// gets the current board
		return game.getCurrentBoard();
	}
	public void setUIManager (UIManager manager) {
		// sets UI manager
		uimanager = manager;
	}
	public void render (Renderer renderer, Camera camera) {
		Board currentBoard = game.getCurrentBoard();
		currentBoard.renderTiles(renderer, camera);
		uimanager.renderMovable(renderer, camera);
		currentBoard.renderUnits(renderer, camera);
	}
	public void newGame () {
		game = new Game (MICRO_BOARD_WIDTH, MICRO_BOARD_HEIGHT, 
				MACRO_BOARD_WIDTH, MACRO_BOARD_HEIGHT);
		game.fillTiles(new GrassTile(0, 0), false);
		game.fillTiles(new GrassTile(0, 0), true);
		// test units
		TestUnit unit = new TestUnit(2, 2);
		TestUnit unit2 = new TestUnit(4, 3);
		TestUnit unit3 = new TestUnit (8, 8);
		TestUnit unit4 = new TestUnit (6, 6);
		unit.team = Team.RED;
		unit2.team = Team.BLUE;
		unit3.team = Team.BLUE;
		unit3.stats.range = 2;
		//System.out.printf("%d %d %d\n", unit.stats.range, unit2.stats.range, unit3.stats.range);
		UnitSquad squad1 = new UnitSquad(2, 2);
		UnitSquad squad2 = new UnitSquad(4, 3);
		squad1.team = Team.RED;
		squad2.team = Team.BLUE;
		squad1.addUnit(unit);
		squad2.addUnit(unit2);
		squad2.addUnit(unit3);
		// must be called on each unit
		/*microBoard.addUnit(unit);
		microBoard.addUnit(unit2);
		microBoard.addUnit(unit3);*/
		game.addUnit(squad1, true);
		game.addUnit(squad2, true);
		game.addUnit(unit, false);
		game.addUnit(unit2, false);
		game.addUnit(unit3, false);
		game.addUnit(unit4, false);
		List<Villager> villagers = new ArrayList<Villager>();
		villagers.add(new Villager(6, 7, 3, 5, true));
		VillageTile v = new VillageTile(villagers, Team.BLUE);
		v.x = 4;
		v.y = 2;
		player1 = new HumanPlayer(this, Team.BLUE);
		player2 = new HumanPlayer(this, Team.RED);
		game.getCurrentBoard().setTile(v);
	}
	public void setMovableUI () {
		BruteForcePather pather = new BruteForcePather();
		List<Node> paths = pather.nodeGetPathableSquares(
													 	game.selectedUnit.x,
													 	game.selectedUnit.y, 
													 	game.selectedUnit.stats.movespeed,
													 	game.getCurrentBoard(), 
													 	game.selectedUnit.team);
		List<Node> attackable = pather.nodeGetAttackableUnits(game.selectedUnit, 
															  game.getCurrentBoard(), 
															  paths);
		//System.out.println("size: " + paths.size());
		/*if (selectedX + selectedY == 2) {
			System.out.println("Done getting pathable squares");
			for (Square s: paths) {
				//System.out.printf("(%d, %d)\n", s.x, s.y);
			}
		}*/
		// gets ui elements for them
		
		List<Square> movable = new LinkedList<Square>();
		for (Square s : paths) {
			movable.add(s);
		}
		List<Square> attack = new LinkedList<Square>();
		for (Square s : attackable) {
			attack.add(s);
		}
		uimanager.addMovableSquares(movable);
		uimanager.addAttackableSquares(attack);
		//System.out.println("Added movable squares");
	}
	public void setMovableUI (Unit u, Team team) {
		BruteForcePather pather = new BruteForcePather();
		List<Node> paths = pather.nodeGetPathableSquares(
													 	u.x,
													 	u.y, 
													 	u.stats.movespeed,
													 	game.getCurrentBoard(), 
													 	u.team);
		List<Node> attackable = pather.nodeGetAttackableUnits(u, 
															  game.getCurrentBoard(), 
															  paths);
		//System.out.println("size: " + paths.size());
		/*if (selectedX + selectedY == 2) {
			System.out.println("Done getting pathable squares");
			for (Square s: paths) {
				//System.out.printf("(%d, %d)\n", s.x, s.y);
			}
		}*/
		// gets ui elements for them
		
		List<Square> movable = new LinkedList<Square>();
		for (Square s : paths) {
			movable.add(s);
		}
		List<Square> attack = new LinkedList<Square>();
		for (Square s : attackable) {
			attack.add(s);
		}
		if (u.team == team) {
			uimanager.addMovableSquares(movable);
		} else {
			uimanager.addAttackableSquares(movable);
		}
		uimanager.addAttackableSquares(attack);
		//System.out.println("Added movable squares");
	}
	public void startMoveAnimation (double currentTime) {
		// does animation
		moveAnimation = new UnitMoveAnimation(game.selectedUnit, 
											  game.selectedX, 
											  game.selectedY, 
											  game.getCurrentBoard());
		if (moveAnimation.canAnimate) {
			// starts animation
			//System.out.println("Starting animation");
			moveAnimation.start(currentTime, currentTime + MOVE_TIME);
			game.state = GameState.ANIMATION;
			uimanager.clearMovableSquares();
		}
	}
	public void startMoveAnimation (double currentTime, Unit u, int x, int y) {
		// does animation
		moveAnimation = new UnitMoveAnimation(u, 
											  x, 
											  y, 
											  game.getCurrentBoard());
		if (moveAnimation.canAnimate) {
			// starts animation
			//System.out.println("Starting animation");
			moveAnimation.start(currentTime, currentTime + MOVE_TIME);
			game.state = GameState.ANIMATION;
			uimanager.clearMovableSquares();
		}
	}
	public void updateAnimation (double deltaTime) {
		moveAnimation.update(deltaTime);
		if (!moveAnimation.inAnimation) {
			game.state = GameState.INPUT;
		}
	}
	public void doEndMove () {
		
	}
	public boolean isInRange () {
		return false;
	}
	public String getUnitName (Unit u) {
		String team = "";
		if (u.team == Team.RED) {
			team = "red";
		} else {
			team = "blue";
		}
		return team + " " + u.getName();
	}
	public void doMovableSquares () {
		if (game.needMovable && !moveAnimation.inAnimation) {
			uimanager.clearMovableSquares();
			switch (game.inputState) {
			case START:
				//System.out.println("Doing start squares");
				List<Square> movableSquares = new LinkedList<Square>();
				for (Unit u: game.getCurrentBoard().getUnits()) {
					if (u.team == game.getCurrentTurn() && !u.exhausted) {
						Square s = new Square();
						s.x = u.x;
						s.y = u.y;
						movableSquares.add(s);
					}
				}
				uimanager.addMovableSquares(movableSquares);
				game.needMovable = false;
				if (movableSquares.size() == 0) {
					// System.out.println("No movable squares!");
					newMicroTurn();
					game.needMovable = true;
				}
				break;
			case MOVE:
				setMovableUI();
				game.needMovable = false;
				break;
			case ENDTURN:
				List<Square> attackableSquares = new LinkedList<Square>();
				//System.out.println("Doing endturn ui");
				//System.out.println(game.selectedUnit == null);
				for (Unit u: game.getCurrentBoard().getUnits()) {
					if (u.team != game.getCurrentTurn() && canAttack(game.selectedUnit, u)) {
						Square s = new Square();
						s.x = u.x;
						s.y = u.y;
						attackableSquares.add(s);
					}
				}
				uimanager.addAttackableSquares(attackableSquares);
				List<Square> currentPosition = new LinkedList<Square>();
				Square s = new Square();
				s.x = game.selectedUnit.x;
				s.y = game.selectedUnit.y;
				currentPosition.add(s);
				uimanager.addMovableSquares(currentPosition);
				game.needMovable = false;
				break;
			}
		}
	}
	public void doAction (double currentTime) {
		Unit testUnit = game.getCurrentBoard()
				.getUnitAtPosition(game.selectedX, game.selectedY);
		switch (game.inputState) {
		case START:
			// just selected a unit
			game.selectedUnit = testUnit;
			if (game.selectedUnit != null) {
				if (!testUnit.exhausted && testUnit.team == game.getCurrentTurn()) {
					game.inputState = InputState.MOVE;
					game.needMovable = true;
				}
			}
			break;
		
		case MOVE:
			// ready to move
			//System.out.println("Move turn");
			//System.out.println(game.selectedUnit == null);
			if (testUnit != null) {
				// another unit selected
				if (testUnit == game.selectedUnit) {
					game.inputState = InputState.ENDTURN;
					game.needMovable = true;
				} else if (testUnit.team == game.selectedUnit.team) {
					// select other unit
					if (!testUnit.exhausted) {
						game.selectedUnit = testUnit;
						game.needMovable = true;
					} else {
						// no selected unit as wanted to cancel
						// in future add code that lets you see
						// how far you can move
						game.selectedUnit = null;
						uimanager.clearMovableSquares();
						game.inputState = InputState.START;
						game.needMovable = true;
						System.out.println("Move cancelled");
					}
				} else {
					// do later
					// selects an enemy unit
				} 
			}else {
				// start move animation
				//System.out.println("Doing move animation:");
				//System.out.println(game.selectedUnit == null);
				startMoveAnimation(currentTime);
				//System.out.println(game.selectedUnit == null);
				//System.out.println("Done move animation");
				if (moveAnimation.canAnimate) {
					game.inputState = InputState.ENDTURN;
					game.needMovable = true;
				}
			}
			break;
			
		case ENDTURN:
			// has moved, just needs end turn commands
			//System.out.println("Ends turn");
			if (testUnit != null) {
				// selected another unit, checking whether can attack
				if (canAttack(game.selectedUnit, testUnit)) {
					if (game.isMacro) {
						attackSquad (testUnit, game.selectedUnit);
					} else {
						doUnitAttack(testUnit);
					}
				}
				
				// make sure the selected unit becomes exhausted and reset
				game.selectedUnit.exhausted = true;
				game.inputState = InputState.START;
				uimanager.clearMovableSquares();
				game.needMovable = true;
			}
			break;
		}
	}
	public void doUnitAttack (Unit testUnit) {
		// attack animation
		
		// do attack code
		int selectedPastHP = game.selectedUnit.currentHitpoints;
		int testPastHP = testUnit.currentHitpoints;
		attack(game.selectedUnit, testUnit);
		System.out.printf("%s attacked %s\n", getUnitName(game.selectedUnit),
											getUnitName(testUnit));
		System.out.printf("%s dealt %d damage\n", getUnitName(game.selectedUnit), 
				testPastHP - testUnit.currentHitpoints);
		if (testUnit.isDead) {
			System.out.printf("%s died\n", getUnitName(testUnit));
		} else {
			System.out.printf("%s is at %d HP\n", getUnitName(testUnit),
					testUnit.currentHitpoints);
			if (selectedPastHP != game.selectedUnit.currentHitpoints) {
				System.out.printf("%s dealt %d damage\n", getUnitName(testUnit),
						selectedPastHP - game.selectedUnit.currentHitpoints);
				if (game.selectedUnit.isDead) {
					System.out.printf("%s died\n", getUnitName(game.selectedUnit));
				} else {
					System.out.printf("%s is at %d HP\n", getUnitName(game.selectedUnit),
							game.selectedUnit.currentHitpoints);
				}
			}
		}
		// check for unit deaths
		if (testUnit.isDead) {
			game.getCurrentBoard().removeUnit(testUnit);
		}
		if (game.selectedUnit.isDead) {
			game.getCurrentBoard().removeUnit(game.selectedUnit);
		} 
	}
	public void doUnitAttack (Unit u1, Unit u2) {
		// attack animation
		
		// do attack code
		int selectedPastHP = game.selectedUnit.currentHitpoints;
		int testPastHP = u2.currentHitpoints;
		attack(game.selectedUnit, u2);
		System.out.printf("%s attacked %s\n", getUnitName(game.selectedUnit),
											getUnitName(u2));
		System.out.printf("%s dealt %d damage\n", getUnitName(game.selectedUnit), 
				testPastHP - u2.currentHitpoints);
		if (u2.isDead) {
			System.out.printf("%s died\n", getUnitName(u2));
		} else {
			System.out.printf("%s is at %d HP\n", getUnitName(u2),
					u2.currentHitpoints);
			if (selectedPastHP != game.selectedUnit.currentHitpoints) {
				System.out.printf("%s dealt %d damage\n", getUnitName(u2),
						selectedPastHP - game.selectedUnit.currentHitpoints);
				if (game.selectedUnit.isDead) {
					System.out.printf("%s died\n", getUnitName(game.selectedUnit));
				} else {
					System.out.printf("%s is at %d HP\n", getUnitName(game.selectedUnit),
							game.selectedUnit.currentHitpoints);
				}
			}
		}
		// check for unit deaths
		if (u2.isDead) {
			game.getCurrentBoard().removeUnit(u2);
		}
		if (game.selectedUnit.isDead) {
			game.getCurrentBoard().removeUnit(game.selectedUnit);
		} 
	}
	public boolean isWonMicro (Team team) {
		List <Unit> enemyUnits = new LinkedList<Unit>();
		for (Unit u : game.getCurrentBoard().getUnits()) {
			if (u.team != team) {
				enemyUnits.add(u);
			}
		}
		if (enemyUnits.size() == 0) {
			return true;
		}
		return false;
	}
	public void queryUnit (int x, int y) {
		if (game.getCurrentBoard().isUnitAtPosition(x, y)) {
			game.getCurrentBoard().getUnitAtPosition(x, y).query();
		}
	}
	public void recruitUnit (int x, int y) {
		VillageTile tile = game.getCurrentBoard().getTile(x, y).getVillageTile();
		if (!game.isMacro) {
			return;
		}
		Team team = game.currentTurn;
		Unit squad = game.getCurrentBoard().getUnitAtPosition(x, y);
		
		if (squad != null) {
			if (squad.team != team) {
				System.out.println("Enemy unit at position can't recruit");
				return;
			}
		}
		if (tile.hasRecruited) {
			System.out.println("Can't recruit any more units this turn");
			return;
		}
		if (team != game.currentTurn) {
			System.out.println("You don't own this village");
		}
		Unit u = tile.recruit(x, y, game.getCurrentTurn());
		if (u == null) {
			return;
		}
		tile.hasRecruited = true;
		u.team = game.currentTurn;
		UnitSquad unitSquad;
		if (squad == null) {
			unitSquad = new UnitSquad(x, y);
			unitSquad.team = game.currentTurn;
			game.addUnit(unitSquad, true);
			unitSquad.exhausted = true;
		} else {
			unitSquad = squad.getUnitSquad();
		}
		unitSquad.addUnit(u);
	}
	public InputState getInputState () {
		return game.inputState;
	}
	public Unit getSelectedUnit (int x, int y) {
		Board board = game.getCurrentBoard();
		return board.getUnitAtPosition(x, y);
	}
	public void selectUnit (Unit u) {
		game.selectedUnit = u;
	}
	public void clearMovableSquares () {
		uimanager.clearMovableSquares();
		clearMovable = true;
	}
	public void drawMovableSquares (Team team) {
		List<Square> movableSquares = new LinkedList<Square>();
		for (Unit u: game.getCurrentBoard().getUnits()) {
			if (u.team == team && !u.exhausted) {
				Square s = new Square();
				s.x = u.x;
				s.y = u.y;
				movableSquares.add(s);
			}
		}
		uimanager.addMovableSquares(movableSquares);
		game.needMovable = false;
		if (movableSquares.size() == 0) {
			// System.out.println("No movable squares!");
			newMicroTurn();
			game.needMovable = true;
		}
		clearMovable = false;
	}
	public void drawMovableSquares (Unit u, Team team) {
		setMovableUI(u, team);
		clearMovable = false;
	}
	public void drawActionableSquares (Unit unit, Team team) {
		List<Square> attackableSquares = new LinkedList<Square>();
		//System.out.println("Doing endturn ui");
		//System.out.println(game.selectedUnit == null);
		for (Unit u: game.getCurrentBoard().getUnits()) {
			if ((u.team != team || (u.team == unit.team && 
					unit.damageType == DamageType.HEALING)) 
					&& canAttack(unit, u)) {
				Square s = new Square();
				s.x = u.x;
				s.y = u.y;
				attackableSquares.add(s);
			}
		}
		uimanager.addAttackableSquares(attackableSquares);
		List<Square> currentPosition = new LinkedList<Square>();
		Square s = new Square();
		s.x = unit.x;
		s.y = unit.y;
		currentPosition.add(s);
		uimanager.addMovableSquares(currentPosition);
		clearMovable = false;
	}
	public boolean isClearMovable () {
		return clearMovable;
	}
	public void changeInputState (InputState state) {
		game.inputState = state;
	}
	public boolean isLegalMove (Unit u, int x, int y) {
		Pather pather = new AStarPather();
		Path path = pather.pathToSquare(x, y, u.x, u.y, u.stats.movespeed, 
				game.getCurrentBoard(), u.team);
		return path != null;
	}
	public void doMove (Unit u, int x, int y) {
		startMoveAnimation(currentTime, u, x, y);
	}
	public void doAttack (Unit u1, Unit u2) {
		if (game.isMacro) {
			attackSquad (u1, u2);
		} else {
			doUnitAttack(u1, u2);
		}
	}
	public Player getCurrentPlayer () {
		if (game.getCurrentTurn() == Team.BLUE) {
			return player1;
		} else {
			return player2;
		}
	}
}
