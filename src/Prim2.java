// src/Prim2.java

// Importa tu clase LinkedList personalizada
import data_structure.list.LinkedList;

/**
 * Clase que implementa una versión del algoritmo de Prim para generar laberintos aleatorios.
 * Utiliza una lista enlazada personalizada para gestionar la frontera de celdas.
 */
public class Prim2 {

    /**
     * Clase interna estática que representa un punto (celda) en el laberinto.
     * Contiene las coordenadas de fila y columna, y una referencia a su nodo padre
     * para reconstruir el camino de generación del laberinto.
     * Esta clase Point está anidada dentro de Prim2.
     */
    static class Point {
        Integer r;      // Fila del punto
        Integer c;      // Columna del punto
        Point parent;   // Referencia al punto padre en el proceso de generación

        /**
         * Constructor para crear un nuevo punto.
         * @param x Fila del punto.
         * @param y Columna del punto.
         * @param p Punto padre.
         */
        public Point(int x, int y, Point p) {
            r = x;
            c = y;
            parent = p;
        }

        /**
         * Calcula el punto "opuesto" en relación con el padre.
         * Esto es crucial para el algoritmo de Prim, que rompe paredes entre un punto
         * de la frontera y el punto al otro lado de su padre.
         * Por ejemplo, si el padre es (0,0) y este punto es (1,0), el opuesto sería (2,0).
         * @return El punto opuesto, o null si el padre es null (caso del punto de inicio).
         */
        public Point opposite() {
            if (parent == null) {
                return null; // Un punto sin padre no tiene un "opuesto" en este contexto
            }

            // Si la diferencia está en la fila (movimiento vertical)
            if (this.r.compareTo(parent.r) != 0) {
                // Se mueve en la misma dirección que desde el padre
                return new Point(this.r + this.r.compareTo(parent.r), this.c, this);
            }
            // Si la diferencia está en la columna (movimiento horizontal)
            if (this.c.compareTo(parent.c) != 0) {
                // Se mueve en la misma dirección que desde el padre
                return new Point(this.r, this.c + this.c.compareTo(parent.c), this);
            }
            return null; // Si el padre es el mismo punto (error lógico)
        }
    }

    /**
     * Genera un laberinto aleatorio utilizando una variación del algoritmo de Prim.
     * @param r Número de filas del laberinto.
     * @param c Número de columnas del laberinto.
     * @return Una matriz de caracteres (char[][]) que representa el laberinto generado.
     * '0' para pared, '1' para camino, 'S' para inicio, 'E' para fin.
     */
    public char[][] generar(int r, int c) {
        // Construye el laberinto e inicializa todas las celdas como paredes ('0')
        char[][] maz = new char[r][c];
        for (int x = 0; x < r; x++) {
            for (int y = 0; y < c; y++) {
                maz[x][y] = '0';
            }
        }

        // Selecciona un punto de inicio aleatorio y lo marca con 'S'
        Point st = new Point((int) (Math.random() * r), (int) (Math.random() * c), null);
        maz[st.r][st.c] = 'S';

        // Inicializa la frontera con los vecinos ortogonales del punto de inicio.
        // Estos son los muros adyacentes que podrían convertirse en caminos.
        // --- ¡IMPORTANTE! Aquí se usa tu LinkedList y se le pasa la clase Point ---
        LinkedList<Prim2.Point> frontier = new LinkedList<>();
        // -----------------------------------------------------------------------
       
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                // Considerar solo vecinos directos (arriba, abajo, izquierda, derecha)
                // Excluye la celda central (x=0, y=0) y las diagonales (x!=0, y!=0)
                if (x == 0 && y == 0 || x != 0 && y != 0) {
                    continue;
                }
                int newR = st.r + x;
                int newC = st.c + y;
                // Verifica que el vecino esté dentro de los límites y sea una pared
                if (newR >= 0 && newR < r && newC >= 0 && newC < c && maz[newR][newC] == '0') {
                    frontier.add(new Point(newR, newC, st)); // Añade el punto a la frontera
                }
            }

        }

        Point last = null; // Almacena el último punto "abierto" para marcarlo como 'E' (Fin)
        while (!frontier.isEmpty()) { // Mientras haya puntos en la frontera

            // Elige un punto (muro) aleatorio de la frontera para procesar
            // --- ¡IMPORTANTE! Aquí se usa tu LinkedList.removeRandom() ---
            Point cu = frontier.removeRandom();
            // --------------------------------------------------------------

            // Si el punto actual o su padre son nulos, saltar (debería ser manejado por las comprobaciones)
            if (cu == null || cu.parent == null) {
                continue;
            }

            Point op = cu.opposite(); // Calcula el punto al otro lado del muro 'cu'

            // Bloque try-catch general para capturar posibles errores de índice o nulos si no se manejan bien
            try {
                // Verificar que tanto 'cu' como 'op' estén dentro de los límites del laberinto
                if (cu.r >= 0 && cu.r < r && cu.c >= 0 && cu.c < c &&
                    op.r >= 0 && op.r < r && op.c >= 0 && op.c < c) {

                    // Si tanto el punto actual (muro) como su punto opuesto (al otro lado) son paredes ('0')
                    // Esto asegura que estamos "rompiendo" un muro entre dos áreas aún no conectadas
                    if (maz[cu.r][cu.c] == '0' && maz[op.r][op.c] == '0') {

                        // Abrir el camino: convertir las paredes en caminos ('1')
                        maz[cu.r][cu.c] = '1';
                        maz[op.r][op.c] = '1';

                        // Almacenar este punto opuesto como el "último" punto visitado,
                        // que potencialmente será el punto final del laberinto.
                        last = op;

                        // Ahora, añade los vecinos ortogonales del punto "op" a la frontera,
                        // si son paredes y no están ya en la frontera (implícitamente si son '0').
                        for (int x = -1; x <= 1; x++) {
                            for (int y = -1; y <= 1; y++) {
                                if (x == 0 && y == 0 || x != 0 && y != 0) {
                                    continue;
                                }
                                int newR = op.r + x;
                                int newC = op.c + y;
                                // Asegúrate de que el vecino esté dentro de los límites y sea una pared
                                if (newR >= 0 && newR < r && newC >= 0 && newC < c && maz[newR][newC] == '0') {
                                    frontier.add(new Point(newR, newC, op)); // Añade a la frontera
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // En un entorno de producción, registraría e.getMessage() o e.printStackTrace()
                // Aquí, simplemente ignoramos errores de índice o nulos en los bordes.
            }
        }

        // Una vez que la frontera está vacía, el laberinto está generado.
        // Marca el último punto abierto como el final 'E'.
        if (last != null) {
            maz[last.r][last.c] = 'E';
        } else {
             // Caso de respaldo: si por alguna razón 'last' es null (laberinto muy pequeño o error),
             // fuerza el 'E' en la esquina inferior derecha.
             maz[r-1][c-1] = 'E';
        }

        return maz; // Devuelve la matriz del laberinto
    }
}