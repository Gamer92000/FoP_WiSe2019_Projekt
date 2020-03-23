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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Gameboard extends Observable<Gameboard> {

	private Tile[][] board;
	private List<Tile> tiles;
	private FeatureGraph graph;
	private Tile newestTile;
	private HashMap<Player, Integer> besetzteBurgen = new HashMap<>();
	

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

		// TODO
			// This might be helpful:
			// As we already ensured that the tile on top exists and fits the tile at x, y,
			// we know that if the feature of its top is a ROAD, the feature at the bottom
			// of the tile on top is a ROAD aswell. As every ROAD has FIELD nodes as
			// neighbours on both sides, we can connect those nodes of the two tiles. The
			// same logic applies to the next three routines.
		// Check bottom tile
		int x1 = x; int y1 = y+1;
		Position a  = BOTTOM;      Position b  = TOP;
		Position a1 = BOTTOMLEFT;  Position b1 = TOPLEFT;
		Position a2 = BOTTOMRIGHT; Position b2 = TOPRIGHT;
		if(board[x1][y1] != null) {
			graph.addEdge(t.getNode(a), board[x1][y1].getNode(b));
			if(t.getNode(a1) != null && board[x1][y1].getNode(b1) != null)
				graph.addEdge(t.getNode(a1), board[x1][y1].getNode(b1));
			if (t.getNode(a2) != null && board[x1][y1].getNode(b2) != null)
				graph.addEdge(t.getNode(a2), board[x1][y1].getNode(b2));
		}


		// Check left tile
		// TODO
		x1 = x-1; y1 = y;
		a  = LEFT;       b  = RIGHT;
		a1 = TOPLEFT;    b1 = TOPRIGHT;
		a2 = BOTTOMLEFT; b2 = BOTTOMRIGHT;
		if(board[x1][y1] != null) {
			graph.addEdge(t.getNode(a), board[x1][y1].getNode(b));
			if(t.getNode(a1) != null && board[x1][y1].getNode(b1) != null)
				graph.addEdge(t.getNode(a1), board[x1][y1].getNode(b1));
			if (t.getNode(a2) != null && board[x1][y1].getNode(b2) != null)
				graph.addEdge(t.getNode(a2), board[x1][y1].getNode(b2));
		}

		// Check right tile
		// TODO
		x1 = x+1; y1 = y;
		a  = RIGHT;       b  = LEFT;
		a1 = TOPRIGHT;    b1 = TOPLEFT;
		a2 = BOTTOMRIGHT; b2 = BOTTOMLEFT;
		if(board[x1][y1] != null) {
			graph.addEdge(t.getNode(a), board[x1][y1].getNode(b));
			if(t.getNode(a1) != null && board[x1][y1].getNode(b1) != null)
				graph.addEdge(t.getNode(a1), board[x1][y1].getNode(b1));
			if (t.getNode(a2) != null && board[x1][y1].getNode(b2) != null)
				graph.addEdge(t.getNode(a2), board[x1][y1].getNode(b2));
		}

		// Check top tile
		// TODO
		x1 = x; y1 = y-1;
		a  = TOP;      b  = BOTTOM;
		a1 = TOPLEFT;  b1 = BOTTOMLEFT;
		a2 = TOPRIGHT; b2 = BOTTOMRIGHT;
		if(board[x1][y1] != null) {
			graph.addEdge(t.getNode(a), board[x1][y1].getNode(b));
			if(t.getNode(a1) != null && board[x1][y1].getNode(b1) != null)
				graph.addEdge(t.getNode(a1), board[x1][y1].getNode(b1));
			if (t.getNode(a2) != null && board[x1][y1].getNode(b2) != null)
				graph.addEdge(t.getNode(a2), board[x1][y1].getNode(b2));
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
        
        isAllowed = (y-1 >= 0) && (board[x][y - 1] != null) || (x-1 >= 0) && (board[x - 1][y] != null) || (x+1 < 144) && (board[x + 1][y] != null) || (y+1 < 144) && (board[x][y + 1] != null);
        
        // Check top tile
        isAllowed &= (y-1 < 0) || (board[x][y - 1] == null) || (board[x][y - 1].getNode(BOTTOM).getType().equals(t.getNode(TOP).getType()));

        // Check left tile
        isAllowed &= (x-1 < 0) || (board[x - 1][y] == null) || (board[x - 1][y].getNode(RIGHT).getType().equals(t.getNode(LEFT).getType()));

        // Check right tile
        isAllowed &= (x+1 >= 144) || (board[x + 1][y] == null) || (board[x + 1][y].getNode(LEFT).getType().equals(t.getNode(RIGHT).getType()));

        // Check bottom tile
        isAllowed &= (y+1 >= 144) || (board[x][y + 1] == null) || (board[x][y + 1].getNode(TOP).getType().equals(t.getNode(BOTTOM).getType()));

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
					if (state == State.GAME_OVER || adj == 9) {
						tile.getMeeple().addScore(adj);
						if (state != State.GAME_OVER) {
							tile.getMeeple().returnMeeple();
							tile.getNode(tile.getMeeplePosition()).setPlayer(null);
						}
					}
				}
			}
	}

	/**
	 * Calculates points and adds them to the players score, if a feature was
	 * completed. FIELDS are only calculated when the game is over.
	 * 
	 * @param state The current game state.
	 */
	
	int maxMeeple;
	List<List<FeatureNode>> completedCastleStructs = new ArrayList<List<FeatureNode>>();
	
	public void calculatePoints(State state) {
		// Fields are only calculated on final scoring.
		completedCastleStructs.clear();
		calculatePoints(CASTLE, state);
		calculatePoints(ROAD, state);
		if (state == State.GAME_OVER)
			calculatePoints(FIELDS, state);
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
		List<FeatureNode> fieldNodes = new ArrayList<FeatureNode>();
		
		
		// queue defines the connected graph. If this queue is empty, every node in this graph will be visited.
		// if nodeList is non-empty, insert the next node of nodeList into this queue
		ArrayDeque<Node<FeatureType>> queue = new ArrayDeque<>();
		
		

		// for each feature
		while (!nodeList.isEmpty()) {
			
			int score = 0;
			boolean completed = true, added = false; // Is the feature completed? Is set to false if a node is visited that does not
									// connect to any other tile
			
			
			if (type == CASTLE) {
				
				completedCastleStructs.add(new ArrayList<FeatureNode>());
			}

			queue.push(nodeList.remove(0));

			HashMap<Player, Integer> meeples = new HashMap<Player, Integer>();
			List<FeatureNode> nodesWithMeeple = new ArrayList<FeatureNode>();
			
			List<Tile> tiles = new ArrayList<Tile>();
			int pennants = 0;
			// complete processing of feature
			while (!queue.isEmpty()) {
				
				List<Node<FeatureType>> addToQueue = new ArrayList<Node<FeatureType>>();
				for (Node<FeatureType> node : queue) {
					
					
					Tile tile = getTileContainingNode((FeatureNode) node); // again, no way this works // but in the end it totally works ;D
					
					// process current node
					if (!tiles.contains(tile)) {
						tiles.add(tile);
						if (tile.hasPennant())
							pennants++;
					}
					
					
					
					// look for meeple
					FeatureNode fNode = (FeatureNode) node;
					
					if(type == FIELDS)
						fieldNodes.add(fNode);
					
					
					if(type == CASTLE && !completedCastleStructs.get(completedCastleStructs.size()-1).contains(fNode)) {
						
						completedCastleStructs.get(completedCastleStructs.size()-1).add(fNode);
						added = true;
					}
					
					if (tile.hasMeeple() && fNode == tile.getNodeAtPosition(tile.getMeeplePosition())) {
						nodesWithMeeple.add(fNode);
						if(!meeples.containsKey(tile.getMeeple()))
							meeples.put(tile.getMeeple(), 1);
						else {
							meeples.put(tile.getMeeple(), meeples.get(tile.getMeeple()) + 1);
						}
					}
					// look for completed
					
					if(tile.getNodeAtPosition(CENTER) != fNode && !fNode.isConnectingTiles())
						completed = false;
					
					
					queue.remove(node);
					
					// nodes connecting tiles
					for (Edge<FeatureType> edge : graph.getEdges(node)) {
						Node<FeatureType> otherNode = edge.getOtherNode(node);
						if (nodeList.remove(otherNode)) // only add if not processed yet
							addToQueue.add(otherNode);
					}
				}
				
				queue.addAll(addToQueue);
				
				
				
			}
			
			if(!completed && type == CASTLE && added) 
				completedCastleStructs.remove(completedCastleStructs.size()-1);
			
			
			
			
			
			if (completed && !meeples.isEmpty() && type != FIELDS) {
				score = tiles.size();
				if (type == CASTLE) {
					score *= 2;
					score += pennants*2;
				}
				
				List<Player> owners = new ArrayList<Player>();
				maxMeeple = 0;
				
				meeples.forEach((x,y) -> {
					
					if(y == maxMeeple)
						owners.add(x);
					if(y > maxMeeple){
						owners.clear();
						owners.add(x);
						maxMeeple = y;
					}
				});
				
				for (Player p : owners) {
					p.addScore(score);
					if(type == CASTLE) {
					if(!besetzteBurgen.containsKey(p))
						besetzteBurgen.put(p, 1);
					else {
						besetzteBurgen.put(p, besetzteBurgen.get(p) + 1);
					}
				}
						
				}
				
				for (FeatureNode fNode : nodesWithMeeple) {
					fNode.getPlayer().returnMeeple();
					fNode.setPlayer(null);
				}
				
			} else if (state == State.GAME_OVER && !meeples.isEmpty()) {
				score = tiles.size();
				
				
				
				if (type == FIELDS) {
					
					score = 0;
					
					List<List<FeatureNode>> lookingAt = new ArrayList<>();
					lookingAt.addAll(completedCastleStructs);
					
					//lookingAt.remove(lookingAt.size()-1);

					List<List<FeatureNode>> nevv = lookingAt;
					for (FeatureNode node : fieldNodes) {

						Tile tile = getTileContainingNode(node);
						
						for (int i = 0; i < lookingAt.size(); i++) {
							boolean removed = false;
							
							if (lookingAt.get(i).contains(tile.getNode(TOP))){
								if (tile.getNode(CENTER) == node || tile.getNode(TOPLEFT) == node || tile.getNode(TOPRIGHT) == node){
									score += 3;
									
									removed = true;
								}
							}
							
							
							if (lookingAt.get(i).contains(tile.getNode(LEFT))){
								if (tile.getNode(CENTER) == node || tile.getNode(TOPLEFT) == node || tile.getNode(BOTTOMLEFT) == node){
									score += 3;
									
									removed = true;
								}
							}
							
							if (lookingAt.get(i).contains(tile.getNode(RIGHT))){
								if (tile.getNode(CENTER) == node || tile.getNode(BOTTOMRIGHT) == node || tile.getNode(TOPRIGHT) == node){
									score += 3;
									
									removed = true;
								}
							}
							
							if (lookingAt.get(i).contains(tile.getNode(BOTTOM))){
								if (tile.getNode(CENTER) == node || tile.getNode(BOTTOMLEFT) == node || tile.getNode(BOTTOMRIGHT) == node){
									score += 3;
									
									removed = true;
								}
							}
							
							if (lookingAt.get(i).contains(tile.getNode(CENTER))){
								if (tile.getNode(TOP) == node || tile.getNode(TOPLEFT) == node || tile.getNode(TOPRIGHT) == node || tile.getNode(RIGHT) == node || tile.getNode(BOTTOMRIGHT) == node || tile.getNode(BOTTOM) == node
										|| tile.getNode(BOTTOMLEFT) == node || tile.getNode(LEFT) == node){
									score += 3;
									
									removed = true;
								}
							}
							if (removed)  {
								nevv.remove(lookingAt.get(i));
							}
							if(removed) i--;
						}
					}
					
				}
				
				List<Player> owners = new ArrayList<Player>();
				maxMeeple = 0;
				
				meeples.forEach((x,y) -> {
					if(y == maxMeeple)
						owners.add(x);
					if(y > maxMeeple){
						owners.clear();
						owners.add(x);
						maxMeeple = y;
					}
				});
				
				//System.out.println("score (type: " + type + "): " + score);
				
				for (Player p : owners)
					p.addScore(score);
				
			}
			
		}
	}
	
	
	
	public HashMap<Player, Integer> getInfoMission1() {
		System.out.println("size:");
		return besetzteBurgen;
	}
	
	
	private int first=0, second=0;
	public boolean isThreeAhead(HashMap<Player, Integer> list) {
		first = 0;
		second = 0;
		list.forEach((x, y) -> {
			if(y > first) {
				second = first;
				first = y;
			} else if (y > second){
				second = y;
			}
		});
		System.out.println("unterschied: " + (first - second));
		if(first - second >= 3)
			return true;
		return false;
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
