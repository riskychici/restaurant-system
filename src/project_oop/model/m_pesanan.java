package project_oop.model;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import koneksi.koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class m_pesanan {

    private final koneksi koneksi;

    public m_pesanan() throws SQLException {
        this.koneksi = new koneksi();
    }

    /**
     * Helper untuk mendapatkan koneksi aktif tanpa menutupnya secara tidak
     * sengaja
     */
    private Connection getConnection() throws SQLException {
        return koneksi.getConnection();
    }

    public List<Object[]> getPesanan(String search) throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT * FROM public.pesanan(?)";

        // PERBAIKAN: Jangan masukkan 'koneksi' ke dalam try(...)
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
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

    public List<String> getMejaKosong() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT nomor_meja, status_meja FROM public.meja ORDER BY nomor_meja ASC";

        // PERBAIKAN: Hanya st dan rs yang ditutup otomatis
        try (Statement st = getConnection().createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(rs.getString("nomor_meja") + " - " + rs.getString("status_meja"));
            }
        }
        return list;
    }

    public List<String> getDaftarMenu() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT id_menu, nama_menu, harga FROM v_menu_aktif ORDER BY nama_menu ASC";

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("###,###", symbols);

        try (Statement st = getConnection().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id_menu");
                String nama = rs.getString("nama_menu");
                double harga = rs.getDouble("harga");
                String hargaFormatted = df.format(harga);

                list.add(id + " - " + nama + " (Rp " + hargaFormatted + ")");
            }
        }
        return list;
    }

    public java.util.UUID getUUIDKaryawan(int idPersonal) throws SQLException {
        String sql = "SELECT id_karyawan FROM karyawan WHERE id_personal = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, idPersonal);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return (java.util.UUID) rs.getObject("id_karyawan");
                }
            }
        }
        return null;
    }

    public int getIdMenuByNama(String namaMenuFull) {
        // Input: "Ayam Bakar Madu (Rp 27.000)"
        // Kita ambil hanya "Ayam Bakar Madu" dengan membuang bagian harga
        String namaSaja = namaMenuFull.split(" \\(")[0].trim();

        // Sesuaikan nama tabel menjadi daftar_menu
        String sql = "SELECT id_menu FROM public.daftar_menu WHERE LOWER(nama_menu) = LOWER(?)";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, namaSaja);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_menu");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saat mencari ID Menu di tabel daftar_menu: " + e.getMessage());
            e.printStackTrace();
        }
        return -1; // Mengembalikan -1 jika menu tidak ditemukan
    }

    public void simpanPesananBaru(String nama, int idMeja, java.util.UUID idKaryawan, String jsonItems) throws SQLException {
        String sql = "SELECT buat_pesanan_baru(?, ?, ?, ?::jsonb)";

        // PERBAIKAN: PreparedStatement menggunakan koneksi yang ada, tanpa menutup koneksinya
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setInt(2, idMeja);
            ps.setObject(3, idKaryawan);
            ps.setString(4, jsonItems);
            ps.execute();
        }
    }

    public void editPesanan(java.util.UUID idPesanan, String namaPelanggan, int idMeja, String jsonItems, String status) throws SQLException {
        String sql = "SELECT public.edit_pesanan(?, ?, ?, ?::jsonb, ?)";

        // PERBAIKAN: Hapus 'Connection conn' dari dalam try(...)
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setObject(1, idPesanan);
            ps.setString(2, namaPelanggan);
            ps.setInt(3, idMeja);
            ps.setString(4, jsonItems);
            ps.setString(5, status);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String result = rs.getString(1);
                    if (result != null && result.startsWith("Error")) {
                        throw new SQLException(result);
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Gagal menjalankan prosedur edit: " + e.getMessage());
        }
    }

    public String getCatatanById(java.util.UUID idPesanan) throws SQLException {
        String catatan = "";
        String sql = "SELECT catatan FROM detail_pesanan WHERE id_pesanan = ? LIMIT 1";

        // PERBAIKAN: Hapus 'Connection conn' dari dalam try(...)
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setObject(1, idPesanan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    catatan = rs.getString("catatan");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getCatatanById: " + e.getMessage());
            throw e;
        }
        return (catatan == null) ? "" : catatan;
    }

    public List<String> getDaftarStatus() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT status FROM status_pesanan";

        // PERBAIKAN: Gunakan getConnection() langsung
        try (PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("status"));
            }
        }
        return list;
    }

    public void hapusPesanan(String id) throws SQLException {
        String sql = "DELETE FROM public.pesanan WHERE id_pesanan = ?::uuid";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}
