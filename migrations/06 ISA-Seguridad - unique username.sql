-- Script para agregar restricción de unicidad al nombre de usuario
-- Evita que se registren nombres de usuario duplicados

IF NOT EXISTS (
    SELECT * 
    FROM sys.objects 
    WHERE object_id = OBJECT_ID(N'[dbo].[UQ_usuario_nombre_usuario]') 
      AND type = 'UQ'
)
BEGIN
    ALTER TABLE [dbo].[usuario]
    ADD CONSTRAINT UQ_usuario_nombre_usuario UNIQUE (nombre_usuario);
    PRINT 'Restricción UNIQUE añadida exitosamente.';
END
ELSE
BEGIN
    PRINT 'La restricción UNIQUE ya existe.';
END
