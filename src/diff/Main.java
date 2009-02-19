package diff;

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
            ConsoleApp app = new ConsoleApp();
            app.parseArgs(args);
            app.run();
        } catch (Exception e) {
            System.err.println(e);
        }

    }

}
