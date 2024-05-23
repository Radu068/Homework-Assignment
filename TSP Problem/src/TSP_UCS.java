import java.util.*;

public class TSP_UCS {
    public static ResultUCS tspUCS(long[][] graph) {
        int n = graph.length;
        if (n == 0) {
            return new ResultUCS(-1, null);
        }

        long minCost = Long.MAX_VALUE;
        NodeUCS minCostNode = null;
        PriorityQueue<NodeUCS> pq = new PriorityQueue<>(Comparator.comparingLong(node -> node.cost));
        Map<String, Long> bestCost = new HashMap<>();

        pq.add(new NodeUCS(0, 1, 0, null));
        bestCost.put("0-1", 0L);

        while (!pq.isEmpty()) {
            NodeUCS currentNode = pq.poll();

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
                    long newCost = currentNode.cost + graph[currentNode.currentCity][i];
                    String key = i + "-" + (currentNode.visited | mask);

                    if (!bestCost.containsKey(key) || newCost < bestCost.get(key)) {
                        bestCost.put(key, newCost);
                        pq.add(new NodeUCS(i, currentNode.visited | mask, newCost, currentNode));
                    }
                }
            }
        }

        List<Long> route = new ArrayList<>();
        for (NodeUCS node = minCostNode; node != null; node = node.prev) {
            route.add(0, (long) node.currentCity);
        }

        return new ResultUCS(minCost == Long.MAX_VALUE ? -1 : minCost, route);
    }

    static class NodeUCS {
        int currentCity;
        int visited;
        long cost;
        NodeUCS prev;

        NodeUCS(int currentCity, int visited, long cost, NodeUCS prev) {
            this.currentCity = currentCity;
            this.visited = visited;
            this.cost = cost;
            this.prev = prev;
        }
    }

    static class ResultUCS {
        long cost;
        List<Long> route;

        public ResultUCS(long cost, List<Long> route) {
            this.cost = cost;
            this.route = route;
        }

        @Override
        public String toString() {
            return "Cost: " + cost + ", Route: " + route;
        }
    }
}
