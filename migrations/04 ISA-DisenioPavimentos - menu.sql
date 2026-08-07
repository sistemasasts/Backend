--//================================= MENU DISENIO PAVIMENTOS=============================================
declare @idMenu bigint
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Diseño Pavimentos', '', 141, null, 'MENU', null)
set @idMenu = @@IDENTITY
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Tipos Diseño', 'pi pi-list', 141.1, @idMenu, 'SUBMENU', '/diseno-pavimentos/tipos-disenos')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Tipos Ligante', 'pi pi-list', 141.2, @idMenu, 'SUBMENU', '/diseno-pavimentos/tipos-ligantes')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Agregados Granulometría', 'pi pi-sliders-h', 141.3, @idMenu, 'SUBMENU', '/diseno-pavimentos/agregados-granulometria')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Agregados Quimica', 'pi pi-sliders-h', 141.4, @idMenu, 'SUBMENU', '/diseno-pavimentos/agregados-quimica')
insert into menu(creado_fecha, creado_por, modificado_fecha, modificado_por, activo, descripcion, etiqueta, icon, orden_menu, padre_id, tipo, url)
values(GETDATE(), 'admin', GETDATE(), 'admin', 1, '', 'Minas', 'pi pi-map-marker', 141.5, @idMenu, 'SUBMENU', '/diseno-pavimentos/minas')

-- ========================== TIEMPOS DE PROCESO DE INFORMES ====================================
UPDATE configuracion_tiempo_solicitud set vigencia_dias_disenio_pavimentos = vigencia_dias
go
UPDATE configuracion_tiempo_solicitud set vigencia_dias_disenio_pavimentos = 10 where orden ='RESPONDER_SOLICITUD' and tipo_entrega='INMEDIATO'
go
UPDATE configuracion_tiempo_solicitud set vigencia_dias_disenio_pavimentos = 10 where orden ='RESPONDER_SOLICITUD' and tipo_entrega='CASOS_ESPECIALES'

-- ======================= ACTUALIZACION CAMPOS PRIMITIVOS SE ==================================
INSERT INTO secuencial(abreviatura, activo, numero_secuencial, sucesion, tipo_solicitud) VALUES('SD', 1, '', 0, 'SOLICITUD_DISENIO');

