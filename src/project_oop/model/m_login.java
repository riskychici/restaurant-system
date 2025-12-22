package project_oop.model;

import koneksi.koneksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class m_login {

    private final koneksi koneksi;
    public List<Object[]> rows;
    public Map<String, Integer> columns;

    public m_login() throws SQLException {
        this.koneksi = new koneksi();
    }

    public void get_login_personal(String username, String password) throws SQLException {
        this.rows = new ArrayList<>();
        this.columns = new HashMap<>();

        String sql = "SELECT * FROM personal.login_user(?, ?)";
        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                boolean ResultSet = false;
                System.out.println(ResultSet);
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                // Buat map nama kolom ke index (sekali saja)
                for (int i = 1; i <= columnCount; i++) {
                    this.columns.put(meta.getColumnName(i), i - 1);
                }

                // Ambil semua rows
                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    this.rows.add(row);
                }
            }
        }
    }
}
