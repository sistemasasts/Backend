-- ===================================================================
-- Script: 05 ISA-Seguridad - actualizar usernames.sql
-- Descripción:
--   Busca y actualiza todas las referencias a nombres de usuario antiguos
--   (provenientes de 'userimptek') con sus nuevos valores modificados
--   (en la tabla 'usuario') en todas las tablas y columnas del sistema.
--
-- Exclusiones:
--   - Tablas 'userimptek', 'usuario', 'configuracion_flujo_prueba_proceso'.
--   - Columnas de la tabla 'employee' ('emp_name', 'emp_lastname', 'emp_job')
--     ya que son campos de perfil/texto descriptivo del empleado.
-- ===================================================================

USE [isa];
GO

SET NOCOUNT ON;

BEGIN TRANSACTION;

BEGIN TRY
    -- 1. Crear tabla temporal de mapeo de usuarios modificados
    IF OBJECT_ID('tempdb..#UsernameMapping') IS NOT NULL
        DROP TABLE #UsernameMapping;

    CREATE TABLE #UsernameMapping (
        old_username VARCHAR(100) COLLATE Modern_Spanish_CI_AS PRIMARY KEY,
        new_username VARCHAR(100) COLLATE Modern_Spanish_CI_AS
    );

    -- Insertar la relación uniendo por identificación y nombre completo (para evitar duplicados por CI genéricas/vacías)
    INSERT INTO #UsernameMapping (old_username, new_username)
    SELECT ui.user_iduser, u.nombre_usuario
    FROM [dbo].[userimptek] ui
    INNER JOIN [dbo].[employee] e ON ui.emp_ciemployee = e.emp_ciemployee
    INNER JOIN [dbo].[usuario] u ON (ui.emp_ciemployee = u.numero_identificacion AND CONCAT(e.emp_name, ' ', e.emp_lastname) = u.nombre)
    WHERE ui.user_iduser <> u.nombre_usuario;

    DECLARE @changedCount INT = @@ROWCOUNT;
    PRINT '================================================================';
    PRINT 'Mapeo de Usuarios Creado: ' + CAST(@changedCount AS VARCHAR(10)) + ' usuarios cambiaron de nombre.';
    PRINT '================================================================';

    -- Listar los mapeos detectados para verificación
    DECLARE @old_u VARCHAR(100), @new_u VARCHAR(100);
    DECLARE map_cursor CURSOR FOR SELECT old_username, new_username FROM #UsernameMapping;
    OPEN map_cursor;
    FETCH NEXT FROM map_cursor INTO @old_u, @new_u;
    WHILE @@FETCH_STATUS = 0
    BEGIN
        PRINT '  * ' + @old_u + ' -> ' + @new_u;
        FETCH NEXT FROM map_cursor INTO @old_u, @new_u;
    END
    CLOSE map_cursor;
    DEALLOCATE map_cursor;
    PRINT '================================================================';

    -- 2. Declarar variables para la iteración dinámica
    DECLARE @tableName NVARCHAR(256);
    DECLARE @columnName NVARCHAR(256);
    DECLARE @sql NVARCHAR(MAX);
    DECLARE @rowsUpdated INT;

    -- Obtener todas las columnas de tipo caracter del esquema
    DECLARE col_cursor CURSOR FOR
    SELECT t.name AS TableName, c.name AS ColumnName
    FROM sys.tables t
    INNER JOIN sys.columns c ON t.object_id = c.object_id
    INNER JOIN sys.types y ON c.user_type_id = y.user_type_id
    WHERE y.name IN ('varchar', 'char', 'nvarchar', 'nchar')
      -- Exclusiones de seguridad
      AND t.name NOT IN ('userimptek', 'usuario', 'configuracion_flujo_prueba_proceso')
      AND NOT (t.name = 'employee' AND c.name IN ('emp_name', 'emp_lastname', 'emp_job'))
    ORDER BY t.name, c.name;

    OPEN col_cursor;
    FETCH NEXT FROM col_cursor INTO @tableName, @columnName;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        -- Verificar si la columna actual contiene algún valor a actualizar antes de proceder
        DECLARE @matchCount INT = 0;
        SET @sql = N'SELECT @cnt = COUNT(*) FROM [dbo].[' + @tableName + N'] t ' +
                   N'INNER JOIN #UsernameMapping m ON t.[' + @columnName + N'] = m.old_username';
        
        EXEC sp_executesql @sql, N'@cnt INT OUTPUT', @cnt = @matchCount OUTPUT;

        IF @matchCount > 0
        BEGIN
            -- Realizar la actualización
            SET @sql = N'UPDATE t SET t.[' + @columnName + N'] = m.new_username ' +
                       N'FROM [dbo].[' + @tableName + N'] t ' +
                       N'INNER JOIN #UsernameMapping m ON t.[' + @columnName + N'] = m.old_username';
            
            EXEC sp_executesql @sql;
            SET @rowsUpdated = @@ROWCOUNT;
            
            PRINT 'Actualizado - Tabla: [' + @tableName + '], Columna: [' + @columnName + '] - Filas afectadas: ' + CAST(@rowsUpdated AS VARCHAR(10));
        END

        FETCH NEXT FROM col_cursor INTO @tableName, @columnName;
    END

    CLOSE col_cursor;
    DEALLOCATE col_cursor;

    -- Confirmar la transacción
    COMMIT TRANSACTION;
    PRINT '================================================================';
    PRINT 'Transacción COMPLETADA y CONFIRMADA (COMMIT) exitosamente.';
    PRINT '================================================================';
END TRY
BEGIN CATCH
    -- Revertir los cambios en caso de error
    IF @@TRANCOUNT > 0
        ROLLBACK TRANSACTION;
    
    PRINT '================================================================';
    PRINT 'ERROR DETECTADO. Transacción REVERTIDA (ROLLBACK).';
    PRINT 'Mensaje de Error: ' + ERROR_MESSAGE();
    PRINT '================================================================';
    
    -- Limpieza del cursor en caso de falla abrupta
    IF CURSOR_STATUS('global', 'col_cursor') >= 0
    BEGIN
        CLOSE col_cursor;
        DEALLOCATE col_cursor;
    END
END CATCH
GO
