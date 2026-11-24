
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SinhVienDAO {

    // Hien thi danh sach sinh vien
    public List<Object[]> getAll() {
        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT * FROM sinhvien";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object[] sv = {
                    rs.getString("maSV"),
                    rs.getString("hoTen"),
                    rs.getString("ngaySinh"),
                    rs.getString("gioiTinh"),
                    rs.getString("lop"),
                    rs.getString("email"),
                    rs.getString("soDienThoai")
                };

                list.add(sv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Them sinh vien
    public boolean insert(Object[] sv) {
        String sql = "INSERT INTO sinhvien VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < 7; i++) {
                ps.setString(i + 1, sv[i] == null ? null : sv[i].toString());
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sua sinh vien
    public boolean update(Object[] sv) {
        String sql = "UPDATE sinhvien SET hoTen=?, ngaySinh=?, gioiTinh=?, lop=?, email=?, soDienThoai=? WHERE maSV=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 1; i < 6; i++) {
                ps.setString(i, sv[i] == null ? null : sv[i].toString());
            }

            ps.setString(6, sv[6] == null ? null : sv[6].toString());
            ps.setString(7, sv[0].toString());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xoa sinh vien
    public boolean delete(String maSV) {
        String sql = "DELETE FROM sinhvien WHERE maSV=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSV);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
