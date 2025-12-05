-- =====================================================
-- INSERCIÓN DE DATOS DE EJEMPLO (VERSIÓN AMPLIADA)
-- =====================================================
USE `PFG`;

-- =====================================================
-- LIMPIAR TODAS LAS TABLAS (solo en desarrollo/pruebas!!)
-- =====================================================
SET FOREIGN_KEY_CHECKS = 0;  -- Desactiva temporalmente las claves foráneas

TRUNCATE TABLE `Reseñas`;
TRUNCATE TABLE `Contrato`;
TRUNCATE TABLE `Interesados_has_Operacion`;
TRUNCATE TABLE `Operacion`;
TRUNCATE TABLE `Citas`;
TRUNCATE TABLE `Inmuebles_has_Interesados`;
TRUNCATE TABLE `Propiedad_Adicional`;
TRUNCATE TABLE `Vendedores_has_Inmuebles`;
TRUNCATE TABLE `Inmuebles`;
TRUNCATE TABLE `Users`;
TRUNCATE TABLE `Interesados`;
TRUNCATE TABLE `Vendedores`;
TRUNCATE TABLE `Trabajadores`;

SET FOREIGN_KEY_CHECKS = 1;  -- Vuelve a activarlas

-- 1. Trabajadores (10 empleados)
INSERT INTO `Trabajadores` (`Nombre`, `Apellido`, `NIF`, `Contacto`, `Puesto`) VALUES
('Laura', 'García López', '12345678A', 'laura.g@inmobiliaria.es', 'Agente Senior'),
('Javier', 'Pérez Sánchez', '87654321B', 'javier.p@inmobiliaria.es', 'Agente Junior'),
('María', 'López Ruiz', '01928374C', 'maria.l@inmobiliaria.es', 'Administración'),
('Carlos', 'Fernández Díaz', '11223344D', 'carlos.f@inmobiliaria.es', 'Agente Senior'),
('Patricia', 'Moreno Vega', '55667788E', 'patricia.m@inmobiliaria.es', 'Agente Comercial'),
('Diego', 'Romero Castillo', '99887766F', 'diego.r@inmobiliaria.es', 'Director Comercial'),
('Ana', 'Herrera Molina', '33445566G', 'ana.h@inmobiliaria.es', 'Marketing'),
('Roberto', 'Jiménez Ortiz', '77889900H', 'roberto.j@inmobiliaria.es', 'Agente Junior'),
('Carmen', 'Santos Navarro', '22334455I', 'carmen.s@inmobiliaria.es', 'Administración'),
('Miguel', 'Ángel Torres', '66778899J', 'miguel.t@inmobiliaria.es', 'Fotógrafo y Home Staging');

