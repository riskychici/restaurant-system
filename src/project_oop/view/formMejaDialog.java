package project_oop.view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

/**
 * Dialog untuk menambah/update meja dengan judul besar
 */
public class formMejaDialog extends JDialog {

    private JTextField txtNomorMeja;
    private JSpinner spnKapasitas;
    private JButton btnSimpan;
    private JButton btnBatal;

    private int idMeja = -1;

    // Constant
    private static final int DIALOG_WIDTH = 450;
    private static final int DIALOG_HEIGHT = 355;
    private static final int PADDING = 25;
    
    // Inisialisasi Font Poppins
    private final Font fontPoppinsBold = new Font("Poppins", Font.BOLD, 21);
    private final Font fontPoppinsPlain = new Font("Poppins", Font.PLAIN, 13);
    private final Font fontPoppinsMedium = new Font("Poppins", Font.BOLD, 14);

    private static final Color BUTTON_COLOR = new Color(34, 197, 94);
    private static final Color TITLE_COLOR = new Color(33, 33, 33);

    public formMejaDialog(JFrame parent) {
        super(parent, "Tambah Meja Baru", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    public formMejaDialog(JFrame parent, int id, String nomor, int kapasitas) {
        super(parent, "Edit Meja", true);
        this.idMeja = id;
        initComponents();
        setData(nomor, kapasitas);
        setLocationRelativeTo(parent);
    }

    public void setData(String nomor, int kapasitas) {
        txtNomorMeja.setText(nomor);
        spnKapasitas.setValue(kapasitas);
    }

    private void initComponents() {
        setupDialog();

        // Panel Utama
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = createGridBagConstraints();

        // 1. Tambah Judul Besar
        addTitleSection(mainPanel, gbc);

        // 2. Form Input
        addNomorMejaSection(mainPanel, gbc);
        addKapasitasSection(mainPanel, gbc);

        // Panel Tombol di bagian bawah (South)
        JPanel footerPanel = createButtonPanel();

        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setupEventListeners();
    }

    private void setupDialog() {
        setLayout(new BorderLayout());
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setResizable(false);
    }

    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        return gbc;
    }

    // Method Menambahkan Judul Besar
    private void addTitleSection(JPanel panel, GridBagConstraints gbc) {
        String teksJudul = (idMeja == -1) ? "Tambah Meja" : "Edit Meja";
        JLabel lblJudul = new JLabel(teksJudul);

        lblJudul.setFont(fontPoppinsBold);
        lblJudul.setForeground(TITLE_COLOR);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(lblJudul, gbc);
    }

    private void addNomorMejaSection(JPanel panel, GridBagConstraints gbc) {
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.gridy = 1;
        panel.add(new JLabel("Nomor Meja:"), gbc);

        txtNomorMeja = new JTextField();
        txtNomorMeja.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nomor meja...");
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(txtNomorMeja, gbc);
    }

    private void addKapasitasSection(JPanel panel, GridBagConstraints gbc) {
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.gridy = 3;
        panel.add(new JLabel("Kapasitas (Orang):"), gbc);

        spnKapasitas = new JSpinner(new SpinnerNumberModel(2, 1, 20, 1));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(spnKapasitas, gbc);
    }

    private JPanel createButtonPanel() {
        // Panel footer dengan background abu-abu tipis seperti referensi
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(new Color(250, 250, 250));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        btnBatal = new JButton("Batal");
        btnSimpan = new JButton("Simpan");

        btnSimpan.putClientProperty(FlatClientProperties.STYLE, ""
                + "minimumWidth: 100;"
                + "minimumHeight: 35;"
                + "arc: 10;"); // Sekalian membuat sudut tombol agak melengkung (opsional)

        btnBatal.putClientProperty(FlatClientProperties.STYLE, ""
                + "minimumWidth: 100;"
                + "minimumHeight: 35;"
                + "arc: 10;");

        // Styling Simpan (Hijau)
        btnSimpan.setBackground(BUTTON_COLOR);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Styling Batal (Abu-abu)
        btnBatal.setBackground(new Color(156, 163, 175));
        btnBatal.setForeground(Color.WHITE);

        panel.add(btnBatal);
        panel.add(btnSimpan);

        return panel;
    }

    private void setupEventListeners() {
        btnBatal.addActionListener(e -> dispose());
    }

    public String getNomorMeja() {
        return txtNomorMeja.getText().trim();
    }

    public int getKapasitas() {
        return (int) spnKapasitas.getValue();
    }

    public JButton getBtnSimpan() {
        return btnSimpan;
    }

    public int getIdMeja() {
        return idMeja;
    }
}
