import java.util.Stack;
import java.util.Scanner;

public class Main {
	/** width & height of the puzzle */
    public static int SIZE = 3;
    /** priority queue */
	private MinPriorityQueue<Node> pq = new MinPriorityQueue<Node>();
	/** counter for number of moves needed to get to solution */
    private int minMoves = -1;
    /** best option for the next move */
    private Node best;
    /** has the puzzle been solved? */
    private boolean isSolved = false;
    /** counter to keep track of nodes expanded */
    private int numNodes = 0;
    
    /**
     * Get the heuristic, initial state, & goal state from the user
     * Then print the solution, the number of moves needed, & the number of nodes expanded
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[][] initialPuzzle = new int[SIZE][SIZE];
        int[][] goalPuzzle = new int[SIZE][SIZE];
        int k = 0;
        
        System.out.println("Enter the heuristic you want to use:\n1 - Manhattan\n2 - Misplaced Tiles\n3 - Exit");
        k = sc.nextInt();
        
        while(k != 3){
        	System.out.println("Enter the Initial State:");
            for (int i = 0; i < SIZE; i++){
            	for (int j = 0; j < SIZE; j++){
                	initialPuzzle[i][j] = sc.nextInt();
                }
            }
            
            System.out.println("Enter the Goal State:");
            for (int i = 0; i < SIZE; i++){
            	for (int j = 0; j < SIZE; j++){
            		goalPuzzle[i][j] = sc.nextInt();
            	}
            }
            
            Puzzle initial = new Puzzle(initialPuzzle, goalPuzzle);
            Main solve = new Main(initial, k);
            
            if (!solve.isSolvable())
                System.out.println("No solution possible\n");
            else {
                for (Puzzle board : solve.solution()){
                	System.out.println(board);
                }
                
                System.out.println("Minimum number of moves = " + solve.moves());
                System.out.println("Total nodes created = " + solve.numNodes + "\n");
            }
            
            System.out.println("Enter the heuristic you want to use:\n1 - Manhattan\n2 - Misplaced Tiles\n3 - Exit");
            k = sc.nextInt();
        }
        sc.close();
    }
    
    /**
     * Finds a solution using A* star
     * @param initial initial state
     * @param k integer passed into Node to determine which heuristic to use
     */
    public Main(Puzzle initial, int k) {
        if (initial == null) {
            throw new NullPointerException();
        }
        
        pq.enqueue(new Node(initial, 0, null, k)); 
        pq.enqueue(new Node(initial.next(), 0, null, k));
        
        while (!pq.isEmpty()) {
            Node current = pq.dequeue();
            if (current.puzzle.isGoal()) {
                Node root = root(current);
                if (!root.puzzle.equals(initial)) {
                    break;
                }
                isSolved = true;
                if (minMoves == -1 || current.moves < minMoves) {
                    minMoves = current.moves;
                    best = current;
                }
            } 
            if (minMoves == -1 || current.moves + current.cost < minMoves) {
                Iterable<Puzzle> it = current.puzzle.neighbors();
                for (Puzzle p : it) {
                    if (current.prev == null || !p.equals(current.prev.puzzle)) {
                        pq.enqueue(new Node(p, current.moves + 1, current, k));
                    }
                }
            } else {
                break;
            }
        }
    }
    
    /**
     * Node class
     */
    private class Node implements Comparable<Node> {
        private Puzzle puzzle;
        private int moves, cost;
        private Node prev;
 
        public Node(Puzzle board, int moves, Node prev, int k) {
            this.puzzle = board;
            this.moves = moves;
            this.prev = prev;
            numNodes++;
            
            if(k == 1){
            	cost = board.manhattan();
            } else if(k == 2){
            	cost = board.misplacedTiles();
            }
        }
        
        /**
         * Compares two nodes
         */
        @Override
        public int compareTo(Node that) {
            return this.moves + this.cost - that.moves - that.cost;
        }
    }
    
    /**
     * Returns the root node
     * @param node the node the search for the root starts with
     */
    private Node root(Node node) {
        Node current = node;
        while (current.prev != null) {
            current = current.prev;
        }
        return current;
    }
    
    /**
     * Returns if the initial state is solvable
     */
    public boolean isSolvable() {
        return isSolved;
    }
    
    /**
     * Returns the minimum number of moves used to get to the solution
     */
    public int moves() {
        return minMoves;
    }
    
    /**
     * Returns a stack with all the nodes used to find the solution
     * ***because it is a stack, the program prints out the goal state first and the initial state at the bottom
     */
    public Iterable<Puzzle> solution() {
        if (isSolvable()) {
            Stack<Puzzle> sol = new Stack<Puzzle>();
            Node current = best;
            while (current != null) {
                sol.push(current.puzzle);
                current = current.prev;
            }
            return sol;
        }
        return null;
    }
}