-- 2. Vendedores (15 propietarios)
INSERT INTO `Vendedores` (`Nombre`, `Apellido`, `Contacto`, `Demanda`, `Direccion`, `CP`, `NIF`) VALUES
('Ana', 'Ruiz Martínez', 'ana.r@ejemplo.com', 'Vender piso rápido, precio alto', 'C/ Falsa 123, Madrid', '28001', '11111111D'),
('Pedro', 'Gómez Herrera', 'pedro.g@ejemplo.com', 'Vender casa sin prisa, buen precio', 'Av. Central 45, Sevilla', '41005', '22222222E'),
('Isabel', 'Castillo Vega', 'isabel.c@hotmail.com', 'Herencia, vender ático', 'C/ del Sol 78, Valencia', '46003', '33333333F'),
('Francisco', 'Navarro Cano', 'paco.navarro@gmail.com', 'Traslado laboral, venta urgente', 'Paseo Marítimo 12, Málaga', '29016', '44444444G'),
('Lucía', 'Delgado Rubio', 'lucia.d@outlook.com', 'Divorcio, vender dúplex', 'C/ Mayor 56, Bilbao', '48001', '55555555H'),
('Antonio', 'Reyes Flores', 'antonio.reyes@icloud.com', 'Jubilación, vender chalet', 'Urbanización Los Pinos 34, Alicante', '03540', '66666666I'),
('Marta', 'Serrano Gil', 'marta.serrano@yahoo.es', 'Mudanza internacional', 'Av. Europa 90, Barcelona', '08023', '77777777J'),
('José Luis', 'Medina Pons', 'joseluis.medina@gmail.com', 'Inversión, vender local', 'C/ Comercio 23, Zaragoza', '50001', '88888888K'),
('Raquel', 'Ortega Blanco', 'raquel.o@gmail.com', 'Vender piso heredado', 'C/ Luna 15, Murcia', '30001', '99999999L'),
('David', 'Cabrera León', 'david.cabrera@protonmail.com', 'Venta rápida por traslado', 'C/ Real 67, Granada', '18001', '10101010M'),
('Elena', 'Prieto Soto', 'elena.prieto@ejemplo.com', 'Vender apartamento playa', 'Paseo de la Playa 8, Cádiz', '11005', '20202020N'),
('Víctor', 'Iglesias Ramos', 'victor.i@gmail.com', 'Herencia familiar', 'C/ del Prado 44, Toledo', '45001', '30303030P'),
('Sandra', 'Campos Durán', 'sandra.campos@hotmail.com', 'Divorcio', 'Av. Andalucía 112, Córdoba', '14004', '40404040Q'),
('Jorge', 'Vidal Marcos', 'jorge.vidal@ejemplo.com', 'Inversión', 'C/ Gran Vía 89, Madrid', '28013', '50505050R'),
('Beatriz', 'Aguilar Cruz', 'beatriz.aguilar@yahoo.es', 'Mudanza a otra ciudad', 'C/ San Vicente 33, Valencia', '46002', '60606060S');

