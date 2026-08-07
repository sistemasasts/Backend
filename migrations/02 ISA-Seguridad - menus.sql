-------======================-- SCRIPT PARA CARGAR MENUS =======================================
--================== insert menus ====================================================
declare @idMenu bigint
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Inicio', 'fa fa-fw fa-home', 10, null, 'MENU', '/home')
--// MENU CONFIGURACION
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Configuración', 'fa fa-cogs', 20, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Usuarios', 'pi pi-users', 20.1, @idMenu, 'SUBMENU', '/configuracion/usuarios')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Perfiles', 'pi pi-check-square', 20.2, @idMenu, 'SUBMENU', '/configuracion/perfiles')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Usuario-Rol Solicitud SE', 'pi pi-cog', 20.3, @idMenu, 'SUBMENU', '/configuracion/flujo_solicitud/SOLICITUD_ENSAYOS')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Usuario-Rol Solicitud PP', 'pi pi-cog', 20.4, @idMenu, 'SUBMENU', '/configuracion/flujo_solicitud/SOLICITUD_PRUEBAS_EN_PROCESO')

--// MENU I + D
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'I + D', 'fa fa-fw fa-magic', 30, null, 'MENU', null)
set @idMenu = @@IDENTITY

insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Producto', 'pi pi-warehouse', 30.1, @idMenu, 'SUBMENU', '/inves-dev/productos')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'HCC', 'fa fa-copy', 30.2, @idMenu, 'SUBMENU', '/inves-dev/hcc')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Ensayos', 'fa fa-flask', 30.3, @idMenu, 'SUBMENU', '/quality-development_resulttest')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Hsitórico S.Ensayos', 'fa fa-file-text', 30.4, @idMenu, 'SUBMENU', '/quality-development_wflow')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Reportes', 'fa fa-file-pdf-o', 30.5, @idMenu, 'SUBMENU', '/quality-development_report')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Proveedores', 'pi pi-truck', 30.6, @idMenu, 'SUBMENU', '/inves-dev/proveedores')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Propiedades', 'pi pi-list', 30.7, @idMenu, 'SUBMENU', '/inves-dev/propiedades')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Normas Lab.', 'pi pi-folder-open', 30.8, @idMenu, 'SUBMENU', '/inves-dev/normasLaboratorio')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Config. Ingreso MP', 'pi pi-cog', 30.9, @idMenu, 'SUBMENU', '/quality-development_config_entrymp')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Unidades de Medida', 'pi pi-list', 31.1, @idMenu, 'SUBMENU', '/inves-dev/unidadesMedida')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Inventario', 'pi pi-th-large', 31.2, @idMenu, 'SUBMENU', '/inves-dev/inventario')


--============= MENU INGRESO DE SOLICITUDES ========================
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Ingreso Solicitudes', 'fa fa-file-text', 40, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Solicitud Ensayo', 'pi pi-file-plus', 40.1, @idMenu, 'SUBMENU', '/solicitudse')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Solicitud DDP04', 'pi pi-clipboard', 40.2, @idMenu, 'SUBMENU', '/solicitudpp')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Hsitórico S.Ensayos', 'fa fa-file-text', 40.3, @idMenu, 'SUBMENU', '/quality-development_wflow')

--=================== MENU VALIDACION DE SOLICITUDES ================
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Validación Solicitud', 'fa fa-question-circle-o', 50, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Solicitud Ensayo', 'pi pi-file-plus', 50.1, @idMenu, 'SUBMENU', '/solicitudse/validar')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Solicitud DDP04', 'pi pi-clipboard', 50.2, @idMenu, 'SUBMENU', '/solicitudpp/validar')

--=================== MENU PROCESAR SOLICITUDES ===========================
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Procesar Solicitud Calidad', 'fa fa-share-square-o', 60, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Solicitud Ensayo', 'pi pi-file-plus', 60.1, @idMenu, 'SUBMENU', '/solicitudse/procesar')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Solicitud DDP04', 'pi pi-clipboard', 60.2, @idMenu, 'SUBMENU', '/solicitudpp/procesar/CALIDAD')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Reclamo MP', 'pi pi-exclamation-triangle', 60.3, @idMenu, 'SUBMENU', '/pnc/procesar-reclamos-mp')

-- =================== MENU APROBAR SOLICITUDES ==========================
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Aprobar Solicitud', 'fa fa-check', 70, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Solicitud Ensayo', 'pi pi-clipboard', 70.1, @idMenu, 'SUBMENU', '/solicitudse/aprobar')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Solicitud DDP04', 'pi pi-clipboard', 70.2, @idMenu, 'SUBMENU', '/solicitudpp/aprobar')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Salida Material PNC', 'pi pi-cart-arrow-down', 70.3, @idMenu, 'SUBMENU', '/pnc/aprobacion')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Reclamo MP', 'pi pi-exclamation-triangle', 70.4, @idMenu, 'SUBMENU', '/pnc/aprobacion-reclamos-mp/calidad')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Reclamo MP', 'pi pi-clipboard', 70.5, @idMenu, 'SUBMENU', '/quality-development_complaint_procesar_tarea')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Desviación Requisito', 'pi pi-arrow-down-left-and-arrow-up-right-to-center', 70.6, @idMenu, 'SUBMENU', '/pnc/desviacion-requisitos-aprobacion')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Extensión de Plazo', 'pi pi-clock', 70.7, @idMenu, 'SUBMENU', '/quality-development_solicitudse_aprobar_extensionplazo')

