
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuanLyTaiKhoan extends JFrame {

    private JTable bangTaiKhoan;
    private DefaultTableModel model;

    public QuanLyTaiKhoan() {
        setTitle("QUẢN LÝ TÀI KHOẢN");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel tieuDe = new JLabel("QUẢN LÝ TÀI KHOẢN", JLabel.CENTER);
        tieuDe.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tieuDe.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(tieuDe, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new String[]{"ID", "Tên đăng nhập", "Vai trò"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bangTaiKhoan = new JTable(model);
        bangTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bangTaiKhoan.setRowHeight(30);

        add(new JScrollPane(bangTaiKhoan), BorderLayout.CENTER);

        JPanel panelNut = new JPanel(new FlowLayout());

        JButton btnTaiLai = new JButton("Tải lại");
        JButton btnDoiVaiTro = new JButton("Đổi vai trò");
        JButton btnDoiMatKhau = new JButton("Đổi mật khẩu");
        JButton btnXoa = new JButton("Xóa tài khoản");

        panelNut.add(btnTaiLai);
        panelNut.add(btnDoiVaiTro);
        panelNut.add(btnDoiMatKhau);
        panelNut.add(btnXoa);

        add(panelNut, BorderLayout.SOUTH);

        btnTaiLai.addActionListener(e -> taiDanhSach());
        btnDoiVaiTro.addActionListener(e -> doiVaiTro());
        btnDoiMatKhau.addActionListener(e -> doiMatKhau());
        btnXoa.addActionListener(e -> xoaTaiKhoan());

        taiDanhSach();
    }

    private void taiDanhSach() {
        model.setRowCount(0);

        String sql = "SELECT id, tenDangNhap, vaiTro FROM taikhoan";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("tenDangNhap"),
                    rs.getString("vaiTro")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi tải tài khoản: " + e.getMessage());
        }
    }

    private String layTenDangNhap() {
        int row = bangTaiKhoan.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng chọn tài khoản!");
            return null;
        }

        return model.getValueAt(row, 1).toString();
    }

    private void doiVaiTro() {
        String ten = layTenDangNhap();
        if (ten == null) return;

        if (ten.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this,
                "Không thể đổi vai trò tài khoản admin!");
            return;
        }

        int row = bangTaiKhoan.getSelectedRow();

        String vaiTro = model.getValueAt(row, 2).toString();
        String moi = vaiTro.equalsIgnoreCase("admin") ? "user" : "admin";

        int chon = JOptionPane.showConfirmDialog(this,
            "Đổi vai trò thành " + moi + "?",
            "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (chon != JOptionPane.YES_OPTION) return;

        String sql = "UPDATE taikhoan SET vaiTro=? WHERE tenDangNhap=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, moi);
            ps.setString(2, ten);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Đổi vai trò thành công!");
            taiDanhSach();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void doiMatKhau() {
        String ten = layTenDangNhap();
        if (ten == null) return;

        JPasswordField matKhau = new JPasswordField();

        int chon = JOptionPane.showConfirmDialog(this,
            matKhau,
            "Nhập mật khẩu mới cho " + ten,
            JOptionPane.OK_CANCEL_OPTION);

        if (chon != JOptionPane.OK_OPTION) return;

        String mk = new String(matKhau.getPassword());

        if (mk.length() < 6) {
            JOptionPane.showMessageDialog(this,
                "Mật khẩu phải có ít nhất 6 ký tự!");
            return;
        }

        String sql = "UPDATE taikhoan SET matKhau=? WHERE tenDangNhap=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mk);
            ps.setString(2, ten);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void xoaTaiKhoan() {
        String ten = layTenDangNhap();
        if (ten == null) return;

        if (ten.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this,
                "Không thể xóa tài khoản admin!");
            return;
        }

        int chon = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa tài khoản " + ten + "?",
            "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (chon != JOptionPane.YES_OPTION) return;

        String sql = "DELETE FROM taikhoan WHERE tenDangNhap=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ten);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!");
            taiDanhSach();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
