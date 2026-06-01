# agence-immmobiliere - Backend API
##  Stack Technique
- **Langage** : Java 
- **Framework Principal** : Spring Boot
- **Sécurité** : Spring Security (Gestion des authentifications et des rôles)
- **Base de Données** : MySQL
- **Gestionnaire de Dépendances** : Maven
- **Architecture** : MVC (Modèle-Vue-Contrôleur)


##  Structure du Projet

```text
  |-- AgenceImmobilière
    |-- .mvn
      |-- wrapper
    |-- src
      |-- main
        |-- java
          |-- com
            |-- example
              |-- AgenceImmobilier
                |-- config
                |-- controllers
                |-- converter
                |-- DTOs
                  |-- request
                  |-- response
                |-- exceptions
                |-- models
                  |-- booking
                  |-- chat
                  |-- logement
                  |-- notification
                  |-- user
                |-- repositories
                  |-- bookingR
                  |-- chatR
                  |-- logementR
                  |-- notificationR
                  |-- userR
                |-- security
                  |-- jwt
                  |-- oauth2
                    |-- oAuth2Info
                  |-- services
                |-- services
                  |-- bookingS
                  |-- chatS
                  |-- flicker
                  |-- logementS
                  |-- notificationS
                  |-- user
                |-- utils
        |-- resources
      |-- test
        |-- java
          |-- com
            |-- example
              |-- AgenceImmobilier

