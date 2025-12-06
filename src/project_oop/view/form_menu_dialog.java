package project_oop.view;

import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatClientProperties;

public class form_menu_dialog extends JDialog {
    
    private JComboBox<String> cmbKategori;
    private JTextField txtNamaProduk;
    private JTextField txtHarga;
    private JSpinner spnStok;
    private JButton btnSimpan;
    private JButton btnBatal;
    
    // Constructor untuk mode INSERT (tambah baru)
    public form_menu_dialog(JFrame parent) {
        this(parent, null);
    }
    
    // Constructor untuk mode UPDATE (edit)
    public form_menu_dialog(JFrame parent, String idProduk) {
        super(parent, idProduk == null ? "Tambah Produk" : "Edit Produk", true); // true = modal
        initComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(500, 450);
        setResizable(false);
        
        // Panel utama dengan padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Title
        JLabel lblTitle = new JLabel(getTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(lblTitle, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Kategori Produk
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblKategori = new JLabel("Kategori Produk:");
        lblKategori.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(lblKategori, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cmbKategori = new JComboBox<>();
        cmbKategori.setPreferredSize(new Dimension(300, 35));
        cmbKategori.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Pilih kategori...");
        cmbKategori.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mainPanel.add(cmbKategori, gbc);
        
        // Nama Produk
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel lblNama = new JLabel("Nama Produk:");
        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(lblNama, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtNamaProduk = new JTextField();
        txtNamaProduk.setPreferredSize(new Dimension(300, 35));
        txtNamaProduk.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama produk...");
        txtNamaProduk.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mainPanel.add(txtNamaProduk, gbc);
        
        // Harga
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        JLabel lblHarga = new JLabel("Harga:");
        lblHarga.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(lblHarga, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtHarga = new JTextField();
        txtHarga.setPreferredSize(new Dimension(300, 35));
        txtHarga.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Contoh: 50000");
        txtHarga.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mainPanel.add(txtHarga, gbc);
        
        // Stok
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        JLabel lblStok = new JLabel("Stok:");
        lblStok.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(lblStok, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 99999, 1);
        spnStok = new JSpinner(spinnerModel);
        spnStok.setPreferredSize(new Dimension(300, 35));
        spnStok.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mainPanel.add(spnStok, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Panel tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        buttonPanel.setBackground(Color.WHITE);
        
        btnBatal = new JButton("Batal");
        btnBatal.setPreferredSize(new Dimension(100, 35));
        btnBatal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBatal.setBackground(new Color(156, 163, 175));
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setFocusPainted(false);
        btnBatal.addActionListener(e -> dispose());
        
        btnSimpan = new JButton("Simpan");
        btnSimpan.setPreferredSize(new Dimension(100, 35));
        btnSimpan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSimpan.setBackground(new Color(34, 197, 94));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        
        buttonPanel.add(btnBatal);
        buttonPanel.add(btnSimpan);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Getters
    public JComboBox<String> getCmbKategori() {
        return cmbKategori;
    }
    
    public JTextField getTxtNamaProduk() {
        return txtNamaProduk;
    }
    
    public JTextField getTxtHarga() {
        return txtHarga;
    }
    
    public JSpinner getSpnStok() {
        return spnStok;
    }
    
    public JButton getBtnSimpan() {
        return btnSimpan;
    }
    
    public JButton getBtnBatal() {
        return btnBatal;
    }
    
    // Method helper untuk set data saat edit
    public void setDataProduk(String kategori, String nama, String harga, int stok) {
        cmbKategori.setSelectedItem(kategori);
        txtNamaProduk.setText(nama);
        txtHarga.setText(harga);
        spnStok.setValue(stok);
    }
}