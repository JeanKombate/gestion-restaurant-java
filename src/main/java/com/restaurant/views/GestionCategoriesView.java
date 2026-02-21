/*
 * Vue modernisée de la gestion des catégories - VERSION FINALE CORRIGÉE
 */
package com.restaurant.views;

import com.restaurant.dao.CategorieDAO;
import com.restaurant.models.Categorie;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GestionCategoriesView extends JFrame {
    private JTable tableCategories;
    private DefaultTableModel tableModel;
    private JButton btnAjouter, btnModifier, btnSupprimer, btnActualiser;
    private JTextField txtRecherche;
    private JLabel lblTotal, lblActives;
    private CategorieDAO categorieDAO;
    
    // Couleurs du thème
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);
    
    public GestionCategoriesView() {
        initComponents();
        setupListeners();
        loadCategories();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Gestion des Catégories");
        setSize(900, 650);
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        categorieDAO = new CategorieDAO();
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(CARD_COLOR);
        
        JLabel titleLabel = new JLabel("Gestion des Catégories");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Organisez vos produits par catégories");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        statsPanel.setBackground(CARD_COLOR);
        
        statsPanel.add(createStatCardPanel("0", "Total", PRIMARY_COLOR));
        statsPanel.add(createStatCardPanel("0", "Actives", SUCCESS_COLOR));
        
        lblTotal = (JLabel) ((JPanel) statsPanel.getComponent(0)).getComponent(0);
        lblActives = (JLabel) ((JPanel) statsPanel.getComponent(1)).getComponent(0);
        
        headerPanel.add(statsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createStatCardPanel(String value, String label, Color color) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelText.setForeground(TEXT_SECONDARY);
        labelText.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cardPanel.add(valueLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(labelText);
        
        return cardPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel contentPanel = new JPanel(new BorderLayout(0, 0));
        contentPanel.setBackground(CARD_COLOR);
        contentPanel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        JPanel toolbarPanel = new JPanel(new BorderLayout());
        toolbarPanel.setBackground(CARD_COLOR);
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel leftToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftToolbar.setBackground(CARD_COLOR);
        
        txtRecherche = new JTextField(25);
        txtRecherche.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtRecherche.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtRecherche.setPreferredSize(new Dimension(300, 38));
        
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(CARD_COLOR);
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(txtRecherche, BorderLayout.CENTER);
        
        leftToolbar.add(searchPanel);
        toolbarPanel.add(leftToolbar, BorderLayout.WEST);
        
        JPanel rightToolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightToolbar.setBackground(CARD_COLOR);
        
        btnAjouter = createStyledButton("Ajouter", SUCCESS_COLOR);
        btnModifier = createStyledButton(" Modifier", PRIMARY_COLOR);
        btnSupprimer = createStyledButton("Supprimer", DANGER_COLOR);
        btnActualiser = createStyledButton(" Actualiser", new Color(149, 165, 166));
        
        rightToolbar.add(btnAjouter);
        rightToolbar.add(btnModifier);
        rightToolbar.add(btnSupprimer);
        rightToolbar.add(btnActualiser);
        
        toolbarPanel.add(rightToolbar, BorderLayout.EAST);
        contentPanel.add(toolbarPanel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Libellé"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableCategories = new JTable(tableModel);
        tableCategories.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableCategories.setRowHeight(50);
        tableCategories.setGridColor(new Color(240, 240, 240));
        tableCategories.setSelectionBackground(new Color(232, 240, 254));
        tableCategories.setSelectionForeground(TEXT_PRIMARY);
        tableCategories.setShowVerticalLines(false);
        
        JTableHeader header = tableCategories.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 45));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)));
        
        tableCategories.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableCategories.getColumnModel().getColumn(1).setPreferredWidth(700);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tableCategories.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        tableCategories.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setForeground(TEXT_PRIMARY);
                label.setText("🏷️ " + value.toString());
                
                return label;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableCategories);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(contentPanel, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(130, 38));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        button.addMouseListener(new MouseAdapter() {
            Color originalColor = bgColor;
            Color hoverColor = bgColor.darker();
            
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
        
        return button;
    }
    
    private void setupListeners() {
        btnAjouter.addActionListener(e -> ajouterCategorie());
        btnModifier.addActionListener(e -> modifierCategorie());
        btnSupprimer.addActionListener(e -> supprimerCategorie());
        btnActualiser.addActionListener(e -> loadCategories());
        
        txtRecherche.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rechercherCategories();
            }
        });
        
        tableCategories.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    modifierCategorie();
                }
            }
        });
    }
    
    private void ajouterCategorie() {
        JDialog dialog = new JDialog(this, "Nouvelle Catégorie", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel titleLabel = new JLabel("️ Nom de la catégorie *");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField txtLibelle = new JTextField();
        txtLibelle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLibelle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtLibelle.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(txtLibelle);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JButton btnAnnuler = createStyledButton(" Annuler", DANGER_COLOR);
        btnAnnuler.addActionListener(e -> dialog.dispose());
        
        JButton btnCreer = createStyledButton(" Créer", SUCCESS_COLOR);
        btnCreer.addActionListener(e -> {
            String libelle = txtLibelle.getText().trim();
            
            if (libelle.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "⚠️ Le nom de la catégorie est obligatoire !",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (categorieDAO.libelleExists(libelle)) {
                JOptionPane.showMessageDialog(dialog,
                    "⚠️ Cette catégorie existe déjà !",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Categorie nouvelle = new Categorie(libelle);
            categorieDAO.create(nouvelle);
            
            JOptionPane.showMessageDialog(dialog,
                "✅ Catégorie créée avec succès !",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
            
            dialog.dispose();
            loadCategories();
        });
        
        buttonPanel.add(btnAnnuler);
        buttonPanel.add(btnCreer);
        
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void modifierCategorie() {
        int selectedRow = tableCategories.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Veuillez sélectionner une catégorie à modifier",
                "Sélection requise",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int catId = (int) tableModel.getValueAt(selectedRow, 0);
        String ancienLibelle = (String) tableModel.getValueAt(selectedRow, 1);
        
        JDialog dialog = new JDialog(this, "Modifier la Catégorie", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel titleLabel = new JLabel("️ Nouveau nom *");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField txtLibelle = new JTextField(ancienLibelle);
        txtLibelle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLibelle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        txtLibelle.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(txtLibelle);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JButton btnAnnuler = createStyledButton(" Annuler", DANGER_COLOR);
        btnAnnuler.addActionListener(e -> dialog.dispose());
        
        JButton btnEnregistrer = createStyledButton(" Enregistrer", SUCCESS_COLOR);
        btnEnregistrer.addActionListener(e -> {
            String nouveauLibelle = txtLibelle.getText().trim();
            
            if (nouveauLibelle.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "⚠️ Le nom de la catégorie est obligatoire !",
                    "Validation",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!nouveauLibelle.equals(ancienLibelle) && categorieDAO.libelleExists(nouveauLibelle)) {
                JOptionPane.showMessageDialog(dialog,
                    "⚠️ Cette catégorie existe déjà !",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Categorie categorie = new Categorie(catId, nouveauLibelle);
            categorieDAO.update(categorie);
            
            JOptionPane.showMessageDialog(dialog,
                "✅ Catégorie modifiée avec succès !",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
            
            dialog.dispose();
            loadCategories();
        });
        
        buttonPanel.add(btnAnnuler);
        buttonPanel.add(btnEnregistrer);
        
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void supprimerCategorie() {
        int selectedRow = tableCategories.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Veuillez sélectionner une catégorie à supprimer",
                "Sélection requise",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int catId = (int) tableModel.getValueAt(selectedRow, 0);
        String libelle = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer la catégorie \"" + libelle + "\" ?\n\n" +
            "Cette action échouera si des produits sont associés.",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                categorieDAO.delete(catId);
                JOptionPane.showMessageDialog(this,
                    "✅ Catégorie supprimée avec succès !",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                loadCategories();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "❌ Erreur lors de la suppression :\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadCategories() {
        tableModel.setRowCount(0);
        List<Categorie> categories = categorieDAO.getAll();
        
        for (Categorie cat : categories) {
            Object[] row = {cat.getId(), cat.getLibelle()};
            tableModel.addRow(row);
        }
        
        lblTotal.setText(String.valueOf(categories.size()));
        lblActives.setText(String.valueOf(categories.size()));
    }
    
    private void rechercherCategories() {
        String recherche = txtRecherche.getText().toLowerCase().trim();
        
        if (recherche.isEmpty()) {
            loadCategories();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Categorie> categories = categorieDAO.getAll();
        
        for (Categorie cat : categories) {
            if (cat.getLibelle().toLowerCase().contains(recherche)) {
                Object[] row = {cat.getId(), cat.getLibelle()};
                tableModel.addRow(row);
            }
        }
    }
}