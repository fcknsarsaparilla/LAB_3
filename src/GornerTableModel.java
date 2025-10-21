
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    public int getColumnCount() {
        return 2;
    }

    public int getRowCount() {
        return Integer.valueOf(Double.valueOf(Math.ceil((to - from) / step)).intValue() + 1);
    }

    public Object getValueAt(int row, int col) {
        double x = from + step * row;
        if (col == 0) {
            return x;
        } else {
            // Horner's scheme implementation
            Double result = coefficients[0];
            for (int i = 1; i < coefficients.length; i++) {
                result = result * x + coefficients[i];
            }
            return result;
        }
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Value X";
            default:
                return "Polynomial Value";
        }
    }

    public Class<?> getColumnClass(int col) {
        return Double.class;
    }
}