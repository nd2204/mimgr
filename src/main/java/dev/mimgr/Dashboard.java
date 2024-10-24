package dev.mimgr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dev.mimgr.custom.GradientPanel;
import dev.mimgr.theme.builtin.ColorScheme;

public class Dashboard extends JPanel {
  Dashboard(ColorScheme colors) {
    this.setLayout(new BorderLayout());

    // Header
    JPanel headerPanel = new JPanel();
    headerPanel.setBackground(Color.LIGHT_GRAY);
    headerPanel.setPreferredSize(new Dimension(this.getWidth(), 60));
    this.add(headerPanel, BorderLayout.NORTH);

    // Sidebar
    SidebarPanel sidebarPanel = new SidebarPanel(colors);
    sidebarPanel.setPreferredSize(new Dimension(300, this.getHeight()));
    this.add(sidebarPanel, BorderLayout.WEST);

    // Content Panel
    JPanel contentPanel = new JPanel();
    contentPanel.setBackground(Color.RED);
    JLabel contentLabel = new JLabel("Main Content Area");
    contentPanel.add(contentLabel, BorderLayout.CENTER);
    this.add(contentPanel, BorderLayout.CENTER);
    // JButton button = new JButton("5");  
    // c.fill = GridBagConstraints.HORIZONTAL;  
    // c.ipady = 0;       //reset to default  
    // c.weighty = 1.0;   //request any extra vertical space  
    // c.anchor = GridBagConstraints.PAGE_END; //bottom of space  
    // c.insets = new Insets(10,0,0,0);  //top padding  
    // c.gridx = 1;       //aligned with button 2  
    // c.gridwidth = 2;   //2 columns wide  
    // c.gridy = 2;       //third row
    // this.add(button, c);
  }
}
