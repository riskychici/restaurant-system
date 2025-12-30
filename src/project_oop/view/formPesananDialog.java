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

    // ========== FIELDS ==========
    private DecimalFormat df;
    private List<String> originalMenuList;
    private List<String> originalPelangganList;

    // ========== UI COMPONENTS ==========
    private JLabel lblJudul;
    private JLabel lblStatus;
    private JTextField txtNamaPelanggan;
    private JTextField txtSubtotal;
    private JComboBox<String> cbNamaPelanggan;
    private JComboBox<String> cbMeja;
    private JComboBox<String> cbMenu;
    private JComboBox<String> cbStatus;
    private JSpinner spQty;
    private JTextArea txtCatatan;
    private JButton btnSimpan;
    private JButton btnBatal;

    public formPesananDialog(JFrame parent, List<String> mejaKosong,
            List<String> daftarMenu, List<String> pelangganAktif) {
        super(parent, "Pesanan", true);
        this.originalMenuList = new ArrayList<>(daftarMenu);
        this.originalPelangganList = new ArrayList<>(pelangganAktif);

        inisialisasiFormat();
        initComponents(mejaKosong, daftarMenu, pelangganAktif);
        setupEventListeners();
        setupAutoCompleteFields();
        updateSubtotal();
        setStatusVisible(false);
        setLocationRelativeTo(parent);
    }

    private void inisialisasiFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
        symbols.setGroupingSeparator('.');
        df = new DecimalFormat("###,###", symbols);
    }

    private void initComponents(List<String> mejaKosong, List<String> daftarMenu,
            List<String> pelangganAktif) {
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLayout(new BorderLayout());

        add(createMainPanel(mejaKosong, daftarMenu, pelangganAktif), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createMainPanel(List<String> mejaKosong, List<String> daftarMenu,
            List<String> pelangganAktif) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 5, 0);

        addTitleSection(mainPanel, gbc);
        addCustomerSection(mainPanel, gbc, pelangganAktif);
        addTableSection(mainPanel, gbc, mejaKosong);
        addMenuSection(mainPanel, gbc, daftarMenu);
        addStatusSection(mainPanel, gbc);
        addQuantitySubtotalSection(mainPanel, gbc);
        addNotesSection(mainPanel, gbc);

        return mainPanel;
    }

    private void addTitleSection(JPanel panel, GridBagConstraints gbc) {
        lblJudul = new JLabel("Pesanan");
        lblJudul.setFont(new Font("Poppins", Font.BOLD, 21));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(lblJudul, gbc);
    }

    private void addCustomerSection(JPanel panel, GridBagConstraints gbc, List<String> pelangganAktif) {
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(new JLabel("Nama Pelanggan:"), gbc);

        JPanel wrapperNama = new JPanel();
        wrapperNama.setLayout(new OverlayLayout(wrapperNama));
        wrapperNama.setOpaque(false);

        txtNamaPelanggan = createTextField("Masukkan nama...");
        txtNamaPelanggan.setVisible(false);

        cbNamaPelanggan = new JComboBox<>(pelangganAktif.toArray(new String[0]));
        cbNamaPelanggan.setFont(new Font("Poppins", Font.PLAIN, 13));

        wrapperNama.add(txtNamaPelanggan);
        wrapperNama.add(cbNamaPelanggan);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(wrapperNama, gbc);
    }

    private void addTableSection(JPanel panel, GridBagConstraints gbc, List<String> mejaKosong) {
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(new JLabel("Pilih Meja:"), gbc);

        cbMeja = new JComboBox<>(mejaKosong.toArray(new String[0]));
        cbMeja.setFont(new Font("Poppins", Font.PLAIN, 13));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(cbMeja, gbc);
    }

    private void addMenuSection(JPanel panel, GridBagConstraints gbc, List<String> daftarMenu) {
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(new JLabel("Pilih Menu:"), gbc);

        cbMenu = new JComboBox<>(daftarMenu.toArray(new String[0]));
        cbMenu.setFont(new Font("Poppins", Font.PLAIN, 13));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(cbMenu, gbc);
    }

    private void addStatusSection(JPanel panel, GridBagConstraints gbc) {
        lblStatus = new JLabel("Status Pesanan:");
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(lblStatus, gbc);

        cbStatus = new JComboBox<>();
        cbStatus.setFont(new Font("Poppins", Font.PLAIN, 13));
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(cbStatus, gbc);
    }

    private void addQuantitySubtotalSection(JPanel panel, GridBagConstraints gbc) {
        JPanel panelQtySub = new JPanel(new GridLayout(1, 2, 15, 0));
        panelQtySub.setOpaque(false);

        JPanel pQty = createQuantityPanel();
        JPanel pSub = createSubtotalPanel();

        panelQtySub.add(pQty);
        panelQtySub.add(pSub);

        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(panelQtySub, gbc);
    }

    private JPanel createQuantityPanel() {
        JPanel pQty = new JPanel(new BorderLayout(0, 5));
        pQty.setOpaque(false);
        pQty.add(new JLabel("Jumlah (Qty):"), BorderLayout.NORTH);

        spQty = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spQty.setFont(new Font("Poppins", Font.PLAIN, 13));
        pQty.add(spQty, BorderLayout.CENTER);

        return pQty;
    }

    private JPanel createSubtotalPanel() {
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

        return pSub;
    }

    private void addNotesSection(JPanel panel, GridBagConstraints gbc) {
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(new JLabel("Catatan (Opsional):"), gbc);

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
        panel.add(new JScrollPane(txtCatatan), gbc);
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        footer.setBackground(FOOTER_BG_COLOR);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));

        btnBatal = createButton("Batal", BUTTON_CANCEL_COLOR);
        btnSimpan = createButton("Simpan", BUTTON_COLOR);

        footer.add(btnBatal);
        footer.add(btnSimpan);

        return footer;
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

    // ========== EVENT HANDLING METHODS ==========
    private void setupEventListeners() {
        btnBatal.addActionListener(e -> dispose());
        cbMenu.addActionListener(e -> updateSubtotal());
        spQty.addChangeListener(e -> updateSubtotal());
    }

    private void setupAutoCompleteFields() {
        cbMenu.setEditable(true);
        JTextComponent menuEditor = (JTextComponent) cbMenu.getEditor().getEditorComponent();
        menuEditor.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Ketik nama menu...");
        addSearchLogic(cbMenu, menuEditor, originalMenuList);

        cbNamaPelanggan.setEditable(true);
        JTextComponent pelangganEditor = (JTextComponent) cbNamaPelanggan.getEditor().getEditorComponent();
        pelangganEditor.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Cari/ketik nama pelanggan...");
        addSearchLogic(cbNamaPelanggan, pelangganEditor, originalPelangganList);
    }

    private void addSearchLogic(JComboBox<String> comboBox, JTextComponent editor, List<String> dataList) {
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (isNavigationKey(e)) {
                    return;
                }

                int caretPosition = editor.getCaretPosition();

                String currentText = editor.getText();

                DefaultComboBoxModel<String> model = buildFilteredModel(currentText, dataList);
                comboBox.setModel(model);

                editor.setText(currentText);

                if (caretPosition <= currentText.length()) {
                    editor.setCaretPosition(caretPosition);
                }

                updatePopupVisibility(comboBox, currentText, model);
            }
        });
    }

    private boolean isNavigationKey(KeyEvent e) {
        return e.getKeyCode() == KeyEvent.VK_UP
                || e.getKeyCode() == KeyEvent.VK_DOWN
                || e.getKeyCode() == KeyEvent.VK_LEFT
                || e.getKeyCode() == KeyEvent.VK_RIGHT
                || e.getKeyCode() == KeyEvent.VK_ENTER;
    }

    private DefaultComboBoxModel<String> buildFilteredModel(String text, List<String> dataList) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        String filter = text.toLowerCase();
        for (String item : dataList) {
            if (item.toLowerCase().contains(filter)) {
                model.addElement(item);
            }
        }
        return model;
    }

    private void updatePopupVisibility(JComboBox<String> comboBox, String text, DefaultComboBoxModel<String> model) {
        if (text.isEmpty()) {
            comboBox.hidePopup();
        } else if (model.getSize() > 0) {
            comboBox.showPopup();
        } else {
            comboBox.hidePopup();
        }
    }

    // ========== BUSINESS LOGIC METHODS ==========
    private void updateSubtotal() {
        try {
            Object selectedItem = cbMenu.getSelectedItem();
            if (selectedItem == null) {
                return;
            }

            String selected = selectedItem.toString();
            double harga = extractPrice(selected);
            int qty = (int) spQty.getValue();
            txtSubtotal.setText("Rp " + df.format(harga * qty));
        } catch (Exception e) {
            txtSubtotal.setText("Rp 0");
        }
    }

    private double extractPrice(String menuText) {
        try {
            if (!menuText.contains("Rp ")) {
                return 0;
            }

            int start = menuText.indexOf("Rp ") + 3;
            int end = menuText.indexOf(")", start);
            if (end == -1) {
                end = menuText.length();
            }

            String hargaText = menuText.substring(start, end).replaceAll("[^0-9]", "");
            return Double.parseDouble(hargaText);
        } catch (Exception e) {
            return 0;
        }
    }

    // ========== PUBLIC GETTER/SETTER METHODS ==========
    public String getNama() {
        return cbNamaPelanggan.getEditor().getItem().toString().trim();
    }

    public void setNama(String nama) {
        this.txtNamaPelanggan.setText(nama);
        this.cbNamaPelanggan.getEditor().setItem(nama);
    }

    public String getMeja() {
        return cbMeja.getSelectedItem() != null ? cbMeja.getSelectedItem().toString() : "";
    }

    public void setMeja(String nomorMeja) {
        for (int i = 0; i < cbMeja.getItemCount(); i++) {
            String item = cbMeja.getItemAt(i).toString();
            if (item.equals(nomorMeja) || item.startsWith(nomorMeja + " ")) {
                cbMeja.setSelectedIndex(i);
                return;
            }
        }
    }

    public String getMenu() {
        return cbMenu.getEditor().getItem().toString();
    }

    public void setMenu(String namaMenu) {
        for (String item : originalMenuList) {
            if (item.contains(namaMenu)) {
                cbMenu.setSelectedItem(item);
                return;
            }
        }
    }

    public int getQty() {
        return (int) spQty.getValue();
    }

    public void setQty(int qty) {
        this.spQty.setValue(qty);
    }

    public String getCatatan() {
        return txtCatatan.getText();
    }

    public void setCatatan(String catatan) {
        this.txtCatatan.setText(catatan);
    }

    public String getStatus() {
        return cbStatus.getSelectedItem() != null ? cbStatus.getSelectedItem().toString() : "";
    }

    public void setStatusTerpilih(String status) {
        this.cbStatus.setSelectedItem(status);
    }

    public void setDaftarStatus(List<String> daftarStatus) {
        cbStatus.removeAllItems();
        for (String status : daftarStatus) {
            cbStatus.addItem(status);
        }
    }

    public JButton getBtnSimpan() {
        return btnSimpan;
    }

    public void setStatusVisible(boolean visible) {
        lblStatus.setVisible(visible);
        cbStatus.setVisible(visible);
    }

    public void setDetailMode() {
        setTitle("Detail Pesanan");
        lblJudul.setText("Detail Pesanan");
        setStatusVisible(true);
        txtNamaPelanggan.setEditable(false);
        txtNamaPelanggan.setBackground(READONLY_BG_COLOR);
        txtNamaPelanggan.setVisible(true);
        cbNamaPelanggan.setVisible(false);
        txtCatatan.setEditable(false);
        txtCatatan.setBackground(READONLY_BG_COLOR);
        cbMeja.setEnabled(false);
        cbMenu.setEnabled(false);
        cbStatus.setEnabled(false);
        spQty.setEnabled(false);
        btnSimpan.setVisible(false);
        btnBatal.setText("Tutup");
    }
}
