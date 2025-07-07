
import data_structure.grahs.Adjacency;
import data_structure.grahs.DirectLabelGraph;
import data_structure.list.LinkedList;
import data_structure.grahs.UndirectLabelGraph;


public class SolucionadorLaberinto {

    
    public UndirectLabelGraph<String> mazeToGraph(char[][] maze) {
        int rows = maze.length;
        int cols = maze[0].length;
        UndirectLabelGraph<String> graph = new UndirectLabelGraph<>(rows * cols, String.class);


        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (maze[r][c] != '0') {
                    String label = r + "," + c;
                    int idx = r * cols + c;
                    graph.label_vertex(idx, label);
                }
            }
        }

       
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (maze[r][c] != '0') {
                    String label = r + "," + c;
                    for (int[] dir : new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }) {
                        int nr = r + dir[0], nc = c + dir[1];
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && maze[nr][nc] != '0') {
                            String neighborLabel = nr + "," + nc;
                            graph.insert_label(label, neighborLabel, 1.0f);
                        }
                    }
                }
            }
        }
        return graph;
    }

    public LinkedList<Prim2.Point> solve(char[][] maze) throws Exception {
        int rows = maze.length;
        int cols = maze[0].length;

        // Encuentra inicio y fin
        String startLabel = null, endLabel = null;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (maze[r][c] == 'S')
                    startLabel = r + "," + c;
                if (maze[r][c] == 'E')
                    endLabel = r + "," + c;
            }
        }
        if (startLabel == null || endLabel == null)
            return null;

        System.out.println("Inicio: " + startLabel + "  Fin: " + endLabel);

        DirectLabelGraph<String> graph = mazeToGraph(maze);

        
        int total = rows * cols;
        float[] dist = new float[total];
        String[] prev = new String[total];
        boolean[] visited = new boolean[total];

        for (int i = 0; i < total; i++)
            dist[i] = Float.POSITIVE_INFINITY;

        Integer startIdx = graph.getVertex(startLabel);
        Integer endIdx = graph.getVertex(endLabel);
        if (startIdx == null || endIdx == null) {
            System.out.println("No se encontró el índice de inicio o fin en el grafo.");
            return null;
        }
        dist[startIdx] = 0;


        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(startIdx);

        while (!queue.isEmpty()) {
         
            int minIdx = 0;
            float minDist = Float.POSITIVE_INFINITY;
            for (int i = 0; i < queue.getLength(); i++) {
                int idx = queue.get(i);
                if (!visited[idx] && dist[idx] < minDist) {
                    minDist = dist[idx];
                    minIdx = i;
                }
            }
            int u = queue.get(minIdx);
            queue.delete(minIdx);

            if (visited[u])
                continue;
            visited[u] = true;

            String uLabel = graph.getLabel(u);
            LinkedList<Adjacency> adj = graph.adjacencies(u);
            Adjacency[] neighbors = adj.toArray();
            for (Adjacency ad : neighbors) {
                int v = ad.getdestiny();
                if (!visited[v]) {
                    float alt = dist[u] + ad.getWeight();
                    if (alt < dist[v]) {
                        dist[v] = alt;
                        prev[v] = uLabel;
                        queue.add(v);
                    }
                }
            }
        }

        // Reconstruye el camino
        LinkedList<Prim2.Point> path = new LinkedList<>();
        String curr = graph.getLabel(endIdx);
        while (curr != null && !curr.equals(startLabel)) {
            String[] parts = curr.split(",");
            path.addFirst(new Prim2.Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), null));
            curr = prev[graph.getVertex(curr)];
        }
        // Agrega el inicio
        if (curr != null) {
            String[] parts = curr.split(",");
            path.addFirst(new Prim2.Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), null));
        }

       
        if (path.getLength() > 0) {
            Prim2.Point last = path.get(path.getLength() - 1);
            String[] endParts = endLabel.split(",");
            int endR = Integer.parseInt(endParts[0]);
            int endC = Integer.parseInt(endParts[1]);
            if (last.r != endR || last.c != endC) {
                path.addLast(new Prim2.Point(endR, endC, null));
            }
        }

        System.out.println("Tamaño del camino: " + path.getLength());
        for (int i = 0; i < path.getLength(); i++) {
            Prim2.Point p = path.get(i);
            System.out.println("Paso " + i + ": (" + p.r + "," + p.c + ")");
        }

        return path;
    }
}