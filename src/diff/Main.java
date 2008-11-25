package diff;

import java.util.*;

/**
 *
 * @author David Marek <H4wk.cz at gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            List<String> a = new ArrayList<String>();
            a.addAll(Arrays.asList(new String[] {"a","b","c","d","e","f"}));
            List<String> b = new ArrayList<String>();
            b.add("c"); b.add("f");

            LongestCommonSubsequence<String> x = new LongestCommonSubsequence<String>(a,b);

            int i = x.findSubsequence();

            System.out.println(x.getFirstListIndexes());
            System.out.println(x.getSecondListIndexes());
            System.out.println(i);

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
