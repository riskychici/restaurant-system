/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_oop.model;

import koneksi.koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class m_produk {

    private final koneksi koneksi;

    public m_produk() throws SQLException {
        this.koneksi = new koneksi();
    }

    public List<Object[]> getDaftarProduk(String search, int limit, int offset) throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT * FROM public.daftar_produk(?, ?, ?)";

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, search);
            ps.setInt(2, limit);
            ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    data.add(row);
                }
            }
        }

        return data;
    }

    public void hapusProduk(String id) {
        // Logic edit produk
        System.out.println("Hapus produk: " + id);
    }
    public void hapusKategoriProduk(String id) {
        // Logic edit produk
        System.out.println("Hapus produk: " + id);
    }
    
    public List<Object[]> getDaftarKategoriProduk(String search, int limit, int offset) throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT * FROM public.daftar_kategori_produk(?, ?, ?)";

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, search);
            ps.setInt(2, limit);
            ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    data.add(row);
                }
            }
        }

        return data;
    }

}
