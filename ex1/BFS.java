import java.util.LinkedList;

/**
 * BFS algorithm class.
 */
public class BFS extends Searcher{

    /**
     * @param initialBoard  first board to run the algorithm on
     */
    public BFS(XPuzzleBoard initialBoard) {
        this.initialBoard = initialBoard;
        this.openList = new LinkedList<>();
        this.closedList = new LinkedList<>();
    }

    /**
     * runs the search method
     */
    @Override
    public String search() {
        this.openList.add(this.initialBoard);
        XPuzzleBoard currentBoard;
        this.solCost = 0;
        while (!this.openList.isEmpty()) {
            //pulls the next board to check from the queue
            currentBoard = this.openList.poll();
            this.closedList.add(currentBoard);
            //if the goal state is found returns the solution.
            if(currentBoard.isGoalState()){
                return solutionPath(currentBoard);
            }
            //add all successors to the open list
            this.openList.addAll(currentBoard.getSuccessors());
        }
        return null;
    }
}