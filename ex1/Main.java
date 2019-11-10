import java.io.*;

/**
 * Created by omer on 16/11/2018.
 */
public class Main {

    public static void main(String[] args) {
        String fileName = "input.txt";
        File file = new File(fileName);
        int algoType, boardSize;
        String initialStat;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            algoType = Integer.valueOf(br.readLine());
            boardSize = Integer.valueOf(br.readLine());
            initialStat = br.readLine();
        } catch (IOException e1) {
            System.out.println("Cannot load " + fileName + " file");
            return;
        }
        Searcher searcher = new Searcher() {
            @Override
            public String search() {
                return null;
            }
        };
        XPuzzleBoard board = new XPuzzleBoard(initialStat, boardSize);
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
        String s;
        s = searcher.search() + " " + searcher.getNumberOfNodesEvaluated() + " " + searcher.solutionCost();

        try {
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            writer.println(s);
            writer.close();
        }catch (Exception e) {
            System.out.println("Failed writing. Error: " + e.getMessage());
        }
    }
}
