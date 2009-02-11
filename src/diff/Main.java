package diff;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author David Marek <H4wk.cz at gmail.com>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
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
        } catch(Exception e) {
            System.out.println(e);
        }*/

        try {

            File f1 = new File("test_A.txt");
            File f2 = new File("test_B.txt");

            CreateDiff cd = new CreateUnifiedDiff(f1, f2);
            System.out.print(cd);

        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

}
