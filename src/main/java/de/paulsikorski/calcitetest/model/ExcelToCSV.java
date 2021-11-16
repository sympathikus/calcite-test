package de.paulsikorski.calcitetest.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.linq4j.Linq4j;
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
	
	/*
	public StringBuffer getRows(XSSFSheet sheet) {
		StringBuffer rowBuffer = new StringBuffer();
        Iterator<Row> rowIterator = sheet.iterator();
        
        // skips header
        if (rowIterator.hasNext()) {
        	rowIterator.next();
        }
        
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            
            // skips index cell
            if (cellIterator.hasNext()) {
            	cellIterator.next();
            }
            
            boolean is_empty = true;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if(!cellIterator.hasNext()) {
                	switch (cell.getCellType()) {
                    case STRING:
                        rowBuffer.append(cell.getStringCellValue());
                        LOG.info("Appending String {}", cell.getStringCellValue());
                        is_empty = false;
                        break;
                    case NUMERIC:
                    	rowBuffer.append(cell.getNumericCellValue());
                        LOG.info("Appending Numeric {}", cell.getNumericCellValue());
                        is_empty = false;
                        break;
                    case BOOLEAN:
                    	rowBuffer.append(cell.getBooleanCellValue());
                    	is_empty = false;
                        break;
                    case BLANK:
                    	break;
                    default:
                    	rowBuffer.append(cell);
                    	LOG.info("Adding empty cells {}", cell);
                    	break;
                    }
                } else {
                	switch (cell.getCellType()) {
                    case STRING:
                    	rowBuffer.append(cell.getStringCellValue() + ",");
                        LOG.info("Appending String {}", cell.getStringCellValue());
                        is_empty = false;
                        break;
                    case NUMERIC:
                    	rowBuffer.append(cell.getNumericCellValue() + ",");
                        LOG.info("Appending Numeric {}", cell.getNumericCellValue());
                        is_empty = false;
                        break;
                    case BOOLEAN:
                    	rowBuffer.append(cell.getBooleanCellValue() + ",");
                    	is_empty = false;
                        break;
                    case BLANK:
                    	break;
                    default:
                    	rowBuffer.append(cell + ",");
                    	break;
                    }
                }
            }
            
            // start new line if next line is present and current line is not empty
            if(rowIterator.hasNext() && !is_empty) {
            	rowBuffer.append("\n");
            }
    	}
    	return rowBuffer;
	}
	*/
	/*
	public StringBuffer getHeader(XSSFSheet sheet) {
		
		StringBuffer rowBuffer = new StringBuffer();
        Iterator<Row> rowIterator = sheet.iterator();
        
        if(rowIterator.hasNext()) {
        	
        	Row row = rowIterator.next();
        	Iterator<Cell> cellIterator = row.cellIterator();
        	
        	// skips index cell
        	if (cellIterator.hasNext()) {
            	cellIterator.next();
            }
        	
        	while(cellIterator.hasNext()) {
        		Cell cell = cellIterator.next();
        		if(!cellIterator.hasNext()) {
        			switch (cell.getCellType()) {
                    case STRING:
                        rowBuffer.append(cell.getStringCellValue() + ":string");
                        LOG.info("Appending String {}", cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        rowBuffer.append(cell.getNumericCellValue() + ":integer");
                        LOG.info("Appending Numeric {}", cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        rowBuffer.append(cell.getBooleanCellValue() + ":boolean");
                        break;
                    case BLANK:
                    	break;
                    default:
                    	rowBuffer.append(cell + ":unknown");
                    	break;
                    }
        		} else {
        			switch (cell.getCellType()) {
                    case STRING:
                        rowBuffer.append(cell.getStringCellValue() + ":string,");
                        LOG.info("Appending String {}", cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        rowBuffer.append(cell.getNumericCellValue() + ":integer,");
                        LOG.info("Appending Numeric {}", cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        rowBuffer.append(cell.getBooleanCellValue() + ":boolean,");
                        break;
                    case BLANK:
                    	break;
                    default:
                    	rowBuffer.append(cell + ":unknown,");
                    	break;
                    }
        		}
        	}
        }
        // start new line if next line is present
        if(rowIterator.hasNext()) {
        	rowBuffer.append("\n");
        }
        return rowBuffer;
	}
	*/
	
	
	public void excelToCSV(File file, int sheetIdx, String path) throws Exception {
		 
        // FileInputStream fileInStream = new FileInputStream(file);
		LOG.info("File path is {}", file.getAbsolutePath());
		
        XSSFWorkbook workBook = new XSSFWorkbook(new FileInputStream(file));
        XSSFSheet selSheet = workBook.getSheetAt(sheetIdx);
        FileOutputStream fos = new FileOutputStream(path);
        StringBuffer sb = new StringBuffer();
        
        /*
        sb.append(getHeader(selSheet));
        sb.append(getRows(selSheet));
		*/
        
        sb.append(processSheet(selSheet));
        
        LOG.info("String Buffer assumes the form {}", sb);
        fos.write(sb.toString().getBytes());
        //fos.flush();
        fos.close();
        workBook.close();
	}
	
	public List<String> getHeaderTypes(XSSFSheet sheet) {
		List <String> list = new ArrayList<>();		
		Row row = sheet.getRow(1);
		Iterator<Cell> cellIterator = row.cellIterator();
		
		while(cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			list.add(cell.getCellType().toString());
		}
		
		return list;
	}
	
	
	public StringBuffer processSheet(XSSFSheet sheet) {
		StringBuffer rowBuffer = new StringBuffer();
		Iterator<Row> rowIterator = sheet.iterator();
		boolean header = true;
		boolean endOfLine;
		List <String> headerTypes = getHeaderTypes(sheet);
		
		
        
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            
            // skips index cell
            if (cellIterator.hasNext()) {
            	cellIterator.next();
            }
            
            int index = 0;
            boolean is_empty = true;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                endOfLine = cellIterator.hasNext() ? false : true;
                
                String suffix = header ? ":" + headerTypes.get(index) : "";
        		suffix = endOfLine ? suffix + "" : suffix + ",";
            	
            	switch (cell.getCellType()) {
                case STRING:
                    rowBuffer.append(cell.getStringCellValue() + suffix);
                    is_empty = false;
                    break;
                case NUMERIC:
                	rowBuffer.append(cell.getNumericCellValue() + suffix);
                    is_empty = false;
                    break;
                case BOOLEAN:
                	rowBuffer.append(cell.getBooleanCellValue() + suffix);
                	is_empty = false;
                    break;
                case BLANK:
                	break;
                default:
                	rowBuffer.append(cell);
                	LOG.info("Adding empty cells {}", cell);
                	break;
            	}
            	index++;
            }
            
            // start new line if next line is present and current line is not empty
            if(rowIterator.hasNext() && !is_empty) {
            	rowBuffer.append("\n");
            }
            header = false;
    	}
    	return rowBuffer;
	}
}
