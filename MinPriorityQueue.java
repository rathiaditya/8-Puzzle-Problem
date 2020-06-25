import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MinPriorityQueue<Key> implements Iterable<Key> {
    /** array of keys */
	private Key[] pq;
	/** number of elements in the priority queue */
    private int n;
    /** comparator to compare the keys */
    private Comparator<Key> comp;

    /**
     * Initializes an empty priority queue
     * @param  cap the initial capacity of this priority queue
     */
    @SuppressWarnings("unchecked")
	public MinPriorityQueue(int cap) {
        pq = (Key[]) new Object[cap + 1];
        n = 0;
    }

    /**
     * Initializes an empty priority queue
     */
    public MinPriorityQueue() {
        this(1);
    }

    /**
     * Initializes an empty priority queue
     * @param  cap the initial capacity of this priority queue
     * @param  comp the order in which to compare the keys
     */
    @SuppressWarnings("unchecked")
	public MinPriorityQueue(int cap, Comparator<Key> comp) {
        this.comp = comp;
        pq = (Key[]) new Object[cap + 1];
        n = 0;
    }

    /**
     * Initializes an empty priority queue
     * @param  comp the order in which to compare the keys
     */
    public MinPriorityQueue(Comparator<Key> comp) {
        this(1, comp);
    }
    
    /**
     * 
     * @param keys
     */
    @SuppressWarnings("unchecked")
	public MinPriorityQueue(Key[] keys) {
        n = keys.length;
        pq = (Key[]) new Object[keys.length + 1];
        for (int i = 0; i < n; i++)
            pq[i+1] = keys[i];
        for (int k = n/2; k >= 1; k--)
            downHeap(k);
        assert isMinHeap();
    }
    
    /**
     * Returns true if this priority queue is empty.
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
     * Returns the number of keys on this priority queue
     */
    public int size() {
        return n;
    }
    
    /**
     * Doubles the size of the heap array
     * @param cap capacity of the heap array
     */
    @SuppressWarnings("unchecked")
	private void resize(int cap) {
        assert cap > n;
        Key[] temp = (Key[]) new Object[cap];
        
        for (int i = 1; i <= n; i++) {
            temp[i] = pq[i];
        }
        
        pq = temp;
    }

    /**
     * Adds a new key to the priority queue
     * @param  x the key to add to this priority queue
     */
    public void enqueue(Key x) {
        if (n == pq.length - 1){
        	resize(2 * pq.length);
        }

        pq[++n] = x;
        upHeap(n);
        assert isMinHeap();
    }

    /**
     * Removes and returns a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Key dequeue() {
        if (isEmpty()){
        	throw new NoSuchElementException("Priority queue is empty");
        }
        
        Key min = pq[1];
        swap(1, n--);
        downHeap(1);
        pq[n+1] = null;
        
        if ((n > 0) && (n == (pq.length - 1) / 4)){
        	resize(pq.length / 2);
        }
        
        assert isMinHeap();
        return min;
    }
    
    //********************** Helpers **********************
    
    /**
     * Moves a key up in the heap
     * @param k the key being moved
     */
    private void upHeap(int k) {
        while (k > 1 && greater(k / 2, k)) {
            swap(k, k / 2);
            k = k/2;
        }
    }
    
    /**
     * Moves a key down in the heap
     * @param k the key being moved
     */
    private void downHeap(int k) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && greater(j, j + 1)){
            	j++;
            }
            
            if (!greater(k, j)){
            	break;
            }
            
            swap(k, j);
            k = j;
        }
    }
    
    /**
     * Compares i and j to find out which is greater
     * @param i an element in the priority queue
     * @param j another element in the priority queue
     */
    @SuppressWarnings("unchecked")
	private boolean greater(int i, int j){
        if (comp == null) {
            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0;
        } else {
            return comp.compare(pq[i], pq[j]) > 0;
        }
    }
    
    /**
     * Swaps two elements in the priority queue
     * @param i first element
     * @param j second element
     */
    private void swap(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    /**
     * Returns whether or not pq is a min heap
     */
    private boolean isMinHeap() {
        return isMinHeap(1);
    }

    /**
     * Returns whether or not a subtree of pq is a min heap
     * @param k root of the subtree
     */
    private boolean isMinHeap(int k) {
        if (k > n){
        	return true;
        }
        
        int left = 2 * k;
        int right = 2 * k + 1;
        
        if (left  <= n && greater(k, left)){
        	return false;
        }
        
        if (right <= n && greater(k, right)){
        	return false;
        }
        
        return isMinHeap(left) && isMinHeap(right);
    }


    /**
     * Returns an iterator that iterates through the keys in the priority queue in ascending order
     */
    public Iterator<Key> iterator() {
        return new HeapIterator();
    }
    
    /**
     * For iterating through a heap
     */
    private class HeapIterator implements Iterator<Key> {
        private MinPriorityQueue<Key> copy;

        public HeapIterator() {
            if (comp == null){
            	copy = new MinPriorityQueue<Key>(size());
            } else {
            	copy = new MinPriorityQueue<Key>(size(), comp);
            }
            
            for (int i = 1; i <= n; i++){
            	copy.enqueue(pq[i]);
            }
        }

		@Override
		public boolean hasNext() {
			return !copy.isEmpty();
		}

		@Override
		public Key next() {
			if (!hasNext()){
				throw new NoSuchElementException();
			}
            return copy.dequeue();
		}
    }

    /**
     * For testing
     *
    public static void main(String[] args) {
        MinPriorityQueue<String> pq = new MinPriorityQueue<String>();
        Scanner sc = new Scanner(System.in);
        
        while (!StdIn.isEmpty()) {
            String item = sc.nextString();
            if (!item.equals("-")) pq.enqueue(item);
            else if (!pq.isEmpty()) System.out.print(pq.dequeue() + " ");
        }
        
        System.out.println("(" + pq.size() + " left on pq)");
    }
    */
}