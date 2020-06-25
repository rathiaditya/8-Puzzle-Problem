import java.util.ArrayList;
import java.util.Scanner;
 
public class Puzzle {
	/** width & height of puzzle */
    private static int SIZE = 3;
    /** the current state of the puzzle */
    private int[][] current;
    /** the goal state of the puzzle */
    private int[][] goal;
    
    /**
     * Constructor, initializes the current state and the goal state
     * @param current current state
     * @param goal goal state
     */
    public Puzzle(int[][] current, int[][] goal) {        
        this.current = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.current[i][j] = current[i][j];
            }
        }
        
        this.goal = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                this.goal[i][j] = goal[i][j];
            }
        }
    }
    
    /**
     * Misplaced tiles heuristic
     */
    public int misplacedTiles() {
        int error = 0;
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (current[i][j] != goal[i][j] && current[i][j] != 0) {
                    error += 1;
                }
            }
        }
        
        return error;
    }
    
    /**
     * Manhattan distance heuristic
     */
    public int manhattan() {
        int error = 0;
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (current[i][j] != 0) {
                	error += Math.abs(getRow(current[i][j]) - i) + Math.abs(getCol(current[i][j] - j));
                }   
            }
        }
        
        return error;
    }
    
    /**
     * Returns the row of one of the blocks in the puzzle
     * @param value the block being looked at
     */
    public int getRow(int value) {
    	for(int i = 0; i < SIZE; i++){
    		for(int j = 0; j < SIZE; j++){
    			if(goal[i][j] == value){
    				return i;
    			}
    		}
    	}
    	
    	return 0;
    }
    
    /**
     * Returns the column of one of the blocks in the puzzle
     * @param value the block being looked at
     */
    public int getCol(int value) {
    	for(int i = 0; i < SIZE; i++){
    		for(int j = 0; j < SIZE; j++){
    			if(goal[i][j] == value){
    				return j;
    			}
    		}
    	}
    	
    	return 0;
    }
    
    /**
     * Returns true if the current state is the goal state, false if it isn't
     */
    public boolean isGoal() {
    	for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                if (current[i][j] != goal[i][j]){
                    return false;
                }
            }
        }
    	  	
    	return true;
    }
    
    /**
     * Checks if two puzzles are equal to each other
     */
    public boolean equals(Object y) {
            if (y == this){
            	return true;
            }
            
            if (y == null){
            	return false;
            }
            
            if (y.getClass() != this.getClass()){
            	return false;
            }
            
            Puzzle that = (Puzzle) y;
            
            if (that.current.length != SIZE || that.current[0].length != SIZE) {
                return false;
            }
            
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (this.current[i][j] != that.current[i][j]) {
                        return false;
                    }
                }
            }
            
            return true;
        }
    
    /**
     * Returns the next node after a move has been made
     */
    public Puzzle next() {
        Puzzle newPuzzle = new Puzzle(current, goal);
        
        for (int i = SIZE - 1; i >= 0; i--){
            for (int j = SIZE - 2; j >= 0; j--){
                if (newPuzzle.current[i][j] != 0 && newPuzzle.current[i][j + 1] != 0){
                	newPuzzle.swap(i, j, i, j + 1);
                    return newPuzzle;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Returns the next possible nodes
     */
    public Iterable<Puzzle> neighbors() {
        ArrayList<Puzzle> n = new ArrayList<Puzzle>();
        
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                if (current[i][j] == 0){
                    if (j > 0) {
                        n.add(getNeighbor(i, j, i, j - 1));
                    }
                    if (j < SIZE - 1) {
                        n.add(getNeighbor(i, j, i, j + 1));
                    }
                    if (i > 0) {
                        n.add(getNeighbor(i, j, i - 1, j));
                    }
                    if (i < SIZE - 1) {
                        n.add(getNeighbor(i, j, i + 1, j));
                    }
                    return n;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Swaps two pieces in the puzzle
     * @param i row of first piece
     * @param j column of first piece
     * @param k row of second piece
     * @param h column of second piece
     */
    private void swap(int i, int j, int k, int h) {
        int temp = current[i][j];
        current[i][j] = current[k][h];
        current[k][h] = temp;
    }
    
    /**
     * Returns the new node after swapping two pieces
     * @param i0
     * @param j0
     * @param i
     * @param j
     * @return
     */
    private Puzzle getNeighbor(int i, int j, int k, int h) {
        Puzzle newPuzzle = new Puzzle(current, goal);
        newPuzzle.swap(i, j, k, h);
        return newPuzzle;
    }
    
    /**
     * Prints the puzzle
     */
    public String toString() {
        String s = "\n";
        
        for (int i = 0; i < SIZE; i++) {
            s += " ";
            for (int j = 0; j < SIZE; j++) {
                s += current[i][j] + "  ";
            }
            s += "\n";
        }
        
        return s;
    }
    
    /**
     * For testing
     *
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
    	int[][] initial = new int[SIZE][SIZE];
        int[][] goal = new int[SIZE][SIZE];
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                initial[i][j] = sc.nextInt();
            }
        }
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                goal[i][j] = sc.nextInt();
            }
        }
        
        Puzzle puzzle = new Puzzle(initial, goal);
        System.out.println(puzzle.misplacedTiles());
        System.out.println(puzzle.manhattan());
        //System.out.println(puzzle.isGoal());
        System.out.println(puzzle);
        System.out.println("Neighbors:");
        Iterable<Puzzle> it = puzzle.neighbors();
        for (Puzzle b : it) {
            System.out.println(b);
        }
        System.out.println("Twin:");
        System.out.println(puzzle.next());
        // System.out.println(puzzle);
    }
    */
}