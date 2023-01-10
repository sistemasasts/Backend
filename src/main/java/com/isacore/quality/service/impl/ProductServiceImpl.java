package com.isacore.quality.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.isacore.exception.reporte.JasperReportsException;
import com.isacore.exception.reporte.ReporteExeption;
import com.isacore.quality.dto.ProductDto;
import com.isacore.quality.dto.ReportProductDTO;
import com.isacore.quality.dto.ReportProductSpecificationDTO;
import com.isacore.quality.model.Family;
import com.isacore.quality.model.Feature;
import com.isacore.quality.model.InformationAditional;
import com.isacore.quality.model.InformationAditionalType;
import com.isacore.quality.model.LineProduction;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.ProductType;
import com.isacore.quality.model.Property;
import com.isacore.quality.model.PropertyList;
import com.isacore.quality.model.PropertyPeriodicity;
import com.isacore.quality.repository.IProductRepo;
import com.isacore.quality.repository.IPropertyRepo;
import com.isacore.quality.service.IProductService;
import com.isacore.servicio.reporte.IGeneradorJasperReports;

@Service
public class ProductServiceImpl implements IProductService {

	private static final Log LOG = LogFactory.getLog(ProductServiceImpl.class);
		
	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private IProductRepo repo;

	@Autowired
	private IPropertyRepo repoProperty;
	
	@Autowired
	private Gson gsonLog;
	
	 @Autowired
	 private IGeneradorJasperReports reporteServicio;

	@Override
	public List<Product> findAll() {
		return this.repo.findAll();
	}

	@Override
	public Product create(Product obj) {
		LOG.info(String.format("Producto creado:  %s", gsonLog.toJson(obj)));
		return this.repo.save(obj);
	}

	@Override
	public Product findById(Product obj) {
		Optional<Product> o = this.repo.findById(obj.getIdProduct());
		if (o.isPresent())
			return o.get();
		else
			return null;
	}

