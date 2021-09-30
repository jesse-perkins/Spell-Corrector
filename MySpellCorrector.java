import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Created by Jesse on 9/15/2016.
 */
public class MySpellCorrector implements ISpellCorrector {
    public Trie dictionary = new Trie();
    @Override
    public void useDictionary(String dictionaryFile) throws IOException {
        try {

            Scanner input = new Scanner(new File(dictionaryFile));
            String holder = null;
            while(input.hasNext())
            {
                holder = input.next();
                holder = holder.toLowerCase();
                dictionary.add(holder);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException {
        if(dictionary.find(inputWord) != null)
        {
            if(dictionary.find(inputWord).getValue() != 0)
            {
                return inputWord;
            }
        }
        TreeSet<String> word = new TreeSet<>();
        word.add(inputWord);
        TreeSet<String> distOne = new TreeSet<>();
        TreeSet<String> distTwo = new TreeSet<>();
        String bestWord = null;

        deletion(word, distOne);
        transposition(word, distOne);
        alteration(word, distOne);
        insertion(word, distOne);
        bestWord = findBestWord(distOne);

        if(bestWord == null)
        {
            deletion(distOne, distTwo);
            transposition(distOne, distTwo);
            alteration(distOne, distTwo);
            insertion(distOne, distTwo);
            bestWord = findBestWord(distTwo);
        }
        else {
            return bestWord;
        }
        if(bestWord == null)
        {
            throw new NoSimilarWordFoundException();
        }
        else{
            return bestWord;
        }
    }

    private void deletion(TreeSet<String> words, TreeSet<String> foundWords)
    {
        StringBuilder holder = new StringBuilder();
        for (String str:words) {
            for(int i = 0; i<str.length(); i++)
            {
                holder.append(str);
                holder.deleteCharAt(i);
                foundWords.add(holder.toString());
                holder.setLength(0);
            }
        }
    }

    private void transposition(TreeSet<String> words, TreeSet<String> foundWords)
    {
        StringBuilder holder = new StringBuilder();
        char charHolder;
        for (String str:words) {
            for(int i = 0; i<str.length()-1; i++)
            {
                holder.append(str);
                charHolder = holder.charAt(i);
                holder.setCharAt(i, holder.charAt(i+1));
                holder.setCharAt(i+1, charHolder);
                foundWords.add(holder.toString());
                holder.setLength(0);
            }
        }
    }

    private void alteration(TreeSet<String> words, TreeSet<String> foundWords)
    {
        StringBuilder holder = new StringBuilder();
        char charHolder;
        for (String str:words) {
            for(int i = 0; i<str.length(); i++)
            {
                for(int j = 0; j < 26; j++) {
                    holder.append(str);
                    charHolder = (char) (j+'a');
                    holder.setCharAt(i, charHolder);
                    foundWords.add(holder.toString());
                    holder.setLength(0);
                }
            }
        }
    }
    private void insertion(TreeSet<String> words, TreeSet<String> foundWords)
    {
        StringBuilder holder = new StringBuilder();
        char charHolder;
        for (String str:words) {
            for(int i = 0; i<str.length(); i++)
            {
                for(int j = 0; j < 26; j++) {
                    holder.append(str);
                    charHolder = (char) (j+'a');
                    holder.insert(i, charHolder);
                    foundWords.add(holder.toString());
                    holder.setLength(0);
                }
            }
            for(int j = 0; j < 26; j++) {
                holder.append(str);
                charHolder = (char) (j+'a');
                holder.append(charHolder);
                foundWords.add(holder.toString());
                holder.setLength(0);
            }
        }
    }

    private String findBestWord(TreeSet<String> inSet){
        String bestWord = null;
        int bestFreq = 0;
        int holderFreq = 0;
        for (String str: inSet) {
            if(dictionary.find(str) != null) {
                holderFreq = dictionary.find(str).getValue();
                if (bestFreq < holderFreq)
                {
                    bestWord = str;
                    bestFreq = holderFreq;
                }
            }
        }
        return bestWord;
    }
}
