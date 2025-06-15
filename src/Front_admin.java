import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.DecimalFormat;

public class Front_admin extends JFrame {
    private JPanel panel1;
    private JTable table1;
    private JButton dodajButton;
    private JButton edytujButton;
    private JButton usunButton;
    private JButton rezerwacjeButton;

    public Front_admin() {
        setTitle("Panel Administratora");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(panel1); // panel1 pochodzi z GUI Designer

        loadObiekty();

        dodajButton.addActionListener(e -> {
            DodajObiekt dodajOkno = new DodajObiekt(this);
            dodajOkno.setVisible(true);
        });

        edytujButton.addActionListener(e -> {
            int row = table1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Wybierz obiekt do edycji.");
                return;
            }

            int id = (int) table1.getValueAt(row, 0);
            String nazwa = table1.getValueAt(row, 1).toString();
            String typ = table1.getValueAt(row, 2).toString();
            double cena;

            try {
                // Usuwamy " zł / godz." i zamieniamy przecinek na kropkę (jeśli jest)
                String cenaTekst = table1.getValueAt(row, 3).toString().replace(" zł / godz.", "").replace(',', '.');
                cena = Double.parseDouble(cenaTekst);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nieprawidłowa cena.");
                return;
            }

            EdytujObiekt edytujOkno = new EdytujObiekt(this, id, nazwa, typ, cena);
            edytujOkno.setVisible(true);
        });

        usunButton.addActionListener(e -> usunObiekt());

        rezerwacjeButton.addActionListener(e -> {
            Rezerwacje_admin rezerwacjeWindow = new Rezerwacje_admin(this);
            rezerwacjeWindow.setVisible(true);
        });
    }

    public void loadObiekty() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nazwa", "Typ", "Cena"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table1.setModel(model);

        DecimalFormat df = new DecimalFormat("#0.00");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekt", "root", "")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM obiekty_sportowe");

            while (rs.next()) {
                double cena = rs.getDouble("cena_za_godzine");
                String cenaSformatowana = df.format(cena) + " zł / godz.";

                model.addRow(new Object[]{
                        rs.getInt("id_obiektu"),
                        rs.getString("nazwa"),
                        rs.getString("typ"),
                        cenaSformatowana
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania obiektów.");
        }
    }

    private void usunObiekt() {
        int row = table1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz obiekt do usunięcia.");
            return;
        }

        int id = (int) table1.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Czy na pewno usunąć obiekt?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekt", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM obiekty_sportowe WHERE id_obiektu = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            loadObiekty();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas usuwania obiektu.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Front_admin().setVisible(true));
    }
}
