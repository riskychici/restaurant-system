//package project_oop.controller;
//
//import com.formdev.flatlaf.FlatLightLaf;
//import java.awt.Color;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import javax.swing.JOptionPane;
//import javax.swing.UIManager;
//import project_oop.model.m_daftarMenu;
//import project_oop.model.m_pesanan;
//import project_oop.view.beranda;
//import project_oop.view.daftarMenu;
//import project_oop.view.karyawan;
//import project_oop.view.meja;
//import project_oop.view.pembayaran;
//import project_oop.view.pesanan;
//import project_oop.view.login;
//import style_table.ModernTable;
//import project_oop.view.formPesananDialog;
//import session_user.user_session;
//
///**
// * Controller untuk mengelola Pesanan
// */
//public class c_pesanan_backup {
//
//    // ==================== ATRIBUT ====================
//    // Model
//    private m_pesanan model;
//
//    // View
//    private beranda view;
//    private pesanan view2;
//    private daftarMenu view3;
//    private karyawan view4;
//    private meja view5;
//    private pembayaran view6;
//    private login view7;
//
//    // ==================== CONSTRUCTOR ====================
//    public c_pesanan_backup() throws SQLException {
//        inisialisasiTemaDanTabel();
//        inisialisasiKomponen();
//        aturEventListeners();
//        tampilkanPesanan();
//    }
//
//    // ==================== METHOD INISIALISASI ====================
//    private void inisialisasiTemaDanTabel() {
//        try {
//            FlatLightLaf.setup();
//            UIManager.setLookAndFeel(new FlatLightLaf());
//            UIManager.put("Table.showHorizontalLines", true);
//            UIManager.put("Table.showVerticalLines", false);
//            UIManager.put("Table.selectionBackground", new Color(220, 235, 252));
//            UIManager.put("Table.selectionForeground", Color.BLACK);
//            UIManager.put("Table.alternateRowColor", new Color(250, 250, 250));
//        } catch (Exception e) {
//            System.err.println("FlatLaf gagal di-load: " + e.getMessage());
//        }
//    }
//
//    private void inisialisasiKomponen() throws SQLException {
//        this.model = new m_pesanan();
//        this.view = new beranda();
//        this.view2 = new pesanan();
//        this.view3 = new daftarMenu();
//        this.view4 = new karyawan();
//        this.view2.setVisible(true);
//    }
//
//    private void aturEventListeners() {
//        view2.getBtnCari().addActionListener(e -> tampilkanPesanan());
////        view2.getBtnSidebarBeranda().addActionListener(new btnSidebarBeranda());
//        view2.getBtnSidebarDaftarMenu().addActionListener(new btnSidebarDaftarMenu());
//        view2.getBtnSidebarKaryawan().addActionListener(new btnSidebarKaryawan());
//        view2.getBtnSidebarMeja().addActionListener(new btnSidebarMeja());
//        view2.getBtnKeluar().addActionListener(new btnKeluar());
//        view2.getBtnBuatPesanan().addActionListener(e -> {
//            try {
//                tambahPesanan();
//            } catch (SQLException ex) {
//                System.getLogger(c_pesanan.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
//            }
//        });
//    }
//
//    // ==================== METHOD TAMPILAN ====================
//    public void tampilkanPesanan() {
//        try {
//            String search = view2.getTxtSearch();
//            List<Object[]> dataFromDB = model.getPesanan(search);
//            List<Object[]> transformedData = transformDataForTable(dataFromDB);
//            renderTable(transformedData);
//        } catch (SQLException ex) {
//            showError("Error: " + ex.getMessage());
//        }
//    }
//
//    private List<Object[]> transformDataForTable(List<Object[]> dataFromDB) {
//        List<Object[]> transformedData = new ArrayList<>();
//
//        for (Object[] row : dataFromDB) {
//            Object[] newRow = new Object[8];
//
//            // Kolom 0: ID
//            newRow[0] = row[0];
//
//            // Kolom 1: Nama Pelanggan
//            String nama = row[1] != null ? row[1].toString() : "";
//            newRow[1] = new Object[]{"", nama};
//
//            // Kolom 2: Kategori
//            String meja = row[2] != null ? row[2].toString() : "";
//            newRow[2] = meja;
//
//            // Kolom 3: Menu
//            String menu = row[3] != null ? row[3].toString() : "";
//            newRow[3] = new String[]{menu};
//
//            // Kolom 4: Qty
//            String qty = row[4] != null ? row[4].toString() : "";
//            newRow[4] = qty;
//
//            // Total Harga
//            String harga = row[6] != null ? row[6].toString() : "";
//            newRow[5] = new String[]{"Rp " + harga};
//
//            // Kolom 4: Status Pesanan
//            newRow[6] = row[5] != null ? row[5].toString() : "0";
//
//            // Kolom 5: Aksi
//            newRow[7] = "";
//
//            transformedData.add(newRow);
//        }
//
//        return transformedData;
//    }
//
//    private void renderTable(List<Object[]> data) {
//        new ModernTable(view2.getTblPesanan())
//                .setColumns(new String[]{"No", "ID", "Nama", "Meja", "Menu", "Qty", "Subtotal", "Status Pesanan", "Aksi"})
//                .hideColumn(1)
//                .configureColumn(0, ModernTable.ColumnType.NUMBER, 50)
//                .configureColumn(2, ModernTable.ColumnType.MULTI_LINE, 200)
//                .configureColumn(3, ModernTable.ColumnType.CATEGORY, 100)
//                .configureColumn(4, ModernTable.ColumnType.PRICE, 180)
//                .configureColumn(5, ModernTable.ColumnType.TEXT, 70)
//                .configureColumn(6, ModernTable.ColumnType.PRICE, 90)
//                .configureColumn(7, ModernTable.ColumnType.STOCK, 120)
//                .configureColumn(8, ModernTable.ColumnType.ACTIONS, 230)
//                .setRowHeight(70)
//                .addActionButton("Detail", new Color(59, 130, 246), this::handleDetailAction)
//                .addActionButton("Edit", new Color(251, 146, 60), this::handleEditAction)
//                .addActionButton("Hapus", new Color(239, 68, 68), this::handleDeleteAction)
//                .setData(data)
//                .render();
//    }
//
//    // ==================== HANDLER TOMBOL AKSI (Detail, Edit, Hapus) ====================
//    private void tambahPesanan() throws SQLException {
//        try {
//            List<String> meja = model.getMejaKosong();
//            List<String> menu = model.getDaftarMenu();
//
//            formPesananDialog dialog = new formPesananDialog(view2, meja, menu);
//
//            dialog.getBtnSimpan().addActionListener(e -> {
//                try {
//                    // 1. Ambil data dari form dialog
//                    String namaPelanggan = dialog.getNama(); // Mengambil input nama
//
//                    // 2. VALIDASI: Nama pelanggan tidak boleh kosong
//                    if (namaPelanggan == null || namaPelanggan.trim().isEmpty()) {
//                        showError("Nama pelanggan tidak boleh kosong!");
//                        return;
//                    }
//
//                    // 3. Ambil ID dari session
//                    Integer idPers = user_session.getIdPersonal();
//                    if (idPers == null) {
//                        showError("Sesi kosong! Silakan login kembali.");
//                        return;
//                    }
//
//                    // 4. Ambil UUID Karyawan dan Validasi
//                    java.util.UUID uuidKaryawan = model.getUUIDKaryawan(idPers);
//                    if (uuidKaryawan == null) {
//                        showError("Akun Anda belum terdaftar sebagai Karyawan aktif!");
//                        return;
//                    }
//
//                    // 5. Jika semua validasi lolos, lanjutkan proses ambil data lainnya
//                    int idMeja = Integer.parseInt(dialog.getMeja().replaceAll("[^0-9]", ""));
//                    int idMenu = Integer.parseInt(dialog.getMenu().split(" - ")[0]);
//                    String jsonItems = String.format("[{\"id_menu\": %d, \"qty\": %d, \"catatan\": \"%s\"}]",
//                            idMenu, dialog.getQty(), dialog.getCatatan());
//
//                    // 6. Eksekusi simpan
//                    model.simpanPesananBaru(namaPelanggan, idMeja, uuidKaryawan, jsonItems);
//
//                    dialog.dispose();
//                    showSuccess("Pesanan berhasil disimpan!");
//                    tampilkanPesanan();
//
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    showError("Gagal simpan: " + ex.getMessage());
//                }
//            });
//
//            dialog.setVisible(true);
//
//        } catch (SQLException e) {
//            showError("Gagal mengambil data meja/menu: " + e.getMessage());
//        }
//    }
//
//    private void handleDetailAction(int row, Object[] rowData) {
//        String id = rowData[1].toString();
//        tampilDetail(id);
//    }
//
//    private void handleEditAction(int row, Object[] rowData) {
//        // 1. Ambil ID Pesanan (UUID)
//        java.util.UUID idPesanan = (java.util.UUID) rowData[1];
//
//        try {
//            List<String> meja = model.getMejaKosong();
//            List<String> menu = model.getDaftarMenu();
//
//            formPesananDialog dialog = new formPesananDialog(view2, meja, menu);
//            dialog.setJudulDialog("Edit Pesanan");
//            dialog.setTextTombolSimpan("Update Pesanan");
//
//            // --- PROSES PARSING DATA DARI TABLE KE DIALOG ---
//            // A. Ambil Nama Pelanggan (Urutkan dari transformDataForTable)
//            // rowData[2] berisi Object[]{"", "Nama"}
//            Object[] namaArr = (Object[]) rowData[2];
//            String namaLama = namaArr[1].toString();
//            dialog.setNama(namaLama);
//
//            // B. Ambil Meja (rowData[3])
//            String mejaLama = rowData[3].toString();
//            dialog.setMeja(mejaLama);
//
//            // C. Ambil Menu (rowData[4] berisi String[]{"Nama Menu"})
//            String[] menuArr = (String[]) rowData[4];
//            String menuLama = menuArr[0];
//            dialog.setMenu(menuLama);
//
//            // D. Ambil Qty (rowData[5])
//            int qtyLama = Integer.parseInt(rowData[5].toString());
//            dialog.setQty(qtyLama);
//
//            // E. Catatan (Jika kolom catatan tidak ada di tabel, kamu perlu query ulang 
//            // atau menyimpannya di kolom tersembunyi. Untuk sekarang kita set default kosong 
//            // atau ambil dari data jika kamu menambahkannya di transformData)
//            // dialog.setCatatan(rowData[...].toString());
//            // --- EVENT SIMPAN EDIT ---
//            dialog.getBtnSimpan().addActionListener(e -> {
//                try {
//                    // Validasi Nama
//                    if (dialog.getNama().trim().isEmpty()) {
//                        showError("Nama pelanggan tidak boleh kosong!");
//                        return;
//                    }
//
//                    int idMeja = Integer.parseInt(dialog.getMeja().replaceAll("[^0-9]", ""));
//                    int idMenu = Integer.parseInt(dialog.getMenu().split(" - ")[0]);
//                    String jsonItems = String.format("[{\"id_menu\": %d, \"qty\": %d, \"catatan\": \"%s\"}]",
//                            idMenu, dialog.getQty(), dialog.getCatatan());
//
//                    model.editPesanan(idPesanan, dialog.getNama(), idMeja, jsonItems);
//
//                    dialog.dispose();
//                    showSuccess("Pesanan berhasil diperbarui!");
//                    tampilkanPesanan();
//
//                } catch (Exception ex) {
//                    showError("Gagal edit: " + ex.getMessage());
//                }
//            });
//
//            dialog.setVisible(true);
//
//        } catch (SQLException e) {
//            showError("Error: " + e.getMessage());
//        }
//    }
//
//    private void handleDeleteAction(int row, Object[] rowData) {
//        String id = rowData[1].toString();
//        int confirm = JOptionPane.showConfirmDialog(
//                view2,
//                "Yakin ingin menghapus produk?",
//                "Konfirmasi",
//                JOptionPane.YES_NO_OPTION
//        );
//
//        if (confirm == JOptionPane.YES_OPTION) {
//            try {
//                model.hapusPesanan(id);
//                showSuccess("Produk berhasil dihapus!");
//                tampilkanPesanan();
//            } catch (Exception e) {
//                showError("Gagal menghapus produk: " + e.getMessage());
//            }
//        }
//    }
//
//    private void tampilDetail(String id) {
//        JOptionPane.showMessageDialog(view2, "Detail Produk ID: " + id);
//        System.out.println("Detail produk: " + id);
//    }
//
//    private void editMenu(String id) {
//        JOptionPane.showMessageDialog(view2, "Edit Produk ID: " + id);
//        System.out.println("Edit produk: " + id);
//    }
//
//    // ==================== METHOD DIALOG PESAN ====================
//    private void showError(String message) {
//        JOptionPane.showMessageDialog(view2, message, "Error", JOptionPane.ERROR_MESSAGE);
//    }
//
//    private void showSuccess(String message) {
//        JOptionPane.showMessageDialog(view2, message, "Sukses", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    // ==================== BAGIAN UNTUK MENGATUR AKSI TOMBOL ====================
////    private class btnSidebarBeranda implements ActionListener {
////
////        @Override
////        public void actionPerformed(ActionEvent e) {
////            try {
//    ////                new c_pesanan();
////                view2.dispose();
////            } catch (SQLException ex) {
////                System.getLogger(c_pesanan.class.getName())
////                        .log(System.Logger.Level.ERROR, "Kesalahan navigasi ke Beranda", ex);
////            }
////        }
////    }
//
//    private class btnSidebarDaftarMenu implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            try {
//                new c_daftarMenu();
//                view2.dispose();
//            } catch (SQLException ex) {
//                System.getLogger(c_pesanan.class.getName())
//                        .log(System.Logger.Level.ERROR, "Kesalahan navigasi ke Pesanan", ex);
//            }
//        }
//    }
//
//    private class btnSidebarKaryawan implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            try {
//                new c_karyawan();
//                view2.dispose();
//            } catch (SQLException ex) {
//                System.getLogger(c_pesanan.class.getName())
//                        .log(System.Logger.Level.ERROR, "Kesalahan navigasi ke Pesanan", ex);
//            }
//        }
//    }
//
//    private class btnSidebarMeja implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            try {
//                new c_meja();
//                view2.dispose();
//            } catch (SQLException ex) {
//                System.getLogger(c_pesanan.class.getName())
//                        .log(System.Logger.Level.ERROR, "Kesalahan navigasi ke Pesanan", ex);
//            }
//        }
//    }
//
//    private class btnKeluar implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            int konfirmasi = JOptionPane.showConfirmDialog(
//                    view2,
//                    "Apakah kamu yakin ingin keluar dari akun ini?",
//                    "Konfirmasi Keluar",
//                    JOptionPane.YES_NO_OPTION
//            );
//
//            if (konfirmasi == JOptionPane.YES_OPTION) {
//                try {
//                    new c_user();
//                    view2.dispose();
//                } catch (Exception ex) {
//                    System.err.println("Gagal navigasi ke Login: " + ex.getMessage());
//                }
//            }
//
//        }
//
//    }
//
//}
