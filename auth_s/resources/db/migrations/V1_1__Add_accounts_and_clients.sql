SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Base de données : `rob_auth`
--

--
-- Déchargement des données de la table `accounts`
--

INSERT INTO `accounts` (`guid`, `username`, `email`, `password`, `salt`, `token`, `date_ban_end`, `last_logged_in`, `created_at`) VALUES
('9ee5a885-8eba-4571-84dd-3ccb268c8a03', 'admin', 'admin@localhost', '$2b$12$iiLga/3mBu6QF1ECutYt2.0vQmti.mKglbqEZSE6nsMQ/eyHOCsRW', 'admin:9c5da4f621326c88f4d2a727b40be566bef3d7e2ad088eaa071e61bbaf38918b', NULL, NULL, NULL, '2020-04-21 19:21:17');
COMMIT;


SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Base de données : `rob_auth`
--

--
-- Déchargement des données de la table `clients`
--

INSERT INTO `clients` (`id`, `name`, `client_id`, `ip`, `secret`) VALUES
(1, 'ROB', 'a_client_id', '127.0.0.1', '');
COMMIT;
