/*
 * Menu Principal Modernisé avec menu utilisateur à droite
 */
package com.restaurant.views;

import com.restaurant.utils.SessionManager;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage; // <-- Ajoutez cette ligne
import java.awt.geom.RoundRectangle2D;

public class MainMenuView extends JFrame {
    // Composants du menu
    private JMenuBar menuBar;
    private JMenu menuGestion, menuStock, menuCommandes, menuStats, menuAide;
    private JMenuItem itemProduits, itemCategories, itemUtilisateurs;
    private JMenuItem itemEntreeStock, itemSortieStock, itemHistorique;
    private JMenuItem itemNouvelleCommande, itemListeCommandes;
    private JMenuItem itemCAByDate, itemTopProduits, itemAlertesStock;
    private JMenuItem itemAide, itemAPropos, itemQuitter;
    
    // Menu utilisateur à droite
    private JMenu menuUser;
    private JMenuItem itemProfil, itemParametres, itemMesStats, itemDeconnexion;
    
    // Panel de bienvenue
    private JLabel lblWelcome;
    
    // Couleurs du thème professionnel
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Bleu professionnel
    private final Color SECONDARY_COLOR = new Color(52, 73, 94);      // Gris-bleu foncé
    private final Color ACCENT_COLOR = new Color(26, 188, 156);       // Turquoise
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);  // Gris très clair
    private final Color CARD_COLOR = Color.WHITE;                      // Blanc
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);         // Texte principal
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);    // Texte secondaire
    private final Color DANGER_COLOR = new Color(231, 76, 60);        // Rouge pour déconnexion
    
    public MainMenuView() {
        initComponents();
        setupMenuListeners();
        setLocationRelativeTo(null);
    }
    
    private void setupMenuListeners() {
        // Gestion des produits
        itemProduits.addActionListener(e -> {
            GestionProduitsView gestionProduits = new GestionProduitsView();
            gestionProduits.setVisible(true);
        });
        
        // Gestion des catégories
        itemCategories.addActionListener(e -> {
            GestionCategoriesView gestionCategories = new GestionCategoriesView();
            gestionCategories.setVisible(true);
        });
        
        // Gestion des utilisateurs
        itemUtilisateurs.addActionListener(e -> {
            GestionUtilisateursView gestionUtilisateurs = new GestionUtilisateursView();
            gestionUtilisateurs.setVisible(true);
        });
        
        // Nouvelle commande
        itemNouvelleCommande.addActionListener(e -> {
            NouvelleCommandeView nouvelleCommande = new NouvelleCommandeView();
            nouvelleCommande.setVisible(true);
        });
        
        // Liste des commandes
        itemListeCommandes.addActionListener(e -> {
            ListeCommandesView listeCommandes = new ListeCommandesView();
            listeCommandes.setVisible(true);
        });
        
        // CA par date
        itemCAByDate.addActionListener(e -> {
            StatistiquesView stats = new StatistiquesView();
            stats.setVisible(true);
        });
        
        // Top produits
        itemTopProduits.addActionListener(e -> {
            TopProduitsView topProduits = new TopProduitsView();
            topProduits.setVisible(true);
        });
        
        // === MENU UTILISATEUR ===
        
        // Mon profil
        itemProfil.addActionListener(e -> {
            String username = SessionManager.getInstance().getLogin();
            String role = SessionManager.getInstance().getRole();
            
            JOptionPane.showMessageDialog(this,
                "<html><b>Profil Utilisateur</b><br><br>" +
                "Nom : " + username + "<br>" +
                "Rôle : " + role + "<br><br>" +
                "<i>Modification du profil disponible prochainement</i></html>",
                "Mon Profil",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Paramètres
        itemParametres.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Paramètres de l'application - À venir",
                "Paramètres",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Mes statistiques
        itemMesStats.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Vos statistiques personnelles - À venir",
                "Mes Statistiques",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Déconnexion
        itemDeconnexion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment vous déconnecter ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                SessionManager.getInstance().logout();
                dispose();
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            }
        });
        
        // Quitter
        itemQuitter.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Voulez-vous vraiment quitter l'application ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                SessionManager.getInstance().logout();
                System.exit(0);
            }
        });
    }
    
    private void initComponents() {
        setTitle("Menu Principal - Gestion Restaurant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(750, 550));
        
        // Créer la barre de menus
        createMenuBar();
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel central
        JPanel centerPanel = createCenterPanel();
        
        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(CARD_COLOR);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        // Message de bienvenue avec le nom de l'utilisateur connecté
        String username = SessionManager.getInstance().getLogin();
        
        lblWelcome = new JLabel("Bienvenue, " + username);
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        lblWelcome.setForeground(TEXT_PRIMARY);
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Sous-titre
        JLabel subtitle = new JLabel("Tableau de bord principal");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        headerPanel.add(lblWelcome);
        headerPanel.add(subtitle);
        
        return headerPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Titre
        JLabel sectionTitle = new JLabel("Modules disponibles");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(TEXT_PRIMARY);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        
        centerPanel.add(sectionTitle, BorderLayout.NORTH);
        
        // Grid des cartes
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        cardsPanel.setBackground(BACKGROUND_COLOR);
        cardsPanel.setPreferredSize(new Dimension(800, 500));
        
        cardsPanel.add(createModuleCard("GESTION", "Gérez vos produits, catégories et utilisateurs", "●", new Color(52, 152, 219)));
        cardsPanel.add(createModuleCard("STOCK", "Suivez vos entrées, sorties et inventaire", "■", new Color(46, 204, 113)));
        cardsPanel.add(createModuleCard("COMMANDES", "Gérez les commandes et leur historique", "◆", new Color(155, 89, 182)));
        cardsPanel.add(createModuleCard("STATISTIQUES", "Analysez vos performances et tendances", "▲", new Color(241, 196, 15)));
        
        centerPanel.add(cardsPanel, BorderLayout.CENTER);
        
        JPanel infoPanel = createQuickInfoPanel();
        centerPanel.add(infoPanel, BorderLayout.SOUTH);
        
        return centerPanel;
    }
    
    private JPanel createModuleCard(String title, String description, String icon, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Icône
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        iconLabel.setForeground(accentColor);
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        iconLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        iconLabel.setOpaque(true);
        iconLabel.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20));
        card.add(iconLabel);
        
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Titre
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(titleLabel);
        
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Description
        JLabel descLabel = new JLabel("<html><div style='width:100%;'>" + description + "</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLabel.setMaximumSize(new Dimension(400, 60));
        card.add(descLabel);
        
        card.add(Box.createVerticalGlue());
        
        return card;
    }
    
    private JPanel createQuickInfoPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        JLabel infoLabel = new JLabel("Utilisez la barre de menu pour accéder rapidement aux fonctionnalités");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        infoLabel.setForeground(TEXT_SECONDARY);
        
        panel.add(infoLabel);
        
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(CARD_COLOR);
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel footerLabel = new JLabel("© 2025 Système de Gestion de Restaurant • Version 2.0");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(TEXT_SECONDARY);
        footerPanel.add(footerLabel);
        
        return footerPanel;
    }
    
    private void createMenuBar() {
        menuBar = new JMenuBar();
        menuBar.setBackground(SECONDARY_COLOR);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, PRIMARY_COLOR));
        menuBar.setPreferredSize(new Dimension(0, 50));
        
        Font menuFont = new Font("Segoe UI", Font.BOLD, 13);
        Color menuForeground = Color.WHITE;
        
        // Menu Gestion
        menuGestion = createStyledMenu("Gestion", menuFont, menuForeground);
        itemProduits = createStyledMenuItem("Gestion des produits");
        itemCategories = createStyledMenuItem("Gestion des catégories");
        itemUtilisateurs = createStyledMenuItem("Gestion des utilisateurs");
        menuGestion.add(itemProduits);
        menuGestion.add(itemCategories);
        menuGestion.addSeparator();
        menuGestion.add(itemUtilisateurs);
        
        // Menu Stock
        menuStock = createStyledMenu("Stock", menuFont, menuForeground);
        itemEntreeStock = createStyledMenuItem("Entrée de stock");
        itemSortieStock = createStyledMenuItem("Sortie de stock");
        itemHistorique = createStyledMenuItem("Historique des mouvements");
        itemAlertesStock = createStyledMenuItem("Alertes de stock");
        menuStock.add(itemEntreeStock);
        menuStock.add(itemSortieStock);
        menuStock.addSeparator();
        menuStock.add(itemHistorique);
        menuStock.add(itemAlertesStock);
        
        // Menu Commandes
        menuCommandes = createStyledMenu("Commandes", menuFont, menuForeground);
        itemNouvelleCommande = createStyledMenuItem("Nouvelle commande");
        itemListeCommandes = createStyledMenuItem("Liste des commandes");
        menuCommandes.add(itemNouvelleCommande);
        menuCommandes.add(itemListeCommandes);
        
        // Menu Statistiques
        menuStats = createStyledMenu("Statistiques", menuFont, menuForeground);
        itemCAByDate = createStyledMenuItem("Chiffre d'affaires par date");
        itemTopProduits = createStyledMenuItem("Top produits vendus");
        menuStats.add(itemCAByDate);
        menuStats.add(itemTopProduits);
        
        // Menu Aide
        menuAide = createStyledMenu("Aide", menuFont, menuForeground);
        itemAide = createStyledMenuItem("Aide");
        itemAPropos = createStyledMenuItem("À propos");
        itemQuitter = createStyledMenuItem("Quitter");
        menuAide.add(itemAide);
        menuAide.add(itemAPropos);
        menuAide.addSeparator();
        menuAide.add(itemQuitter);
        
        // Ajouter les menus
        menuBar.add(menuGestion);
        menuBar.add(menuStock);
        menuBar.add(menuCommandes);
        menuBar.add(menuStats);
        menuBar.add(menuAide);
        
        // === ESPACE FLEXIBLE POUR POUSSER LE MENU UTILISATEUR À DROITE ===
        menuBar.add(Box.createHorizontalGlue());
        
        // === MENU UTILISATEUR À DROITE ===
        createUserMenu(menuFont, menuForeground);
        menuBar.add(menuUser);
        
        setJMenuBar(menuBar);
    }
    
    private void createUserMenu(Font menuFont, Color menuForeground) {
        // Utiliser les méthodes directes du SessionManager
        String username = SessionManager.getInstance().getLogin();
        String role = SessionManager.getInstance().getRole();

        // Récupérer l'icône selon le rôle
        String roleIcon = getRoleIcon(role);

        menuUser = new JMenu(roleIcon + " " + username + " (" + role + ") ▼");
        menuUser.setFont(menuFont);
        menuUser.setForeground(menuForeground);
        menuUser.setOpaque(true);
        menuUser.setBackground(PRIMARY_COLOR);
        menuUser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(255, 255, 255, 30)),
            BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));

        // Effet hover plus subtil
        menuUser.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                menuUser.setBackground(PRIMARY_COLOR.brighter().brighter());
                menuUser.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent evt) {
                menuUser.setBackground(PRIMARY_COLOR);
                menuUser.setForeground(menuForeground);
            }
        });

        // Créer les items avec icônes et style amélioré
        itemProfil = createStyledMenuItem("👤 Mon Profil", "Voir et modifier vos informations personnelles");
        itemParametres = createStyledMenuItem("⚙️ Paramètres", "Configurer les préférences de l'application");
        itemMesStats = createStyledMenuItem("📊 Mes Statistiques", "Consulter vos performances personnelles");
        itemDeconnexion = createStyledMenuItem("🚪 Déconnexion", "Quitter la session en cours");

        // Ajouter un header au menu déroulant
        JPopupMenu userPopup = menuUser.getPopupMenu();
        userPopup.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));

        // Ajouter un header avec les infos utilisateur
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 247, 250));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel userLabel = new JLabel("<html><b>" + username + "</b><br><small>" + role + "</small></html>");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        userLabel.setForeground(TEXT_PRIMARY);

        headerPanel.add(userLabel, BorderLayout.CENTER);

        // Créer un faux JMenuItem pour le header
        JMenuItem headerItem = new JMenuItem();
        headerItem.setEnabled(false);
        headerItem.setBackground(new Color(245, 247, 250));
        headerItem.setBorder(BorderFactory.createEmptyBorder());
        headerItem.setLayout(new BorderLayout());
        headerItem.add(headerPanel, BorderLayout.CENTER);

        // Insérer le header au début du menu
        userPopup.insert(headerItem, 0);
        userPopup.insert(new JSeparator(), 1);

        menuUser.add(itemProfil);
        menuUser.add(itemParametres);
        menuUser.add(itemMesStats);
        menuUser.addSeparator();
        menuUser.add(itemDeconnexion);

        // Effets hover améliorés pour la déconnexion
        itemDeconnexion.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                itemDeconnexion.setBackground(new Color(255, 235, 235));
                itemDeconnexion.setForeground(DANGER_COLOR);
                itemDeconnexion.setIcon(new ImageIcon(createWarningIcon()));
            }
            public void mouseExited(MouseEvent evt) {
                itemDeconnexion.setBackground(Color.WHITE);
                itemDeconnexion.setForeground(TEXT_PRIMARY);
                itemDeconnexion.setIcon(null);
            }
        });
    }

