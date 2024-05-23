import java.util.*;

public class TSP_AStar {
    public static ResultAStar tspAStar(long[][] graph) {
        int n = graph.length;
        long minCost = Long.MAX_VALUE;
        Node minCostNode = null;
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingLong(node -> node.f));
        Map<String, Long> bestCost = new HashMap<>();
        Node startNode = new Node(0, 1, 0L, null, estimateHeuristic(0, 1, graph));
        pq.add(startNode);
        bestCost.put("0-1", 0L);

        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();

            if (currentNode.visited == (1 << n) - 1) {
                long lastLegCost = graph[currentNode.currentCity][0];
                if (lastLegCost > 0) {
                    long totalCost = currentNode.cost + lastLegCost;
                    if (totalCost < minCost) {
                        minCost = totalCost;
                        minCostNode = currentNode;
                    }
                }
                continue;
            }

            for (int i = 0; i < n; i++) {
                int mask = 1 << i;
                if ((currentNode.visited & mask) == 0 && graph[currentNode.currentCity][i] > 0) {
                    int newVisited = currentNode.visited | mask;
                    long newCost = currentNode.cost + graph[currentNode.currentCity][i];
                    long heuristic = estimateHeuristic(i, newVisited, graph);
                    long newF = newCost + heuristic;
                    String key = i + "-" + newVisited;

                    if (!bestCost.containsKey(key) || newCost < bestCost.get(key)) {
                        bestCost.put(key, newCost);
                        pq.add(new Node(i, newVisited, newCost, currentNode, newF));
                    }
                }
            }
        }

        List<Long> route = new ArrayList<>();
        for (Node node = minCostNode; node != null; node = node.prev) {
            route.add(0, (long) node.currentCity);
        }

        return new ResultAStar(minCost == Long.MAX_VALUE ? -1 : minCost, route);
    }

    static long estimateHeuristic(int currentCity, int visited, long[][] graph) {
        int n = graph.length;
        long mstCost = 0;
        boolean[] inMST = new boolean[n];
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(edge -> edge[2]));

        for (int i = 0; i < n; i++) {
            if (i != currentCity && (visited & (1 << i)) == 0 && graph[currentCity][i] > 0) {
                pq.add(new long[]{currentCity, i, graph[currentCity][i]});
            }
        }

        inMST[currentCity] = true;
        int edgesUsed = 0;
        while (!pq.isEmpty() && edgesUsed < n - 1) {
            long[] edge = pq.poll();
            int v = (int) edge[1];
            long weight = edge[2];
            if (!inMST[v]) {
                inMST[v] = true;
                mstCost += weight;
                edgesUsed++;
                for (int i = 0; i < n; i++) {
                    if (!inMST[i] && (visited & (1 << i)) == 0 && graph[v][i] > 0) {
                        pq.add(new long[]{v, i, graph[v][i]});
                    }
                }
            }
        }

        long minEdgeFromCurrent = Long.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            if ((visited & (1 << i)) == 0 && graph[currentCity][i] > 0) {
                minEdgeFromCurrent = Math.min(minEdgeFromCurrent, graph[currentCity][i]);
            }
        }

        long minEdgeToStart = Long.MAX_VALUE;
        for (int i = 1; i < n; i++) {
            if ((visited & (1 << i)) == 0 && graph[i][0] > 0) {
                minEdgeToStart = Math.min(minEdgeToStart, graph[i][0]);
            }
        }

        mstCost += minEdgeFromCurrent + minEdgeToStart;
        return mstCost;
    }

    static class Node {
        int currentCity;
        int visited;
        long cost;
        long f;
        Node prev;

        Node(int currentCity, int visited, long cost, Node prev, long f) {
            this.currentCity = currentCity;
            this.visited = visited;
            this.cost = cost;
            this.prev = prev;
            this.f = f;
        }
    }

    static class ResultAStar {
        long cost;
        List<Long> route;

        public ResultAStar(long cost, List<Long> route) {
            this.cost = cost;
            this.route = route;
        }
    }
}
