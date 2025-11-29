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
import project_oop.view.daftarMenu;
import project_oop.view.pesanan;
import style_table.ModernTable;

public class c_daftarMenu {

    private m_daftarMenu model;
    private daftarMenu view;
    private pesanan view2;

    public c_daftarMenu() throws SQLException {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.selectionBackground", new Color(220, 235, 252));
            UIManager.put("Table.selectionForeground", Color.BLACK);
            UIManager.put("Table.alternateRowColor", new Color(250, 250, 250));
        } catch (Exception e) {
            System.err.println("FlatLaf gagal di-load: " + e.getMessage());
        }

        this.model = new m_daftarMenu();
        this.view = new daftarMenu();
        this.view2 = new pesanan();
        this.view.setVisible(true);

        // Tambah listener tombol cari
        this.view.getBtnCari().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tampilkanProduk();
            }
        });

        // Load awal
        tampilkanProduk();

        try {
            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Gagal load FlatLaf: " + ex.getMessage());
        }
        view.btn_sidebar_pesanan().addActionListener(new pesanan_listener());
    }

    public void tampilkanProduk() {
        try {
            String search = view.getTxtSearch().getText().trim();
            List<Object[]> dataFromDB = model.getDaftarProduk(search);

            // Transform data ke format yang dibutuhkan
            List<Object[]> transformedData = new ArrayList<>();
            for (Object[] row : dataFromDB) {
                Object[] newRow = new Object[5];

                // Kolom 0: ID
                newRow[0] = row[0];

                // Kolom 1: Produk (multi-line) → kategori, nama, status
                String nama = row[1] != null ? row[1].toString() : "";
                String kategori = row[2] != null ? row[2].toString() : "";
                String status = row[5] != null ? row[5].toString() : "";
                newRow[1] = new Object[]{kategori, nama, status};
                
                // Kolom 2: Harga (pakai satu harga saja)
                String harga = row[3] != null ? row[3].toString() : "";
                newRow[2] = new String[]{harga};

                // Kolom 3: Stok
                newRow[3] = row[4] != null ? row[4].toString() : "0";

                // Kolom 4: Aksi
                newRow[4] = "";

                transformedData.add(newRow);
            }

            // Setup & render tabel
            new ModernTable(view.getTblMenu())
                    .setColumns(new String[]{"No", "ID", "Menu", "Harga", "Stok", "Aksi"})
                    .hideColumn(1) // Hide kolom ID
                    .configureColumn(0, ModernTable.ColumnType.NUMBER, 50)
                    .configureColumn(2, ModernTable.ColumnType.MULTI_LINE, 380)
                    .configureColumn(3, ModernTable.ColumnType.PRICE, 180)
                    .configureColumn(4, ModernTable.ColumnType.STOCK, 90)
                    .configureColumn(5, ModernTable.ColumnType.ACTIONS, 230)
                    .setRowHeight(70)
                    .addActionButton("Detail", new Color(59, 130, 246), (row, rowData) -> {
                        String id = rowData[1].toString();
                        tampilDetail(id);
                    })
                    .addActionButton("Edit", new Color(251, 146, 60), (row, rowData) -> {
                        String id = rowData[1].toString();
                        editProduk(id);
                    })
                    .addActionButton("Hapus", new Color(239, 68, 68), (row, rowData) -> {
                        String id = rowData[1].toString();
                        int confirm = JOptionPane.showConfirmDialog(view,
                                "Yakin ingin menghapus produk?",
                                "Konfirmasi",
                                JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            model.hapusProduk(id);
                            JOptionPane.showMessageDialog(view, "Produk berhasil dihapus!");
                            tampilkanProduk(); // Refresh
                        }
                    })
                    .setData(transformedData)
                    .render();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
        }
    }

    private void tampilDetail(String id) {
        // Logic detail produk
        JOptionPane.showMessageDialog(view, "Detail Produk ID: " + id);
        System.out.println("Detail produk: " + id);
    }

    private void editProduk(String id) {
        // Logic edit produk
        JOptionPane.showMessageDialog(view, "Edit Produk ID: " + id);
        System.out.println("Edit produk: " + id);
    }

    private class pesanan_listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_pesanan();
                view.dispose();
            } catch (SQLException ex) {
                System.getLogger(c_pesanan.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }

    }

    

}
