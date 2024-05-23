import java.util.concurrent.ThreadLocalRandom;

class RANDOMGRAPH {
    public static long[][] generateRandomGraph(long n, long maxWeight) {
        // Using ThreadLocalRandom for potentially better performance in multithreaded environments
        long[][] graph = new long[(int)n][(int)n];
        // Parallel processing to fill the graph matrix if n is sufficiently large
        for (long i = 0; i < n; i++) {
            for (long j = 0; j < n; j++) {
                if (i != j) {
                    graph[(int)i][(int)j] = ThreadLocalRandom.current().nextLong(1, maxWeight + 1); // Random weight between 1 and maxWeight
                }
            }
        }
        return graph;
    }
}
