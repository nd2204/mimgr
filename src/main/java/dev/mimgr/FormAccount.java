package dev.mimgr;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MPasswordField;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.UserRecord;
import dev.mimgr.theme.builtin.ColorScheme;

public class FormAccount extends JPanel {
  FormAccount(ColorScheme colors) {
    this.colors = colors;
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBackground(colors.m_bg_dim);
    this.setup_form_style();

    this.setPreferredSize(new Dimension(1000, this.getPreferredSize().height));
    this.setMaximumSize(new Dimension(1000, Integer.MAX_VALUE));

    MScrollPane scrollPane = new MScrollPane(colors);
    scrollPane.getVerticalScrollBar().setUnitIncrement(100);
    this.add(scrollPane);

    scrollPane.add(thisPanel);
    scrollPane.setViewportView(thisPanel);

    thisPanel.setLayout(new GridBagLayout());
    thisPanel.setBackground(colors.m_bg_dim);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    lblAccount.setFont(nunito_bold_20);
    lblAccount.setForeground(colors.m_fg_0);
    gbc.insets = new Insets(40, 10, 20, 10);
    gbc.gridx = 0;
    gbc.gridy = 0;
    thisPanel.add(lblAccount, gbc);

    gbc.insets = new Insets(0, 10, 10, 10);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    thisPanel.add(new UsernameAndPasswordPanel(), gbc);

    gbc.insets = new Insets(0, 5, 40, 10);
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    thisPanel.add(btnSubmit, gbc);
  }

  public void setup_form_style() {
    // ========================= Fields =========================
    Consumer<MTextField> setup_common_textfield = (tf) -> {
      tf.setInputForeground(colors.m_fg_0);
      tf.setBackground(colors.m_bg_dim);
      tf.setBorderColor(colors.m_bg_4);
      tf.setFocusBorderColor(colors.m_blue);
      tf.setForeground(colors.m_fg_0);
      tf.setBorderWidth(2);
      tf.setFont(nunito_bold_14);
    };

    tfUsername = new MTextField(20);
    setup_common_textfield.accept(tfUsername);

    taPassword = new MPasswordField();
    taPassword.setInputForeground(colors.m_fg_0);
    taPassword.setBackground(colors.m_bg_dim);
    taPassword.setBorderColor(colors.m_bg_4);
    taPassword.setFocusBorderColor(colors.m_blue);
    taPassword.setForeground(colors.m_fg_0);
    taPassword.setBorderWidth(2);
    taPassword.setBorderRadius(15);
    taPassword.setFont(nunito_bold_14);

    cbRole = new MComboBox<>(colors);
    cbRole.getEditor().getEditorComponent().setFont(nunito_bold_14);

    // ========================= Buttons =========================
    this.btnSubmit = new MButton("Submit");
    this.btnSubmit.setFont(nunito_bold_14);
    this.btnSubmit.setBackground(colors.m_blue);
    this.btnSubmit.setBorderColor(colors.m_blue);
    this.btnSubmit.setForeground(colors.m_fg_1);
    this.btnSubmit.setBorderWidth(2);
    this.btnSubmit.setEnabled(true);
  }

  private class UsernameAndPasswordPanel extends RoundedPanel {
    public UsernameAndPasswordPanel() {
      this.setBorderColor(colors.m_bg_2);
      this.setBorderRadius(15);
      this.setBackground(colors.m_bg_0);
      this.setLayout(new GridBagLayout());
      GridBagConstraints gc = new GridBagConstraints();

      setLabelStyle(lblUsername);
      setLabelStyle(lblPassword);
      setLabelStyle(lblRole);

      gc.fill = GridBagConstraints.HORIZONTAL;
      gc.weightx = 1.0;

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 0;
      gc.gridy = 0;
      gc.insets = new Insets(20, 25, 5, 20);
      this.add(lblUsername, gc);

      gc.gridx = 0;
      gc.gridy = 1;
      gc.insets = new Insets(0, 20, 10, 20);
      this.add(tfUsername, gc);

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 1;
      gc.gridy = 0;
      gc.insets = new Insets(20, 25, 5, 20);
      this.add(lblRole, gc);

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 1;
      gc.gridy = 1;
      gc.insets = new Insets(0, 20, 10, 20);
      gc.weighty = 1.0;
      gc.fill = GridBagConstraints.BOTH;
      this.add(cbRole, gc);

      gc.gridwidth = 2;
      gc.gridx = 0;
      gc.gridy = 2;
      gc.insets = new Insets(5, 25, 5, 20);
      this.add(lblPassword, gc);
      gc.gridx = 0;
      gc.gridy = 3;
      gc.insets = new Insets(0, 20, 20, 20);
      this.add(taPassword, gc);
    }

    private void setLabelStyle(JLabel lbl) {
      lbl.setForeground(colors.m_grey_1);
      lbl.setFont(nunito_bold_14);
    }

    private JLabel lblUsername = new JLabel("Username");
    private JLabel lblPassword = new JLabel("Password");
    private JLabel lblRole = new JLabel("Role");
  }

  // Declare form components
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  private JPanel thisPanel = new JPanel();
  private JLabel lblAccount = new JLabel("Profile");
  private MTextField tfUsername;
  private MComboBox<UserRecord> cbRole;
  private MPasswordField taPassword;
  private MButton btnSubmit;
  private ColorScheme colors;
}
