package fop.model.gameplay;

import fop.base.Edge;
import fop.base.Node;
import fop.model.graph.FeatureGraph;
import fop.model.graph.FeatureNode;
import fop.model.player.Player;
import fop.model.tile.FeatureType;
import static fop.model.tile.FeatureType.*;
import fop.model.tile.Position;
import static fop.model.tile.Position.*;
import fop.model.tile.Tile;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Gameboard extends Observable<Gameboard> {

	private Tile[][] board;
	private List<Tile> tiles;
	private FeatureGraph graph;
	private Tile newestTile;

	public Gameboard() {
		board = new Tile[144][144];
		tiles = new LinkedList<Tile>();
		graph = new FeatureGraph();
	}

	// kann nicht im konstrukor erfolgen, weil erst observer gesetzt werden muss
	public void initGameboard(Tile t) {
		newTile(t, 72, 72);
	}

	public void newTile(Tile t, int x, int y) {
		t.x = x;
		t.y = y;
		board[x][y] = newestTile = t;
		tiles.add(t);

		connectNodes(x, y);
		push(this); // pushes the new gameboard state to its observers (= GameBoardPanel)
	}

	/**
	 * Connects the nodes of all neighboring tiles facing the tile at given
	 * coordinates x, y. It is assumed that the tile is placed according to the
	 * rules.
	 * 
	 * @param x coordinate
	 * @param y coordinate
	 */
	private void connectNodes(int x, int y) {
		graph.addAllNodes(board[x][y].getNodes());
		graph.addAllEdges(board[x][y].getEdges());

		Tile t = board[x][y];

		// Check top tile
		// TODO
			// This might be helpful:
			// As we already ensured that the tile on top exists and fits the tile at x, y,
			// we know that if the feature of its top is a ROAD, the feature at the bottom
			// of the tile on top is a ROAD aswell. As every ROAD has FIELD nodes as
			// neighbours on both sides, we can connect those nodes of the two tiles. The
			// same logic applies to the next three routines.
		int x1 = x; int y1 = y-1;
		Position a  = TOP;      Position b  = BOTTOM;
		Position a1 = TOPLEFT;  Position b1 = BOTTOMLEFT;
		Position a2 = TOPRIGHT; Position b2 = BOTTOMRIGHT;
		if(board[x1][y1] != null) {
			graph.addEdge(board[x][y].getNode(a), board[x1][y].getNode(b));
			if(board[x][y].getNode(a1) != null && board[x1][y1].getNode(b1) != null)
				if (board[x][y].getNode(a1).equals(board[x1][y].getNode(b1)))
					graph.addEdge(board[x][y].getNode(a1), board[x1][y1].getNode(b1));
			if (board[x][y].getNode(a2) != null && board[x1][y1].getNode(b2) != null)
				if(board[x][y].getNode(a2).equals(board[x1][y1].getNode(b2)))
					graph.addEdge(board[x][y].getNode(a2), board[x1][y1].getNode(b2));
		}


		// Check left tile
		// TODO
		x1 = x-1; y1 = y;
		a  = LEFT;       b  = RIGHT;
		a1 = TOPLEFT;    b1 = TOPRIGHT;
		a2 = BOTTOMLEFT; b2 = BOTTOMRIGHT;
		if(board[x1][y1] != null) {
			graph.addEdge(board[x][y].getNode(a), board[x1][y].getNode(b));
			if(board[x][y].getNode(a1) != null && board[x1][y1].getNode(b1) != null)
				if (board[x][y].getNode(a1).equals(board[x1][y].getNode(b1)))
					graph.addEdge(board[x][y].getNode(a1), board[x1][y1].getNode(b1));
			if (board[x][y].getNode(a2) != null && board[x1][y1].getNode(b2) != null)
				if(board[x][y].getNode(a2).equals(board[x1][y1].getNode(b2)))
					graph.addEdge(board[x][y].getNode(a2), board[x1][y1].getNode(b2));
		}

		// Check right tile
		// TODO
		x1 = x-1; y1 = y;
		a  = RIGHT;       b  = LEFT;
		a1 = TOPRIGHT;    b1 = TOPLEFT;
		a2 = BOTTOMRIGHT; b2 = BOTTOMLEFT;
		if(board[x1][y1] != null) {
			graph.addEdge(board[x][y].getNode(a), board[x1][y].getNode(b));
			if(board[x][y].getNode(a1) != null && board[x1][y1].getNode(b1) != null)
				if (board[x][y].getNode(a1).equals(board[x1][y].getNode(b1)))
					graph.addEdge(board[x][y].getNode(a1), board[x1][y1].getNode(b1));
			if (board[x][y].getNode(a2) != null && board[x1][y1].getNode(b2) != null)
				if(board[x][y].getNode(a2).equals(board[x1][y1].getNode(b2)))
					graph.addEdge(board[x][y].getNode(a2), board[x1][y1].getNode(b2));
		}

		// Check bottom tile
		// TODO
		x1 = x-1; y1 = y;
		a  = BOTTOM;      b  = TOP;
		a1 = BOTTOMLEFT;  b1 = TOPLEFT;
		a2 = BOTTOMRIGHT; b2 = TOPRIGHT;
		if(board[x1][y1] != null) {
			graph.addEdge(board[x][y].getNode(a), board[x1][y].getNode(b));
			if(board[x][y].getNode(a1) != null && board[x1][y1].getNode(b1) != null)
				if (board[x][y].getNode(a1).equals(board[x1][y].getNode(b1)))
					graph.addEdge(board[x][y].getNode(a1), board[x1][y1].getNode(b1));
			if (board[x][y].getNode(a2) != null && board[x1][y1].getNode(b2) != null)
				if(board[x][y].getNode(a2).equals(board[x1][y1].getNode(b2)))
					graph.addEdge(board[x][y].getNode(a2), board[x1][y1].getNode(b2));
		}
	}

    /**
     * Checks if the given tile could be placed at position x, y on the board
     * according to the rules.
     *
     * @param t The tile
     * @param x The x position on the board
     * @param y The y position on the board
     *
     * @return True if it would be allowed, false if not.
     */
    public boolean isTileAllowed(Tile t, int x, int y) {
        if (x < 0 || y < 0 || x >= 144 || y >= 144)
            return false;

        boolean isAllowed;
        // Check top tile
        isAllowed = (board[x][y - 1] == null) || (board[x][y - 1].getNode(BOTTOM).getType().equals(t.getNode(TOP).getType()));

        // Check left tile
        isAllowed &= (board[x - 1][y] == null) || (board[x - 1][y].getNode(RIGHT).getType().equals(t.getNode(LEFT).getType()));

        // Check right tile
        isAllowed &= (board[x + 1][y] == null) || (board[x + 1][y].getNode(LEFT).getType().equals(t.getNode(RIGHT).getType()));

        // Check bottom tile
        isAllowed &= (board[x][y + 1] == null) || (board[x][y + 1].getNode(TOP).getType().equals(t.getNode(BOTTOM).getType()));

        return isAllowed;
    }

	/**
	 * Checks if the given tile would be allowed anywhere on the board adjacent to
	 * other tiles and according to the rules.
	 * 
	 * @param newTile The tile.
	 * @return True if it is allowed to place the tile somewhere on the board, false
	 *         if not.
	 */
	public boolean isTileAllowedAnywhere(Tile newTile) {
		// Iterate over all tiles
		for(Tile t : tiles) {
			// check top
			// TODO
			if(!t.getNode(TOP).isConnectingTiles()) for(int j = 0; j < 4; j++) {
				if(isTileAllowed(newTile, t.x, t.y-1)) return true;
				newTile.rotateRight();
			}

			// check left
			// TODO
			if(!t.getNode(LEFT).isConnectingTiles()) for(int j = 0; j < 4; j++) {
				if(isTileAllowed(newTile, t.x-1, t.y)) return true;
				newTile.rotateRight();
			}

			// check right
			// TODO
			if(!t.getNode(RIGHT).isConnectingTiles()) for(int j = 0; j < 4; j++) {
				if(isTileAllowed(newTile, t.x+1, t.y)) return true;
				newTile.rotateRight();
			}

			// check bottom
			// TODO
			if(!t.getNode(BOTTOM).isConnectingTiles()) for(int j = 0; j < 4; j++) {
				if(isTileAllowed(newTile, t.x, t.y+1)) return true;
				newTile.rotateRight();
			}
		
		}
		// no valid position was found
		return false;
	}

	/**
	 * Calculates points for monasteries (one point for the monastery and one for
	 * each adjacent tile).
	 */
	public void calculateMonasteries(State state) {
		//the methods getNode() and getType of class Tile and FeatureNode might be helpful
		
		//Check all surrounding tiles and add the points
				
		//Points are given if the landscape is complete or the game is over
		//Meeples are just returned in case of state == State.GAME_OVER
				
		//After adding the points to the overall points of the player, set the score to 1 again
		int adj;
		
		for (int x = 0; x < board.length; x++)
			for (int y = 0; y < board[0].length; y++) {
				adj = 0;
				Tile tile = board[x][y];
				if (tile != null && tile.getNode(CENTER) != null && tile.getNode(CENTER).getType() == MONASTERY && tile.getMeeple() != null && tile.getMeeplePosition() == CENTER) {
					for (int i = -1; i <= 1; i++)
						for (int j = -1; j <= 1; j++) {
							if (x+i < 0 || y+j < 0 || x+i >= 144 || y+j >= 144) continue;
							adj += board[x+i][y+j] != null ? 1 : 0;
						}
					System.out.println(adj);
					if (state == state.GAME_OVER || adj == 9)
						tile.getMeeple().addScore(adj); //TODO: reset score
				}
			}
	}

	/**
	 * Calculates points and adds them to the players score, if a feature was
	 * completed. FIELDS are only calculated when the game is over.
	 * 
	 * @param state The current game state.
	 */
	public void calculatePoints(State state) {
		// Fields are only calculated on final scoring.
		if (state == State.GAME_OVER)
			calculatePoints(FIELDS, state);

		calculatePoints(CASTLE, state);
		calculatePoints(ROAD, state);
		calculateMonasteries(state);
	}

	/**
	 * Calculates and adds points to the players that scored a feature. If the given
	 * state is GAME_OVER, points are added to the player with the most meeple on a
	 * subgraph, even if it is not completed.
	 * 
	 * @param type  The FeatureType that is supposed to be calculated.
	 * @param state The current game state.
	 */
	public void calculatePoints(FeatureType type, State state) {
		List<Node<FeatureType>> nodeList = new ArrayList<>(graph.getNodes(type));
		
		// queue defines the connected graph. If this queue is empty, every node in this graph will be visited.
		// if nodeList is non-empty, insert the next node of nodeList into this queue
		ArrayDeque<Node<FeatureType>> queue = new ArrayDeque<>();
		


		int score = 0;
		boolean completed = true; // Is the feature completed? Is set to false if a node is visited that does not
									// connect to any other tile

		queue.push(nodeList.remove(0));
		// Iterate as long as the queue is not empty
		// Remember: queue defines a connected graph
		
		//TODO
		
		// Hint:
		// If there is one straight positioned node that does not connect to another
		// tile, the feature cannot be completed.


		//TODO
	}

	/**
	 * Returns all Tiles on the Gameboard.
	 * 
	 * @return all Tiles on the Gameboard.
	 */
	public List<Tile> getTiles() {
		return tiles;
	}

	/**
	 * Returns the Tile containing the given FeatureNode.
	 * 
	 * @param node A FeatureNode.
	 * @return the Tile containing the given FeatureNode.
	 */
	private Tile getTileContainingNode(FeatureNode node) {
		for (Tile t : tiles) {
			if (t.containsNode(node))
				return t;
		}
		return null;
	}

	/**
	 * Returns the spots on the most recently placed tile on which it is allowed to
	 * place a meeple .
	 * 
	 * @return The spots on which it is allowed to place a meeple as a boolean array
	 *         representing the tile split in nine cells from top left, to right, to
	 *         bottom right. If there is no spot available at all, returns null.
	 */
	public boolean[] getMeepleSpots() {
		boolean[] positions = new boolean[9];
		boolean anySpot = false; // if there is not a single spot, this remains false

		for (Position p : Position.values()) {
			FeatureNode n = newestTile.getNodeAtPosition(p);
			if (n != null)
				if (n.hasMeepleSpot() && !hasMeepleOnSubGraph(n))
					positions[p.ordinal()] = anySpot = true;
		}

		if (anySpot)
			return positions;
		else
			return null;
	}

	/**
	 * Checks if there are any meeple on the subgraph that FeatureNode n is a part
	 * of.
	 * 
	 * @param n The FeatureNode to be checked.
	 * @return True if the given FeatureNode has any meeple on its subgraph, false
	 *         if not.
	 */
	private boolean hasMeepleOnSubGraph(FeatureNode n) {
		List<Node<FeatureType>> visitedNodes = new ArrayList<>();
		ArrayDeque<Node<FeatureType>> queue = new ArrayDeque<>();

		queue.push(n);
		while (!queue.isEmpty()) {
			FeatureNode node = (FeatureNode) queue.pop();
			if (node.hasMeeple())
				return true;

			List<Edge<FeatureType>> edges = graph.getEdges(node);
			for (Edge<FeatureType> edge : edges) {
				Node<FeatureType> nextNode = edge.getOtherNode(node);
				if (!visitedNodes.contains(nextNode)) {
					queue.push(nextNode);
					visitedNodes.add(nextNode);
				}
			}
		}
		return false;
	}

	/**
	 * Returns the newest tile.
	 * 
	 * @return the newest tile.
	 */
	public Tile getNewestTile() {
		return newestTile;
	}

	/**
	 * Places a meeple of given player at given position on the most recently placed
	 * tile (it is only allowed to place meeple on the most recent tile).
	 * 
	 * @param position The position the meeple is supposed to be placed on on the
	 *                 tile (separated in a 3x3 grid).
	 * @param player   The owner of the meeple.
	 */
	public void placeMeeple(Position position, Player player) {
		board[newestTile.x][newestTile.y].getNode(position).setPlayer(player);
		player.removeMeeple();
	}

	public Tile[][] getBoard() {
		return board;
	}
	
	public FeatureGraph getGraph() {
		return this.graph;
	}
	public void setFeatureGraph(FeatureGraph graph) {
		this.graph = graph; 
	}
}
