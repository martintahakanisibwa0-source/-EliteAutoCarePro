package eliteautocare.ui;

import eliteautocare.model.DataStore;
import eliteautocare.model.ServiceJob;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TrendGraphPanel extends JPanel {

    private String timeframe = "Daily (Last 7 Days)";
    private String viewMode = "Bar Chart"; // Default mode

    public TrendGraphPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Earnings Trend"));
    }

    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
        repaint();
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        // Enable anti-aliasing for smooth lines and text
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 40;
        int graphWidth = width - (padding * 2);
        int graphHeight = height - (padding * 2);

        int numPoints = 0;
        double[] earnings = null;
        String[] labels = null;
        double maxEarning = 1;

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter;

        // ── CALCULATE DATA BASED ON TIMEFRAME ──
        if (timeframe.equals("Daily (Last 7 Days)")) {
            numPoints = 7;
            earnings = new double[7];
            labels = new String[7];
            formatter = DateTimeFormatter.ofPattern("EEE");

            for (int i = 0; i < 7; i++) {
                LocalDate d = today.minusDays(6 - i);
                labels[i] = d.format(formatter);
                earnings[i] = getEarningsForDate(d);
                if (earnings[i] > maxEarning) maxEarning = earnings[i];
            }

        } else if (timeframe.equals("Weekly (Last 4 Weeks)")) {
            numPoints = 4;
            earnings = new double[4];
            labels = new String[4];

            for (int i = 0; i < 4; i++) {
                LocalDate endOfWeek = today.minusWeeks(3 - i);
                LocalDate startOfWeek = endOfWeek.minusDays(6);
                labels[i] = "Wk " + (i + 1);
                
                double weekTotal = 0;
                for (LocalDate d = startOfWeek; !d.isAfter(endOfWeek); d = d.plusDays(1)) {
                    weekTotal += getEarningsForDate(d);
                }
                earnings[i] = weekTotal;
                if (weekTotal > maxEarning) maxEarning = weekTotal;
            }

        } else if (timeframe.equals("Monthly (This Year)")) {
            numPoints = 12;
            earnings = new double[12];
            labels = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            for (int i = 0; i < 12; i++) {
                final int month = i + 1;
                double monthTotal = DataStore.jobs.stream()
                        .filter(j -> j.getStatus().equals("Completed") 
                                && j.getDate().getYear() == today.getYear() 
                                && j.getDate().getMonthValue() == month)
                        .mapToDouble(ServiceJob::getCost)
                        .sum();
                earnings[i] = monthTotal;
                if (monthTotal > maxEarning) maxEarning = monthTotal;
            }

        } else if (timeframe.equals("Yearly (Last 5 Years)")) {
            numPoints = 5;
            earnings = new double[5];
            labels = new String[5];

            for (int i = 0; i < 5; i++) {
                int year = today.getYear() - (4 - i);
                labels[i] = String.valueOf(year);
                double yearTotal = DataStore.jobs.stream()
                        .filter(j -> j.getStatus().equals("Completed") && j.getDate().getYear() == year)
                        .mapToDouble(ServiceJob::getCost)
                        .sum();
                earnings[i] = yearTotal;
                if (yearTotal > maxEarning) maxEarning = yearTotal;
            }
        }

        // ── DRAW AXES ──
        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(padding, padding, padding, height - padding); // Y-axis
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X-axis

        if (numPoints > 0) {
            int slotWidth = graphWidth / numPoints;

            if (viewMode.equals("Bar Chart")) {
                // ── DRAW BAR CHART ──
                int barWidth = slotWidth - (numPoints > 7 ? 4 : 15);
                
                for (int i = 0; i < numPoints; i++) {
                    int x = padding + (i * slotWidth) + ((slotWidth - barWidth) / 2);
                    int barHeight = (int) ((earnings[i] / maxEarning) * graphHeight);
                    int y = (height - padding) - barHeight;

                    if (earnings[i] > 0) {
                        g2.setColor(new Color(0x2196F3));
                        g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                        
                        if (numPoints <= 7) {
                            g2.setColor(Color.BLACK);
                            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                            String amt = String.format("%.0f", earnings[i]);
                            g2.drawString(amt, x + (barWidth / 2) - (amt.length() * 3), y - 5);
                        }
                    }
                }
            } else {
                // ── DRAW LINE CHART ──
                Path2D.Double path = new Path2D.Double();
                g2.setColor(new Color(0x2196F3)); // Blue line
                g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                for (int i = 0; i < numPoints; i++) {
                    int x = padding + (i * slotWidth) + (slotWidth / 2);
                    int y = (height - padding) - (int) ((earnings[i] / maxEarning) * graphHeight);

                    if (i == 0) path.moveTo(x, y);
                    else path.lineTo(x, y);

                    // Draw data points (circles)
                    g2.fillOval(x - 5, y - 5, 10, 10);
                    
                    // Draw amount above point
                    if (earnings[i] > 0 && numPoints <= 7) {
                        g2.setColor(Color.BLACK);
                        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                        String amt = String.format("%.0f", earnings[i]);
                        g2.drawString(amt, x - (amt.length() * 3), y - 15);
                        g2.setColor(new Color(0x2196F3)); // Reset color
                    }
                }
                g2.draw(path);
            }

            // ── DRAW X-AXIS LABELS ──
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            FontMetrics fm = g2.getFontMetrics();
            
            for (int i = 0; i < numPoints; i++) {
                int x = padding + (i * slotWidth) + (slotWidth / 2);
                int labelWidth = fm.stringWidth(labels[i]);

                if (numPoints == 12) {
                    // Rotate labels for 12 months
                    Graphics2D g2d = (Graphics2D) g2.create();
                    g2d.translate(x + 5, height - padding + 15);
                    g2d.rotate(Math.toRadians(-45));
                    g2d.drawString(labels[i], 0, 0);
                    g2d.dispose();
                } else {
                    g2.drawString(labels[i], x - (labelWidth / 2), height - padding + 20);
                }
            }
        }
    }

    private double getEarningsForDate(LocalDate date) {
        return DataStore.jobs.stream()
                .filter(j -> j.getStatus().equals("Completed") && j.getDate().equals(date))
                .mapToDouble(ServiceJob::getCost)
                .sum();
    }
}