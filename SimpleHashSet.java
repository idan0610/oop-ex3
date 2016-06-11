/**
 * A superclass for implementations of hash-sets implementing the SimpleSet interface.
 * @author Idan Refaeli
 * @see SimpleSet
 */
public abstract class SimpleHashSet implements SimpleSet {
	
	protected final int DEFAULT_INITIAL_CAPACITY = 16;
	protected final float DEFAULT_UPPER_LOAD_FACTOR = 0.75f;
	protected final float DEFAULT_LOWER_LOAD_FACTOR = 0.25f;
	protected final float FACTOR_INCREASE_TABLE = 2f;
	protected final float FACTOR_DECREASE_TABLE = 0.5f;
	
	public abstract int capacity();
	
}