private String getRoleIcon(String role) {
    switch (role.toUpperCase()) {
        case "ADMIN":
            return "👑"; // Couronne pour admin
        case "MANAGER":
            return "💼"; // Mallette pour manager
        case "CHEF":
            return "👨‍🍳"; // Chef cuisinier
        case "SERVEUR":
            return "🤵"; // Serveur
        default:
            return "👤"; // Utilisateur par défaut
    }
}

private JMenuItem createStyledMenuItem(String text, String tooltip) {
    JMenuItem item = new JMenuItem(text);
    item.setToolTipText(tooltip);
    item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    item.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)),
        BorderFactory.createEmptyBorder(12, 25, 12, 25)
    ));
    item.setBackground(Color.WHITE);
    item.setForeground(TEXT_PRIMARY);
    item.setPreferredSize(new Dimension(280, 45));
    
    // Ajouter une marge à gauche pour les icônes
    item.setIconTextGap(15);
    
    item.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent evt) {
            item.setBackground(new Color(241, 250, 255));
            item.setForeground(PRIMARY_COLOR);
            // Petit effet de soulignement
            item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, PRIMARY_COLOR),
                BorderFactory.createEmptyBorder(12, 22, 12, 25)
            ));
        }
        public void mouseExited(MouseEvent evt) {
            item.setBackground(Color.WHITE);
            item.setForeground(TEXT_PRIMARY);
            item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)),
                BorderFactory.createEmptyBorder(12, 25, 12, 25)
            ));
        }
    });
    
    return item;
}

