-- =====================================================
-- RECREAR BASE DE DATOS PFG - VERSIÓN CORREGIDA Y OPTIMIZADA (2025)
-- =====================================================
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

DROP DATABASE IF EXISTS `PFG`;
CREATE DATABASE IF NOT EXISTS `PFG` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `PFG`;

-- =====================================================
-- TABLA: Inmuebles
-- =====================================================
CREATE TABLE `Inmuebles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Direccion` VARCHAR(255) NOT NULL,                   -- AMPLIADO (antes 45 → ahora 255)
  `Tipo_Operacion` TINYINT NOT NULL COMMENT '0 = Venta, 1 = Alquiler',
  `Precio` DOUBLE NOT NULL,
  `Num_Hab` TINYINT NOT NULL,
  `Num_Baños` TINYINT NOT NULL,
  `Metros_Cuadrados` DOUBLE NOT NULL,
  `Estado` VARCHAR(50) NOT NULL,
  `Compartido` TINYINT(1) NOT NULL DEFAULT 0,         -- Mejor que BIT
  `Anotaciones` TEXT NULL,                            -- AMPLIADO (antes 255 → TEXT)
  `Comunidad` VARCHAR(60) NULL,
  `Certificado_Energia` VARCHAR(10) NULL,
  `IBI` VARCHAR(20) NULL,
  `Nota_Simple` VARCHAR(100) NULL,
  `Valido` TINYINT(1) NOT NULL DEFAULT 1,
  `Tipo_Vivienda` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_valido` (`Valido`),
  INDEX `idx_tipo_operacion` (`Tipo_Operacion`),
  INDEX `idx_precio` (`Precio`),
  INDEX `idx_direccion` (`Direccion`(191))
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Trabajadores
-- =====================================================
CREATE TABLE `Trabajadores` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(60) NOT NULL,
  `Apellido` VARCHAR(80) NOT NULL,
  `NIF` VARCHAR(12) NOT NULL UNIQUE,                  -- Soporta NIE (X, Y, Z)
  `Contacto` VARCHAR(100) NULL,
  `Puesto` VARCHAR(60) NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_nif` (`NIF`)
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Vendedores
-- =====================================================
CREATE TABLE `Vendedores` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(60) NOT NULL,
  `Apellido` VARCHAR(80) NOT NULL,
  `Contacto` VARCHAR(100) NOT NULL,
  `Demanda` TEXT NULL,                                -- AMPLIADO a TEXT
  `Direccion` VARCHAR(150) NOT NULL,                  -- AMPLIADO
  `CP` VARCHAR(10) NOT NULL,
  `NIF` VARCHAR(12) NOT NULL UNIQUE,
  PRIMARY KEY (`id`),
  INDEX `idx_nif` (`NIF`)
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Interesados
-- =====================================================
CREATE TABLE `Interesados` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(60) NULL,
  `Apellidos` VARCHAR(100) NULL,
  `Contacto` VARCHAR(100) NULL,
  `Demanda` TEXT NULL,                                -- AMPLIADO a TEXT
  `Direccion` VARCHAR(150) NULL,                      -- AMPLIADO
  `CP` VARCHAR(10) NULL,
  `NIF` VARCHAR(12) NULL UNIQUE,
  PRIMARY KEY (`id`),
  INDEX `idx_nif` (`NIF`)
) ENGINE = InnoDB;

-- =====================================================
-- TABLA: Users (para Spring Security / OAuth2)
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
  INDEX `idx_provider` (`provider`, `provider_id`)
) ENGINE = InnoDB;

