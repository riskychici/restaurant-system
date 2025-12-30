package project_oop.view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Dialog untuk menampilkan dan mengedit data pembayaran. Versi Perbaikan:
 * Menangani reset status tombol Simpan dan menyembunyikan ID Dropdown.
 */
public class formPembayaranDialog extends JDialog {

    // ========== INNER CLASS HELPER ==========
    private class ComboItem {

        private int id;
        private String label;

        public ComboItem(int id, String label) {
            this.id = id;
            this.label = label;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    // ========== CONSTANTS ==========
    private static final Color BUTTON_COLOR = new Color(34, 197, 94);
    private static final Color BUTTON_CANCEL_COLOR = new Color(156, 163, 175);
    private static final Color READONLY_BG_COLOR = new Color(245, 245, 245);
    private static final Color FOOTER_BG_COLOR = new Color(250, 250, 250);
    private static final Color BORDER_COLOR = new Color(230, 230, 230);

    private static final int BUTTON_MIN_WIDTH = 100;
    private static final int BUTTON_MIN_HEIGHT = 35;
    private static final int BUTTON_ARC = 10;
    private static final int DIALOG_WIDTH = 480;
    private static final int DIALOG_HEIGHT_DETAIL = 680;
    private static final int DIALOG_HEIGHT_EDIT = 530;

    // ========== UI COMPONENTS ==========
    private JTextField txtNama;
    private JTextField txtMeja;
    private JTextField txtTotal;
    private JComboBox<Object> cbStatus;
    private JComboBox<Object> cbMetode;
    private JButton btnSimpan;
    private JButton btnBatal;
    private JTable tableDetail;
    private DefaultTableModel tableModel;

    // ========== DATA FIELDS ==========
    private String idPelanggan;
    private boolean saveClicked;
    private final DecimalFormat currencyFormat;

    // ========== CONSTRUCTOR ==========
    public formPembayaranDialog(JFrame parent, List<Object[]> listStatus,
            List<Object[]> listMetode, boolean isDetail) {
        super(parent, isDetail ? "Detail Pembayaran" : "Edit Pembayaran", true);

        this.saveClicked = false;
        this.currencyFormat = new DecimalFormat("#,###");

        initComponents(listStatus, listMetode, isDetail);
        setLocationRelativeTo(parent);
    }

    // ========== OVERRIDE SETVISIBLE ==========
    @Override
    public void setVisible(boolean b) {
        if (b) {
            this.saveClicked = false;
        }
        super.setVisible(b);
    }

    // ========== INITIALIZATION ==========
    private void initComponents(List<Object[]> listStatus, List<Object[]> listMetode, boolean isDetail) {
        int height = isDetail ? DIALOG_HEIGHT_DETAIL : DIALOG_HEIGHT_EDIT;
        setSize(DIALOG_WIDTH, height);
        setLayout(new BorderLayout());

        add(createMainPanel(listStatus, listMetode, isDetail), BorderLayout.CENTER);
        add(createFooterPanel(isDetail), BorderLayout.SOUTH);

        setupEventListeners(isDetail);
    }

    private void setupEventListeners(boolean isDetail) {
        btnBatal.addActionListener(e -> {
            saveClicked = false;
            dispose();
        });

        if (!isDetail) {
            btnSimpan.addActionListener(e -> {
                saveClicked = true;
                dispose();
            });
        }
    }

    // ========== PANEL CREATORS ==========
    private JPanel createMainPanel(List<Object[]> listStatus, List<Object[]> listMetode, boolean isDetail) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = createDefaultConstraints();
        int currentRow = 0;

        currentRow = addTitle(mainPanel, gbc, currentRow, isDetail);
        currentRow = addFormFields(mainPanel, gbc, currentRow, listStatus, listMetode, isDetail);

        if (isDetail) {
            addDetailTable(mainPanel, gbc, currentRow);
        }

        return mainPanel;
    }

    private int addTitle(JPanel panel, GridBagConstraints gbc, int row, boolean isDetail) {
        JLabel lblJudul = new JLabel(isDetail ? "Rincian Pembayaran" : "Edit Pembayaran");
        lblJudul.setFont(new Font("Poppins", Font.BOLD, 21));
        gbc.gridy = row;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(lblJudul, gbc);
        return row + 1;
    }

