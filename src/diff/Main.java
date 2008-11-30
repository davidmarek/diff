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
            a.addAll(Arrays.asList(new String[] {}));
            List<String> b = new ArrayList<String>();
            b.addAll(Arrays.asList(new String[] {"a","b","c","e","f"}));

            LongestCommonSubsequence<String> x = new LongestCommonSubsequence<String>(a,b);
            List<LongestCommonSubsequence<String>.SequenceElement> lse = x.findDiff();
            for (LongestCommonSubsequence<String>.SequenceElement l : lse) {
                switch(l.getStatus()) {
                    case ADDED:
                        System.out.print("+ ");
                        break;
                    case REMOVED:
                        System.out.print("- ");
                        break;
                    case UNTOUCHED:
                        System.out.print("  ");
                        break;
                }
                System.out.println(l.getElement());
            }

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
