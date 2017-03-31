package org.traleecoderdojo.wordsearch;

public class WordSearchCreator {

    public static void createSampleWordSearch() {
        WordSearch wordSearch = new WordSearch()
                .addWord("tralee")
                .addWord("coderdojo")
                .addWord("ninjas")
                .addWord("are")
                .addWord("super")
                .fill();
        WordSearchPrinter.print(wordSearch);
    }

    public static void createClassWordSearchAndShowSolution() {
        String[] searchWords = new String[] {
                "Jack",
                "JohnJoe",
                "Patrick",
                "Oisin",
                "Clodagh",
                "Isobel",
                "Kelly",
                "Hazel",
                "Natasha",
                "Sarah",
                "Micheal",
                "Denis",
                "Rebecca",
                "Dorcas",
                "Kacper",
                "Jason",
                "Alise",
                "Shea"
        };
        WordSearch ws = new WordSearch(13,13).addWords(searchWords).fill();
        WordSearchPrinter.print(ws);
        WordSearchPrinter.print(ws, true);
    }

    public static void main(String[] args) {
        createClassWordSearchAndShowSolution();
    }
}
