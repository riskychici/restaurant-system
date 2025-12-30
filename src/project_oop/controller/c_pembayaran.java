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
import project_oop.model.m_pembayaran;
import project_oop.model.m_meja;
import project_oop.view.beranda;
import project_oop.view.daftarMenu;
import project_oop.view.karyawan;
import project_oop.view.meja;
import project_oop.view.pembayaran;
import project_oop.view.pesanan;
import project_oop.view.login;
import style_table.ModernTable;
import project_oop.view.formPembayaranDialog;

/**
 * Controller untuk mengelola Daftar Menu
 */
public class c_pembayaran {

    // ==================== ATRIBUT ====================
    // Model
    private m_pembayaran model;

    // View
    private beranda view;
    private pesanan view2;
    private daftarMenu view3;
    private karyawan view4;
    private meja view5;
    private pembayaran view6;
    private login view7;

    // ==================== CONSTRUCTOR ====================
    public c_pembayaran() throws SQLException {
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
        this.model = new m_pembayaran();
        this.view = new beranda();
        this.view2 = new pesanan();
        this.view3 = new daftarMenu();
        this.view4 = new karyawan();
        this.view5 = new meja();
        this.view6 = new pembayaran();
        this.view6.setVisible(true);
    }

    private void aturEventListeners() {
        view6.getBtnCari().addActionListener(e -> tampilkanDaftarMeja());
        view6.getBtnSidebarBeranda().addActionListener(new btnSidebarBeranda());
        view6.getBtnSidebarPesanan().addActionListener(new btnSidebarPesanan());
        view6.getBtnSidebarDaftarMenu().addActionListener(new btnSidebarDaftarMenu());
        view6.getBtnSidebarKaryawan().addActionListener(new btnSidebarKaryawan());
        view6.getBtnSidebarMeja().addActionListener(new btnSidebarMeja());
        view6.getBtnKeluar().addActionListener(new btnKeluar());
    }

    // ==================== METHOD TAMPILAN ====================
    public void tampilkanDaftarMeja() {
        try {
            String search = view6.getTxtSearch();
            List<Object[]> dataFromDB = model.getPembayaran(search);
            List<Object[]> transformedData = transformDataForTable(dataFromDB);
            renderTable(transformedData);
        } catch (SQLException ex) {
            showError("Error: " + ex.getMessage());
        }
    }

    private List<Object[]> transformDataForTable(List<Object[]> dataFromDB) {
        java.text.DecimalFormatSymbols symbols = new java.text.DecimalFormatSymbols(new java.util.Locale("id", "ID"));
        symbols.setGroupingSeparator('.');
        java.text.DecimalFormat df = new java.text.DecimalFormat("###,###", symbols);

        List<Object[]> transformedData = new ArrayList<>();

        for (Object[] row : dataFromDB) {
            Object[] newRow = new Object[8];

            newRow[0] = row[0]; // ID Pelanggan
            newRow[1] = new Object[]{"", row[1] != null ? row[1].toString() : ""}; // Nama
            newRow[2] = row[2] != null ? row[2].toString() : ""; // Meja

            double totalAngka = row[3] != null ? Double.parseDouble(row[3].toString()) : 0;
            newRow[3] = new String[]{"Rp " + df.format(totalAngka)}; // Total

            newRow[4] = row[5] != null ? row[5].toString() : "-"; // Status

            newRow[5] = row[6]; // id_status_bayar
            newRow[6] = row[7]; // id_metode_bayar
            newRow[7] = "";     // Aksi

            transformedData.add(newRow);
        }
        return transformedData;
    }

    private void renderTable(List<Object[]> data) {
        ModernTable mt = new ModernTable(view6.getTblPembayaran())
                .setColumns(new String[]{"No", "ID", "Nama", "Meja", "Total Tagihan", "Status Pembayaran", "S_ID", "M_ID", "Aksi"})
                .showNumbering(true)
                .configureColumn(0, ModernTable.ColumnType.NUMBER, 50)
                .configureColumn(2, ModernTable.ColumnType.MULTI_LINE, 150)
                .configureColumn(3, ModernTable.ColumnType.TEXT, 70)
                .configureColumn(4, ModernTable.ColumnType.PRICE, 120)
                .configureColumn(5, ModernTable.ColumnType.TEXT, 120)
                .configureColumn(8, ModernTable.ColumnType.ACTIONS, 150)
                .setRowHeight(70)
                .addActionButton("Detail", new Color(59, 130, 246), this::handleDetailAction)
                .addActionButton("Edit", new Color(251, 146, 60), this::handleEditAction)
                .setData(data);

        mt.render();

        // Sembunyikan kolom ID (1), S_ID (6), dan M_ID (7)
        int[] columnsToHide = {1, 6, 7};
        for (int colIndex : columnsToHide) {
            view6.getTblPembayaran().getColumnModel().getColumn(colIndex).setMinWidth(0);
            view6.getTblPembayaran().getColumnModel().getColumn(colIndex).setMaxWidth(0);
            view6.getTblPembayaran().getColumnModel().getColumn(colIndex).setWidth(0);
            view6.getTblPembayaran().getColumnModel().getColumn(colIndex).setPreferredWidth(0);
        }
    }

