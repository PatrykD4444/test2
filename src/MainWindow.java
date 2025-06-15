import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainWindow() {
        setTitle("Aplikacja Zintegrowana");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new FrontPanel(this), "FRONT");
        mainPanel.add(new AdminPanel(this), "ADMIN");
        mainPanel.add(new UserPanel(this), "USER");
        mainPanel.add(new DodajObiektPanel(this), "DODAJ_OBIEKT");
        mainPanel.add(new EdytujObiektPanel(this), "EDYTUJ_OBIEKT");
        mainPanel.add(new DodajOpiniePanel(this), "DODAJ_OPINIE");
        mainPanel.add(new RejestracjaPanel(this), "REJESTRACJA");
        mainPanel.add(new RezerwacjeAdminPanel(this), "REZERWACJE_ADMIN");
        mainPanel.add(new RezerwacjeUserPanel(this), "REZERWACJE_USER");
        mainPanel.add(new RegulaminPanel(this), "REGULAMIN");
        mainPanel.add(new StatystykiOpiniiPanel(this), "STATYSTYKI");

        add(mainPanel);
        setVisible(true);
    }

    public void switchTo(String name) {
        cardLayout.show(mainPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}

class FrontPanel extends JPanel {
    public FrontPanel(MainWindow parent) {
        setLayout(new GridLayout(0, 1));
        add(new JLabel("Witaj w aplikacji!", SwingConstants.CENTER));

        JButton loginAdmin = new JButton("Zaloguj jako admin");
        loginAdmin.addActionListener(e -> parent.switchTo("ADMIN"));
        add(loginAdmin);

        JButton loginUser = new JButton("Zaloguj jako user");
        loginUser.addActionListener(e -> parent.switchTo("USER"));
        add(loginUser);

        JButton rejestracja = new JButton("Zarejestruj się");
        rejestracja.addActionListener(e -> parent.switchTo("REJESTRACJA"));
        add(rejestracja);
    }
}

class AdminPanel extends JPanel {
    public AdminPanel(MainWindow parent) {
        setLayout(new GridLayout(0, 1));
        add(new JLabel("Panel Admina"));

        JButton edytujObiekt = new JButton("Edytuj obiekt");
        edytujObiekt.addActionListener(e -> parent.switchTo("EDYTUJ_OBIEKT"));
        add(edytujObiekt);

        JButton dodajObiekt = new JButton("Dodaj obiekt");
        dodajObiekt.addActionListener(e -> parent.switchTo("DODAJ_OBIEKT"));
        add(dodajObiekt);

        JButton rezerwacje = new JButton("Zobacz rezerwacje");
        rezerwacje.addActionListener(e -> parent.switchTo("REZERWACJE_ADMIN"));
        add(rezerwacje);

        JButton statystyki = new JButton("Statystyki opinii");
        statystyki.addActionListener(e -> parent.switchTo("STATYSTYKI"));
        add(statystyki);

        JButton back = new JButton("Wyloguj");
        back.addActionListener(e -> parent.switchTo("FRONT"));
        add(back);
    }
}

class UserPanel extends JPanel {
    public UserPanel(MainWindow parent) {
        setLayout(new GridLayout(0, 1));
        add(new JLabel("Panel Użytkownika"));

        JButton dodajOpinie = new JButton("Dodaj opinię");
        dodajOpinie.addActionListener(e -> parent.switchTo("DODAJ_OPINIE"));
        add(dodajOpinie);

        JButton rezerwacje = new JButton("Zobacz swoje rezerwacje");
        rezerwacje.addActionListener(e -> parent.switchTo("REZERWACJE_USER"));
        add(rezerwacje);

        JButton regulamin = new JButton("Pokaż regulamin");
        regulamin.addActionListener(e -> parent.switchTo("REGULAMIN"));
        add(regulamin);

        JButton back = new JButton("Wyloguj");
        back.addActionListener(e -> parent.switchTo("FRONT"));
        add(back);
    }
}

class DodajObiektPanel extends JPanel {
    public DodajObiektPanel(MainWindow parent) {
        setLayout(new BorderLayout());
        add(new JLabel("Dodaj Obiekt - miejsce na formularz"), BorderLayout.CENTER);

        JButton back = new JButton("Powrót");
        back.addActionListener(e -> parent.switchTo("ADMIN"));
        add(back, BorderLayout.SOUTH);
    }
}

class EdytujObiektPanel extends JPanel {
    public EdytujObiektPanel(MainWindow parent) {
        setLayout(new BorderLayout());
        add(new JLabel("Edytuj Obiekt - wybierz i zmodyfikuj"), BorderLayout.CENTER);

        JButton back = new JButton("Powrót");
        back.addActionListener(e -> parent.switchTo("ADMIN"));
        add(back, BorderLayout.SOUTH);
    }
}

class DodajOpiniePanel extends JPanel {
    public DodajOpiniePanel(MainWindow parent) {
        setLayout(new BorderLayout());
        add(new JLabel("Dodaj Opinię - formularz dodawania"), BorderLayout.CENTER);

        JButton back = new JButton("Powrót");
        back.addActionListener(e -> parent.switchTo("USER"));
        add(back, BorderLayout.SOUTH);
    }
}

class RejestracjaPanel extends JPanel {
    public RejestracjaPanel(MainWindow parent) {
        setLayout(new BorderLayout());
        add(new JLabel("Rejestracja użytkownika"), BorderLayout.CENTER);

        JButton back = new JButton("Powrót");
        back.addActionListener(e -> parent.switchTo("FRONT"));
        add(back, BorderLayout.SOUTH);
    }
}

class RezerwacjeAdminPanel extends JPanel {
    public RezerwacjeAdminPanel(MainWindow parent) {
        setLayout(new BorderLayout());
        add(new JLabel("Lista rezerwacji (admin)"), BorderLayout.CENTER);

        JButton back = new JButton("Powrót");
        back.addActionListener(e -> parent.switchTo("ADMIN"));
        add(back, BorderLayout.SOUTH);
    }
}

class RezerwacjeUserPanel extends JPanel {
    public RezerwacjeUserPanel(MainWindow parent) {
        setLayout(new BorderLayout());
        add(new JLabel("Twoje rezerwacje"), BorderLayout.CENTER);

        JButton back = new JButton("Powrót");
        back.addActionListener(e -> parent.switchTo("USER"));
        add(back, BorderLayout.SOUTH);
    }
}

class RegulaminPanel extends JPanel {
    public RegulaminPanel(MainWindow parent) {
        setLayout(new BorderLayout());
        add(new JLabel("Regulamin - tutaj będzie tekst regulaminu"), BorderLayout.CENTER);

        JButton back = new JButton("Powrót");
        back.addActionListener(e -> parent.switchTo("USER"));
        add(back, BorderLayout.SOUTH);
    }
}

class StatystykiOpiniiPanel extends JPanel {
    public StatystykiOpiniiPanel(MainWindow parent) {
        setLayout(new BorderLayout());
        add(new JLabel("Statystyki opinii - dane statystyczne"), BorderLayout.CENTER);

        JButton back = new JButton("Powrót");
        back.addActionListener(e -> parent.switchTo("ADMIN"));
        add(back, BorderLayout.SOUTH);
    }
}
