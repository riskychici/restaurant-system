package project_oop.model;

import koneksi.koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class m_daftarMenu {

    private final koneksi koneksi;

    public m_daftarMenu() throws SQLException {
        this.koneksi = new koneksi();
    }

    public List<Object[]> getDaftarMenu(String search) throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT * FROM public.daftar_menu(?)";

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, search);

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

    // Pilih kategori menu
    public List<String> ambilKategori() throws SQLException {
        List<String> data = new ArrayList<>();
        String sql = "SELECT * FROM public.pilih_kategori()";

        try (PreparedStatement ps = koneksi.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                data.add(rs.getString("kategori_info"));
            }
        } catch (SQLException e) {
            throw new SQLException("Gagal mengambil data kategori: " + e.getMessage());
        }
        return data;
    }

    // Tambah Menu
    public String tambahMenu(String nama, int idKategori, double harga, int stok) throws SQLException {
        String sql = "SELECT public.tambah_menu(?::character varying, ?::integer, ?::numeric, ?::integer)";

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setInt(2, idKategori);
            ps.setDouble(3, harga);
            ps.setInt(4, stok);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Gagal mengeksekusi fungsi tambah_menu: " + e.getMessage());
        }
        return "GAGAL: Database tidak memberikan respon.";
    }

    //Edit Menu
    public String updateMenu(int id, String nama, int idKategori, double harga, int stok) throws SQLException {
        String sql = "SELECT public.update_menu(?::integer, ?::character varying, ?::integer, ?::numeric, ?::integer)";

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, nama);
            ps.setInt(3, idKategori);
            ps.setDouble(4, harga);
            ps.setInt(5, stok);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return "GAGAL: Tidak ada respon dari database.";
    }

    public String hapusMenu(int idMenu) throws SQLException {
        String sql = "SELECT public.hapus_menu(?)";
        try (PreparedStatement ps = koneksi.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idMenu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return "Gagal menghapus data";
    }
}
