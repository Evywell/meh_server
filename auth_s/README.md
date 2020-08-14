# Base de données
accounts:
 - id INT PRIMARY KEY AUTO_INCREMENT
 - uuid VARCHAR
 - username VARCHAR
 - password VARCHAR
 - salt VARCHAR
 - token VARCHAR
 - date_ban_end DATETIME
 - last_logged_in DATETIME
 - created_at DATETIME
 
sessions:
 - account_id INT FOREIGN KEY accounts(id) PRIMARY KEY
 - game_code enum(ROB) PRIMARY KEY
 - session_id VARCHAR*
 - created_at DATETIME
 
* La clé session_id est utilisé pour savoir si une session de jeu est en cours

Processus d'authentification:
1. Le jeu demande au serveur d'authentification un token en échange d'un login/mot de passe
2. Le serveur d'authentification vérifie le login/mot de passe
3.a. Le serveur accepte l'authentification
3.b. Le serveur refuse l'authentification (Fin du processus)
4. Le serveur d'authentification retourne un token au jeu
5. Le jeu demande la création d'une session pour le jeu ROB
6. Le serveur d'authentification vérifie le token
7.a. Le token est bon
7.b. Le token est incorrect (Fin du processus)
8. Le serveur d'authentification vérifie si une session de jeu pour le jeu en question (game_code) est en cours
9.a. Aucune session de jeu pour le jeu en question n'est en cours
9.b. Une session de jeu est en cours (Fin du processus)
10. Un session est créé et le serveur d'authentification retourne la session_id
11. Une socket sera créé avec le serveur de jeu pour cette session_id

On va aussi utiliser une sorte d'OAuth custom