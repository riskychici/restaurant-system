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
import project_oop.model.m_pesanan;
import project_oop.view.beranda;
import project_oop.view.daftarMenu;
import project_oop.view.karyawan;
import project_oop.view.meja;
import project_oop.view.pembayaran;
import project_oop.view.pesanan;
import project_oop.view.login;
import style_table.ModernTable;
import project_oop.view.formPesananDialog;
import session_user.user_session;

/**
 * Controller untuk mengelola Pesanan
 */
public class c_pesanan {

    // ==================== ATRIBUT ====================
    // Model
    private m_pesanan model;

    // View
    private beranda view;
    private pesanan view2;
    private daftarMenu view3;
    private karyawan view4;
    private meja view5;
    private pembayaran view6;
    private login view7;

    // ==================== CONSTRUCTOR ====================
    public c_pesanan() throws SQLException {
        inisialisasiTemaDanTabel();
        inisialisasiKomponen();
        aturEventListeners();
        tampilkanPesanan();
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
        this.model = new m_pesanan();
        this.view = new beranda();
        this.view2 = new pesanan();
        this.view3 = new daftarMenu();
        this.view4 = new karyawan();
        this.view2.setVisible(true);
    }

    private void aturEventListeners() {
        view2.getBtnCari().addActionListener(e -> tampilkanPesanan());
//        view2.getBtnSidebarBeranda().addActionListener(new btnSidebarBeranda());
        view2.getBtnSidebarDaftarMenu().addActionListener(new btnSidebarDaftarMenu());
        view2.getBtnSidebarKaryawan().addActionListener(new btnSidebarKaryawan());
        view2.getBtnSidebarMeja().addActionListener(new btnSidebarMeja());
        view2.getBtnKeluar().addActionListener(new btnKeluar());
        view2.getBtnBuatPesanan().addActionListener(e -> {
            try {
                tambahPesanan();
            } catch (SQLException ex) {
                System.getLogger(c_pesanan.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
    }

    // ==================== METHOD TAMPILAN ====================
    public void tampilkanPesanan() {
        try {
            String search = view2.getTxtSearch();
            List<Object[]> dataFromDB = model.getPesanan(search);
            List<Object[]> transformedData = transformDataForTable(dataFromDB);
            renderTable(transformedData);
        } catch (SQLException ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private List<Object[]> transformDataForTable(List<Object[]> dataFromDB) {
        List<Object[]> transformedData = new ArrayList<>();
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
        java.text.DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(symbols);

        for (Object[] row : dataFromDB) {
            Object[] newRow = new Object[8];

            // Kolom 0: ID
            newRow[0] = row[0];

            // Kolom 1: Nama Pelanggan
            String nama = row[1] != null ? row[1].toString() : "";
            newRow[1] = new Object[]{"", nama};

            // Kolom 2: Kategori
            String meja = row[2] != null ? row[2].toString() : "";
            newRow[2] = meja;

            // Kolom 3: Menu
            String menu = row[3] != null ? row[3].toString() : "";
            newRow[3] = new String[]{menu};

            // Kolom 4: Qty
            String qty = row[4] != null ? row[4].toString() : "";
            newRow[4] = qty;

            // Total Harga
            String hargaStr = row[6] != null ? row[6].toString() : "0";
            try {
                // Bersihkan string dari karakter aneh dan ubah ke angka
                double hargaBersih = Double.parseDouble(hargaStr.replaceAll("[^0-9.]", ""));
                // Masukkan ke array dengan format titik
                newRow[5] = new String[]{"Rp " + df.format(hargaBersih)};
            } catch (Exception e) {
                newRow[5] = new String[]{"Rp " + hargaStr};
            }

            // Kolom 4: Status Pesanan
            newRow[6] = row[5] != null ? row[5].toString() : "0";

            // Kolom 5: Aksi
            newRow[7] = "";

            transformedData.add(newRow);
        }

        return transformedData;
    }

    private void renderTable(List<Object[]> data) {
        // 1. Jalankan render seperti biasa
        ModernTable mt = new ModernTable(view2.getTblPesanan())
                .setColumns(new String[]{"No", "ID", "Nama", "Meja", "Menu", "Qty", "Subtotal", "Status Pesanan", "Aksi"})
                .configureColumn(0, ModernTable.ColumnType.NUMBER, 50)
                .configureColumn(2, ModernTable.ColumnType.MULTI_LINE, 200)
                .configureColumn(3, ModernTable.ColumnType.CATEGORY, 70)
                .configureColumn(4, ModernTable.ColumnType.PRICE, 200)
                .configureColumn(5, ModernTable.ColumnType.TEXT, 70)
                .configureColumn(7, ModernTable.ColumnType.STOCK, 120)
                .configureColumn(8, ModernTable.ColumnType.ACTIONS, 230)
                .setRowHeight(70)
                .addActionButton("Detail", new Color(59, 130, 246), this::handleDetailAction)
                .addActionButton("Edit", new Color(251, 146, 60), this::handleEditAction)
                .addActionButton("Hapus", new Color(239, 68, 68), this::handleDeleteAction)
                .setData(data);

        mt.render(); // Eksekusi render library

        // 2. PAKSA SEMBUNYIKAN MENGGUNAKAN SWING NATIVE (PASTI BERHASIL)
        // Kita sembunyikan kolom ID (indeks 1) dan Subtotal (indeks 6)
        int[] columnsToHide = {1, 6};

        for (int colIndex : columnsToHide) {
            view2.getTblPesanan().getColumnModel().getColumn(colIndex).setMinWidth(0);
            view2.getTblPesanan().getColumnModel().getColumn(colIndex).setMaxWidth(0);
            view2.getTblPesanan().getColumnModel().getColumn(colIndex).setWidth(0);
            view2.getTblPesanan().getColumnModel().getColumn(colIndex).setPreferredWidth(0);
        }
    }

    // ==================== HANDLER TOMBOL AKSI (Detail, Edit, Hapus) ====================
    private void tambahPesanan() throws SQLException {
        try {
            // 1. Ambil data awal untuk Form
            List<String> meja = model.getMejaKosong();
            List<String> menu = model.getDaftarMenu();

            formPesananDialog dialog = new formPesananDialog(view2, meja, menu);

            dialog.getBtnSimpan().addActionListener(e -> {
                try {
                    // 2. Ambil data dari form
                    String namaPelanggan = dialog.getNama();

                    // 3. Validasi Dasar
                    if (namaPelanggan == null || namaPelanggan.trim().isEmpty()) {
                        showError("Nama pelanggan tidak boleh kosong!");
                        return;
                    }

                    Integer idPers = user_session.getIdPersonal();
                    if (idPers == null) {
                        showError("Sesi login berakhir. Silakan login kembali.");
                        return;
                    }

                    java.util.UUID uuidKaryawan = model.getUUIDKaryawan(idPers);
                    if (uuidKaryawan == null) {
                        showError("User Anda tidak terhubung ke data Karyawan!");
                        return;
                    }

                    // 4. Parsing ID (Meja & Menu)
                    // Meja: "1 - Kosong" -> ambil "1"
                    int idMeja = Integer.parseInt(dialog.getMeja().split(" ")[0].replaceAll("[^0-9]", ""));

                    // Menu: "10 - Ayam Bakar" -> ambil "10"
                    String rawMenu = dialog.getMenu();
                    if (!rawMenu.contains(" - ")) {
                        showError("Silakan pilih menu dari daftar yang tersedia!");
                        return;
                    }
                    int idMenu = Integer.parseInt(rawMenu.split(" - ")[0]);

                    // 5. JSON Formatting Aman (Menghindari Error SQL JSON Syntax)
                    String catatanRaw = dialog.getCatatan() != null ? dialog.getCatatan() : "";
                    String catatanAman = catatanRaw.replace("\\", "\\\\")
                            .replace("\"", "\\\"")
                            .replace("\n", "\\n")
                            .replace("\r", "");

                    String jsonItems = String.format("[{\"id_menu\": %d, \"qty\": %d, \"catatan\": \"%s\"}]",
                            idMenu, dialog.getQty(), catatanAman);

                    // 6. Eksekusi Database
                    model.simpanPesananBaru(namaPelanggan, idMeja, uuidKaryawan, jsonItems);

                    // 7. Penanganan UI (Urutan sangat penting)
                    dialog.dispose(); // Tutup dialog dulu

                    // Gunakan SwingUtilities agar refresh tabel tidak mengganggu thread utama
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        showSuccess("Pesanan berhasil disimpan!");
                        try {
                            tampilkanPesanan(); // Refresh tabel di view utama
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            showError("Gagal memuat ulang tabel: " + ex.getMessage());
                        }
                    });

                } catch (NumberFormatException nfe) {
                    showError("Kesalahan format angka pada Meja atau Menu.");
                } catch (SQLException sqle) {
                    // Menangkap error dari Trigger Postgres (seperti Stok Habis)
                    showError("Gagal simpan ke database: " + sqle.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showError("Terjadi kesalahan sistem: " + ex.getMessage());
                }
            });

            dialog.setVisible(true);
        } catch (Exception ex) {
            showError("Gagal membuka form pesanan: " + ex.getMessage());
        }
    }

    private void handleDetailAction(int row, Object[] rowData) {
        try {
            java.util.UUID idPesanan = (java.util.UUID) rowData[1];
            String mejaAktif = rowData[3].toString(); // Ambil dari tabel (misal: "A5")

            // 1. Ambil daftar semua meja dari database
            List<String> listMejaRaw = model.getMejaKosong();
            List<String> listMejaFinal = new java.util.ArrayList<>();

            // 2. Cari apakah meja dari tabel sudah ada di daftar database (dengan status apapun)
            String targetMeja = "";
            for (String m : listMejaRaw) {
                if (m.startsWith(mejaAktif)) {
                    targetMeja = m; // Ketemu, misal "A5 - Dipakai"
                    break;
                }
            }

            // 3. Susun daftar final: Meja aktif harus di paling atas
            if (!targetMeja.isEmpty()) {
                listMejaFinal.add(targetMeja);
                for (String m : listMejaRaw) {
                    if (!m.equals(targetMeja)) {
                        listMejaFinal.add(m);
                    }
                }
            } else {
                // Jika tidak ketemu di list (keadaan darurat), masukkan yang dari tabel
                listMejaFinal.add(mejaAktif);
                listMejaFinal.addAll(listMejaRaw);
            }

            formPesananDialog dialog = new formPesananDialog(view2, listMejaFinal, model.getDaftarMenu());
            dialog.setDaftarStatus(model.getDaftarStatus());
            dialog.setDetailMode();

            Object[] namaArr = (Object[]) rowData[2];
            dialog.setNama(namaArr[1].toString());

            // Set Meja ke item pertama (yang sudah kita urutkan tadi)
            dialog.setMeja(listMejaFinal.get(0));

            String[] menuArr = (String[]) rowData[4];
            dialog.setMenu(menuArr[0]);
            dialog.setQty(Integer.parseInt(rowData[5].toString()));
            dialog.setStatusTerpilih(rowData[7].toString());
            dialog.setCatatan(model.getCatatanById(idPesanan));

            dialog.setVisible(true);
        } catch (Exception ex) {
            showError("Gagal menampilkan detail: " + ex.getMessage());
        }
    }

    private void handleEditAction(int row, Object[] rowData) {
        try {
            java.util.UUID idPesanan = (java.util.UUID) rowData[1];
            String mejaAktif = rowData[3].toString();

            List<String> listMejaRaw = model.getMejaKosong();
            List<String> listMejaFinal = new java.util.ArrayList<>();

            String targetMeja = "";
            for (String m : listMejaRaw) {
                if (m.startsWith(mejaAktif)) {
                    targetMeja = m;
                    break;
                }
            }

            if (!targetMeja.isEmpty()) {
                listMejaFinal.add(targetMeja);
                for (String m : listMejaRaw) {
                    if (!m.equals(targetMeja)) {
                        listMejaFinal.add(m);
                    }
                }
            } else {
                listMejaFinal.add(mejaAktif);
                listMejaFinal.addAll(listMejaRaw);
            }

            formPesananDialog dialog = new formPesananDialog(view2, listMejaFinal, model.getDaftarMenu());
            dialog.setStatusVisible(true);
            dialog.setDaftarStatus(model.getDaftarStatus());

            Object[] namaArr = (Object[]) rowData[2];
            dialog.setNama(namaArr[1].toString());
            dialog.setMeja(listMejaFinal.get(0));

            String[] menuArr = (String[]) rowData[4];
            dialog.setMenu(menuArr[0]);
            dialog.setQty(Integer.parseInt(rowData[5].toString()));
            dialog.setStatusTerpilih(rowData[7].toString());
            dialog.setCatatan(model.getCatatanById(idPesanan));

            for (java.awt.event.ActionListener al : dialog.getBtnSimpan().getActionListeners()) {
                dialog.getBtnSimpan().removeActionListener(al);
            }

            dialog.getBtnSimpan().setText("Update Pesanan");
            dialog.getBtnSimpan().addActionListener(e -> {
                try {
                    // Ambil angka mejanya saja (A5 - Dipakai -> 5)
                    int nomorMeja = Integer.parseInt(dialog.getMeja().split(" ")[0].replaceAll("[^0-9]", ""));
                    int idMenu = Integer.parseInt(dialog.getMenu().split(" - ")[0]);

                    String jsonItems = String.format("[{\"id_menu\": %d, \"qty\": %d, \"catatan\": \"%s\"}]",
                            idMenu, dialog.getQty(), dialog.getCatatan().replace("\"", "\\\""));

                    model.editPesanan(idPesanan, dialog.getNama(), nomorMeja, jsonItems, dialog.getStatus());
                    dialog.dispose();
                    showSuccess("Pesanan berhasil diperbarui!");
                    tampilkanPesanan();
                } catch (Exception ex) {
                    showError("Update gagal: " + ex.getMessage());
                }
            });

            dialog.setVisible(true);
        } catch (Exception ex) {
            showError("Gagal membuka form edit: " + ex.getMessage());
        }
    }

    private void handleDeleteAction(int row, Object[] rowData) {
        // 1. Ambil ID (UUID) untuk keperluan database (Kolom 1 - Tersembunyi)
        String idPesanan = rowData[1].toString();

        // 2. Ambil Nama Pelanggan untuk tampilan user (Kolom 2)
        // Ingat: Di transformData Anda mengisi newRow[1] = new Object[]{"", nama};
        // Jadi di rowData[2] (indeks kolom Pelanggan), kita ambil indeks ke-1 dari array-nya.
        Object[] namaData = (Object[]) rowData[2];
        String namaPelanggan = namaData[1].toString();

        // 3. Tampilkan konfirmasi menggunakan Nama Pelanggan
        int confirm = JOptionPane.showConfirmDialog(
                view2,
                "Apakah Anda yakin ingin menghapus pesanan atas nama: " + namaPelanggan + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Tetap kirim idPesanan (UUID) ke model karena database butuh ID
                model.hapusPesanan(idPesanan);

                showSuccess("Pesanan milik " + namaPelanggan + " berhasil dihapus!");
                tampilkanPesanan(); // Refresh tabel
            } catch (Exception e) {
                showError("Gagal menghapus pesanan: " + e.getMessage());
            }
        }
    }

    private void tampilDetail(String id) {
        JOptionPane.showMessageDialog(view2, "Detail Produk ID: " + id);
        System.out.println("Detail produk: " + id);
    }

    private void editMenu(String id) {
        JOptionPane.showMessageDialog(view2, "Edit Produk ID: " + id);
        System.out.println("Edit produk: " + id);
    }

    // ==================== METHOD DIALOG PESAN ====================
    private void showError(String message) {
        JOptionPane.showMessageDialog(view2, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(view2, message, "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== BAGIAN UNTUK MENGATUR AKSI TOMBOL ====================
//    private class btnSidebarBeranda implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            try {
    ////                new c_pesanan();
//                view2.dispose();
//            } catch (SQLException ex) {
//                System.getLogger(c_pesanan.class.getName())
//                        .log(System.Logger.Level.ERROR, "Kesalahan navigasi ke Beranda", ex);
//            }
//        }
//    }

    private class btnSidebarDaftarMenu implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_daftarMenu();
                view2.dispose();
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
                view2.dispose();
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
                view2.dispose();
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
                    view2,
                    "Apakah kamu yakin ingin keluar dari akun ini?",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION
            );

            if (konfirmasi == JOptionPane.YES_OPTION) {
                try {
                    new c_user();
                    view2.dispose();
                } catch (Exception ex) {
                    System.err.println("Gagal navigasi ke Login: " + ex.getMessage());
                }
            }

        }

    }

}
