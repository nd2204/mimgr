package dev.mimgr;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import dev.mimgr.db.DBConnection;
import dev.mimgr.theme.builtin.ColorScheme;

public class ProductDashboard extends JPanel implements ActionListener {

    JTextField tf_username;
    JPasswordField pf_password;
    ColorScheme m_colors;
    JLabel form_label;
    JButton login_button;
    JButton signup_button;
    JCheckBox remember;
    String data[][];
    Connection m_connection;

    private final String username_placeholder = "Tên người dùng";
    private final String password_placeholder = "Mật khẩu";

    @SuppressWarnings("deprecation")
    ProductDashboard(ColorScheme colors) {
        m_connection = DBConnection.get_instance().get_connection();
        Statement statement;
        try {
            statement = m_connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, // Cho phép cuộn qua kết quả
                    ResultSet.CONCUR_READ_ONLY // Không cho phép chỉnh sửa trực tiếp
            );
            // Câu truy vấn SQL
            String sql = "SELECT * FROM products";

            // Thực thi câu truy vấn và lấy kết quả
            ResultSet resultSet = statement.executeQuery(sql);

            // Lấy metadata để biết số cột
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Tạo tên cột
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Tạo dữ liệu cho bảng
            resultSet.last(); // Chuyển con trỏ đến dòng cuối
            int rowCount = resultSet.getRow(); // Lấy số hàng
            resultSet.beforeFirst(); // Quay về đầu bảng
            Object[][] data = new Object[rowCount][columnCount];
            int rowIndex = 0;
            while (resultSet.next()) {
                for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
                    if (metaData.getColumnName(colIndex).equals("image_url")) {
                        // // Load the image from the URL
                        // try {
                        //     // ImageIcon imageIcon;
                        //     // JLabel imageLabel = "";
                        //     // imageIcon = new ImageIcon(new URL(String.valueOf(resultSet.getObject(colIndex))));
                        //     // // Resize the image to fit 50x50
                        //     // Image resizedImage = imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);

                        //     // // Set the image in the JLabel
                        //     // imageLabel.setIcon(new ImageIcon(resizedImage));
                        //     // imageLabel.setText("");  // Clear any placeholder text
                        //     // URL url = new URL(String.valueOf(resultSet.getObject(colIndex)));
                        //     // Image image = new ImageIcon(url).getImage();
                        //     // // Resize the image to fit within the cell if needed
                        //     // Image resizedImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                        // } catch (MalformedURLException ex) {
                        // }
                        data[rowIndex][colIndex - 1] = loadImageFromUrl(String.valueOf(resultSet.getObject(colIndex)));

                    }
                    else {
                        data[rowIndex][colIndex - 1] = resultSet.getObject(colIndex);
                    }
                }
                rowIndex++;
            }

            // Tạo JTable với dữ liệu từ MySQL
            JTable table = new JTable(data, columnNames);
            table.setPreferredScrollableViewportSize(new Dimension(800, 500));
            table.setRowHeight(30);
            resizeColumnWidth(table);

            // Đặt JTable vào JScrollPane
            JScrollPane scrollPane = new JScrollPane(table);
            this.add(scrollPane);
            this.setVisible(false);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setup_form_style() {

    }

    // Phương thức tính toán kích thước cột dựa trên nội dung
    public static void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 100; // Giá trị khởi tạo độ rộng tối thiểu
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            // Đặt chiều rộng cột lớn nhất mà không vượt quá 300
            if (width > 300) {
                width = 300;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    // Method to load image from URL and convert it to an ImageIcon
    @SuppressWarnings("deprecation")
    public static ImageIcon loadImageFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            Image image = new ImageIcon(url).getImage();
            // Resize the image to fit within the cell if needed
            Image resizedImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