-- 3. Interesados (20 posibles compradores/inquilinos)
INSERT INTO `Interesados` (`Nombre`, `Apellidos`, `Contacto`, `Demanda`, `Direccion`, `CP`, `NIF`) VALUES
('Sofía', 'Martínez López', 'sofia.m@ejemplo.com', 'Piso 3 hab centro, compra hasta 200k', 'Pz. Mayor 1, Madrid', '28012', '33333333F'),
('Carlos', 'Díaz Romero', 'carlos.d@ejemplo.com', 'Alquiler 2 hab urgente, máx 900€', 'Ronda Exterior 7, Barcelona', '08001', '44444444G'),
('Elena', 'Vázquez Pérez', 'elena.v@ejemplo.com', 'Inversión, rentabilidad mínima 5%', 'C/ Sol 15, Sevilla', '41002', '55555555H'),
('Alejandro', 'Jiménez Torres', 'alejandro.j@gmail.com', 'Primera vivienda, 3-4 hab hasta 300k', 'Av. Constitución 22, Valencia', '46001', '66666666T'),
('Laura', 'Sánchez Ruiz', 'laura.sanchez@hotmail.com', 'Alquiler con opción a compra', 'C/ Libertad 88, Bilbao', '48012', '77777777U'),
('Pablo', 'Herrero Cano', 'pablo.herrero@outlook.com', 'Chalet con piscina, hasta 500k', 'Urbanización El Bosque, Madrid', '28670', '88888888V'),
('Natalia', 'Garrido Molina', 'natalia.g@ejemplo.com', 'Estudio centro para estudiante', 'C/ Universidad 5, Salamanca', '37001', '99999999W'),
('Marcos', 'Fuentes Vega', 'marcos.fuentes@gmail.com', 'Local comercial zona peatonal', 'C/ Mayor 100, Granada', '18009', '10111213X'),
('Irene', 'Calvo Ortiz', 'irene.calvo@yahoo.es', 'Piso reformado 2 hab <700€ alquiler', 'C/ del Mar 33, Málaga', '29015', '22334455Y'),
('Rubén', 'Parra Nieto', 'ruben.parra@protonmail.com', 'Ático con terraza, compra', 'Av. del Puerto 150, Valencia', '46023', '33445566Z'),
('Cristina', 'Blanco Serrano', 'cristina.b@gmail.com', 'Casa adosada con jardín', 'Pol. Las Sedas, Alicante', '03300', '44556677A'),
('Adrián', 'Montero Vega', 'adrian.m@ejemplo.com', 'Inversión varios pisos', 'C/ Comercio 45, Murcia', '30002', '55667788B'),
('Verónica', 'Lozano Campos', 'veronica.l@hotmail.com', 'Piso exterior luminoso 3 hab', 'C/ Alcalá 200, Madrid', '28009', '66778899C'),
('Daniel', 'Escudero Pons', 'daniel.e@icloud.com', 'Alquiler temporal 6 meses', 'Paseo de Gracia 80, Barcelona', '08008', '77889900D'),
('Tamara', 'Roldán Gil', 'tamara.r@gmail.com', 'Bajo con patio, hasta 180k', 'C/ San Fernando 12, Sevilla', '41004', '88990011E'),
('Guillermo', 'Pascual Duarte', 'guillermo.p@ejemplo.com', 'Garaje + trastero obligatorios', 'Av. Mediterráneo 67, Valencia', '46011', '99001122F'),
('Lorena', 'Aranda Ruiz', 'lorena.a@yahoo.es', 'Obra nueva entrega 2026', 'Nueva Zona Norte, Zaragoza', '50021', '11223344G'),
('Óscar', 'Beltrán Mora', 'oscar.b@gmail.com', 'Piso para reformar inversión', 'Casco Antiguo, Toledo', '45001', '22334455H'),
('Noelia', 'Corral Vega', 'noelia.c@ejemplo.com', 'Alquiler larga temporada mascotas', 'C/ del Carmen 23, Cádiz', '11005', '33445566I'),
('Iker', 'Zubizarreta López', 'iker.z@outlook.com', 'Chalet independiente parcela >500m²', 'La Moraleja, Madrid', '28108', '44556677J');

-- 4. Users (más usuarios de prueba)
INSERT INTO `Users` (`email`, `name`, `password`, `enabled`, `role`, `provider`) VALUES
('user1@example.com', 'Usuario Uno', '$2a$10$fN4YxK8A7ZpI6O5S3T2Q1eCgV3T0R9L0J2K1I0H9G8F7E6D5C4B3A21', TRUE, 'USER', 'local'),
('admin@example.com', 'Admin PFG', '$2a$10$fN4YxK8A7ZpI6O5S3T2Q1eCgV3T0R9L0J2K1I0H9G8F7E6D5C4B3A21', TRUE, 'ADMIN', 'local'),
('laura.g@inmobiliaria.es', 'Laura García', '$2a$10$fN4YxK8A7ZpI6O5S3T2Q1eCgV3T0R9L0J2K1I0H9G8F7E6D5C4B3A21', TRUE, 'USER', 'local'),
('javier.sanchez@test.com', 'Javier Sánchez', '$2a$10$fN4YxK Cim... (password123)', TRUE, 'USER', 'local'),
('carlos.f@inmobiliaria.es', 'Carlos Fernández', '$2a$10$fN4YxK8A7ZpI6O5S3T2Q1eCgV3T0R9L0J2K1I0H9G8F7E6D5C4B3A21', TRUE, 'USER', 'local');

