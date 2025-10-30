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
-- TABLA: Interesados
-- =====================================================
CREATE TABLE `Interesados` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NOT NULL,
  `Apellidos` VARCHAR(45) NOT NULL,
  `Contacto` VARCHAR(45) NOT NULL,
  `Demanda` VARCHAR(255) NULL,
  `Direccion` VARCHAR(100) NULL,
  `CP` VARCHAR(10) NULL,
  `NIF` VARCHAR(10) NULL UNIQUE,
  PRIMARY KEY (`id`),
  INDEX `idx_nif` (`NIF`)
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
-- TABLA: property (Web pública)
-- =====================================================
CREATE TABLE `property` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NOT NULL COMMENT 'Título descriptivo del inmueble',
  `location` VARCHAR(150) NOT NULL COMMENT 'Ubicación',
  `bedrooms` INT NOT NULL DEFAULT 0,
  `bathrooms` INT NULL DEFAULT 0,
  `price` DECIMAL(12,2) NOT NULL,
  `operation` VARCHAR(20) NOT NULL COMMENT 'venta o alquiler',
  `type` VARCHAR(30) NOT NULL COMMENT 'piso, chalet, atico, casa, vpo',
  `zone` VARCHAR(50) NOT NULL COMMENT 'jerez, el-puerto, cadiz',
  `image_url` VARCHAR(500) NULL,
  `surface` DOUBLE NULL,
  `description` TEXT NULL,
  `featured` BOOLEAN DEFAULT FALSE,
  `active` BOOLEAN DEFAULT TRUE,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_operation` (`operation`),
  INDEX `idx_type` (`type`),
  INDEX `idx_zone` (`zone`),
  INDEX `idx_active` (`active`),
  INDEX `idx_featured` (`featured`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- DATOS DE EJEMPLO: property
-- =====================================================
INSERT INTO `property` 
  (`name`, `location`, `bedrooms`, `bathrooms`, `price`, `operation`, `type`, `zone`, `image_url`, `surface`, `description`, `featured`, `active`) 
VALUES
  ('Piso exclusivo en el centro de Jerez', 'Calle Larga, Jerez de la Frontera', 3, 2, 215000.00, 'venta', 'piso', 'jerez', 
   'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?w=800', 120, 
   'Magnífico piso en pleno centro histórico, completamente reformado con materiales de primera calidad.', 
   TRUE, TRUE),
  
  ('Chalet con piscina en zona residencial', 'Urbanización La Marquesa, Jerez', 4, 3, 385000.00, 'venta', 'chalet', 'jerez', 
   'https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=800', 250, 
   'Espectacular chalet independiente con jardín privado y piscina.', 
   TRUE, TRUE),
  
  ('Ático con terraza y vistas panorámicas', 'Avenida Alcalde Álvaro Domecq, Jerez', 2, 2, 295000.00, 'venta', 'atico', 'jerez', 
   'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=800', 95, 
   'Precioso ático con terraza de 40m², vistas espectaculares.', 
   TRUE, TRUE),
  
  ('Piso céntrico para estudiantes', 'Calle Porvera, Jerez', 2, 1, 550.00, 'alquiler', 'piso', 'jerez', 
   'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=800', 70, 
   'Piso amueblado ideal para estudiantes o pareja joven.', 
   FALSE, TRUE),
  
  ('Casa tradicional en el centro de Cádiz', 'Calle San José, Cádiz', 3, 2, 325000.00, 'venta', 'casa', 'cadiz', 
   'https://images.unsplash.com/photo-1580587771525-78b9dba3b914?w=800', 140, 
   'Casa típica gaditana en pleno casco histórico.', 
   TRUE, TRUE),
  
  ('Apartamento moderno en El Puerto', 'Paseo Marítimo, El Puerto de Santa María', 2, 1, 850.00, 'alquiler', 'piso', 'el-puerto', 
   'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?w=800', 85, 
   'Apartamento de nueva construcción con vistas al mar.', 
   FALSE, TRUE),
  
  ('VPO nueva construcción en Jerez', 'Barrio de la Granja, Jerez', 3, 2, 125000.00, 'venta', 'vpo', 'jerez', 
   'https://images.unsplash.com/photo-1564013799919-ab600027ffc6?w=800', 90, 
   'Vivienda de protección oficial en fase de construcción.', 
   FALSE, TRUE),
  
  ('Piso reformado cerca de la Universidad', 'Avenida de la Universidad, Jerez', 3, 1, 650.00, 'alquiler', 'piso', 'jerez', 
   'https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=800', 90, 
   'Piso recién reformado, perfecto para estudiantes.', 
   FALSE, TRUE);

-- =====================================================
-- RESTAURAR CONFIGURACIÓN
-- =====================================================
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;