CREATE TABLE IF NOT EXISTS characters (
    uuid INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    map_id INT,
    pos_x FLOAT,
    pos_y FLOAT,
    pos_z FLOAT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP ,
    account_guid VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;