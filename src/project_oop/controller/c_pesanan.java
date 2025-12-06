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
import project_oop.view.pesanan;
import project_oop.view.daftarMenu;
import style_table.ModernTable;

public class c_pesanan {

    private m_pesanan model;
    private pesanan view;
    private daftarMenu view2;
    
        // Sidebar Daftar Menu
    private class btnSidebarDaftarMenu implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new c_daftarMenu();
                view.dispose();
            } catch (SQLException ex) {
                System.getLogger(c_pesanan.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }

    }

    public c_pesanan() throws SQLException {
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

        this.model = new m_pesanan();
        this.view = new pesanan();
        this.view2 = new daftarMenu();
        this.view.setVisible(true);
            view.getBtnSidebarDaftarMenu().addActionListener(new btnSidebarDaftarMenu());
            
            tampilkanDaftarPesanan();
    }

    
    
    public void tampilkanDaftarPesanan() {
        try {
            String search = view.getTxtSearch().getText().trim();
            List<Object[]> dataFromDB = model.getPesanan(search);

            // Transform data ke format yang dibutuhkan
            List<Object[]> transformedData = new ArrayList<>();
            for (Object[] row : dataFromDB) {
                Object[] newRow = new Object[6];

                // Kolom 0: ID
                newRow[0] = row[0];

                // Kolom 1: Produk (multi-line) → kategori, nama, status
                // Sekarang di ubah hanya menampilkan nama menu saja
                String nama = row[1] != null ? row[1].toString() : "";
                newRow[1] = new Object[]{"", nama};
                
                // Kolom 2: Kategori
                String kategori = row[2] != null ? row[2].toString() : "";
                newRow[2] = kategori;
                
                // Kolom 3: Harga (pakai satu harga saja)
                String harga = row[3] != null ? row[3].toString() : "";
                newRow[3] = new String[]{harga};

                // Kolom 4: Stok
                newRow[4] = row[4] != null ? row[4].toString() : "0";

                // Kolom 5: Aksi
                newRow[5] = "";

                transformedData.add(newRow);
            }

            // Setup & render tabel
            new ModernTable(view.getTblPesanan())
                    .setColumns(new String[]{"No", "ID", "Nama", "Meja", "Menu", "Status Pesanan", "Aksi"})
                    .hideColumn(1) // Hide kolom ID
                    .configureColumn(0, ModernTable.ColumnType.NUMBER, 50)
                    .configureColumn(2, ModernTable.ColumnType.MULTI_LINE, 300)
                    .configureColumn(3, ModernTable.ColumnType.CATEGORY, 120)
                    .configureColumn(4, ModernTable.ColumnType.PRICE, 180)
                    .configureColumn(5, ModernTable.ColumnType.STOCK, 90)
                    .configureColumn(6, ModernTable.ColumnType.ACTIONS, 230)
                    .setRowHeight(70)
                    .addActionButton("Detail", new Color(59, 130, 246), (row, rowData) -> {
                        String id = rowData[1].toString();
                        tampilDetail(id);
                    })
                    .addActionButton("Edit", new Color(251, 146, 60), (row, rowData) -> {
                        String id = rowData[1].toString();
                        editMenu(id);
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
                            tampilkanDaftarPesanan(); // Refresh
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

    private void editMenu(String id) {
        // Logic edit produk
        JOptionPane.showMessageDialog(view, "Edit Produk ID: " + id);
        System.out.println("Edit produk: " + id);
    }
    
}
