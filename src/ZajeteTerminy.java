import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ZajeteTerminy extends Rezerwacje_admin {

    public ZajeteTerminy(JFrame parent) {
        super(parent);
        setTitle("Zajęte Terminy");

        // Przeładuj dane, aby pokazać tylko zaakceptowane rezerwacje
        loadZajeteTerminy();


    }

    private void loadZajeteTerminy() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Obiekt", "Użytkownik", "Start", "Koniec", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table1.setModel(model);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekt", "root", "");
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(
                    "SELECT r.id_rezerwacji, o.nazwa, r.id_uzytkownika, r.data_rozpoczecia, r.data_zakonczenia, r.status " +
                            "FROM rezerwacje r JOIN obiekty_sportowe o ON r.id_obiektu = o.id_obiektu " +
                            "WHERE r.status = 'zaakceptowana' " +
                            "ORDER BY o.nazwa, r.data_rozpoczecia"
            );

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
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania zajętych terminów.");
        }
    }

    @Override
    protected void zmienStatus(String nowyStatus) {
        JOptionPane.showMessageDialog(this, "Nie można zmieniać statusu w widoku zajętych terminów.");
    }
}
