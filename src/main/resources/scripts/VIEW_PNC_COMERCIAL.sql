CREATE OR ALTER VIEW VIEW_PNC_COMERCIAL
AS
SELECT a.id,
       a.producto_product_id producto_id,
       p.product_name nombre_producto,
       b.saldo cantidad_existente,
       (
           SELECT abreviatura
           FROM unidad_medida(NOLOCK) u
           WHERE u.id = a.unidad_id
       ) unidad,
       a.numero,
       a.lote,
       b.ubicacion,
       b.validez,
       b.defecto_id
FROM producto_no_conforme(NOLOCK) a
         INNER JOIN pnc_defecto(NOLOCK) b ON a.id = b.producto_no_conforme_id
         INNER JOIN product(NOLOCK) p ON p.product_id = a.producto_product_id
WHERE b.saldo > 0;