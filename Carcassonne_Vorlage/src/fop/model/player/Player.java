package fop.model.player;


import fop.controller.GameController;
import fop.model.gameplay.GamePlay;
import fop.model.interfaces.GameConstants;
import fop.model.interfaces.PlayerMethods;
import fop.model.tile.Position;
import fop.model.tile.Tile;

public class Player implements PlayerMethods{

	private MeepleColor color;
	private String name;
	private int score;
	private int meeples; // the amount of meeples

	public Player(String name, MeepleColor color) {
		this.color = color;
		this.name = name;
		this.score = 0;
		this.meeples = GameConstants.NUMBER_OF_MEEPLES;
	}

	
	@Override
	public MeepleColor getColor() {
		return color;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void addScore(int score) {
		this.score += score;
	}

	@Override
	public int getScore() {
		return score;
	}

	@Override
	public int getMeepleAmount() {
		return meeples;
	}
	
	@Override
	public void removeMeeple() {
		meeples--;
	}
	
	@Override
	public void returnMeeple() {
		meeples++;
	}
	
	public void draw(GamePlay gp, Tile tile) {
		if (tile == null) gp.nextRound();
		
		GameController gc = gp.getGameController();
		// TODO
		Tile[][] board = gc.getGameBoard().getBoard();
		outer:
		for (int i = 0; i < 144; i++)
			for (int j = 0; j < 144; j++) {
				if (board[i][j] == null && gc.getGameBoard().isTileAllowed(tile, i, j)) {		
					gp.newTile(tile, i, j);
					//gc.getTileStack().push(gc.getTileStack());
					return;
				}
			}
	}
	
	public void placeMeeple(GamePlay gp) {
		//TODO
		//if no position is allowed, you have to call nextRound() by yourself. 
		//to place a meeple, call gp.placeMeeple(...).
		GameController gc = gp.getGameController();

		boolean meaples[] = gc.getGameBoard().getMeepleSpots();
		if(meaples != null)
			for (int i = 0; i < 9; i++) 
				if (meaples[i] == true) {
					gp.placeMeeple(Position.values()[i]);
					return;
				}

		gp.nextRound();
		return;
	}
	
}
