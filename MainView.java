import javax.swing.*;
import java.awt.*;

import java.util.List;
import java.util.ArrayList;
import java.util.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.LinkedHashMap;


import javax.swing.table.DefaultTableModel;

import javax.swing.border.TitledBorder;

public class MainView {
    private MainController controller;
    private JOptionPane mainMenuDialog;

    public MainView(MainController controller) {
        this.controller = controller;
    }

    public void init() {
        while (!showLoginDialog()) {
            JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.", "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        showMainMenu();
    }

    private boolean showLoginDialog() {
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
panel.add(new JLabel("Password:"));
panel.add(passwordField);

ImageIcon imageIcon = new ImageIcon("s.png");
JLabel imageLabel = new JLabel(imageIcon);

        int result = JOptionPane.showConfirmDialog(null, panel, "Login",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, imageIcon);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            String expectedUsername = "1";
            String expectedPassword = "1";

            return username.equals(expectedUsername) && password.equals(expectedPassword);
        }

        return false;
    }

private void showMainMenu() {
String[] options = { "Manage Products", "Manage Customers", "Loyalty Program", "Exit" };
mainMenuDialog = new JOptionPane("Convenience Corner Inventory:", JOptionPane.INFORMATION_MESSAGE,
        JOptionPane.DEFAULT_OPTION, null, options, options[0]);

        JDialog dialog = mainMenuDialog.createDialog("Main Menu");
        dialog.setVisible(true);
        Object selectedValue = mainMenuDialog.getValue();

        switch ((String) selectedValue) {
            case "Manage Products":
                dialog.dispose();
        showProductManagement();
        break;
    case "Manage Customers":
                dialog.dispose();
                showCustomerManagement();
    break;

case "Loyalty Program":
    dialog.dispose();
    showLoyaltyProgram();
    break;
            case "Exit":
                System.exit(0);
                break;

        }
    }

    private void showLoyaltyProgram() {
        JFrame customerSelectFrame = new JFrame("Select Customer for Purchase");
        customerSelectFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        customerSelectFrame.setSize(800, 400);

        JTable customerTable = new JTable(controller.getCustomerTableModel());
customerTable.setFillsViewportHeight(true);
JScrollPane scrollPane = new JScrollPane(customerTable);
customerSelectFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton selectButton = new JButton("Select");
        JButton backButton = new JButton("Back");

selectButton.addActionListener(e -> {
    int selectedRow = customerTable.getSelectedRow();
    if (selectedRow >= 0) {
        Customer selectedCustomer = controller.getCustomers().get(selectedRow);
        customerSelectFrame.dispose();
        showPurchaseDialog(selectedCustomer);
    }
});

        backButton.addActionListener(e -> {
            customerSelectFrame.dispose();
        });

        buttonPanel.add(selectButton);
        buttonPanel.add(backButton);
        customerSelectFrame.add(buttonPanel, BorderLayout.SOUTH);

        customerSelectFrame.setLocationRelativeTo(null);
        customerSelectFrame.setVisible(true);
    }

