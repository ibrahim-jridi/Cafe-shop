package serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import constants.cafeConstant;
import dao.BillDao;
import jwt.jwtFilter;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import model.Bill;
import service.BillService;
import utils.cafeUtils;

@Slf4j
@Service
public class BillServiceImpl implements BillService {
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	jwtFilter jwtFilter;
	@Autowired
	BillDao billDao;
	@Override
	public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
		// TODO Auto-generated method stub
		logger.info("inside generateReport");

		try {
			String fileName;
			if (validateRequestMap(requestMap)) {
				if (requestMap.containsKey("isGenerate")&&!(Boolean)requestMap.get("isGenerate")) {
					fileName = (String) requestMap.get("uuid");
				} else {
					fileName =cafeUtils.getUUID();
					requestMap.put("uuid", fileName);
					billInsert(requestMap);
				}
				String data="Name: "+requestMap.get("name")+"\n"+"Contact Number: "+requestMap.get("contactNumber")+"\n"
				+"Email: "+requestMap.get("email")+"\n"+"Payment Method: "+requestMap.get("paymentMethod");
				Document document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(cafeConstant.S_LOCATION+"\\"+fileName+".pdf"));
				document.open();
				RectanglePdf(document);
				
				Paragraph part= new Paragraph("Cafe Shop",getFont("Header"));
				part.setAlignment(Element.ALIGN_CENTER);
				document.add(part);
				
				Paragraph paragraph = new Paragraph(data+"\n \n",getFont("Data"));
				document.add(paragraph);
				
				PdfPTable table = new PdfPTable(5);
				table.setWidthPercentage(100);
				addTableHeader(table);
				
				JSONArray jsonArray = cafeUtils.getJsonFromString((String)requestMap.get("productDetails"));
				for (int i = 0; i < jsonArray.length(); i++) {
					addRow(table,cafeUtils.getMapFromJson(jsonArray.getString(i)));
				}
				document.add(table);
				
				Paragraph footer = new Paragraph("Total: "+requestMap.get("totalAmount")+"\n"+"Thanks,visit us again",getFont("Data"));
				document.add(footer);
				document.close();
				return new ResponseEntity<>("{\"uuid\":\""+fileName+"\"}",HttpStatus.OK);
			}
			return cafeUtils.getResponseEntity("Data Not Found", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void addRow(PdfPTable table, Map<String, Object> data) {
		// TODO Auto-generated method stub
		logger.info("inside addRow");
		table.addCell((String)data.get("name"));
		table.addCell((String)data.get("category"));
		table.addCell((String)data.get("quantity"));

		table.addCell(Double.toString((Double) data.get("price")));

		table.addCell(Double.toString((Double) data.get("total")));
		
	}

	private void addTableHeader(PdfPTable table) {
		// TODO Auto-generated method stub
		logger.info("inside addTableHeader");
		Stream.of("Name","Category","Quantity","Price","Total").forEach(columnTitle->{
			PdfPCell header=new PdfPCell();
			header.setBackgroundColor(BaseColor.DARK_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(columnTitle));
			header.setBackgroundColor(BaseColor.CYAN);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setVerticalAlignment(Element.ALIGN_CENTER);
			table.addCell(header);
		});
	}

	private Font getFont(String type) {
		// TODO Auto-generated method stub
		logger.info("inside getFont");
		switch (type) {
		case "Header":
			Font headerFont= FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,20,BaseColor.BLACK);
			headerFont.setStyle(Font.BOLD);
			return headerFont;
		case "Data":
			Font dataFont= FontFactory.getFont(FontFactory.TIMES,12,BaseColor.BLACK);
			dataFont.setStyle(Font.BOLD);
			return dataFont;
			
			

		default:
			return new Font();
			
		}

		
	}

	private void RectanglePdf(Document document) throws DocumentException {
		// TODO Auto-generated method stub
		logger.info("inside rectanglePdf");
		Rectangle rect= new Rectangle(577,825,18,15);
		rect.enableBorderSide(1);
		rect.enableBorderSide(2);
		rect.enableBorderSide(4);
		rect.enableBorderSide(8);
		rect.setBorderColor(BaseColor.DARK_GRAY);
		rect.setBorderWidth(1);
		document.add(rect);

		
	}

	private void billInsert(Map<String, Object> requestMap) {
		// TODO Auto-generated method stub
		try {
			Bill bill = new Bill();
			bill.setUuid((String)requestMap.get("uuid"));
			bill.setName((String)requestMap.get("name"));
			bill.setEmail((String)requestMap.get("email"));
			bill.setContactNumber((String)requestMap.get("contactNumber"));
			bill.setPaymentMethod((String)requestMap.get("paymentMethod"));
			bill.setTotal(Integer.parseInt((String) requestMap.get("totalAmount")));
			bill.setProductDetail((String)requestMap.get("productDetails"));
			bill.setCreatedBy(jwtFilter.getCurrentUser());
			billDao.save(bill);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	private boolean validateRequestMap(Map<String, Object> requestMap) {
		// TODO Auto-generated method stub
		return requestMap.containsKey("name")&&
				requestMap.containsKey("contactNumber")&&
				requestMap.containsKey("email")&&
				requestMap.containsKey("paymentMethod")&&
				requestMap.containsKey("productDetails")&&
				requestMap.containsKey("totalAmount");
	}

	@Override
	public ResponseEntity<List<Bill>> getBill() {
		// TODO Auto-generated method stub
		List<Bill> list=new ArrayList<>();
		if (jwtFilter.isAdmin()) {
			list=billDao.getAllBills();
		} else {
			list = billDao.getBillByUserName(jwtFilter.getCurrentUser());
		}
		return new ResponseEntity<>(list,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
		// TODO Auto-generated method stub
		logger.info("inside getPdf ; requestMap{}",requestMap);
		try {
			byte[]byteArray = new byte[0];
			if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap)) {
				return new ResponseEntity<>(byteArray,HttpStatus.BAD_REQUEST);
			}
			String filePath = cafeConstant.S_LOCATION + "\\" +(String) requestMap.get("uuid") + ".pdf";
			if (cafeUtils.isFileExist(filePath)) {
				byteArray = getByteArray(filePath);
				return new ResponseEntity<>(byteArray,HttpStatus.OK);
			}else {
				requestMap.put("isGenerate",false);
				generateReport(requestMap);
				byteArray= getByteArray(filePath);
				return new ResponseEntity<>(byteArray,HttpStatus.OK);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	private byte[] getByteArray(String filePath) throws Exception {
		// TODO Auto-generated method stub
		File initialFile = new File(filePath);
		InputStream targetStream = new FileInputStream(initialFile);
		byte[]byteArray = org.apache.pdfbox.io.IOUtils.toByteArray(targetStream);
		targetStream.close();
		return byteArray;
		
	}

	@Override
	public ResponseEntity<String> deleteBill(Integer id) {
		// TODO Auto-generated method stub
		try {
			Optional optional = billDao.findById(id);
			if (!optional.isEmpty()) {
				billDao.deleteById(id);
				return cafeUtils.getResponseEntity("Bill successfully deleted", HttpStatus.OK); 
			}
			return cafeUtils.getResponseEntity("Bill does not exist", HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cafeUtils.getResponseEntity(cafeConstant.SIW, HttpStatus.INTERNAL_SERVER_ERROR) ;
	}

}
