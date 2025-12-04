-- =====================================================
-- BORRAR Y RECREAR BASE DE DATOS CON IDs SIMPLIFICADOS
-- =====================================================

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- =====================================================
-- BORRAR BASE DE DATOS EXISTENTE
-- =====================================================
DROP DATABASE IF EXISTS `PFG`;

-- =====================================================
-- CREAR BASE DE DATOS
-- =====================================================
CREATE DATABASE IF NOT EXISTS `PFG` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `PFG`;

-- =====================================================
-- TABLA: Inmuebles
-- =====================================================
CREATE TABLE `Inmuebles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Direccion` VARCHAR(45) NOT NULL,
  `Tipo_Operacion` TINYINT NOT NULL COMMENT '0=Venta, 1=Alquiler',
  `Precio` DOUBLE NOT NULL,
  `Num_Hab` INT NOT NULL,
  `Num_Baños` INT NOT NULL,
  `Metros_Cuadrados` DOUBLE NOT NULL,
  `Estado` VARCHAR(45) NOT NULL,
  `Compartido` BIT NOT NULL,
  `Anotaciones` VARCHAR(255) NULL,
  `Comunidad` VARCHAR(45) NOT NULL,
  `Certificado_Energia` VARCHAR(45) NOT NULL,
  `IBI` VARCHAR(45) NOT NULL,
  `Nota_Simple` VARCHAR(45) NOT NULL,
  `Valido` TINYINT NOT NULL DEFAULT 1,
  `Tipo_Vivienda` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_valido` (`Valido`),
  INDEX `idx_tipo_operacion` (`Tipo_Operacion`)
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Trabajadores
-- =====================================================
CREATE TABLE `Trabajadores` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  `Apellido` VARCHAR(45) NOT NULL,
  `NIF` VARCHAR(10) NOT NULL UNIQUE,
  `Contacto` VARCHAR(45) NULL,
  `Puesto` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_nif` (`NIF`)
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Vendedores
-- =====================================================
CREATE TABLE `Vendedores` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  `Apellido` VARCHAR(45) NOT NULL,
  `Contacto` VARCHAR(45) NOT NULL,
  `Demanda` VARCHAR(255) NULL,
  `Direccion` VARCHAR(100) NOT NULL,
  `CP` VARCHAR(10) NOT NULL,
  `NIF` VARCHAR(10) NOT NULL UNIQUE,
  PRIMARY KEY (`id`),
  INDEX `idx_nif` (`NIF`)
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Interesados (SIN CAMPOS DE SEGURIDAD/OAUTH2)
-- =====================================================
CREATE TABLE `Interesados` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NULL,
  `Apellidos` VARCHAR(45) NULL,
  `Contacto` VARCHAR(45) NULL,
  `Demanda` VARCHAR(255) NULL,
  `Direccion` VARCHAR(100) NULL,
  `CP` VARCHAR(10) NULL,
  `NIF` VARCHAR(10) NULL UNIQUE,
  PRIMARY KEY (`id`),
  INDEX `idx_nif` (`NIF`)
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Users (AÑADIDA TEMPORALMENTE PARA OAuth)
-- =====================================================
CREATE TABLE `Users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL UNIQUE, 
  `name` VARCHAR(100) NULL,
  `password` VARCHAR(255) NULL,
  `enabled` BOOLEAN NOT NULL DEFAULT TRUE,
  `role` VARCHAR(20) NOT NULL DEFAULT 'USER',
  `avatarUrl` VARCHAR(512) NULL,
  `provider` VARCHAR(20) NULL,
  `provider_id` VARCHAR(255) NULL,
  `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastLogin` DATETIME NULL,
  
  PRIMARY KEY (`id`),
  INDEX `idx_email` (`email`),
  INDEX `idx_provider_id` (`provider`, `provider_id`)
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Citas
-- =====================================================
CREATE TABLE `Citas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Fecha` DATE NOT NULL,
  `Hora` TIME NOT NULL,
  `Anotaciones` VARCHAR(255) NULL,
  `Inmueble_id` INT NOT NULL,
  `Trabajador_id` INT NOT NULL,
  `Interesado_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_citas_inmueble_idx` (`Inmueble_id` ASC),
  INDEX `fk_citas_trabajador_idx` (`Trabajador_id` ASC),
  INDEX `fk_citas_interesado_idx` (`Interesado_id` ASC),
  INDEX `idx_fecha` (`Fecha`),
  CONSTRAINT `fk_citas_inmueble`
    FOREIGN KEY (`Inmueble_id`)
    REFERENCES `Inmuebles` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_trabajador`
    FOREIGN KEY (`Trabajador_id`)
    REFERENCES `Trabajadores` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_interesado`
    FOREIGN KEY (`Interesado_id`)
    REFERENCES `Interesados` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Operacion
-- =====================================================
CREATE TABLE `Operacion` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Fecha` DATE NOT NULL,
  `Monto` DOUBLE NOT NULL,
  `Inmueble_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_operacion_inmueble_idx` (`Inmueble_id` ASC),
  INDEX `idx_fecha` (`Fecha`),
  CONSTRAINT `fk_operacion_inmueble`
    FOREIGN KEY (`Inmueble_id`)
    REFERENCES `Inmuebles` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Reseñas
