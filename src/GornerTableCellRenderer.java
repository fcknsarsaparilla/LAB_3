
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GornerTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    private JCheckBox checkBox = new JCheckBox();
    private String needle = null;
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    public GornerTableCellRenderer() {
        formatter.setMaximumFractionDigits(5);
        formatter.setGroupingUsed(false);

        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);

        panel.add(label);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Настройка checkbox для третьего столбца
        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int col) {

        if (col == 2) {
            checkBox.setSelected((Boolean) value);

            // Установка цвета фона
               checkBox.setBackground(isSelected? table.getSelectionBackground() : table.getBackground());

            return checkBox;
        } else {
            // Для первых двух столбцов - обычное отображение
            String formattedDouble = formatter.format(value);
            label.setText(formattedDouble);

            if (col == 1 && needle != null && needle.equals(formattedDouble)) {
                panel.setBackground(Color.RED);
            } else {
                if (isSelected) {
                    panel.setBackground(table.getSelectionBackground());
                } else {
                    panel.setBackground(table.getBackground());
                }
            }
            return panel;
        }
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }
}