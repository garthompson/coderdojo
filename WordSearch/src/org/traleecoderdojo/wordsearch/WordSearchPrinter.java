package org.traleecoderdojo.wordsearch;


public class WordSearchPrinter {

    public static void print(WordSearch wordSearch) {
        print(wordSearch, false);
    }

    public static void print(WordSearch wordSearch, boolean solution) {
        int numRows = wordSearch.getNumRows();
        int numCols = wordSearch.getNumCols();

        String[] contentLines = new String[numRows];
        for (int row = 0; row < numRows; row++) {
            // left border
            String contentLine = "| ";
            // content
            for (int col = 0; col < numCols; col++) {
                contentLine += solution ? wordSearch.solutionCharAt(row, col) : wordSearch.charAt(row, col);
                contentLine += " ";
            }
            // right border
            contentLine += "|";

            contentLines[row] = contentLine;
        }
        int numOutputCharsPerRow = contentLines[0].length();

        // title
        String title = "WordSearch";
        if (solution) {
            title += " Solution";
        } else {
            title += " Puzzle";
        }
        System.out.println(title);

        // top border
        String topBorder = "┌";
        for (int i = 0; i < numOutputCharsPerRow - 2; i++) {
            topBorder += "-";
        }
        topBorder += "┐";
        System.out.println(topBorder);

        // body
        for (String line : contentLines) {
            System.out.println(line);
        }

        // bottom border
        String bottomBorder = "└";
        for (int i = 0; i < numOutputCharsPerRow - 2; i++) {
            bottomBorder += "-";
        }
        bottomBorder += "┘";
        System.out.println(bottomBorder);

        // words
        if (!solution) {
            System.out.println("Words:");
            for (String word : wordSearch.getWords()) {
                System.out.println("\t" + word);
            }
        }

        System.out.println();
    }

}
