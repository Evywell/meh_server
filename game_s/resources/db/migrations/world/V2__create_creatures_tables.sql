--
-- Base de données : `rob_world`
--

-- --------------------------------------------------------

--
-- Structure de la table `creatures`
--

CREATE TABLE `creatures` (
  `uuid` INT PRIMARY KEY AUTO_INCREMENT,
  `creature_template_id` int(11) NOT NULL,
  `map_id` int(11) NOT NULL,
  `pos_x` float NOT NULL,
  `pos_y` float NOT NULL,
  `pos_z` float NOT NULL,
  `orientation` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `creatures`
--

INSERT INTO `creatures` (`uuid`, `creature_template_id`, `map_id`, `pos_x`, `pos_y`, `pos_z`, `orientation`) VALUES
(1, 1, 1, 2, 0, 1, 0),
(2, 1, 1, 0, 0, 0, 0),
(3, 2, 1, 2, 0, 2, 0);

-- --------------------------------------------------------

--
-- Structure de la table `creatures_templates`
--

CREATE TABLE `creatures_templates` (
  `id` int(11) PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `creatures_templates`
--

INSERT INTO `creatures_templates` (`id`, `name`) VALUES
(1, 'Sticky Spider'),
(2, 'Wolf'),
(3, 'Gobelin');

-- --------------------------------------------------------

--
-- Structure de la table `creatures_templates_locales`
--

CREATE TABLE `creatures_templates_locales` (
  `creature_template_id` int(11) NOT NULL,
  `locale` varchar(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `creatures_templates_locales`
--

INSERT INTO `creatures_templates_locales` (`creature_template_id`, `locale`, `name`) VALUES
(1, 'en', 'Sticky Spider'),
(1, 'fr', 'Araignée Collante');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `creatures_templates_locales`
--
ALTER TABLE `creatures_templates_locales`
  ADD PRIMARY KEY (`creature_template_id`,`locale`);
