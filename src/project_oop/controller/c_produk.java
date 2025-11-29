/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_oop.controller;

import project_oop.model.m_produk;
import project_oop.view.daftarMenu;

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
import style_table.ModernTable;
import project_oop.view.pesanan;

public class c_produk {

    private m_produk model;
    private daftarMenu view;
    private pesanan view2;

    public c_produk() throws SQLException {
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
                    .setColumns(new String[]{"No", "ID", "Produk", "Harga", "Stok", "Aksi"})
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
