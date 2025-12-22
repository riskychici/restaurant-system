package project_oop.model;

import koneksi.koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class m_meja {

    private final koneksi koneksi;

    public m_meja() throws SQLException {
        this.koneksi = new koneksi();
    }

    public List<Object[]> getDaftarMeja(String search) throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT * FROM public.meja(?)";

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

    //Detail Meja
    public Object[] getDetailMeja(int idMeja) throws SQLException {
        Object[] data = null;
        String sql = "SELECT * FROM public.get_detail_meja(?)";

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setInt(1, idMeja);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    data = new Object[6];
                    data[0] = rs.getInt("id_meja");
                    data[1] = rs.getString("nomor_meja");
                    data[2] = rs.getInt("kapasitas");
                    data[3] = rs.getString("status_meja");
                    data[4] = rs.getString("nama_pelanggan");
                    data[5] = rs.getString("waktu_masuk");
                }
            }
        }
        return data;
    }

    // Kosongkan Meja
    public String konfirmasiMejaKosong(int idMeja) throws SQLException {
        String hasil = "";
        // Memanggil fungsi SQL yang tadi kita buat
        String sql = "SELECT public.konfirmasi_meja_kosong(?)";

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setInt(1, idMeja);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hasil = rs.getString(1);
                }
            }
        }
        return hasil;
    }

    // Tambah Meja
    public String tambahMeja(String nomor, int kapasitas) throws SQLException {
        String hasil = "";
        String sql = "SELECT public.tambah_meja(?, ?)";

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, nomor);
            ps.setInt(2, kapasitas);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hasil = rs.getString(1);
                }
            }
        }
        return hasil;
    }

    // Edit Meja
    public String updateMeja(int idMeja, String nomor, int kapasitas) throws SQLException {
        String hasil = "";
        String sql = "SELECT public.update_meja(?, ?, ?)";

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setInt(1, idMeja);
            ps.setString(2, nomor);
            ps.setInt(3, kapasitas);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hasil = rs.getString(1);
                }
            }
        }
        return hasil;
    }

    // Hapus Meja
    public String hapusMeja(int idMeja) throws SQLException {
        String hasil = "";
        String sql = "SELECT public.hapus_meja(?)";

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setInt(1, idMeja);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hasil = rs.getString(1);
                }
            }
        }
        return hasil;
    }

}
