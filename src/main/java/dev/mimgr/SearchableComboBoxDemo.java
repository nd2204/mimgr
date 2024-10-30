package dev.mimgr;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SearchableComboBoxDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Searchable JComboBox Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 150);
            frame.setLayout(new FlowLayout());

            // Create the searchable combo box
            SearchableComboBox<String> comboBox = new SearchableComboBox<>(new String[]{
                    "Apple", "Banana", "Cherry", "Date", "Fig", "Grape", "Kiwi", "Mango", "Orange", "Papaya"
            });

            frame.add(comboBox);
            frame.setVisible(true);
        });
    }
}

class SearchableComboBox<E> extends JPanel {
    private JComboBox<E> comboBox;
    private JTextField textField;
    private List<E> items;
    private DefaultComboBoxModel<E> model;

    public SearchableComboBox(E[] items) {
        this.items = new ArrayList<>(List.of(items));

        // Set up the combo box model
        model = new DefaultComboBoxModel<>(items);
        comboBox = new JComboBox<>(model);
        comboBox.setEditable(true);

        // Get the editor component (text field)
        textField = (JTextField) comboBox.getEditor().getEditorComponent();

        // Filter items when typing
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterItems();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterItems();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterItems();
            }
        });

        // When the user selects an item, update the text field
        comboBox.addActionListener(e -> {
            if (comboBox.getSelectedItem() != null) {
                textField.setText(comboBox.getSelectedItem().toString());
            }
        });

        // Set the layout
        setLayout(new BorderLayout());
        add(comboBox, BorderLayout.CENTER);
    }

    private void filterItems() {
        String searchText = textField.getText().toLowerCase();
        DefaultComboBoxModel<E> filteredModel = new DefaultComboBoxModel<>();

        // Filter items based on the search text
        for (E item : items) {
            if (item.toString().toLowerCase().contains(searchText)) {
                filteredModel.addElement(item);
            }
        }

        comboBox.setModel(filteredModel);
        comboBox.setSelectedItem(searchText); // Optionally set the text field to the current search
        if (filteredModel.getSize() > 0) {
            comboBox.showPopup(); // Show the dropdown if there are matches
        }
    }
}