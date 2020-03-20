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
		tilePositions.forEach(p -> this.rateTile(tile, p));
		tilePositions.sort(Comparator.comparing(Rateable::getRating));
		RateableTilePosition position = tilePositions.get(0);
		gp.newTile(tile, position.getX(), position.getY());
	}

	private void rateTile(Tile tile, RateableTilePosition position) {
		int middle = (144 / 2);
		double distance = Math.sqrt(((middle - tile.x) * (middle - tile.x)) + ((middle - tile.y) * (middle - tile.y)));
		position.setRating(distance);
	}

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

		public double getRating() {
			return rating;
		}

		public void incrementRating() {
			incrementRating(1);
		}

		public void incrementRating(int i) {
			rating += i;
		}

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

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}

	private static class RateableMeeplePosition extends Rateable {

		private Position position;

		public RateableMeeplePosition(Position position) {
			this.position = position;
		}

		public Position getPosition() {
			return position;
		}
	}
	
}
