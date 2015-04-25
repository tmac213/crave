USE restaurantsApp;

DROP USER 'appUser'@'localhost';
FLUSH PRIVILEGES;
CREATE USER 'appUser'@'localhost' IDENTIFIED BY 'Food216';
GRANT SELECT ON restaurantsApp.* TO 'appuser'@'localhost';
GRANT INSERT ON restaurantsApp.* TO 'appuser'@'localhost';
GRANT UPDATE ON restaurantsApp.* TO 'appuser'@'localhost';
GRANT DELETE ON restaurantsApp.* TO 'appuser'@'localhost';

DROP TABLE IF EXISTS users;
CREATE TABLE users (
    ID      INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name    VARCHAR(45) NOT NULL,
    username    VARCHAR(45) NOT NULL,

    PRIMARY KEY (ID),
    UNIQUE (username)
);

DROP TABLE IF EXISTS restaurants;
CREATE TABLE restaurants (
    ID      INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name    VARCHAR(45) NOT NULL,
    address VARCHAR(45) NOT NULL,
    
    PRIMARY KEY (ID),
    UNIQUE (name, address)
);

DROP TABLE IF EXISTS dishes;
CREATE TABLE dishes (
    ID          INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name        VARCHAR(45) NOT NULL,
    avgPrice    INT UNSIGNED,
    avgRating   SMALLINT UNSIGNED,
    
    PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS types;
CREATE TABLE types (
    ID      INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name    VARCHAR(45) NOT NULL,

    PRIMARY KEY (ID),
    UNIQUE (name)
);

DROP TABLE IF EXISTS regions;
CREATE TABLE regions (
    ID      INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name    VARCHAR(45) NOT NULL,
    
    PRIMARY KEY (ID),
    UNIQUE (name)
);

DROP TABLE IF EXISTS origins;
CREATE TABLE origins (
    ID      INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name    VARCHAR(45) NOT NULL,

    PRIMARY KEY (ID),
    UNIQUE (name)
);

DROP TABLE IF EXISTS dishOrigin;
CREATE TABLE dishOrigin (
    dID     INT UNSIGNED NOT NULL,
    oID    INT UNSIGNED NOT NULL,

    PRIMARY KEY (dID, oID),
    FOREIGN KEY (dID) REFERENCES dishes(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (oID) REFERENCES origins(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS dishType;
CREATE TABLE dishType (
    dID     INT UNSIGNED NOT NULL,
    tID     INT UNSIGNED NOT NULL,

    PRIMARY KEY (dID, tID),
    FOREIGN KEY (dID) REFERENCES dishes(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (tID) REFERENCES types(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS restaurantOrigin;
CREATE TABLE restaurantOrigin (
    rID     INT UNSIGNED NOT NULL,
    oID    INT UNSIGNED NOT NULL,

    PRIMARY KEY (rID, oID),
    FOREIGN KEY (rID) REFERENCES restaurants(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (oID) REFERENCES origins(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS locatedIn;
CREATE TABLE locatedIn (
    resID   INT UNSIGNED NOT NULL,
    regID   INT UNSIGNED NOT NULL,
    
    PRIMARY KEY (resID, regID),
    FOREIGN KEY (resID) REFERENCES restaurants(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (regID) REFERENCES regions(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS serves;
CREATE TABLE serves (
    rID     INT UNSIGNED NOT NULL,
    dID     INT UNSIGNED NOT NULL,
    price   INT UNSIGNED NOT NULL,
    description VARCHAR(100),
    
    PRIMARY KEY (rID, dID),
    FOREIGN KEY (rID) REFERENCES restaurants(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (dID) REFERENCES dishes(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
DELIMITER //
DROP TRIGGER IF EXISTS update_avg_price//
CREATE TRIGGER update_avg_price AFTER INSERT ON serves
FOR EACH ROW BEGIN
    DECLARE oldCount INT;
    SET oldCount = (SELECT  count(*)
                    FROM    serves
                    WHERE   dID = NEW.dID);
    UPDATE dishes SET avgPrice = ((avgPrice*oldCount) + NEW.price) / (oldCount + 1);
END//
DELIMITER ;


DROP TABLE IF EXISTS visits;
CREATE TABLE visits (
    uID     INT UNSIGNED NOT NULL,
    rID     INT UNSIGNED NOT NULL,
    times   INT UNSIGNED NOT NULL default 0,
    
    PRIMARY KEY (uID, rID),
    FOREIGN KEY (uID) REFERENCES users(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (rID) REFERENCES restaurants(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS reviews;
CREATE TABLE reviews (
    rID     INT UNSIGNED NOT NULL AUTO_INCREMENT,
    uID     INT UNSIGNED NOT NULL,
    dID     INT UNSIGNED NOT NULL,
    rating  SMALLINT UNSIGNED NOT NULL,
    
    PRIMARY KEY (rID),
    FOREIGN KEY (uID) REFERENCES users(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (dID) REFERENCES dishes(ID)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
DELIMITER //
CREATE TRIGGER update_avg_rating AFTER INSERT ON reviews
FOR EACH ROW BEGIN
    DECLARE oldCount INT;
    SET oldCount = (SELECT  count(*)
                    FROM    serves
                    WHERE   dID = NEW.dID);
    UPDATE dishes SET avgRating = ((avgRating*oldCount) + NEW.rating) / (oldCount + 1);
END//
DELIMITER ;

