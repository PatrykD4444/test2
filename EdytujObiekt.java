import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EdytujObiekt extends JFrame {
    private Front_admin parent;
    private int id;
    private JTextField nazwaField;
    private JTextField typField;
    private JTextField cenaField;
    private JButton zapiszBtn;

    public EdytujObiekt(Front_admin parent, int id, String nazwa, String typ, double cena) {
        this.parent = parent;
        this.id = id;

        setTitle("Edytuj Obiekt");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Nazwa:"));
        nazwaField = new JTextField(nazwa);
        add(nazwaField);

        add(new JLabel("Typ:"));
        typField = new JTextField(typ);
        add(typField);

        add(new JLabel("Cena za godzinę:"));
        cenaField = new JTextField(String.valueOf(cena));
        add(cenaField);

        zapiszBtn = new JButton("Zapisz");
        add(new JLabel());
        add(zapiszBtn);

        zapiszBtn.addActionListener(e -> zapiszZmiany());
    }

    private void zapiszZmiany() {
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
                    "UPDATE obiekty_sportowe SET nazwa = ?, typ = ?, cena_za_godzine = ? WHERE id_obiektu = ?");
            stmt.setString(1, nazwa);
            stmt.setString(2, typ);
            stmt.setDouble(3, cena);
            stmt.setInt(4, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Zapisano zmiany!");
            parent.loadObiekty();  // ODŚWIEŻENIE tabeli w Front_admin
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas zapisywania zmian.");
        }
    }
}
