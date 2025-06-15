import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DodajObiekt extends JFrame {
    private Front_admin parent;
    private JTextField nazwaField;
    private JTextField typField;
    private JTextField cenaField;
    private JButton dodajBtn;

    public DodajObiekt(Front_admin parent) {
        this.parent = parent;

        setTitle("Dodaj Obiekt");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Nazwa:"));
        nazwaField = new JTextField();
        add(nazwaField);

        add(new JLabel("Typ:"));
        typField = new JTextField();
        add(typField);

        add(new JLabel("Cena za godzinę:"));
        cenaField = new JTextField();
        add(cenaField);

        dodajBtn = new JButton("Dodaj");
        add(new JLabel());
        add(dodajBtn);

        dodajBtn.addActionListener(e -> dodajObiekt());
    }

    private void dodajObiekt() {
        String nazwa = nazwaField.getText().trim();
        String typ = typField.getText().trim();
        String cenaStr = cenaField.getText().trim();

        if (nazwa.isEmpty() || typ.isEmpty() || cenaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Wypełnij wszystkie pola!");
            return;
        }

        double cena;
        try {
            cena = Double.parseDouble(cenaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nieprawidłowa cena.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekt", "root", "")) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO obiekty_sportowe (nazwa, typ, cena_za_godzine) VALUES (?, ?, ?)");
            stmt.setString(1, nazwa);
            stmt.setString(2, typ);
            stmt.setDouble(3, cena);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Dodano obiekt!");
            parent.loadObiekty();
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas dodawania obiektu.");
        }
    }
}
