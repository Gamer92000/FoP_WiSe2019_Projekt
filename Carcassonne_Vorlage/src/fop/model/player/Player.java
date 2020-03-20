package fop.model.player;


import fop.controller.GameController;
import fop.model.gameplay.GamePlay;
import fop.model.interfaces.GameConstants;
import fop.model.interfaces.PlayerMethods;
import fop.model.tile.Position;
import fop.model.tile.Tile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

		List<PossiblePosition> possiblePositions = new ArrayList<>();
		GameController gc = gp.getGameController();
		// TODO
		Tile[][] board = gc.getGameBoard().getBoard();
		for (int i = 0; i < 144; i++) {
			for (int j = 0; j < 144; j++) {
				if (board[i][j] != null) continue;
				if (!gc.getGameBoard().isTileAllowed(tile, i, j)) continue;
				possiblePositions.add(new PossiblePosition(i, j));
			}
		}

		if (possiblePositions.isEmpty()) return;
		possiblePositions.sort(Comparator.comparing(PossiblePosition::getRating));
		PossiblePosition position = possiblePositions.get(0);
		gp.newTile(tile, position.getX(), position.getY());
		//gc.getTileStack().push(gc.getTileStack());
	}
	
	public void placeMeeple(GamePlay gp) {
		//TODO
		//if no position is allowed, you have to call nextRound() by yourself. 
		//to place a meeple, call gp.placeMeeple(...).
		GameController gc = gp.getGameController();

		boolean[] meaples = gc.getGameBoard().getMeepleSpots();
		if (meaples == null) {
			gp.nextRound();
			return;
		}

		for (int i = 0; i < 9; i++) {
			if (!meaples[i]) continue;
			gp.placeMeeple(Position.values()[i]);
			return;
		}

		gp.nextRound();
	}

	private static class PossiblePosition {
		private int x, y, rating;

		public PossiblePosition(int x, int y) {
			this.x = x;
			this.y = y;
			this.rating = 0;
		}


		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getRating() {
			return rating;
		}

		public void setRating(int rating) {
			this.rating = rating;
		}
	}
	
}
