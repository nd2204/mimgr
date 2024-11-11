package dev.mimgr.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import dev.mimgr.FontManager;
import dev.mimgr.IconManager;
import dev.mimgr.custom.DropContainerPanel;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.db.ImageRecord;
import dev.mimgr.theme.builtin.ColorScheme;

public class MediaGridView extends JPanel implements IMediaView {
  public MediaGridView(ColorScheme colors) {
    this.colors = colors;
    initializeUI();
  }

  private void initializeUI() {
    // Set up the main image panel with BoxLayout (vertical)
    pnImageBtns = new JPanel();
    pnImageBtns.setBackground(colors.m_bg_0);
    pnImageBtns.setLayout(new BoxLayout(pnImageBtns, BoxLayout.Y_AXIS));
    pnImageBtns.setBorder(new EmptyBorder(0, 5, 0, 20));

    optionBar = new OptionBar();
    // Wrap the image panel in a scroll pane for vertical scrolling
    scrollPane = new MScrollPane(colors);
    scrollPane.add(pnImageBtns);
    scrollPane.setViewportView(pnImageBtns);
    scrollPane.setOpaque(true);
    scrollPane.setBackground(colors.m_bg_0);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
    add(optionBar, BorderLayout.NORTH);


    updateView(() -> ImageRecord.selectAll());
    // Adjust the number of images per row when window is resized
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent evt) {
        if (!buttonToImage.isEmpty()) {
          createImageGrid(); // Re-create the grid on resize
        }
      }
    });
  }

  @Override
  public void deleteSelectedImages() {
    for (ImageRecord ir : selectedImages.values()) {
      deleteImage(ir);
    }
    selectedImages.clear();
    lblSelectCount.setText("");
    refresh();
  }

  @Override
  public List<ImageRecord> getSelectedImages() {
    return new ArrayList<>(this.selectedImages.values());
  }

  @Override
  public void refresh() {
    updateGrid(currentQueryInvoker);
  }

  @Override
  public void updateView(Supplier<ResultSet> queryInvoker) {
    updateGrid(queryInvoker);
  }

  @Override
  public void reset() {
    updateGrid(() -> ImageRecord.selectAll());
  }

  @Override
  public Supplier<ResultSet> getCurrentQueryInvoker() {
    return this.currentQueryInvoker;
  }

  public void updateGrid(Supplier<ResultSet> queryInvoker) {
    buttonToImage.clear();
    buttons.clear();
    MButton button = null;
    ResultSet queryResult = queryInvoker.get();
    currentQueryInvoker = queryInvoker;
    try {
      while (queryResult.next()) {
        ImageRecord ir = new ImageRecord(queryResult);
        button = DropContainerPanel.createButton(IconManager.loadIcon(
          Paths.get(ir.m_url).toAbsolutePath().toFile()
        ));
        button.setBackground(colors.m_bg_dim);
        button.setForeground(colors.m_fg_0);
        button.setHoverBorderColor(colors.m_blue);
        button.setBorderColor(colors.m_bg_5);
        button.setBorderWidth(2);
        button.addActionListener(buttonListener);
        buttonToImage.put(button, ir);
        buttons.add(button);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    createImageGrid();
  }

  public void setButtonSelected(MButton button, boolean isSelected) {
    if (isSelected) {
      button.setBorderColor(colors.m_green);
      selectedImages.put(button, buttonToImage.get(button));
    } else {
      button.setBorderColor(colors.m_bg_5);
      selectedImages.remove(button);
    }
    int count = selectedImages.size();
    if (count <= 0) {
      lblSelectCount.setText("");
    } else {
      lblSelectCount.setText(count + " Selected");
    }
  }

  private void createImageGrid() {
    pnImageBtns.removeAll();

    int panelWidth = scrollPane.getViewport().getWidth();
    int imagesPerRow = Math.max(1, panelWidth / (imageSize + 10)); // 10px margin between images

    JPanel rowPanel = null;
    for (int i = 0; i < buttons.size(); i++) {
      if (i % imagesPerRow == 0) {
        // Start a new row
        rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        rowPanel.setPreferredSize(new Dimension(scrollPane.getViewport().getWidth(), rowHeight));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));
        rowPanel.setMinimumSize(new Dimension(0, rowHeight));
        rowPanel.setOpaque(false);
        pnImageBtns.add(rowPanel);
      }

      rowPanel.add(buttons.get(i));
    }

    // Refresh panel to apply new layout
    pnImageBtns.revalidate();
    pnImageBtns.repaint();
  }


  private void deleteImage(ImageRecord ir) {
    ImageRecord.delete(ir.m_id);
    try {
      Files.deleteIfExists(Path.of(ir.m_url));
    } catch (IOException e) {}
  }

  private class ButtonListener implements ActionListener{
    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == btnSelectAll) {
        for (MButton button : buttons) {
          setButtonSelected(button, true);
        }
        return;
      }

      if (e.getSource() == btnClearAll) {
        clearSelectedButtons();
        return;
      }

      if (e.getSource() instanceof MButton button)  {
        setButtonSelected(button, !selectedImages.containsKey(button));
        return;
      }
    }
  }

  public class OptionBar extends JPanel {
    public OptionBar() {
      Init();
    }

    private void Init() {
      this.setLayout(new GridBagLayout());
      this.setPreferredSize(new Dimension(this.getPreferredSize().width, 60));
      this.setBackground(colors.m_bg_dim);

      lblSelectCount = new JLabel();
      lblSelectCount.setFont(FontManager.getFont("NunitoBold", 14f));
      lblSelectCount.setForeground(colors.m_grey_0);

      Consumer<MButton> setupButton = button -> {
        button.setBackground(colors.m_bg_1);
        button.setForeground(colors.m_grey_2);
        button.setBorderColor(colors.m_bg_3);
        button.setBackground(colors.m_bg_0);
        button.setHoverBorderColor(colors.m_bg_0);
        button.setHoverBackgroundColor(colors.m_bg_0);
        button.addActionListener(buttonListener);
      };

      btnSelectAll = new MButton("Select All");
      setupButton.accept(btnSelectAll);

      btnClearAll = new MButton("Clear All");
      setupButton.accept(btnClearAll);

      GridBagConstraints c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = 0;
      c.insets = new Insets(10, 15, 10, 15);
      this.add(btnSelectAll, c);

      c.gridx = 1;
      c.gridy = 0;
      c.insets = new Insets(10, 0, 10, 20);
      this.add(btnClearAll, c);

      c.gridx = 2;
      c.weightx = 1.0;
      this.add(Box.createHorizontalGlue(), c);

      c.gridx = 3;
      c.weightx = 0.0;
      this.add(lblSelectCount, c);
    }
  }

  public ImageRecord getAssociatedImageRecord(MButton button) {
    return buttonToImage.get(button);
  }

  public void clearButtonsActionListener() {
    buttonListener = null;
    refresh();
  }

  public void clearSelectedButtons() {
    for (MButton button : buttons) {
      setButtonSelected(button, false);
    }
  }

  public List<MButton> getButtons() {
    return this.buttons;
  }

  private Supplier<ResultSet> currentQueryInvoker;
  public Map<MButton, ImageRecord> selectedImages = new HashMap<>();
  private Map<MButton, ImageRecord> buttonToImage = new HashMap<>();
  public List<MButton> buttons = new ArrayList<>();
  public OptionBar optionBar;

  private MButton btnSelectAll;
  private MButton btnClearAll;

  private JLabel lblSelectCount;
  private ActionListener buttonListener = new ButtonListener();
  private ColorScheme colors;
  private JPanel pnImageBtns;
  private MScrollPane scrollPane;
  private final int imageSize = 100; // Fixed size for images
  private final int rowHeight = 110;  // Fixed height for each row
}

