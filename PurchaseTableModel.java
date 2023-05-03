import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PurchaseTableModel extends AbstractTableModel {

    private List<Purchase> purchases;
    private String[] columnNames = { "Customer", "Total Amount", "Loyalty Points Earned", "Address" }; // Add "Address"

    public PurchaseTableModel(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    @Override
    public int getRowCount() {
        return purchases.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Purchase purchase = purchases.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return purchase.getCustomer().getName();
            case 1:
                return purchase.getTotalPrice();
            case 2:
                return purchase.calculateLoyaltyPoints();
            case 3:
                return purchase.getCustomer().getAddress(); // Add this line
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 3: // Add this line
                return String.class;
            case 1:
            case 2:
                return Integer.class;
            default:
                return null;
        }
    }

    public Purchase getPurchaseAt(int rowIndex) {
        return purchases.get(rowIndex);
    }

    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
        fireTableDataChanged();
    }

    public void updatePurchase(int rowIndex, Purchase updatedPurchase) {
        purchases.set(rowIndex, updatedPurchase);
        fireTableDataChanged();
    }

    public void deletePurchase(int rowIndex) {
        purchases.remove(rowIndex);
        fireTableDataChanged();
    }
}
