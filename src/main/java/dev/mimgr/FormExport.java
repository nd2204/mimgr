package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit; import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import dev.mimgr.component.ConfirmPopup;
import dev.mimgr.component.NotificationPopup;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.db.CategoryRecord;
import dev.mimgr.db.DBConnection;
import dev.mimgr.db.ProductRecord;
import dev.mimgr.db.QueryBuilder;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.Helpers;
import dev.mimgr.utils.ResourceManager;

public class FormExport extends JFrame {
  private double m_aspect_ratio;
  private int m_width;
  private int m_height;

  public MButton getAddProductSubmitButton() {
    return this.btnSubmit;
  }

  public FormExport() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    m_aspect_ratio = 16.0f / 10.0f;
    m_width = 1280;
    m_height = (int) ((float) m_width / m_aspect_ratio);

    // Get the screen size
    Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();

    UploadButtonListener uploadButtonListener = new UploadButtonListener();
    // Main window
    ProductExportPanel = new ProductExportPanel();
    btnSubmit = ProductExportPanel.getSubmitComponent();
    btnSubmit.addActionListener(uploadButtonListener);
    btnDelete = ProductExportPanel.getDeleteComponent();
    btnDelete.addActionListener(uploadButtonListener);

    // category options
    cbCategory = ProductExportPanel.getCategoryComponent();
    cbCategoryTextField = (JTextField) cbCategory.getEditor().getEditorComponent();
    cbCategoryTextField.setEditable(false);
    cbCategoryTextField.setText("Export all categories");
    cbCategoryTextField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        checkFields();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        checkFields();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        checkFields();
      }

      void checkFields() {
        if (!cbCategoryTextField.getText().equals("Export all categories")) {
          ProductExportPanel.getOptionsCategorySelectedPanel().addData(cbCategory.getSelectedItem());
        }
      }
    });
    cbCategoryTextField.addFocusListener(new TextFieldFocusListener("Export all categories", cbCategoryTextField));
    ProductExportPanel.getOptionsCategorySelectedPanel().addActionListener((actionEvent) -> {
      new Thread(() -> {
        SwingUtilities.invokeLater(() -> {
          if (ProductExportPanel.getOptionsCategorySelectedPanel().isEmpty()) {
            cbCategoryTextField.setText("Export all categories");
            ProductExportPanel.getOptionsCategorySelectedPanel().clearData();
          }
        });
      }).start();
    });

    // column options
    cbColumn = ProductExportPanel.getProductComponent();
    cbProductTextField = (JTextField) cbColumn.getEditor().getEditorComponent();
    cbProductTextField.setEditable(false);
    cbProductTextField.setText("Export all columns");
    cbProductTextField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        checkFields();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        checkFields();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        checkFields();
      }

      void checkFields() {
        if (!cbProductTextField.getText().equals("Export all columns")) {
          ProductExportPanel.getOptionsProductSelectedPanel().addData(cbColumn.getSelectedItem());
        }
      }
    });
    ;
    cbProductTextField.addFocusListener(new TextFieldFocusListener("Export all columns", cbProductTextField));
    ProductExportPanel.getOptionsProductSelectedPanel().addActionListener((actionEvent) -> {
      new Thread(() -> {
        SwingUtilities.invokeLater(() -> {
          if (ProductExportPanel.getOptionsProductSelectedPanel().isEmpty()) {
            cbProductTextField.setText("Export all columns");
            ProductExportPanel.getOptionsProductSelectedPanel().clearData();
          }
        });
      }).start();
    });

    this.setLayout(new BorderLayout());
    this.setMinimumSize(new Dimension(854, 600));
    this.setTitle("Export to CSV file");
    this.setSize(854, m_height);
    this.add(ProductExportPanel);
    this.setLocation(
        (screen_size.width - this.getWidth()) / 2,
        (screen_size.height - this.getHeight()) / 2);
    this.setVisible(true);
    this.requestFocus();
  }

  private class UploadButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == btnDelete) {
        FormExport.this.dispose();
        return;
      }

      if (e.getSource() == btnSubmit) {
        ArrayList<Integer> cr_ids = new ArrayList<>();
        ArrayList<String> pr_columns = new ArrayList<>();
        // retrieve data
        if (!ProductExportPanel.getOptionsCategorySelectedPanel().isEmpty()) {
          ArrayList<Object> datalist = new ArrayList<>();
          datalist = ProductExportPanel.getOptionsCategorySelectedPanel().getAllData();
          for (Object data : datalist) {
            if (data instanceof CategoryRecord cr) {
              cr_ids.add(cr.m_id);
            }
          }

        } else {
          try (ResultSet rs = CategoryRecord.selectAll()) {
            while (rs.next()) {
              cr_ids.add(rs.getInt("category_id"));
            }
          } catch (SQLException ex) {
            ex.printStackTrace();
          }
        }

        if (!ProductExportPanel.getOptionsProductSelectedPanel().isEmpty()) {
          ArrayList<Object> columnlist = new ArrayList<>();
          columnlist = ProductExportPanel.getOptionsProductSelectedPanel().getAllData();
          for (Object column : columnlist) {
            if (column instanceof String pr) {
              pr_columns.add(pr);
            }
          }
        } else {
          try (ResultSet rs = ProductRecord.selectColumnsName()) {
            while (rs.next()) {
              pr_columns.add(rs.getString("COLUMN_NAME"));
            }
          } catch (SQLException ex) {
            ex.printStackTrace();
          }
        }

        // Build the query with placeholders
        QueryBuilder queryBuilder = new QueryBuilder();
        StringBuilder query = new StringBuilder(
          queryBuilder
            .select("*")
            .from(ProductRecord.TABLE)
            .join("INNER JOIN categories ON products.category_id = categories.category_id")
            .where("products.category_id IN ")
            .build()
        );

        query.append(QueryBuilder.buildList(cr_ids));
        query.append(" OR categories.parent_id IN ");
        query.append(QueryBuilder.buildList(cr_ids));

        File csvFile = new File("export_" + Helpers.getCurrentTimestamp() + ".csv");
        Path csvFilePath = ResourceManager.getInstance().moveStagedFileToEpxortDir(csvFile);

        // Prepare the statement
        try (PreparedStatement statement = DBConnection.getInstance().getConection()
            .prepareStatement(query.toString());
            BufferedWriter writer = new BufferedWriter(
                new FileWriter(csvFilePath.toString()))) {
          // Execute the query
          ResultSet resultSet = statement.executeQuery();
          for (int i = 0; i < pr_columns.size(); i++) {
            writer.write(pr_columns.get(i));
            if (i < pr_columns.size() - 1) {
              writer.write(",");
            }
          }
          writer.newLine();
          while (resultSet.next()) {
            for (String s : pr_columns) {
              String value = resultSet.getString(s);
              value = "\"" + value.replace("\"", "\"\"") + "\"";
              writer.write(value);
              if (s != pr_columns.getLast()) {
                writer.write(",");
              }
            }
            writer.newLine();
          }
          int response = JOptionPane.showConfirmDialog(FormExport.this, "Open folder?", "Confirm", JOptionPane.YES_NO_OPTION);
          if (response == JOptionPane.YES_OPTION) {
            Helpers.openFileExplorer(csvFilePath);
          }
          PanelManager.createPopup(new ConfirmPopup(
            "Saved",
            "Saved to: " + csvFilePath + "\nDo you want to open the folder"
          ));
          FormExport.this.dispose();
        } catch (SQLException ex) {
          System.err.println(query);
          System.err.println(ex);
        } catch (IOException ex) {
        }
      }
    }
  }

  private class TextFieldFocusListener extends FocusAdapter {
    TextFieldFocusListener(String text, JTextField textField) {
      placeholder = text;
      TextField = textField;
    }

    @Override
    public void focusLost(FocusEvent e) {
      if (TextField.getText().isEmpty()) {
        TextField.setText(placeholder);
      }
      repaint();
    }

    @Override
    public void focusGained(FocusEvent e) {
      if (TextField.getText().equals(placeholder)) {
        TextField.setText(placeholder);
      }
      repaint();
    }

    private String placeholder;
    private JTextField TextField;
  }

  public static void main(String arg[]) {
    new FormExport();
  }

  private ProductExportPanel ProductExportPanel;
  private MComboBox<CategoryRecord> cbCategory;
  private JTextField cbCategoryTextField;
  private MComboBox<String> cbColumn;
  private JTextField cbProductTextField;
  private MButton btnDelete;
  private MButton btnSubmit;
  private ColorScheme colors;
}
