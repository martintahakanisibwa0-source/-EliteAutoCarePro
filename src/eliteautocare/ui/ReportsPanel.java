package eliteautocare.ui;

import eliteautocare.model.DataStore;
import eliteautocare.model.ServiceJob;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ReportsPanel extends JPanel {

    public ReportsPanel() {
        setLayout(new BorderLayout(20, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // ── TOP HEADER & DROPDOWNS ──
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("📈 Business Trends & Earnings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(title, BorderLayout.WEST);

        // Controls Panel (holds both dropdowns)
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        String[] viewModes = {"Bar Chart", "Line Chart"};
        JComboBox<String> viewBox = new JComboBox<>(viewModes);
        viewBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        String[] timeframes = {
            "Daily (Last 7 Days)", 
            "Weekly (Last 4 Weeks)", 
            "Monthly (This Year)", 
            "Yearly (Last 5 Years)"
        };
        JComboBox<String> timeBox = new JComboBox<>(timeframes);
        timeBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        controls.add(new JLabel("View:"));
        controls.add(viewBox);
        controls.add(new JLabel("Timeframe:"));
        controls.add(timeBox);
        
        headerPanel.add(controls, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // ── MIDDLE CARDS ──
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        double todayEarnings = getEarningsForDate(today);
        double yesterdayEarnings = getEarningsForDate(yesterday);
        
        String trendText = (todayEarnings >= yesterdayEarnings) ? "📈 TRENDING UP" : "📉 TRENDING DOWN";
        Color trendColor = (todayEarnings >= yesterdayEarnings) ? new Color(0x4CAF50) : new Color(0xE53935);

        JPanel cardsGrid = new JPanel(new GridLayout(1, 4, 15, 15));
        cardsGrid.add(createCard("Today", "UGX " + String.format("%,.0f", todayEarnings), new Color(0x2196F3)));
        cardsGrid.add(createCard("Yesterday", "UGX " + String.format("%,.0f", yesterdayEarnings), new Color(0x9E9E9E)));
        cardsGrid.add(createCard("Lifetime Total", "UGX " + String.format("%,.0f", getTotalEarnings()), new Color(0x9C27B0)));
        
        JPanel trendCard = new JPanel(new BorderLayout());
        trendCard.setBackground(trendColor);
        trendCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel trendLbl = new JLabel(trendText);
        trendLbl.setForeground(Color.WHITE);
        trendLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        trendCard.add(trendLbl, BorderLayout.CENTER);
        cardsGrid.add(trendCard);

        add(cardsGrid, BorderLayout.CENTER);

        // ── BOTTOM GRAPH ──
        TrendGraphPanel graph = new TrendGraphPanel();
        graph.setPreferredSize(new Dimension(800, 250));
        add(graph, BorderLayout.SOUTH);

        // ── LINK CONTROLS TO GRAPH ──
        timeBox.addActionListener(e -> graph.setTimeframe((String) timeBox.getSelectedItem()));
        viewBox.addActionListener(e -> graph.setViewMode((String) viewBox.getSelectedItem()));
    }

    private double getEarningsForDate(LocalDate date) {
        return DataStore.jobs.stream()
                .filter(j -> j.getStatus().equals("Completed") && j.getDate().equals(date))
                .mapToDouble(ServiceJob::getCost)
                .sum();
    }

    private double getTotalEarnings() {
        return DataStore.jobs.stream()
                .filter(j -> j.getStatus().equals("Completed"))
                .mapToDouble(ServiceJob::getCost)
                .sum();
    }

    private JPanel createCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }
}