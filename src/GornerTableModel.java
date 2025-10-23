
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;
    private ArrayList<Double> PolinomValues = new ArrayList<>();

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getPolinomValue(int i){
        if (PolinomValues != null)
            return PolinomValues.get(i);
        return 0.0;
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
        // Теперь три столбца
        return 3;
    }

    public int getRowCount() {
        return (int) Math.ceil((to - from) / step) + 1;
    }

    public Object getValueAt(int row, int col) {
        double x = from + step * row;
        switch (col) {
            case 0 -> {
                return x;
            }
            case 1 -> {
                // Horner's scheme implementation
                Double result = coefficients[0];
                for (int i = 1; i < coefficients.length; i++) {
                    result = result * x + coefficients[i];
                }
                return result;
            }
            default -> { // col == 2
                Double result = coefficients[0];
                for (int i = 1; i < coefficients.length; i++) {
                    result = result * x + coefficients[i];
                }
                PolinomValues.add(result);
                int intPart = result.intValue();
                return isSquare(intPart);
            }
        }
    }

    private Boolean isSquare(int number) {
        if (number < 0) return false;
        int sqrt = (int) Math.sqrt(number);
        return sqrt * sqrt == number;
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Value X";
            case 1:
                return "Polynomial Value";
            case 2:
                return "Is integer part a square?";
            default:
                return "";
        }
    }

    public Class<?> getColumnClass(int col) {
        switch (col) {
            case 0:
                return Double.class;
            case 1:
                return Double.class;
            case 2:
                return Boolean.class;
            default:
                return Object.class;
        }
    }
}