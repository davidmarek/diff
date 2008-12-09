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
            a.addAll(Arrays.asList(new String[] {"x","a","a","a","a","a"}));
            List<String> b = new ArrayList<String>();
            b.addAll(Arrays.asList(new String[] {"y","a","b","c","e","f"}));

            LongestCommonSubsequence<String> x = new LongestCommonSubsequence<String>(a,b);
            List<SequenceElement<String>> lse = x.findDiff();
            for (SequenceElement<String> l : lse) {
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

            System.out.println("");
            
            LevenshteinDistance<String> ld = new LevenshteinDistance<String>(a, b);
            lse = ld.findDiff();
            for (SequenceElement<String> l : lse) {
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
            System.out.println(e);
        }
    }

}
