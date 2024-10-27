package dev.mimgr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.mimgr.db.DBConnection;

public class UploadService {
    static Connection     m_connection = DBConnection.get_instance().get_connection();
    public static String post_instrument(String name, double price, String description, int stock_quantity, int category_id, String image_url) {
    String insertSQL = "INSERT INTO products (name, price, description, stock_quantity, category_id, image_url) VALUES (?, ?, ?, ?, ?, ?)";
    try {
        PreparedStatement pstmt = m_connection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
        // Setting the values in the PreparedStatement
        pstmt.setString(1, name);
        pstmt.setDouble(2, price);
        pstmt.setString(3, description);
        pstmt.setInt(4, stock_quantity);
        pstmt.setInt(5, category_id);
        pstmt.setString(6, image_url);          
        // Execute the update
        int rowsAffected = pstmt.executeUpdate();
        
        // Return success message if rows are affected
        if (rowsAffected > 0) {
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                long generatedId = generatedKeys.getLong(1);  // Get the ID of the inserted row
                ImageDropPanel.saveImageToFile(image_url, String.valueOf(generatedId));
                System.out.println("Inserted product ID: " + generatedId);
            }
            return "Product uploaded successfully!";
        } else {
            return "Failed to upload Product.";
        }

    } catch (SQLException e) {
        // Handle SQL errors
        e.printStackTrace();
        return "An error occurred while uploading the Product: " + e.getMessage();
    }
  }
}