-- 5. Inmuebles (20 inmuebles variados)
INSERT INTO `Inmuebles` 
(`Direccion`, `Tipo_Operacion`, `Precio`, `Num_Hab`, `Num_Baños`, `Metros_Cuadrados`, `Estado`, `Compartido`, `Anotaciones`, `Comunidad`, `Certificado_Energia`, `IBI`, `Nota_Simple`, `Valido`, `Tipo_Vivienda`) VALUES
('C/ Alameda 10, 3ºA, Madrid', 0, 185000.00, 3, 2, 95.5, 'Reformado', 0, 'Vistas al parque, muy luminoso, exterior', '55€/mes', 'A', '420', 'Reg. 1001', 1, 'Piso'),
('Av. Libertad 50, 1ºB, Sevilla', 1, 850.00, 2, 1, 70.0, 'Buen estado', 1, 'Amueblado, cerca metro, soleado', '75€/mes', 'C', '310', 'Reg. 1002', 1, 'Apartamento'),
('Paseo del Mar 22, Chalet Independiente, Cádiz', 0, 420000.00, 4, 3, 180.0, 'Obra Nueva', 0, 'Piscina privada, parcela 600m²', '150€/mes', 'A+', '850', 'Reg. 1003', 1, 'Chalet'),
('C/ Gran Vía 88, 5ºC, Madrid', 0, 295000.00, 3, 2, 110.0, 'A reformar', 0, 'Última planta, mucha luz, a reformar', '80€/mes', 'E', '680', 'Reg. 1004', 1, 'Piso'),
('C/ del Sol 33, Local Comercial, Valencia', 0, 175000.00, 0, 2, 120.0, 'Buen estado', 0, 'Local esquina, salida humos', '120€/mes', 'D', '1200', 'Reg. 1005', 1, 'Local'),
('Urbanización Monteclaro, Pozuelo, Madrid', 1, 2200.00, 5, 4, 320.0, 'Excelente', 0, 'Chalet lujo, piscina climatizada', '280€/mes', 'A', '2200', 'Reg. 1006', 1, 'Chalet'),
('C/ Mayor 12, Entresuelo, Málaga', 1, 650.00, 1, 1, 50.0, 'Reformado', 1, 'Ideal estudiante o joven pareja', '45€/mes', 'B', '220', 'Reg. 1007', 1, 'Piso'),
('Av. Andalucía 150, Ático Dúplex, Sevilla', 0, 335000.00, 3, 2, 125.0, 'Lujo', 0, 'Terraza 40m², vistas panorámicas', '110€/mes', 'A', '720', 'Reg. 1008', 1, 'Ático'),
('C/ del Carmen 5, 2ºA, Granada', 1, 580.00, 2, 1, 68.0, 'Buen estado', 1, 'Exterior, balcón, cerca Albaicín', '60€/mes', 'C', '280', 'Reg. 1009', 1, 'Piso'),
('Paseo Marítimo 89, 1ª línea, Marbella', 0, 890000.00, 4, 3, 210.0, 'Reformado', 0, 'Vistas al mar, urbanización con piscina', '250€/mes', 'B', '1800', 'Reg. 1010', 1, 'Apartamento'),
('C/ San Pedro 23, Bajo con patio, Córdoba', 0, 158000.00, 3, 1, 92.0, 'A reformar', 0, 'Patio 30m², posibilidad ampliar', '40€/mes', 'F', '380', 'Reg. 1011', 1, 'Piso'),
('C/ de la Rosa 44, Trastero + Garaje, Madrid', 0, 35000.00, 0, 0, 25.0, 'Buen estado', 0, 'Plaza garaje + trastero 8m²', '15€/mes', 'N/A', '80', 'Reg. 1012', 1, 'Garaje'),
('Av. del Mediterráneo 200, Nueva construcción, Valencia', 0, 278000.00, 3, 2, 102.0, 'Obra Nueva', 0, 'Entrega junio 2026, trastero incluido', '90€/mes', 'A', '520', 'Reg. 1013', 1, 'Piso'),
('C/ del Prado 15, Casa adosada, Alicante', 0, 265000.00, 4, 3, 160.0, 'Muy buen estado', 0, 'Jardín privado, comunidad con piscina', '130€/mes', 'C', '620', 'Reg. 1014', 1, 'Casa adosada'),
('C/ Comercio 78, Oficina 120m², Barcelona', 1, 1800.00, 0, 2, 120.0, 'Reformado', 0, 'Zona financiera, aire acondicionado', '200€/mes', 'D', '2100', 'Reg. 1015', 1, 'Oficina'),
('Plaza del Ayuntamiento 3, 4º, Valencia', 0, 198000.00, 2, 1, 80.0, 'Reformado', 0, 'Centro histórico, exterior', '70€/mes', 'B', '480', 'Reg. 1016', 1, 'Piso'),
('Urbanización La Finca, Pozuelo, Madrid', 0, 1250000.00, 6, 5, 550.0, 'Lujo', 0, 'Parcelas 2000m², seguridad 24h', '650€/mes', 'A+', '4800', 'Reg. 1017', 1, 'Chalet'),
('C/ Luna 22, Estudio reformado, Bilbao', 1, 720.00, 1, 1, 45.0, 'Reformado', 1, 'Amueblado, gastos incluidos', 'incluidos', 'C', '180', 'Reg. 1018', 1, 'Estudio'),
('C/ Real 56, Bajo comercial, Zaragoza', 0, 210000.00, 0, 1, 160.0, 'A reformar', 0, 'Posibilidad vivienda + local', '150€/mes', 'E', '1400', 'Reg. 1019', 1, 'Local'),
('Av. del Mar 100, 3 hab frente playa, Torrevieja', 1, 950.00, 3, 2, 105.0, 'Buen estado', 0, 'Urbanización con piscina y jardines', '110€/mes', 'C', '420', 'Reg. 1020', 1, 'Apartamento');

