import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Rezerwacje_user extends Front_user {

    private JTable rezerwacjeTable;
    private JPanel rezerwacjePanel;

    public Rezerwacje_user(int idUzytkownika) {
        super(); // wywołuje konstruktor Front_user, ale zaraz nadpiszemy GUI

        // Nowy widok (oddzielny od Front_user)
        setTitle("Moje Rezerwacje");
        setSize(700, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        rezerwacjePanel = new JPanel(new BorderLayout());
        rezerwacjeTable = new JTable();
        rezerwacjePanel.add(new JScrollPane(rezerwacjeTable), BorderLayout.CENTER);

        setContentPane(rezerwacjePanel);

        loadRezerwacje(idUzytkownika);
    }

    private void loadRezerwacje(int idUzytkownika) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Obiekt", "Start", "Koniec", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        rezerwacjeTable.setModel(model);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekt", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT r.id_rezerwacji, o.nazwa, r.data_rozpoczecia, r.data_zakonczenia, r.status " +
                             "FROM rezerwacje r JOIN obiekty_sportowe o ON r.id_obiektu = o.id_obiektu " +
                             "WHERE r.id_uzytkownika = ? ORDER BY r.data_rozpoczecia DESC")) {

            stmt.setInt(1, idUzytkownika);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_rezerwacji"),
                        rs.getString("nazwa"),
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
}
