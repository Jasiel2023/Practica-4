
import javax.swing.*;
import java.awt.*;


public class PanelLaberinto extends JPanel {
    private char[][] maze;
    private int cellSize = 20; 
    private Prim2.Point[] solutionPath;

    public PanelLaberinto() {
        this.setBackground(Color.BLACK); 
    }

    public void animateSolution(Prim2.Point[] path, int delayMs, Runnable onFinish) {
        if (path == null || path.length == 0) {
            if (onFinish != null)
                onFinish.run();
            return;
        }
        new Thread(() -> {
            for (int i = 1; i <= path.length; i++) {
                this.solutionPath = new Prim2.Point[i];
                System.arraycopy(path, 0, this.solutionPath, 0, i);
                System.out.println("Animando paso: " + i + "/" + path.length);
                repaint();
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                    break;
                }
            }
            if (onFinish != null)
                SwingUtilities.invokeLater(onFinish);
        }).start();
    }

    public void setMaze(char[][] maze) {
        this.maze = maze;
        this.solutionPath = null; 
        if (maze != null) {
           
            setPreferredSize(new Dimension(maze[0].length * cellSize, maze.length * cellSize));
        }
        repaint(); 
    }

   
    public void setSolutionPath(Prim2.Point[] path) {
        this.solutionPath = path;
        repaint();
    }

    
    public char[][] getMaze() {
        return this.maze;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (maze == null) {
            return;
        }

        int rows = maze.length;
        int cols = maze[0].length;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char cell = maze[r][c];
                int x = c * cellSize;
                int y = r * cellSize;

                switch (cell) {
                    case '0': // Pared
                        g.setColor(Color.DARK_GRAY);
                        break;
                    case '1': // Camino
                        g.setColor(Color.WHITE);
                        break;
                    case 'S': // Inicio
                        g.setColor(Color.GREEN);
                        break;
                    case 'E': // Fin
                        g.setColor(Color.RED);
                        break;
                    default:
                        g.setColor(Color.BLACK); 
                }
                g.fillRect(x, y, cellSize, cellSize);

               
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }

      
        if (solutionPath != null && solutionPath.length > 1) {
            g.setColor(Color.BLUE); 
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(cellSize / 4f)); 

            for (int i = 0; i < solutionPath.length - 1; i++) {
                Prim2.Point p1 = solutionPath[i];
                Prim2.Point p2 = solutionPath[i + 1];

                int x1 = p1.c * cellSize + cellSize / 2;
                int y1 = p1.r * cellSize + cellSize / 2;
                int x2 = p2.c * cellSize + cellSize / 2;
                int y2 = p2.r * cellSize + cellSize / 2;

                g2d.drawLine(x1, y1, x2, y2);
            }
        }

        if (solutionPath != null && solutionPath.length > 0) {
            Prim2.Point agent = solutionPath[solutionPath.length - 1];
            int x = agent.c * cellSize;
            int y = agent.r * cellSize;
            g.setColor(Color.ORANGE);
            g.fillOval(x + cellSize / 4, y + cellSize / 4, cellSize / 2, cellSize / 2);
        }
    }
}