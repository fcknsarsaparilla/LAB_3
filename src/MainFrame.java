
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;

    private Double[] coefficients;
    private JFileChooser fileChooser = null;
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;

    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;

    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
    private GornerTableModel data;

    public MainFrame(Double[] coefficients) {
        super("Polynomial Tabulation using Horner's Scheme");
        this.coefficients = coefficients;
        setSize(WIDTH, HEIGHT);

        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);

        // Create menu
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenu tableMenu = new JMenu("Table");
        menuBar.add(tableMenu);

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        // Save to text file action
        Action saveToTextAction = new AbstractAction("Save to text file") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    saveToTextFile(fileChooser.getSelectedFile());
                }
            }
        };
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        saveToTextMenuItem.setEnabled(false);

        // Save to graphics file action
        Action saveToGraphicsAction = new AbstractAction("Save data for graph") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    saveToGraphicsFile(fileChooser.getSelectedFile());
                }
            }
        };
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        saveToGraphicsMenuItem.setEnabled(false);

        // Search value action
        Action searchValueAction = new AbstractAction("Find polynomial value") {
            public void actionPerformed(ActionEvent event) {
                String value = JOptionPane.showInputDialog(MainFrame.this,
                        "Enter value to search", "Value search", JOptionPane.QUESTION_MESSAGE);
                renderer.setNeedle(value);
                getContentPane().repaint();
            }

            Action aboutAction = new AbstractAction("About") {
                public void actionPerformed(ActionEvent event) {
                    showAboutDialog();
                }
            };
            JMenuItem aboutMenuItem = helpMenu.add(aboutAction);

        };
        searchValueMenuItem = tableMenu.add(searchValueAction);
        searchValueMenuItem.setEnabled(false);

        // Create input fields
        JLabel labelForFrom = new JLabel("X changes from:");
        textFieldFrom = new JTextField("0.0", 10);
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());

        JLabel labelForTo = new JLabel("to:");
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());

        JLabel labelForStep = new JLabel("with step:");
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());

        Box hboxRange = Box.createHorizontalBox();
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.add(labelForFrom);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldFrom);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForTo);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldTo);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForStep);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldStep);
        hboxRange.add(Box.createHorizontalGlue());

        hboxRange.setPreferredSize(new Dimension(
                (int) hboxRange.getMaximumSize().getWidth(),
                (int) hboxRange.getMinimumSize().getHeight() * 2));

        getContentPane().add(hboxRange, BorderLayout.NORTH);

        // Calculate button
        JButton buttonCalc = new JButton("Calculate");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step = Double.parseDouble(textFieldStep.getText());

                    data = new GornerTableModel(from, to, step, MainFrame.this.coefficients);
                    JTable table = new JTable(data);
                    table.setDefaultRenderer(Double.class, renderer);
                    table.setDefaultRenderer(Boolean.class, renderer);
                    table.setRowHeight(30);

                    hBoxResult.removeAll();
                    hBoxResult.add(new JScrollPane(table));
                    getContentPane().validate();

                    saveToTextMenuItem.setEnabled(true);
                    saveToGraphicsMenuItem.setEnabled(true);
                    searchValueMenuItem.setEnabled(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Error in floating point number format", "Number format error",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Reset button
        JButton buttonReset = new JButton("Clear fields");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
                hBoxResult.removeAll();
                hBoxResult.add(new JPanel());
                saveToTextMenuItem.setEnabled(false);
                saveToGraphicsMenuItem.setEnabled(false);
                searchValueMenuItem.setEnabled(false);
                getContentPane().validate();
            }
        });


        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());

        hboxButtons.setPreferredSize(new Dimension(
                (int) hboxButtons.getMaximumSize().getWidth(),
                (int) hboxButtons.getMinimumSize().getHeight() * 2));

        getContentPane().add(hboxButtons, BorderLayout.SOUTH);

        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }

    private void showAboutDialog() {
        String aboutMessage = "Author: Yegor" + "Group: 10";
                JOptionPane.showMessageDialog(this, aboutMessage,
                        "About Program", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void saveToGraphicsFile(File selectedFile) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
            for (int i = 0; i < data.getRowCount(); i++) {
                out.writeDouble((Double) data.getValueAt(i, 0));
                out.writeDouble((Double) data.getValueAt(i, 1));
            }
            out.close();
        } catch (Exception e) {
            // Handle exception if needed
        }
    }

    protected void saveToTextFile(File selectedFile) {
        try {
            PrintStream out = new PrintStream(selectedFile);
            out.println("Results of polynomial tabulation using Horner's scheme");
            out.print("Polynomial: ");
            for (int i = 0; i < coefficients.length; i++) {
                out.print(coefficients[i] + "*X^" + (coefficients.length - i - 1));
                if (i != coefficients.length - 1)
                    out.print(" + ");
            }
            out.println("");
            out.println("Interval from " + data.getFrom() + " to " + data.getTo() +
                    " with step " + data.getStep());
            out.println("====================================================");

            for (int i = 0; i < data.getRowCount(); i++) {
                out.println("Value at point " + data.getPolinomValue(i).toString() +
                        " is " + data.getValueAt(i, 1));
            }
            out.close();
        } catch (FileNotFoundException e) {
            // Handle exception if needed
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Cannot tabulate polynomial without coefficients!");
            System.exit(-1);
        }

        Double[] coefficients = new Double[args.length];
        int i = 0;
        try {
            for (String arg : args) {
                coefficients[i++] = Double.parseDouble(arg);
            }
        } catch (NumberFormatException ex) {
            System.out.println("Error converting string '" + args[i] + "' to Double");
            System.exit(-2);
        }

        MainFrame frame = new MainFrame(coefficients);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}