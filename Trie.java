import java.util.TreeSet;

/**
 * Created by Jesse on 9/15/2016.
 */
public class Trie implements ITrie{
    public int nodeCount = 1;
    public Node rootNode = new Node();
    public TreeSet<String> dictionary = new TreeSet<>();

    @Override
    public int hashCode() {
        return getNodeCount() * 131 * getWordCount();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (String str : dictionary) {
            out.append(str);
            out.append('\n');
        }
        return out.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
        {
            return false;
        }
        if(this.getClass() != obj.getClass())
        {
            return false;
        }
        Trie inTrie = (Trie) obj;
        if(inTrie.getNodeCount() != this.getNodeCount())
        {
            return false;
        }
        if (inTrie.getWordCount() != this.getWordCount())
        {
            return false;
        }
        return this.rootNode.equals(inTrie.rootNode);
    }

    @Override
    public void add(String word) {
        Node currentNode = rootNode;
        dictionary.add(word);
        for(int i = 0; i< word.length(); i++)
        {
            int index = word.charAt(i) - 'a';
            if (currentNode.letterArray[index] == null)
            {
                currentNode.letterArray[index] = new Node();
                nodeCount++;
            }
            currentNode = currentNode.letterArray[index];
        }
        currentNode.freq++;
    }

    @Override
    public INode find(String word) {
        word = word.toLowerCase();
        Node currentNode = rootNode;
        for(int i = 0; i < word.length(); i++)
        {
            int index = word.charAt(i) - 'a';
            if (currentNode.letterArray[index] == null)
            {
                return null;
            }
            currentNode = currentNode.letterArray[index];
        }
        if(currentNode.freq == 0)
        {
            return null;
        }
        return currentNode;
    }

    @Override
    public int getWordCount() {
        return dictionary.size();
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }
    public class Node implements ITrie.INode{
        public Node(){};
        public int freq = 0;
        public Node[] letterArray= new Node[26];
        public boolean equals(Node n)
        {
            if(freq != n.freq){return false;}
            for(int i = 0; i < 26; i++)
            {
                if(letterArray[i] == null && n.letterArray[i]!= null){return false;}
                if(letterArray[i] != null && n.letterArray[i]== null){return false;}
                if(letterArray[i] == null && n.letterArray[i]== null){}
                else if (!(letterArray[i].equals(n.letterArray[i]))){return false;}
            }
            return true;
        }
        @Override
        public int getValue() {
            return freq;
        }
    }
}
