package eliteautocare.ui;

import eliteautocare.model.DataStore;
import eliteautocare.util.ImageLoader;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private final Image background;

    public DashboardPanel() {
        background = ImageLoader.loadRaw("background.jpg");

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setOpaque(false);

        JLabel title = new JLabel("Welcome to Elite AutoCare Pro", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(20, 60, 120));
        title.setOpaque(true);
        title.setBackground(new Color(255, 255, 255, 200));
        add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 4, 15, 15));
        cards.setOpaque(false);
        
        cards.add(statCard("Customers", String.valueOf(DataStore.customers.size()), new Color(0x2196F3)));
        cards.add(statCard("Vehicles",  String.valueOf(DataStore.vehicles.size()),  new Color(0x4CAF50)));
        cards.add(statCard("Active Jobs", String.valueOf(
                DataStore.jobs.stream().filter(j -> !j.getStatus().equals("Completed")).count()),
                new Color(0xFF9800)));        cards.add(statCard("Total Revenue",
                "UGX " + String.format("%,.0f", DataStore.jobs.stream().mapToDouble(j -> j.getCost()).sum()),
                new Color(0x9C27B0)));
       
        add(cards, BorderLayout.CENTER);

        JLabel footer = new JLabel("\"The Best Hands for Your Car\" — since 2010", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        footer.setForeground(Color.WHITE);
        footer.setOpaque(true);
        footer.setBackground(new Color(0, 0, 0, 100)); 
        add(footer, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(255, 255, 255, 80)); 
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private JPanel statCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 32));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }
}