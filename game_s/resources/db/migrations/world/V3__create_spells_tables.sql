-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Hôte : 172.17.0.3
-- Généré le :  jeu. 13 août 2020 à 11:29
-- Version du serveur :  5.7.28
-- Version de PHP :  7.2.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Base de données :  `rob_world`
--

-- --------------------------------------------------------

--
-- Structure de la table `spells`
--

CREATE TABLE `spells` (
  `ID` int(11) NOT NULL,
  `name` varchar(60) NOT NULL,
  `description` varchar(255) NOT NULL,
  `cost` int(10) UNSIGNED DEFAULT NULL,
  `resource_type` tinyint(3) UNSIGNED DEFAULT NULL,
  `cooldown` int(10) UNSIGNED DEFAULT NULL,
  `range_length` int(10) UNSIGNED DEFAULT NULL,
  `cast_time` int(10) UNSIGNED NOT NULL,
  `gcd` int(10) UNSIGNED NOT NULL,
  `gcd_category` tinyint(3) UNSIGNED NOT NULL,
  `duration` int(10) UNSIGNED NOT NULL,
  `school` int(11) NOT NULL,
  `flags` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `spells`
--
ALTER TABLE `spells`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `spells`
--
ALTER TABLE `spells`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

-- --------------------------------------------------------

--
-- Structure de la table `auras`
--

CREATE TABLE `auras` (
  `ID` int(10) UNSIGNED NOT NULL,
  `name` varchar(60) NOT NULL,
  `description` varchar(255) NOT NULL,
  `flags` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `auras`
--
ALTER TABLE `auras`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `auras`
--
ALTER TABLE `auras`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- Structure de la table `effects`
--

CREATE TABLE `effects` (
  `ID` int(10) UNSIGNED NOT NULL,
  `name` varchar(50) NOT NULL,
  `param1` varchar(25) DEFAULT NULL,
  `param2` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `effects`
--
ALTER TABLE `effects`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `effects`
--
ALTER TABLE `effects`
  MODIFY `ID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;


--
-- Structure de la table `spells_effects`
--

CREATE TABLE `spells_effects` (
  `SPELL_ID` int(10) UNSIGNED NOT NULL,
  `EFFECT_ID` int(10) UNSIGNED NOT NULL,
  `value1` int(11),
  `value2` int(11),
  `position` int(11) NOT NULL,
  `target_mask` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `spells_effects`
--
ALTER TABLE `spells_effects`
  ADD PRIMARY KEY (`SPELL_ID`,`EFFECT_ID`);


--
-- Structure de la table `auras_effects`
--

CREATE TABLE `auras_effects` (
  `AURA_ID` int(11) NOT NULL,
  `EFFECT_ID` int(11) NOT NULL,
  `value1` int(11) DEFAULT NULL,
  `value2` int(11) DEFAULT NULL,
  `position` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `auras_effects`
--
ALTER TABLE `auras_effects`
  ADD PRIMARY KEY (`AURA_ID`,`EFFECT_ID`);

--
-- Structure de la table `spells_scripts`
--

CREATE TABLE `spells_scripts` (
  `SPELL_ID` int(10) UNSIGNED NOT NULL,
  `script_name` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `spells_scripts`
--
ALTER TABLE `spells_scripts`
  ADD PRIMARY KEY (`SPELL_ID`,`script_name`);
COMMIT;
