--
-- Base de données : `rob_world`
--

-- --------------------------------------------------------

--
-- Structure de la table `maps`
--

CREATE TABLE `maps` (
  `map_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `max_players` int(11) NOT NULL DEFAULT '-1',
  `grid_w` int(11) NOT NULL,
  `grid_h` int(11) NOT NULL,
  `available` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `maps`
--

INSERT INTO `maps` (`map_id`, `name`, `max_players`, `grid_w`, `grid_h`, `available`) VALUES
(1, 'La première map', -1, 50, 50, 1);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `maps`
--
ALTER TABLE `maps`
  ADD PRIMARY KEY (`map_id`);
