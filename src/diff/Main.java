package diff;

/**
 *
 * @author @author David Marek <david at davidmarek.cz>
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            ConsoleApp app = new ConsoleApp();
            app.parseArgs(args);
            app.run();
        } catch (Exception e) {
            System.err.println(e);
        }

    }

}
