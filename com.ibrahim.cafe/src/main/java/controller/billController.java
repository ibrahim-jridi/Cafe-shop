package controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import model.Bill;

@RequestMapping
public interface billController {

	@PostMapping(path="/generateReport")
	ResponseEntity<String>generateReport(@RequestBody Map<String, Object> requestMap);
	
	@GetMapping(path="/getBill")
	ResponseEntity<List<Bill>> getBill();
	
	@PostMapping(path="/getPdf")
	ResponseEntity<byte[]> getPdf(@RequestBody Map<String,Object> requestMap );
	
	@PostMapping(path="/delete/{id}")
	ResponseEntity<String> deleteBill(@PathVariable Integer id);
	
}
