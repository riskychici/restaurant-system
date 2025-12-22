package project_oop.view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

public class formMejaDialog extends JDialog {

    // ==================== CONSTANTS ====================
    private static final int DIALOG_WIDTH = 450;
    private static final int DEFAULT_HEIGHT = 400;
    private static final int EXPANDED_HEIGHT = 500;
    private static final int PADDING = 25;
    private static final int BUTTON_MIN_WIDTH = 100;
    private static final int BUTTON_MIN_HEIGHT = 35;
    private static final int BUTTON_ARC = 10;

    private static final Font FONT_TITLE = new Font("Poppins", Font.BOLD, 21);
    private static final Font FONT_LABEL = new Font("Poppins", Font.PLAIN, 13);

    private static final Color COLOR_TITLE = new Color(33, 33, 33);
    private static final Color COLOR_BG_WHITE = Color.WHITE;
    private static final Color COLOR_BG_PANEL = new Color(250, 250, 250);
    private static final Color COLOR_BG_READONLY = new Color(245, 245, 245);
    private static final Color COLOR_BORDER = new Color(230, 230, 230);
    private static final Color COLOR_BTN_SAVE = new Color(34, 197, 94);
    private static final Color COLOR_BTN_CANCEL = new Color(156, 163, 175);

    // ==================== COMPONENTS ====================
    private JPanel mainPanel;
    private JLabel lblJudul;
    private JLabel lblStatus;
    private JLabel lblPelanggan;
    private JLabel lblJam;

    private JTextField txtNomorMeja;
    private JTextField txtStatus;
    private JTextField txtNamaPelanggan;
    private JTextField txtJamMasuk;
    private JSpinner spnKapasitas;

    private JButton btnSimpan;
    private JButton btnBatal;

    // ==================== PROPERTIES ====================
    private int idMeja = -1;

