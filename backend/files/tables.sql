CREATE DATABASE `save-em`;
USE `save-em`;

DROP TABLE `persons`;

CREATE TABLE `persons`(
	`id` INT auto_increment,
    `firstname` VARCHAR(64) NOT NULL,
    `lastname` VARCHAR(128) NOT NULL,
    `birthdate` DATE NOT NULL,
    `sex` INT NOT NULL,
    `nationality` VARCHAR(128) NOT NULL,
    `height` DOUBLE DEFAULT 0.00,
    `hairColor` VARCHAR(24),
    `weight` DOUBLE DEFAULT 0.00,
    `gpsLocation` VARCHAR(128) DEFAULT NULL,
    `found` BOOLEAN DEFAULT FALSE,
    `comments` TEXT DEFAULT NULL,
    `missingDate` DATETIME DEFAULT NOW(),
    
    PRIMARY KEY(`id`)
)ENGINE=INNODB;

INSERT INTO `persons`(`firstname`,`lastname`,`birthdate`,`sex`,`nationality`,`height`,`hairColor`,`weight`, `gpsLocation`, `comments`)
	VALUES('John','Smith',NOW(),1,'Mexican',1.68,'Black',68.5,'120.00000;456.2329015','Lorem ipsum dolor');

INSERT INTO `persons`(`firstname`,`lastname`,`birthdate`,`sex`,`nationality`,`height`,`hairColor`,`weight`, `gpsLocation`, `comments`)
	VALUES('Henry','Kissinger',NOW(),1,'French',1.78,'Gray',74.3,'065.1245487;120.0546546','Lorem ipsum dolor');


SELECT * FROM `persons`;

DROP TABLE `userKeys`;

CREATE TABLE `userKeys`(
	`id` INT AUTO_INCREMENT,
    `key` VARCHAR(256) UNIQUE NOT NULL,
    `dateOfRegistration` DATETIME DEFAULT NOW(),
    
    PRIMARY KEY(`id`)
)ENGINE=INNODB;

INSERT INTO `userKeys` (`key`) VALUES ('133243423434');

SELECT * FROM `userKeys`;

DROP TABLE `key_has_person`;

CREATE TABLE `key_has_person`(
	`fk_key` VARCHAR(256) NOT NULL,
    `fk_person` INT NOT NULL,
    
    PRIMARY KEY(`fk_key`,`fk_person`),
    CONSTRAINT `fk_user_key` FOREIGN KEY
		(`fk_key`) REFERENCES `userKeys`(`key`),
	CONSTRAINT `fk_person_id` FOREIGN KEY
		(`fk_person`) REFERENCES `persons`(`id`)
)ENGINE=INNODB;

INSERT INTO `key_has_person` (`fk_key`, `fk_person`) VALUES ('133243423434', 1);

SELECT * FROM `key_has_person`;

CREATE TABLE `person_log`(
	`id` INT AUTO_INCREMENT,
    `date` DATETIME DEFAULT NOW(),
    `operation` VARCHAR(64) DEFAULT 'select',
	`fk_key` VARCHAR(256) NOT NULL,
    
    PRIMARY KEY(`id`),
    FOREIGN KEY (`fk_key`)
		REFERENCES `userKeys`(`key`)
)ENGINE=INNODB;

