package project_oop.view;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class formPesananDialog extends JDialog {

    // ========== CONSTANTS ==========
    private static final Color BUTTON_COLOR = new Color(34, 197, 94);
    private static final Color BUTTON_CANCEL_COLOR = new Color(156, 163, 175);
    private static final Color READONLY_BG_COLOR = new Color(245, 245, 245);
    private static final Color FOOTER_BG_COLOR = new Color(250, 250, 250);
    private static final Color BORDER_COLOR = new Color(230, 230, 230);

    private static final int DIALOG_WIDTH = 450;
    private static final int DIALOG_HEIGHT = 700;
    private static final int BUTTON_MIN_WIDTH = 100;
    private static final int BUTTON_MIN_HEIGHT = 35;
    private static final int BUTTON_ARC = 10;

    // ========== COMPONENTS ==========
    private JLabel lblJudul, lblStatus;
    private JTextField txtNamaPelanggan, txtSubtotal;
    private JComboBox<String> cbMeja, cbMenu, cbStatus;
    private JSpinner spQty;
    private JTextArea txtCatatan;
    private JButton btnSimpan, btnBatal;
    private DecimalFormat df;

    // List untuk menyimpan data menu asli (sebelum difilter)
    private List<String> originalMenuList;

    public formPesananDialog(JFrame parent, List<String> mejaKosong, List<String> daftarMenu) {
        super(parent, "Buat Pesanan Baru", true);
        this.originalMenuList = new ArrayList<>(daftarMenu); // Simpan daftar asli
        inisialisasiFormat();
        initComponents(mejaKosong, daftarMenu);
        updateSubtotal();
        setStatusVisible(false);
        setLocationRelativeTo(parent);
    }

    private void inisialisasiFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
        symbols.setGroupingSeparator('.');
        df = new DecimalFormat("###,###", symbols);
    }

    private void initComponents(List<String> mejaKosong, List<String> daftarMenu) {
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLayout(new BorderLayout());

        add(createMainPanel(mejaKosong, daftarMenu), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);

        setupEventListeners();
        setupAutoCompleteMenu();
    }

    /**
     * PERBAIKAN: Logika Filter Real-time
     */
    private void setupAutoCompleteMenu() {
        cbMenu.setEditable(true);
        JTextComponent editor = (JTextComponent) cbMenu.getEditor().getEditorComponent();
        editor.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ketik nama menu...");

        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Abaikan tombol navigasi (Atas, Bawah, Enter) agar tidak bentrok dengan pemilihan
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    return;
                }

                String text = editor.getText().toLowerCase();
                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

                // Filter menu berdasarkan input
                for (String item : originalMenuList) {
                    if (item.toLowerCase().contains(text)) {
                        model.addElement(item);
                    }
                }

                // Update model di JComboBox
                cbMenu.setModel(model);

                // Kembalikan teks yang sedang diketik ke editor
                editor.setText(text);

                if (text.isEmpty()) {
                    cbMenu.hidePopup();
                } else {
                    if (model.getSize() > 0) {
                        cbMenu.showPopup();
                    } else {
                        cbMenu.hidePopup();
                    }
                }
            }
        });
    }

    private JPanel createMainPanel(List<String> mejaKosong, List<String> daftarMenu) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 5, 0);

        lblJudul = new JLabel("Buat Pesanan");
        lblJudul.setFont(new Font("Poppins", Font.BOLD, 21));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(lblJudul, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(new JLabel("Nama Pelanggan:"), gbc);
        txtNamaPelanggan = createTextField("Masukkan nama...");
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(txtNamaPelanggan, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(new JLabel("Pilih Meja:"), gbc);
        cbMeja = new JComboBox<>(mejaKosong.toArray(new String[0]));
        cbMeja.setFont(new Font("Poppins", Font.PLAIN, 13));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(cbMeja, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(new JLabel("Pilih Menu:"), gbc);
        cbMenu = new JComboBox<>(daftarMenu.toArray(new String[0]));
        cbMenu.setFont(new Font("Poppins", Font.PLAIN, 13));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(cbMenu, gbc);

        lblStatus = new JLabel("Status Pesanan:");
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(lblStatus, gbc);
        cbStatus = new JComboBox<>();
        cbStatus.setFont(new Font("Poppins", Font.PLAIN, 13));
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(cbStatus, gbc);

        JPanel panelQtySub = new JPanel(new GridLayout(1, 2, 15, 0));
        panelQtySub.setOpaque(false);

        JPanel pQty = new JPanel(new BorderLayout(0, 5));
        pQty.setOpaque(false);
        pQty.add(new JLabel("Jumlah (Qty):"), BorderLayout.NORTH);
        spQty = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spQty.setFont(new Font("Poppins", Font.PLAIN, 13));
        pQty.add(spQty, BorderLayout.CENTER);

        JPanel pSub = new JPanel(new BorderLayout(0, 5));
        pSub.setOpaque(false);
        pSub.add(new JLabel("Subtotal:"), BorderLayout.NORTH);
        txtSubtotal = createTextField("");
        txtSubtotal.setEditable(false);
        txtSubtotal.setFocusable(false);
        txtSubtotal.setBackground(READONLY_BG_COLOR);
        txtSubtotal.setFont(new Font("Poppins", Font.PLAIN, 13));
        txtSubtotal.setText("Rp 0");
        pSub.add(txtSubtotal, BorderLayout.CENTER);

        panelQtySub.add(pQty);
        panelQtySub.add(pSub);
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 15, 0);
        mainPanel.add(panelQtySub, gbc);

        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, 5, 0);
        mainPanel.add(new JLabel("Catatan (Opsional):"), gbc);
        txtCatatan = new JTextArea(4, 20);
        txtCatatan.setFont(new Font("Poppins", Font.PLAIN, 13));
        txtCatatan.setLineWrap(true);
        txtCatatan.setWrapStyleWord(true);
        txtCatatan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridy = 11;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(new JScrollPane(txtCatatan), gbc);

        return mainPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footer.setBackground(FOOTER_BG_COLOR);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        btnBatal = createButton("Batal", BUTTON_CANCEL_COLOR);
        btnSimpan = createButton("Simpan Pesanan", BUTTON_COLOR);

        footer.add(btnBatal);
        footer.add(btnSimpan);
        return footer;
    }

    private void setupEventListeners() {
        btnBatal.addActionListener(e -> dispose());
        cbMenu.addActionListener(e -> updateSubtotal());
        spQty.addChangeListener(e -> updateSubtotal());
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        String style = String.format("minimumWidth: %d; minimumHeight: %d; arc: %d; borderWidth: 0; focusWidth: 0;", BUTTON_MIN_WIDTH, BUTTON_MIN_HEIGHT, BUTTON_ARC);
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

    private void updateSubtotal() {
        try {
            // PERBAIKAN: Ambil dari editor jika editable
            Object selectedItem = cbMenu.getSelectedItem();
            if (selectedItem == null) {
                return;
            }

            String selected = selectedItem.toString();
            if (!selected.contains("Rp ")) {
                txtSubtotal.setText("Rp 0");
                return;
            }
            int start = selected.indexOf("Rp ") + 3;
            int end = selected.lastIndexOf(")");
            String hargaText = selected.substring(start, end);
            double harga = Double.parseDouble(hargaText.replace(".", ""));
            int qty = (int) spQty.getValue();
            txtSubtotal.setText("Rp " + df.format(harga * qty));
        } catch (Exception e) {
            txtSubtotal.setText("Rp 0");
        }
    }

    // ========== PUBLIC METHODS ==========
    public void setStatusVisible(boolean visible) {
        lblStatus.setVisible(visible);
        cbStatus.setVisible(visible);
    }

    public void setDaftarStatus(List<String> daftarStatus) {
        cbStatus.removeAllItems();
        for (String status : daftarStatus) {
            cbStatus.addItem(status);
        }
    }

    public String getMenu() {
        // PERBAIKAN: Mengambil teks yang sedang ditampilkan di editor
        return cbMenu.getEditor().getItem().toString();
    }

    public void setMenu(String namaMenu) {
        // Cari di daftar asli agar tidak terpengaruh filter
        for (String item : originalMenuList) {
            if (item.contains(namaMenu)) {
                cbMenu.setSelectedItem(item);
                return;
            }
        }
    }

    public void setStatusTerpilih(String status) {
        this.cbStatus.setSelectedItem(status);
    }

    public String getStatus() {
        return cbStatus.getSelectedItem() != null ? cbStatus.getSelectedItem().toString() : "";
    }

    public void setDetailMode() {
        setTitle("Detail Pesanan");
        lblJudul.setText("Detail Pesanan");
        setStatusVisible(true);
        txtNamaPelanggan.setEditable(false);
        txtNamaPelanggan.setBackground(READONLY_BG_COLOR);
        txtCatatan.setEditable(false);
        txtCatatan.setBackground(READONLY_BG_COLOR);
        cbMeja.setEnabled(false);
        cbMenu.setEnabled(false);
        cbStatus.setEnabled(false);
        spQty.setEnabled(false);
        btnSimpan.setVisible(false);
        btnBatal.setText("Tutup");
    }

    // Getters Setters standar
    public void setNama(String nama) {
        this.txtNamaPelanggan.setText(nama);
    }

    public void setMeja(String namaMeja) {
        this.cbMeja.setSelectedItem(namaMeja);
    }

    public void setQty(int qty) {
        this.spQty.setValue(qty);
    }

    public void setCatatan(String catatan) {
        this.txtCatatan.setText(catatan);
    }

    public String getNama() {
        return txtNamaPelanggan.getText();
    }

    public String getMeja() {
        return cbMeja.getSelectedItem().toString();
    }

    public int getQty() {
        return (int) spQty.getValue();
    }

    public String getCatatan() {
        return txtCatatan.getText();
    }

    public JButton getBtnSimpan() {
        return btnSimpan;
    }
}
