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
import project_oop.model.m_meja;
import project_oop.view.beranda;
import project_oop.view.daftarMenu;
import project_oop.view.karyawan;
import project_oop.view.meja;
import project_oop.view.pembayaran;
import project_oop.view.pesanan;
import project_oop.view.login;
import style_table.ModernTable;
import project_oop.view.formMejaDialog;

/**
 * Controller untuk mengelola Daftar Menu
 */
public class c_meja {

    // ==================== ATRIBUT ====================
    // Model
    private m_meja model;

    // View
    private beranda view;
    private pesanan view2;
    private daftarMenu view3;
    private karyawan view4;
    private meja view5;
    private pembayaran view6;
    private login view7;

    // ==================== CONSTRUCTOR ====================
    public c_meja() throws SQLException {
        inisialisasiTemaDanTabel();
        inisialisasiKomponen();
        aturEventListeners();
        tampilkanDaftarMeja();
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
        this.model = new m_meja();
        this.view = new beranda();
        this.view2 = new pesanan();
        this.view3 = new daftarMenu();
        this.view4 = new karyawan();
        this.view5 = new meja();
        this.view5.setVisible(true);
    }

    private void aturEventListeners() {
        view5.getBtnCari().addActionListener(e -> tampilkanDaftarMeja());
        view5.getBtnSidebarBeranda().addActionListener(new btnSidebarBeranda());
        view5.getBtnSidebarPesanan().addActionListener(new btnSidebarPesanan());
        view5.getBtnSidebarDaftarMenu().addActionListener(new btnSidebarDaftarMenu());
        view5.getBtnSidebarKaryawan().addActionListener(new btnSidebarKaryawan());
        view5.getBtnKeluar().addActionListener(new btnKeluar());
        view5.getBtnTambahMeja().addActionListener(e -> DialogTambah());
    }

    // ==================== METHOD TAMPILAN ====================
    public void tampilkanDaftarMeja() {
        try {
            String search = view5.getTxtSearch();
            List<Object[]> dataFromDB = model.getDaftarMeja(search);
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
            String nomorMeja = row[1] != null ? row[1].toString() : "";
            newRow[1] = new Object[]{"", nomorMeja};

            // Kolom 2: Kategori
            String kapasitas = row[2] != null ? row[2].toString() : "";
            newRow[2] = kapasitas;

            // Kolom 3: Harga
            String statusMeja = row[3] != null ? row[3].toString() : "";
            newRow[3] = new String[]{statusMeja};

            // Kolom 5: Aksi
            newRow[4] = "";

            transformedData.add(newRow);
        }

        return transformedData;
    }

    private void renderTable(List<Object[]> data) {
        new ModernTable(view5.getTblMeja())
                .setColumns(new String[]{"No", "ID", "Nomor Meja", "Kapasitas", "Status Meja", "Aksi"})
                .hideColumn(1)
                .configureColumn(0, ModernTable.ColumnType.NUMBER, 50)
                .configureColumn(2, ModernTable.ColumnType.MULTI_LINE, 30)
                .configureColumn(3, ModernTable.ColumnType.TEXT, 50)
                .configureColumn(4, ModernTable.ColumnType.PRICE, 18)
                .configureColumn(5, ModernTable.ColumnType.ACTIONS, 230)
                .setRowHeight(70)
                .addActionButton("Detail", new Color(59, 130, 246), this::handleDetailAction)
                .addActionButton("Kosongkan", new Color(76, 155, 73), this::handleKosongkanMejaAction)
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

    private void handleKosongkanMejaAction(int row, Object[] rowData) {
        String id = rowData[1].toString();
        KosongkanMeja(id);
    }

    private void handleEditAction(int row, Object[] rowData) {
        try {
            int id = Integer.parseInt(rowData[1].toString());
            // Ambil detail terbaru dari database sebelum edit
            Object[] detail = model.getDetailMeja(id);

            if (detail != null) {
                formMejaDialog dialog = new formMejaDialog(view5,
                        (int) detail[0], (String) detail[1], (int) detail[2]);

                dialog.getBtnSimpan().addActionListener(e -> {
                    String nomorBaru = dialog.getNomorMeja();
                    int kapasitasBaru = dialog.getKapasitas();

                    try {
                        String pesan = model.updateMeja(id, nomorBaru, kapasitasBaru);
                        if (pesan.contains("GAGAL")) {
                            showError(pesan);
                        } else {
                            showSuccess(pesan);
                            dialog.dispose();
                            tampilkanDaftarMeja(); // Refresh tabel
                        }
                    } catch (SQLException ex) {
                        showError("Gagal update: " + ex.getMessage());
                    }
                });

                dialog.setVisible(true);
            }
        } catch (Exception e) {
            showError("Gagal memproses edit: " + e.getMessage());
        }
    }

    private void handleDeleteAction(int row, Object[] rowData) {
        int id = Integer.parseInt(rowData[1].toString());
        Object[] cellNomor = (Object[]) rowData[2];
        String nomorMeja = cellNomor[1].toString();

        int confirm = JOptionPane.showConfirmDialog(
                view5,
                "Apakah Anda yakin ingin menghapus Meja " + nomorMeja + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Panggil model hapusMeja
                String pesan = model.hapusMeja(id);

                if (pesan.contains("GAGAL")) {
                    showError(pesan);
                } else {
                    showSuccess(pesan);
                    tampilkanDaftarMeja(); // Refresh tabel
                }
            } catch (SQLException e) {
                showError("Gagal menghapus meja: " + e.getMessage());
            }
        }
    }

