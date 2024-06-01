import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainFrame extends javax.swing.JFrame {

    private static final String url = "jdbc:mysql://localhost:3306/akademik";
    private static final String user = "root";
    private static final String password = ""; 

    private static Connection connection;

    // Komponen GUI
    private JTextField NIMField;
    private JTextField NAMAField;
    private JTextField ANGKATANField;
    private JTextField KOTA_LAHIRField;
    private JTextField JENIS_KELAMINField;
    private JTextArea displayArea;
    private JButton addButton;

    public MainFrame() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // mengatur frame GUI
        setTitle("DATA MAHASISWA");
        setSize(800, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Form input
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        NIMField = new JTextField();
        NAMAField = new JTextField();
        ANGKATANField = new JTextField();
        JENIS_KELAMINField = new JTextField();
        KOTA_LAHIRField = new JTextField();
        inputPanel.add(new JLabel("Nim:"));
        inputPanel.add(NIMField);
        inputPanel.add(new JLabel("Nama:"));
        inputPanel.add(NAMAField);
        inputPanel.add(new JLabel("Angkatan:"));
        inputPanel.add(ANGKATANField);
        inputPanel.add(new JLabel("Kota Lahir:"));
        inputPanel.add(KOTA_LAHIRField);
        inputPanel.add(new JLabel("Jenis Kelamin:"));
        inputPanel.add(JENIS_KELAMINField);

        addButton = new JButton("Add");
        inputPanel.add(new JLabel()); 
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        loadData();

        addButton.addActionListener(e -> {
            String NIM = NIMField.getText();
            int ANGKATAN = Integer.parseInt(ANGKATANField.getText());
            String NAMA = NAMAField.getText();
            String KOTA_LAHIR = KOTA_LAHIRField.getText();
            String charString = JENIS_KELAMINField.getText();
            char jk= charString.isEmpty() ? '\0' : charString.charAt(0);

            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO mahasiswa.MAHASISWA (NIM, NAMA, ANGKATAN, KOTA_LAHIR, JENIS_KELAMIN) VALUES (?, ?, ?, ?, ?)")) {
                statement.setString(1, NIM);
                statement.setString(2, NAMA);
                statement.setInt(3, ANGKATAN);
                statement.setString(4, KOTA_LAHIR);
                statement.setString(5, String.valueOf(jk));
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data added successfully!");
                loadData();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding data: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    // Method untuk mengambil data dari database
    private void loadData() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM mahasiswa.MAHASISWA")) {

            StringBuilder data = new StringBuilder();
            while (resultSet.next()) {
                String NIM = resultSet.getString("NIM");
                String NAMA = resultSet.getString("NAMA");
                String KOTA_LAHIR = resultSet.getString("KOTA_LAHIR");
                String JENIS_KELAMIN = resultSet.getString("JENIS_KELAMIN");
                int ANGKATAN = resultSet.getInt("ANGKATAN");
                data.append("NIM: ").append(NIM).append(" | NAMA: ").append(NAMA).append(" | ANGKATAN: ").append(ANGKATAN).append(" | KOTA LAHIR: ").append(KOTA_LAHIR).append(" | JENIS KELAMIN: ").append(JENIS_KELAMIN).append("\n");
            }
            displayArea.setText(data.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }


    @SuppressWarnings("unchecked")

    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }


 public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }


}
