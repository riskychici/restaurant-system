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
import project_oop.view.formKaryawanDialog;

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
        this.view5 = new meja();
        this.view6 = new pembayaran();
        this.view4.setVisible(true);
    }

    private void aturEventListeners() {
        view4.getBtnCari().addActionListener(e -> tampilkanDaftarMenu());
        view4.getBtnSidebarBeranda().addActionListener(new btnSidebarBeranda());
        view4.getBtnSidebarPesanan().addActionListener(new btnSidebarPesanan());
        view4.getBtnSidebarDaftarMenu().addActionListener(new btnSidebarDaftarMenu());
        view4.getBtnSidebarMeja().addActionListener(new btnSidebarMeja());
        view4.getBtnSidebarPembayaran().addActionListener(new btnSidebarPembayaran());
        view4.getBtnKeluar().addActionListener(new btnKeluar());
        view4.getBtnTambahKaryawan().addActionListener(e -> handleTambahKaryawan());
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

            // Kolom 1: Nama Karyawan
            String nama = row[1] != null ? row[1].toString() : "";
            newRow[1] = new Object[]{"", nama};

            // Kolom 2: No Telepon
            String noTelp = row[2] != null ? row[2].toString() : "";
            newRow[2] = noTelp;

            // Kolom 3: Role
            String role = row[3] != null ? row[3].toString() : "";
            newRow[3] = new String[]{role};

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
    private void handleTambahKaryawan() {
        try {
            List<String> roles = model.ambilRoles();

            formKaryawanDialog dialog = new formKaryawanDialog(view4, roles);

            dialog.getBtnSimpan().addActionListener(event -> {
                String nama = dialog.getNama();
                String noTelp = dialog.getNoTelp();
                int idRole = dialog.getIdRole();

                if (nama.isEmpty() || noTelp.isEmpty()) {
                    showError("Nama dan Nomor Telepon wajib diisi!");
                    return;
                }

                try {
                    String hasil = model.tambahKaryawan(nama, noTelp, idRole);

                    if (hasil.startsWith("GAGAL")) {
                        showError(hasil);
                    } else {
                        showSuccess(hasil);
                        dialog.dispose();
                        tampilkanDaftarMenu();
                    }
                } catch (SQLException ex) {
                    showError("Gagal menyimpan ke database: " + ex.getMessage());
                }
            });

            dialog.setVisible(true);

        } catch (SQLException ex) {
            showError("Gagal memuat data Role: " + ex.getMessage());
        }
    }

    private void handleDetailAction(int row, Object[] rowData) {
        try {
            String id = rowData[1].toString();

            String nama = "";
            if (rowData[2] instanceof Object[]) {
                Object[] nameData = (Object[]) rowData[2];
                nama = (nameData.length > 1) ? nameData[1].toString() : nameData[0].toString();
            } else {
                nama = rowData[2].toString();
            }

            String noTelp = rowData[3].toString();

            String roleStr = "";
            if (rowData[4] instanceof Object[]) {
                Object[] roleData = (Object[]) rowData[4];
                roleStr = (roleData.length > 1) ? roleData[1].toString() : roleData[0].toString();
            } else {
                roleStr = rowData[4].toString();
            }

            int idRole = 0;
            if (roleStr.contains(" - ")) {
                idRole = Integer.parseInt(roleStr.split(" - ")[0]);
            } else {
                idRole = cariIdRoleDariNama(roleStr);
            }

            List<String> roles = model.ambilRoles();
            formKaryawanDialog dialog = new formKaryawanDialog(view4, roles);

            dialog.setData(id, nama, noTelp, idRole);
            dialog.setDetailMode();
            dialog.setVisible(true);

        } catch (Exception ex) {
            showError("Gagal memuat detail: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private int cariIdRoleDariNama(String namaRole) {
        try {
            List<String> roles = model.ambilRoles();
            for (String r : roles) {
                String[] parts = r.split(" - ");
                if (parts.length >= 2) {
                    int id = Integer.parseInt(parts[0].trim());
                    String namaDiList = parts[1].trim();
                    if (namaDiList.equalsIgnoreCase(namaRole.trim())) {
                        return id;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal mencari ID Role: " + e.getMessage());
        }
        return 1;
    }

    private void handleEditAction(int row, Object[] rowData) {
        try {
            String id = rowData[1].toString();

            String nama = "";
            if (rowData[2] instanceof Object[]) {
                Object[] nameData = (Object[]) rowData[2];
                nama = (nameData.length > 1) ? nameData[1].toString() : nameData[0].toString();
            } else {
                nama = rowData[2].toString();
            }

            String noTelp = rowData[3].toString();

            String roleStr = "";
            if (rowData[4] instanceof Object[]) {
                Object[] roleData = (Object[]) rowData[4];
                roleStr = roleData[0].toString();
            } else {
                roleStr = rowData[4].toString();
            }

            int idRole = cariIdRoleDariNama(roleStr);

            List<String> roles = model.ambilRoles();
            formKaryawanDialog dialog = new formKaryawanDialog(view4, roles);

            dialog.setData(id, nama, noTelp, idRole);

            dialog.getBtnSimpan().addActionListener(e -> {
                try {
                    String hasil = model.updateKaryawan(
                            dialog.getIdKaryawan(),
                            dialog.getNama(),
                            dialog.getNoTelp(),
                            dialog.getIdRole()
                    );

                    if (hasil.startsWith("GAGAL")) {
                        showError(hasil);
                    } else {
                        showSuccess(hasil);
                        dialog.dispose();
                        tampilkanDaftarMenu();
                    }
                } catch (SQLException ex) {
                    showError("Gagal Update: " + ex.getMessage());
                }
            });

            dialog.setVisible(true);

        } catch (Exception ex) {
            showError("Terjadi kesalahan saat membuka form edit: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void handleDeleteAction(int row, Object[] rowData) {
        String id = rowData[1].toString();

        String nama = "";
        if (rowData[2] instanceof Object[]) {
            Object[] nameData = (Object[]) rowData[2];
            nama = (nameData.length > 1) ? nameData[1].toString() : nameData[0].toString();
        } else {
            nama = rowData[2].toString();
        }

        int confirm = JOptionPane.showConfirmDialog(view4,
                "Apakah Anda yakin ingin menghapus karyawan: " + nama + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String hasil = model.hapusKaryawan(id);
                if (hasil.startsWith("Berhasil")) {
                    showSuccess(hasil);
                    tampilkanDaftarMenu();
                } else {
                    showError(hasil);
                }
            } catch (SQLException ex) {
                showError("Gagal menghapus: " + ex.getMessage());
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
                new c_beranda();
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

    private class btnSidebarPembayaran implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_pembayaran();
                view4.dispose();
            } catch (SQLException ex) {
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
