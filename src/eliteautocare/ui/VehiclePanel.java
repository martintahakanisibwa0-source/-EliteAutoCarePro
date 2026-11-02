package eliteautocare.ui;

import eliteautocare.model.DataStore;
import eliteautocare.model.Vehicle;
import eliteautocare.util.ImageLoader;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VehiclePanel extends JPanel {
    private final DefaultTableModel model;
    private final JTable table;
    
    // Changed to JComboBox for memory
    private final JComboBox<String> plateField = new JComboBox<>();
    private final JComboBox<String> makeField  = new JComboBox<>();
    private final JComboBox<String> modelField = new JComboBox<>();
    private final JTextField yearField  = new JTextField(); // Years are easy to type
    private final JComboBox<String> ownerField = new JComboBox<>();

    public VehiclePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        plateField.setEditable(true);
        makeField.setEditable(true);
        modelField.setEditable(true);
        ownerField.setEditable(true);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Plate No:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        form.add(plateField, gbc);
        
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Make:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        form.add(makeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Model:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        form.add(modelField, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Year:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0;
        form.add(yearField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        form.add(new JLabel("Owner:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        form.add(ownerField, gbc);

        JButton addBtn = new JButton("➕ Add Vehicle");
        addBtn.addActionListener(e -> addVehicle());

        JButton deleteBtn = new JButton("🗑 Delete Selected");
        deleteBtn.addActionListener(e -> deleteVehicle());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(addBtn); buttons.add(deleteBtn);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        JPanel topWrapper = new JPanel(new BorderLayout(10, 0));
        topWrapper.add(top, BorderLayout.CENTER);
        topWrapper.add(new JLabel(ImageLoader.load("vehicle.jpg", 120, 120)), BorderLayout.EAST);

        add(topWrapper, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Plate No", "Make", "Model", "Year", "Owner"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshTable();
    }

    private void addVehicle() {
        try {
            String plate = plateField.getEditor().getItem().toString().trim().toUpperCase();
            String make  = makeField.getEditor().getItem().toString().trim();
            String mdl   = modelField.getEditor().getItem().toString().trim();
            int year     = Integer.parseInt(yearField.getText().trim());
            String owner = ownerField.getEditor().getItem().toString().trim();

            if (plate.isEmpty() || make.isEmpty() || mdl.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Plate, Make and Model are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DataStore.vehicles.add(new Vehicle(plate, make, mdl, year, owner));
            
            addToHistory(plateField, plate);
            addToHistory(makeField, make);
            addToHistory(modelField, mdl);
            addToHistory(ownerField, owner);
            
            plateField.setSelectedItem(""); makeField.setSelectedItem(""); modelField.setSelectedItem("");
            yearField.setText(""); ownerField.setSelectedItem("");
            
            refreshTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Year must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addToHistory(JComboBox<String> box, String value) {
        if (value.isEmpty()) return;
        box.removeItem(value); 
        box.insertItemAt(value, 0);
        if (box.getItemCount() > 10) box.removeItemAt(10);
    }

    private void deleteVehicle() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        String plate = (String) model.getValueAt(row, 0);
        DataStore.vehicles.removeIf(v -> v.getPlateNumber().equals(plate));
        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (Vehicle v : DataStore.vehicles)
            model.addRow(new Object[]{v.getPlateNumber(), v.getMake(), v.getModel(), v.getYear(), v.getOwnerName()});
    }
}