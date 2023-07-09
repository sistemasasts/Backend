package com.isacore.quality.tx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.dto.ProductDto;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.ProductType;
import com.isacore.quality.service.IProductService;
import com.isacore.quality.service.IProviderService;
import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxProduct {

	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	public static final String TX_NAME_GetAllProducts = "GetAllProducts";
	public static final String TX_CODE_GetAllProducts = "TxQQRgetProducts";

	public static final String TX_NAME_GetProductById = "GetProductById";
	public static final String TX_CODE_GetProductById = "TxQQRgetProductById";

	public static final String TX_NAME_SetProduct = "SetProduct";
	public static final String TX_CODE_SetProduct = "TxQQRsetProduct";

	public static final String TX_NAME_GetFeature = "GetFeatures";
	public static final String TX_CODE_GetFeature = "TxQQRgetProductFeature";

	public static final String TX_NAME_GetProductByIdAndPropertyId = "GetProductByIdAndPropertyId";
	public static final String TX_CODE_GetProductByIdAndPropertyId = "TxQQRgetProductByIdAndPropertyId";

	public static final String TX_NAME_GetProductPropertiesById = "GetProductPropertiesById";
	public static final String TX_CODE_GetProductPropertiesById = "TxQQRgetProductPropertiesById";

	@Autowired
	private IProductService service;

	@Autowired
	private IProviderService sProvider;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * TX NAME: GetAllProducts Obtiene todos los productos de la base de datos
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQRgetProducts(WebRequestIsa wri) {
		logger.info("> TX: TxQQRgetProducts");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetAllProducts);
		wrei.setTransactionCode(TX_CODE_GetAllProducts);

		List<Product> products = this.service.findAllProducts();

		if (products.isEmpty() || products == null) {
			logger.info("> No existe registros en la base de datos");
			wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
		} else {
			try {
				// Serializamos la lista a JSON
				List<Product> productsTmp = new ArrayList<>();
				for (Product product : products) {
					/*
					 * if(product.getTypeProduct().equalsIgnoreCase("MP")) {
					 * 
					 * }
					 */
					product.setProviders(this.sProvider.findByProductIdVigente(product.getIdProduct()));
					productsTmp.add(product);

				}
				String json = JSON_MAPPER.writeValueAsString(productsTmp);
				// encriptamos el JSON

				wrei.setMessage(WebResponseMessage.SEARCHING_OK);
				wrei.setStatus(WebResponseMessage.STATUS_OK);
				wrei.setParameters(json);
				return new ResponseEntity<Object>(wrei, HttpStatus.OK);

			} catch (IOException e) {
				logger.error("> error al serializar el JSON");
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	/**
	 * TX NAME: GetProductById Obtiene un producto en base al id solicitado
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQRgetProductById(WebRequestIsa wri) {
		logger.info("> TX: TxQQRgetProductById");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetProductById);
		wrei.setTransactionCode(TX_CODE_GetProductById);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {
			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + Product.class);
				ObjectMapper mapper = new ObjectMapper();
				// mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
				Product p = mapper.readValue(jsonValue, Product.class);
				logger.info("> id Product: " + p.getIdProduct());
				p = this.service.findOnlyProductById(p);

				if (p == null) {
					logger.info("> Product not found");
					wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
					wrei.setStatus(WebResponseMessage.STATUS_INFO);
					return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
				} else {
					p.setFeature(null);
					String json = JSON_MAPPER.writeValueAsString(p);

					wrei.setMessage(WebResponseMessage.SEARCHING_OK);
					wrei.setParameters(json);
					wrei.setStatus(WebResponseMessage.STATUS_OK);
					return new ResponseEntity<Object>(wrei, HttpStatus.OK);

				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("> No se ha podido serializar el JSON a la clase: " + Product.class);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
			}

		}
	}

	/**
	 * TX NAME: Crea y actualiza el maestro de productos
	 * 
	 * @param wri es el objeto que trae los parámetros para la transacción
	 * @return Rertorna un mensaje de OK si la transacción fue exitosa y un BAD si
	 *         no lo fue
	 */

	public ResponseEntity<Object> TxQQRsetProduct(WebRequestIsa wri) {
		logger.info("> TX: TxQQRsetProduct");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_SetProduct);
		wrei.setTransactionCode(TX_CODE_SetProduct);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {

			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + Product.class);
				Product p = JSON_MAPPER.readValue(jsonValue, Product.class);
				logger.info("> objeto a guardar: " + p.toString());
				Product pp = this.service.create(p);

				String json = JSON_MAPPER.writeValueAsString(pp);

				wrei.setMessage(WebResponseMessage.CREATE_UPDATE_OK);
				wrei.setParameters(json);
				wrei.setStatus(WebResponseMessage.STATUS_OK);
				return new ResponseEntity<Object>(wrei, HttpStatus.OK);

			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + Product.class);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				return new ResponseEntity<Object>(wri, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
	}

	/**
	 * TX NAME: GetProductFeature obtiene las características del producto
	 * solicitado
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQRgetProductFeature(WebRequestIsa wri) {
		logger.info("> TX: TxQQRgetProductFeature");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetFeature);
		wrei.setTransactionCode(TX_CODE_GetFeature);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {
			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + Product.class);
				ObjectMapper mapper = new ObjectMapper();
				Product p = mapper.readValue(jsonValue, Product.class);
				logger.info("> id Product: " + p.getIdProduct());
				p = this.service.findProductFeature(p.getIdProduct());

				if (p == null) {
					logger.info("> Product not found");
					wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
					wrei.setStatus(WebResponseMessage.STATUS_INFO);
					return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
				} else {
//						JSON_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
					String json = JSON_MAPPER.writeValueAsString(p);

					wrei.setMessage(WebResponseMessage.SEARCHING_OK);
					wrei.setParameters(json);
					wrei.setStatus(WebResponseMessage.STATUS_OK);
					return new ResponseEntity<Object>(wrei, HttpStatus.OK);

				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("> No se ha podido serializar el JSON a la clase: " + Product.class);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
			}

		}
	}

	/**
	 * TX NAME: GetProductIdAndPropertyId obtiene la propiedad del producto
	 * solicitado
	 * 
	 * @param wri
	 * @return
	 */

	public ResponseEntity<Object> TxQQRgetProductByIdAndPropertyId(WebRequestIsa wri) {
		logger.info("> TX: TxQQRgetProductFeature");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetProductByIdAndPropertyId);
		wrei.setTransactionCode(TX_CODE_GetProductByIdAndPropertyId);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {
			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + Product.class);
				ObjectMapper mapper = new ObjectMapper();
				Product p = mapper.readValue(jsonValue, Product.class);
				logger.info("> id Product: " + p.getIdProduct());
				//String valueCodeProperty = p.getProperties().get(0).getPropertyList().getIdProperty();
				String valueCodeProperty = p.getPropertyList();
				ProductDto pd = this.service.findProductByIdAndIdProperty(p.getIdProduct(), valueCodeProperty);

				if (pd == null) {
					logger.info("> Product and Property not found");
					wrei.setMessage(WebResponseMessage.PROUDUCT_PROPERTY_NOT_FOUND);
					wrei.setStatus(WebResponseMessage.STATUS_INFO);
					return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
				} else {
//						JSON_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
					JSONObject jobj = new JSONObject();
					jobj.put("data", p);
					String json = JSON_MAPPER.writeValueAsString(pd);

					wrei.setMessage(WebResponseMessage.SEARCHING_OK);
					wrei.setParameters(json);
					wrei.setStatus(WebResponseMessage.STATUS_OK);
					return new ResponseEntity<Object>(wrei, HttpStatus.OK);

				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("> No se ha podido serializar el JSON a la clase: " + Product.class);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		}
	}

	/**
	 * TX NAME: GetProductPropertiesByIdProduct obtiene las propiedades del producto
	 * solicitado
	 * 
	 * @param wri
	 * @return
	 */

	public ResponseEntity<Object> TxQQRgetProductPropertiesById(WebRequestIsa wri) {
		logger.info("> TX: TxQQRgetProductPropertiesByIdProduct");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetProductPropertiesById);
		wrei.setTransactionCode(TX_CODE_GetProductPropertiesById);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {
			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + Product.class);
				ObjectMapper mapper = new ObjectMapper();
				Product p = mapper.readValue(jsonValue, Product.class);
				logger.info("> id Product: " + p.getIdProduct());
				// String valueCodeProperty=
				// p.getProperties().get(0).getPropertyList().getIdProperty();
				Product pd = this.service.findProductPropertiesByIdProduct(p.getIdProduct());

				if (pd == null) {
					logger.info("> Product and Property not found");
					wrei.setMessage(WebResponseMessage.PROUDUCT_PROPERTY_NOT_FOUND);
					wrei.setStatus(WebResponseMessage.STATUS_INFO);
					return new ResponseEntity<Object>(wrei, HttpStatus.NOT_FOUND);
				} else {
//						JSON_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
					JSONObject jobj = new JSONObject();
					jobj.put("data", p);
					if (pd.getTypeProduct().equals(ProductType.MATERIA_PRIMA))
						pd.setProviders(this.sProvider.findByProductIdVigente(pd.getIdProduct()));
					String json = JSON_MAPPER.writeValueAsString(pd);

					wrei.setMessage(WebResponseMessage.SEARCHING_OK);
					wrei.setParameters(json);
					wrei.setStatus(WebResponseMessage.STATUS_OK);
					return new ResponseEntity<Object>(wrei, HttpStatus.OK);

				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("> No se ha podido serializar el JSON a la clase: " + Product.class);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		}
	}
}
