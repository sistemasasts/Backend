CREATE VIEW ISA_VIEW_TESTS
AS
select
    pl.propl_name nombre_propiedad,
    p.product_name nombre_producto,
    t.test_batch lote,
    t.test_date fecha,
    t.test_owner usuario,
    t.test_m1_ini m1ini,
    t.test_m1_end m1end,
    t.test_m2_ini m2ini,
    t.test_m2_end m2end,
    t.test_m3_ini m3ini,
    t.test_m3_end m3end,
    t.test_p1 p1,
    t.test_p2 p2,
    t.test_p3 p3,
    t.test_promissing promediar,
    t.test_result resultado,
    pl.propl_id propiedad_id,
    p.product_id producto_id,
    t.test_comment comentario,
    t.test_provider proveedor,
    t.test_res114_batch res114batch,
    t.test_res114_provider res114provider ,
    t.test_speed_rpm speed_rpm,
    t.test_temperature temperatura,
    t.test_time_viscocidad tiempo_viscocidad,
    t.test_torque torque

from test(NOLOCK) t
         inner join product (NOLOCK) p on t.test_idproduct = p.product_id
         inner join propertylist(NOLOCK) pl on pl.propl_id = t.test_property_code