-- =====================================================
CREATE TABLE `Reseñas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Fecha` DATE NOT NULL,
  `Calificacion` INT NOT NULL,
  `Comentario` VARCHAR(255) NULL,
  `Operacion_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_resenas_operacion_idx` (`Operacion_id` ASC),
  INDEX `idx_calificacion` (`Calificacion`),
  CONSTRAINT `fk_resenas_operacion`
    FOREIGN KEY (`Operacion_id`)
    REFERENCES `Operacion` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `chk_calificacion` CHECK (`Calificacion` BETWEEN 1 AND 5)
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Contrato
-- =====================================================
CREATE TABLE `Contrato` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Fecha_Firma` DATE NOT NULL,
  `Tipo_Contrato` VARCHAR(45) NOT NULL,
  `Terminos` VARCHAR(255) NOT NULL,
  `Operacion_id` INT NOT NULL,
  `Trabajador_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_contrato_operacion_idx` (`Operacion_id` ASC),
  INDEX `fk_contrato_trabajador_idx` (`Trabajador_id` ASC),
  CONSTRAINT `fk_contrato_operacion`
    FOREIGN KEY (`Operacion_id`)
    REFERENCES `Operacion` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_contrato_trabajador`
    FOREIGN KEY (`Trabajador_id`)
    REFERENCES `Trabajadores` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Propiedad_Adicional
-- =====================================================
CREATE TABLE `Propiedad_Adicional` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Tipo` VARCHAR(45) NOT NULL,
  `Derrama` DOUBLE NOT NULL,
  `IBI` VARCHAR(45) NOT NULL,
  `Inmueble_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_propiedad_adicional_inmueble_idx` (`Inmueble_id` ASC),
  INDEX `idx_tipo` (`Tipo`),
  CONSTRAINT `fk_propiedad_adicional_inmueble`
    FOREIGN KEY (`Inmueble_id`)
    REFERENCES `Inmuebles` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Inmuebles_has_Interesados (Relación N:M)
-- =====================================================
CREATE TABLE `Inmuebles_has_Interesados` (
  `Inmueble_id` INT NOT NULL,
  `Interesado_id` INT NOT NULL,
  PRIMARY KEY (`Inmueble_id`, `Interesado_id`),
  INDEX `fk_inmuebles_interesados_interesado_idx` (`Interesado_id` ASC),
  INDEX `fk_inmuebles_interesados_inmueble_idx` (`Inmueble_id` ASC),
  CONSTRAINT `fk_inmuebles_interesados_inmueble`
    FOREIGN KEY (`Inmueble_id`)
    REFERENCES `Inmuebles` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_inmuebles_interesados_interesado`
    FOREIGN KEY (`Interesado_id`)
    REFERENCES `Interesados` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Vendedores_has_Inmuebles (Relación N:M)
-- =====================================================
CREATE TABLE `Vendedores_has_Inmuebles` (
  `Vendedor_id` INT NOT NULL,
  `Inmueble_id` INT NOT NULL,
  `Representante` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`Vendedor_id`, `Inmueble_id`),
  INDEX `fk_vendedores_inmuebles_inmueble_idx` (`Inmueble_id` ASC),
  INDEX `fk_vendedores_inmuebles_vendedor_idx` (`Vendedor_id` ASC),
  CONSTRAINT `fk_vendedores_inmuebles_vendedor`
    FOREIGN KEY (`Vendedor_id`)
    REFERENCES `Vendedores` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_vendedores_inmuebles_inmueble`
    FOREIGN KEY (`Inmueble_id`)
    REFERENCES `Inmuebles` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Interesados_has_Operacion (Relación N:M)
-- =====================================================
CREATE TABLE `Interesados_has_Operacion` (
  `Interesado_id` INT NOT NULL,
  `Operacion_id` INT NOT NULL,
  `Representante` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`Interesado_id`, `Operacion_id`),
  INDEX `fk_interesados_operacion_operacion_idx` (`Operacion_id` ASC),
  INDEX `fk_interesados_operacion_interesado_idx` (`Interesado_id` ASC),
  CONSTRAINT `fk_interesados_operacion_interesado`
    FOREIGN KEY (`Interesado_id`)
    REFERENCES `Interesados` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_interesados_operacion_operacion`
    FOREIGN KEY (`Operacion_id`)
    REFERENCES `Operacion` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB;

-- =====================================================
-- RESTAURAR CONFIGURACIÓN
-- =====================================================
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;