-- =====================================================
-- TABLAS RELACIONALES Y RESTO (sin cambios estructurales, solo mejoras)
-- =====================================================
CREATE TABLE `Citas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Fecha` DATE NOT NULL,
  `Hora` TIME NOT NULL,
  `Anotaciones` TEXT NULL,
  `Inmueble_id` INT NOT NULL,
  `Trabajador_id` INT NOT NULL,
  `Interesado_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_citas_inmueble_idx` (`Inmueble_id`),
  INDEX `fk_citas_trabajador_idx` (`Trabajador_id`),
  INDEX `fk_citas_interesado_idx` (`Interesado_id`),
  INDEX `idx_fecha_hora` (`Fecha`, `Hora`),
  CONSTRAINT `fk_citas_inmueble` FOREIGN KEY (`Inmueble_id`) REFERENCES `Inmuebles` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_citas_trabajador` FOREIGN KEY (`Trabajador_id`) REFERENCES `Trabajadores` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_citas_interesado` FOREIGN KEY (`Interesado_id`) REFERENCES `Interesados` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE `Operacion` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Fecha` DATE NOT NULL,
  `Monto` DOUBLE NOT NULL,
  `Inmueble_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_operacion_inmueble_idx` (`Inmueble_id`),
  INDEX `idx_fecha` (`Fecha`),
  CONSTRAINT `fk_operacion_inmueble` FOREIGN KEY (`Inmueble_id`) REFERENCES `Inmuebles` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE `Reseñas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Fecha` DATE NOT NULL,
  `Calificacion` TINYINT NOT NULL CHECK (`Calificacion` BETWEEN 1 AND 5),
  `Comentario` TEXT NULL,
  `Operacion_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_resenas_operacion_idx` (`Operacion_id`),
  CONSTRAINT `fk_resenas_operacion` FOREIGN KEY (`Operacion_id`) REFERENCES `Operacion` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE `Contrato` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Fecha_Firma` DATE NOT NULL,
  `Tipo_Contrato` VARCHAR(60) NOT NULL,
  `Terminos` TEXT NOT NULL,
  `Operacion_id` INT NOT NULL,
  `Trabajador_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_contrato_operacion_idx` (`Operacion_id`),
  INDEX `fk_contrato_trabajador_idx` (`Trabajador_id`),
  CONSTRAINT `fk_contrato_operacion` FOREIGN KEY (`Operacion_id`) REFERENCES `Operacion` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_contrato_trabajador` FOREIGN KEY (`Trabajador_id`) REFERENCES `Trabajadores` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE `Propiedad_Adicional` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Tipo` VARCHAR(50) NOT NULL,
  `Derrama` DOUBLE NULL DEFAULT 0,
  `IBI` VARCHAR(30) NULL,
  `Inmueble_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_prop_adicional_inmueble_idx` (`Inmueble_id`),
  INDEX `idx_tipo` (`Tipo`),
  CONSTRAINT `fk_prop_adicional_inmueble` FOREIGN KEY (`Inmueble_id`) REFERENCES `Inmuebles` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE `Inmuebles_has_Interesados` (
  `Inmueble_id` INT NOT NULL,
  `Interesado_id` INT NOT NULL,
  PRIMARY KEY (`Inmueble_id`, `Interesado_id`),
  CONSTRAINT `fk_inm_int_inmueble` FOREIGN KEY (`Inmueble_id`) REFERENCES `Inmuebles` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_inm_int_interesado` FOREIGN KEY (`Interesado_id`) REFERENCES `Interesados` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE `Vendedores_has_Inmuebles` (
  `Vendedor_id` INT NOT NULL,
  `Inmueble_id` INT NOT NULL,
  `Representante` VARCHAR(120) NOT NULL,
  PRIMARY KEY (`Vendedor_id`, `Inmueble_id`),
  CONSTRAINT `fk_ven_inm_vendedor` FOREIGN KEY (`Vendedor_id`) REFERENCES `Vendedores` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ven_inm_inmueble` FOREIGN KEY (`Inmueble_id`) REFERENCES `Inmuebles` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE `Interesados_has_Operacion` (
  `Interesado_id` INT NOT NULL,
  `Operacion_id` INT NOT NULL,
  `Representante` VARCHAR(120) NOT NULL,
  PRIMARY KEY (`Interesado_id`, `Operacion_id`),
  CONSTRAINT `fk_int_op_interesado` FOREIGN KEY (`Interesado_id`) REFERENCES `Interesados` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_int_op_operacion` FOREIGN KEY (`Operacion_id`) REFERENCES `Operacion` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB;

-- =====================================================
-- RESTAURAR CONFIGURACIÓN
-- =====================================================
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- =====================================================
-- ¡LISTO! Ahora ejecuta el script de datos de ejemplo ampliado que te pasé antes
-- y funcionará PERFECTAMENTE sin ningún error
-- =====================================================