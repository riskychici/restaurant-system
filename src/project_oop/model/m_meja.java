
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

    public void hapusMeja(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
