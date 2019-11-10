import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * XPuzzleBoard class.
 * X-puzzle representation and functionality.
 */
public class XPuzzleBoard {

    private int size;
    private String movement;
    private XPuzzleBoard cameFrom;
    private int[][] board;
    private int depth;

    /**
     * Constructor
     * @param initState the root board. the root board to work on.
     * @param size the board size (sizeXsize matrix).
     */
    public XPuzzleBoard(String initState, int size) {
        this.cameFrom = null;
        this.movement = null;
        this.size = size;
        this.depth = 0;
        initializeRoot(initState);
    }

    /**
     * Second constructor for inner use. creates the root board successors
     * @param cameFrom the board the the successor was created from.
     * @param movement the move to do on the new board
     */
    private XPuzzleBoard(XPuzzleBoard cameFrom, String movement) {
        this.cameFrom = cameFrom;
        this.movement = movement;
        this.size = cameFrom.getSize();
        this.depth = cameFrom.getDepth() + 1;
        this.movement = movement;
        initializeBoard();
    }

    /**
     *  taking a string which represents the state of the board.
     * @param state the init state of the board.
     */
    private void initializeRoot(String state) {
        //splits the stings into number
        List<Integer> numbersForInitialize = Arrays.stream(state.trim().split("-")).map(Integer::parseInt).collect(Collectors.toList());
        this.board = new int[this.size][this.size];
        // put the numbers in the board.
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.board[i][j] = numbersForInitialize.get(this.size * i + j);
            }
        }
    }

    /**
     * initialize the board. copy cameFrom board then makes the move.
     */
    private void initializeBoard() {
        this.board = new int[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.board[i][j] = this.cameFrom.getTile(i,j);
            }
        }
        makeMove(movement.toLowerCase());
    }

    /**
     *
     * @param row row index
     * @param col column index
     * @return the value of the board in [row,col] location.
     */
    public int getTile(int row, int col) {
        return this.board[row][col];
    }

    /**
     * make the asked move on the board
     * @param move string representing a move to make (up, down, left or right)
     */
    private void makeMove(String move) {

        Point p = emptyTileLocation();
        int row = p.x;
        int col = p.y;

        //checks the string and make the move if its valid.
        if (move.equals("up") && (row < this.size - 1)) {
            swapTiles(row,col,(row + 1), col);
        }
        else if (move.equals("down") && row > 0) {
            swapTiles(row,col,(row - 1), col);
        }
        else if (move.equals("left") && col < this.size - 1) {
            swapTiles(row,col,row, (col + 1));
        }
        else if (move.equals("right") && col > 0) {
            swapTiles(row,col,row, (col - 1));
        }
    }

    /**
     * replace between to tiles in the board given their location
     * @param x first row index
     * @param y first col index
     * @param i second row index
     * @param j second col index
     */
    private void swapTiles(int x, int y, int i, int j) {
        int temp = this.board[x][y];
        this.board[x][y] = this.board[i][j];
        this.board[i][j] = temp;
    }

    /**
     *
     * @return the firs char (in capital) of the movement that was applied on the board.
     */
    public String getMovement() {
        if(movement == null)
            return null;
        switch (this.movement) {
            case "up":
                return "U";
            case "down":
                return "D";
            case "left":
                return "L";
            case "right":
                return "R";
            default:
                return null;
        }
    }

    /**
     *
     * @return the location of the zero tile.
     */
    private Point emptyTileLocation() {
        int row, col = 0;
        Point p = new Point();
        //search for the tile in the board and breaks when found.
        for (row = 0; row < this.size; row++) {
            for (col = 0; col < this.size; col++) {
                if (getTile(row, col) == 0) {
                    p.x = row;
                    p.y = col;
                    break;
                }
            }
        }
        return p;
    }

    /**
     *
     * @return list of this board successors.
     */
    public List<XPuzzleBoard> getSuccessors() {

        Point p = emptyTileLocation();
        int row = p.x;
        int col = p.y;
        List<XPuzzleBoard> successors = new LinkedList<>();
        // checks for each direction if we have a valid move and adds it to the list if so.
        if (row < this.size - 1) {
            successors.add(new XPuzzleBoard(this, "up"));
        }
        if (row > 0) {
            successors.add(new XPuzzleBoard(this, "down"));
        }
        if (col <  this.size - 1 ) {
            successors.add(new XPuzzleBoard(this, "left"));
        }
        if (col > 0) {
            successors.add(new XPuzzleBoard(this, "right"));
        }
        return  successors;
    }

    /**
     *
     * @return the board size
     */
    public int getSize() {
        return this.size;
    }

    /**
     *
     * @return the depth of the board
     */
    public int getDepth() {
        return this.depth;
    }

    /**
     *
     * @return the board which this board was created (null for the root)
     */
    public XPuzzleBoard getCameFrom() {
        return cameFrom;
    }

    /**
     *  checks all of the tiles by order and compare them from 1
     *  till the board size (the last one compared to 0)
     * @return true if the board reached the goal state false other wise.
     */
    public boolean isGoalState() {
        int count = 1;
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if(i == this.size - 1 && j == this.size - 1)
                    count = 0;
                if (this.board[i][j] != count)
                    return false;
                count++;
            }
        }
        return true;
    }
}
