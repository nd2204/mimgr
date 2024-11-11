package dev.mimgr;

import java.util.ArrayList;
import java.util.function.BiConsumer;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import dev.mimgr.custom.*;
import dev.mimgr.theme.builtin.ColorScheme;

public class TableView {
  public static BiConsumer<MTable, Integer> setup_checkbox_column(ColorScheme colors) {
    return (table, colIndex) -> {
      TableColumn column = table.getColumnModel().getColumn(colIndex);
      MCheckBoxHeader checkBoxHeader = new MCheckBoxHeader(colors, table, colIndex);
      checkBoxHeader.getCheckBoxComponent().setCheckColor(colors.m_green);
      checkBoxHeader.getCheckBoxComponent().setBoxColor(colors.m_bg_3);
      checkBoxHeader.getCheckBoxComponent().setBoxSelectedColor(colors.m_green);
      checkBoxHeader.getCheckBoxComponent().setBackground(colors.m_bg_dim);
      MCheckBoxCellRenderer checkBoxRenderer = new MCheckBoxCellRenderer(colors);
      checkBoxRenderer.setCheckColor(colors.m_green);
      checkBoxRenderer.setBoxSelectedColor(colors.m_green);
      checkBoxRenderer.setBoxColor(colors.m_bg_3);
      checkBoxRenderer.setBackground(colors.m_bg_0);

      MCheckBoxCellEditor.CustomCheckBox editorCheckbox = new MCheckBoxCellEditor.CustomCheckBox(checkBoxRenderer, colors);
      MCheckBoxCellEditor checkBoxCellEditor = new MCheckBoxCellEditor(editorCheckbox, colors);
      column.setHeaderRenderer(checkBoxHeader);
      column.setCellRenderer(checkBoxRenderer);
      column.setCellEditor(checkBoxCellEditor);
      column.setPreferredWidth(50);
      column.setMaxWidth(50);
      column.setMinWidth(50);
    };
  }

  public static BiConsumer<MTable, Integer> setup_image_column(ColorScheme colors) {
    return (table, colIndex) -> {
      TableColumn column = table.getColumnModel().getColumn(colIndex);
      column.setMinWidth(80);
      column.setPreferredWidth(80);
      column.setMaxWidth(100);
      column.setCellRenderer(new MImageCellRenderer(colors));
    };
  }

  public static BiConsumer<MTable, Integer> setup_status_column(ColorScheme colors) {
    return (table, colIndex) -> {
      TableColumn column = table.getColumnModel().getColumn(colIndex);
      column.setMinWidth(150);
      column.setMaxWidth(200);
      column.setPreferredWidth(150);
      column.setCellRenderer(new MStatusCellRenderer(colors));
    };
  }


  public static BiConsumer<MTable, Integer> setup_default_column() {
    return (table, colIndex) -> {
      TableColumn column = table.getColumnModel().getColumn(colIndex);
      column.setPreferredWidth(150);
    };
  }

  public static BiConsumer<MTable, Integer> setup_custom_column(int min, int prefered, int max) {
    return (table, colIndex) -> {
      TableColumn column = table.getColumnModel().getColumn(colIndex);
      column.setMinWidth(min);
      column.setMaxWidth(max);
      column.setPreferredWidth(prefered);
    };
  }

  public void add_column(MTable table, String colName, BiConsumer<MTable, Integer> setup_column) {
    ColumnInfo columnInfo = new ColumnInfo();
    columnInfo.colIndex = columnInfos.size();
    columnInfo.colName = colName;
    columnInfo.colSetupFunction = setup_column;
    columnInfos.add(columnInfo);
  }

  public void load_column(MTable table, DefaultTableModel model) {
    for (ColumnInfo ci : columnInfos) {
      model.addColumn(ci.colName);
    }
    for (ColumnInfo ci : columnInfos) {
      ci.colSetupFunction.accept(table, ci.colIndex);
    }
  }

  private ArrayList<ColumnInfo> columnInfos = new ArrayList<>();

  private class ColumnInfo {
    public int colIndex;
    public String colName;
    public BiConsumer<MTable, Integer> colSetupFunction;
  }
}
