package controllerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import constants.cafeConstant;
import controller.ProductController;
import service.ProductService;
import utils.cafeUtils;
import wrapper.ProductWrapper;

@RestController
public class ProductControllerImpl implements ProductController {
	
	@Autowired
	ProductService productService;
	
	
	@Override
	public ResponseEntity<String> addProduct(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			return productService.addProduct(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@Override
	public ResponseEntity<List<ProductWrapper>> getProducts() {
		// TODO Auto-generated method stub
		try {
			return productService.getProducts();
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
			return productService.updateProduct(requestMap);
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
			return productService.deleteProduct(id);
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
			return productService.updateStatus(requestMap);
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
			return productService.getByCategory(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}


	@Override
	public ResponseEntity<ProductWrapper> getById(Integer id) {
		// TODO Auto-generated method stub
		try {
			return productService.getProductById(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		
		}
		
		return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
