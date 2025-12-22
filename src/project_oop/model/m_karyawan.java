package project_oop.model;

import koneksi.koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class m_karyawan {

    private final koneksi koneksi;

    public m_karyawan() throws SQLException {
        this.koneksi = new koneksi();
    }

    public List<Object[]> getKaryawan(String search) throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT * FROM public.karyawan(?)";

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

    // Ambil Role
    public List<String> ambilRoles() throws SQLException {
        List<String> roles = new ArrayList<>();
        try (PreparedStatement ps = koneksi.prepareStatement("SELECT * FROM public.pilih_role()"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                roles.add(rs.getString(1));
            }
        }
        return roles;
    }

    // Tambah Karyawan
    public String tambahKaryawan(String nama, String noTelp, int idRole) throws SQLException {
        String hasil = "";
        String sql = "SELECT public.tambah_karyawan(?, ?, ?)";

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setString(2, noTelp);
            ps.setInt(3, idRole);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    hasil = rs.getString(1);
                }
            }
        }
        return hasil;
    }

    // Edit Karyawan
    public String updateKaryawan(String id, String nama, String noTelp, int idRole) throws SQLException {
        String sql = "SELECT public.update_karyawan(?::uuid, ?, ?, ?)";
        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, nama);
            ps.setString(3, noTelp);
            ps.setInt(4, idRole);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : "GAGAL: Error sistem";
            }
        }
    }

    // Hapus Karyawan
    public String hapusKaryawan(String id) throws SQLException {
        String sql = "SELECT public.hapus_karyawan(?::uuid)";
        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : "GAGAL: Error sistem";
            }
        }
    }
}
