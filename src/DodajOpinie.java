import javax.swing.*;
import java.sql.*;

public class DodajOpinie extends JFrame {
    private JPanel panel1;
    private JComboBox<String> comboBox1; // ocena (1-5)
    private JComboBox<String> comboBox2; // obiekty (id - nazwa)
    private JTextField textField2;       // komentarz
    private JButton wyjdzButton;
    private JButton dodajButton;

    private int idUzytkownika;

    public DodajOpinie(int idUzytkownika) {
        this.idUzytkownika = idUzytkownika;

        setTitle("Dodaj opinię");
        setContentPane(panel1);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        loadOcenyDoCombo();
        loadObiektyDoCombo();

        dodajButton.addActionListener(e -> dodajOpinie());
        wyjdzButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void loadOcenyDoCombo() {
        for (int i = 1; i <= 5; i++) {
            comboBox1.addItem(String.valueOf(i));
        }
    }

    private void loadObiektyDoCombo() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekt", "root", "");
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT id_obiektu, nazwa FROM obiekty_sportowe");

            while (rs.next()) {
                int id = rs.getInt("id_obiektu");
                String nazwa = rs.getString("nazwa");
                comboBox2.addItem(id + " - " + nazwa);  // np. "3 - Hala"
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania obiektów:\n" + e.getMessage());
        }
    }

    private void dodajOpinie() {
        String komentarz = textField2.getText().trim();
        String wybranaOcena = (String) comboBox1.getSelectedItem();
        String wybranyObiekt = (String) comboBox2.getSelectedItem();

        if (komentarz.isEmpty() || wybranaOcena == null || wybranyObiekt == null) {
            JOptionPane.showMessageDialog(this, "Wypełnij wszystkie pola.");
            return;
        }

        int ocena = Integer.parseInt(wybranaOcena);
        int idObiektu = Integer.parseInt(wybranyObiekt.split(" - ")[0]);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekt", "root", "")) {
            String sql = "INSERT INTO opinie (id_uzytkownika, id_obiektu, ocena, komentarz) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idUzytkownika);
            stmt.setInt(2, idObiektu);
            stmt.setInt(3, ocena);
            stmt.setString(4, komentarz);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Opinia dodana pomyślnie.");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas dodawania opinii:\n" + e.getMessage());
        }
    }
}
