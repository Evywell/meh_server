CREATE TABLE accounts (
    guid varchar(255) PRIMARY KEY,
    username VARCHAR(65) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    salt VARCHAR(255),
    token VARCHAR(255),
    date_ban_end DATETIME,
    last_logged_in DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";
-- --------------------------------------------------------

--
-- Structure de la table `clients`
--

CREATE TABLE `clients` (
   `id` int(11) NOT NULL,
   `name` varchar(255) NOT NULL,
   `client_id` varchar(255) NOT NULL,
   `ip` varchar(50) NOT NULL,
   `secret` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `clients`
--
ALTER TABLE `clients`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `name` (`name`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `clients`
--
ALTER TABLE `clients`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;