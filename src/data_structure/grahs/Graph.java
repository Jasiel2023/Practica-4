package data_structure.grahs;
import data_structure.list.LinkedList;

public abstract class Graph {
    
    public abstract Integer nro_vertex();
    public abstract Integer nro_edge();
    public abstract Adjacency exists_edge(Integer o, Integer d);
    public abstract Float weight_edge(Integer o, Integer d);
    public abstract void insert(Integer o, Integer d);
    public abstract void insert(Integer o, Integer d, Float weight);
    public abstract LinkedList<Adjacency> adjacencies(Integer o);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= nro_vertex(); i++){
            sb.append("Vertex =").append(String.valueOf(i)).append("\n");
            LinkedList<Adjacency> list = adjacencies(i);
            if (!list.isEmpty()) {
                Adjacency[] matrix = list.toArray();
                for (Adjacency ad : matrix) {
                    sb.append("\tAdjancency = ").append("\n").append("\tVertex = ")
                    .append(String.valueOf(ad.getdestiny()));
                    if (!ad.getWeight().isNaN()) {
                        sb.append(" Weight = "+ad.getWeight().toString()).append("\n");
                        
                    }
                 }

              }
        
        }
        return sb.toString();
    }
}