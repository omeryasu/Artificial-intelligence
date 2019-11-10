import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import static java.lang.Math.abs;
import static java.lang.Math.ceil;

/**
 * Created by omer on 21/11/2018.
 */
public class AStar extends Searcher {

    /**
     * constructor
     * @param initialBoard first board to run the algorithm on
     */
    public AStar(XPuzzleBoard initialBoard) {
        this.initialBoard = initialBoard;
        this.closedList = new LinkedList<>();
    }

    /**
     * runs the search method
     */
    @Override
    public String search() {
        PriorityQueue<XPuzzleBoard> openList = new PriorityQueue<>(new Heuristic());
        XPuzzleBoard currentBoard;
        openList.add(this.initialBoard);
        while (!openList.isEmpty()) {
            currentBoard = openList.poll();
            this.closedList.add(currentBoard);
            if (currentBoard.isGoalState()) {
                this.solCost = currentBoard.getDepth();
                return solutionPath(currentBoard);
            }
            openList.addAll(currentBoard.getSuccessors());
        }
        return null;
    }

    /**
     * Heuristic class for the heuristic function.
     */
    public class Heuristic implements Comparator<XPuzzleBoard> {
        /**
         * sums the tiles manhattan distance from the goal distance.
         * @param o1 first board
         * @param o2 second board
         * @return which board has less tile to moves for reaching the goal state (represented by int)
         */
        @Override
        public int compare(XPuzzleBoard o1, XPuzzleBoard o2) {
            int sum1 = 0, sum2 = 0, row, col, tileValue;

            // move over the tile and sums their manhattan distances.
            for (int i = 0; i < o1.getSize(); i++) {
                for (int j = 0; j < o1.getSize(); j++) {
                    //calculates the distance to the goal position of the tile in the [i,j] location
                    if ((tileValue = o1.getTile(i, j)) != 0) {
                        row = (int)(ceil((float) tileValue / o1.getSize())) - 1;
                        col = (tileValue % o1.getSize() + (o1.getSize() - 1)) % o1.getSize();
                        sum1 += abs(i - row) + abs(j - col);
                    }
                    if ((tileValue = o2.getTile(i, j)) != 0) {
                        row = (int) (ceil((float) tileValue / o1.getSize())) - 1;
                        col = (tileValue % o2.getSize() + (o2.getSize() - 1)) % o2.getSize();
                        sum2 += abs(i - row) + abs(j - col);
                    }
                }
            }
            return (o1.getDepth() + sum1 - (o2.getDepth() + sum2));
        }
    }
}
