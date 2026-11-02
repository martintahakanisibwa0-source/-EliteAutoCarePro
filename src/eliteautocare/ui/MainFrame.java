package eliteautocare.ui;

import eliteautocare.util.ImageLoader;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Elite AutoCare Pro — Repair & Maintenance Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700); // Made slightly wider to fit the new tab
        setLocationRelativeTo(null);

        Image appIcon = ImageLoader.loadRaw("logo.jpg");
        if (appIcon != null) setIconImage(appIcon);

        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setBackground(new Color(20, 60, 120));
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel logoLabel = new JLabel(ImageLoader.load("logo.jpg", 160, 48));
        JLabel title = new JLabel("Elite AutoCare Pro");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brand.setOpaque(false);
        brand.add(logoLabel);
        brand.add(title);
        header.add(brand, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        
        tabs.addTab("Dashboard", new JPanel());
        tabs.addTab("Customers", new CustomerPanel());
        tabs.addTab("Vehicles", new VehiclePanel());
        tabs.addTab("Service Jobs", new ServiceJobPanel());
        tabs.addTab("📈 Reports & Trends", new ReportsPanel()); // NEW TAB
        
        // Refresh Dashboard and Reports every time you click them
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 0) {
                tabs.setComponentAt(0, new DashboardPanel());
            } else if (tabs.getSelectedIndex() == 4) {
                tabs.setComponentAt(4, new ReportsPanel());
            }
        });

        tabs.setComponentAt(0, new DashboardPanel());
        add(tabs, BorderLayout.CENTER);
    }
}