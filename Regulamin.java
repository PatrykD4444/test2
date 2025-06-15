import javax.swing.*;

public class Regulamin extends Rejestracja {

    public Regulamin() {
        super(); // Dziedziczymy wszystko, ale nadpisujemy GUI

        setTitle("Regulamin");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Czyścimy panel odziedziczony z Rejestracja
        panel1.removeAll();
        panel1.setLayout(null); // Możesz użyć innego layoutu, jeśli wolisz

        JTextArea regulaminText = new JTextArea(
                "REGULAMIN KONTA:\n\n" +
                        "1. Nie udostępniaj swojego hasła.\n" +
                        "2. Konto służy tylko do użytku osobistego.\n" +
                        "3. Naruszenie zasad może skutkować blokadą konta.\n" +
                        "4. Administracja może zmienić regulamin w każdej chwili.\n" +
                        "5. Korzystając z konta, akceptujesz ten regulamin."
        );
        regulaminText.setEditable(false);
        regulaminText.setLineWrap(true);
        regulaminText.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(regulaminText);
        scrollPane.setBounds(10, 10, 360, 200);
        panel1.add(scrollPane);

        JButton powrotButton = new JButton("Wstecz");
        powrotButton.setBounds(150, 220, 100, 30);
        panel1.add(powrotButton);

        powrotButton.addActionListener(e -> dispose());

        setContentPane(panel1);
        revalidate();
        repaint();
    }
}
