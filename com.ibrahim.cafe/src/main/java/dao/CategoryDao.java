package dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Category;

public interface CategoryDao extends JpaRepository<Category, Integer> {
	List<Category> getAllCategory();
}
