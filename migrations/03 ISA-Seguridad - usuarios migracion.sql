-- ===================================================================
-- Script para migrar datos de la tabla 'userimptek' a 'usuario'
--
-- NOTA: Reemplaza el valor de 'area_id' con un ID válido de tu
--       tabla 'area' antes de ejecutar el script.
-- ===================================================================

USE [isa];
GO

-- 1. Habilitar IDENTITY_INSERT para poder especificar los valores de la
--    columna de identidad 'id' si fuera necesario. Sin embargo, en este
--    caso la tabla 'usuario' tiene una nueva identidad, por lo que este
--    paso no es necesario si quieres que se autogenere.
--    Lo dejamos comentado, ya que la tabla 'usuario' tiene IDENTITY(1,1).

-- SET IDENTITY_INSERT [dbo].[usuario] ON;
-- GO

-- 2. Insertar los datos de userimptek en la tabla usuario
INSERT INTO [dbo].[usuario]
(
    -- Columnas de la tabla 'usuario'
    -- La columna 'id' (IDENTITY) se generará automáticamente
	--[id],
    [creado_fecha],
    [creado_por],
    [modificado_fecha],
    [modificado_por],
    [email], -- No tiene un equivalente en userimptek, se inserta NULL
    [estado],
    [nombre],
    [nombre_usuario],
    [numero_identificacion],
    [trabajo], -- No tiene un equivalente en userimptek, se inserta NULL
    [area_id],
	[contrasena]
)
SELECT
    -- Valores de la tabla 'userimptek' y valores por defecto
	--ui.user_iduser as id,
    GETDATE() AS creado_fecha,                     -- Fecha actual
    'admin' AS creado_por,                       -- Usuario por defecto
    ui.user_lastaccess AS modificado_fecha,
    'admin' AS modificado_por,                     -- Usuario por defecto
    e.emp_email AS email,
    CASE e.emp_state
        WHEN 1 THEN 'ACTIVO'
        WHEN 0 THEN 'INACTIVO'
        ELSE 'INACTIVO' -- Valor por defecto para otros casos
    END AS estado,                              -- Estado por defecto
    CONCAT(e.emp_name, ' ' ,emp_lastname) AS nombre,
    ui.user_iduser AS nombre_usuario,
    ui.emp_ciemployee AS numero_identificacion,
    e.emp_job AS trabajo,
    e.area_id AS area_id,
	ui.user_pass
FROM
    [dbo].[userimptek] ui inner join employee e on ui.emp_ciemployee= e.emp_ciemployee;
GO

-- 3. Deshabilitar IDENTITY_INSERT si se habilitó
 --SET IDENTITY_INSERT [dbo].[usuario] OFF;
-- GO

-- 4. Verificación (opcional)
-- Comentar o eliminar esta sección después de la verificación inicial.
-- SELECT TOP 100 * FROM [dbo].[usuario] ORDER BY id DESC;
-- GO


--- Se corrige tablas dependientes de USERIMPTEK
alter table configuracion_flujo_prueba_proceso add usuario_id bigint NOT NULL DEFAULT 0; 
update configuracion_flujo_prueba_proceso set usuario_id = (select top 1 id from usuario where nombre_usuario= usuario_user_iduser )

go
alter table configuracion_usuario_rol_ensayo add usuario_user_iduser varchar(100);
go
update configuracion_usuario_rol_ensayo set usuario_user_iduser = usuario_id;
go
ALTER TABLE configuracion_usuario_rol_ensayo DROP CONSTRAINT FKh11pu8e9g1j76yatv1uaynx5;
go
alter table configuracion_usuario_rol_ensayo drop column usuario_id
go
alter table configuracion_usuario_rol_ensayo add usuario_id bigint NOT NULL DEFAULT 0; 
go
update configuracion_usuario_rol_ensayo set usuario_id = (select top 1 id from usuario where nombre_usuario= usuario_user_iduser )

go
-- actualizacion de tipo de archivos solicitud_documento
UPDATE solicitud_documento
SET tipo = 
    CASE 
        WHEN nombre_archivo LIKE '%.pdf'  THEN 'application/pdf'
        WHEN nombre_archivo LIKE '%.doc%'  OR
			 nombre_archivo LIKE '%.docx%' THEN 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
        WHEN nombre_archivo LIKE '%.xls%' THEN 'Excel'
        WHEN nombre_archivo LIKE '%.ppt%' THEN 'PowerPoint'
        WHEN nombre_archivo LIKE '%.png'  THEN 'image/png' 
        WHEN nombre_archivo LIKE '%.jpg'  THEN 'image/jpg' 
        WHEN nombre_archivo LIKE '%.jpeg' THEN 'image/jpeg'
		WHEN nombre_archivo LIKE '%.mp4'  THEN 'video/mp4'
        WHEN nombre_archivo LIKE '%.rtf'  THEN 'application/msword'
		WHEN nombre_archivo LIKE '%.zip'  THEN 'application/x-zip-compressed'
		WHEN nombre_archivo LIKE '%.rar'  THEN 'application/x-compressed'
		WHEN nombre_archivo LIKE '%.eml'  THEN 'message/rfc822'
        
    END
WHERE nombre_archivo IS NOT NULL and tipo is null;