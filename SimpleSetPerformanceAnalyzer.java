import java.util.*;

/**
 * 
 * @author Maayan
 *
 */
public class SimpleSetPerformanceAnalyzer {
	
	private final float nanoToMill = 1000000f;
	private int chainedHashSetIndex = 0;
	private int openHashSetIndex = 1;
	private int treeSetIndex = 2;
	private int linkedListIndex = 3;
	private int hashSetIndex = 4;
	private int numOfCollections = 5;

	private SimpleSet[] dataStructures;
	
	/**
	 * Initiates a new SimpleSetPerformanceAnalyzer object with SimpleSet array containing the following:
	 * 0. ChainedHashSet
	 * 1. OpenHashSet
	 * 2. CollectionFacadeSet(TreeSet)
	 * 3. CollectionFacadeSet(LinkedList)
	 * 4. CollectionFacadeSet(HashSet).
	 */
	public SimpleSetPerformanceAnalyzer() {
		dataStructures = new SimpleSet[numOfCollections];
		
		ChainedHashSet chainedHashSet = new ChainedHashSet();
		dataStructures[chainedHashSetIndex] = chainedHashSet;
		
		OpenHashSet openHashSet = new OpenHashSet();
		dataStructures[openHashSetIndex] = openHashSet;
		
		TreeSet<String> treeSet = new TreeSet<>();
		dataStructures[treeSetIndex] = new CollectionFacadeSet(treeSet);
		
		LinkedList<String> linkedList = new LinkedList<>();
		dataStructures[linkedListIndex] = new CollectionFacadeSet(linkedList);
		
		HashSet<String> hashSet = new HashSet<>();
		dataStructures[hashSetIndex] = new CollectionFacadeSet(hashSet);
	}
	
	/**
	 * Add all the words from given array to the given collection.
	 * @param c Index of the SimpleSet.
	 * @param data String array of words.
	 */
	public void addData(int c, String[] data) {
		System.out.print("Adding words to data structure number " + c + "... ");
		
		long timeBefore = System.nanoTime();
		
		// Add each word from data to the collection
		for(String item : data) {
			dataStructures[c].add(item);
		}
		
		// Calculate the time this operation took
		long difference = (long) ((System.nanoTime() - timeBefore)/nanoToMill);
		System.out.println("Done. Took " + difference + " milliseconds");
	}
	
	/**
	 * Check if the given collection contains the given word.
	 * @param c Index of the SimpleSet.
	 * @param word The word to check.
	 */
	public void containWord(int c, String word) {
		System.out.print("Check if data structure number " + c + " contains '" + word + "'... ");
		
		long timeBefore = System.nanoTime();
		
		System.out.print(dataStructures[c].contains(word) + " ");
		
		long difference = System.nanoTime() - timeBefore;
		System.out.println("Took " + difference + " nanoseconds");
	}
	
	public static void main(String[] args) {
		
		// Create 2 SimpleSetPerformanceAnalyzer objects for each data.
		SimpleSetPerformanceAnalyzer testsData1 = new SimpleSetPerformanceAnalyzer();
		SimpleSetPerformanceAnalyzer testsData2 = new SimpleSetPerformanceAnalyzer();
		
		String[] data1 = Ex3Utils.file2array("data1.txt");
		String[] data2 = Ex3Utils.file2array("data2.txt");
		
		System.out.println("Data Structures: 0 - ChainedHashSet; 1 - OpenHashSet; 2 - TreeSet; "
				+ "3 - LinkedList; 4 - HashSet");
		
		System.out.println("Adding words from data1 to data structures:");
		for(int i = 0; i < 5; i++) {
			testsData1.addData(i, data1);
		}
		
		System.out.println("Adding Words from data2 to data structures:");
		for(int i = 0; i < 5; i++) {
			testsData2.addData(i, data2);
		}
		
		System.out.println("Checking if data structures with data1 contain the word 'hi':");
		for(int i = 0; i < 5; i++) {
			testsData1.containWord(i, "hi");
		}
		
		System.out.println("Checking if data structures with data1 contain the word '-13170890158':");
		for(int i = 0; i < 5; i++) {
			testsData1.containWord(i, "-13170890158");
		}
		
		System.out.println("Checking if data structures with data2 contain the word '23':");
		for(int i = 0; i < 5; i++) {
			testsData2.containWord(i, "23");
		}
		
		System.out.println("Checking if data structures with data2 contain the word 'hi':");
		for(int i = 0; i < 5; i++) {
			testsData2.containWord(i, "hi");
		}

	}

}