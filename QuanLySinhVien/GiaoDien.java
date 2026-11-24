
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GiaoDien extends JFrame {

    SinhVienDAO dao = new SinhVienDAO();
    String vaiTro;

    Font font = new Font("Segoe UI", Font.PLAIN, 14);
    Font fontDam = new Font("Segoe UI", Font.BOLD, 14);

    JTextField maSV = new JTextField();
    JTextField hoTen = new JTextField();
    JTextField ngaySinh = new JTextField();
    JTextField gioiTinh = new JTextField();
    JTextField lop = new JTextField();
    JTextField email = new JTextField();
    JTextField sdt = new JTextField();

    JTextField txtTimKiem = new JTextField(18);
    JComboBox<String> cboLop = new JComboBox<>();

    DefaultTableModel model;
    JTable table;

    Color xanhDam = new Color(25, 55, 109);
    Color xanh = new Color(42, 112, 210);
    Color nen = new Color(242, 246, 252);

    JLabel lblTong = new JLabel("0");
    JLabel lblNam = new JLabel("0");
    JLabel lblNu = new JLabel("0");

    public GiaoDien() {
        this("admin");
    }

    public GiaoDien(String vaiTro) {
        this.vaiTro = vaiTro;

        setTitle("PHẦN MỀM QUẢN LÝ SINH VIÊN");
        setSize(1250, 780);
        setMinimumSize(new Dimension(1050, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(nen);
        setLayout(new BorderLayout());

        // MENU BÊN TRÁI
        JPanel menu = new JPanel();
        menu.setBackground(xanhDam);
        menu.setPreferredSize(new Dimension(220, 0));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(BorderFactory.createEmptyBorder(25, 15, 20, 15));

        JLabel logo = new JLabel("QUẢN LÝ");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 25));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logo2 = new JLabel("SINH VIÊN");
        logo2.setForeground(new Color(170, 205, 255));
        logo2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblVaiTro = new JLabel("QUYỀN: " + vaiTro.toUpperCase());
        lblVaiTro.setForeground(Color.WHITE);
        lblVaiTro.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblVaiTro.setAlignmentX(Component.CENTER_ALIGNMENT);

        menu.add(logo);
        menu.add(logo2);
        menu.add(Box.createVerticalStrut(15));
        menu.add(lblVaiTro);
        menu.add(Box.createVerticalStrut(45));

        JButton btnTrangChu = taoNutMenu("Tổng quan");
        JButton btnQuanLy = taoNutMenu("Quản lý sinh viên");
        JButton btnThongKe = taoNutMenu("Thống kê");
        JButton btnTaiKhoan = taoNutMenu("Quản lý tài khoản");

        menu.add(btnTrangChu);
        menu.add(Box.createVerticalStrut(12));
        menu.add(btnQuanLy);
        menu.add(Box.createVerticalStrut(12));
        menu.add(btnThongKe);

        boolean laAdmin = vaiTro != null &&
                          vaiTro.equalsIgnoreCase("admin");

        btnTaiKhoan.setVisible(laAdmin);

        if (laAdmin) {
            menu.add(Box.createVerticalStrut(12));
            menu.add(btnTaiKhoan);
        }

        menu.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("QUẢN LÝ SINH VIÊN");
        footer.setForeground(new Color(190, 205, 230));
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        menu.add(footer);

        add(menu, BorderLayout.WEST);

        // KHU VỰC NỘI DUNG
        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setBackground(nen);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ SINH VIÊN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 25));
        title.setForeground(xanhDam);

        JLabel subtitle = new JLabel(
            "Quản lý thông tin sinh viên và dữ liệu học tập"
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(Color.GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);
        content.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        // THẺ THỐNG KÊ
        JPanel thongKePanel = new JPanel(new GridLayout(1, 3, 15, 0));
        thongKePanel.setOpaque(false);
        thongKePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        thongKePanel.add(taoTheThongKe(
            "TỔNG SINH VIÊN", lblTong, xanh
        ));

        thongKePanel.add(taoTheThongKe(
            "SINH VIÊN NAM", lblNam, new Color(30, 145, 120)
        ));

        thongKePanel.add(taoTheThongKe(
            "SINH VIÊN NỮ", lblNu, new Color(190, 90, 150)
        ));

        body.add(thongKePanel);
        body.add(Box.createVerticalStrut(15));

        // FORM THÔNG TIN
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 230, 240)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel formTitle = new JLabel("THÔNG TIN SINH VIÊN");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(xanhDam);

        JPanel form = new JPanel(new GridLayout(4, 4, 12, 12));
        form.setBackground(Color.WHITE);

        themTruong(form, "Mã sinh viên:", maSV);
        themTruong(form, "Họ và tên:", hoTen);
        themTruong(form, "Ngày sinh:", ngaySinh);
        themTruong(form, "Giới tính:", gioiTinh);
        themTruong(form, "Lớp:", lop);
        themTruong(form, "Email:", email);
        themTruong(form, "Số điện thoại:", sdt);

        JPanel formContent = new JPanel(new BorderLayout(10, 12));
        formContent.setBackground(Color.WHITE);
        formContent.add(formTitle, BorderLayout.NORTH);
        formContent.add(form, BorderLayout.CENTER);

        formPanel.add(formContent, BorderLayout.CENTER);

        body.add(formPanel);
        body.add(Box.createVerticalStrut(15));

        // TÌM KIẾM VÀ LỌC
        JPanel searchPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 10, 10)
        );
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createLineBorder(
            new Color(225, 230, 240)
        ));

        JButton btnTimKiem = taoNut("Tìm kiếm", xanh);
        JButton btnLoc = taoNut(
            "Lọc theo lớp", new Color(30, 145, 120)
        );
        JButton btnTatCa = taoNut(
            "Hiện tất cả", new Color(100, 110, 125)
        );

        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        lblTimKiem.setFont(font);

        JLabel lblChonLop = new JLabel("Chọn lớp:");
        lblChonLop.setFont(font);

        txtTimKiem.setFont(font);
        cboLop.setFont(font);

        searchPanel.add(lblTimKiem);
        searchPanel.add(txtTimKiem);
        searchPanel.add(btnTimKiem);
        searchPanel.add(lblChonLop);
        searchPanel.add(cboLop);
        searchPanel.add(btnLoc);
        searchPanel.add(btnTatCa);

        body.add(searchPanel);
        body.add(Box.createVerticalStrut(15));

        // BẢNG SINH VIÊN
        String[] columns = {
            "Mã SV", "Họ tên", "Ngày sinh",
            "Giới tính", "Lớp", "Email", "Số điện thoại"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(font);
        table.setSelectionBackground(new Color(210, 230, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(230, 235, 243));

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(xanhDam);
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setFont(fontDam);
        tableHeader.setPreferredSize(new Dimension(0, 35));

        JScrollPane scroll = new JScrollPane(table);

        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel tableTitle = new JLabel("DANH SÁCH SINH VIÊN");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(xanhDam);

        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scroll, BorderLayout.CENTER);

        body.add(tablePanel);

        // CÁC NÚT CHỨC NĂNG
        JPanel buttons = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 10)
        );
        buttons.setBackground(nen);

        JButton them = taoNut("THÊM", new Color(30, 145, 90));
        JButton sua = taoNut("SỬA", xanh);
        JButton xoa = taoNut("XÓA", new Color(210, 65, 65));
        JButton lamMoi = taoNut(
            "LÀM MỚI", new Color(100, 110, 125)
        );

        buttons.add(them);
        buttons.add(sua);
        buttons.add(xoa);
        buttons.add(lamMoi);

        them.setVisible(laAdmin);
        sua.setVisible(laAdmin);
        xoa.setVisible(laAdmin);

        maSV.setEditable(laAdmin);
        hoTen.setEditable(laAdmin);
        ngaySinh.setEditable(laAdmin);
        gioiTinh.setEditable(laAdmin);
        lop.setEditable(laAdmin);
        email.setEditable(laAdmin);
        sdt.setEditable(laAdmin);

        JPanel mainBody = new JPanel(new BorderLayout(10, 10));
        mainBody.setOpaque(false);
        mainBody.add(body, BorderLayout.CENTER);
        mainBody.add(buttons, BorderLayout.SOUTH);

        content.add(mainBody, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        // SỰ KIỆN
        them.addActionListener(e -> themSinhVien());
        sua.addActionListener(e -> suaSinhVien());
        xoa.addActionListener(e -> xoaSinhVien());
        lamMoi.addActionListener(e -> lamMoi());

        btnTimKiem.addActionListener(e -> timKiemSinhVien());
        txtTimKiem.addActionListener(e -> timKiemSinhVien());

        btnLoc.addActionListener(e -> locTheoLop());

        btnTatCa.addActionListener(e -> {
            txtTimKiem.setText("");
            cboLop.setSelectedItem("Tất cả");
            taiDanhSach();
        });

        btnThongKe.addActionListener(e -> thongKeSinhVien());

        btnTaiKhoan.addActionListener(e -> {
            if (laAdmin) {
                new QuanLyTaiKhoan().setVisible(true);
            }
        });

        btnTrangChu.addActionListener(e -> {
            taiDanhSach();
            taiDanhSachLop();
            lamMoi();
        });

        btnQuanLy.addActionListener(e -> {
            taiDanhSach();
            taiDanhSachLop();
            lamMoi();
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                hienThiDuLieu();
            }
        });

        taiDanhSach();
        taiDanhSachLop();
        capNhatThongKe();
    }

    JButton taoNutMenu(String ten) {
        JButton nut = new JButton(ten);
        nut.setFont(fontDam);
        nut.setForeground(Color.WHITE);
        nut.setBackground(new Color(38, 75, 135));
        nut.setFocusPainted(false);
        nut.setBorderPainted(false);
        nut.setHorizontalAlignment(SwingConstants.LEFT);
        nut.setMaximumSize(new Dimension(190, 45));
        nut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return nut;
    }

    JButton taoNut(String ten, Color mau) {
        JButton nut = new JButton(ten);
        nut.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nut.setForeground(Color.WHITE);
        nut.setBackground(mau);
        nut.setFocusPainted(false);
        nut.setBorderPainted(false);
        nut.setPreferredSize(new Dimension(125, 38));
        nut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return nut;
    }

    JPanel taoTheThongKe(String ten, JLabel giaTri, Color mau) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, mau),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel label = new JLabel(ten);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(Color.GRAY);

        giaTri.setFont(new Font("Segoe UI", Font.BOLD, 25));
        giaTri.setForeground(mau);

        panel.add(label, BorderLayout.NORTH);
        panel.add(giaTri, BorderLayout.CENTER);

        return panel;
    }

    void themTruong(JPanel panel, String ten, JTextField field) {
        JLabel label = new JLabel(ten);
        label.setFont(font);
        panel.add(label);

        field.setFont(font);
        panel.add(field);
    }

    void taiDanhSach() {
        model.setRowCount(0);

        List<Object[]> list = dao.getAll();

        for (Object[] sv : list) {
            model.addRow(sv);
        }

        capNhatThongKe();
    }

    void taiDanhSachLop() {
        cboLop.removeAllItems();
        cboLop.addItem("Tất cả");

        List<Object[]> list = dao.getAll();

        for (Object[] sv : list) {
            String tenLop = String.valueOf(sv[4]).trim();

            if (!tenLop.isEmpty() && !tenLop.equals("null")) {
                boolean daCo = false;

                for (int i = 0; i < cboLop.getItemCount(); i++) {
                    if (cboLop.getItemAt(i).equals(tenLop)) {
                        daCo = true;
                        break;
                    }
                }

                if (!daCo) {
                    cboLop.addItem(tenLop);
                }
            }
        }
    }

    void capNhatThongKe() {
        List<Object[]> list = dao.getAll();

        int nam = 0;
        int nu = 0;

        for (Object[] sv : list) {
            String gt = String.valueOf(sv[3]).trim();

            if (gt.equalsIgnoreCase("Nam")) {
                nam++;
            } else if (gt.equalsIgnoreCase("Nu") ||
                       gt.equalsIgnoreCase("Nữ")) {
                nu++;
            }
        }

        lblTong.setText(String.valueOf(list.size()));
        lblNam.setText(String.valueOf(nam));
        lblNu.setText(String.valueOf(nu));
    }

    void timKiemSinhVien() {
        String tuKhoa = txtTimKiem.getText().trim().toLowerCase();

        model.setRowCount(0);

        List<Object[]> list = dao.getAll();

        for (Object[] sv : list) {
            String ma = String.valueOf(sv[0]).toLowerCase();
            String ten = String.valueOf(sv[1]).toLowerCase();

            if (ma.contains(tuKhoa) || ten.contains(tuKhoa)) {
                model.addRow(sv);
            }
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                this, "Không tìm thấy sinh viên!"
            );
        }
    }

    void locTheoLop() {
        if (cboLop.getSelectedItem() == null) {
            return;
        }

        String lopChon = cboLop.getSelectedItem().toString();

        model.setRowCount(0);

        List<Object[]> list = dao.getAll();

        for (Object[] sv : list) {
            if (lopChon.equals("Tất cả") ||
                String.valueOf(sv[4]).equalsIgnoreCase(lopChon)) {
                model.addRow(sv);
            }
        }
    }

    void thongKeSinhVien() {
        List<Object[]> list = dao.getAll();

        int nam = 0;
        int nu = 0;

        Map<String, Integer> thongKeLop = new TreeMap<>();

        for (Object[] sv : list) {
            String gt = String.valueOf(sv[3]).trim();
            String tenLop = String.valueOf(sv[4]).trim();

            if (gt.equalsIgnoreCase("Nam")) {
                nam++;
            } else if (gt.equalsIgnoreCase("Nu") ||
                       gt.equalsIgnoreCase("Nữ")) {
                nu++;
            }

            if (tenLop.equals("null") || tenLop.isEmpty()) {
                tenLop = "Chưa phân lớp";
            }

            thongKeLop.put(
                tenLop,
                thongKeLop.getOrDefault(tenLop, 0) + 1
            );
        }

        JDialog dialog = new JDialog(
            this,
            "DASHBOARD - THỐNG KÊ SINH VIÊN",
            true
        );

        dialog.setSize(1000, 620);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.getContentPane().setBackground(nen);

        JLabel title = new JLabel(
            "DASHBOARD THỐNG KÊ SINH VIÊN",
            SwingConstants.CENTER
        );
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(xanhDam);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        dialog.add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 3, 15, 0));
        cards.setBackground(nen);
        cards.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));

        JLabel tong = new JLabel(String.valueOf(list.size()));
        JLabel namLabel = new JLabel(String.valueOf(nam));
        JLabel nuLabel = new JLabel(String.valueOf(nu));

        cards.add(taoTheThongKe("TỔNG SINH VIÊN", tong, xanh));
        cards.add(taoTheThongKe(
            "SINH VIÊN NAM", namLabel, new Color(30, 145, 120)
        ));
        cards.add(taoTheThongKe(
            "SINH VIÊN NỮ", nuLabel, new Color(190, 90, 150)
        ));

        JPanel charts = new JPanel(new GridLayout(1, 2, 15, 0));
        charts.setBackground(nen);
        charts.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        charts.add(new BieuDoGioiTinh(nam, nu));
        charts.add(new BieuDoLop(thongKeLop));

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBackground(nen);
        center.add(cards, BorderLayout.NORTH);
        center.add(charts, BorderLayout.CENTER);

        dialog.add(center, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    Object[] layDuLieu() {
        return new Object[] {
            maSV.getText().trim(),
            hoTen.getText().trim(),
            ngaySinh.getText().trim(),
            gioiTinh.getText().trim(),
            lop.getText().trim(),
            email.getText().trim(),
            sdt.getText().trim()
        };
    }

    void themSinhVien() {
        if (vaiTro == null || !vaiTro.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(
                this, "Bạn không có quyền thêm!"
            );
            return;
        }

        Object[] sv = layDuLieu();

        if (sv[0].toString().isEmpty() ||
            sv[1].toString().isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Mã sinh viên và họ tên không được để trống!"
            );
            return;
        }

        if (dao.insert(sv)) {
            JOptionPane.showMessageDialog(
                this, "Thêm sinh viên thành công!"
            );
            taiDanhSach();
            taiDanhSachLop();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(
                this, "Thêm thất bại! Kiểm tra dữ liệu."
            );
        }
    }

    void suaSinhVien() {
        if (vaiTro == null || !vaiTro.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(
                this, "Bạn không có quyền sửa!"
            );
            return;
        }

        Object[] sv = layDuLieu();

        if (sv[0].toString().isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Hãy chọn sinh viên cần sửa!"
            );
            return;
        }

        if (dao.update(sv)) {
            JOptionPane.showMessageDialog(
                this, "Cập nhật thành công!"
            );
            taiDanhSach();
            taiDanhSachLop();
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(
                this, "Cập nhật thất bại!"
            );
        }
    }

    void xoaSinhVien() {
        if (vaiTro == null || !vaiTro.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(
                this, "Bạn không có quyền xóa!"
            );
            return;
        }

        String ma = maSV.getText().trim();

        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Hãy chọn sinh viên cần xóa!"
            );
            return;
        }

        int chon = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa sinh viên này?",
            "XÁC NHẬN XÓA",
            JOptionPane.YES_NO_OPTION
        );

        if (chon == JOptionPane.YES_OPTION) {
            if (dao.delete(ma)) {
                JOptionPane.showMessageDialog(
                    this, "Xóa thành công!"
                );
                taiDanhSach();
                taiDanhSachLop();
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(
                    this, "Xóa thất bại!"
                );
            }
        }
    }

    void hienThiDuLieu() {
        int row = table.getSelectedRow();

        if (row >= 0) {
            maSV.setText(String.valueOf(model.getValueAt(row, 0)));
            hoTen.setText(String.valueOf(model.getValueAt(row, 1)));
            ngaySinh.setText(String.valueOf(model.getValueAt(row, 2)));
            gioiTinh.setText(String.valueOf(model.getValueAt(row, 3)));
            lop.setText(String.valueOf(model.getValueAt(row, 4)));
            email.setText(String.valueOf(model.getValueAt(row, 5)));
            sdt.setText(String.valueOf(model.getValueAt(row, 6)));
        }
    }

    void lamMoi() {
        maSV.setText("");
        hoTen.setText("");
        ngaySinh.setText("");
        gioiTinh.setText("");
        lop.setText("");
        email.setText("");
        sdt.setText("");

        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DangNhap().setVisible(true);
        });
    }
}

