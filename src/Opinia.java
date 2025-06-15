import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class Opinia extends JFrame {

    protected JPanel panel1;
    protected JTable table1;
    private JButton dodajOpinieButton;
    private JButton wyjdzButton;
    private JButton statystykiButton;

    public Opinia() {
        setTitle("Opinie użytkowników");
        setContentPane(panel1); // panel1 z GUI Designer
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        loadOpinie();

        dodajOpinieButton.addActionListener(e -> {
            // Przykładowe ID użytkownika = 1
            DodajOpinie dodajOkno = new DodajOpinie(1);
            dodajOkno.setVisible(true);

            // Odśwież dane po zamknięciu okna dodawania opinii
            dodajOkno.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadOpinie();
                }
            });
        });

        wyjdzButton.addActionListener(e -> dispose());

        statystykiButton.addActionListener(e -> {
            // Otwieramy nowe okno statystyk
            StatystykiOpinii statWindow = new StatystykiOpinii();
            statWindow.setVisible(true);
        });
    }

    protected void loadOpinie() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID opinii", "ID użytkownika", "ID obiektu", "Ocena", "Komentarz", "Data"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // blokujemy edycję komórek
            }
        };

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projekt", "root", "")) {
            String sql = "SELECT id_opinii, id_uzytkownika, id_obiektu, ocena, komentarz, data_dodania FROM opinie";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_opinii"),
                        rs.getInt("id_uzytkownika"),
                        rs.getInt("id_obiektu"),
                        rs.getInt("ocena"),
                        rs.getString("komentarz"),
                        rs.getTimestamp("data_dodania")
                });
            }

            table1.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Błąd przy pobieraniu danych:\n" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Opinia::new);
    }
}
