# 🍽️ GestionRestaurantIAI — Application de Gestion de Restaurant

> Projet académique développé par **KOMBATE Jean**  
> Étudiant L2 Informatique — IAI-TOGO | [github.com/JeanKombate](https://github.com/JeanKombate)

---

## 📋 À Propos

**GestionRestaurantIAI** est une application de bureau développée en Java dans le cadre de ma formation à l'IAI-TOGO. Elle permet de gérer efficacement les opérations d'un restaurant : commandes, menu, tables et employés, le tout via une interface graphique intuitive connectée à une base de données MySQL.

> ⚠️ **Statut** : Fonctionnel en local — projet académique en cours de développement.

---

## ✨ Fonctionnalités

- 🛒 **Gestion des commandes** — Créer, suivre et clôturer les commandes des clients
- 🍕 **Gestion du menu / des plats** — Ajouter, modifier et supprimer des plats
- 👥 **Gestion des utilisateurs / employés** — Gérer les comptes et les accès
- 🪑 **Gestion des tables** — Suivre la disponibilité des tables en temps réel
- 🖥️ **Interface graphique** — Application desktop avec fenêtres et boutons (Java Swing)
- 🗄️ **Base de données** — Stockage persistant des données avec MySQL Server

---

## 🛠️ Technologies Utilisées

| Élément | Technologie |
|--------|------------|
| Langage | Java |
| Interface graphique | Java Swing |
| Base de données | MySQL Server |
| Paradigme | Programmation Orientée Objet (POO) |
| Structures de données | Listes, tableaux |
| IDE | Apache NetBeans |
| Versioning | Git / GitHub |

---

## 📁 Structure du Projet

```
GestionRestaurantIAI/
│
├── src/
│   └── gestionrestaurantiai/
│       ├── Main.java
│       ├── models/
│       │   ├── Commande.java
│       │   ├── Plat.java
│       │   ├── Table.java
│       │   └── Utilisateur.java
│       ├── views/
│       │   ├── MainFrame.java
│       │   └── ...
│       └── utils/
│           └── DatabaseConnection.java
│
├── database/
│   └── restaurant.sql
│
└── README.md
```

---

## 🔧 Installation & Lancement en local

### Prérequis

- Java JDK 8 ou supérieur
- MySQL Server installé et configuré
- Apache NetBeans (recommandé)
- Git

### Étapes

1. **Cloner le projet**

```bash
git clone https://github.com/JeanKombate/gestion-restaurant-java.git
```

2. **Importer dans NetBeans**

   - Ouvrir NetBeans
   - `File` → `Open Project`
   - Sélectionner le dossier cloné

3. **Configurer la base de données**

   Créer la base de données MySQL :

```sql
CREATE DATABASE restaurant;
```

   Puis importer le fichier SQL :

```bash
mysql -u root -p restaurant < database/restaurant.sql
```

4. **Configurer la connexion**

   Mettre à jour les paramètres dans `src/utils/DatabaseConnection.java` :

```java
String url = "jdbc:mysql://localhost:3306/restaurant";
String user = "root";
String password = "votre_mot_de_passe";
```

5. **Lancer l'application**

   Dans NetBeans : cliquer sur le bouton **▶ Run** ou appuyer sur `F6`

---

## 🚧 Améliorations prévues

- [ ] Génération de factures PDF
- [ ] Tableau de bord avec statistiques
- [ ] Système de rapports journaliers

---

## 👨‍💻 Auteur

**KOMBATE Jean**  
Étudiant en L2 Informatique à l'IAI-TOGO — Spécialisation GLSI prévue en 3ème année.  
Passionné par le développement logiciel et l'Intelligence Artificielle appliquée au domaine médical.

- 📧 k2007.jean@gmail.com
- 🔗 [github.com/JeanKombate](https://github.com/JeanKombate)

---

## 📄 Licence

Ce projet est open source — libre d'utilisation à des fins académiques.

---

*Projet académique réalisé dans le cadre de la Licence en Informatique — IAI-TOGO 2025-2026 ⚙️*
