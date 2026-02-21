/*
 * Vue modernisée des statistiques et rapports
 */
package com.restaurant.views;

import com.restaurant.dao.CommandeDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class StatistiquesView extends JFrame {
    private JTextField txtDateDebut, txtDateFin;
    private JButton btnCalculer, btnAujourdhui, btnCetteSemaine, btnCeMois, btnExporter;
    private JTextArea txtResultats;
    private JLabel lblCAJour, lblCAMois, lblCATotal, lblNbCommandes;
    
    private CommandeDAO commandeDAO;
    private SimpleDateFormat dateFormat;
    
    // Couleurs du thème
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color INFO_COLOR = new Color(52, 152, 219);
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(44, 62, 80);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);
    
    public StatistiquesView() {
        initComponents();
        calculerStatsAujourdhui();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Statistiques et Rapports");
        setSize(1200, 850);
        setMinimumSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        commandeDAO = new CommandeDAO();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Centre avec cartes et rapport
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
        
        JLabel titleLabel = new JLabel("Statistiques & Rapports");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Analysez les performances de votre restaurant");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        // Bouton exporter
        btnExporter = createStyledButton("Exporter", INFO_COLOR);
        btnExporter.addActionListener(e -> exporterRapport());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.add(btnExporter);
        
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de sélection de période
        centerPanel.add(createPeriodPanel(), BorderLayout.NORTH);
        
        // Panel avec cartes statistiques et rapport
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(BACKGROUND_COLOR);
        
        // Cartes statistiques
        contentPanel.add(createStatsCardsPanel());
        
        // Rapport détaillé
        contentPanel.add(createReportPanel());
        
        centerPanel.add(contentPanel, BorderLayout.CENTER);
        
        return centerPanel;
    }
    
    private JPanel createPeriodPanel() {
        JPanel periodPanel = new JPanel(new BorderLayout());
        periodPanel.setBackground(CARD_COLOR);
        periodPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        // Titre de la section
        JLabel sectionLabel = new JLabel(" Période d'analyse");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionLabel.setForeground(TEXT_PRIMARY);
        
        // Champs de date
        JPanel dateFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        dateFieldsPanel.setBackground(CARD_COLOR);
        
        // Date début
        JLabel lblDebut = new JLabel("Du :");
        lblDebut.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDebut.setForeground(TEXT_PRIMARY);
        
        txtDateDebut = new JTextField(12);
        txtDateDebut.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDateDebut.setText(dateFormat.format(new Date()));
        txtDateDebut.setPreferredSize(new Dimension(120, 38));
        txtDateDebut.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Date fin
        JLabel lblFin = new JLabel("Au :");
        lblFin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblFin.setForeground(TEXT_PRIMARY);
        
        txtDateFin = new JTextField(12);
        txtDateFin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDateFin.setText(dateFormat.format(new Date()));
        txtDateFin.setPreferredSize(new Dimension(120, 38));
        txtDateFin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        dateFieldsPanel.add(lblDebut);
        dateFieldsPanel.add(txtDateDebut);
        dateFieldsPanel.add(Box.createHorizontalStrut(10));
        dateFieldsPanel.add(lblFin);
        dateFieldsPanel.add(txtDateFin);
        
        // Boutons de période rapide
        JPanel quickButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        quickButtonsPanel.setBackground(CARD_COLOR);
        
        btnAujourdhui = createStyledButton(" Aujourd'hui", PRIMARY_COLOR);
        btnCetteSemaine = createStyledButton(" Cette semaine", SUCCESS_COLOR);
        btnCeMois = createStyledButton(" Ce mois", WARNING_COLOR);
        btnCalculer = createStyledButton(" Calculer", INFO_COLOR);
        
        btnAujourdhui.addActionListener(e -> setPeriodeAujourdhui());
        btnCetteSemaine.addActionListener(e -> setPeriodeCetteSemaine());
        btnCeMois.addActionListener(e -> setPeriodeCeMois());
        btnCalculer.addActionListener(e -> calculerStatsPersonnalisees());
        
        quickButtonsPanel.add(btnAujourdhui);
        quickButtonsPanel.add(btnCetteSemaine);
        quickButtonsPanel.add(btnCeMois);
        quickButtonsPanel.add(btnCalculer);
        
        JPanel topPanel = new JPanel(new BorderLayout(0, 15));
        topPanel.setBackground(CARD_COLOR);
        topPanel.add(sectionLabel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(CARD_COLOR);
        centerPanel.add(dateFieldsPanel, BorderLayout.WEST);
        centerPanel.add(quickButtonsPanel, BorderLayout.EAST);
        
        topPanel.add(centerPanel, BorderLayout.CENTER);
        
        periodPanel.add(topPanel, BorderLayout.CENTER);
        
        return periodPanel;
    }
    
    private JPanel createStatsCardsPanel() {
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBackground(BACKGROUND_COLOR);
        
        // Titre
        JLabel titleLabel = new JLabel(" Indicateurs clés");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        cardsPanel.add(titleLabel);
        
        // Panel des cartes avec scroll
        JPanel cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(BACKGROUND_COLOR);
        
        // Cartes
        JPanel card1 = createBigStatCard("0,00 FCFA", "CA Aujourd'hui", "💰", PRIMARY_COLOR);
        JPanel card2 = createBigStatCard("0,00 FCFA", "CA Ce mois", "📊", SUCCESS_COLOR);
        JPanel card3 = createBigStatCard("0,00 FCFA", "CA Total (période)", "💵", WARNING_COLOR);
        JPanel card4 = createBigStatCard("0", "Jours avec vente", "🛒", INFO_COLOR);
        
        card1.setAlignmentX(Component.LEFT_ALIGNMENT);
        card2.setAlignmentX(Component.LEFT_ALIGNMENT);
        card3.setAlignmentX(Component.LEFT_ALIGNMENT);
        card4.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cardsContainer.add(card1);
        cardsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        cardsContainer.add(card2);
        cardsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        cardsContainer.add(card3);
        cardsContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        cardsContainer.add(card4);
        
        // Ajouter un scroll pour les petites fenêtres
        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cardsPanel.add(scrollPane);
        
        // Récupérer les labels pour mise à jour
        lblCAJour = getValueLabelFromCard(card1);
        lblCAMois = getValueLabelFromCard(card2);
        lblCATotal = getValueLabelFromCard(card3);
        lblNbCommandes = getValueLabelFromCard(card4);
        
        return cardsPanel;
    }
    
    private JLabel getValueLabelFromCard(JPanel card) {
        // Le label de valeur est dans le textPanel qui est au CENTER
        Component centerComp = ((BorderLayout) card.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (centerComp instanceof JPanel) {
            JPanel textPanel = (JPanel) centerComp;
            // Le premier composant du textPanel est le valueLabel
            return (JLabel) textPanel.getComponent(0);
        }
        return null;
    }
    
    private JPanel createBigStatCard(String value, String label, String icon, Color color) {
        JPanel cardPanel = new JPanel(new BorderLayout(15, 0));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Icône
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setPreferredSize(new Dimension(60, 60));
        
        // Textes
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(CARD_COLOR);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        labelText.setForeground(TEXT_SECONDARY);
        labelText.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        labelText.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(valueLabel);
        textPanel.add(labelText);
        
        cardPanel.add(iconLabel, BorderLayout.WEST);
        cardPanel.add(textPanel, BorderLayout.CENTER);
        
        return cardPanel;
    }
    
    private JPanel createReportPanel() {
        JPanel reportPanel = new JPanel(new BorderLayout(0, 15));
        reportPanel.setBackground(CARD_COLOR);
        reportPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Titre
        JLabel titleLabel = new JLabel(" Rapport détaillé");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        reportPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Zone de texte
        txtResultats = new JTextArea();
        txtResultats.setEditable(false);
        txtResultats.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtResultats.setLineWrap(false);
        txtResultats.setWrapStyleWord(false);
        txtResultats.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtResultats.setBackground(new Color(248, 249, 250));
        
        JScrollPane scrollPane = new JScrollPane(txtResultats);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        reportPanel.add(scrollPane, BorderLayout.CENTER);
        
        return reportPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(140, 38));
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
    
    private void setPeriodeAujourdhui() {
        Date aujourdhui = new Date();
        txtDateDebut.setText(dateFormat.format(aujourdhui));
        txtDateFin.setText(dateFormat.format(aujourdhui));
        calculerStatsPersonnalisees();
    }
    
    private void setPeriodeCetteSemaine() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        txtDateDebut.setText(dateFormat.format(cal.getTime()));
        
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        txtDateFin.setText(dateFormat.format(cal.getTime()));
        
        calculerStatsPersonnalisees();
    }
    
    private void setPeriodeCeMois() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        txtDateDebut.setText(dateFormat.format(cal.getTime()));
        
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        txtDateFin.setText(dateFormat.format(cal.getTime()));
        
        calculerStatsPersonnalisees();
    }
    
    private void calculerStatsAujourdhui() {
        Date aujourdhui = new Date();
        
        // CA d'aujourd'hui
        double caAujourdhui = commandeDAO.getChiffreAffairesByDate(aujourdhui);
        lblCAJour.setText(String.format("%,.0f FCFA", caAujourdhui));
        
        // CA du mois complet (du 1er jusqu'à aujourd'hui)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        Date debutMois = cal.getTime();
        double totalCAMois = 0;
        int joursAvecVente = 0;
        
        // Parcourir tous les jours du 1er du mois jusqu'à aujourd'hui INCLUS
        Calendar calTemp = Calendar.getInstance();
        calTemp.setTime(debutMois);
        
        while (!calTemp.getTime().after(aujourdhui)) {
            double caJour = commandeDAO.getChiffreAffairesByDate(calTemp.getTime());
            if (caJour > 0) {
                totalCAMois += caJour;
                joursAvecVente++;
            }
            calTemp.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        lblCAMois.setText(String.format("%,.0f FCFA", totalCAMois));
        lblCATotal.setText(String.format("%,.0f FCFA", totalCAMois));
        lblNbCommandes.setText(String.valueOf(joursAvecVente));
    }
    
    private void calculerStatsPersonnalisees() {
        try {
            Date dateDebut = dateFormat.parse(txtDateDebut.getText());
            Date dateFin = dateFormat.parse(txtDateFin.getText());
            
            if (dateDebut.after(dateFin)) {
                JOptionPane.showMessageDialog(this,
                    "⚠️ La date de début doit être antérieure à la date de fin",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Calculer le CA pour la période
            StringBuilder rapport = new StringBuilder();
            rapport.append("╔════════════════════════════════════════════════════════╗\n");
            rapport.append("║         RAPPORT DE STATISTIQUES - DÉTAILLÉ            ║\n");
            rapport.append("╚════════════════════════════════════════════════════════╝\n\n");
            
            rapport.append("📅 Période : ").append(txtDateDebut.getText())
                   .append(" → ").append(txtDateFin.getText()).append("\n");
            rapport.append("─────────────────────────────────────────────────────────\n\n");
            
            double totalCA = 0;
            int joursAvecVente = 0;
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateDebut);
            
            rapport.append("DÉTAIL PAR JOUR :\n\n");
            
            while (!cal.getTime().after(dateFin)) {
                Date dateCourante = cal.getTime();
                double caJour = commandeDAO.getChiffreAffairesByDate(dateCourante);
                
                if (caJour > 0) {
                    rapport.append("  📆 ").append(dateFormat.format(dateCourante))
                           .append("  →  ").append(String.format("%,15.0f FCFA", caJour))
                           .append("\n");
                    totalCA += caJour;
                    joursAvecVente++;
                }
                
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            
            if (joursAvecVente == 0) {
                rapport.append("\n  ℹ️  Aucune vente durant cette période\n");
            }
            
            rapport.append("\n─────────────────────────────────────────────────────────\n");
            rapport.append("RÉSUMÉ :\n\n");
            rapport.append("  💰 Chiffre d'affaires total   : ").append(String.format("%,15.0f FCFA", totalCA)).append("\n");
            rapport.append("  📊 Jours avec vente           : ").append(String.format("%15d", joursAvecVente)).append("\n");
            rapport.append("  📈 Moyenne journalière        : ").append(String.format("%,15.0f FCFA", totalCA / Math.max(1, joursAvecVente))).append("\n");
            rapport.append("\n╚════════════════════════════════════════════════════════╝\n");
            
            txtResultats.setText(rapport.toString());
            
            // Mise à jour des cartes
            lblCATotal.setText(String.format("%,.0f FCFA", totalCA));
            lblNbCommandes.setText(String.valueOf(joursAvecVente));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "❌ Format de date invalide. Utilisez JJ/MM/AAAA",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exporterRapport() {
        if (txtResultats.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Aucun rapport à exporter. Veuillez d'abord calculer des statistiques.",
                "Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter le rapport");
        fileChooser.setSelectedFile(new java.io.File("rapport_statistiques_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.PrintWriter writer = new java.io.PrintWriter(file);
                writer.write(txtResultats.getText());
                writer.close();
                
                JOptionPane.showMessageDialog(this,
                    "✅ Rapport exporté avec succès !\n\nFichier : " + file.getAbsolutePath(),
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