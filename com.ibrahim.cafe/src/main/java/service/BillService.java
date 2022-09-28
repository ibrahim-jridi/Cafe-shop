package service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import model.Bill;

public interface BillService {
	ResponseEntity<String> generateReport(Map<String, Object>requestMap);
	
	ResponseEntity<List<Bill>> getBill();
	
	ResponseEntity<byte[]> getPdf(Map<String,Object>requestMap);
	
	ResponseEntity<String> deleteBill(Integer id);

}
