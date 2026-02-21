package com.restaurant.views;

import com.restaurant.dao.ProduitDAO;
import com.restaurant.models.Produit;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class AlertesStockView extends JFrame {
    private JTable tableAlertes;
    private DefaultTableModel tableModel;
    private JButton btnFermer;
    private JLabel lblInfo;
    
    private ProduitDAO produitDAO;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(230, 126, 34);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);
    private final Color BORDER_COLOR = new Color(220, 220, 220);
    private final Color TABLE_HEADER_COLOR = new Color(248, 249, 250);
    
    public AlertesStockView() {
        initComponents();
        chargerAlertes();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Alertes de Stock Bas");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        produitDAO = new ProduitDAO();
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DANGER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel lblTitre = new JLabel(" ALERTES DE STOCK BAS");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitre.setForeground(Color.WHITE);
        
        headerPanel.add(lblTitre, BorderLayout.WEST);
        
        /* Bouton fermer dans le header
        btnFermer = createIconButton("✕", Color.WHITE, DANGER_COLOR.darker());
        btnFermer.addActionListener(e -> dispose());
        headerPanel.add(btnFermer, BorderLayout.EAST);*/
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel de contenu
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Label d'information
        lblInfo = new JLabel("PRODUITS EN STOCK BAS OU EN RUPTURE");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblInfo.setForeground(TEXT_PRIMARY);
        lblInfo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        contentPanel.add(lblInfo, BorderLayout.NORTH);
        
        // Table des alertes
        JScrollPane tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JScrollPane createTablePanel() {
        // Colonnes du tableau
        String[] columns = {"ID", "Produit", "Catégorie", "Stock actuel", "Seuil alerte", "Déficit", "Statut"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0 || column == 3 || column == 4 || column == 5) return Integer.class;
                return String.class;
            }
        };
        
        tableAlertes = new JTable(tableModel);
        tableAlertes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableAlertes.setRowHeight(35);
        tableAlertes.setSelectionBackground(new Color(41, 128, 185, 50));
        tableAlertes.setSelectionForeground(TEXT_PRIMARY);
        tableAlertes.setGridColor(new Color(240, 240, 240));
        tableAlertes.setShowGrid(true);
        
        // Style du header
        tableAlertes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableAlertes.getTableHeader().setBackground(TABLE_HEADER_COLOR);
        tableAlertes.getTableHeader().setForeground(TEXT_PRIMARY);
        tableAlertes.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        tableAlertes.getTableHeader().setReorderingAllowed(false);
        
        // Largeur des colonnes
        tableAlertes.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        tableAlertes.getColumnModel().getColumn(1).setPreferredWidth(180);  // Produit
        tableAlertes.getColumnModel().getColumn(2).setPreferredWidth(120);  // Catégorie
        tableAlertes.getColumnModel().getColumn(3).setPreferredWidth(100);  // Stock actuel
        tableAlertes.getColumnModel().getColumn(4).setPreferredWidth(100);  // Seuil alerte
        tableAlertes.getColumnModel().getColumn(5).setPreferredWidth(80);   // Déficit
        tableAlertes.getColumnModel().getColumn(6).setPreferredWidth(100);  // Statut
        
        // RENDERER personnalisé avec coloration des lignes
        tableAlertes.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    String statut = (String) table.getValueAt(row, 6);
                    if (statut.equals("RUPTURE")) {
                        c.setBackground(new Color(255, 220, 220)); // Rouge très clair
                    } else if (statut.equals("STOCK BAS")) {
                        c.setBackground(new Color(255, 250, 220)); // Jaune très clair
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                
                return c;
            }
        });
        
        // Alignement spécifique pour les colonnes numériques
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        tableAlertes.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);  // ID
        tableAlertes.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);  // Stock actuel
        tableAlertes.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);  // Seuil alerte
        tableAlertes.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);  // Déficit
        
        // Renderer spécial pour le statut avec couleur de texte
        tableAlertes.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                setHorizontalAlignment(SwingConstants.CENTER);
                
                if (value != null) {
                    String statut = value.toString();
                    if (statut.equals("RUPTURE")) {
                        c.setForeground(DANGER_COLOR);
                    } else if (statut.equals("STOCK BAS")) {
                        c.setForeground(WARNING_COLOR);
                    } else {
                        c.setForeground(TEXT_PRIMARY);
                    }
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableAlertes);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        return scrollPane;
    }
    
    private JButton createIconButton(String text, Color fg, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        
        return btn;
    }
    
    private void chargerAlertes() {
        tableModel.setRowCount(0);
        List<Produit> produits = produitDAO.getProduitsStockBas();
        
        for (Produit produit : produits) {
            String statut;
            if (produit.getStockActuel() == 0) {
                statut = "RUPTURE";
            } else {
                statut = "STOCK BAS";
            }
            
            int deficit = produit.getSeuilAlerte() - produit.getStockActuel();
            if (deficit < 0) deficit = 0;
            
            Object[] row = {
                produit.getId(),
                produit.getNom(),
                produit.getCategorie().getLibelle(),
                produit.getStockActuel(),
                produit.getSeuilAlerte(),
                deficit,
                statut
            };
            tableModel.addRow(row);
        }
        
        // Mettre à jour le label d'information (SUPPRIMÉ le JOptionPane)
        if (produits.isEmpty()) {
            lblInfo.setText("Aucun produit en stock bas pour le moment");
            lblInfo.setForeground(TEXT_SECONDARY);
        } else {
            lblInfo.setText("PRODUITS EN STOCK BAS OU EN RUPTURE (" + produits.size() + " produits)");
            lblInfo.setForeground(DANGER_COLOR);
        }
    }
}