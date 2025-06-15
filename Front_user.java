import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Front_user extends JFrame {
    private JPanel panel1;
    private JButton zarezerwujButton;
    private JButton cofnijButton;
    private JButton mojeRezewacjeButton;
    private JButton opiniaButton;
    private JTable table1;

    protected int idZalogowanegoUzytkownika = 2; // przykładowy ID

    public Front_user() {
        setTitle("Panel użytkownika");
        setContentPane(panel1);
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadObiekty();

        zarezerwujButton.addActionListener(e -> zarezerwujObiekt());

        mojeRezewacjeButton.addActionListener(e -> {
            Rezerwacje_user rezerwacjeUser = new Rezerwacje_user(idZalogowanegoUzytkownika);
            rezerwacjeUser.setVisible(true);
        });

        opiniaButton.addActionListener(e -> {
            Opinia opinia = new Opinia();
            opinia.setVisible(true);
        });

        cofnijButton.addActionListener(e -> dispose());
    }

    private void loadObiekty() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nazwa", "Typ", "Cena"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table1.setModel(model);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekt", "root", "")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM obiekty_sportowe");

            while (rs.next()) {
                int id = rs.getInt("id_obiektu");
                String nazwa = rs.getString("nazwa");
                String typ = rs.getString("typ");
                double cena = rs.getDouble("cena_za_godzine");
                String cenaFormatted = String.format("%.2f zł / godz.", cena);
                model.addRow(new Object[]{id, nazwa, typ, cenaFormatted});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd podczas ładowania obiektów.");
        }
    }

    private void zarezerwujObiekt() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz obiekt do rezerwacji!");
            return;
        }

        int idObiektu = (int) table1.getValueAt(selectedRow, 0);

        JSpinner startSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner endSpinner = new JSpinner(new SpinnerDateModel());

        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startSpinner, "yyyy-MM-dd HH:mm");
        startSpinner.setEditor(startEditor);
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endSpinner, "yyyy-MM-dd HH:mm");
        endSpinner.setEditor(endEditor);

        startSpinner.setValue(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) startSpinner.getValue());
        cal.add(Calendar.HOUR_OF_DAY, 1);
        endSpinner.setValue(cal.getTime());

        JPanel panel = new JPanel();
        panel.add(new JLabel("Data rozpoczęcia:"));
        panel.add(startSpinner);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Data zakończenia:"));
        panel.add(endSpinner);

        int result = JOptionPane.showConfirmDialog(this, panel, "Wybierz czas rezerwacji",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Date startDate = (Date) startSpinner.getValue();
            Date endDate = (Date) endSpinner.getValue();

            if (!startDate.before(endDate)) {
                JOptionPane.showMessageDialog(this, "Data zakończenia musi być późniejsza niż rozpoczęcia!");
                return;
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String start = formatter.format(startDate);
            String end = formatter.format(endDate);

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekt", "root", "")) {
                String sql = "INSERT INTO rezerwacje (id_obiektu, id_uzytkownika, data_rozpoczecia, data_zakonczenia, status) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, idObiektu);
                stmt.setInt(2, idZalogowanegoUzytkownika);
                stmt.setString(3, start);
                stmt.setString(4, end);
                stmt.setString(5, "oczekująca");

                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Zarezerwowano obiekt!");

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Błąd podczas rezerwacji.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Front_user().setVisible(true));
    }
}
