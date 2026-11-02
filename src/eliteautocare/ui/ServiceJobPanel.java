package eliteautocare.ui;

import eliteautocare.model.DataStore;
import eliteautocare.model.ServiceJob;
import eliteautocare.util.ImageLoader;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class ServiceJobPanel extends JPanel {
    private final DefaultTableModel model;
    private final JTable table;
    
    // Changed to JComboBox for memory
    private final JComboBox<String> plateField = new JComboBox<>();
    private final JComboBox<String> serviceBox = new JComboBox<>(new String[]{
            "Oil Change", "Brake Repair", "Engine Diagnostics", "Tire Rotation",
            "AC Service", "Battery Replacement", "Full Service", "Suspension Repair"
    });
    private final JTextField costField = new JTextField();

    public ServiceJobPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        plateField.setEditable(true);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Vehicle Plate:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        form.add(plateField, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        form.add(new JLabel("Cost (GH₵):"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        form.add(costField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        form.add(new JLabel("Service Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        gbc.gridwidth = 3; 
        form.add(serviceBox, gbc);

        JButton addBtn = new JButton("➕ Create Job");
        addBtn.addActionListener(e -> addJob());

        JButton completeBtn = new JButton("✅ Mark Completed");
        completeBtn.addActionListener(e -> markCompleted());

        JButton deleteBtn = new JButton("🗑 Delete");
        deleteBtn.addActionListener(e -> deleteJob());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(addBtn); buttons.add(completeBtn); buttons.add(deleteBtn);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        JPanel topWrapper = new JPanel(new BorderLayout(10, 0));
        topWrapper.add(top, BorderLayout.CENTER);
        topWrapper.add(new JLabel(ImageLoader.load("service.jpg", 120, 120)), BorderLayout.EAST);

        add(topWrapper, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Job ID", "Plate", "Service", "Status", "Cost", "Date"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        add(createGallery(), BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel createGallery() {
        JPanel gallery = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        gallery.setBorder(BorderFactory.createTitledBorder("Our Workshop & Services"));

        ImageIcon img1 = ImageLoader.load("gallery1.jpg", 180, 120);
        ImageIcon img2 = ImageLoader.load("gallery2.jpg", 180, 120);
        ImageIcon img3 = ImageLoader.load("gallery3.jpg", 180, 120);

        gallery.add(new JLabel(img1));
        gallery.add(new JLabel(img2));
        gallery.add(new JLabel(img3));

        return gallery;
    }

    private void addJob() {
        String plate   = plateField.getEditor().getItem().toString().trim().toUpperCase();
        String service = (String) serviceBox.getSelectedItem();

        if (plate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vehicle plate is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean exists = DataStore.vehicles.stream().anyMatch(v -> v.getPlateNumber().equals(plate));
        if (!exists) {
            JOptionPane.showMessageDialog(this, "Vehicle not registered. Add it first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double cost;
        try {
            cost = Double.parseDouble(costField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cost must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        DataStore.jobs.add(new ServiceJob(DataStore.getNextJobId(), plate, service, "Pending", cost, LocalDate.now()));
        
        // Remember the plate number
        if (plateField.getItemCount() == 0 || !plateField.getItemAt(0).equals(plate)) {
            plateField.insertItemAt(plate, 0);
            if (plateField.getItemCount() > 10) plateField.removeItemAt(10);
        }
        
        plateField.setSelectedItem(""); 
        costField.setText("");
        refreshTable();
    }

    private void markCompleted() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int jobId = (int) model.getValueAt(row, 0);
        DataStore.jobs.stream().filter(j -> j.getJobId() == jobId).forEach(j -> j.setStatus("Completed"));
        refreshTable();
    }

    private void deleteJob() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int jobId = (int) model.getValueAt(row, 0);
        DataStore.jobs.removeIf(j -> j.getJobId() == jobId);
        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);
        for (ServiceJob j : DataStore.jobs)
            model.addRow(new Object[]{j.getJobId(), j.getPlateNumber(), j.getServiceType(), j.getStatus(), "GH₵ " + j.getCost(), j.getDate()});
    }
}