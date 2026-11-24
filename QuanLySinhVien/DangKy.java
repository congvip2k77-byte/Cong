
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DangKy extends JFrame {

    JTextField txtTaiKhoan = new JTextField();
    JPasswordField txtMatKhau = new JPasswordField();
    JPasswordField txtNhapLaiMatKhau = new JPasswordField();

    Font font = new Font("Segoe UI", Font.PLAIN, 15);

    public DangKy() {
        setTitle("ĐĂNG KÝ TÀI KHOẢN");
        setSize(450, 430);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(35, 50, 35, 50));

        JLabel title = new JLabel("TẠO TÀI KHOẢN MỚI");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(25, 55, 109));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTaiKhoan = new JLabel("Tên đăng nhập");
        lblTaiKhoan.setFont(font);
        lblTaiKhoan.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtTaiKhoan.setFont(font);
        txtTaiKhoan.setMaximumSize(new Dimension(350, 38));

        JLabel lblMatKhau = new JLabel("Mật khẩu");
        lblMatKhau.setFont(font);
        lblMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtMatKhau.setFont(font);
        txtMatKhau.setMaximumSize(new Dimension(350, 38));

        JLabel lblNhapLai = new JLabel("Nhập lại mật khẩu");
        lblNhapLai.setFont(font);
        lblNhapLai.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNhapLaiMatKhau.setFont(font);
        txtNhapLaiMatKhau.setMaximumSize(new Dimension(350, 38));

        JButton btnDangKy = new JButton("ĐĂNG KÝ");
        btnDangKy.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setBackground(new Color(42, 112, 210));
        btnDangKy.setMaximumSize(new Dimension(350, 42));
        btnDangKy.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDangKy.setFocusPainted(false);

        panel.add(title);
        panel.add(Box.createVerticalStrut(30));
        panel.add(lblTaiKhoan);
        panel.add(Box.createVerticalStrut(7));
        panel.add(txtTaiKhoan);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblMatKhau);
        panel.add(Box.createVerticalStrut(7));
        panel.add(txtMatKhau);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblNhapLai);
        panel.add(Box.createVerticalStrut(7));
        panel.add(txtNhapLaiMatKhau);
        panel.add(Box.createVerticalStrut(25));
        panel.add(btnDangKy);

        add(panel);

        btnDangKy.addActionListener(e -> dangKy());
    }

    void dangKy() {
        String taiKhoan = txtTaiKhoan.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());
        String nhapLai = new String(txtNhapLaiMatKhau.getPassword());

        if (taiKhoan.isEmpty() || matKhau.isEmpty() || nhapLai.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (taiKhoan.length() < 4) {
            JOptionPane.showMessageDialog(this,
                    "Tên đăng nhập phải có ít nhất 4 ký tự!");
            return;
        }

        if (matKhau.length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "Mật khẩu phải có ít nhất 6 ký tự!");
            return;
        }

        if (!matKhau.equals(nhapLai)) {
            JOptionPane.showMessageDialog(this,
                    "Mật khẩu nhập lại không khớp!");
            return;
        }

        String sql = "INSERT INTO taikhoan (tenDangNhap, matKhau, vaiTro) VALUES (?, ?, 'user')";

        try (Connection conn = DatabaseConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                        "Không thể kết nối MySQL!");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, taiKhoan);
                ps.setString(2, matKhau);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Đăng ký tài khoản thành công!");

                dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Đăng ký thất bại! Tên đăng nhập có thể đã tồn tại.");
        }
    }
}
