import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * Main class
 */
public class Java_ex1 {

    private static final String INPUT = "input.txt";
    private static final String OUTPUT = "output.txt";

    public static void main(String[] args) {
        File file = new File(INPUT);
        int algoType, boardSize;
        String initialStat;

        //read the input file and if failed to do so print the reason and close the program.
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            algoType = Integer.valueOf(br.readLine());
            boardSize = Integer.valueOf(br.readLine());
            initialStat = br.readLine();
        } catch (IOException e1) {
            System.out.println("Cannot load " + INPUT + " file");
            return;
        }
        Searcher searcher = null;
        XPuzzleBoard board = new XPuzzleBoard(initialStat, boardSize);
        //choose the algorithm according to the input file.
        switch (algoType){
            case 1:
                searcher = new IDS(board);
                break;
            case 2:
                searcher = new BFS(board);
                break;
            case 3:
                searcher = new AStar(board);
                break;
            default:
                System.out.println("Algorithm number does not exit.");
                return;
        }
        //search for the solution and save it into a string
        String s = searcher.search() + " " + searcher.getNumberOfNodesEvaluated() + " " + searcher.solutionCost();

        //write the outcome of the search to the output file. in case of and erorr prints the reason and close the program
        try {
            PrintWriter writer = new PrintWriter(OUTPUT);
            writer.print(s);
            writer.close();
        }catch (IOException e) {
            System.out.println("Failed writing. Error: " + e.getMessage());
        }
    }
}
