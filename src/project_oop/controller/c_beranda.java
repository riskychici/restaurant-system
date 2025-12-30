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
import project_oop.view.beranda;
import project_oop.view.daftarMenu;
import project_oop.view.karyawan;
import project_oop.view.meja;
import project_oop.view.pembayaran;
import project_oop.view.pesanan;
import project_oop.view.login;
import style_table.ModernTable;
import project_oop.view.formMenuDialog;

/**
 * Controller untuk mengelola Daftar Menu
 */
public class c_beranda {

    // ==================== ATRIBUT ====================
    private m_daftarMenu model;
    private beranda view;
    private pesanan view2;
    private daftarMenu view3;
    private karyawan view4;
    private meja view5;
    private pembayaran view6;
    private login view7;

    // ==================== CONSTRUCTOR ====================
    public c_beranda() throws SQLException {
        inisialisasiTemaDanTabel();
        inisialisasiKomponen();
        aturEventListeners();
//        tampilkanDaftarMenu();
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
        this.model = new m_daftarMenu();
        this.view = new beranda();
        this.view2 = new pesanan();
        this.view3 = new daftarMenu();
        this.view4 = new karyawan();
        this.view.setVisible(true);
    }

    private void aturEventListeners() {
//        view.getBtnCari().addActionListener(e -> tampilkanDaftarMenu());
        view.getBtnSidebarPesanan().addActionListener(new btnSidebarPesanan());
        view.getBtnSidebarDaftarMenu().addActionListener(new btnSidebarDaftarMenu());
        view.getBtnSidebarKaryawan().addActionListener(new btnSidebarKaryawan());
        view.getBtnSidebarMeja().addActionListener(new btnSidebarMeja());
        view.getBtnSidebarPembayaran().addActionListener(new btnSidebarPembayaran());
        view.getBtnKeluar().addActionListener(new btnKeluar());
    }

    // ==================== METHOD TAMPILAN ====================
//    public void tampilkanDaftarMenu() {
//        try {
//            String search = view.getTxtSearch();
//            List<Object[]> dataFromDB = model.getDaftarMenu(search);
    ////            List<Object[]> transformedData = transformDataForTable(dataFromDB);
////            renderTable(transformedData);
//        } catch (SQLException ex) {
//            showError("Error: " + ex.getMessage());
//        }
//    }

//    private List<Object[]> transformDataForTable(List<Object[]> dataFromDB) {
//        java.text.DecimalFormatSymbols symbols = new java.text.DecimalFormatSymbols(new java.util.Locale("id", "ID"));
//        symbols.setGroupingSeparator('.');
//        java.text.DecimalFormat df = new java.text.DecimalFormat("###,###", symbols);
//        List<Object[]> transformedData = new ArrayList<>();
//        for (Object[] row : dataFromDB) {
//            Object[] newRow = new Object[6];
//            newRow[0] = row[0];
//            String nama = row[1] != null ? row[1].toString() : "";
//            newRow[1] = new Object[]{"", nama};
//            newRow[2] = row[2] != null ? row[2].toString() : "";
//            String hargaStr = row[3] != null ? row[3].toString() : "0";
//            try {
//                double hargaBersih = Double.parseDouble(hargaStr.replaceAll("[^0-9]", ""));
//                newRow[3] = new String[]{"Rp " + df.format(hargaBersih)};
//            } catch (Exception e) {
//                newRow[3] = new String[]{"Rp " + hargaStr};
//            }
//            newRow[4] = row[4] != null ? row[4].toString() : "0";
//            newRow[5] = "";
//            transformedData.add(newRow);
//        }
//        return transformedData;
//    }

//    private void renderTable(List<Object[]> data) {
//        new ModernTable(view3.getTblMenu())
//                .setColumns(new String[]{"No", "ID", "Menu", "Kategori", "Harga", "Stok", "Aksi"})
//                .hideColumn(1)
//                .configureColumn(0, ModernTable.ColumnType.NUMBER, 50)
//                .configureColumn(2, ModernTable.ColumnType.MULTI_LINE, 300)
//                .configureColumn(3, ModernTable.ColumnType.CATEGORY, 120)
//                .configureColumn(4, ModernTable.ColumnType.PRICE, 180)
//                .configureColumn(5, ModernTable.ColumnType.STOCK, 90)
//                .configureColumn(6, ModernTable.ColumnType.ACTIONS, 230)
//                .setRowHeight(70)
//                .addActionButton("Detail", new Color(59, 130, 246), this::handleDetailAction)
//                .addActionButton("Edit", new Color(251, 146, 60), this::handleEditAction)
//                .addActionButton("Hapus", new Color(239, 68, 68), this::handleDeleteAction)
//                .setData(data)
//                .render();
//    }

    // ==================== HANDLER TOMBOL AKSI ====================

    
    // ==================== DIALOG PESAN ====================
    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(view, message, "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== NAVIGASI SIDEBAR ====================
    private class btnSidebarPesanan implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_pesanan();
                view.dispose();
            } catch (SQLException ex) {
            }
        }
    }

    private class btnSidebarDaftarMenu implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_daftarMenu();
                view.dispose();
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
                view.dispose();
            } catch (SQLException ex) {
            }
        }
    }

    private class btnSidebarMeja implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_meja();
                view.dispose();
            } catch (SQLException ex) {
            }
        }
    }

    private class btnSidebarPembayaran implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_pembayaran();
                view.dispose();
            } catch (SQLException ex) {
            }
        }
    }

    private class btnKeluar implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int konfirmasi = JOptionPane.showConfirmDialog(view, "Keluar dari akun?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                try {
                    new c_user();
                    view.dispose();
                } catch (Exception ex) {
                }
            }
        }
    }
}
