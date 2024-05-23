import java.util.*;

public class TSP_BFS {
    public static ResultBFS tspBFS(long[][] graph) {
        int n = graph.length;
        long minCost = Long.MAX_VALUE;
        Node minCostNode = null;
        Queue<Node> queue = new LinkedList<>();
        Map<String, Long> bestCost = new HashMap<>();

        queue.add(new Node(0, 1, 0L, null));
        bestCost.put("0-1", 0L);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();

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
                    String key = i + "-" + newVisited;

                    if (!bestCost.containsKey(key) || newCost < bestCost.get(key)) {
                        bestCost.put(key, newCost);
                        queue.add(new Node(i, newVisited, newCost, currentNode));
                    }
                }
            }
        }

        List<Long> route = new ArrayList<>();
        for (Node node = minCostNode; node != null; node = node.prev) {
            route.add(0, (long) node.currentCity);
        }

        return new ResultBFS(minCost == Long.MAX_VALUE ? -1 : minCost, route);
    }

    static class Node {
        int currentCity;
        int visited;
        long cost;
        Node prev;

        Node(int currentCity, int visited, long cost, Node prev) {
            this.currentCity = currentCity;
            this.visited = visited;
            this.cost = cost;
            this.prev = prev;
        }
    }

    static class ResultBFS {
        long cost;
        List<Long> route;

        public ResultBFS(long cost, List<Long> route) {
            this.cost = cost;
            this.route = route;
        }
    }
}