// BIỂU ĐỒ TRÒN GIỚI TÍNH
class BieuDoGioiTinh extends JPanel {

    int nam;
    int nu;

    BieuDoGioiTinh(int nam, int nu) {
        this.nam = nam;
        this.nu = nu;

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 230, 240)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setFont(new Font("Segoe UI", Font.BOLD, 17));
        g2.setColor(new Color(25, 55, 109));
        g2.drawString("TỶ LỆ SINH VIÊN NAM - NỮ", 15, 25);

        int tong = nam + nu;

        int x = 35;
        int y = 65;
        int size = 190;

        if (tong == 0) {
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            g2.drawString("Chưa có dữ liệu thống kê", 35, 150);
        } else {
            int gocNam = (int) Math.round(nam * 360.0 / tong);
            int gocNu = 360 - gocNam;

            g2.setColor(new Color(42, 112, 210));
            g2.fillArc(x, y, size, size, 90, gocNam);

            g2.setColor(new Color(190, 90, 150));
            g2.fillArc(x, y, size, size, 90 - gocNam, -gocNu);
        }

        int legendX = 250;
        int legendY = 105;

        g2.setColor(new Color(42, 112, 210));
        g2.fillRect(legendX, legendY, 18, 18);

        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        g2.drawString("Nam: " + nam, legendX + 28, legendY + 15);

