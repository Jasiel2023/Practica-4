import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Laberinto extends JFrame {
    private Prim2 primGenerator;
    private Solucionador mazeSolver;
    private PanelLaberinto mazePanel;

    private JSpinner rowsSpinner;
    private JSpinner colsSpinner;

    public Laberinto() {
        super("Generador y Solucionador de Laberintos");

        primGenerator = new Prim2();
        mazeSolver = new Solucionador();
        mazePanel = new PanelLaberinto();

        initComponents();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centrado con espacio

        JLabel rowsLabel = new JLabel("Filas (30-100):");
        rowsSpinner = new JSpinner(new SpinnerNumberModel(40, 30, 100, 1)); // Valor inicial, min, max, paso
        JLabel colsLabel = new JLabel("Columnas (30-100):");
        colsSpinner = new JSpinner(new SpinnerNumberModel(40, 30, 100, 1));

        JButton generateButton = new JButton("Generar Laberinto");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMaze();
            }
        });

        JButton solveButton = new JButton("Resolver Laberinto");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveMaze();
            }
        });

        controlPanel.add(rowsLabel);
        controlPanel.add(rowsSpinner);
        controlPanel.add(colsLabel);
        controlPanel.add(colsSpinner);
        controlPanel.add(generateButton);
        controlPanel.add(solveButton);

        add(controlPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(mazePanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void generateMaze() {
        int rows = (Integer) rowsSpinner.getValue();
        int cols = (Integer) colsSpinner.getValue();

        try {
            char[][] newMaze = primGenerator.generar(rows, cols);
            mazePanel.setMaze(newMaze);

            pack();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el laberinto: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void solveMaze() {
        char[][] currentMaze = mazePanel.getMaze();
        if (currentMaze == null) {
            JOptionPane.showMessageDialog(this, "Primero genera un laberinto.", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
            
        }

        try {
            data_structure.list.LinkedList<Prim2.Point> path = mazeSolver.solve(currentMaze);
            if (path != null && path.getLength() > 0) {
                if (path != null && path.getLength() > 0) {
                    mazePanel.animateSolution(path.toArray(), 50, () -> {
                        JOptionPane.showMessageDialog(this, "Laberinto resuelto con éxito!", "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                    });
                }
            } else {
                mazePanel.setSolutionPath(null);
                JOptionPane.showMessageDialog(this, "No se logro encontrar una solucion al laberinto.", "Sin Solución",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al resolver el laberinto: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
    
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Laberinto();
            }
        });
    }
}