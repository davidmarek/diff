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

        try {

            File f1 = new File(args[0]);
            File f2 = new File(args[1]);

            CreateDiff cd = new CreateUnifiedDiff(f1, f2);
            System.out.print(cd);

        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

}
