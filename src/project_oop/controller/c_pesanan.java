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
        this.view5 = new meja();
        this.view6 = new pembayaran();
        this.view2.setVisible(true);
    }

    private void aturEventListeners() {
        view2.getBtnCari().addActionListener(e -> tampilkanPesanan());
        view2.getBtnSidebarBeranda().addActionListener(new btnSidebarBeranda());
        view2.getBtnSidebarDaftarMenu().addActionListener(new btnSidebarDaftarMenu());
        view2.getBtnSidebarKaryawan().addActionListener(new btnSidebarKaryawan());
        view2.getBtnSidebarMeja().addActionListener(new btnSidebarMeja());
        view2.getBtnSidebarPembayaran().addActionListener(new btnSidebarPembayaran());
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
                double hargaBersih = Double.parseDouble(hargaStr.replaceAll("[^0-9.]", ""));
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

        mt.render();

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
            List<String> meja = model.getMejaKosong();
            List<String> menu = model.getDaftarMenu();
            List<String> pelangganAktif = model.getPelangganAktif();

            formPesananDialog dialog = new formPesananDialog(view2, meja, menu, pelangganAktif);

            dialog.getBtnSimpan().addActionListener(e -> {
                try {
                    String namaPelanggan = dialog.getNama().trim();
                    if (namaPelanggan.isEmpty()) {
                        showError("Nama pelanggan tidak boleh kosong!");
                        return;
                    }

                    String labelMenu = dialog.getMenu();
                    if (labelMenu.contains("[STOK HABIS]")) {
                        showError("Menu ini tidak bisa dipesan karena stok sedang kosong!");
                        return;
                    }

                    int stokTersedia = Integer.parseInt(
                            labelMenu.substring(labelMenu.lastIndexOf(": ") + 2, labelMenu.lastIndexOf("]"))
                    );
                    int qtyDipesan = dialog.getQty();

                    if (qtyDipesan > stokTersedia) {
                        showError("Stok tidak mencukupi! Tersedia: " + stokTersedia);
                        return;
                    }

                    int idMenu = model.getIdMenuByNama(labelMenu);
                    int idMejaBenar = model.getIdMejaByNomor(dialog.getMeja());

                    Integer idPers = user_session.getIdPersonal();
                    java.util.UUID uuidKaryawan = model.getUUIDKaryawan(idPers);

                    String catatanRaw = dialog.getCatatan() != null ? dialog.getCatatan() : "";
                    String catatanAman = catatanRaw.replace("\\", "\\\\")
                            .replace("\"", "\\\"")
                            .replace("\n", "\\n");

                    String jsonItems = String.format("[{\"id_menu\": %d, \"qty\": %d, \"catatan\": \"%s\"}]",
                            idMenu, qtyDipesan, catatanAman);

                    model.simpanPesananBaru(namaPelanggan, idMejaBenar, uuidKaryawan, jsonItems);

                    dialog.dispose();
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        showSuccess("Pesanan berhasil disimpan!");
                        tampilkanPesanan();
                    });
                } catch (Exception ex) {
                    showError("Gagal simpan: " + ex.getMessage());
                }
            });

            dialog.setVisible(true);
        } catch (Exception ex) {
            showError("Gagal membuka form: " + ex.getMessage());
        }
    }

    private void handleDetailAction(int row, Object[] rowData) {
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

            formPesananDialog dialog = new formPesananDialog(view2, listMejaFinal, model.getDaftarMenu(), model.getPelangganAktif());

            dialog.setDaftarStatus(model.getDaftarStatus());
            dialog.setDetailMode();

            Object[] namaArr = (Object[]) rowData[2];
            dialog.setNama(namaArr[1].toString());
            dialog.setMeja(listMejaFinal.get(0));

            String[] menuArr = (String[]) rowData[4];
            dialog.setMenu(menuArr[0]);
            dialog.setQty(Integer.parseInt(rowData[5].toString()));
            dialog.setStatusTerpilih(rowData[7].toString());
            dialog.setCatatan(model.getCatatanById(idPesanan));

            dialog.setVisible(true);
        } catch (Exception ex) {
            showError("Gagal detail: " + ex.getMessage());
        }
    }

    private void handleEditAction(int row, Object[] rowData) {
        try {
            java.util.UUID idPesanan = (java.util.UUID) rowData[1];
            String mejaAktif = rowData[3].toString();

            int qtyAwal = Integer.parseInt(rowData[5].toString());

            List<String> listMejaRaw = model.getMejaKosong();
            List<String> listMejaFinal = new java.util.ArrayList<>();
            String targetMejaFull = "";
            for (String m : listMejaRaw) {
                if (m.startsWith(mejaAktif)) {
                    targetMejaFull = m;
                    break;
                }
            }

            if (!targetMejaFull.isEmpty()) {
                listMejaFinal.add(targetMejaFull);
                for (String m : listMejaRaw) {
                    if (!m.equals(targetMejaFull)) {
                        listMejaFinal.add(m);
                    }
                }
            } else {
                listMejaFinal.add(mejaAktif);
                listMejaFinal.addAll(listMejaRaw);
            }

            formPesananDialog dialog = new formPesananDialog(view2, listMejaFinal, model.getDaftarMenu(), model.getPelangganAktif());
            dialog.setStatusVisible(true);
            dialog.setDaftarStatus(model.getDaftarStatus());

            Object[] namaArr = (Object[]) rowData[2];
            dialog.setNama(namaArr[1].toString());
            dialog.setMeja(mejaAktif);

            String[] menuArr = (String[]) rowData[4];
            dialog.setMenu(menuArr[0]);
            dialog.setQty(qtyAwal);
            dialog.setStatusTerpilih(rowData[7].toString());
            dialog.setCatatan(model.getCatatanById(idPesanan));

            for (java.awt.event.ActionListener al : dialog.getBtnSimpan().getActionListeners()) {
                dialog.getBtnSimpan().removeActionListener(al);
            }

            dialog.getBtnSimpan().setText("Simpan");
            dialog.getBtnSimpan().addActionListener(e -> {
                try {
                    String labelMenu = dialog.getMenu();
                    int qtyBaru = dialog.getQty();

                    if (labelMenu.contains("[STOK HABIS]")) {
                        if (qtyBaru > qtyAwal) {
                            showError("Stok sedang habis! Anda hanya bisa mengurangi atau tetap pada jumlah sebelumnya.");
                            return;
                        }
                    } else {
                        int stokGudang = Integer.parseInt(
                                labelMenu.substring(labelMenu.lastIndexOf(": ") + 2, labelMenu.lastIndexOf("]"))
                        );

                        if ((qtyBaru - qtyAwal) > stokGudang) {
                            showError("Stok tidak mencukupi untuk penambahan! Sisa stok gudang: " + stokGudang);
                            return;
                        }
                    }

                    int idMenu = model.getIdMenuByNama(labelMenu);
                    int idMejaBenar = model.getIdMejaByNomor(dialog.getMeja());

                    String catatanRaw = dialog.getCatatan() != null ? dialog.getCatatan() : "";
                    String catatanAman = catatanRaw.replace("\\", "\\\\")
                            .replace("\"", "\\\"")
                            .replace("\n", "\\n");

                    String jsonItems = String.format("[{\"id_menu\": %d, \"qty\": %d, \"catatan\": \"%s\"}]",
                            idMenu, qtyBaru, catatanAman);

                    model.editPesanan(idPesanan, dialog.getNama(), idMejaBenar, jsonItems, dialog.getStatus());

                    dialog.dispose();
                    showSuccess("Pesanan diperbarui!");
                    tampilkanPesanan();
                } catch (Exception ex) {
                    showError("Update gagal: " + ex.getMessage());
                }
            });

            dialog.setVisible(true);
        } catch (Exception ex) {
            showError("Gagal edit: " + ex.getMessage());
        }
    }

    private void handleDeleteAction(int row, Object[] rowData) {
        String idPesanan = rowData[1].toString();

        Object[] namaData = (Object[]) rowData[2];
        String namaPelanggan = namaData[1].toString();

        int confirm = JOptionPane.showConfirmDialog(
                view2,
                "Apakah Anda yakin ingin menghapus pesanan atas nama: " + namaPelanggan + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                model.hapusPesanan(idPesanan);

                showSuccess("Pesanan milik " + namaPelanggan + " berhasil dihapus!");
                tampilkanPesanan();
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
    private class btnSidebarBeranda implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_beranda();
                view2.dispose();
            } catch (SQLException ex) {
                System.getLogger(c_pesanan.class.getName())
                        .log(System.Logger.Level.ERROR, "Kesalahan navigasi ke Beranda", ex);
            }
        }
    }

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

    private class btnSidebarPembayaran implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_pembayaran();
                view2.dispose();
            } catch (SQLException ex) {
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
