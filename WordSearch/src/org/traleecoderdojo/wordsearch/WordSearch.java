package org.traleecoderdojo.wordsearch;


import java.util.*;
import java.util.logging.Logger;

public class WordSearch {
    private final static Logger LOGGER = Logger.getLogger(WordSearch.class.getName());
    private static final int DEFAULT_SIZE = 10;
    private final Random rand = new Random();
    private final int numRows;
    private final int numCols;
    private final char[][] grid;
    private final Set<GridPosition> filledPositions = new HashSet<>();
    private final List<String> wordsAdded = new ArrayList<>();
    private final List<GridPosition> allPositions = new ArrayList<>();


    public WordSearch() {
        this(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    public WordSearch(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        grid = new char[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                allPositions.add(new GridPosition(row, col));
            }
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public WordSearch addWord(String searchWord) {
        String normalisedSearchWord = searchWord.toUpperCase();
        Collections.shuffle(allPositions);
        for (GridPosition pos : allPositions) {
            LOGGER.finer("Trying start position: " + pos);
            if (filledPositions.contains(pos) && charAt(pos.row, pos.col) != normalisedSearchWord.charAt(0)) {
                LOGGER.finer("Conflicting letter found at start position.");
                continue;
            }

            Direction availableDirection = getRandomAvailableDirection(pos, normalisedSearchWord);
            if (availableDirection != null) {
                placeWord(normalisedSearchWord, pos, availableDirection);
                wordsAdded.add(searchWord);
                return this;
            }
        }
        throw new RuntimeException("Could not add word '" + searchWord + "'");
    }

    public WordSearch addWords(String ... searchWords) {
        for (String searchWord : searchWords) {
            addWord(searchWord);
        }
        return this;
    }

    public WordSearch fill() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (grid[row][col] == '\0') {
                    grid[row][col] = (char) ('A' + rand.nextInt(26));
                }
            }
        }
        return this;
    }

    public char charAt(int row, int col) {
        return grid[row][col];
    }

    public char solutionCharAt(int row, int col) {
        if (filledPositions.contains(new GridPosition(row, col))) {
            return charAt(row, col);
        }
        return '-';
    }

    public List<String> getWords() {
        return wordsAdded;
    }

    private Direction getRandomAvailableDirection(GridPosition startPos, String searchWord) {
        List<Direction> availableDirections = new ArrayList<>(Arrays.asList(Direction.values()));
        Collections.shuffle(availableDirections);
        LOGGER.finer("Checking directions: " + availableDirections);
        Iterator<Direction> iter = availableDirections.iterator();
        int searchWordLength = searchWord.length();
        while (iter.hasNext()) {
            Direction currentDirection = iter.next();
            LOGGER.finer("Checking direction: " + currentDirection);
            boolean inGridBounds = true;
            switch (currentDirection) {
                case UP:
                    if (startPos.row + 1 - searchWordLength < 0) {
                        inGridBounds = false;
                    }
                    break;
                case UP_RIGHT:
                    if (startPos.row + 1 - searchWordLength < 0 || startPos.col + searchWordLength > numCols) {
                        inGridBounds = false;
                    }
                    break;
                case RIGHT:
                    if (startPos.col + searchWordLength > numCols) {
                        inGridBounds = false;
                    }
                    break;
                case DOWN_RIGHT:
                    if (startPos.row + searchWordLength > numRows || startPos.col + searchWordLength > numCols) {
                        inGridBounds = false;
                    }
                    break;
                case DOWN:
                    if (startPos.row + searchWordLength > numRows) {
                        inGridBounds = false;
                    }
                    break;
                case DOWN_LEFT:
                    if (startPos.row + searchWordLength > numRows || startPos.col + 1 - searchWordLength < 0) {
                        inGridBounds = false;
                    }
                    break;
                case LEFT:
                    if (startPos.col + 1 - searchWordLength < 0) {
                        inGridBounds = false;
                    }
                    break;
                case UP_LEFT:
                    if (startPos.row + 1 - searchWordLength < 0 || startPos.col + 1 - searchWordLength < 0) {
                        inGridBounds = false;
                    }
                    break;
            }
            if (!inGridBounds) {
                LOGGER.finer("Direction " + currentDirection + " is outside grid bounds, ignoring.");
                continue;
            }

            boolean pathBlocked = false;
            for (int i = 0; i < searchWordLength; i++) {
                GridPosition charPos = getPositionForCharacterAt(i, startPos, currentDirection);
                if (filledPositions.contains(charPos) && grid[charPos.row][charPos.col] != searchWord.charAt(i)) {
                    LOGGER.finer("Found path blocked at position: " + charPos);
                    pathBlocked = true;
                    break;
                }
            }
            if (!pathBlocked) {
                return currentDirection;
            }
        }
        return null;
    }

    private void placeWord(String searchWord, GridPosition startPos, Direction direction) {
        LOGGER.fine("Placing word '" + searchWord + "' starting from " + startPos + " in direction " + direction);
        char[] searchWordChars = searchWord.toCharArray();
        for (int i = 0; i < searchWord.length(); i++) {
            GridPosition charPos = getPositionForCharacterAt(i, startPos, direction);
            grid[charPos.row][charPos.col] = searchWordChars[i];
            filledPositions.add(new GridPosition(charPos.row, charPos.col));
        }
    }

    private GridPosition getPositionForCharacterAt(int searchWordIndex, GridPosition startPos, Direction direction) {
        int posRow = 0;
        int posCol = 0;
        switch (direction) {
            case UP:
                posRow = startPos.row - searchWordIndex;
                posCol = startPos.col;
                break;
            case UP_RIGHT:
                posRow = startPos.row - searchWordIndex;
                posCol = startPos.col + searchWordIndex;
                break;
            case RIGHT:
                posRow = startPos.row;
                posCol = startPos.col + searchWordIndex;
                break;
            case DOWN_RIGHT:
                posRow = startPos.row + searchWordIndex;
                posCol = startPos.col + searchWordIndex;
                break;
            case DOWN:
                posRow = startPos.row + searchWordIndex;
                posCol = startPos.col;
                break;
            case DOWN_LEFT:
                posRow = startPos.row + searchWordIndex;
                posCol = startPos.col - searchWordIndex;
                break;
            case LEFT:
                posRow = startPos.row;
                posCol = startPos.col - searchWordIndex;
                break;
            case UP_LEFT:
                posRow = startPos.row - searchWordIndex;
                posCol = startPos.col - searchWordIndex;
                break;
        }
        return new GridPosition(posRow, posCol);
    }

    private static class GridPosition {
        int row;
        int col;

        GridPosition(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GridPosition that = (GridPosition) o;

            return row == that.row && col == that.col;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + col;
            return result;
        }

        @Override
        public String toString() {
            return "(" + row + ", " + col + ')';
        }
    }

    private enum Direction {
        UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT
    }
}

