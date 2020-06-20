--
-- Base de données : `rob_world`
--

-- --------------------------------------------------------

--
-- Structure de la table `creatures`
--

CREATE TABLE `creatures` (
  `UUID` varchar(50) NOT NULL,
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

INSERT INTO `creatures` (`UUID`, `creature_template_uuid`, `map_id`, `pos_x`, `pos_y`, `pos_z`, `orientation`) VALUES
('7050ecad-d202-49f5-bc26-fbd9b53227e4', 1, 1, 2, 0, 1, 0),
('c0bae98a-4ed6-4427-927d-7e8b8da0241f', 1, 1, 0, 0, 0, 0),
('cb700998-d457-40ae-9025-fb30a8d7fda8', 2, 1, 2, 0, 2, 0);

-- --------------------------------------------------------

--
-- Structure de la table `creatures_templates`
--

CREATE TABLE `creatures_templates` (
  `id` int(11) AUTO_INCREMENT,
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
-- Index pour la table `creatures`
--
ALTER TABLE `creatures`
  ADD PRIMARY KEY (`UUID`);

--
-- Index pour la table `creatures_templates`
--
ALTER TABLE `creatures_templates`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `creatures_templates_locales`
--
ALTER TABLE `creatures_templates_locales`
  ADD PRIMARY KEY (`creature_template_id`,`locale`);
