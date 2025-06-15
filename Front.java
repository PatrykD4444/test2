import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Front extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton zalogujButton;
    private JButton zarejestrujButton;
    private JButton wyjdzButton;

    public Front() {
        setTitle("Logowanie");
        setSize(400, 200);
        setContentPane(panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        zalogujButton.addActionListener(e -> {
            String email = textField1.getText();
            String haslo = new String(passwordField1.getPassword());

            if (sprawdzLogowanie(email, haslo)) {

            } else {
                JOptionPane.showMessageDialog(null, "Błędny email lub hasło");
            }
        });

        zarejestrujButton.addActionListener(e -> {
            Rejestracja rejestracja = new Rejestracja();
            rejestracja.setVisible(true);
        });

        wyjdzButton.addActionListener(e -> System.exit(0));
    }

    private boolean sprawdzLogowanie(String email, String haslo) {
        final String URL = "jdbc:mysql://localhost:3306/projekt";
        final String USER = "root";
        final String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT rola FROM uzytkownicy WHERE email = ? AND haslo = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, haslo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String rola = rs.getString("rola");
                if ("admin".equalsIgnoreCase(rola)) {
                    JOptionPane.showMessageDialog(null, "Witaj Administratorze!");
                    dispose();
                    Front_admin frontAdmin = new Front_admin();
                    frontAdmin.setVisible(true);
                } else if ("uzytkownik".equalsIgnoreCase(rola)) {
                    JOptionPane.showMessageDialog(null, "Witaj Użytkowniku!");
                    dispose();
                    Front_user frontUser = new Front_user();
                    frontUser.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Rola nierozpoznana.");
                }
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Błąd połączenia z bazą danych!");
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Front().setVisible(true));
    }
}