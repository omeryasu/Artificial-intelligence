import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *  IDS algorithm class.
 */
public class IDS extends Searcher{

    /**
     * constructor
     * @param initialBoard  first board (root) to run the algorithm on
     */
    public IDS(XPuzzleBoard initialBoard) {
        this.initialBoard = initialBoard;
    }

    /**
     * runs the search method
     */
    @Override
    public String search() {
        Stack<XPuzzleBoard> openList = new Stack<>();
        this.closedList = new LinkedList<>();
        //iteration depth that we can go into.
        int iterator = 0;
        XPuzzleBoard board;
        List<XPuzzleBoard> successors;

        while(true) {
            openList.clear();
            this.closedList.clear();
            openList.push(this.initialBoard);
            //gets a board from the open list and cheack if its the goal state
            while (!openList.isEmpty()) {
                board = openList.pop();
                this.closedList.add(board);
                if (board.isGoalState()) {
                    this.solCost = board.getDepth();
                    return solutionPath(board);
                }
                //if the board depth is lower then our iteration bound, add it successors to the open list.
                if (board.getDepth() < iterator) {
                    successors = board.getSuccessors();
                    Collections.reverse(successors);
                    openList.addAll(successors);
                }
            }
            //if we didn't find the answer we increment iterator so we can check deeper
            iterator++;
        }
    }
}