    // ==================== CONSTRUCTORS ====================
    /**
     * Constructor untuk mode Tambah Meja
     */
    public formMejaDialog(JFrame parent) {
        super(parent, "Tambah Meja Baru", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    /**
     * Constructor untuk mode Edit Meja
     */
    public formMejaDialog(JFrame parent, int id, String nomor, int kapasitas) {
        super(parent, "Edit Meja", true);
        this.idMeja = id;
        initComponents();
        setData(nomor, kapasitas);
        setLocationRelativeTo(parent);
    }

    // ==================== INITIALIZATION ====================
    private void initComponents() {
        setLayout(new BorderLayout());
        setSize(DIALOG_WIDTH, DEFAULT_HEIGHT);
        setResizable(false);

        mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        setupEventListeners();
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        panel.setBackground(COLOR_BG_WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        addTitleSection(panel, gbc);
        addInputNomorMeja(panel, gbc);
        addInputKapasitas(panel, gbc);
        addInputStatus(panel, gbc);
        addInputPelanggan(panel, gbc);
        addInputJamMasuk(panel, gbc);

        return panel;
    }

    private void addTitleSection(JPanel panel, GridBagConstraints gbc) {
        lblJudul = new JLabel(idMeja == -1 ? "Tambah Meja" : "Edit Meja");
        lblJudul.setFont(FONT_TITLE);
        lblJudul.setForeground(COLOR_TITLE);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(lblJudul, gbc);
    }

    private void addInputNomorMeja(JPanel panel, GridBagConstraints gbc) {
        txtNomorMeja = new JTextField();
        addInputField(panel, "Nomor Meja:", txtNomorMeja, gbc, 1);
    }

    private void addInputKapasitas(JPanel panel, GridBagConstraints gbc) {
        spnKapasitas = new JSpinner(new SpinnerNumberModel(2, 1, 50, 1));

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(new JLabel("Kapasitas (Orang):"), gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(spnKapasitas, gbc);
    }

    private void addInputStatus(JPanel panel, GridBagConstraints gbc) {
        lblStatus = new JLabel("Status Meja:");
        txtStatus = new JTextField();
        txtStatus.setEditable(false);
        addDetailField(panel, lblStatus, txtStatus, gbc, 5);
    }

    private void addInputPelanggan(JPanel panel, GridBagConstraints gbc) {
        lblPelanggan = new JLabel("Nama Pelanggan:");
        txtNamaPelanggan = new JTextField();
        txtNamaPelanggan.setEditable(false);
        addDetailField(panel, lblPelanggan, txtNamaPelanggan, gbc, 7);
    }

    private void addInputJamMasuk(JPanel panel, GridBagConstraints gbc) {
        lblJam = new JLabel("Jam Masuk:");
        txtJamMasuk = new JTextField();
        txtJamMasuk.setEditable(false);
        addDetailField(panel, lblJam, txtJamMasuk, gbc, 9);
    }

    private void addInputField(JPanel panel, String labelText, JTextField field,
            GridBagConstraints gbc, int startRow) {
        gbc.gridy = startRow;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(new JLabel(labelText), gbc);

        gbc.gridy = startRow + 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(field, gbc);
    }

    private void addDetailField(JPanel panel, JLabel label, JTextField field,
            GridBagConstraints gbc, int startRow) {
        label.setVisible(false);
        field.setVisible(false);

        gbc.gridy = startRow;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(label, gbc);

        gbc.gridy = startRow + 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(COLOR_BG_PANEL);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER));

        btnSimpan = createButton("Simpan", COLOR_BTN_SAVE);
        btnBatal = createButton("Batal", COLOR_BTN_CANCEL);

        panel.add(btnBatal);
        panel.add(btnSimpan);

        return panel;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);

        String style = String.format(
                "minimumWidth: %d; minimumHeight: %d; arc: %d;",
                BUTTON_MIN_WIDTH, BUTTON_MIN_HEIGHT, BUTTON_ARC
        );
        button.putClientProperty(FlatClientProperties.STYLE, style);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void setupEventListeners() {
        btnBatal.addActionListener(e -> dispose());
    }

    // ==================== PUBLIC SETTERS ====================
    /**
     * Set data untuk mode Edit
     */
    public void setData(String nomor, int kapasitas) {
        txtNomorMeja.setText(nomor);
        spnKapasitas.setValue(kapasitas);
    }

    /**
     * Set data detail aktivitas pelanggan
     */
    public void setDataDetail(String status, String pelanggan, String jam) {
        txtStatus.setText(status);

        if (pelanggan != null && !pelanggan.isEmpty()) {
            txtNamaPelanggan.setText(pelanggan);
            txtJamMasuk.setText(jam);

            showDetailComponents();
            expandDialogSize();
        }
    }

    /**
     * Aktifkan mode Detail (read-only)
     */
    public void setDetailMode() {
        updateTitle("Detail Meja");
        setFieldsReadOnly();
        showStatusField();
        hideActionButtons();
        applyReadOnlyStyles();
    }

    // ==================== PRIVATE HELPERS ====================
    private void showDetailComponents() {
        lblPelanggan.setVisible(true);
        txtNamaPelanggan.setVisible(true);
        lblJam.setVisible(true);
        txtJamMasuk.setVisible(true);
    }

    private void expandDialogSize() {
        setSize(DIALOG_WIDTH, EXPANDED_HEIGHT);
    }

    private void updateTitle(String title) {
        if (lblJudul != null) {
            lblJudul.setText(title);
        }
    }

    private void setFieldsReadOnly() {
        txtNomorMeja.setEditable(false);
        spnKapasitas.setEnabled(false);
    }

    private void showStatusField() {
        lblStatus.setVisible(true);
        txtStatus.setVisible(true);
    }

    private void hideActionButtons() {
        btnSimpan.setVisible(false);
        btnBatal.setText("Tutup");
    }

    private void applyReadOnlyStyles() {
        txtNomorMeja.setBackground(COLOR_BG_READONLY);
        txtStatus.setBackground(COLOR_BG_READONLY);
        txtNamaPelanggan.setBackground(COLOR_BG_READONLY);
        txtJamMasuk.setBackground(COLOR_BG_READONLY);
    }

    // ==================== PUBLIC GETTERS ====================
    public JButton getBtnSimpan() {
        return btnSimpan;
    }

    public String getNomorMeja() {
        return txtNomorMeja.getText();
    }

    public int getKapasitas() {
        return (int) spnKapasitas.getValue();
    }
}
