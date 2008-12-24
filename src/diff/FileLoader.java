package diff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * @author David Marek <david at davidmarek.cz>
 */
public class FileLoader {

    /**
     * Seznam radku zadaneho souboru.
     */
    protected LinkedList<String> fileLines;

    /**
     * Konstruktor, nacte zadany soubor.
     *
     * @param filename
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public FileLoader(String filename) throws FileNotFoundException, IOException {
        File file = new File(filename);
        fileLines = new LinkedList<String>();

        String line;
        BufferedReader bfr = new BufferedReader(new FileReader(file));
        while ((line = bfr.readLine()) != null) {
            fileLines.addLast(line);
        }
    }

    /**
     *
     * @return Seznam radku zadaneho souboru.
     */
    public List<String> getFileLines() {
        return fileLines;
    }

}
