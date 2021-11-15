package de.paulsikorski.calcitetest.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelToCSV {
	
	private static final Logger LOG = LogManager.getLogger(ExcelToCSV.class);
	
	public ExcelToCSV() {
		
	}
	
	public void excelToCSV(File file, int sheetIdx, String path) throws Exception {
		 
        // FileInputStream fileInStream = new FileInputStream(file);
		LOG.info("File path is {}", file.getAbsolutePath());
		
        XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream(file));
        XSSFSheet selSheet = workBook.getSheetAt(sheetIdx);
        FileOutputStream fos = new FileOutputStream(path);
 
        Iterator<Row> rowIterator = selSheet.iterator();
        StringBuffer sb = new StringBuffer();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            if (cellIterator.hasNext()) {
            	cellIterator.next();
            }
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                case STRING:
                    sb.append(cell.getStringCellValue() + ",");
                    LOG.info("Appending String {}", cell.getStringCellValue());
                    break;
                case NUMERIC:
                    sb.append(cell.getNumericCellValue() + ",");
                    LOG.info("Appending Numeric {}", cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    sb.append(cell.getBooleanCellValue() + ",");
                    break;
                default:
                	sb.append(cell + ",");
                	break;
                }
            }
            sb.append("\n");
        }
        fos.write(sb.toString().getBytes());
        fos.flush();
        fos.close();
        workBook.close();
	}
}
