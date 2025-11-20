/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_oop.controller;

import project_oop.model.m_produk;
import project_oop.view.main_view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.ui.FlatBorder;
import java.awt.BorderLayout;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import project_oop.view.kategori_produk;
import style_table.ModernTable;

public class c_kategori_produk {

    private final m_produk model;
    private final kategori_produk view;

    public c_kategori_produk() throws SQLException {
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

        this.model = new m_produk();
        this.view = new kategori_produk();
        this.view.setVisible(true);

        // Tambah listener tombol cari
        this.view.getBtnCari().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tampilkanKategoriProduk();
            }
        });

        // Load awal
        tampilkanKategoriProduk();

        try {
            FlatLightLaf.setup();
        } catch (Exception ex) {
            System.err.println("Gagal load FlatLaf: " + ex.getMessage());
        }
    }

    public void tampilkanKategoriProduk() {
        try {
            String search = view.getTxtSearch().getText().trim();
            List<Object[]> dataFromDB = model.getDaftarKategoriProduk(search, 50, 0);

            // Transform data ke format yang dibutuhkan
            List<Object[]> transformedData = new ArrayList<>();
            for (Object[] row : dataFromDB) {
                Object[] newRow = new Object[6]; // 5 kolom (tanpa No, akan otomatis)

                // Kolom 0: ID (akan di-hide)
                newRow[0] = row[0];

                // Kolom 1: Produk (multi-line)
                String kategori = row[1] != null ? row[1].toString() : "";

                newRow[1] = kategori;

                // Kolom 2: KETERANGAN
                String keterangan = row[2] != null ? row[2].toString() : "";
                newRow[2] = keterangan;

                // Kolom 3: Stok
                String tgl = row[4] != null ? row[4].toString() : "";
                newRow[3] = tgl;

                // Kolom 4: Aksi (placeholder)
                newRow[5] = "";

                transformedData.add(newRow);
            }

            // Setup & render tabel
            new ModernTable(view.getTblKategoriProduk())
                    .setColumns(new String[]{"No", "ID", "Kategori", "Keterangan", "Tgl Created", "Aksi"})
                    .hideColumn(1) // Hide kolom ID
                    .configureColumn(0, ModernTable.ColumnType.NUMBER, 50)
                    .configureColumn(2, ModernTable.ColumnType.TEXT, 150)
                    .configureColumn(3, ModernTable.ColumnType.TEXT, 380)
                    .configureColumn(4, ModernTable.ColumnType.TEXT, 300)
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
                                "Yakin ingin menghapus kategori?",
                                "Konfirmasi",
                                JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            model.hapusKategoriProduk(id);
                            JOptionPane.showMessageDialog(view, "Kategori berhasil dihapus!");
                            tampilkanKategoriProduk(); // Refresh
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
        JOptionPane.showMessageDialog(view, "Detail Kategori ID: " + id);
        System.out.println("Detail produk: " + id);
    }

    private void editProduk(String id) {
        // Logic edit produk
        JOptionPane.showMessageDialog(view, "Edit Kategori ID: " + id);
        System.out.println("Edit produk: " + id);
    }

}