        g2.setColor(new Color(190, 90, 150));
        g2.fillRect(legendX, legendY + 45, 18, 18);

        g2.setColor(Color.DARK_GRAY);
        g2.drawString("Nữ: " + nu, legendX + 28, legendY + 60);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.drawString("Tổng: " + tong + " sinh viên", legendX, legendY + 110);
    }
}

// BIỂU ĐỒ CỘT THEO LỚP
class BieuDoLop extends JPanel {

    Map<String, Integer> duLieu;

    BieuDoLop(Map<String, Integer> duLieu) {
        this.duLieu = new TreeMap<>(duLieu);

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 230, 240)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setFont(new Font("Segoe UI", Font.BOLD, 17));
        g2.setColor(new Color(25, 55, 109));
        g2.drawString("SỐ LƯỢNG SINH VIÊN THEO LỚP", 15, 25);

        if (duLieu.isEmpty()) {
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            g2.drawString("Chưa có dữ liệu thống kê", 30, 120);
            return;
        }

        int max = 0;

        for (int soLuong : duLieu.values()) {
            if (soLuong > max) {
                max = soLuong;
            }
        }

        int x = 45;
        int dayY = getHeight() - 65;
        int maxHeight = getHeight() - 125;

        int soCot = duLieu.size();
        int khoangTrong = Math.max(8, 30 - soCot);