    private void showProductManagement() {
        JFrame productFrame = new JFrame("Product Management");
        productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        productFrame.setSize(800, 400);

        String[] columnNames = { "Name", "Price", "Quantity" };
        Object[][] data = new Object[controller.getProducts().size()][3];

        for (int i = 0; i < controller.getProducts().size(); i++) {
            Product product = controller.getProducts().get(i);
            data[i][0] = product.getName();
            data[i][1] = product.getPrice();
            data[i][2] = product.getQuantity();
        }

        JTable productTable = new JTable(controller.getProductTableModel());
        productTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(productTable);
        productFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton backButton = new JButton("Back");

        addButton.addActionListener(e -> showAddProductDialog());
        updateButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow >= 0) {
        showUpdateProductDialog(selectedRow);
    }
});
deleteButton.addActionListener(e -> {
    int selectedRow = productTable.getSelectedRow();
    if (selectedRow >= 0) {
                controller.deleteProduct(controller.getProducts().get(selectedRow));
            }
        });
        backButton.addActionListener(e -> {
            productFrame.dispose();
            showMainMenu();
        });

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        productFrame.add(buttonPanel, BorderLayout.SOUTH);

        productFrame.setLocationRelativeTo(null);
        productFrame.setVisible(true);
    }

    private void showAddProductDialog() {
        JTextField nameField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JTextField quantityField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
panel.add(new JLabel("Quantity:"));
panel.add(quantityField);

int result = JOptionPane.showConfirmDialog(null, panel, "Add Product",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

if (result == JOptionPane.OK_OPTION) {
    String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            Product newProduct = new Product(name, price, quantity);
            controller.addProduct(newProduct);
        }
    }

    private void savePurchasesToFile() {
        for (Purchase purchase : controller.getPurchases()) {
            String customerName = purchase.getCustomer().getName();
            String fileName = customerName + "_purchases.txt";

          
            List<String> existingData = controller.getFileManager().loadFromFile(fileName);

          
            LinkedHashMap<String, Integer> updatedQuantities = new LinkedHashMap<>();


    for (String line : existingData) {
        if (!line.trim().isEmpty()) {
            String[] productData = line.split(",");
                    if (productData.length >= 3) {
                        String productName = productData[0];
                        int quantity = Integer.parseInt(productData[2]);
                        updatedQuantities.put(productName, quantity);
                    } else {
                        System.err.println("Invalid data in the file: " + line);
                    }
                }
            }

    
            for (Product product : purchase.getProducts()) {
                String productName = product.getName();
                int quantity = product.getQuantity();
                updatedQuantities.put(productName, updatedQuantities.getOrDefault(productName, 0) + quantity);
            }

          
            List<String> newData = new ArrayList<>();
newData.add(customerName);
for (Map.Entry<String, Integer> entry : updatedQuantities.entrySet()) {
    String productName = entry.getKey();
    double productPrice = controller.getProductByName(productName).getPrice();
    int quantity = entry.getValue();
    newData.add(productName + "," + productPrice + "," + quantity);
            }

     
            controller.getFileManager().writeToFile(fileName, newData);
        }
    }

    private void onPurchaseButtonClick(Customer selectedCustomer, JTable selectedProductsTable) {

        List<Product> purchasedProducts = new ArrayList<>();
        double totalPurchaseAmount = 0;
        for (int i = 0; i < selectedProductsTable.getRowCount(); i++) {
            String productName = (String) selectedProductsTable.getValueAt(i, 0);
            double productPrice = (double) selectedProductsTable.getValueAt(i, 1);
            int quantity = (int) selectedProductsTable.getValueAt(i, 2);
            for (int j = 0; j < quantity; j++) {
    purchasedProducts.add(new Product(productName, productPrice, 1));
}
totalPurchaseAmount += productPrice * quantity;
}


if (purchasedProducts.isEmpty()) {
JOptionPane.showMessageDialog(null, "Please add products to the purchase.");
return;
}

        if (selectedCustomer.getBalance() < totalPurchaseAmount) {
    JOptionPane.showMessageDialog(null, "Insufficient balance. Please add funds to the customer's account.");
    return;
}

controller.createPurchase(selectedCustomer, purchasedProducts);
        savePurchasesToFile();

  
        selectedCustomer.setBalance(selectedCustomer.getBalance() - totalPurchaseAmount);

      
        int selectedIndex = controller.getCustomers().indexOf(selectedCustomer);
        if (selectedIndex != -1) {
            controller.updateCustomer(selectedIndex, selectedCustomer); 
                                                                        
        }

        saveCustomersToFile();

        JOptionPane.showMessageDialog(null, "Purchase completed. Loyalty points updated.");
    }

    public void saveCustomersToFile() {
        List<String> customerLines = new ArrayList<>();
        for (Customer customer : controller.getCustomers()) {
            String line = String.join(",", customer.getName(), Double.toString(customer.getBalance()),
                    Integer.toString(customer.getLoyaltyPoints()));
            customerLines.add(line);
        }
        controller.getFileManager().writeToFile("customers.txt", customerLines);
    }

    public void loadSelectedCustomerPurchases(Customer customer, JTable selectedProductsTable) {
        DefaultTableModel selectedProductsTableModel = (DefaultTableModel) selectedProductsTable.getModel();
        selectedProductsTableModel.setRowCount(0);

        List<Product> customerPurchases = controller.getCustomerPurchases(customer.getName());
        for (Product product : customerPurchases) {
            Object[] rowData = new Object[] { product.getName(), product.getPrice(), product.getQuantity() };
            selectedProductsTableModel.addRow(rowData);
        }
    }

    private void showUpdateCustomerDialog(int rowIndex) {

        Customer customer = controller.getCustomers().get(rowIndex);

        JTextField nameField = new JTextField(customer.getName());
        JTextField addressField = new JTextField(customer.getAddress());
        JTextField balanceField = new JTextField(Double.toString(customer.getBalance()));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Balance:"));
        panel.add(balanceField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Customer",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String address = addressField.getText();
            double balance = Double.parseDouble(balanceField.getText());
            customer.setName(name);
            customer.setAddress(address);
            customer.setBalance(balance);
        }
    }

    private void showAddCustomerDialog() {
        JTextField nameField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField balanceField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Balance:"));
        panel.add(balanceField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Customer",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String address = addressField.getText();
            double balance = Double.parseDouble(balanceField.getText());
            Customer newCustomer = new Customer(name, address, balance);
            controller.addCustomer(newCustomer);
        }

    }

    private void showCustomerManagement() {
        JFrame customerFrame = new JFrame("Customer Management");
        customerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        customerFrame.setSize(800, 400);

        CustomerTableModel customerTableModel = new CustomerTableModel(controller.getCustomers());
        JTable customerTable = new JTable(controller.getCustomerTableModel());
        customerTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        customerFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton purchaseButton = new JButton("Purchase");

        JButton viewPurchasesButton = new JButton("View Purchases");
        JButton backButton = new JButton("Back");

        viewPurchasesButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow >= 0) {
                showCustomerPurchases(controller.getCustomers().get(selectedRow));
            }
        });
        buttonPanel.add(viewPurchasesButton);

        addButton.addActionListener(e -> showAddCustomerDialog());
        updateButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow >= 0) {
                showUpdateCustomerDialog(selectedRow);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow >= 0) {
                controller.deleteCustomer(controller.getCustomers().get(selectedRow));
            }
        });
        backButton.addActionListener(e -> {
            customerFrame.dispose();
            showMainMenu();
        });

        purchaseButton.addActionListener(e -> showPurchaseDialogForSelectedCustomer(customerTable));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(purchaseButton); 
        customerFrame.add(buttonPanel, BorderLayout.SOUTH);

        customerFrame.setLocationRelativeTo(null);
        customerFrame.setVisible(true);
    }

    private void showPurchaseDialogForSelectedCustomer(JTable customerTable) {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow >= 0) {
            Customer customer = controller.getCustomers().get(selectedRow);
            showPurchaseDialog(customer);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a customer.");
        }
    }

    private void showUpdateProductDialog(int rowIndex) {

        Product product = controller.getProducts().get(rowIndex);

        JTextField nameField = new JTextField(product.getName());
        JTextField priceField = new JTextField(Double.toString(product.getPrice()));
        JTextField quantityField = new JTextField(Integer.toString(product.getQuantity()));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Product",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());
            product.setName(name);
            product.setPrice(price);
            product.setQuantity(quantity);
        }
    }

    private void showPurchaseDialog(Customer customer) {
        JFrame purchaseFrame = new JFrame("Purchase for " + customer.getName());
        purchaseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        purchaseFrame.setSize(800, 600);

        JTable availableProductsTable = new JTable(controller.getProductTableModel());
        availableProductsTable.setFillsViewportHeight(true);
        JScrollPane availableProductsScrollPane = new JScrollPane(availableProductsTable);
        TitledBorder availableProductsBorder = BorderFactory.createTitledBorder("Available Products");
        availableProductsScrollPane.setBorder(availableProductsBorder);

        DefaultTableModel selectedProductsModel = new DefaultTableModel(new Object[][] {},
                new String[] { "Name", "Price", "Quantity" });
        JTable selectedProductsTable = new JTable(selectedProductsModel);
        selectedProductsTable.setFillsViewportHeight(true);
        JScrollPane selectedProductsScrollPane = new JScrollPane(selectedProductsTable);
        TitledBorder selectedProductsBorder = BorderFactory.createTitledBorder("Selected Products");
        selectedProductsScrollPane.setBorder(selectedProductsBorder);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, availableProductsScrollPane,
                selectedProductsScrollPane);
        splitPane.setResizeWeight(0.5);
        purchaseFrame.add(splitPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Product");
        JButton removeButton = new JButton("Remove Product");
        JButton purchaseButton = new JButton("Purchase");
        JButton backButton = new JButton("Back");

        addButton.addActionListener(e -> {
            int selectedRow = availableProductsTable.getSelectedRow();
            if (selectedRow >= 0) {
                Product selectedProduct = controller.getProducts().get(selectedRow);
                selectedProductsModel.addRow(new Object[] { selectedProduct.getName(), selectedProduct.getPrice(), 1 });
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = selectedProductsTable.getSelectedRow();
            if (selectedRow >= 0) {
                selectedProductsModel.removeRow(selectedRow);
            }
        });

        purchaseButton.addActionListener(e -> onPurchaseButtonClick(customer, selectedProductsTable));

        backButton.addActionListener(e -> purchaseFrame.dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(purchaseButton);
        buttonPanel.add(backButton);
        purchaseFrame.add(buttonPanel, BorderLayout.SOUTH);

        purchaseFrame.setLocationRelativeTo(null);
        purchaseFrame.setVisible(true);
    }

    public void showCustomerPurchases(Customer customer) {
        JFrame purchasesFrame = new JFrame("Purchases for " + customer.getName());
        purchasesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        purchasesFrame.setSize(800, 600);

        DefaultTableModel purchasedProductsModel = new DefaultTableModel(new Object[][] {},
                new String[] { "Name", "Price", "Quantity" });
        JTable purchasedProductsTable = new JTable(purchasedProductsModel);
        purchasedProductsTable.setFillsViewportHeight(true);
        JScrollPane purchasedProductsScrollPane = new JScrollPane(purchasedProductsTable);
        TitledBorder purchasedProductsBorder = BorderFactory.createTitledBorder("Purchased Products");
        purchasedProductsScrollPane.setBorder(purchasedProductsBorder);

        purchasedProductsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    loadHistoricalPurchases(customer, purchasedProductsTable);
                }
            }
        });

        purchasesFrame.add(purchasedProductsScrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> purchasesFrame.dispose());
        purchasesFrame.add(backButton, BorderLayout.SOUTH);

        purchasesFrame.setLocationRelativeTo(null);
        purchasesFrame.setVisible(true);

       
        loadHistoricalPurchases(customer, purchasedProductsTable);
    }

    public void loadHistoricalPurchases(Customer customer, JTable purchasedProductsTable) {
        DefaultTableModel purchasedProductsTableModel = (DefaultTableModel) purchasedProductsTable.getModel();
        purchasedProductsTableModel.setRowCount(0);
    
        List<Product> purchasedProducts = controller.getCustomerPurchases(customer.getName());
        for (Product product : purchasedProducts) {
            Object[] rowData = new Object[] { product.getName(), product.getPrice(), product.getQuantity() };
            purchasedProductsTableModel.addRow(rowData);
        }
    }
    

    private void hideMainMenu() {
        if (mainMenuDialog != null) {
            mainMenuDialog.setVisible(false);
        }
    }

}
