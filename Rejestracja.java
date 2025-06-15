import javax.swing.*;
import java.sql.*;

public class Rejestracja extends JFrame {
    protected JPanel panel1;
    protected JTextField textField1;
    protected JTextField textField2;
    protected JTextField textField3;
    protected JTextField textField4;
    protected JPasswordField passwordField1;
    protected JButton utworzButton;
    protected JButton wsteczButton;
    protected JButton regulaminButton;

    public Rejestracja() {
        setTitle("Rejestracja");
        setSize(400, 350);
        setContentPane(panel1);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        utworzButton.addActionListener(e -> zarejestrujUzytkownika());
        wsteczButton.addActionListener(e -> dispose());

        // Dodajemy tylko tę jedną akcję — otwiera klasę Regulamin
        regulaminButton.addActionListener(e -> {
            new Regulamin().setVisible(true);
        });
    }

    private void zarejestrujUzytkownika() {
        String imie = textField1.getText().trim();
        String nazwisko = textField2.getText().trim();
        String email = textField3.getText().trim();
        String telefon = textField4.getText().trim();
        String haslo = new String(passwordField1.getPassword()).trim();

        if(imie.isEmpty() || nazwisko.isEmpty() || email.isEmpty() || telefon.isEmpty() || haslo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Wszystkie pola muszą być wypełnione");
            return;
        }

        if (dodajUzytkownika(imie, nazwisko, email, telefon, haslo)) {
            JOptionPane.showMessageDialog(this, "Zarejestrowano pomyślnie");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Rejestracja nie powiodła się");
        }
    }

    private boolean dodajUzytkownika(String imie, String nazwisko, String email, String telefon, String haslo) {
        final String URL = "jdbc:mysql://localhost:3306/projekt";
        final String USER = "root";
        final String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO uzytkownicy (imie, nazwisko, email, numer_telefonu, rola, haslo) VALUES (?, ?, ?, ?, 'uzytkownik', ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, imie);
            stmt.setString(2, nazwisko);
            stmt.setString(3, email);
            stmt.setString(4, telefon);
            stmt.setString(5, haslo);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas rejestracji: " + ex.getMessage());
            return false;
        }
    }
}