        int chieuRongCot = Math.max(
            12,
            Math.min(
                45,
                (getWidth() - 100) / soCot - khoangTrong
            )
        );

        int index = 0;

        for (Map.Entry<String, Integer> entry : duLieu.entrySet()) {

            int giaTri = entry.getValue();

            int chieuCao = (int) (
                giaTri * (maxHeight * 1.0 / max)
            );

            int cotX = x + index * (chieuRongCot + khoangTrong);

            g2.setColor(new Color(42, 112, 210));
            g2.fillRoundRect(
                cotX,
                dayY - chieuCao,
                chieuRongCot,
                chieuCao,
                8,
                8
            );

            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));

            String so = String.valueOf(giaTri);
            g2.drawString(
                so,
                cotX + chieuRongCot / 2 - 4,
                dayY - chieuCao - 8
            );

            String tenLop = entry.getKey();

            if (tenLop.length() > 8) {
                tenLop = tenLop.substring(0, 8) + "...";
            }

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.drawString(tenLop, cotX - 5, dayY + 18);

            index++;
        }

        g2.setColor(Color.GRAY);
        g2.drawLine(30, dayY, getWidth() - 20, dayY);
    }
}

// QUẢN LÝ TÀI KHOẢN
class QuanLyTaiKhoan extends JFrame {

