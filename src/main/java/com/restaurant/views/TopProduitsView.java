/*
 * Vue modernisée du Top Produits avec podium
 */
package com.restaurant.views;

import com.restaurant.dao.CommandeDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TopProduitsView extends JFrame {
    private JTable tableTopProduits;
    private DefaultTableModel tableModel;
    private JSpinner spinnerLimit;
    private JButton btnActualiser, btnExporter;
    private JLabel lblTop1, lblTop2, lblTop3;
    private JLabel lblQty1, lblQty2, lblQty3;  // AJOUTÉ : labels pour les quantités
    private JPanel podiumPanel;
    
    private CommandeDAO commandeDAO;
    
    // Couleurs du thème
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color GOLD_COLOR = new Color(255, 215, 0);
    private final Color SILVER_COLOR = new Color(192, 192, 192);
    private final Color BRONZE_COLOR = new Color(205, 127, 50);
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);
    
    // Classe interne pour retourner à la fois le panel et le label
    private static class PodiumCardResult {
        JPanel card;
        JLabel nameLabel;
        JLabel qtyLabel;
        
        PodiumCardResult(JPanel card, JLabel nameLabel, JLabel qtyLabel) {
            this.card = card;
            this.nameLabel = nameLabel;
            this.qtyLabel = qtyLabel;
        }
    }
    
    public TopProduitsView() {
        initComponents();
        chargerTopProduits(10);
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Top Produits Vendus");
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        commandeDAO = new CommandeDAO();
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Centre avec podium et table
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
        
        // Titre
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(CARD_COLOR);
        
        JLabel titleLabel = new JLabel(" Top Produits Vendus");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Découvrez vos produits les plus populaires");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        // Contrôles
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlsPanel.setBackground(CARD_COLOR);
        
        JLabel lblLimit = new JLabel("Top :");
        lblLimit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLimit.setForeground(TEXT_PRIMARY);
        
        spinnerLimit = new JSpinner(new SpinnerNumberModel(10, 5, 50, 5));
        spinnerLimit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spinnerLimit.setPreferredSize(new Dimension(70, 38));
        ((JSpinner.DefaultEditor) spinnerLimit.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        
        btnActualiser = createStyledButton(" Actualiser", PRIMARY_COLOR);
        btnActualiser.addActionListener(e -> {
            int limit = (int) spinnerLimit.getValue();
            chargerTopProduits(limit);
        });
        
        btnExporter = createStyledButton(" Exporter", SUCCESS_COLOR);
        btnExporter.addActionListener(e -> exporterTop());
        
        controlsPanel.add(lblLimit);
        controlsPanel.add(spinnerLimit);
        controlsPanel.add(btnActualiser);
        controlsPanel.add(btnExporter);
        
        headerPanel.add(controlsPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Podium Top 3
        centerPanel.add(createPodiumPanel(), BorderLayout.NORTH);
        
        // Table complète
        centerPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private JPanel createPodiumPanel() {
        podiumPanel = new JPanel(new GridBagLayout());
        podiumPanel.setBackground(CARD_COLOR);
        podiumPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 15, 0, 15);
        gbc.anchor = GridBagConstraints.SOUTH;
        
        // Position 2 (Argent) - Plus bas
        gbc.gridx = 0;
        gbc.gridy = 0;
        PodiumCardResult silverResult = createPodiumCard(2, "---", "0", SILVER_COLOR, 180);
        podiumPanel.add(silverResult.card, gbc);
        lblTop2 = silverResult.nameLabel;
        lblQty2 = silverResult.qtyLabel;  // AJOUTÉ : stocker le label quantité
        
        // Position 1 (Or) - Plus haut
        gbc.gridx = 1;
        gbc.gridy = 0;
        PodiumCardResult goldResult = createPodiumCard(1, "---", "0", GOLD_COLOR, 220);
        podiumPanel.add(goldResult.card, gbc);
        lblTop1 = goldResult.nameLabel;
        lblQty1 = goldResult.qtyLabel;  // AJOUTÉ : stocker le label quantité
        
        // Position 3 (Bronze) - Encore plus bas
        gbc.gridx = 2;
        gbc.gridy = 0;
        PodiumCardResult bronzeResult = createPodiumCard(3, "---", "0", BRONZE_COLOR, 180);
        podiumPanel.add(bronzeResult.card, gbc);
        lblTop3 = bronzeResult.nameLabel;
        lblQty3 = bronzeResult.qtyLabel;  // AJOUTÉ : stocker le label quantité
        
        return podiumPanel;
    }
    
    private PodiumCardResult createPodiumCard(int position, String produitNom, String quantite, Color color, int height) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(color);
        card.setPreferredSize(new Dimension(250, height));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 3),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));
        
        // Médaille
        String medal = position == 1 ? "🥇" : position == 2 ? "🥈" : "🥉";
        JLabel medalLabel = new JLabel(medal);
        medalLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        medalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Position
        JLabel posLabel = new JLabel("#" + position);  // ← AJOUTÉ : # devant le numéro
        posLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        posLabel.setForeground(Color.WHITE);  // ← Blanc au lieu de gris
        posLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Nom du produit
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setBackground(color);
        
        JLabel nameLabel = new JLabel("<html><center>" + produitNom + "</center></html>");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);  // ← CHANGÉ : Blanc pour meilleure visibilité
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel qtyLabel = new JLabel(quantite + " vendus");
        qtyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        qtyLabel.setForeground(Color.WHITE);  // ← CHANGÉ : Blanc au lieu de gris
        qtyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        qtyLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        namePanel.add(nameLabel);
        namePanel.add(qtyLabel);
        
        card.add(medalLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(posLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(namePanel);
        
        return new PodiumCardResult(card, nameLabel, qtyLabel);
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 15));
        tablePanel.setBackground(CARD_COLOR);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Titre
        JLabel titleLabel = new JLabel(" Classement complet");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        tablePanel.add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Pos.", "Produit", "Quantité", "CA (FCFA)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableTopProduits = new JTable(tableModel);
        tableTopProduits.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableTopProduits.setRowHeight(45);
        tableTopProduits.setGridColor(new Color(240, 240, 240));
        tableTopProduits.setSelectionBackground(new Color(232, 240, 254));
        tableTopProduits.setSelectionForeground(TEXT_PRIMARY);
        tableTopProduits.setShowVerticalLines(false);
        
        // Header
        JTableHeader header = tableTopProduits.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(248, 249, 250));
        header.setForeground(TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)));
        
        // Largeurs
        tableTopProduits.getColumnModel().getColumn(0).setPreferredWidth(80);
        tableTopProduits.getColumnModel().getColumn(1).setPreferredWidth(400);
        tableTopProduits.getColumnModel().getColumn(2).setPreferredWidth(150);
        tableTopProduits.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        // Renderer pour Position avec médailles
        tableTopProduits.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                
                // Ligne de total
                if (value != null && value.toString().isEmpty()) {
                    label.setBackground(new Color(248, 249, 250));
                    label.setForeground(TEXT_PRIMARY);
                    return label;
                }
                
                int pos = row + 1;
                if (pos == 1) {
                    label.setText("● #1");  // Cercle plein (bullet) au lieu d'emoji
                    label.setBackground(new Color(255, 250, 205));
                    label.setForeground(GOLD_COLOR.darker().darker());
                } else if (pos == 2) {
                    label.setText("● #2");  // Cercle plein
                    label.setBackground(new Color(245, 245, 245));
                    label.setForeground(SILVER_COLOR.darker().darker());
                } else if (pos == 3) {
                    label.setText("● #3");  // Cercle plein
                    label.setBackground(new Color(245, 245, 245));
                    label.setForeground(BRONZE_COLOR.darker());
                } else {
                    label.setText("#" + pos);
                    label.setBackground(Color.WHITE);
                    label.setForeground(TEXT_PRIMARY);
                }
                
                if (isSelected) {
                    label.setBackground(table.getSelectionBackground());
                    label.setForeground(table.getSelectionForeground());
                }
                
                return label;
            }
        });
        
        // Renderer pour Produit
        tableTopProduits.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                
                // Ligne de total
                if (value != null && value.toString().equals("TOTAL")) {
                    label.setBackground(new Color(248, 249, 250));
                    label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    label.setForeground(TEXT_PRIMARY);
                } else {
                    label.setForeground(TEXT_PRIMARY);
                }
                
                return label;
            }
        });
        
        // Renderer pour Quantité (centré)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tableTopProduits.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        // Renderer pour CA (vert et aligné à droite)
        tableTopProduits.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setForeground(SUCCESS_COLOR);
                
                // Ligne de total
                if (tableModel.getValueAt(row, 1) != null && 
                    tableModel.getValueAt(row, 1).toString().equals("TOTAL")) {
                    label.setBackground(new Color(248, 249, 250));
                    label.setFont(new Font("Segoe UI", Font.BOLD, 15));
                }
                
                return label;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableTopProduits);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
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
    
    private void chargerTopProduits(int limit) {
        tableModel.setRowCount(0);
        
        List<Object[]> topProduits = commandeDAO.getTopProduits(limit);
        
        int position = 1;
        double totalCAProduits = 0;
        
        for (Object[] rowData : topProduits) {
            String produitNom = (String) rowData[0];
            int quantiteVendue = (int) rowData[1];
            
            // Estimation du CA
            double prixEstime = 2000.0;
            double caProduit = quantiteVendue * prixEstime;
            totalCAProduits += caProduit;
            
            Object[] row = {
                position,
                produitNom,
                quantiteVendue,
                String.format("%,.0f", caProduit)
            };
            tableModel.addRow(row);
            
            // Mettre à jour le podium
            if (position == 1 && lblTop1 != null && lblQty1 != null) {
                lblTop1.setText("<html><center>" + produitNom + "</center></html>");
                lblQty1.setText(quantiteVendue + " vendus");  // ← AJOUTÉ : mettre à jour la quantité
            } else if (position == 2 && lblTop2 != null && lblQty2 != null) {
                lblTop2.setText("<html><center>" + produitNom + "</center></html>");
                lblQty2.setText(quantiteVendue + " vendus");  // ← AJOUTÉ : mettre à jour la quantité
            } else if (position == 3 && lblTop3 != null && lblQty3 != null) {
                lblTop3.setText("<html><center>" + produitNom + "</center></html>");
                lblQty3.setText(quantiteVendue + " vendus");  // ← AJOUTÉ : mettre à jour la quantité
            }
            
            position++;
        }
        
        // Ligne de total
        if (topProduits.size() > 0) {
            tableModel.addRow(new Object[]{"", "TOTAL", "", String.format("%,.0f", totalCAProduits)});
        }
    }
    
    private void exporterTop() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                " Aucune donnée à exporter",
                "Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter le Top Produits");
        fileChooser.setSelectedFile(new java.io.File("top_produits_" + 
            new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.PrintWriter writer = new java.io.PrintWriter(file);
                
                writer.println("Position;Produit;Quantité;CA (FCFA)");
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    writer.println(
                        tableModel.getValueAt(i, 0) + ";" +
                        tableModel.getValueAt(i, 1) + ";" +
                        tableModel.getValueAt(i, 2) + ";" +
                        tableModel.getValueAt(i, 3)
                    );
                }
                
                writer.close();
                
                JOptionPane.showMessageDialog(this,
                    "✅ Top produits exporté avec succès !\n\nFichier : " + file.getAbsolutePath(),
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "❌ Erreur lors de l'export : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}