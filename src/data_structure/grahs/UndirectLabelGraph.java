package data_structure.grahs;


import data_structure.list.LinkedList;

public class UndirectLabelGraph<E> extends DirectLabelGraph<E> {

    public UndirectLabelGraph(Integer nro_vertex, Class clazz) {
        super(nro_vertex, clazz);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void insert_label(E o, E d, Float weight) {
        if (isLabelGraph()) {
            insert(getVertex(o), getVertex(d), weight);
            insert(getVertex(d), getVertex(o), weight);
        }
    }

    
}