-- 6. Vendedores_has_Inmuebles (relacionar propietarios con sus inmuebles)
INSERT INTO `Vendedores_has_Inmuebles` (`Vendedor_id`, `Inmueble_id`, `Representante`) VALUES
(1, 1, 'Ana Ruiz (Propietaria única)'),
(2, 3, 'Pedro Gómez (Propietario)'),
(3, 4, 'Isabel Castillo (Heredera)'),
(4, 5, 'Francisco Navarro (Propietario)'),
(5, 8, 'Lucía Delgado (Propietaria)'),
(6, 6, 'Antonio Reyes (Propietario)'),
(7, 13, 'Marta Serrano (Propietaria)'),
(8, 19, 'José Luis Medina (Propietario)'),
(9, 11, 'Raquel Ortega (Propietaria)'),
(10, 14, 'David Cabrera (Propietario)'),
(11, 10, 'Elena Prieto (Propietaria)'),
(1, 16, 'Ana Ruiz (segunda vivienda)'),
(12, 17, 'Víctor Iglesias (Propietario)'),
(13, 9, 'Sandra Campos (Propietaria)'),
(14, 2, 'Jorge Vidal (Propietario)');

-- 7. Propiedad_Adicional
INSERT INTO `Propiedad_Adicional` (`Tipo`, `Derrama`, `IBI`, `Inmueble_id`) VALUES
('Trastero', 0.00, 'Incluido', 1),
('Garaje', 0.00, 'Incluido', 1),
('Garaje', 200.00, '80', 3),
('Trastero', 0.00, 'Incluido', 8),
('Garaje', 150.00, '90', 10),
('Trastero', 0.00, 'Incluido', 13),
('Garaje', 0.00, 'Incluido', 17),
('Piscina privada', 0.00, 'Incluido', 6),
('Trastero', 0.00, 'Incluido', 20);

-- 8. Inmuebles_has_Interesados (cruces de interés)
INSERT INTO `Inmuebles_has_Interesados` (`Inmueble_id`, `Interesado_id`) VALUES
(1, 1), (1, 4), (1, 13),
(2, 2), (2, 9),
(3, 6), (3, 20),
(4, 18),
(6, 6), (6, 17),
(7, 7), (7, 5),
(8, 1), (8, 10),
(10, 11),
(13, 12),
(14, 15),
(16, 4),
(20, 2), (20, 19);

