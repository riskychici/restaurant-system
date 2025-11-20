package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class koneksi implements AutoCloseable {
    private Connection con;

    public koneksi() throws SQLException {
        String host = "localhost";
        String port = "5432";
        String db = "penjualan";
        String username = "risky";
        String pass = "010206";

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + db;

        try {
            Class.forName("org.postgresql.Driver");
            this.con = DriverManager.getConnection(url, username, pass);
            System.out.println("Koneksi PostgreSQL berhasil!");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL tidak ditemukan", e);
        }
    }

    /**
     * Eksekusi query SELECT dan mengembalikan ResultSet.
     */
    public ResultSet executeSelect(String sql, Object... params) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps.executeQuery();
    }

    /**
     * Eksekusi query INSERT, UPDATE, DELETE.
     */
    public int executeUpdate(String sql, Object... params) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps.executeUpdate();
    }

    /**
     * Mengembalikan koneksi aktif (jika perlu digunakan manual).
     */
    public Connection getConnection() {
        return this.con;
    }

    /**
     * Tambahan baru: mengembalikan PreparedStatement siap pakai.
     * Ini digunakan jika ingin mengontrol eksekusi atau ambil metadata.
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if (con == null || con.isClosed()) {
            throw new SQLException("Koneksi belum dibuka atau sudah ditutup.");
        }
        return con.prepareStatement(sql);
    }

    @Override
    public void close() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
            System.out.println("Koneksi PostgreSQL ditutup.");
        }
    }
}
