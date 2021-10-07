package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		TrieNode root = new TrieNode(null, null, null);
		// not sure if I even need to put that there
		// they did say they wouldn't be empty tests
		// idk doesn't hurt to put it here just cause
		if(allWords.length == 0){
			return root;
		}
		// REMOVE IF IT SEEMS TO CAUSE PROBLEMS (probably wont tho)
		///////////////////////////////////////////////////////////////////////////////
		Indexes indxRC = new Indexes(0,(short)0,(short)(allWords[0].length() - 1));	
		root.firstChild = new TrieNode(indxRC, null, null);
		TrieNode curr = root.firstChild;
		TrieNode prev = root.firstChild;
		// maybe make them 0 instead  of -1, might be the cause of the problems
		// or actually -1 should be the correct choice
		int wordI = -1;
		int strtI = -1;
		int endI = -1;
		int hiEq = -1;
		for(int i = 1; i < allWords.length; i++){
			String toAdd = allWords[i];
			int l = toAdd.length();
			while(curr != null){
				wordI = curr.substr.wordIndex;
				strtI = curr.substr.startIndex;
				endI = curr.substr.endIndex;
				String c1 = allWords[wordI].substring(strtI, endI+1);
				String c2 = toAdd.substring(strtI);
				hiEq = compare(c1,c2);
				if(strtI > l){
					prev = curr;
					curr = curr.sibling;
					continue;
				}
				if(hiEq != -1){
					hiEq = hiEq + strtI;
				}
				if(hiEq == -1){
					prev = curr;
					curr = curr.sibling;
				}else{
					if(hiEq == endI){
						prev = curr;
						curr = curr.firstChild;
					}
					else if (hiEq < endI){
						prev = curr;
						break;
					}
				}
			}		   
			if(curr == null){
				Indexes indxS = new Indexes(i, (short)strtI, (short)(l-1));
				prev.sibling = new TrieNode(indxS, null, null);
			}else{
				Indexes indxFC = prev.substr;
				TrieNode temp = prev.firstChild;
				Indexes newI = new Indexes(indxFC.wordIndex, (short)(hiEq+1), indxFC.endIndex);
				indxFC.endIndex = (short)hiEq;
				prev.firstChild = new TrieNode(newI, null, null);
				prev.firstChild.firstChild = temp;
				Indexes indxS = new Indexes((short)i, (short)(hiEq+1), (short)(l-1));
				prev.firstChild.sibling = new TrieNode(indxS,null, null);
			}
			prev = root.firstChild;
			curr = prev;
			hiEq = -1;
			wordI = -1;
			strtI = -1;
			endI = -1;
		}
		
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return root;
	}
	private static int compare(String s1, String s2){
		int ret = 0;
		while(ret<s1.length() && ret<s2.length() && s1.charAt(ret)==s2.charAt(ret)){
			ret++;
		}
		ret = ret - 1;
		return ret;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire toAdd.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no toAdd in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		ArrayList<TrieNode> ret = new ArrayList<TrieNode>();
		// yet again probably dont need that
		// just gonna put it here anyway
		if(allWords.length == 0 || root == null || root.firstChild == null){
			return ret;
		}
		// REMOVE IF IT SEEMS TO CAUSE PROBLEMS
		///////////////////////////////////////////////////////////////////////////////////
		TrieNode curr = root.firstChild;
		while(curr != null){
			String substr = allWords[curr.substr.wordIndex].substring(0, (curr.substr.endIndex+1));
			if(substr.startsWith(prefix)){
				if(substr.compareTo(prefix) == 0 && curr.firstChild == null){
					// changed condition from and to or
					ret.add(curr);
					return ret;
				}else if(substr.compareTo(prefix) != 0 && curr.firstChild == null){
					ret.add(curr);
					return ret;
				}
				if(curr.firstChild != null){
					ret = addSubTrie(curr.firstChild, ret);
				}else{
					ret = addSubTrie(curr, ret);
				}
				break;
			}else if(prefix.startsWith(substr)){
				// old condition: hiEq != -1 || hiEq > prevEq
				// also: substr.charAt(i) == prefix.charAt(i)
				// new condition should be good
				curr = curr.firstChild;
				
			}else{
				curr = curr.sibling;
			}
		
		}
		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
		return ret;
	}
	
	private static ArrayList<TrieNode> addSubTrie(TrieNode root, ArrayList<TrieNode> arrL){
		ArrayList<TrieNode> ret = arrL;
		if(root.firstChild == null && root.sibling == null){
			ret.add(root);
			return ret;
		}
		if(root.firstChild == null && root.sibling != null){
			ret = addSubTrie(root.sibling, ret);
			ret.add(root);
			return ret;
		}
		if(root.firstChild != null){
			ret = addSubTrie(root.firstChild, ret);
		}
		if (root.sibling != null){
			ret = addSubTrie(root.sibling, ret);
		}

		return ret;
	}

	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode curr=root.firstChild; curr != null; curr=curr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(curr, indent+1, words);
		}
	}
 }
