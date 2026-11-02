package eliteautocare.ui;

import eliteautocare.model.Customer;
import eliteautocare.model.DataStore;
import eliteautocare.util.ImageLoader;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerPanel extends JPanel {
    private final DefaultTableModel model;
    private final JTable table;
    
    // Changed from JTextField to JComboBox for memory
    private final JComboBox<String> nameField  = new JComboBox<>();
    private final JComboBox<String> phoneField = new JComboBox<>();
    private final JComboBox<String> emailField = new JComboBox<>();

    public CustomerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Make them editable so users can type new things
        nameField.setEditable(true);
        phoneField.setEditable(true);
        emailField.setEditable(true);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        form.add(nameField, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        form.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        gbc.gridwidth = 3; 
        form.add(emailField, gbc);

        JButton addBtn = new JButton("➕ Add Customer");
        addBtn.addActionListener(e -> addCustomer());

        JButton deleteBtn = new JButton("🗑 Delete Selected");
        deleteBtn.addActionListener(e -> deleteCustomer());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(addBtn); buttons.add(deleteBtn);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        JPanel topWrapper = new JPanel(new BorderLayout(10, 0));
        topWrapper.add(top, BorderLayout.CENTER);
        topWrapper.add(new JLabel(ImageLoader.load("customer.jpg", 120, 120)), BorderLayout.EAST);

        add(topWrapper, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Phone", "Email"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();
    }

    private void addCustomer() {
        // .getSelectedItem() gets the typed text OR the selected history item
        String name  = nameField.getEditor().getItem().toString().trim();
        String phone = phoneField.getEditor().getItem().toString().trim();
        String email = emailField.getEditor().getItem().toString().trim();
        
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Phone are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        DataStore.customers.add(new Customer(DataStore.getNextCustomerId(), name, phone, email));
        
        // Add to history (Limit to 10 items so it doesn't grow forever)
        addToHistory(nameField, name);
        addToHistory(phoneField, phone);
        addToHistory(emailField, email);
        
        // Clear the input
        nameField.setSelectedItem("");
        phoneField.setSelectedItem("");
        emailField.setSelectedItem("");
        
        refreshTable();
    }
    
    // Helper method to remember the data
    private void addToHistory(JComboBox<String> box, String value) {
        if (value.isEmpty()) return;
        // Remove if it already exists so we can move it to the top
        box.removeItem(value); 
        box.insertItemAt(value, 0);
        // Keep only the last 10 items
        if (box.getItemCount() > 10) {
            box.removeItemAt(10);
        }
    }

    private void deleteCustomer() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) model.getValueAt(row, 0);
        DataStore.customers.removeIf(c -> c.getId() == id);
        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Customer c : DataStore.customers)
            model.addRow(new Object[]{c.getId(), c.getName(), c.getPhone(), c.getEmail()});
    }
}