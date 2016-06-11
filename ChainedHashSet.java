import java.util.LinkedList;

/**
 * Implements the chained hash-set.
 * @author Idan Refaeli
 * @see SimpleHashSet
 */
public class ChainedHashSet extends SimpleHashSet {
	
	private int capacity = DEFAULT_INITIAL_CAPACITY;
	private CollectionFacadeSet[] hashTable;
	private int capacityMinusOne = capacity - 1; // In order to save the calculation every time add() called.
	private int size = 0;
	private float loadFactor = 0f;
	private float upperLoadFactor;
	private float lowerLoadFactor;
	
	/**
	 * A default constructor. Constructs a new, empty table with default initial capacity (16), 
	 * upper load factor (0.75) and lower load factor (0.25).
	 */
	public ChainedHashSet() {
		hashTable = new CollectionFacadeSet[capacity];
		upperLoadFactor = DEFAULT_UPPER_LOAD_FACTOR;
		lowerLoadFactor = DEFAULT_LOWER_LOAD_FACTOR;
	}
	
	/**
	 * Constructs a new, empty table with the specified load factors, and the default initial capacity (16).
	 * @param upperLoadFactor The upper load factor of the hash table.
	 * @param lowerLoadFactor The lower load factor of the hash table.
	 */
	public ChainedHashSet(float upperLoadFactor, float lowerLoadFactor) {
		hashTable = new CollectionFacadeSet[capacity];
		if(0 < lowerLoadFactor  && lowerLoadFactor < upperLoadFactor && upperLoadFactor < 1) {
			this.upperLoadFactor = upperLoadFactor;
			this.lowerLoadFactor = lowerLoadFactor;
		}
		else {
			// If the upper and lower load factors parameters are not in range (0, 1), set to default.
			this.upperLoadFactor = DEFAULT_UPPER_LOAD_FACTOR;
			this.lowerLoadFactor = DEFAULT_LOWER_LOAD_FACTOR;
		}
	}
	
	/**
	 * Data constructor - builds the hash set by adding the elements one by one. Duplicate values should be
	 * ignored. The new table has the default values of initial capacity (16), upper load factor (0.75), and
	 * lower load factor (0.25).
	 * @param data Values to add to the set.
	 */
	public ChainedHashSet(String[] data) {
		hashTable = new CollectionFacadeSet[capacity];
		upperLoadFactor = DEFAULT_UPPER_LOAD_FACTOR;
		lowerLoadFactor = DEFAULT_LOWER_LOAD_FACTOR;
		for(String item : data) {
			add(item);
		}
	}
	
	private void rehashing(boolean moreOrLess) {
		// If moreOrLess = true, it means the hush table needs to be increased, so factor = 2.
		// Otherwise, the hush table needs to be decreased, so factor = 0.5.
		float factor;
		if (moreOrLess) factor = FACTOR_INCREASE_TABLE;
		else factor = FACTOR_DECREASE_TABLE;
		
		// Set the new capacity and capacityMinusOne, reset the size and create new hash table.
		capacity *= factor;
		size = 0;
		capacityMinusOne = capacity - 1;
		CollectionFacadeSet[] oldHashTable = hashTable;
		hashTable = new CollectionFacadeSet[capacity];
		
		// Iterate over every bucket on the old hush table. If the bucket is not empty, iterate over every
		// item on the linked list the bucket contains, and add it to the new hash table.
		for(CollectionFacadeSet bucket : oldHashTable) {
			if (bucket != null) {
				for(String item : bucket.getCollection()) {
					add(item);
				}
			}
		}
	}
	
	/**
	 * Add a specified element to the set if it's not already in it.
	 * @param newValue New value to add to the set
	 * @return False iff newValue already exists in the set
	 */
	public boolean add(String newValue) {
		
		// Find the index for newValue using its hash code on interval [0:capacity-1]
		int index = newValue.hashCode() & capacityMinusOne;
		
		if (hashTable[index] == null) {
			// If the bucket on index is empty, create new linked list on that bucket
			LinkedList<String> linkedList = new LinkedList<>();
			hashTable[index] = new CollectionFacadeSet(linkedList);
		}
		
		if (hashTable[index].add(newValue)) {
			// If the bucket successfully added newValue to its list, increase size of hash table by 1 and
			// calculate the new load factor.
			size++;
			loadFactor = (float) size / capacity;
		}
		else {
			// If the bucket already contained newValue, do nothing to change the hush table and
			// return false.
			return false;
		}
		
		if (loadFactor > upperLoadFactor) {
			// After the increase on hash table, check if the load factor is over upper load factor and
			// rehash if necessary.
			rehashing(true);
		}
		return true; // Only if newValue was not exist in hash table before
	}

	/**
	 * Look for a specified value in the set.
	 * @param searchVal Value to search for
	 * @return True iff searchVal is found in the set
	 */
	public boolean contains(String searchVal) {
		// Find the index for newValue using its hash code on interval [0:capacity-1]
		int index = searchVal.hashCode() & capacityMinusOne;
		
		if(hashTable[index] != null && hashTable[index].contains(searchVal)) {
			// If the bucket on index is not empty and the linked list it has contains searchVal, return true
			return true;
		}
		
		return false;
	}

	/**
	 * Remove the input element from the set.
	 * @param toDelete Value to delete
	 * @return True iff toDelete is found and deleted
	 */
	public boolean delete(String toDelete) {
		// Find the index for newValue using its hash code on interval [0:capacity-1]
		int index = toDelete.hashCode() & capacityMinusOne;
		
		if(hashTable[index] != null && hashTable[index].delete(toDelete)) {
			// If the bucket successfully deleted toDelete, decrease size of hash table by 1 and
			// calculate the new load factor.
			size--;
			loadFactor = (float) size / capacity;
		}
		else {
			// If toDelete was not on the bucket, do nothing to change the hush table and
			// return false.
			return false;
		}
		
		if(loadFactor < lowerLoadFactor) {
			// After the decrease on hush table, check if the load factor is under lower load factor and
			// rehash if necessary.
			rehashing(false);
		}
			
		return true; // Only if toDelete was exist in hash table before
	}

	/**
	 * @return The number of elements currently in the set
	 */
	public int size() {
		return size;
	}

	/**
	 * @return The current capacity (number of cells) of the table.
	 */
	public int capacity() {
		return capacity;
	}
	
}
