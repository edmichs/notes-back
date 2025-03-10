# notes-back
Notes App back


# Notes API
Une API RESTful sécurisée et évolutive permettant aux utilisateurs de créer, lire, mettre à jour et supprimer des notes, avec des fonctionnalités de partage et de recherche.

# Caractéristiques


Création, lecture, mise à jour et suppression de notes
Partage de notes entre utilisateurs
Recherche de notes par mots-clés avec indexation de texte
Authentification basée sur JWT
Limitation de débit et protection contre les surcharges
API documentée avec Swagger/OpenAPI
Tests unitaires et d'intégration

Technologies utilisées

Java 21 - Langage de programmation
Spring Boot 3.3 - Framework principal
Spring Security - Pour l'authentification et l'autorisation
Spring Data JPA - Pour la persistence des données
PostgreSQL - Base de données
Flyway - Gestion des migrations de base de données
JWT - Pour l'authentification sans état
Resilience4j - Pour la limitation de débit et la résilience
Hibernate Search - Pour l'indexation de texte et la recherche
Swagger/OpenAPI - Pour la documentation de l'API
JUnit 5 & Mockito - Pour les tests
Docker & Docker Compose