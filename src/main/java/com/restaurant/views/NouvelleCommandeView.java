package com.restaurant.views;

import com.restaurant.dao.ProduitDAO;
import com.restaurant.dao.CommandeDAO;
import com.restaurant.dao.LigneCommandeDAO;
import com.restaurant.models.Produit;
import com.restaurant.models.LigneCommande;
import com.restaurant.models.Commande;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kanlanfaï KOMBATE
 */
public class NouvelleCommandeView extends JFrame {
    private JTable tableProduits;
    private JTable tableLignes;
    private DefaultTableModel modelProduits, modelLignes;
    private JTextField txtQuantite;
    private JLabel lblTotal, lblInfo, lblInstruction, lblProduitCount;
    private JButton btnAjouter, btnSupprimer, btnValider, btnAnnuler, btnVider, btnFermer;
    
    private List<LigneCommande> lignesCommande;
    private double totalCommande;
    
    private ProduitDAO produitDAO;
    
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(230, 126, 34);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);
    private final Color BORDER_COLOR = new Color(220, 220, 220);
    private final Color TABLE_HEADER_COLOR = new Color(248, 249, 250);
    private final Color TOTAL_BG_COLOR = new Color(240, 248, 255);
    
    public NouvelleCommandeView() {
        initComponents();
        chargerProduits();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Nouvelle Commande");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        produitDAO = new ProduitDAO();
        lignesCommande = new ArrayList<>();
        totalCommande = 0.0;
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel lblTitre = new JLabel("Nouvelle Commande");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitre.setForeground(Color.WHITE);
        
        headerPanel.add(lblTitre, BorderLayout.WEST);
        
        // Bouton fermer dans le header
        btnFermer = createIconButton("✕", Color.WHITE, PRIMARY_COLOR.darker());
        btnFermer.addActionListener(e -> dispose());
        headerPanel.add(btnFermer, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel d'instruction
        JPanel instructionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        instructionPanel.setBackground(new Color(240, 248, 255));
        instructionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel iconLabel = new JLabel("");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        lblInstruction = new JLabel("<html><b>Comment créer une commande :</b> 1) Sélectionnez un produit dans la liste → 2) Indiquez la quantité → 3) Cliquez sur 'Ajouter' → 4) Validez la commande une fois terminée</html>");
        lblInstruction.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInstruction.setForeground(TEXT_PRIMARY);
        
        instructionPanel.add(iconLabel);
        instructionPanel.add(lblInstruction);
        
        mainPanel.add(instructionPanel, BorderLayout.CENTER);
        
        // Panel de contenu avec split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(600);
        splitPane.setDividerSize(3);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Panel gauche : Produits disponibles
        JPanel panelProduits = createProduitsPanel();
        splitPane.setLeftComponent(panelProduits);
        
        // Panel droit : Détail de la commande
        JPanel panelCommande = createCommandePanel();
        splitPane.setRightComponent(panelCommande);
        
        mainPanel.add(splitPane, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createProduitsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // En-tête avec compteur
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        lblInfo = new JLabel("Produits disponibles");
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblInfo.setForeground(TEXT_PRIMARY);
        
        lblProduitCount = new JLabel("15");
        lblProduitCount.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblProduitCount.setForeground(PRIMARY_COLOR);
        
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        countPanel.setOpaque(false);
        countPanel.add(new JLabel(": "));
        countPanel.add(lblProduitCount);
        
        headerPanel.add(lblInfo, BorderLayout.WEST);
        headerPanel.add(countPanel, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Table des produits
        String[] colsProduits = {"ID", "Nom", "Prix"}; // SUPPRIMÉ: Stock
        modelProduits = new DefaultTableModel(colsProduits, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return Integer.class;
                if (column == 2) return Double.class;
                return String.class;
            }
        };
        
        tableProduits = new JTable(modelProduits);
        tableProduits.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableProduits.setRowHeight(35);
        tableProduits.setSelectionBackground(new Color(41, 128, 185, 50));
        tableProduits.setSelectionForeground(TEXT_PRIMARY);
        tableProduits.setGridColor(new Color(240, 240, 240));
        tableProduits.setShowGrid(true);
        
        // Style du header
        tableProduits.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableProduits.getTableHeader().setBackground(TABLE_HEADER_COLOR);
        tableProduits.getTableHeader().setForeground(TEXT_PRIMARY);
        tableProduits.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        tableProduits.getTableHeader().setReorderingAllowed(false);
        
        // Largeur des colonnes
        tableProduits.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        tableProduits.getColumnModel().getColumn(1).setPreferredWidth(250);  // Nom
        tableProduits.getColumnModel().getColumn(2).setPreferredWidth(100);  // Prix
        
        // Alignement des colonnes
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        tableProduits.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);  // ID
        tableProduits.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);  // Prix
        
        // Renderer pour les prix
        tableProduits.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.RIGHT);
                c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                return c;
            }
        });
        
        JScrollPane scrollProduits = new JScrollPane(tableProduits);
        scrollProduits.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollProduits.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollProduits, BorderLayout.CENTER);
        
        // Panel de contrôle pour ajouter un produit
        JPanel panelControle = new JPanel(new GridBagLayout());
        panelControle.setBackground(CARD_COLOR);
        panelControle.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblQuantite = new JLabel("Quantité:");
        lblQuantite.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelControle.add(lblQuantite, gbc);
        
        gbc.gridx = 1;
        txtQuantite = new JTextField("1", 3);
        txtQuantite.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtQuantite.setPreferredSize(new Dimension(80, 40));
        txtQuantite.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panelControle.add(txtQuantite, gbc);
        
        gbc.gridx = 2;
        btnAjouter = createButton(" Ajouter à la commande", SUCCESS_COLOR, 200);
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterProduit();
            }
        });
        panelControle.add(btnAjouter, gbc);
        
        panel.add(panelControle, BorderLayout.SOUTH);
        
        // Double-clic sur la table des produits
        tableProduits.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    ajouterProduit();
                }
            }
        });
        
        return panel;
    }
    
    private JPanel createCommandePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // En-tête
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel lblTitre = new JLabel("Détail de la commande");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitre.setForeground(TEXT_PRIMARY);
        headerPanel.add(lblTitre, BorderLayout.WEST);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Table des lignes de commande
        String[] colsLignes = {"Produit", "Quantité", "Prix unitaire", "Montant"}; // SUPPRIMÉ: Action
        modelLignes = new DefaultTableModel(colsLignes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Toutes les colonnes sont en lecture seule
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 1) return Integer.class;
                if (column == 2 || column == 3) return Double.class;
                return String.class;
            }
        };
        
        tableLignes = new JTable(modelLignes);
        tableLignes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableLignes.setRowHeight(35);
        tableLignes.setSelectionBackground(new Color(41, 128, 185, 50));
        tableLignes.setSelectionForeground(TEXT_PRIMARY);
        tableLignes.setGridColor(new Color(240, 240, 240));
        tableLignes.setShowGrid(true);
        
        // Style du header
        tableLignes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableLignes.getTableHeader().setBackground(TABLE_HEADER_COLOR);
        tableLignes.getTableHeader().setForeground(TEXT_PRIMARY);
        tableLignes.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        tableLignes.getTableHeader().setReorderingAllowed(false);
        
        // Largeur des colonnes
        tableLignes.getColumnModel().getColumn(0).setPreferredWidth(200);  // Produit
        tableLignes.getColumnModel().getColumn(1).setPreferredWidth(80);   // Quantité
        tableLignes.getColumnModel().getColumn(2).setPreferredWidth(120);  // Prix unitaire
        tableLignes.getColumnModel().getColumn(3).setPreferredWidth(120);  // Montant
        
        // Alignement des colonnes
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        tableLignes.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);  // Quantité
        tableLignes.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);  // Prix unitaire
        tableLignes.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);  // Montant
        
        // Renderer pour les montants
        tableLignes.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.RIGHT);
                c.setFont(new Font("Segoe UI", Font.BOLD, 12));
                c.setForeground(SUCCESS_COLOR);
                return c;
            }
        });
        
        JScrollPane scrollLignes = new JScrollPane(tableLignes);
        scrollLignes.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollLignes.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollLignes, BorderLayout.CENTER);
        
        // Panel des totaux et boutons
        JPanel panelFooter = new JPanel(new BorderLayout(20, 20));
        panelFooter.setBackground(CARD_COLOR);
        panelFooter.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Section Total dans un rectangle
        JPanel panelTotalRect = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelTotalRect.setBackground(TOTAL_BG_COLOR);
        panelTotalRect.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2, true),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblTotalText = new JLabel("Montant total : ");
        lblTotalText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalText.setForeground(TEXT_PRIMARY);
        
        lblTotal = new JLabel("0.0");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTotal.setForeground(SUCCESS_COLOR);
        
        JLabel lblFcfa = new JLabel(" FCFA");
        lblFcfa.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFcfa.setForeground(TEXT_SECONDARY);
        
        panelTotalRect.add(lblTotalText);
        panelTotalRect.add(lblTotal);
        panelTotalRect.add(lblFcfa);
        
        // Section Boutons
        JPanel panelBoutons = new JPanel(new GridLayout(2, 2, 10, 10));
        panelBoutons.setOpaque(false);
        panelBoutons.setPreferredSize(new Dimension(300, 90));
        
        btnVider = createButton(" Vider la commande", DANGER_COLOR, 180);
        btnVider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viderCommande();
            }
        });
        
        btnSupprimer = createButton(" Supprimer la ligne", DANGER_COLOR, 180);
        btnSupprimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                supprimerLigne();
            }
        });
        
        btnValider = createButton(" Valider la commande", SUCCESS_COLOR, 180);
        btnValider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validerCommande();
            }
        });
        
        btnAnnuler = createButton("Annuler", TEXT_SECONDARY, 180);
        btnAnnuler.setBackground(new Color(200, 200, 200));
        btnAnnuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        panelBoutons.add(btnVider);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnValider);
        panelBoutons.add(btnAnnuler);
        
        // Assembler le footer
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.add(panelTotalRect, BorderLayout.WEST);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(panelBoutons);
        
        panelFooter.add(leftPanel, BorderLayout.WEST);
        panelFooter.add(rightPanel, BorderLayout.EAST);
        
        panel.add(panelFooter, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JButton createButton(String text, Color bg, int width) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(width, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(bg.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        
        return btn;
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
    
    private void chargerProduits() {
        modelProduits.setRowCount(0);
        List<Produit> produits = produitDAO.getAll();
        
        int count = 0;
        for (Produit produit : produits) {
            if (produit.getStockActuel() > 0) {
                Object[] row = {
                    produit.getId(),
                    produit.getNom(),
                    produit.getPrixVente()
                };
                modelProduits.addRow(row);
                count++;
            }
        }
        
        lblProduitCount.setText(String.valueOf(count));
    }
    
    private void ajouterProduit() {
        int selectedRow = tableProduits.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un produit dans la liste", 
                "Sélection requise", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int produitId = (int) modelProduits.getValueAt(selectedRow, 0);
            String produitNom = (String) modelProduits.getValueAt(selectedRow, 1);
            double prix = (double) modelProduits.getValueAt(selectedRow, 2);
            
            // Récupérer le produit complet pour vérifier le stock
            Produit produitComplet = produitDAO.read(produitId);
            int stock = produitComplet.getStockActuel();
            
            int quantite = Integer.parseInt(txtQuantite.getText());
            if (quantite <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "La quantité doit être supérieure à zéro", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (quantite > stock) {
                JOptionPane.showMessageDialog(this, 
                    "Stock insuffisant !\nStock disponible: " + stock, 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Vérifier si le produit est déjà dans la commande
            for (LigneCommande ligne : lignesCommande) {
                if (ligne.getProduit().getId() == produitId) {
                    int reponse = JOptionPane.showConfirmDialog(this,
                        "Ce produit est déjà dans la commande. Voulez-vous modifier la quantité?\n\n" +
                        "Produit: " + produitNom + "\n" +
                        "Quantité actuelle: " + ligne.getQuantite(),
                        "Produit déjà ajouté",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                    
                    if (reponse == JOptionPane.YES_OPTION) {
                        ligne.setQuantite(quantite);
                        recalculerTotal();
                        actualiserTableLignes();
                    }
                    return;
                }
            }
            
            // Créer une nouvelle ligne
            Produit produit = new Produit();
            produit.setId(produitId);
            produit.setNom(produitNom);
            produit.setPrixVente(prix);
            
            LigneCommande ligne = new LigneCommande(produit, quantite);
            lignesCommande.add(ligne);
            
            recalculerTotal();
            actualiserTableLignes();
            
            // Réinitialiser la quantité
            txtQuantite.setText("1");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez entrer une quantité valide (nombre entier)", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void supprimerLigne() {
        int selectedRow = tableLignes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner une ligne à supprimer dans le détail de la commande", 
                "Sélection requise", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        LigneCommande ligne = lignesCommande.get(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Voulez-vous supprimer cette ligne ?\n\n" +
            "Produit: " + ligne.getProduit().getNom() + "\n" +
            "Quantité: " + ligne.getQuantite() + "\n" +
            "Montant: " + String.format("%.2f", ligne.getMontantLigne()),
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            lignesCommande.remove(selectedRow);
            recalculerTotal();
            actualiserTableLignes();
        }
    }
    
    private void viderCommande() {
        if (lignesCommande.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La commande est déjà vide",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Voulez-vous vraiment vider toute la commande ?\n" +
            "Cette action supprimera " + lignesCommande.size() + " article(s).",
            "Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            lignesCommande.clear();
            totalCommande = 0.0;
            actualiserTableLignes();
        }
    }
    
    private void validerCommande() {
        if (lignesCommande.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "La commande est vide !\nSélectionnez des produits et ajoutez-les à la commande.", 
                "Commande vide", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Préparer le message de confirmation
        StringBuilder message = new StringBuilder();
        message.append("<html><b>Confirmez-vous la validation de cette commande ?</b><br><br>");
        message.append("<b>Détail de la commande:</b><br>");
        
        for (int i = 0; i < lignesCommande.size(); i++) {
            LigneCommande ligne = lignesCommande.get(i);
            message.append(String.format("&nbsp;&nbsp;%d. %s × %d = %.2f FCFA<br>", 
                i + 1,
                ligne.getProduit().getNom(),
                ligne.getQuantite(),
                ligne.getMontantLigne()));
        }
        
        message.append("<br>");
        message.append("<b>Nombre d'articles:</b> ").append(lignesCommande.size()).append("<br>");
        message.append("<b>Total:</b> <font color='green'>").append(String.format("%.2f", totalCommande)).append(" FCFA</font><br><br>");
        message.append("Cette action est irréversible et mettra à jour les stocks.</html>");
        
        int confirm = JOptionPane.showConfirmDialog(this,
            message.toString(),
            "Validation de commande",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Créer la commande
                Commande commande = new Commande();
                commande.setTotal(totalCommande);
                commande.setEtat("EN_COURS");  
                
                CommandeDAO commandeDAO = new CommandeDAO();
                int commandeId = commandeDAO.create(commande);
                
                // Créer les lignes de commande
                LigneCommandeDAO ligneDAO = new LigneCommandeDAO();
                for (LigneCommande ligne : lignesCommande) {
                    ligneDAO.create(ligne, commandeId);
                }
                
                // Mettre à jour les stocks
                commandeDAO.validerCommande(commandeId);
                
                JOptionPane.showMessageDialog(this,
                    "<html><b>Commande validée avec succès !</b><br><br>" +
                    "<b>Numéro de commande:</b> " + commandeId + "<br>" +
                    "<b>Total:</b> " + String.format("%.2f", totalCommande) + " FCFA<br>" +
                    "<b>Articles:</b> " + lignesCommande.size() + "<br><br>" +
                    "Les stocks ont été mis à jour automatiquement.</html>",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la validation: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void recalculerTotal() {
        totalCommande = 0.0;
        for (LigneCommande ligne : lignesCommande) {
            totalCommande += ligne.getMontantLigne();
        }
        lblTotal.setText(String.format("%.2f", totalCommande));
    }
    
    private void actualiserTableLignes() {
        modelLignes.setRowCount(0);
        
        for (LigneCommande ligne : lignesCommande) {
            Object[] row = {
                ligne.getProduit().getNom(),
                ligne.getQuantite(),
                ligne.getPrixUnitaire(),
                ligne.getMontantLigne()
            };
            modelLignes.addRow(row);
        }
        
        recalculerTotal();
    }
}