    private int addFormFields(JPanel panel, GridBagConstraints gbc, int row,
            List<Object[]> listStatus, List<Object[]> listMetode, boolean isDetail) {

        row = addFieldGroup(panel, gbc, row, "Nama Pelanggan:", txtNama = createTextField(true));
        row = addFieldGroup(panel, gbc, row, "Nomor Meja:", txtMeja = createTextField(true));
        row = addFieldGroup(panel, gbc, row, "Total Tagihan:", txtTotal = createTextField(true));

        cbStatus = createComboBox(listStatus);
        row = addFieldGroup(panel, gbc, row, "Status Pembayaran:", cbStatus);

        cbMetode = createComboBox(listMetode);
        row = addFieldGroup(panel, gbc, row, "Metode Pembayaran:", cbMetode);

        if (isDetail) {
            cbStatus.setEnabled(false);
            cbMetode.setEnabled(false);
        }

        return row;
    }

    private int addFieldGroup(JPanel panel, GridBagConstraints gbc, int row,
            String labelText, JComponent component) {
        addLabel(panel, labelText, row++, gbc);
        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(component, gbc);
        return row;
    }

    private void addDetailTable(JPanel panel, GridBagConstraints gbc, int row) {
        gbc.gridy = row++;
        gbc.insets = new Insets(10, 0, 5, 0);
        JLabel lblItem = new JLabel("Menu yang dipesan:");
        lblItem.setFont(new Font("Poppins", Font.BOLD, 15));
        panel.add(lblItem, gbc);

        tableModel = new DefaultTableModel(new String[]{"Menu", "Harga", "Qty", "Subtotal"}, 0);
        tableDetail = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableDetail);
        scrollPane.setPreferredSize(new Dimension(400, 150));

        gbc.gridy = row;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(scrollPane, gbc);
    }

    private JPanel createFooterPanel(boolean isDetail) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footer.setBackground(FOOTER_BG_COLOR);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        btnBatal = createButton(isDetail ? "Tutup" : "Batal", BUTTON_CANCEL_COLOR);
        footer.add(btnBatal);

        if (!isDetail) {
            btnSimpan = createButton("Simpan", BUTTON_COLOR);
            footer.add(btnSimpan);
        }

        return footer;
    }

    // ========== COMPONENT FACTORY METHODS ==========
    private GridBagConstraints createDefaultConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 5, 0);
        return gbc;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        String style = String.format("minimumWidth: %d; minimumHeight: %d; arc: %d; borderWidth: 0; focusWidth: 0;",
                BUTTON_MIN_WIDTH, BUTTON_MIN_HEIGHT, BUTTON_ARC);
        button.putClientProperty(FlatClientProperties.STYLE, style);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Poppins", Font.PLAIN, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JTextField createTextField(boolean readOnly) {
        JTextField field = new JTextField();
        field.setFont(new Font("Poppins", Font.PLAIN, 13));
        if (readOnly) {
            field.setEditable(false);
            field.setBackground(READONLY_BG_COLOR);
        }
        return field;
    }

    private JComboBox<Object> createComboBox(List<Object[]> data) {
        JComboBox<Object> comboBox = new JComboBox<>();
        for (Object[] item : data) {
            comboBox.addItem(new ComboItem((int) item[0], item[1].toString()));
        }
        return comboBox;
    }

    private void addLabel(JPanel panel, String text, int row, GridBagConstraints gbc) {
        gbc.gridy = row;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel label = new JLabel(text);
        label.setFont(new Font("Poppins", Font.PLAIN, 13));
        panel.add(label, gbc);
    }

    // ========== DATA MANIPULATION ==========
    public void setData(String id, String nama, String meja, String total,
            int idStatus, int idMetode) {
        this.idPelanggan = id;
        txtNama.setText(nama);
        txtMeja.setText(meja);
        txtTotal.setText(total);
        selectComboItemById(cbStatus, idStatus);
        selectComboItemById(cbMetode, idMetode);
    }

    private void selectComboItemById(JComboBox<Object> comboBox, int id) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            ComboItem item = (ComboItem) comboBox.getItemAt(i);
            if (item.getId() == id) {
                comboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    public void setTableData(List<Object[]> data) {
        if (tableModel == null) {
            return;
        }
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(new Object[]{
                row[0], formatCurrency(row[1]), row[2], formatCurrency(row[3])
            });
        }
    }

    // ========== GETTERS ==========
    public boolean isSaveClicked() {
        return saveClicked;
    }

    public int getSelectedIdStatus() {
        ComboItem item = (ComboItem) cbStatus.getSelectedItem();
        return (item != null) ? item.getId() : 0;
    }

    public int getSelectedIdMetode() {
        ComboItem item = (ComboItem) cbMetode.getSelectedItem();
        return (item != null) ? item.getId() : 0;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    private String formatCurrency(Object value) {
        double amount = Double.parseDouble(value.toString());
        return "Rp " + currencyFormat.format(amount);
    }
}
