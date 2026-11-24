
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        Connection conn = DatabaseConnection.getConnection();

        if (conn != null) {
            System.out.println("Chuong trinh da ket noi Database!");
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Khong the ket noi Database!");
        }
    }
}
