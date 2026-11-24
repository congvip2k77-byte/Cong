
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DangNhap extends JFrame {

    JTextField txtTaiKhoan = new JTextField();
    JPasswordField txtMatKhau = new JPasswordField();

    Font font = new Font("Segoe UI", Font.PLAIN, 15);

    public DangNhap() {
        setTitle("ĐĂNG NHẬP HỆ THỐNG");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridLayout(1, 2));

        JPanel trai = new JPanel();
        trai.setBackground(new Color(25, 55, 109));
        trai.setLayout(new BoxLayout(trai, BoxLayout.Y_AXIS));

        JLabel ten = new JLabel("STUDENT");
        ten.setFont(new Font("Segoe UI", Font.BOLD, 38));
        ten.setForeground(Color.WHITE);
        ten.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel ten2 = new JLabel("MANAGEMENT");
        ten2.setFont(new Font("Segoe UI", Font.BOLD, 30));
        ten2.setForeground(new Color(130, 190, 255));
        ten2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel moTa = new JLabel("HỆ THỐNG QUẢN LÝ SINH VIÊN");
        moTa.setFont(font);
        moTa.setForeground(Color.WHITE);
        moTa.setAlignmentX(Component.CENTER_ALIGNMENT);

        trai.add(Box.createVerticalGlue());
        trai.add(ten);
        trai.add(Box.createVerticalStrut(10));
        trai.add(ten2);
        trai.add(Box.createVerticalStrut(20));
        trai.add(moTa);
        trai.add(Box.createVerticalGlue());

        JPanel phai = new JPanel();
        phai.setBackground(Color.WHITE);
        phai.setLayout(new BoxLayout(phai, BoxLayout.Y_AXIS));
        phai.setBorder(BorderFactory.createEmptyBorder(55, 45, 55, 45));

        JLabel title = new JLabel("ĐĂNG NHẬP");
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        title.setForeground(new Color(25, 55, 109));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Chào mừng bạn quay trở lại!");
        subtitle.setFont(font);
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTaiKhoan = new JLabel("Tên đăng nhập");
        lblTaiKhoan.setFont(font);
        lblTaiKhoan.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtTaiKhoan.setFont(font);
        txtTaiKhoan.setMaximumSize(new Dimension(300, 40));

        JLabel lblMatKhau = new JLabel("Mật khẩu");
        lblMatKhau.setFont(font);
        lblMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtMatKhau.setFont(font);
        txtMatKhau.setMaximumSize(new Dimension(300, 40));

        JButton btnDangNhap = new JButton("ĐĂNG NHẬP");
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setBackground(new Color(42, 112, 210));
        btnDangNhap.setMaximumSize(new Dimension(300, 42));
        btnDangNhap.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDangNhap.setFocusPainted(false);

        JButton btnDangKy = new JButton("TẠO TÀI KHOẢN MỚI");
        btnDangKy.setFont(font);
        btnDangKy.setForeground(new Color(42, 112, 210));
        btnDangKy.setBackground(Color.WHITE);
        btnDangKy.setMaximumSize(new Dimension(300, 40));
        btnDangKy.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDangKy.setFocusPainted(false);

        phai.add(title);
        phai.add(Box.createVerticalStrut(10));
        phai.add(subtitle);
        phai.add(Box.createVerticalStrut(40));
        phai.add(lblTaiKhoan);
        phai.add(Box.createVerticalStrut(8));
        phai.add(txtTaiKhoan);
        phai.add(Box.createVerticalStrut(20));
        phai.add(lblMatKhau);
        phai.add(Box.createVerticalStrut(8));
        phai.add(txtMatKhau);
        phai.add(Box.createVerticalStrut(25));
        phai.add(btnDangNhap);
        phai.add(Box.createVerticalStrut(12));
        phai.add(btnDangKy);

        add(trai);
        add(phai);

        btnDangNhap.addActionListener(e -> dangNhap());
        txtMatKhau.addActionListener(e -> dangNhap());

        btnDangKy.addActionListener(e -> {
            new DangKy().setVisible(true);
        });
    }

    void dangNhap() {
        String taiKhoan = txtTaiKhoan.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());

        if (taiKhoan.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
            return;
        }

        String sql = "SELECT vaiTro FROM taikhoan WHERE tenDangNhap=? AND matKhau=?";

        try (Connection conn = DatabaseConnection.getConnection()) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                        "Không thể kết nối MySQL!");
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, taiKhoan);
                ps.setString(2, matKhau);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String vaiTro = rs.getString("vaiTro");

                        JOptionPane.showMessageDialog(this,
                                "Đăng nhập thành công!");

                        dispose();
                        new GiaoDien(vaiTro).setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Sai tài khoản hoặc mật khẩu!");
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi đăng nhập: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DangNhap().setVisible(true);
        });
    }
}