-- 9. Citas (20 citas programadas y pasadas)
INSERT INTO `Citas` (`Fecha`, `Hora`, `Anotaciones`, `Inmueble_id`, `Trabajador_id`, `Interesado_id`) VALUES
('2025-12-05', '11:00:00', 'Primera visita, muy interesada', 1, 1, 1),
('2025-12-06', '17:30:00', 'Visita alquiler urgente', 2, 2, 2),
('2025-12-07', '10:00:00', 'Interesado en chalet de lujo', 6, 4, 6),
('2025-12-08', '12:00:00', 'Visita ático con terraza', 8, 1, 10),
('2025-12-04', '18:00:00', 'Ya visitado, segunda visita', 1, 1, 4),
('2025-12-03', '16:00:00', 'Visita piso a reformar inversión', 4, 5, 18),
('2025-11-28', '11:30:00', 'Visita local comercial', 5, 3, 8),
('2025-12-10', '10:30:00', 'Visita apartamento playa', 10, 1, 11),
('2025-12-11', '19:00:00', 'Visita bajo con patio', 11, 2, 15),
('2025-12-12', '12:00:00', 'Interesada en obra nueva', 13, 4, 17),
('2025-12-02', '17:00:00', 'Segunda visita, trae pareja', 8, 1, 1),
('2025-12-09', '11:00:00', 'Visita estudio estudiante', 7, 2, 7);

-- 10. Operacion (operaciones cerradas)
INSERT INTO `Operacion` (`Fecha`, `Monto`, `Inmueble_id`) VALUES
('2025-11-20', 182000.00, 1),
('2025-10-15', 330000.00, 8),
('2025-09-30', 800.00, 2), -- alquiler
('2025-08-22', 260000.00, 14),
('2025-11-05', 205000.00, 16);

-- 11. Interesados_has_Operacion (quién compró/alquiló)
INSERT INTO `Interesados_has_Operacion` (`Interesado_id`, `Operacion_id`, `Representante`) VALUES
(1, 1, 'Sofía Martínez (Compradora)'),
(10, 2, 'Rubén Parra (Comprador)'),
(2, 3, 'Carlos Díaz (Inquilino)'),
(15, 4, 'Tamara Roldán (Compradora)'),
(4, 5, 'Alejandro Jiménez (Comprador)');

-- 12. Reseñas
INSERT INTO `Reseñas` (`Fecha`, `Calificacion`, `Comentario`, `Operacion_id`) VALUES
('2025-11-25', 5, 'Excelente atención de Laura, todo rápido y claro.', 1),
('2025-10-20', 5, 'Muy profesionales, encontré mi ático ideal.', 2),
('2025-10-05', 4, 'Buen servicio, aunque tardaron un poco en responder emails.', 3),
('2025-08-30', 5, '100% recomendables, Javier muy atento.', 4),
('2025-11-10', 5, 'Encantado con la gestión, repetiría sin duda.', 5);

-- 13. Contrato
INSERT INTO `Contrato` (`Fecha_Firma`, `Tipo_Contrato`, `Terminos`, `Operacion_id`, `Trabajador_id`) VALUES
('2025-11-15', 'Compra-Venta', '10% arras penitenciales, entrega enero 2026', 1, 1),
('2025-10-10', 'Compra-Venta', 'Condiciones estándar, financiación hipotecaria', 2, 1),
('2025-09-25', 'Arrendamiento', '1 año prorrogable, fianza 2 meses', 3, 2),
('2025-08-20', 'Compra-Venta', 'Pago al contado, escritura noviembre', 4, 2),
('2025-11-01', 'Compra-Venta', 'Arras confirmatorias, entrega inmediata', 5, 4);

-- ¡Listo! Ahora tienes una base de datos mucho más completa y realista para pruebas.