    private void tampilDetail(String id) {
        try {
            int idMeja = Integer.parseInt(id);
            Object[] detail = model.getDetailMeja(idMeja);

            if (detail != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("===== INFORMASI MEJA =====\n");
                sb.append("ID Meja      : ").append(detail[0]).append("\n");
                sb.append("Nomor Meja   : ").append(detail[1]).append("\n");
                sb.append("Kapasitas    : ").append(detail[2]).append(" Orang\n");
                sb.append("Status Meja  : ").append(detail[3]).append("\n");

                // Jika nama_pelanggan ada, berarti meja terisi
                if (detail[4] != null) {
                    sb.append("\n==== AKTIVITAS SAAT INI ====\n");
                    sb.append("Nama Pelanggan : ").append(detail[4]).append("\n");
                    sb.append("Jam Masuk      : ").append(detail[5]).append("\n");
                } else {
                    sb.append("\nKeterangan: Meja ini tidak digunakan.");
                }

                JOptionPane.showMessageDialog(view5, sb.toString(), "Detail Meja " + detail[1], JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            showError("Gagal menampilkan detail: " + e.getMessage());
        }
    }

    private void KosongkanMeja(String id) {
        int confirm = JOptionPane.showConfirmDialog(
                view5,
                "Apakah pelanggan sudah meninggalkan meja?",
                "Konfirmasi Meja Kosong",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idMeja = Integer.parseInt(id);

                String pesan = model.konfirmasiMejaKosong(idMeja);

                if (pesan.contains("GAGAL")) {
                    JOptionPane.showMessageDialog(view5, pesan, "Peringatan", JOptionPane.WARNING_MESSAGE);
                } else {
                    showSuccess(pesan);
                    tampilkanDaftarMeja();
                }

            } catch (NumberFormatException e) {
                showError("ID Meja tidak valid.");
            } catch (SQLException e) {
                showError("Gagal mengosongkan meja: " + e.getMessage());
            }
        }
    }

    private void DialogTambah() {
        formMejaDialog dialog = new formMejaDialog(view5);

        dialog.getBtnSimpan().addActionListener(e -> {
            String nomor = dialog.getNomorMeja();
            int kapasitas = dialog.getKapasitas();

            if (nomor.trim().isEmpty()) {
                showError("Nomor meja wajib diisi!");
                return;
            }

            try {
                // Memanggil model yang sudah memakai Function
                String pesan = model.tambahMeja(nomor, kapasitas);

                if (pesan.contains("GAGAL")) {
                    showError(pesan);
                } else {
                    showSuccess(pesan);
                    dialog.dispose();
                    tampilkanDaftarMeja(); // Refresh tabel biar meja baru muncul
                }
            } catch (SQLException ex) {
                showError("Terjadi kesalahan database: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private void editMenu(String id) {
        JOptionPane.showMessageDialog(view5, "Edit Produk ID: " + id);
        System.out.println("Edit produk: " + id);
    }

    // ==================== METHOD DIALOG PESAN ====================
    private void showError(String message) {
        JOptionPane.showMessageDialog(view5, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(view5, message, "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== BAGIAN UNTUK MENGATUR AKSI TOMBOL ====================
    private class btnSidebarBeranda implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_pesanan();
                view5.dispose();
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
                view5.dispose();
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
                view5.dispose();
            } catch (SQLException ex) {
                System.getLogger(c_pesanan.class.getName())
                        .log(System.Logger.Level.ERROR, "Kesalahan navigasi ke Pesanan", ex);
            }
        }
    }

    private class btnSidebarKaryawan implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_karyawan();
                view5.dispose();
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
                    view5,
                    "Apakah kamu yakin ingin keluar dari akun ini?",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION
            );

            if (konfirmasi == JOptionPane.YES_OPTION) {
                try {
                    new c_user();
                    view5.dispose();
                } catch (Exception ex) {
                    System.err.println("Gagal navigasi ke Login: " + ex.getMessage());
                }
            }

        }

    }
}
