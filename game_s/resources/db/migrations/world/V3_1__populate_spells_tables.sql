--
-- Spells
--
INSERT INTO `spells` (`ID`, `name`, `description`, `cost`, `resource_type`, `cooldown`, `range_length`, `cast_time`, `gcd`, `gcd_category`, `duration`, `school`, `flags`) VALUES
('1', 'Frostbold', 'Launches a frostbolt', '20', '1', '0', '5', '2000', '1500', '1', '5', '2', '0');

--
-- Effects
--
INSERT INTO `effects` (`ID`, `name`, `param1`, `param2`) VALUES
('1', 'APPLY_AURA', 'aura', NULL),
('2', 'SCHOOL_DAMAGE', 'value', 'school'),
('3', 'DECREASE_MOVEMENT_SPEED_PERCENT', 'value', NULL),
('4', 'INSTANT_KILL', NULL, NULL);

--
-- Auras
--
INSERT INTO `auras` (`ID`, `name`, `description`, `flags`) VALUES
('1', 'Frostbolt', "I'm slow, but by what ??", '0');

--
-- Spells Effects
--
INSERT INTO `spells_effects` (`SPELL_ID`, `EFFECT_ID`, `value1`, `value2`, `position`) VALUES
('1', '1', '1', NULL, '0'); -- Add effect APPLY_AURA with the AURA_ID 1 (Frostbolt)

--
-- Auras Effects
--
INSERT INTO `auras_effects` (`AURA_ID`, `EFFECT_ID`, `value1`, `value2`, `position`) VALUES
('1', '3', '30', NULL, '0'); -- Add effect DECREASE_MOVEMENT_SPEED_PERCENT with the value 30 (30% slow)