    // ==================== HANDLER TOMBOL AKSI (Detail, Edit, Hapus) ====================
    private void handleDetailAction(int row, Object[] rowData) {
        try {
            int idPelanggan = Integer.parseInt(rowData[1].toString());

            String nama = (rowData[2] instanceof Object[])
                    ? ((Object[]) rowData[2])[1].toString()
                    : rowData[2].toString();

            String meja = rowData[3].toString();

            String total = (rowData[4] instanceof String[])
                    ? ((String[]) rowData[4])[0]
                    : rowData[4].toString();

            int idStatus = (rowData[6] != null) ? Integer.parseInt(rowData[6].toString()) : 1;
            int idMetode = (rowData[7] != null) ? Integer.parseInt(rowData[7].toString()) : 1;

            List<Object[]> rincian = model.detailPembayaran(idPelanggan);

            formPembayaranDialog dialog = new formPembayaranDialog(view6, model.getStatusPembayaran(), model.getMetodePembayaran(), true);
            dialog.setData(String.valueOf(idPelanggan), nama, meja, total, idStatus, idMetode);
            dialog.setTableData(rincian);
            dialog.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Gagal memuat detail pesanan: " + ex.getMessage());
        }
    }

    private void handleEditAction(int row, Object[] rowData) {
        try {
            int idPelanggan = Integer.parseInt(rowData[1].toString());
            String namaPelanggan = (rowData[2] instanceof Object[]) ? ((Object[]) rowData[2])[1].toString() : rowData[2].toString();
            String meja = rowData[3].toString();
            String totalTagihan = (rowData[4] instanceof String[]) ? ((String[]) rowData[4])[0] : rowData[4].toString();

            int currentStatusId = (rowData[6] != null) ? Integer.parseInt(rowData[6].toString()) : 1;
            int currentMetodeId = (rowData[7] != null) ? Integer.parseInt(rowData[7].toString()) : 1;

            formPembayaranDialog dialog = new formPembayaranDialog(view6, model.getStatusPembayaran(), model.getMetodePembayaran(), false);
            dialog.setData(String.valueOf(idPelanggan), namaPelanggan, meja, totalTagihan, currentStatusId, currentMetodeId);

            while (true) {
                dialog.setVisible(true);
                if (!dialog.isSaveClicked()) {
                    break;
                }

                int selectedStatus = dialog.getSelectedIdStatus();
                int selectedMetode = dialog.getSelectedIdMetode();
                boolean validasiLolos = true;

                if (selectedStatus == 1) {
                    if (selectedMetode == 1) {
                        showError("Transaksi LUNAS wajib menentukan Metode Pembayaran!");
                        validasiLolos = false;
                    }
                } else if (selectedStatus == 2) {
                    selectedMetode = 1;
                }

                if (!validasiLolos) {
                    continue;
                }

                String hasil = model.updatePembayaran(idPelanggan, selectedStatus, selectedMetode);

                if (hasil != null && hasil.equalsIgnoreCase("Success")) {
                    showSuccess("Pembayaran Berhasil Diperbarui!");

                    tampilkanDaftarMeja();

                    break;
                } else {
                    showError("Gagal simpan: " + hasil);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Sistem Error: " + ex.getMessage());
        }
    }

    private void tampilDetail(String id) {

    }

    private void editMenu(String id) {
        JOptionPane.showMessageDialog(view6, "Edit Produk ID: " + id);
        System.out.println("Edit produk: " + id);
    }

    // ==================== METHOD DIALOG PESAN ====================
    private void showError(String message) {
        JOptionPane.showMessageDialog(view6, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(view6, message, "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== BAGIAN UNTUK MENGATUR AKSI TOMBOL ====================
    private class btnSidebarBeranda implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_beranda();
                view6.dispose();
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
                view6.dispose();
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
                view6.dispose();
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
                view6.dispose();
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
                view6.dispose();
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
                    view6,
                    "Apakah kamu yakin ingin keluar dari akun ini?",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION
            );

            if (konfirmasi == JOptionPane.YES_OPTION) {
                try {
                    new c_user();
                    view6.dispose();
                } catch (Exception ex) {
                    System.err.println("Gagal navigasi ke Login: " + ex.getMessage());
                }
            }

        }

    }
}
