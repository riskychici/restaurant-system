package project_oop.controller;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import project_oop.model.m_daftarMenu;
import project_oop.model.m_karyawan;
import project_oop.view.beranda;
import project_oop.view.daftarMenu;
import project_oop.view.karyawan;
import project_oop.view.meja;
import project_oop.view.pembayaran;
import project_oop.view.pesanan;
import project_oop.view.login;
import style_table.ModernTable;

/**
 * Controller untuk mengelola Daftar Menu
 */
public class c_karyawan {

    // ==================== ATRIBUT ====================
    // Model
    private m_karyawan model;

    // View
    private beranda view;
    private pesanan view2;
    private daftarMenu view3;
    private karyawan view4;
    private meja view5;
    private pembayaran view6;
    private login view7;

    // ==================== CONSTRUCTOR ====================
    public c_karyawan() throws SQLException {
        inisialisasiTemaDanTabel();
        inisialisasiKomponen();
        aturEventListeners();
        tampilkanDaftarMenu();
    }

    // ==================== METHOD INISIALISASI ====================
    private void inisialisasiTemaDanTabel() {
        try {
            FlatLightLaf.setup();
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.selectionBackground", new Color(220, 235, 252));
            UIManager.put("Table.selectionForeground", Color.BLACK);
            UIManager.put("Table.alternateRowColor", new Color(250, 250, 250));
        } catch (Exception e) {
            System.err.println("FlatLaf gagal di-load: " + e.getMessage());
        }
    }

    private void inisialisasiKomponen() throws SQLException {
        this.model = new m_karyawan();
        this.view = new beranda();
        this.view2 = new pesanan();
        this.view3 = new daftarMenu();
        this.view4 = new karyawan();
        this.view4.setVisible(true);
    }

    private void aturEventListeners() {
        view4.getBtnCari().addActionListener(e -> tampilkanDaftarMenu());
        view4.getBtnSidebarBeranda().addActionListener(new btnSidebarBeranda());
        view4.getBtnSidebarPesanan().addActionListener(new btnSidebarPesanan());
        view4.getBtnSidebarDaftarMenu().addActionListener(new btnSidebarDaftarMenu());
        view4.getBtnSidebarMeja().addActionListener(new btnSidebarMeja());
        view4.getBtnKeluar().addActionListener(new btnKeluar());
    }

    // ==================== METHOD TAMPILAN ====================
    public void tampilkanDaftarMenu() {
        try {
            String search = view4.getTxtSearch();
            List<Object[]> dataFromDB = model.getKaryawan(search);
            List<Object[]> transformedData = transformDataForTable(dataFromDB);
            renderTable(transformedData);
        } catch (SQLException ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private List<Object[]> transformDataForTable(List<Object[]> dataFromDB) {
        List<Object[]> transformedData = new ArrayList<>();

        for (Object[] row : dataFromDB) {
            Object[] newRow = new Object[5];

            // Kolom 0: ID
            newRow[0] = row[0];

            // Kolom 1: Nama Menu
            String nama = row[1] != null ? row[1].toString() : "";
            newRow[1] = new Object[]{"", nama};

            // Kolom 2: Kategori
            String kategori = row[2] != null ? row[2].toString() : "";
            newRow[2] = kategori;

            // Kolom 3: Harga
            String harga = row[3] != null ? row[3].toString() : "";
            newRow[3] = new String[]{harga};

            // Kolom 5: Aksi
            newRow[4] = "";

            transformedData.add(newRow);
        }

        return transformedData;
    }

    private void renderTable(List<Object[]> data) {
        new ModernTable(view4.getTblKaryawan())
                .setColumns(new String[]{"No", "ID", "Nama", "No Telp", "Role", "Aksi"})
                .hideColumn(1)
                .configureColumn(0, ModernTable.ColumnType.NUMBER, 50)
                .configureColumn(2, ModernTable.ColumnType.MULTI_LINE, 300)
                .configureColumn(3, ModernTable.ColumnType.TEXT, 50)
                .configureColumn(4, ModernTable.ColumnType.PRICE, 180)
                .configureColumn(5, ModernTable.ColumnType.ACTIONS, 230)
                .setRowHeight(70)
                .addActionButton("Detail", new Color(59, 130, 246), this::handleDetailAction)
                .addActionButton("Edit", new Color(251, 146, 60), this::handleEditAction)
                .addActionButton("Hapus", new Color(239, 68, 68), this::handleDeleteAction)
                .setData(data)
                .render();
    }

    // ==================== HANDLER TOMBOL AKSI (Detail, Edit, Hapus) ====================
    private void handleDetailAction(int row, Object[] rowData) {
        String id = rowData[1].toString();
        tampilDetail(id);
    }

    private void handleEditAction(int row, Object[] rowData) {
        String id = rowData[1].toString();
        editMenu(id);
    }

    private void handleDeleteAction(int row, Object[] rowData) {
        String id = rowData[1].toString();
        int confirm = JOptionPane.showConfirmDialog(
                view4,
                "Yakin ingin menghapus produk?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                model.hapusKaryawan(id);
                showSuccess("Produk berhasil dihapus!");
                tampilkanDaftarMenu();
            } catch (Exception e) {
                showError("Gagal menghapus produk: " + e.getMessage());
            }
        }
    }

    private void tampilDetail(String id) {
        JOptionPane.showMessageDialog(view4, "Detail Produk ID: " + id);
        System.out.println("Detail produk: " + id);
    }

    private void editMenu(String id) {
        JOptionPane.showMessageDialog(view4, "Edit Produk ID: " + id);
        System.out.println("Edit produk: " + id);
    }

    // ==================== METHOD DIALOG PESAN ====================
    private void showError(String message) {
        JOptionPane.showMessageDialog(view4, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(view4, message, "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== BAGIAN UNTUK MENGATUR AKSI TOMBOL ====================
    private class btnSidebarBeranda implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_pesanan();
                view4.dispose();
            } catch (SQLException ex) {
                System.getLogger(c_pesanan.class.getName())
                        .log(System.Logger.Level.ERROR, "Kesalahan navigasi ke Beranda", ex);
            }
        }
    }

    private class btnSidebarPesanan implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_pesanan();
                view4.dispose();
            } catch (SQLException ex) {
                System.getLogger(c_pesanan.class.getName())
                        .log(System.Logger.Level.ERROR, "Kesalahan navigasi ke Pesanan", ex);
            }
        }
    }
    
    private class btnSidebarMeja implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_meja();
                view4.dispose();
            } catch (SQLException ex) {
                System.getLogger(c_pesanan.class.getName())
                        .log(System.Logger.Level.ERROR, "Kesalahan navigasi ke Pesanan", ex);
            }
        }
    }
    
    private class btnSidebarDaftarMenu implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_daftarMenu();
                view4.dispose();
            } catch (SQLException ex) {
                System.getLogger(c_pesanan.class.getName())
                        .log(System.Logger.Level.ERROR, "Kesalahan navigasi ke Pesanan", ex);
            }
        }
    }

    private class btnKeluar implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int konfirmasi = JOptionPane.showConfirmDialog(
                    view4,
                    "Apakah kamu yakin ingin keluar dari akun ini?",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION
            );

            if (konfirmasi == JOptionPane.YES_OPTION) {
                try {
                    new c_user();
                    view4.dispose();
                } catch (Exception ex) {
                    System.err.println("Gagal navigasi ke Login: " + ex.getMessage());
                }
            }

        }

    }
}
