package serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import constants.cafeConstant;
import dao.ProductDao;
import model.Category;
import model.Product;
import service.ProductService;
import utils.cafeUtils;
import wrapper.ProductWrapper;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductDao productDao;
	
	@Autowired
	jwt.jwtFilter jwtFilter;
	@Override
	public ResponseEntity<String> addProduct(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			if (jwtFilter.isAdmin()) {
				if (validateProductMap(requestMap,false)) {
					productDao.save(getProductFromMap(requestMap,false));
					return cafeUtils.getResponseEntity("Product successfully added", HttpStatus.OK);
				}
				return cafeUtils.getResponseEntity(cafeConstant.DII, HttpStatus.BAD_REQUEST);
			}else
				return cafeUtils.getResponseEntity(cafeConstant.UA, HttpStatus.UNAUTHORIZED);

			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private Product getProductFromMap(Map<String, String> requestMap, boolean add) {
		// TODO Auto-generated method stub
		Category category = new Category();
		category.setId(Integer.parseInt(requestMap.get("categoryId")));
		Product product= new Product();
		if (add) {
			product.setId(Integer.parseInt(requestMap.get("id")));
		} else {
			product.setStatus("true");

		}
		product.setCategory(category);
		product.setName(requestMap.get("name"));
		product.setDescription(requestMap.get("description"));
		product.setPrice(Integer.parseInt(requestMap.get("price")));
		return product;
		
	}
	private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
		// TODO Auto-generated method stub
		if (requestMap.containsKey("name")) {
				if (requestMap.containsKey("id") && validateId) {
					return true;
				}
		} else if(!validateId) {
			return true;
		}
		return false;
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getProducts() {
		// TODO Auto-generated method stub
		try {
			return new ResponseEntity<>(productDao.getProducts(),HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			if (jwtFilter.isAdmin()) {
				if (validateProductMap(requestMap, true)) {
					Optional<Product>optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
					
					if (!optional.isEmpty()) {
						Product product = getProductFromMap(requestMap, true);
						product.setStatus(optional.get().getStatus());
						productDao.save(product);
						return cafeUtils.getResponseEntity("Product successfully updated", HttpStatus.OK);

					} else {
						return cafeUtils.getResponseEntity("Product id does not exist", HttpStatus.OK);

					}
				} else {
					return cafeUtils.getResponseEntity(cafeConstant.DII, HttpStatus.BAD_REQUEST);
				}
			} else {
				return cafeUtils.getResponseEntity(cafeConstant.UA, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> deleteProduct(Integer id) {
		// TODO Auto-generated method stub
		try {
			if (jwtFilter.isAdmin()) {
				Optional optional = productDao.findById(id);
				if (!optional.isEmpty()) {
					productDao.deleteById(id);
					return cafeUtils.getResponseEntity("Product successfully deleted", HttpStatus.OK);
				}
			} else {
				return cafeUtils.getResponseEntity(cafeConstant.UA, HttpStatus.UNAUTHORIZED);

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			if (jwtFilter.isAdmin()) {
				
					Optional<Product>optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
					
					if (!optional.isEmpty()) {
						productDao.updateProductStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
						return cafeUtils.getResponseEntity("Product status successfully updated", HttpStatus.OK);

					} else {
						return cafeUtils.getResponseEntity("Product id does not exist", HttpStatus.OK);

					}
				
			} else {
				return cafeUtils.getResponseEntity(cafeConstant.UA, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
		// TODO Auto-generated method stub
		try {
			return new ResponseEntity<>(productDao.getProductByCategory(id),HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<ProductWrapper> getProductById(Integer id) {
		// TODO Auto-generated method stub
		try {
			return new ResponseEntity<>(productDao.getProductById(id),HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
