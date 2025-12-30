package project_oop.model;

import koneksi.koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class untuk mengelola data pembayaran Menangani operasi CRUD dan query
 * terkait pembayaran, status, dan metode pembayaran
 */
public class m_pembayaran {

    private final koneksi db;

    /**
     * Constructor - inisialisasi koneksi database
     *
     * @throws SQLException jika koneksi database gagal
     */
    public m_pembayaran() throws SQLException {
        this.db = new koneksi();
    }

    // ==================== QUERY METHODS ====================
    /**
     * Mengambil data pembayaran berdasarkan parameter pencarian
     *
     * @param search parameter pencarian
     * @return List data pembayaran dalam bentuk array Object
     * @throws SQLException jika terjadi error database
     */
    public List<Object[]> getPembayaran(String search) throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT * FROM public.pembayaran(?)";

        try (PreparedStatement ps = db.prepareStatement(sql)) {
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
     * Mengambil detail pembayaran berdasarkan ID Pelanggan Menampilkan rincian
     * menu pesanan untuk dialog pembayaran
     *
     * @param idPelanggan ID pelanggan yang akan dilihat detailnya
     * @return List detail pembayaran (nama menu, harga, qty, subtotal)
     * @throws SQLException jika terjadi error database
     */
    public List<Object[]> detailPembayaran(int idPelanggan) throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT * FROM public.detail_pembayaran(?)";

        try (ResultSet rs = db.executeSelect(sql, idPelanggan)) {
            while (rs.next()) {
                data.add(new Object[]{
                    rs.getString("nama_menu"),
                    rs.getBigDecimal("harga_satuan"),
                    rs.getInt("qty"),
                    rs.getBigDecimal("subtotal")
                });
            }
        }

        return data;
    }

    // ==================== UPDATE METHODS ====================
    /**
     * Memperbarui data pembayaran pelanggan
     *
     * @param idPelanggan ID pelanggan
     * @param idStatusBayar ID status pembayaran
     * @param idMetodeBayar ID metode pembayaran
     * @return Pesan hasil operasi update
     * @throws SQLException jika terjadi error database
     */
    public String updatePembayaran(int idPelanggan, int idStatusBayar, int idMetodeBayar)
            throws SQLException {
        String sql = "SELECT public.update_pembayaran(?, ?, ?)";
        String pesan = "Error: Gagal memproses";

        try (ResultSet rs = db.executeSelect(sql, idPelanggan, idStatusBayar, idMetodeBayar)) {
            if (rs.next()) {
                pesan = rs.getString(1);
            }
        }

        return pesan;
    }

    // ==================== DROPDOWN/COMBOBOX METHODS ====================
    /**
     * Mengambil data status pembayaran untuk ComboBox
     *
     * @return List data status pembayaran (id, status)
     * @throws SQLException jika terjadi error database
     */
    public List<Object[]> getStatusPembayaran() throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT id_status_bayar, status FROM public.status_pembayaran "
                + "ORDER BY id_status_bayar";

        try (ResultSet rs = db.executeSelect(sql)) {
            while (rs.next()) {
                data.add(new Object[]{
                    rs.getInt("id_status_bayar"),
                    rs.getString("status")
                });
            }
        }

        return data;
    }

    /**
     * Mengambil data metode pembayaran untuk ComboBox
     *
     * @return List data metode pembayaran (id, jenis)
     * @throws SQLException jika terjadi error database
     */
    public List<Object[]> getMetodePembayaran() throws SQLException {
        List<Object[]> data = new ArrayList<>();
        String sql = "SELECT id_metode_bayar, jenis FROM public.metode_pembayaran "
                + "ORDER BY id_metode_bayar";

        try (ResultSet rs = db.executeSelect(sql)) {
            while (rs.next()) {
                data.add(new Object[]{
                    rs.getInt("id_metode_bayar"),
                    rs.getString("jenis")
                });
            }
        }

        return data;
    }
}
