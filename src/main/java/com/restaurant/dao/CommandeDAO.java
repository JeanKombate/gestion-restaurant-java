package com.restaurant.dao;

import com.restaurant.models.Commande;
import com.restaurant.models.LigneCommande;
import com.restaurant.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeDAO {
    
    // NE PAS avoir de variable de connexion d'instance
    
    public CommandeDAO() {
        // Constructeur vide
    }
    
    // Créer une nouvelle commande
    public int create(Commande commande) {
        String sql = "INSERT INTO Commande (dateCommande, etat, total) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setTimestamp(1, new Timestamp(commande.getDateCommande().getTime()));
            stmt.setString(2, commande.getEtat());
            stmt.setDouble(3, commande.getTotal());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int newId = rs.getInt(1);
                System.out.println("Commande créée avec ID: " + newId);
                return newId;
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur création commande: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return -1;
    }
    
    // Lire une commande par ID
    public Commande read(int id) {
        String sql = "SELECT * FROM Commande WHERE id = ?";
        Commande commande = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                commande = new Commande();
                commande.setId(rs.getInt("id"));
                commande.setDateCommande(rs.getTimestamp("dateCommande"));
                commande.setEtat(rs.getString("etat"));
                commande.setTotal(rs.getDouble("total"));
                
                // Charger les lignes de commande
                LigneCommandeDAO ligneDAO = new LigneCommandeDAO();
                commande.setLignes(ligneDAO.getByCommande(id));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lecture commande: " + e.getMessage());
        }
        
        return commande;
    }
    
    // Mettre à jour une commande
    public void update(Commande commande) {
        String sql = "UPDATE Commande SET etat = ?, total = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, commande.getEtat());
            stmt.setDouble(2, commande.getTotal());
            stmt.setInt(3, commande.getId());
            stmt.executeUpdate();
            
            System.out.println("Commande mise à jour: " + commande.getId());
            
        } catch (SQLException e) {
            System.err.println("Erreur mise à jour commande: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // Supprimer une commande
    public void delete(int id) {
        String sql = "DELETE FROM Commande WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            
            System.out.println("Commande supprimée: " + id);
            
        } catch (SQLException e) {
            System.err.println("Erreur suppression commande: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    // Obtenir toutes les commandes
    public List<Commande> getAll() {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM Commande ORDER BY dateCommande DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Commande commande = new Commande();
                commande.setId(rs.getInt("id"));
                commande.setDateCommande(rs.getTimestamp("dateCommande"));
                commande.setEtat(rs.getString("etat"));
                commande.setTotal(rs.getDouble("total"));
                commandes.add(commande);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération commandes: " + e.getMessage());
        }
        
        return commandes;
    }
    
    // Obtenir les commandes par état
    public List<Commande> getByEtat(String etat) {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM Commande WHERE etat = ? ORDER BY dateCommande DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, etat);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Commande commande = new Commande();
                commande.setId(rs.getInt("id"));
                commande.setDateCommande(rs.getTimestamp("dateCommande"));
                commande.setEtat(rs.getString("etat"));
                commande.setTotal(rs.getDouble("total"));
                commandes.add(commande);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération commandes par état: " + e.getMessage());
        }
        
        return commandes;
    }
    
    // Valider une commande - NOUVELLE VERSION
    public void validerCommande(int commandeId) {
        Connection conn = null;
        try {
              // Utiliser getNewConnection() pour éviter les problèmes de ThreadLocal
            conn = DBConnection.getNewConnection();
            conn.setAutoCommit(false);
            
            // 1. Vérifier que la commande existe et est en cours
            String sqlCheck = "SELECT etat FROM Commande WHERE id = ?";
            String etat = null;
            try (PreparedStatement stmt = conn.prepareStatement(sqlCheck)) {
                stmt.setInt(1, commandeId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    etat = rs.getString("etat");
                } else {
                    throw new RuntimeException("Commande #" + commandeId + " non trouvée");
                }
            }
            
            if (!"EN_COURS".equals(etat)) {
                throw new RuntimeException("La commande n'est pas en cours. État: " + etat);
            }
            
            // 2. Récupérer les lignes de commande
            List<LigneCommande> lignes = getLignesForTransaction(commandeId, conn);
            
            // 3. Vérifier les stocks
            for (LigneCommande ligne : lignes) {
                verifierStockDisponible(ligne.getProduit().getId(), ligne.getQuantite(), conn);
            }
            
            // 4. Diminuer les stocks et enregistrer les mouvements
            for (LigneCommande ligne : lignes) {
                // Diminuer le stock
                String sqlUpdateStock = "UPDATE Produit SET stockActuel = stockActuel - ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateStock)) {
                    stmt.setInt(1, ligne.getQuantite());
                    stmt.setInt(2, ligne.getProduit().getId());
                    stmt.executeUpdate();
                }
                
                // Enregistrer le mouvement de sortie
                String sqlMouvement = "INSERT INTO MouvementStock (type, produit_id, quantite, motif) " +
                                     "VALUES ('SORTIE', ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlMouvement)) {
                    stmt.setInt(1, ligne.getProduit().getId());
                    stmt.setInt(2, ligne.getQuantite());
                    stmt.setString(3, "Vente - Commande #" + commandeId);
                    stmt.executeUpdate();
                }
            }
            
            // 5. Mettre à jour l'état de la commande
            String sqlUpdateCommande = "UPDATE Commande SET etat = 'VALIDÉE' WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateCommande)) {
                stmt.setInt(1, commandeId);
                stmt.executeUpdate();
            }
            
            // 6. Valider la transaction
            conn.commit();
            System.out.println("✅ Commande #" + commandeId + " validée avec succès");
            
        } catch (Exception e) {
            // Annuler la transaction en cas d'erreur
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("❌ Transaction annulée pour commande #" + commandeId);
                } catch (SQLException ex) {
                    System.err.println("Erreur lors du rollback: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Erreur validation commande #" + commandeId + ": " + e.getMessage(), e);
        } finally {
            // Réactiver auto-commit mais NE PAS fermer la connexion
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close(); // Fermer cette connexion dédiée
                } catch (SQLException e) {
                    System.err.println("Erreur réactivation auto-commit: " + e.getMessage());
                }
            }
        }
    }
    
    // Annuler une commande - NOUVELLE VERSION
    public void annulerCommande(int commandeId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // 1. Vérifier l'état actuel
            String sqlCheck = "SELECT etat FROM Commande WHERE id = ?";
            String etat = null;
            try (PreparedStatement stmt = conn.prepareStatement(sqlCheck)) {
                stmt.setInt(1, commandeId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    etat = rs.getString("etat");
                } else {
                    throw new RuntimeException("Commande #" + commandeId + " non trouvée");
                }
            }
            
            if ("ANNULÉE".equals(etat)) {
                throw new RuntimeException("La commande est déjà annulée");
            }
            
            // 2. Si la commande était validée, remettre les stocks
            if ("VALIDÉE".equals(etat)) {
                List<LigneCommande> lignes = getLignesForTransaction(commandeId, conn);
                
                for (LigneCommande ligne : lignes) {
                    // Remettre le stock
                    String sqlUpdateStock = "UPDATE Produit SET stockActuel = stockActuel + ? WHERE id = ?";
                    try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateStock)) {
                        stmt.setInt(1, ligne.getQuantite());
                        stmt.setInt(2, ligne.getProduit().getId());
                        stmt.executeUpdate();
                    }
                    
                    // Enregistrer le mouvement d'entrée
                    String sqlMouvement = "INSERT INTO MouvementStock (type, produit_id, quantite, motif) " +
                                         "VALUES ('ENTRÉE', ?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sqlMouvement)) {
                        stmt.setInt(1, ligne.getProduit().getId());
                        stmt.setInt(2, ligne.getQuantite());
                        stmt.setString(3, "Annulation - Commande #" + commandeId);
                        stmt.executeUpdate();
                    }
                }
            }
            
            // 3. Mettre à jour l'état de la commande
            String sqlUpdateCommande = "UPDATE Commande SET etat = 'ANNULÉE' WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateCommande)) {
                stmt.setInt(1, commandeId);
                stmt.executeUpdate();
            }
            
            conn.commit();
            System.out.println("✅ Commande #" + commandeId + " annulée avec succès");
            
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erreur lors du rollback: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Erreur annulation commande #" + commandeId + ": " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Erreur réactivation auto-commit: " + e.getMessage());
                }
            }
        }
    }
    
    // Méthode utilitaire pour récupérer les lignes dans une transaction
    private List<LigneCommande> getLignesForTransaction(int commandeId, Connection conn) throws SQLException {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql = "SELECT lc.*, p.nom, p.prixVente " +
                    "FROM LigneCommande lc " +
                    "JOIN Produit p ON lc.produit_id = p.id " +
                    "WHERE lc.commande_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                LigneCommande ligne = new LigneCommande();
                ligne.setId(rs.getInt("id"));
                ligne.setQuantite(rs.getInt("quantite"));
                ligne.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                ligne.setMontantLigne(rs.getDouble("montantLigne"));
                
                com.restaurant.models.Produit produit = new com.restaurant.models.Produit();
                produit.setId(rs.getInt("produit_id"));
                produit.setNom(rs.getString("nom"));
                produit.setPrixVente(rs.getDouble("prixVente"));
                ligne.setProduit(produit);
                
                lignes.add(ligne);
            }
        }
        return lignes;
    }
    
    // Méthode utilitaire pour vérifier le stock
    private void verifierStockDisponible(int produitId, int quantiteDemandee, Connection conn) throws SQLException {
        String sql = "SELECT stockActuel, nom FROM Produit WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produitId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int stockActuel = rs.getInt("stockActuel");
                String nomProduit = rs.getString("nom");
                
                if (stockActuel < quantiteDemandee) {
                    throw new RuntimeException("Stock insuffisant pour " + nomProduit + 
                                             ". Stock disponible: " + stockActuel + 
                                             ", Quantité demandée: " + quantiteDemandee);
                }
            } else {
                throw new RuntimeException("Produit non trouvé: ID " + produitId);
            }
        }
    }
    
    // Obtenir le chiffre d'affaires pour une date
    public double getChiffreAffairesByDate(java.util.Date date) {
        String sql = "SELECT SUM(total) as CA FROM Commande " +
                    "WHERE etat = 'VALIDÉE' AND CONVERT(DATE, dateCommande) = CONVERT(DATE, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, new java.sql.Date(date.getTime()));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("CA");
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur calcul CA par date: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    // Obtenir le top des produits vendus
    public List<Object[]> getTopProduits(int limit) {
        List<Object[]> topProduits = new ArrayList<>();
        String sql = "SELECT TOP(?) p.nom, SUM(lc.quantite) as total_vendu " +
                    "FROM LigneCommande lc " +
                    "JOIN Produit p ON lc.produit_id = p.id " +
                    "JOIN Commande c ON lc.commande_id = c.id " +
                    "WHERE c.etat = 'VALIDÉE' " +
                    "GROUP BY p.nom " +
                    "ORDER BY total_vendu DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("nom"),
                    rs.getInt("total_vendu")
                };
                topProduits.add(row);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur récupération top produits: " + e.getMessage());
        }
        
        return topProduits;
    }
}