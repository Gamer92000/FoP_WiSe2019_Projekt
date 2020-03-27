package fop.model.player;


import fop.controller.GameController;
import fop.model.gameplay.GamePlay;
import fop.model.interfaces.GameConstants;
import fop.model.interfaces.PlayerMethods;
import fop.model.tile.Position;
import fop.model.tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
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

	/**
	 * AI place tile add center most position
	 * @param gp
	 * @param tile
	 */
	public void draw(GamePlay gp, Tile tile) {
		if (tile == null) return;

		List<RateableTilePosition> tilePositions = new ArrayList<>();
		GameController gc = gp.getGameController();
		Tile[][] board = gc.getGameBoard().getBoard();
		for (int i = 0; i < 144; i++) {
			for (int j = 0; j < 144; j++) {
				if (board[i][j] != null) continue;
				if (!gc.getGameBoard().isTileAllowed(tile, i, j)) continue;
				tilePositions.add(new RateableTilePosition(i, j));
			}
		}

		if (tilePositions.isEmpty()) return;
		tilePositions.forEach(RateableTilePosition::rate);
		tilePositions.sort(Comparator.comparing(Rateable::getRating));
		gp.newTile(tile, tilePositions.get(0).getX(), tilePositions.get(0).getY());
	}

	/**
	 * AI place meeple at random position
	 * @param gp
	 */
	public void placeMeeple(GamePlay gp) {
		GameController gc = gp.getGameController();
		boolean[] meeples = gc.getGameBoard().getMeepleSpots();
		if (meeples == null) {
			gp.nextRound();
			return;
		}

		List<RateableMeeplePosition> meeplePositions = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			if (!meeples[i]) continue;
			meeplePositions.add(new RateableMeeplePosition(Position.values()[i]));
		}

		if (meeplePositions.isEmpty()) {
			gp.nextRound();
			return;
		}

		Collections.shuffle(meeplePositions);
		gp.placeMeeple(meeplePositions.get(0).getPosition());
	}

	private static class Rateable {
		private double rating = 0;

		/**
		 * @return the rating
		 */
		public double getRating() {
			return rating;
		}

		/**
		 * set the rating
		 * @param rating
		 */
		public void setRating(double rating) {
			this.rating = rating;
		}
	}

	private static class RateableTilePosition extends Rateable {
		private int x, y;

		public RateableTilePosition(int x, int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * @return x
		 */
		public int getX() {
			return x;
		}

		/**
		 * @return y
		 */
		public int getY() {
			return y;
		}

		/**
		 * rate a tale based on it's distance to the center
		 */
		public void rate() {
			double distance = Math.sqrt(Math.pow((72 - this.x), 2) + Math.pow((72 - this.y), 2));
			this.setRating(distance);
		}
	}

	private static class RateableMeeplePosition extends Rateable {
		private Position position;

		public RateableMeeplePosition(Position position) {
			this.position = position;
		}

		/**
		 * @return position
		 */
		public Position getPosition() {
			return position;
		}
	}
	
}
