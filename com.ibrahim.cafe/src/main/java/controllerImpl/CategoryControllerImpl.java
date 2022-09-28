package controllerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import constants.cafeConstant;
import controller.CategoryController;
import model.Category;
import service.CategoryService;
import utils.cafeUtils;

@RestController
public class CategoryControllerImpl implements CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@Override
	public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			return categoryService.addCategory(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW,HttpStatus.INTERNAL_SERVER_ERROR );
	}

	@Override
	public ResponseEntity<List<Category>> getAllCategories(String filterValue) {
		// TODO Auto-generated method stub
		try {
			return categoryService.getAllCategory(filterValue);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			return categoryService.updateCategory(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW,HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
