/**
 * Interface ISearcher.
 * Interface for a searching algorithms.
 */
public interface ISearcher {

    /**
     * runs the search method
     */
    public String search();

    /**
     *
     * @return the number of the node evaluated in order to find the solution
     */
    public int getNumberOfNodesEvaluated();

    /**
     *
     * @return The cost of the search
     */
    public int solutionCost();

}
