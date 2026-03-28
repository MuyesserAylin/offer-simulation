package com.simulation.offer_system.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.simulation.offer_system.dto.ProductDTO;
import com.simulation.offer_system.dto.QuotationItemDTO;
import com.simulation.offer_system.entity.Product;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {
	
	public List<QuotationItemDTO> readExcel(MultipartFile file) {
        List<QuotationItemDTO> list = new ArrayList<>();
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            
            if (rows.hasNext()) rows.next(); // Başlığı atla (ID, Name vs.)

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                QuotationItemDTO dto = new QuotationItemDTO();

                // 0 -> Product ID
                dto.setProductId((long) currentRow.getCell(0).getNumericCellValue());
                
                // 1 -> Product Name
                dto.setProductName(currentRow.getCell(1).getStringCellValue());
                
                // 3 -> New Price (D Sütunu) - BURAYI 3 YAPTIK ÇÜNKÜ FİYATLAR BURADA!
                dto.setOfferedPrice(currentRow.getCell(3).getNumericCellValue());

                list.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("Excel okuma hatası: " + e.getMessage());
        }
        return list;
    }
    
	public ByteArrayInputStream exportProductsToExcel(List<Product> products) {
	    try (Workbook workbook = new XSSFWorkbook(); 
	         ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	        
	        Sheet sheet = workbook.createSheet("Products");

	        // 1. BAŞLIK SATIRINI OLUŞTURUYORUZ
	        Row headerRow = sheet.createRow(0);
	        headerRow.createCell(0).setCellValue("Product ID");
	        headerRow.createCell(1).setCellValue("Product Name");
	        headerRow.createCell(2).setCellValue("Current Price");
	        headerRow.createCell(3).setCellValue("New Price");

	        // 2. ÜRÜNLERİ SATIR SATIR EXCEL'E YAZIYORUZ
	        int rowIdx = 1;
	        for (com.simulation.offer_system.entity.Product product : products) {
	            Row row = sheet.createRow(rowIdx++);
	            
	            
	            row.createCell(0).setCellValue(product.getId());
	            
	            
	            row.createCell(1).setCellValue(product.getName());
	            
	            
	            row.createCell(2).setCellValue(product.getLastQuotedPrice() != null ? product.getLastQuotedPrice() : 0.0);
	            
	            
	            row.createCell(3).setCellValue(""); 
	        }

	        // Sütun genişliklerini otomatik ayarla (Şık dursun kanka)
	        for (int i = 0; i < 4; i++) {
	            sheet.autoSizeColumn(i);
	        }

	        workbook.write(out);
	        return new ByteArrayInputStream(out.toByteArray());
	        
	    } catch (Exception e) {
	        throw new RuntimeException("Excel oluşturma hatası: " + e.getMessage());
	    }
	}
}


