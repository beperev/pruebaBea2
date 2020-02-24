# Create Testuser
-- CREATE USER 'prbd'@'localhost' IDENTIFIED BY 'prbdprbd';
-- GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP ON *.* TO 'prbd'@'localhost';
# Create DB
CREATE DATABASE IF NOT EXISTS `inmobiliaria` DEFAULT CHARACTER SET latin1 COLLATE latin1_general_ci;
USE `inmobiliaria`;

CREATE DATABASE  IF NOT EXISTS `inmobiliaria` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `inmobiliaria`;


-- Table structure for table `captacion`
--
show variables like 'lower%';

DROP TABLE IF EXISTS `captacion`;

/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = latin1 */;

CREATE TABLE `captacion` (
  
	`ID_FINCA` varchar(10) DEFAULT NULL,
  
	`ID_EMPLEADO` varchar(6) DEFAULT NULL,
  
	`FECHA_CAPTACION` datetime DEFAULT NULL

) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `cliente`
--


DROP TABLE IF EXISTS `cliente`;

/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = latin1*/;

CREATE TABLE `cliente` (
 
	 `ID_CLIENTE` varchar(6) NOT NULL,
  
	`NOMBRE_CLIENTE` varchar(30) DEFAULT NULL,
  
	`APELLIDOS_CLIENTE` varchar(60) DEFAULT NULL,
  
	`TELEFONO` varchar(9) DEFAULT NULL,
  
	`PREFERENCIA` varchar(10) DEFAULT NULL,
  
	`PRESUPUESTO` double DEFAULT NULL,
  
	PRIMARY KEY (`ID_CLIENTE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `empleado`
--


DROP TABLE IF EXISTS `empleado`;

/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = latin1 */;

CREATE TABLE `empleado` (
  
	`ID_EMPLEADO` varchar(6) NOT NULL,
  
	`NOMBRE_EMPLEADO` varchar(30) DEFAULT NULL,
  
	`APELLIDOS_EMPLEADO` varchar(60) DEFAULT NULL,
  
	`TRABAJO` varchar(30) DEFAULT NULL,
  
	`SEXO` varchar(1) DEFAULT NULL,
  
	`FECHA_NACIMIENTO` datetime DEFAULT NULL,
  
	`SALARIO` double DEFAULT NULL,
  
	`SUCURSAL` varchar(6) DEFAULT NULL,
  
	PRIMARY KEY (`ID_EMPLEADO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Table structure for table `finca`
--


DROP TABLE IF EXISTS `finca`;

/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = latin1 */;


CREATE TABLE `finca` (
  
	`ID_FINCA` varchar(10) NOT NULL,
  
	`DIRECCION` varchar(30) DEFAULT NULL,
  
	`CIUDAD` varchar(30) DEFAULT NULL,
  
	`CODIGO_POSTAL` varchar(5) DEFAULT NULL,
  
	`TIPO` varchar(15) DEFAULT NULL,
  
	`HABITACIONES` tinyint(3) unsigned DEFAULT NULL,
  
	`BANIOS` tinyint(3) unsigned DEFAULT NULL,
  
	`CALEFACCION` varchar(40) DEFAULT NULL,
  
	`ASCENSOR` varchar(2) DEFAULT NULL,
  
	`ALQUILER` double DEFAULT NULL,
  
	`PROPIETARIO` varchar(6) DEFAULT NULL,
  
	PRIMARY KEY (`ID_FINCA`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET character_set_client = @saved_cs_client */;




--
-- Table structure for table `propietario`
--



DROP TABLE IF EXISTS `propietario`;

/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = latin1 */;


CREATE TABLE `propietario` (
  
	`ID_PROPIETARIO` varchar(6) NOT NULL,
  
	`NOMBRE_PROPIETARIO` varchar(30) DEFAULT NULL,
 
	`APELLIDOS_PROPIETARIO` varchar(60) DEFAULT NULL,
  
	`DIRECCION` varchar(60) DEFAULT NULL,
  
	`TELEFONO` varchar(9) DEFAULT NULL,
  
	PRIMARY KEY (`ID_PROPIETARIO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



/*!40101 SET character_set_client = @saved_cs_client */;





--
-- Table structure for table `sucursal`
--



DROP TABLE IF EXISTS `sucursal`;

/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = latin1 */;


CREATE TABLE `sucursal` (
  
`ID_SUCURSAL` varchar(6) NOT NULL,
  
`DIRECCION` varchar(20) DEFAULT NULL,
  
`CIUDAD` varchar(20) DEFAULT NULL,
  
`CODIGO_POSTAL` varchar(5) DEFAULT NULL,
  
PRIMARY KEY (`ID_SUCURSAL`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


/*!40101 SET character_set_client = @saved_cs_client */;





--
-- Table structure for table `visita`
--


DROP TABLE IF EXISTS `visita`;

/*!40101 SET @saved_cs_client     = @@character_set_client */;

/*!40101 SET character_set_client = latin1 */;

CREATE TABLE `visita` (
  
	`ID_CLIENTE` varchar(6) DEFAULT NULL,
  
	`ID_FINCA` varchar(10) DEFAULT NULL,
  
	`ID_EMPLEADO` varchar(6) DEFAULT NULL,
  
	`FECHA_VISITA` datetime DEFAULT NULL,
    
	`COMENTARIOS` varchar(250) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


select * from `visita`;
# Add Data