	@Override
	public Product update(Product obj) {
		return this.repo.save(obj);
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

	@Override
	public Product findOnlyProductById(Product p) {
		Query query = entityManager.createNativeQuery(
				"select p.product_id, p.product_sap_code, p.product_name, p.product_description, p.product_itcdq, p.product_type, p.product_typetxt,\r\n"
						+ "f.fam_id, f.fam_name,\r\n" + "lp.lp_id, lp.lp_name, p.product_review\r\n"
						+ "from product p\r\n" + "full join family f on p.fam_id = f.fam_id\r\n"
						+ "full join line_production lp on p.lp_id = lp.lp_id\r\n" + "WHERE P.product_id = ?");

		query.setParameter(1, p.getIdProduct());
		List<Object[]> list = query.getResultList();

		if (list.isEmpty() || list == null)
			return null;
		else {
			Object[] o = list.get(0);
			Product pp = new Product();
			pp.setIdProduct((Integer) o[0]);
			pp.setSapCode((String) o[1]);
			pp.setNameProduct((String) o[2]);
			pp.setDescProduct((String) o[3]);
			pp.setItcdq((String) o[4]);
			pp.setTypeProduct(ProductType.valueOf((String) o[5]));
			pp.setTypeProductTxt((String) o[6]);

			if (o[6] != null) {
				Family f = new Family();
				f.setFamilyId((Integer) o[7]);
				f.setFamilyName((String) o[8]);
				pp.setFamily(f);
			}

			if (o[8] != null) {
				LineProduction lp = new LineProduction();
				lp.setIdLineP((Integer) o[9]);
				lp.setLineName((String) o[10]);
				pp.setLineProduction(lp);
			}
			pp.setReview((String) o[11]);
			return pp;

		}

	}

	@Override
	public List<Product> findAllProducts() {

		List<Product> products;
		List<Object[]> list = this.repo.findAllProducts();

		if (list.isEmpty() || list == null)
			return null;
		else {
			products = new ArrayList<>();
			list.forEach((Object[] x) -> {
				Product pp = new Product();

				if (x[0] != null) {
					pp.setIdProduct((Integer) x[0]);
					pp.setSapCode((String) x[1]);
					pp.setNameProduct((String) x[2]);
					pp.setDescProduct((String) x[3]);
					pp.setItcdq((String) x[4]);
					pp.setTypeProduct(ProductType.valueOf((String) x[5]));

					if (x[6] != null) {
						Family f = new Family();
						f.setFamilyId((Integer) x[6]);
						f.setFamilyName((String) x[7]);
						pp.setFamily(f);
					}

					if (x[8] != null) {
						LineProduction lp = new LineProduction();
						lp.setIdLineP((Integer) x[8]);
						lp.setLineName((String) x[9]);
						pp.setLineProduction(lp);
					}

					products.add(pp);
				}

			});
			return products;
		}
	}

	@Override
	public Product findProductByIdAndPeriod(Integer idP, String period) {

		List<Object[]> list = this.repo.findProductByIdAndPeriod(idP, period);

		if (list.isEmpty() || list == null)
			return null;
		else {
			Product product = new Product();

			Object[] o = list.get(0);
			product.setIdProduct((Integer) o[0]);
			product.setSapCode((String) o[1]);
			product.setNameProduct((String) o[2]);
			product.setDescProduct((String) o[3]);
			product.setTypeProduct(ProductType.valueOf((String) o[4]));

			List<Property> listProperty = new ArrayList<>();
			list.forEach((Object[] x) -> {
				Property p = new Property();
				PropertyList pl = new PropertyList();

				pl.setIdProperty((String) x[5]);
				pl.setNameProperty((String) x[6]);
				// p.setIdProperty((String)x[5]);
				// p.setNameProperty((String)x[6]);
				p.setTypeProperty((String) x[7]);
				if(x[8] != null)
					pl.setPeriodicity(PropertyPeriodicity.valueOf((String) x[8]));
				// pl.setNormName((String)x[9]);
				// p.setPeriodicityProperty((String)x[8]);
				// p.setNormProperty((String)x[9]);
				p.setPropertyNorm((String) x[9]);
				p.setPropertyList(pl);
				p.setMinProperty((x[10]) == null ? null : ((BigDecimal) x[10]).doubleValue());
				p.setMaxProperty((x[11]) == null ? null : ((BigDecimal) x[11]).doubleValue());
				p.setUnitProperty((String) x[12]);
				p.setViewProperty((String) x[13]);
				p.setViewPropertyOnHcc((Boolean) x[14]);
				listProperty.add(p);
			});
			product.setProperties(listProperty);
			return product;
		}
	}

	@Override
	public Product findProductFeature(Integer idP) {
		List<Object[]> list = this.repo.findProductFeature(idP);

		if (list.isEmpty() || list == null)
			return null;
		else {

			Product product = new Product();

			Object[] o = list.get(0);
			product.setIdProduct((Integer) o[0]);
			product.setSapCode((String) o[1]);
			product.setNameProduct((String) o[2]);

			Feature f = new Feature();
			f.setLength((o[3]) == null ? null : ((BigDecimal) o[3]).doubleValue());
			f.setUnitLength((String) o[4]);
			f.setGrossWeigth((o[5]) == null ? null : ((BigDecimal) o[5]).doubleValue());
			f.setUnitGrossWeigth((String) o[6]);
			f.setNetWeigth((o[7]) == null ? null : ((BigDecimal) o[7]).doubleValue());
			f.setUnitNetWeigth((String) o[8]);
			f.setWeigthArea((o[9]) == null ? null : ((BigDecimal) o[9]).doubleValue());
			f.setUmb((String) o[10]);
			f.setUnitCost((o[11]) == null ? null : ((BigDecimal) o[11]).doubleValue());
			f.setDistributorPrice((o[12]) == null ? null : ((BigDecimal) o[12]).doubleValue());

			product.setFeature(f);

			return product;
		}
	}

	@Override
	public void saveProductProperty(List<Product> listProduct, String user) {

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String dateUpdate = now.format(formatter);

		listProduct.forEach(x -> {

			System.out.println("Producto a guardar----> " + x.getNameProduct());

			Product p;
			List<Property> listProperties = x.getProperties();
			x.setProperties(null);
			p = this.repo.save(x);

			listProperties.forEach(y -> {
				y.setProduct(p);
				y.setDateUpdate(LocalDateTime.now());
				Long unidad = p.getUnit() != null ? y.getUnit().getId() : null;
				List<Object> propertyDescription = this.repoProperty
						.validateExistProperty(y.getProduct().getIdProduct(), y.getPropertyList().getIdProperty());
				if (propertyDescription == null)
					this.repoProperty.createProperty(y.getProduct().getIdProduct(), y.getPropertyList().getIdProperty(),
							y.getMinProperty(), y.getMaxProperty(), unidad, y.getViewProperty(),
							dateUpdate, y.getTypeProperty(), y.getPropertyNorm(), user);
				else
					this.repoProperty.updateProperty(y.getMinProperty(), y.getMaxProperty(), unidad,
							y.getViewProperty(), dateUpdate, y.getTypeProperty(), y.getPropertyNorm(), user,
							y.getProduct().getIdProduct(), y.getPropertyList().getIdProperty());
			});

		});
	}

	@Override
	public ProductDto findProductByIdAndIdProperty(Integer idP, String idProperty) {
		List<Object[]> list = this.repo.findProductByIdAndIdProperty(idP, idProperty);
		ProductDto pdto = new ProductDto();

		if (list.isEmpty() || list == null)
			return null;
		else {
			Object[] o = list.get(0);

			pdto.setProductId(idP);
			pdto.setProductsapCode((String) o[1]);
			pdto.setProductName((String) o[2]);
			pdto.setProductDescription((String) o[3]);
			pdto.setProductType((String) o[4]);
			pdto.setPropertyId((String) o[5]);
			pdto.setPropertyMax((BigDecimal) o[11]);
			pdto.setPropertyMin((BigDecimal) o[10]);
			pdto.setPropertyName((String) o[6]);
			pdto.setPropertyNorm((String) o[9]);
			pdto.setPropertyPeriodicity((String) o[8]);
			pdto.setPropertyType((String) o[7]);
			pdto.setPropertyUnit((String) o[12]);
			pdto.setPropertyView((String) o[13]);
			pdto.setPropertyViewHcc((Boolean) o[14]);
		}
		return pdto;
	}

	@Override
	public Product findProductPropertiesByIdProduct(Integer idP) {
		// TODO Auto-generated method stub
		List<Object[]> list = this.repo.findProductPropertiesByIdProduct(idP);

		if (list.isEmpty() || list == null)
			return null;
		else {
			Product product = new Product();

			Object[] o = list.get(0);
			product.setIdProduct((Integer) o[0]);
			product.setSapCode((String) o[1]);
			product.setNameProduct((String) o[2]);
			product.setDescProduct((String) o[3]);
			product.setTypeProduct(ProductType.valueOf((String) o[4]));
			product.setTypeProductTxt((String) o[15]);

			List<Property> listProperty = new ArrayList<>();
			list.forEach((Object[] x) -> {
				Property p = new Property();
				PropertyList pl = new PropertyList();

				pl.setIdProperty((String) x[5]);
				pl.setNameProperty((String) x[6]);
				// p.setIdProperty((String)x[5]);
				// p.setNameProperty((String)x[6]);
				p.setTypeProperty((String) x[7]);
				if(x[8] != null)
					pl.setPeriodicity(PropertyPeriodicity.valueOf((String) x[8]));
				// pl.setNormName((String)x[9]);
				// p.setPeriodicityProperty((String)x[8]);
				// p.setNormProperty((String)x[9]);
				p.setPropertyNorm((String) x[9]);
				p.setPropertyList(pl);
				p.setMinProperty((x[10]) == null ? null : ((BigDecimal) x[10]).doubleValue());
				p.setMaxProperty((x[11]) == null ? null : ((BigDecimal) x[11]).doubleValue());
				p.setUnitProperty((String) x[12]);
				p.setViewProperty((String) x[13]);
				p.setViewPropertyOnHcc((Boolean) x[14]);
				listProperty.add(p);
			});
			product.setProperties(listProperty);
			return product;
		}

	}

	@Override
	public Product listById(Integer id) {
		Optional<Product> productOptional = repo.findById(id);
		return productOptional.isPresent()? productOptional.get(): null;
	}

	@Override
	@Transactional
	public Product modify(Product product) {
		Optional<Product> productTmp = repo.findById(product.getIdProduct());
		if(productTmp.isPresent()) {
			Product productModify = productTmp.get();
			productModify.setNameProduct(product.getNameProduct());
			productModify.setGenericName(product.getGenericName());
			productModify.setDescProduct(product.getDescProduct());
			productModify.setItcdq(product.getItcdq());
			productModify.setSapCode(product.getSapCode());
			productModify.setSapGroup(product.getSapGroup());
			productModify.setTypeProduct(product.getTypeProduct());
			productModify.setTypeProductTxt(product.getTypeProductTxt());
			productModify.setOrigin(product.getOrigin());
			productModify.setManipulationStorage(product.getManipulationStorage());
			productModify.setReferenceNorm(product.getReferenceNorm());
			productModify.setDesignation(product.getDesignation());
			productModify.setBarcode(product.getBarcode());
			productModify.setPresentation(product.getPresentation());
			productModify.setSpecificUse(product.getSpecificUse());
			productModify.setGeneralIndication(product.getGeneralIndication());
			productModify.setIndustrialSafetyRecommendation(product.getIndustrialSafetyRecommendation());
			productModify.setInspectionSamplingTesting(product.getInspectionSamplingTesting());
			productModify.setArmorType(product.getArmorType());
			
			LOG.info(String.format("Producto Modificado %s", gsonLog.toJson(product)));
			return productModify;
		}
		
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public byte[] generateReport(Integer id) {
		Optional<Product> productOptional = repo.findById(id);
		if (productOptional.isPresent()) {
			try {
				String nombreReporte = productOptional.get().getTypeProduct().equals(ProductType.MATERIA_PRIMA) ? "ProductMP"
						: "ProductPT";
				ReportProductDTO dto = creaReporteProductDTO(productOptional.get());
				return reporteServicio.generarReporte(nombreReporte, Collections.singleton(dto), new HashMap<>());

			} catch (JasperReportsException e) {
				LOG.error(String.format("Error Product Reporte: %s", e.getMessage()));
				throw new ReporteExeption("Product");
			}
		}
		return null;
	}
	
	private ReportProductDTO creaReporteProductDTO(Product product) {

		List<ReportProductSpecificationDTO> properties = product.getProperties().stream().map(x -> {
			return new ReportProductSpecificationDTO(x.getPropertyList().getNameProperty(), x.getUnitProperty(),
					x.getPropertyNorm(), x.getPropertyList().getMachine(), x.getPropertyList().getMethod(),
					x.getPropertyList().getLaboratory(), x.getPropertyList().getSamplingPlan(), x.getMinProperty(),
					x.getMaxProperty(), x.getPropertyList().getTypeProperty(), x.getViewProperty(),
					x.getPropertyList().getPeriodicity());
		}).collect(Collectors.toList());

		ReportProductDTO dto = new ReportProductDTO(product.getNameProduct(), product.getGenericName(),
				product.getDescProduct(), product.getSapCode(), product.getItcdq(), product.getTypeProductTxt(),
				product.getOrigin() != null ? product.getOrigin().getDescripcion():null, product.getSpecificUse(), product.getPresentation(),
				product.getReview(), product.getManipulationStorage(), product.getGeneralIndication(), properties, product.getDetailInformationAditional(),
				product.getDesignation(), product.getReferenceNorm(), product.getBarcode(), product.getIndustrialSafetyRecommendation(), product.getInspectionSamplingTesting(),
				product.getArmorType());

		return dto;
	}

	@Override
	@Transactional
	public List<InformationAditional> createInformationAditional(Integer productId,
			InformationAditional infoAditional) {
		Optional<Product> productOptional = repo.findById(productId);

		if (productOptional.isPresent()) {

			LOG.info(String.format("Información Adicional %s registrado", infoAditional.getType(),
					gsonLog.toJson(infoAditional)));

			productOptional.get().addApprobationCriteria(infoAditional);

			return infoAditional.getType().equals(InformationAditionalType.CRITERIOS_APROBACION)
					? productOptional.get().getDetailCriteria()
					: productOptional.get().getDetailPaletizado();
		}
		return null;
	}

	@Override
	@Transactional
	public String updateReview(Integer productId) {
		Optional<Product> productOptional = repo.findById(productId);

		if (productOptional.isPresent()) {
			
			Product product = productOptional.get();
			String review = generarNextReview(product.getReview());
			product.setReview(review);
			
			LOG.info(String.format("Nueva Revisión Generada %s para el producto %s", review, product.getNameProduct()));
			return review;
		}

		return null;
	}
	
	private String generarNextReview(String review) {
		int reviewNumber = review == null ? 0:Integer.parseInt(review);
		reviewNumber++;
		return StringUtils.leftPad(String.valueOf(reviewNumber), 2, "0");
	}

}
