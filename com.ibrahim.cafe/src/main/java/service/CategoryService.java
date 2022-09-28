package service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import model.Category;

public interface CategoryService {
	ResponseEntity<String> addCategory(Map<String,String> requestMap);
	ResponseEntity<List<Category>> getAllCategory(String filterValue);
	ResponseEntity<String> updateCategory(Map<String,String> requestMap);


}
