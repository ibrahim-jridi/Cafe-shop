package serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import constants.cafeConstant;
import dao.CategoryDao;
import jwt.jwtFilter;
import lombok.extern.slf4j.Slf4j;
import model.Category;
import service.CategoryService;
import utils.cafeUtils;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	CategoryDao categoryDao;
	@Autowired
	jwtFilter jwtFilter;
	
	@Override
	public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			if (jwtFilter.isAdmin()) {
				if (validateCategoryMap(requestMap,false)) {
					categoryDao.save(getCategoryFromMap(requestMap,false));
					return cafeUtils.getResponseEntity("category successfulle added", HttpStatus.OK);
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

	private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
		// TODO Auto-generated method stub
		if (requestMap.containsKey("name")) {
			if (requestMap.containsKey("id") && validateId) {
				return true;
			}else if (!validateId) {
				return true;
			}
		}
		return false;
	}
	
	private Category getCategoryFromMap(Map<String,String> requestMap,Boolean Add) {
		Category category = new Category();
		if (Add) {
			category.setId(Integer.parseInt(requestMap.get("id")));
		}
		category.setName(requestMap.get("name"));
		return category;
	}

	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		// TODO Auto-generated method stub
		try {
			if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
				
				logger.info("Inside Inside");
				return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(),HttpStatus.OK);
			}
			return new ResponseEntity<>(categoryDao.findAll(),HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		// TODO Auto-generated method stub
		try {
			if (jwtFilter.isAdmin()) {
				if (validateCategoryMap(requestMap, true)) {
					Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
					if (!optional.isEmpty()) {
						categoryDao.save(getCategoryFromMap(requestMap, true));
						return cafeUtils.getResponseEntity("Category Successfully Updated", HttpStatus.OK);
					} else {
						return cafeUtils.getResponseEntity(" the category with this Id doesn't exist", HttpStatus.OK);
					}
				}
				return cafeUtils.getResponseEntity(cafeConstant.DII,HttpStatus.BAD_REQUEST);
			} else {
				return cafeUtils.getResponseEntity(cafeConstant.UA, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
