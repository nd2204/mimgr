package dev.mimgr;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dev.mimgr.component.NotificationPopup;
import dev.mimgr.custom.MButton;
import dev.mimgr.custom.MComboBox;
import dev.mimgr.custom.MScrollPane;
import dev.mimgr.custom.MTextArea;
import dev.mimgr.custom.MTextField;
import dev.mimgr.custom.RoundedPanel;
import dev.mimgr.db.UserRecord;
import dev.mimgr.theme.ColorTheme;
import dev.mimgr.theme.builtin.ColorScheme;
import dev.mimgr.utils.Helpers;

public class FormAccount extends JPanel {
  public MButton getUpdateProfileButton() {
    return this.btnSubmit;
  }

  public String getUsername() {
    return this.tfUsername.getTextString();
  }

  public String getRole() {
    return (String) this.cbRole.getSelectedItem();
  }

  FormAccount() {
    this.colors = ColorTheme.getInstance().getCurrentScheme();
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBackground(colors.m_bg_dim);
    this.setup_form_style();

    this.setPreferredSize(new Dimension(1000, this.getPreferredSize().height));
    this.setMaximumSize(new Dimension(1000, Integer.MAX_VALUE));

    MScrollPane scrollPane = new MScrollPane();
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

    gbc.gridx = 0;
    gbc.gridy = 0;

    gbc.insets = new Insets(20, 0, 20, 15);
    thisPanel.add(Helpers.createHomeButton(), gbc);
    gbc.gridx++;

    gbc.insets = new Insets(24, 0, 20, 0);
    thisPanel.add(lblAccount, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(0, 0, 10, 0);

    thisPanel.add(new UsernameAndPasswordPanel(), gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    gbc.insets = new Insets(0, 0, 40, 0);
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
    tfUsername.setText(SessionManager.getCurrentUser().m_username);

    tfEmail = new MTextField(20);
    setup_common_textfield.accept(tfEmail);
    tfEmail.setText(SessionManager.getCurrentUser().m_email);

    tfNumber = new MTextField(20);
    setup_common_textfield.accept(tfNumber);
    tfNumber.setText(SessionManager.getCurrentUser().m_number);

    taBio = new MTextArea(8, 50);
    taBio.setPadding(new Insets(20, 20, 20, 20));
    taBio.setInputForeground(colors.m_fg_0);
    taBio.setBackground(colors.m_bg_dim);
    taBio.setBorderColor(colors.m_bg_4);
    taBio.setFocusBorderColor(colors.m_blue);
    taBio.setForeground(colors.m_fg_0);
    taBio.setBorderWidth(2);
    taBio.setBorderRadius(15);
    taBio.setFont(nunito_bold_14);
    taBio.setLineWrap(true);
    taBio.setText(SessionManager.getCurrentUser().m_bio);

    cbRole = new MComboBox<>(colors);
    for (String role : UserRecord.roles) {
      cbRole.addItem(role);
    }
    cbRole.getEditor().getEditorComponent().setFont(nunito_bold_14);
    cbRole.setSelectedItem(SessionManager.getCurrentUser().m_role);

    // ========================= Buttons =========================
    updateProfileButton UploadButtonListener = new updateProfileButton();
    this.btnSubmit = new MButton("Update Profile");
    this.btnSubmit.setFont(nunito_bold_14);
    this.btnSubmit.setBackground(colors.m_blue);
    this.btnSubmit.setBorderColor(colors.m_blue);
    this.btnSubmit.setForeground(colors.m_fg_1);
    this.btnSubmit.setBorderWidth(2);
    this.btnSubmit.setEnabled(true);
    this.btnSubmit.addActionListener(UploadButtonListener);
  }

  private class UsernameAndPasswordPanel extends RoundedPanel {
    public UsernameAndPasswordPanel() {
      this.setBorderColor(colors.m_bg_2);
      this.setBorderRadius(15);
      this.setBackground(colors.m_bg_0);
      this.setLayout(new GridBagLayout());
      GridBagConstraints gc = new GridBagConstraints();

      setLabelStyle(lblUsername);
      setLabelStyle(lblEmail);
      setLabelStyle(lblNumber);
      setLabelStyle(lblBio);
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

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 0;
      gc.gridy = 2;
      gc.insets = new Insets(20, 25, 5, 20);
      this.add(lblEmail, gc);

      gc.gridx = 0;
      gc.gridy = 3;
      gc.insets = new Insets(0, 20, 20, 20);
      this.add(tfEmail, gc);

      gc.anchor = GridBagConstraints.FIRST_LINE_START;
      gc.gridx = 1;
      gc.gridy = 2;
      gc.insets = new Insets(20, 25, 5, 20);
      this.add(lblNumber, gc);

      gc.gridx = 1;
      gc.gridy = 3;
      gc.insets = new Insets(0, 20, 20, 20);
      this.add(tfNumber, gc);

      gc.gridwidth = 2;
      gc.gridx = 0;
      gc.gridy = 4;
      gc.insets = new Insets(5, 25, 5, 20);
      this.add(lblBio, gc);
      gc.gridx = 0;
      gc.gridy = 5;
      gc.insets = new Insets(0, 20, 20, 20);
      this.add(taBio, gc);
    }

    private void setLabelStyle(JLabel lbl) {
      lbl.setForeground(colors.m_grey_1);
      lbl.setFont(nunito_bold_14);
    }

    private JLabel lblUsername = new JLabel("Username");
    private JLabel lblEmail = new JLabel("Email");
    private JLabel lblNumber = new JLabel("Number");
    private JLabel lblBio = new JLabel("Bio");
    private JLabel lblRole = new JLabel("Role");
  }

  private class updateProfileButton implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == btnSubmit) {
        String username = tfUsername.getTextString();
        String role = (String) cbRole.getSelectedItem();
        String email = tfEmail.getTextString();
        String number = tfNumber.getTextString();
        String bio = taBio.getTextString();
        int id = SessionManager.getCurrentUser().m_id;
        if (Arrays.asList(UserRecord.roles).contains(role)) {
          UserRecord.update(username, role, email, number, bio, id);
          PanelManager.createPopup(new NotificationPopup("Profile changed", NotificationPopup.NOTIFY_LEVEL_INFO, 8000));
        }
        else {
          System.out.println(role);
        }
      }
    }
  }

  // Declare form components
  private Font nunito_bold_14 = FontManager.getFont("NunitoBold", 14f);
  private Font nunito_bold_20 = FontManager.getFont("NunitoBold", 22f);

  private JPanel thisPanel = new JPanel();
  private JLabel lblAccount = new JLabel("Profile");
  private MTextField tfUsername;
  private MTextField tfEmail;
  private MTextField tfNumber;
  private MComboBox<String> cbRole;
  private MTextArea taBio;
  private MButton btnSubmit;
  private ColorScheme colors;
}
