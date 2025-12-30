package project_oop.view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog form untuk menambah, edit, dan melihat detail menu.
 */
public class formMenuDialog extends JDialog {

    // ========== CONSTANTS ==========
    // Colors
    private static final Color BUTTON_COLOR = new Color(34, 197, 94);
    private static final Color BUTTON_CANCEL_COLOR = new Color(156, 163, 175);
    private static final Color READONLY_BG_COLOR = new Color(245, 245, 245);
    private static final Color FOOTER_BG_COLOR = new Color(250, 250, 250);
    private static final Color BORDER_COLOR = new Color(230, 230, 230);

    // Dimensions
    private static final int DIALOG_WIDTH = 450;
    private static final int DIALOG_HEIGHT = 470;
    private static final int BUTTON_MIN_WIDTH = 100;
    private static final int BUTTON_MIN_HEIGHT = 35;
    private static final int BUTTON_ARC = 10;

    // Margins & Spacing
    private static final int MAIN_PANEL_PADDING = 25;
    private static final int FOOTER_SPACING = 15;
    private static final int LABEL_BOTTOM_MARGIN = 5;
    private static final int FIELD_BOTTOM_MARGIN = 15;
    private static final int TITLE_BOTTOM_MARGIN = 20;

    // ========== UI COMPONENTS ==========
    private JLabel lblJudul;
    private JTextField txtNama;
    private JTextField txtHarga;
    private JTextField txtStok;
    private JComboBox<String> cbKategori;
    private JButton btnSimpan;
    private JButton btnBatal;

    // ========== FIELDS ==========
    private String idMenu = null;

    // ========== CONSTRUCTOR ==========
    public formMenuDialog(JFrame parent, List<String> kategori) {
        super(parent, "Tambah Menu Baru", true);
        initComponents(kategori);
        setLocationRelativeTo(parent);
    }

    // ========== INITIALIZATION ==========
    private void initComponents(List<String> kategori) {
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLayout(new BorderLayout());
        setResizable(false);

        add(createMainPanel(kategori), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);

        setupEventListeners();
    }

    private JPanel createMainPanel(List<String> kategori) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                MAIN_PANEL_PADDING, MAIN_PANEL_PADDING,
                MAIN_PANEL_PADDING, MAIN_PANEL_PADDING
        ));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = createDefaultConstraints();

        addTitle(mainPanel, gbc);
        addNamaField(mainPanel, gbc);
        addKategoriField(mainPanel, gbc, kategori);
        addHargaField(mainPanel, gbc);
        addStokField(mainPanel, gbc);

        return mainPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, FOOTER_SPACING, FOOTER_SPACING));
        footer.setBackground(FOOTER_BG_COLOR);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        btnBatal = createButton("Batal", BUTTON_CANCEL_COLOR);
        btnSimpan = createButton("Simpan", BUTTON_COLOR);

        footer.add(btnBatal);
        footer.add(btnSimpan);

        return footer;
    }

    private void setupEventListeners() {
        btnBatal.addActionListener(e -> dispose());
    }

    // ========== FIELD CREATORS ==========
    private void addTitle(JPanel panel, GridBagConstraints gbc) {
        lblJudul = new JLabel("Tambah Menu");
        lblJudul.setFont(new Font("Poppins", Font.BOLD, 21));

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, TITLE_BOTTOM_MARGIN, 0);
        panel.add(lblJudul, gbc);
    }

    private void addNamaField(JPanel panel, GridBagConstraints gbc) {
        addLabel(panel, gbc, 1, "Nama Menu:");

        txtNama = createTextField("Masukkan nama menu..");
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, FIELD_BOTTOM_MARGIN, 0);
        panel.add(txtNama, gbc);
    }

    private void addKategoriField(JPanel panel, GridBagConstraints gbc, List<String> kategori) {
        addLabel(panel, gbc, 3, "Kategori:");

        cbKategori = new JComboBox<>(kategori.toArray(new String[0]));
        cbKategori.setFont(new Font("Poppins", Font.PLAIN, 13));

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, FIELD_BOTTOM_MARGIN, 0);
        panel.add(cbKategori, gbc);
    }

    private void addHargaField(JPanel panel, GridBagConstraints gbc) {
        addLabel(panel, gbc, 5, "Harga (Rp):");

        txtHarga = createTextField("Masukkan harga...");
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, FIELD_BOTTOM_MARGIN, 0);
        panel.add(txtHarga, gbc);
    }

    private void addStokField(JPanel panel, GridBagConstraints gbc) {
        addLabel(panel, gbc, 7, "Stok Awal:");

        txtStok = createTextField("Masukkan stok...");
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(txtStok, gbc);
    }

    private void addLabel(JPanel panel, GridBagConstraints gbc, int row, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Poppins", Font.PLAIN, 13));

        gbc.gridy = row;
        gbc.insets = new Insets(0, 0, LABEL_BOTTOM_MARGIN, 0);
        panel.add(label, gbc);
    }

    // ========== COMPONENT FACTORIES ==========
    private GridBagConstraints createDefaultConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        return gbc;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);

        String style = String.format(
                "minimumWidth: %d; minimumHeight: %d; arc: %d; borderWidth: 0; focusWidth: 0;",
                BUTTON_MIN_WIDTH, BUTTON_MIN_HEIGHT, BUTTON_ARC
        );

        button.putClientProperty(FlatClientProperties.STYLE, style);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Poppins", Font.PLAIN, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Poppins", Font.PLAIN, 13));
        field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        return field;
    }

    // ========== EDIT MODE ==========
    /**
     * Set data untuk mode edit.
     */
    public void setData(String id, String nama, double harga, int stok, String kategori) {
        this.idMenu = id;
        setTitle("Edit Menu");
        lblJudul.setText("Edit Menu");

        txtNama.setText(nama);
        txtHarga.setText(String.valueOf((int) harga));
        txtStok.setText(String.valueOf(stok));

        selectKategori(kategori);
    }

    /**
     * Set mode detail (read-only).
     */
    public void setDetailMode() {
        setTitle("Detail Menu");
        lblJudul.setText("Detail Menu");

        setFieldsReadOnly();
        setReadOnlyBackground();

        btnSimpan.setVisible(false);
        btnBatal.setText("Tutup");
    }

    // ========== GETTERS ==========
    public String getNama() {
        return txtNama.getText().trim();
    }

    public double getHarga() {
        return parseNumericValue(txtHarga.getText());
    }

    public int getStok() {
        return (int) parseNumericValue(txtStok.getText());
    }

    public String getSelectedKategori() {
        return cbKategori.getSelectedItem().toString();
    }

    public String getIdMenu() {
        return idMenu;
    }

    public JButton getBtnSimpan() {
        return btnSimpan;
    }

    // ========== PRIVATE HELPERS ==========
    private void selectKategori(String kategori) {
        for (int i = 0; i < cbKategori.getItemCount(); i++) {
            if (cbKategori.getItemAt(i).contains(kategori)) {
                cbKategori.setSelectedIndex(i);
                break;
            }
        }
    }

    private void setFieldsReadOnly() {
        txtNama.setEditable(false);
        txtHarga.setEditable(false);
        txtStok.setEditable(false);
        cbKategori.setEnabled(false);
    }

    private void setReadOnlyBackground() {
        txtNama.setBackground(READONLY_BG_COLOR);
        txtHarga.setBackground(READONLY_BG_COLOR);
        txtStok.setBackground(READONLY_BG_COLOR);
    }

    private double parseNumericValue(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        try {
            String numericValue = text.replaceAll("[^0-9]", "");
            return numericValue.isEmpty() ? 0 : Double.parseDouble(numericValue);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