    DefaultTableModel model;
    JTable table;

    JTextField txtTenDangNhap = new JTextField();
    JPasswordField txtMatKhauCu = new JPasswordField();
    JPasswordField txtMatKhauMoi = new JPasswordField();
    JPasswordField txtXacNhan = new JPasswordField();

    Color xanhDam = new Color(25, 55, 109);
    Color xanh = new Color(42, 112, 210);

    QuanLyTaiKhoan() {
        setTitle("QUẢN LÝ TÀI KHOẢN");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(new Color(242, 246, 252));

        JLabel title = new JLabel(
            "QUẢN LÝ TÀI KHOẢN HỆ THỐNG",
            SwingConstants.CENTER
        );
        title.setFont(new Font("Segoe UI", Font.BOLD, 23));
        title.setForeground(xanhDam);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        add(title, BorderLayout.NORTH);

        // BẢNG TÀI KHOẢN
        String[] columns = {"ID", "Tên đăng nhập", "Vai trò"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(xanhDam);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 35));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        add(scroll, BorderLayout.CENTER);

        // NÚT QUẢN LÝ
        JPanel nutPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 10)
        );
        nutPanel.setBackground(new Color(242, 246, 252));

        JButton btnTaiLai = taoNut("TẢI LẠI", xanh);
        JButton btnDoiQuyen = taoNut(
            "ĐỔI QUYỀN", new Color(30, 145, 120)
        );
        JButton btnXoa = taoNut(
            "XÓA TÀI KHOẢN", new Color(210, 65, 65)
        );
        JButton btnDoiMatKhau = taoNut(
            "ĐỔI MẬT KHẨU", new Color(120, 90, 180)
        );

        nutPanel.add(btnTaiLai);
        nutPanel.add(btnDoiQuyen);
        nutPanel.add(btnXoa);
        nutPanel.add(btnDoiMatKhau);

        add(nutPanel, BorderLayout.SOUTH);

        btnTaiLai.addActionListener(e -> taiTaiKhoan());
        btnDoiQuyen.addActionListener(e -> doiQuyen());
        btnXoa.addActionListener(e -> xoaTaiKhoan());
        btnDoiMatKhau.addActionListener(e -> hienThiDoiMatKhau());

        taiTaiKhoan();
    }

    JButton taoNut(String ten, Color mau) {
        JButton nut = new JButton(ten);
        nut.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nut.setForeground(Color.WHITE);
        nut.setBackground(mau);
        nut.setFocusPainted(false);
        nut.setBorderPainted(false);
        nut.setPreferredSize(new Dimension(150, 40));
        nut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return nut;
    }

    void taiTaiKhoan() {
        model.setRowCount(0);

        String sql = "SELECT id, tenDangNhap, vaiTro FROM taikhoan";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("id"),
                    rs.getString("tenDangNhap"),
                    rs.getString("vaiTro")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Không thể tải danh sách tài khoản!\n" + e.getMessage()
            );
        }
    }

    void doiQuyen() {
        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(
                this, "Hãy chọn tài khoản cần đổi quyền!"
            );
            return;
        }

        String ten = model.getValueAt(row, 1).toString();
        String quyen = model.getValueAt(row, 2).toString();

        if (ten.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(
                this, "Không thể đổi quyền tài khoản admin!"
            );
            return;
        }

        String quyenMoi = quyen.equalsIgnoreCase("admin")
                          ? "user" : "admin";

        int chon = JOptionPane.showConfirmDialog(
            this,
            "Bạn có muốn đổi quyền tài khoản " + ten +
            " thành " + quyenMoi + " không?",
            "XÁC NHẬN ĐỔI QUYỀN",
            JOptionPane.YES_NO_OPTION
        );

        if (chon != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "UPDATE taikhoan SET vaiTro=? WHERE tenDangNhap=?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, quyenMoi);
            ps.setString(2, ten);

            int ketQua = ps.executeUpdate();

            if (ketQua > 0) {
                JOptionPane.showMessageDialog(
                    this, "Đổi quyền thành công!"
                );
                taiTaiKhoan();
            } else {
                JOptionPane.showMessageDialog(
                    this, "Không thể đổi quyền!"
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this, "Lỗi đổi quyền: " + e.getMessage()
            );
        }
    }

    void xoaTaiKhoan() {
        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(
                this, "Hãy chọn tài khoản cần xóa!"
            );
            return;
        }

        String ten = model.getValueAt(row, 1).toString();

        if (ten.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(
                this, "Không thể xóa tài khoản admin!"
            );
            return;
        }

        int chon = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn xóa tài khoản " + ten + "?",
            "XÁC NHẬN XÓA",
            JOptionPane.YES_NO_OPTION
        );

        if (chon != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM taikhoan WHERE tenDangNhap=?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, ten);

            int ketQua = ps.executeUpdate();

            if (ketQua > 0) {
                JOptionPane.showMessageDialog(
                    this, "Xóa tài khoản thành công!"
                );
                taiTaiKhoan();
            } else {
                JOptionPane.showMessageDialog(
                    this, "Xóa tài khoản thất bại!"
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this, "Lỗi xóa tài khoản: " + e.getMessage()
            );
        }
    }

    void hienThiDoiMatKhau() {
        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(
                this, "Hãy chọn tài khoản cần đổi mật khẩu!"
            );
            return;
        }

        String ten = model.getValueAt(row, 1).toString();

        JTextField txtTen = new JTextField(ten);
        txtTen.setEditable(false);

        JPasswordField matKhauCu = new JPasswordField();
        JPasswordField matKhauMoi = new JPasswordField();
        JPasswordField xacNhan = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Tên đăng nhập:"));
        panel.add(txtTen);

        panel.add(new JLabel("Mật khẩu hiện tại:"));
        panel.add(matKhauCu);

        panel.add(new JLabel("Mật khẩu mới:"));
        panel.add(matKhauMoi);

        panel.add(new JLabel("Xác nhận mật khẩu mới:"));
        panel.add(xacNhan);

        int chon = JOptionPane.showConfirmDialog(
            this,
            panel,
            "ĐỔI MẬT KHẨU",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (chon != JOptionPane.OK_OPTION) {
            return;
        }

        String cu = new String(matKhauCu.getPassword());
        String moi = new String(matKhauMoi.getPassword());
        String xacNhanMK = new String(xacNhan.getPassword());

        if (cu.isEmpty() || moi.isEmpty() || xacNhanMK.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Vui lòng nhập đầy đủ thông tin!"
            );
            return;
        }

        if (moi.length() < 6) {
            JOptionPane.showMessageDialog(
                this, "Mật khẩu mới phải có ít nhất 6 ký tự!"
            );
            return;
        }

        if (!moi.equals(xacNhanMK)) {
            JOptionPane.showMessageDialog(
                this, "Mật khẩu xác nhận không khớp!"
            );
            return;
        }

        String sql =
            "UPDATE taikhoan SET matKhau=? " +
            "WHERE tenDangNhap=? AND matKhau=?";

        try (
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, moi);
            ps.setString(2, ten);
            ps.setString(3, cu);

            int ketQua = ps.executeUpdate();

            if (ketQua > 0) {
                JOptionPane.showMessageDialog(
                    this, "Đổi mật khẩu thành công!"
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Mật khẩu hiện tại không chính xác!"
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this, "Lỗi đổi mật khẩu: " + e.getMessage()
            );
        }
    }
}
