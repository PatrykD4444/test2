import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class StatystykiOpinii extends Opinia {

    private JPanel panelStatystyki;

    public StatystykiOpinii() {
        setTitle("Statystyki opinii - wykres");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panelStatystyki = new WykresPanel();
        setContentPane(panelStatystyki);
    }


    private class WykresPanel extends JPanel {
        private Map<String, Double> dane = new LinkedHashMap<>();

        public WykresPanel() {
            loadDane();
        }

        private void loadDane() {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekt", "root", "")) {
                String sql = "SELECT o.nazwa, AVG(op.ocena) as srednia_ocena " +
                        "FROM opinie op JOIN obiekty_sportowe o ON op.id_obiektu = o.id_obiektu " +
                        "GROUP BY o.nazwa ORDER BY srednia_ocena DESC";

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String nazwa = rs.getString("nazwa");
                    double avg = rs.getDouble("srednia_ocena");
                    dane.put(nazwa, avg);
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Błąd podczas ładowania statystyk:\n" + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (dane.isEmpty()) {
                g.drawString("Brak danych do wyświetlenia", 20, 20);
                return;
            }

            Graphics2D g2 = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();

            int padding = 50;
            int labelPadding = 50;
            int barWidth = (width - 2 * padding) / dane.size();

            double maxValue = 5.0; // max ocena
            int i = 0;

            // Rysowanie osi Y i wartości
            g2.drawLine(padding, height - padding, padding, padding);
            g2.drawLine(padding, height - padding, width - padding, height - padding);

            for (int y = 0; y <= maxValue; y++) {
                int yPos = (int) (height - padding - (y / maxValue) * (height - 2 * padding));
                g2.drawLine(padding - 5, yPos, padding, yPos);
                g2.drawString(String.valueOf(y), padding - 30, yPos + 5);
            }

            // Rysowanie słupków
            for (Map.Entry<String, Double> entry : dane.entrySet()) {
                String nazwa = entry.getKey();
                double ocena = entry.getValue();

                int x = padding + i * barWidth + 10;
                int barHeight = (int) ((ocena / maxValue) * (height - 2 * padding));
                int y = height - padding - barHeight;

                g2.setColor(Color.BLUE);
                g2.fillRect(x, y, barWidth - 20, barHeight);

                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, barWidth - 20, barHeight);


                g2.drawString(nazwa, x, height - padding + 20);

                g2.drawString(String.format("%.2f", ocena), x, y - 5);

                i++;
            }
        }
    }
}
