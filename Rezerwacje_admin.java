import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Rezerwacje_admin extends JFrame {
    private JPanel panel1;
    protected JTable table1;
    private JButton akceptujButton;
    private JButton odrzućButton;
    private JButton wsteczButton;
    private JButton zajeteTerminyButton;

    public Rezerwacje_admin(JFrame parent) {
        setTitle("Rezerwacje");
        setContentPane(panel1);
        setSize(700, 400);
        setLocationRelativeTo(parent);

        loadRezerwacje();

        akceptujButton.addActionListener(e -> zmienStatus("zaakceptowana"));
        odrzućButton.addActionListener(e -> zmienStatus("odrzucona"));
        wsteczButton.addActionListener(e -> dispose());

        zajeteTerminyButton.addActionListener(e -> {
            ZajeteTerminy zajeteTerminy = new ZajeteTerminy(this);
            zajeteTerminy.setVisible(true);
        });
    }

    public void loadRezerwacje() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Obiekt", "Użytkownik", "Start", "Koniec", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Blokuj edytowanie wszystkich komórek przez użytkownika
            }
        };

        table1.setModel(model);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekt", "root", "");
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(
                    "SELECT r.id_rezerwacji, o.nazwa, r.id_uzytkownika, r.data_rozpoczecia, r.data_zakonczenia, r.status " +
                            "FROM rezerwacje r JOIN obiekty_sportowe o ON r.id_obiektu = o.id_obiektu");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_rezerwacji"),
                        rs.getString("nazwa"),
                        rs.getInt("id_uzytkownika"),
                        rs.getString("data_rozpoczecia"),
                        rs.getString("data_zakonczenia"),
                        rs.getString("status")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania rezerwacji.");
        }
    }

    protected void zmienStatus(String nowyStatus) {
        int row = table1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz rezerwację.");
            return;
        }

        int id = (int) table1.getValueAt(row, 0);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekt", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE rezerwacje SET status = ? WHERE id_rezerwacji = ?");
            stmt.setString(1, nowyStatus);
            stmt.setInt(2, id);
            stmt.executeUpdate();

            // Aktualizacja statusu w modelu tabeli, aby odzwierciedlić zmianę w UI
            table1.setValueAt(nowyStatus, row, 5);
            JOptionPane.showMessageDialog(this, "Status zmieniony na " + nowyStatus);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas zmiany statusu.");
        }
    }
}
