package project_oop.model;

import koneksi.koneksi;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Model class untuk mengelola data pesanan
 */
public class m_pesanan {

    private final koneksi koneksi;
    private final DecimalFormat currencyFormatter;

    // ==================== CONSTRUCTOR ====================
    public m_pesanan() throws SQLException {
        this.koneksi = new koneksi();
        this.currencyFormatter = initializeCurrencyFormatter();
    }

    // ==================== HELPER METHODS ====================
    /**
     * Mendapatkan koneksi database aktif
     */
    private Connection getConnection() throws SQLException {
        return koneksi.getConnection();
    }

    /**
     * Inisialisasi formatter untuk mata uang Rupiah
     */
    private DecimalFormat initializeCurrencyFormatter() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("id", "ID"));
        symbols.setGroupingSeparator('.');
        return new DecimalFormat("###,###", symbols);
    }

    // ==================== QUERY METHODS - READ ====================
    /**
     * Mengambil daftar pesanan berdasarkan pencarian
     */
    public List<Object[]> getPesanan(String search) throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT * FROM public.pesanan(?)";

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

    /**
     * Mengambil daftar meja beserta statusnya
     */
    public List<String> getMejaKosong() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT nomor_meja, status_meja FROM public.meja ORDER BY nomor_meja ASC";

        try (Statement st = getConnection().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("nomor_meja") + " - " + rs.getString("status_meja"));
            }
        }
        return list;
    }

    /**
     * Mengambil daftar menu yang aktif beserta harganya
     */
    public List<String> getDaftarMenu() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT nama_menu, harga, stok FROM v_menu_aktif ORDER BY nama_menu ASC";

        try (Statement st = getConnection().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String nama = rs.getString("nama_menu");
                double harga = rs.getDouble("harga");
                int stok = rs.getInt("stok");
                String hargaFormatted = currencyFormatter.format(harga);

                String labelStok = (stok > 0) ? "[Stok: " + stok + "]" : "[STOK HABIS]";

                list.add(nama + " (Rp " + hargaFormatted + ") " + labelStok);
            }
        }
        return list;
    }

    /**
     * Mengambil daftar status pesanan
     */
    public List<String> getDaftarStatus() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT status FROM status_pesanan";

        try (PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("status"));
            }
        }
        return list;
    }

    public int getIdMejaByNomor(String nomorMeja) throws SQLException {
        String nomorClean = nomorMeja.split(" ")[0].trim();

        String sql = "SELECT id_meja FROM public.meja WHERE nomor_meja = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, nomorClean);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_meja");
                }
            }
        }
        throw new SQLException("Meja dengan nomor " + nomorClean + " tidak ditemukan!");
    }

    /**
     * Mengambil daftar pelanggan yang memiliki pesanan aktif
     */
    public List<String> getPelangganAktif() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT * FROM public.get_pelanggan_aktif()";

        try (Statement st = getConnection().createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        }
        return list;
    }

    /**
     * Mengambil catatan pesanan berdasarkan ID pesanan
     */
    public String getCatatanById(UUID idPesanan) throws SQLException {
        String sql = "SELECT catatan FROM detail_pesanan WHERE id_pesanan = ? LIMIT 1";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setObject(1, idPesanan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String catatan = rs.getString("catatan");
                    return (catatan == null) ? "" : catatan;
                }
            }
        }
        return "";
    }

    // ==================== LOOKUP METHODS ====================
    /**
     * Mengambil UUID karyawan berdasarkan ID personal
     */
    public UUID getUUIDKaryawan(int idPersonal) throws SQLException {
        String sql = "SELECT id_karyawan FROM karyawan WHERE id_personal = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, idPersonal);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return (UUID) rs.getObject("id_karyawan");
                }
            }
        }
        return null;
    }

    /**
     * Mengambil ID menu berdasarkan nama menu Input: "Ayam Bakar Madu (Rp
     * 27.000)" Output: ID menu dari database
     */
    public int getIdMenuByNama(String namaMenuFull) throws SQLException {
        String namaSaja = namaMenuFull.split(" \\(")[0].trim();

        String sql = "SELECT id_menu FROM public.daftar_menu WHERE LOWER(nama_menu) = LOWER(?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, namaSaja);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_menu");
                }
            }
        }
        return -1;
    }

    // ==================== CRUD METHODS ====================
    /**
     * Menyimpan pesanan baru ke database
     */
    public void simpanPesananBaru(String nama, int idMeja, UUID idKaryawan, String jsonItems)
            throws SQLException {
        String sql = "SELECT buat_pesanan_baru(?, ?, ?, ?::jsonb)";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setInt(2, idMeja);
            ps.setObject(3, idKaryawan);
            ps.setString(4, jsonItems);
            ps.execute();
        }
    }

    /**
     * Mengupdate data pesanan yang sudah ada
     */
    public void editPesanan(UUID idPesanan, String namaPelanggan, int idMeja,
            String jsonItems, String status) throws SQLException {
        String sql = "SELECT public.edit_pesanan(?, ?, ?, ?::jsonb, ?)";

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

    /**
     * Menghapus pesanan berdasarkan ID
     */
    public void hapusPesanan(String id) throws SQLException {
        String sql = "DELETE FROM public.pesanan WHERE id_pesanan = ?::uuid";

        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}