private BufferedImage createWarningIcon() {
    int size = 16;
    BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = image.createGraphics();
    
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(DANGER_COLOR);
    g2.fillOval(0, 0, size, size);
    g2.setColor(Color.WHITE);
    g2.setFont(new Font("Arial", Font.BOLD, 12));
    g2.drawString("!", 5, 13);
    g2.dispose();
    
    return image;
}
    
    private JMenu createStyledMenu(String text, Font font, Color foreground) {
        JMenu menu = new JMenu(text);
        menu.setFont(font);
        menu.setForeground(foreground);
        menu.setOpaque(true);
        menu.setBackground(SECONDARY_COLOR);
        menu.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        
        menu.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                menu.setBackground(PRIMARY_COLOR);
            }
            public void mouseExited(MouseEvent evt) {
                menu.setBackground(SECONDARY_COLOR);
            }
        });
        
        return menu;
    }
    
    private JMenuItem createStyledMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        item.setBackground(Color.WHITE);
        item.setForeground(TEXT_PRIMARY);
        item.setPreferredSize(new Dimension(250, 40));
        
        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                item.setBackground(new Color(241, 250, 255));
                item.setForeground(PRIMARY_COLOR);
            }
            public void mouseExited(MouseEvent evt) {
                item.setBackground(Color.WHITE);
                item.setForeground(TEXT_PRIMARY);
            }
        });
        
        return item;
    }
    
    public JMenuItem getItemProduits() { return itemProduits; }
    public JMenuItem getItemCategories() { return itemCategories; }
    public JMenuItem getItemUtilisateurs() { return itemUtilisateurs; }
    public JMenuItem getItemEntreeStock() { return itemEntreeStock; }
    public JMenuItem getItemSortieStock() { return itemSortieStock; }
    public JMenuItem getItemHistorique() { return itemHistorique; }
    public JMenuItem getItemAlertesStock() { return itemAlertesStock; }
    public JMenuItem getItemNouvelleCommande() { return itemNouvelleCommande; }
    public JMenuItem getItemListeCommandes() { return itemListeCommandes; }
    public JMenuItem getItemCAByDate() { return itemCAByDate; }
    public JMenuItem getItemTopProduits() { return itemTopProduits; }
    public JMenuItem getItemAide() { return itemAide; }
    public JMenuItem getItemAPropos() { return itemAPropos; }
    public JMenuItem getItemQuitter() { return itemQuitter; }
    
    public void setWelcomeMessage(String username) {
        lblWelcome.setText("Bienvenue, " + username);
    }
}