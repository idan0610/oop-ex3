/**
 * Implements the open hash-set.
 * @author Idan Refaeli
 * @see SimpleHashSet
 */
public class OpenHashSet extends SimpleHashSet {
	
	private int capacity = DEFAULT_INITIAL_CAPACITY;
	private String[] hashTable;
	private final String deletedCell = new String("");
	private int capacityMinusOne = capacity - 1; // In order to save the calculation every time add() called.
	private int size = 0;
	private float loadFactor = 0f;
	private float upperLoadFactor;
	private float lowerLoadFactor;
	
	/**
	 * A default constructor. Constructs a new, empty table with default initial capacity (16), 
	 * upper load factor (0.75) and lower load factor (0.25).
	 */
	public OpenHashSet() {
		hashTable = new String[capacity];
		upperLoadFactor = DEFAULT_UPPER_LOAD_FACTOR;
		lowerLoadFactor = DEFAULT_LOWER_LOAD_FACTOR;
	}
	
	/**
	 * Constructs a new, empty table with the specified load factors, and the default initial capacity (16).
	 * @param upperLoadFactor The upper load factor of the hash table.
	 * @param lowerLoadFactor The lower load factor of the hash table.
	 */
	public OpenHashSet(float upperLoadFactor, float lowerLoadFactor) {
		hashTable = new String[capacity];
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
	public OpenHashSet(String[] data) {
		hashTable = new String[capacity];
		upperLoadFactor = DEFAULT_UPPER_LOAD_FACTOR;
		lowerLoadFactor = DEFAULT_LOWER_LOAD_FACTOR;
		for(String item : data) {
			add(item);
		}
	}
	
	public void rehashing(boolean moreOrLess) {
		// If moreOrLess = true, it means the hush table needs to be increased, so factor = 2.
		// Otherwise, the hush table needs to be decreased, so factor = 0.5.
		float factor;
		if (moreOrLess) factor = FACTOR_INCREASE_TABLE;
		else factor = FACTOR_DECREASE_TABLE;
		
		// Set the new capacity and capacityMinusOne, reset the size and create new hash table.
		capacity *= factor;
		size = 0;
		capacityMinusOne = capacity - 1;
		String[] oldHashTable = hashTable;
		hashTable = new String[capacity];
		
		// Iterate over every cell on the old hush table. If the cell is not empty, and the string in it
		// is not deletedCell (by reference), add the value on that cell to the new hash table.
		for(String cell : oldHashTable) {
			if (cell != null && cell != deletedCell) {
				add(cell);
			}
		}
	}
	
	/**
	 * Add a specified element to the set if it's not already in it.
	 * @param newValue New value to add to the set
	 * @return False iff newValue already exists in the set
	 */
	public boolean add(String newValue) {
		int hashCode = newValue.hashCode();
		int i = 0; // Try's number to add newValue
		int index = -1;
		int deletedCellIndex = -1;
		boolean canAdd = false;
		
		while (canAdd == false) {
			// Calculate the index for newValue on interval [0:capacityMinusOne]
			index = (hashCode + (i + i^2)/2) & capacityMinusOne;
			
			if (hashTable[index] == deletedCell) {
				// If hashTable[index] is deletedCell by reference, check if no other deleted cell was found
				// before, and if so, save the deleted cell index.
				if (deletedCellIndex == -1) {
					deletedCellIndex = index;
				}
			}
			else if (newValue.equals(hashTable[index])) {
				// If hashTable[index] is not deletedCell by reference and is equal by value to newValue, do
				// nothing to change the hash table and return false.
				return false;
			}
			else if (hashTable[index] == null) {
				// If hashTable[index] is null, means newValue is not already in the hash table and may be
				// added to it. A null cell will always be found because the hash table is never fully empty.
				canAdd = true;
			}
			i++;
		}
		
		// Now newValue will be added to the hash table for sure
		
		// If a deleted cell was found, add newValue to the deleted cell, otherwise, add to the current index
		// containing null. After that, add 1 to size and calculate the new load factor.
		if (deletedCellIndex != -1) {
			hashTable[deletedCellIndex] = newValue;
		}
		else {
			hashTable[index] = newValue;
		}
		size++;
		loadFactor = (float) size / capacity;
		
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
		// Calculate the index of searchVal on interval [0:capacityMinusOne]
		int hashCode = searchVal.hashCode();
		int i = 0; // Try's number to find searchVal
		int index;
		boolean found = false;
		while (found == false) {
			index = (hashCode + (i + i^2)/2) & capacityMinusOne;
			
			if(hashTable[index] == null) {
				// If the hash table does not contains searchVal, at some point the index will reach an empty
				// cell, and the loop will break.
				break;
			}
			
			if(hashTable[index] != deletedCell && searchVal.equals(hashTable[index])) {
				// If searchVal was found and is not deletedCell by reference
				found = true;
			}
			else {
				i++;
			}
		}
		
		return found;
	}
	
	/**
	 * Remove the input element from the set.
	 * @param toDelete Value to delete
	 * @return True iff toDelete is found and deleted
	 */
	public boolean delete(String toDelete) {
		int hashCode = toDelete.hashCode();
		int i = 0; // Try's number to add newValue
		int index;
		boolean found = false;
		
		while(found == false) {
			// Calculate the index of toDelete on interval [0:capacityMinusOne]
			index = (hashCode + (i + i^2)/2) & capacityMinusOne;
			
			if(hashTable[index] == null) {
				// If the hash table does not contains toDelete, at some point the index will reach an empty
				// cell, and the loop will break.
				return false;
			}
			
			if(hashTable[index] != deletedCell && toDelete.equals(hashTable[index])) {
				// If toDelete was found and is not deletedCell by reference
				hashTable[index] = deletedCell;
				size--;
				loadFactor = (float) size / capacity;
				found = true;
			}
			else {
				i++;
			}
		}
		
		if (loadFactor < lowerLoadFactor) {
			// After the decrease on hash table, check if the load factor is under lower load factor and
			// rehash if necessary.
			rehashing(false);
		}
		return true; // Only if toDelete was exist in hash table
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
