package controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping
public interface DashboardController {
	
	@GetMapping(path="/details")
	ResponseEntity<Map<String,Object>> getCount();

}
