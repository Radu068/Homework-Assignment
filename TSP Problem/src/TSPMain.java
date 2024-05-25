import java.util.Arrays;
import java.util.Random;

public class TSPMain {
    public static void main(String[] args) {
        // Provided Graph
        long[][] graph = {
                {0, 5, 0, 6, 0, 4, 0, 7},
                {5, 0, 2, 4, 3, 0, 0, 0},
                {0, 2, 0, 1, 0, 0, 0, 0},
                {6, 4, 1, 0, 7, 0, 0, 0},
                {0, 3, 0, 7, 0, 0, 6, 4},
                {4, 0, 0, 0, 0, 0, 3, 0},
                {0, 0, 0, 0, 6, 3, 0, 2},
                {7, 0, 0, 0, 4, 0, 2, 0},
        };

        // Random Graph Generation
        long n = 18L; // Number of cities
        long maxWeight = 10L; // Maximum weight for the edges
        long[][] randomGraph = generateRandomGraph((int) n, maxWeight);

        // Print the provided graph
        System.out.println("Provided Graph:");
        printGraph(graph);

        // Solve and print results for the provided graph
        solveAndPrintResults(graph, "provided");

        // Print the generated random graph
        System.out.println("\nGenerated Random Graph:");
        printGraph(randomGraph);

        // Solve and print results for the random graph
        solveAndPrintResults(randomGraph, "random");
    }

    private static void solveAndPrintResults(long[][] graph, String graphName) {
        double startTime, endTime;
        // Solve TSP for the graph using BFS
        startTime = System.nanoTime();
        TSP_BFS.ResultBFS resultBFS = TSP_BFS.tspBFS(graph);
        endTime = System.nanoTime();
        printResult(resultBFS, graphName, "BFS", (endTime - startTime) / 1e9);

        // Solve TSP for the graph using A*
        startTime = System.nanoTime();
        TSP_AStar.ResultAStar resultAStar = TSP_AStar.tspAStar(graph);
        endTime = System.nanoTime();
        printResult(resultAStar, graphName, "A*", (endTime - startTime) / 1e9);

        // Solve TSP for the graph using UCS
        startTime = System.nanoTime();
        TSP_UCS.ResultUCS resultUCS = TSP_UCS.tspUCS(graph);
        endTime = System.nanoTime();
        printResult(resultUCS, graphName, "UCS", (endTime - startTime) / 1e9);
    }

    private static void printResult(Object result, String graphName, String algorithm, double executionTime) {
        if (result instanceof TSP_BFS.ResultBFS) {
            TSP_BFS.ResultBFS res = (TSP_BFS.ResultBFS) result;
            System.out.printf("Minimum Cost for %s graph using %s: %d%n", graphName, algorithm, res.cost);
            System.out.printf("Route for %s graph using %s: %s%n", graphName, algorithm, res.route);
        } else if (result instanceof TSP_AStar.ResultAStar) {
            TSP_AStar.ResultAStar res = (TSP_AStar.ResultAStar) result;
            System.out.printf("Minimum Cost for %s graph using %s: %d%n", graphName, algorithm, res.cost);
            System.out.printf("Route for %s graph using %s: %s%n", graphName, algorithm, res.route);
        } else if (result instanceof TSP_UCS.ResultUCS) {
            TSP_UCS.ResultUCS res = (TSP_UCS.ResultUCS) result;
            System.out.printf("Minimum Cost for %s graph using %s: %d%n", graphName, algorithm, res.cost);
            System.out.printf("Route for %s graph using %s: %s%n", graphName, algorithm, res.route);
        }
        System.out.println("Execution Time: " + executionTime + " seconds");
        System.out.println();
    }

    private static void printGraph(long[][] graph) {
        for (long[] row : graph) {
            System.out.println(Arrays.toString(row));
        }
    }

    private static long[][] generateRandomGraph(int n, long maxWeight) {
        Random random = new Random();
        long[][] graph = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long weight = random.nextInt((int) maxWeight) + 1;
                graph[i][j] = weight;
                graph[j][i] = weight;
            }
        }
        return graph;
    }
}
