package diff;

import java.io.*;
import java.util.*;
import java.text.*;

/**
 * Computes longest common subsequence between two files.
 * @author David Marek <H4wk.cz at gmail.com>
 */
public class BadDiff {
    
    private List<String> firstFile;
    private List<String> secondFile;
    
    private int table[][];
    private List<String> lcs;
    private List<Integer> lcs_numbers_first;
    private List<Integer> lcs_numbers_second;
    
    private String firstHeader;
    private String secondHeader;
    
    /**
     * Reads input files.
     * @param firstFile First file to be compared
     * @param secondFile Second file to be compared
     */
    public BadDiff(String firstFilename, String secondFilename) {
	try {
	    /*
	     * Read first file
	     */
            File ff = new File(firstFilename);
            String fd = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ROOT).format(new Date(ff.lastModified()));
            this.firstHeader = new String("--- "+ff.getName()+"\t"+fd);
            System.out.println(this.firstHeader);
            
	    BufferedReader first = new BufferedReader(new FileReader(ff));
	    this.firstFile = new ArrayList<String>();
	    String line;
	    while ((line = first.readLine()) != null) {
		this.firstFile.add(line);
	    }
	    
	    /*
	     * Read second file
	     */
	    BufferedReader second = new BufferedReader(new FileReader(secondFilename));
	    this.secondFile = new ArrayList<String>();
	    while ((line = second.readLine()) != null) {
		this.secondFile.add(line);
	    }
	    
	/*
	 * Files were not found.
	 */
	} catch (FileNotFoundException e) {
	    System.err.println(e.getMessage());
	/*
	 * Could not readLine
	 */
	} catch (IOException e) {
	    System.err.println(e.getMessage());
	}
    }
    
    /**
     * Inits the table used for computing lcs.
     */
    protected void initTable() {
	int firstLen = firstFile.size();
	int secondLen = secondFile.size();
	
	table = new int[firstLen+1][secondLen+1];
	
	for (int i = 0; i < firstLen+1; ++i) {
	    table[i][0] = 0;
	}
	
	for (int i = 0; i < secondLen+1; ++i) {
	    table[0][i] = 0;
	}
    }
    
    /**
     * Dynamically computes the length of longest common subsequence of two files.
     * @return Length of the lcs.
     */
    protected int computeLength() {
	this.initTable();
	int firstLen = firstFile.size();
	int secondLen = secondFile.size();
	
	for (int i = 0; i < firstLen; ++i) {
	    for (int j = 0; j < secondLen; ++j) {
		if (firstFile.get(i).equals(secondFile.get(j))) {
		    table[i+1][j+1] = table[i][j] + 1;
		} else {
		    table[i+1][j+1] = table[i][j+1] > table[i+1][j] ? table[i][j+1] : table[i+1][j];
		}
	    }
	}
	
	return table[firstLen][secondLen];
    }
    
    /**
     * Finds lines that are common for both files.
     */
    protected void findSubsequence() {
        this.lcs = new LinkedList<String>();
        this.lcs_numbers_first = new LinkedList<Integer>();
        this.lcs_numbers_second = new LinkedList<Integer>();
        
        int n = firstFile.size();
        int m = secondFile.size();
        
        while (this.table[n][m] != 0) {
            
            /*
             * Strings are indexed from 0, table from 1
             */
            if (firstFile.get(n-1).equals(secondFile.get(m-1))) {
                this.lcs.add(0, firstFile.get(n-1));
                this.lcs_numbers_first.add(0, n-1);
                this.lcs_numbers_second.add(0, m-1);
                --n; --m;
            } else if (table[n][m-1] > table[n-1][m]) {
                --m;
            } else {
                --n;
            }
        }
    }
    
    /**
     * Makes array of lines with annotations + and - to mark lines that have been
     * added or deleted.
     * @return Merged both files with marks which lines have been addded or deleted.
     */
    public List<String> makeDiff() {
        this.computeLength();
        this.findSubsequence();
        
        List<String> diff = new LinkedList<String>();
        
        int lastFirst = 0;
        int i,j;
        
        int lastSecond = 0;
        
        /*
         * Each line in lcs could be on different place in both files. Indexes for 
         * first file are in lcs_numbers_first. Indexes for second file are in 
         * lcs_numbers_second. Size of these lists is same.
         */
        while (this.lcs_numbers_first.size() > 0) {
            
            /*
             * Finds all removed lines in first file(added to second) up to next
             * common lines.
             */
            for (i = lastFirst; i < lcs_numbers_first.get(0); ++i) {
                diff.add("-"+firstFile.get(i));
            }
            
            /*
             * Finds all added lines to first file(removed in second) up to next
             * common lines.
             */
            for (j = lastSecond; j < lcs_numbers_second.get(0); ++j) {
                diff.add("+"+secondFile.get(j));
            }
            
            /*
             * Adds all common lines.
             */
            diff.add(" "+firstFile.get(lcs_numbers_first.get(0)));
            
            /*
             * Removes added common lines.
             */
            lcs_numbers_first.remove(0);
            lcs_numbers_second.remove(0);
            
            lastFirst = i+1;
            lastSecond = j+1;
        }
        
        /*
         * There are lines after last common line
         */
        for (i = lastFirst; i < lcs.size(); ++i) {
                diff.add("-"+firstFile.get(i));
        }
        
        for (j = lastSecond; j < lcs.size(); ++j) {
                diff.add("+"+secondFile.get(i));
        }
        
        return diff;
    }
    
    public void printDiff(List<String> diff) {
        for (String s : diff) {
            System.out.println(s);
        }
    }
}
