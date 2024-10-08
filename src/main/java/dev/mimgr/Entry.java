package dev.mimgr;
// import java.awt.Button;
// import java.awt.Dimension;
// import java.awt.Frame;
// import java.awt.Label;
// import java.awt.Panel;
// import java.awt.TextField;
// import java.awt.Toolkit;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dev.mimgr.db.MySQLCon;
//create CreateLoginForm class to create login form  
//class extends JFrame to create a window where our component add  
//class implements ActionListener to perform an action on button click  

class CreateLoginForm extends JFrame implements ActionListener {
    Connection connection = null;

    //initialize button, panel, label, and text field  
    JButton b1;
    JPanel newPanel;
    JLabel userLabel, passLabel;
    final JTextField textField1, textField2;

    //calling constructor  
    CreateLoginForm() {
        String DB_URL = "jdbc:mysql://127.0.0.1:3306/admin";
        String userDB = "mimgr";
        String passwordDB = "mimgr";
        connection = new MySQLCon(DB_URL, userDB, passwordDB).get_connection();

        //create label for username   
        userLabel = new JLabel();
        userLabel.setText("Username");      //set label value for textField1  

        //create text field to get username from the user  
        textField1 = new JTextField(15);    //set length of the text  

        //create label for password  
        passLabel = new JLabel();
        passLabel.setText("Password");      //set label value for textField2  

        //create text field to get password from the user  
        textField2 = new JPasswordField(15);    //set length for the password  

        //create submit button  
        b1 = new JButton("SUBMIT"); //set label to button  

        //create panel to put form elements  
        newPanel = new JPanel(new GridLayout(3, 1));
        newPanel.add(userLabel);    //set username label to panel  
        newPanel.add(textField1);   //set text field to panel  
        newPanel.add(passLabel);    //set password label to panel  
        newPanel.add(textField2);   //set text field to panel  
        newPanel.add(b1);           //set button to panel  

        //set border to panel   
        add(newPanel, BorderLayout.CENTER);

        //perform action on button click   
        b1.addActionListener(this);     //add action listener to button  
        setTitle("LOGIN FORM");         //set title to the login form  
    }

    //define abstract method actionPerformed() which will be called on button click   
    public void actionPerformed(ActionEvent ae) //pass action listener as a parameter  
    {
        String userValue = textField1.getText();        //get user entered username from the textField1  
        String passValue = textField2.getText();        //get user entered pasword from the textField2  

        String sql = "select username from Users where username = '" + userValue + "' and password = '" + passValue + "'";
        try {
            // Connection connection = mySQLCon.get_connection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                Dashboard page = new Dashboard();
                //make page visible to the user  
                page.setVisible(true);
            } else {
                System.out.println("Please enter valid username and password");
            }
        } catch (Exception erException) {
            erException.printStackTrace();
        }
    }
}
//create the main class  

class Entry {
  //main() method start  

  static final WindowListener on_close_handler() {
    return new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    };
  }

  public static void main(String arg[]) {
    try {
      //create instance of the CreateLoginForm  
      CreateLoginForm form = new CreateLoginForm();

      form.setSize(300, 100);  //set size of the frame  
      form.setVisible(true);  //make form visible to the user  
      form.addWindowListener(on_close_handler());

    } catch (Exception e) {
      //handle exception   
      JOptionPane.showMessageDialog(null, e.getMessage());
    }
  }
}
