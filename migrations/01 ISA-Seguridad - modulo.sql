--======= copiar estructura y datos tabla menu =====================
SELECT *
INTO menu_backup
FROM menu
WHERE 1 = 0;

GO
SET IDENTITY_INSERT menu_backup ON;

INSERT INTO menu_backup (men_idmenu,men_icon, men_itemdesc, men_ref)
SELECT men_idmenu,men_icon, men_itemdesc, men_ref 
FROM menu;

SET IDENTITY_INSERT menu_backup OFF;

--======= copiar estructura y datos tabla submenu =====================

SELECT *
INTO submenu_backup
FROM submenu
WHERE 1 = 0;

GO
SET IDENTITY_INSERT submenu_backup ON;

INSERT INTO submenu_backup (sub_idsubmenu, sub_itemdesc, sub_icon, sub_ref, men_idmenu)
SELECT sub_idsubmenu, sub_itemdesc, sub_icon, sub_ref, men_idmenu
FROM submenu;

SET IDENTITY_INSERT submenu_backup OFF;

GO

--======= copiar estructura y datos tabla submenu =====================

SELECT *
INTO memu_rol_backup
FROM menu_rol
WHERE 1 = 0;

GO
INSERT INTO memu_rol_backup
SELECT *
FROM menu_rol;
GO

-- =========== Eliminar tablas de menu, submenu, rol_menu
drop table submenu
drop table menu_rol
drop table menu

-- =========== cargar perfiles 
insert into perfil(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, nombre, rol)
values(GETDATE(), 'admin',GETDATE(), 'admin', 1, 'ADMINISTRADOR SISTEMA', 'ADMINISTRADOR_SISTEMA')
insert into perfil(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, nombre, rol)
values(GETDATE(), 'admin',GETDATE(), 'admin', 1, 'ADMINISTRADOR', 'ADMINISTRADOR')
insert into perfil(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, nombre, rol)
values(GETDATE(), 'admin',GETDATE(), 'admin', 1, 'CALIDAD', 'CALIDAD')
insert into perfil(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, nombre, rol)
values(GETDATE(), 'admin',GETDATE(), 'admin', 1, 'COMPRAS', 'COMPRAS')
insert into perfil(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, nombre, rol)
values(GETDATE(), 'admin',GETDATE(), 'admin', 1, 'GERENCIA', 'GERENCIA')
insert into perfil(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, nombre, rol)
values(GETDATE(), 'admin',GETDATE(), 'admin', 1, 'COMERCIAL', 'COMERCIAL')
insert into perfil(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, nombre, rol)
values(GETDATE(), 'admin',GETDATE(), 'admin', 1, 'PLANTA', 'PLANTA')
insert into perfil(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, nombre, rol)
values(GETDATE(), 'admin',GETDATE(), 'admin', 1, 'OPERACIONES', 'OPERACIONES')
insert into perfil(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, nombre, rol)
values(GETDATE(), 'admin',GETDATE(), 'admin', 1, 'MANTENIMIENTO', 'MANTENIMIENTO')
insert into perfil(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, nombre, rol)
values(GETDATE(), 'admin',GETDATE(), 'admin', 1, 'JEFATURA PLANTA', 'JEFATURA_PLANTA')
insert into perfil(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, nombre, rol)
values(GETDATE(), 'admin',GETDATE(), 'admin', 1, 'JEFATURA PROYECTOS MANTENIMIENTO', 'JEFATURA_PROYECTOS_MANTENIMIENTO')