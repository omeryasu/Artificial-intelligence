import java.util.List;
import java.util.Queue;

/**
 * Abstract class Searcher.
 * Similar function will be implemented here.
 */
public abstract class Searcher implements ISearcher {

    protected Queue<XPuzzleBoard> openList;
    protected List<XPuzzleBoard> closedList;
    protected XPuzzleBoard initialBoard;
    protected int solCost;

    /**
     * runs the search method
     */
    @Override
    abstract public String search();

    /**
     * @return the number of the node evaluated in order to find the solution
     */
    @Override
    public int getNumberOfNodesEvaluated() {
        return closedList.size();
    }

    /**
     * @return The cost of the search
     */
    @Override
    public int solutionCost() {
        return solCost;
    }

    /**
     * @return the solution path represented by a string
     */
    public String solutionPath(XPuzzleBoard goalBoard) {
        StringBuilder solution = new StringBuilder();
        XPuzzleBoard cameFrom = goalBoard;
        while (cameFrom != null) {
            if(cameFrom.getMovement() != null)
                solution.append(cameFrom.getMovement());
            cameFrom = cameFrom.getCameFrom();
        }
        return solution.reverse().toString();}
}