-- =================== MENU COORDINACION PLANTA ==========================
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Coordinación Planta', 'pi pi-calendar-minus', 80, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Asignación Solicitud DDP04', 'pi pi-user-plus', 80.1, @idMenu, 'SUBMENU', '/coordinacion-planta/asignar-solicitudes')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Aprobación Solicitud DDP04', 'pi pi-check-circle', 80.2, @idMenu, 'SUBMENU', '/coordinacion-planta/aprobar-solicitudes')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Reasignar Solicitud DDP04', 'pi pi-refresh', 80.3, @idMenu, 'SUBMENU', '/coordinacion-planta/reasignar-solicitudes')

-- =================== MENU PROCESAR SOLICITUDES PLANTA DDP04 ==========================
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Procesar Solicitud Planta', 'fa fa-share-square-o', 90, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Solicitud DDP04', 'pi pi-clipboard', 90.1, @idMenu, 'SUBMENU', '/solicitudpp/procesar/PRODUCCION')

-- =================== MENU COORDINACION DESARROLLO PRODUCTOS ==========================
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Coordinación Desarrollo Productos', 'fa fa-share-square-o', 100, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Asignación Solicitud DDP04', 'pi pi-user-plus', 100.1, @idMenu, 'SUBMENU', '/coordinacion-desarrollo/asignar-solicitudes')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Aprobación Solicitud Y DDP04', 'pi pi-check-square', 100.2, @idMenu, 'SUBMENU', '/coordinacion-desarrollo/aprobar-solicitudes')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Reasignación Solicitud DDP04', 'pi pi-refresh', 100.3, @idMenu, 'SUBMENU', '/coordinacion-desarrollo/reasignar-solicitudes')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Revisar Planes Acción', 'pi pi-check', 100.4, @idMenu, 'SUBMENU', '/solicitudse/revisarPlanAccion')

-- =================== MENU COORDINACION PROYECTOS Y MANTENIMIENTO ==========================
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Coordinación PYM', 'fa fa-share-square-o', 110, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Asignación Solicitud DDP04', 'pi pi-user-plus', 110.1, @idMenu, 'SUBMENU', '/coordinacion-mantenimiento/asignar-solicitudes')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Aprobación Solicitud DDP04', 'pi pi-check-square', 110.2, @idMenu, 'SUBMENU', '/coordinacion-mantenimiento/aprobar-solicitudes')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Ajuste Maquinaria DDP04', 'pi pi-briefcase', 110.3, @idMenu, 'SUBMENU', '/coordinacion-mantenimiento/ajustes-maquinaria')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Reasignación Solicitud DDP04', 'pi pi-refresh', 110.4, @idMenu, 'SUBMENU', '/coordinacion-mantenimiento/reasignar-solicitudes')

-- =================== MENU PROCESAR SOLICITUD PROYECTOS Y MANTENIMIENTO ==========================
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Procesar Solicitud Mantenimiento', 'fa fa-share-square-o', 120, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Solicitud DDP04', 'pi pi-clipboard', 120.1, @idMenu, 'SUBMENU', '/solicitudpp/procesar/MANTENIMIENTO')

--//================================= RECLAMOS DE MP=============================================
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Reclamos MP', 'fa fa-exclamation-triangle', 130, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Reclamos MP', 'fa fa-exclamation-triangle', 130.1, @idMenu, 'SUBMENU', '/quality-development_complaint')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Procesar Tareas', 'pi pi-list', 130.2, @idMenu, 'SUBMENU', '/quality-development_complaint_procesar_tarea')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Aprobación', 'pi pi-exclamation-circle', 130.3, @idMenu, 'SUBMENU', '/pnc/aprobacion-reclamos-mp/compras')


--//================================= MENU PNC=============================================
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'PNC', 'fa fa-exclamation-triangle', 140, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Reclamos MP', 'fa fa-exclamation-triangle', 140.1, @idMenu, 'SUBMENU', '/pnc/reclamos-mp')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Defectos', 'pi pi-info-circle', 140.2, @idMenu, 'SUBMENU', '/pnc/defectos')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Ingresos', 'pi pi-plus', 140.3, @idMenu, 'SUBMENU', '/pnc/ingresos')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Procesar Tareas', 'pi pi-list', 140.4, @idMenu, 'SUBMENU', '/pnc/tareas-procesar')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Validar Tareas', 'pi pi-check', 140.5, @idMenu, 'SUBMENU', '/pnc/tareas-validar')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Reporte Comercial', 'pi pi-table', 140.6, @idMenu, 'SUBMENU', '/pnc/reporte-comercial')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Desviación Requisitos', 'pi pi-arrow-circle-right', 140.7, @idMenu, 'SUBMENU', '/pnc/desviacion-requisitos')

--//================================= MENU CONSULTAS=============================================
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Consultas', 'fa fa-search', 150, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Solicitud Ensayos', 'pi pi-file-plus', 150.1, @idMenu, 'SUBMENU', '/consultas/solicitud-ensayo')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Ensayos', 'pi pi-hammer', 150.2, @idMenu, 'SUBMENU', '/consultas/ensayos')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Solicitudes DDP04', 'pi pi-file-o', 150.3, @idMenu, 'SUBMENU', '/consultas/solicitud-ddp04')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Reporte Comercial', 'pi pi-table', 150.4, @idMenu, 'SUBMENU', '/quality-development_pnc_reporteComercial')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Desviación Requisitos', 'pi pi-arrow-circle-right', 150.5, @idMenu, 'SUBMENU', '/quality-development_pnc_consulta_desviacion_req')

