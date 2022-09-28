package controllerImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import constants.cafeConstant;
import controller.billController;
import model.Bill;
import service.BillService;
import utils.cafeUtils;

@RestController
public class billControllerImpl implements billController {

	@Autowired
	BillService billService;
	
	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
		// TODO Auto-generated method stub
		try {
			return billService.generateReport(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<Bill>> getBill() {
		// TODO Auto-generated method stub
		try {
			return billService.getBill();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
		// TODO Auto-generated method stub
		try {
			return billService.getPdf(requestMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<String> deleteBill(Integer id) {
		// TODO Auto-generated method stub
		try {
			return billService.deleteBill(id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW,HttpStatus.HTTP_VERSION_NOT_SUPPORTED.INTERNAL_SERVER_ERROR);
	}

}
