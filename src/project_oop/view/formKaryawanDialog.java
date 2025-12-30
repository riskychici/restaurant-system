package project_oop.view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog untuk menambah/update karyawan dengan desain modern. ID Role
 * disembunyikan dari dropdown namun tetap dapat diambil nilainya.
 */
public class formKaryawanDialog extends JDialog {

    // ========== CONSTANTS ==========
    private static final int DIALOG_WIDTH = 450;
    private static final int DIALOG_HEIGHT = 430;
    private static final int PADDING = 25;

    private static final Color BUTTON_COLOR = new Color(34, 197, 94);
    private static final Color BUTTON_CANCEL_COLOR = new Color(156, 163, 175);
    private static final Color TITLE_COLOR = new Color(33, 33, 33);
    private static final Color FOOTER_BG_COLOR = new Color(250, 250, 250);
    private static final Color BORDER_COLOR = new Color(230, 230, 230);
    private static final Color READONLY_BG_COLOR = new Color(245, 245, 245);

    private final Font fontPoppinsBold = new Font("Poppins", Font.BOLD, 21);
    private final Font fontPoppinsPlain = new Font("Poppins", Font.PLAIN, 13);

    // ========== COMPONENTS ==========
    private JLabel lblJudul;
    private JTextField txtNama;
    private JTextField txtNoTelp;
    private JComboBox<RoleItem> cbRole;
    private JButton btnSimpan;
    private JButton btnBatal;

    // ========== FIELDS ==========
    private String idKaryawan = null;

    /**
     * Inner Class untuk menampung data Role
     */
    private static class RoleItem {

        private final int id;
        private final String nama;

        public RoleItem(int id, String nama) {
            this.id = id;
            this.nama = nama;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return nama;
        }
    }

    // ========== CONSTRUCTORS ==========
    public formKaryawanDialog(JFrame parent, List<String> roles) {
        super(parent, "Tambah Karyawan Baru", true);
        initComponents(roles);
        setLocationRelativeTo(parent);
    }

    public formKaryawanDialog(JFrame parent, List<String> roles, String id, String nama, String noTelp, int idRole) {
        super(parent, "Edit Karyawan", true);
        this.idKaryawan = id;
        initComponents(roles);
        setData(id, nama, noTelp, idRole);
        setLocationRelativeTo(parent);
    }

    // ========== INITIALIZATION ==========
    private void initComponents(List<String> roles) {
        setupDialog();

        JPanel mainPanel = createMainPanel(roles);
        JPanel footerPanel = createFooterPanel();

        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setupEventListeners();
    }

    private void setupDialog() {
        setLayout(new BorderLayout());
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setResizable(false);
    }

    private void setupEventListeners() {
        btnBatal.addActionListener(e -> dispose());
    }

    private JPanel createMainPanel(List<String> roles) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = createGridBagConstraints();

        addTitleSection(panel, gbc);
        addNamaSection(panel, gbc);
        addNoTelpSection(panel, gbc);
        addRoleSection(panel, gbc, roles);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(FOOTER_BG_COLOR);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        btnBatal = createButton("Batal", BUTTON_CANCEL_COLOR);
        btnSimpan = createButton("Simpan", BUTTON_COLOR);

        panel.add(btnBatal);
        panel.add(btnSimpan);

        return panel;
    }

    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        return gbc;
    }

    // ========== FORM SECTIONS ==========
    private void addTitleSection(JPanel panel, GridBagConstraints gbc) {
        String teksJudul = (idKaryawan == null) ? "Tambah Karyawan" : "Edit Karyawan";
        lblJudul = new JLabel(teksJudul);
        lblJudul.setFont(fontPoppinsBold);
        lblJudul.setForeground(TITLE_COLOR);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(lblJudul, gbc);
    }

    private void addNamaSection(JPanel panel, GridBagConstraints gbc) {
        JLabel label = createLabel("Nama Lengkap:");
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(label, gbc);

        txtNama = createTextField("Masukkan nama lengkap...");
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(txtNama, gbc);
    }

    private void addNoTelpSection(JPanel panel, GridBagConstraints gbc) {
        JLabel label = createLabel("Nomor Telepon:");
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(label, gbc);

        txtNoTelp = createTextField("Contoh: 08123456789");
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(txtNoTelp, gbc);
    }

    private void addRoleSection(JPanel panel, GridBagConstraints gbc, List<String> roles) {
        JLabel label = createLabel("Role / Posisi:");
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(label, gbc);

        cbRole = new JComboBox<>();
        for (String r : roles) {
            try {
                String[] parts = r.split(" - ");
                int id = Integer.parseInt(parts[0].trim());
                String nama = parts[1].trim();
                cbRole.addItem(new RoleItem(id, nama));
            } catch (Exception e) {
                System.err.println("Gagal parsing role: " + r);
            }
        }

        cbRole.setFont(fontPoppinsPlain);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(cbRole, gbc);
    }

    // ========== COMPONENT CREATION ==========
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(fontPoppinsPlain);
        return label;
    }

    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField();
        textField.setFont(fontPoppinsPlain);
        textField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
        return textField;
    }

    private JButton createButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.putClientProperty(FlatClientProperties.STYLE, "minimumWidth: 100; minimumHeight: 35; arc: 10;");
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(fontPoppinsPlain);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // ========== PUBLIC METHODS ==========
    public void setData(String id, String nama, String noTelp, int idRole) {
        this.idKaryawan = id;
        this.txtNama.setText(nama);
        this.txtNoTelp.setText(noTelp);

        selectRoleById(idRole);

        if (lblJudul != null) {
            lblJudul.setText("Edit Karyawan");
        }
        setTitle("Edit Karyawan");
    }

    public void setDetailMode() {
        lblJudul.setText("Detail Karyawan");
        setTitle("Detail Karyawan");

        txtNama.setEditable(false);
        txtNoTelp.setEditable(false);
        cbRole.setEnabled(false);

        txtNama.setBackground(READONLY_BG_COLOR);
        txtNoTelp.setBackground(READONLY_BG_COLOR);

        btnSimpan.setVisible(false);
        btnBatal.setText("Tutup");
    }

    private void selectRoleById(int idRole) {
        for (int i = 0; i < cbRole.getItemCount(); i++) {
            RoleItem item = cbRole.getItemAt(i);
            if (item.getId() == idRole) {
                cbRole.setSelectedIndex(i);
                break;
            }
        }
    }

    // ========== GETTERS ==========
    public String getIdKaryawan() {
        return idKaryawan;
    }

    public String getNama() {
        return txtNama.getText().trim();
    }

    public String getNoTelp() {
        return txtNoTelp.getText().trim();
    }

    public int getIdRole() {
        RoleItem selected = (RoleItem) cbRole.getSelectedItem();
        return (selected != null) ? selected.getId() : -1;
    }

    public JButton getBtnSimpan() {
        return btnSimpan;
    }
}
