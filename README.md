# Projet Spring Gr2

## Description
Ce projet est une application Spring Boot qui interagit avec une base de données MariaDB pour gérer des films. Les données des films sont importées depuis un fichier JSON.

## Technologies Utilisées
- **Spring Boot**: 3.3.3
- **Java**: 17
- **MariaDB**: 10.6
- **Maven**: 3.9.2
- **Docker**: pour la conteneurisation

## Configuration

### Fichiers de Configuration
- **`application.properties`**: Configuration de l'application, incluant la connexion à la base de données et le chemin vers le fichier JSON.
- **`pom.xml`**: Fichier de configuration Maven qui définit les dépendances du projet.

### Base de Données
- **Nom de la base de données**: `projetgr2`
- **Utilisateur**: `root`
- **Mot de passe**: (vide)

### Fichier JSON
- **Chemin**: `/app/data/films.json`
- Ce fichier doit être monté dans le conteneur Docker.

## Instructions de Développement

### Prérequis
- Avoir Docker et Docker Compose installés.

### Lancer l'application
1. Cloner le dépôt :
   ```bash
   git clone <URL_DU_DEPOT>
   cd projetSpringGr2

2. Construire et démarrer les conteneurs:
    ```bash
   docker-compose up --build

3. Démarrer le serveur
    ```bash
   http://localhost:8080/
   
3. Récupérer tous les noms des films
    ```bash
   http://localhost:8080/movie/all
   

