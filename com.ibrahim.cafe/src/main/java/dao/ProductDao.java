package dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import model.Product;
import wrapper.ProductWrapper;

public interface ProductDao extends JpaRepository<Product,Integer> {

	List<ProductWrapper> getProducts();
	@Modifying
	@Transactional
	Integer updateProductStatus(@Param("status")String status,@Param("id")Integer id);
	
	List<ProductWrapper>getProductByCategory(@Param("id")Integer id);
	 ProductWrapper getProductById(@Param("id")Integer id);

}
