package de.paulsikorski.calcitetest.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.util.Pair;
import org.apache.calcite.util.Source;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxEnumerator implements Enumerator <@Nullable Object[]>{
	

	private @Nullable RelDataType rowType;
	public final Enumerator <@Nullable Object[]> enumerator;
	
	public XlsxEnumerator (List<? extends @Nullable Object[]> list) {
		enumerator = Linq4j.enumerator(list);
	}
	
	public static XlsxDataConverter deduceRowType(RelDataTypeFactory typeFactory, Source source) {
		
		FileInputStream fis = new FileInputStream(source.file());
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> sheetIterator = sheet.iterator();
		Iterator<Cell> cellIterator = sheetIterator.next().cellIterator();
		List<RelDataType> types = new ArrayList<RelDataType>();
		List<String> names = new ArrayList<String>();
		DataFormatter dataFormatter = new DataFormatter();
		List<Object> list = new ArrayList<Object>();
		
		while(cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			types.add(typeFactory.createJavaType(cell.getClass()));
			switch(cell.getCellType()) {
			case STRING:
				names.add(cell.getStringCellValue());
				list.add(cell.getStringCellValue());
				break;
			case BOOLEAN:
				names.add(dataFormatter.formatCellValue(cell));
				list.add(cell.getBooleanCellValue());
				break;
			case NUMERIC:
				names.add(dataFormatter.formatCellValue(cell));
				list.add(cell.getNumericCellValue());
				break;
			default:
			}
		}
		while(sheetIterator.hasNext()) {
			Iterator<Cell> cellIt = sheetIterator.next().cellIterator();
			while(cellIt.hasNext()){
				Cell cell = cellIt.next();
				switch(cell.getCellType()) {
				case STRING:
					list.add(cell.getStringCellValue());
					break;
				case BOOLEAN:
					list.add(cell.getBooleanCellValue());
					break;
				case NUMERIC:
					list.add(cell.getNumericCellValue());
					break;
				default:
				}
			}
		}
		
		workbook.close();
		RelDataType relDataType = typeFactory.createStructType(Pair.zip(names, types));
		return new XlsxDataConverter(relDataType, list);
		
	}
	
	static class XlsxDataConverter {
		private final RelDataType relDataType;
		private final List<Object> dataList;

		 private XlsxDataConverter(RelDataType relDataType, List<Object> dataList) {
			 this.relDataType = relDataType;
			 this.dataList = dataList;
		 }

		 RelDataType getRelDataType() {
			 return relDataType;
		 }

		 List<Object> getDataList() {
			 return dataList;
		 }
	}

	@Override
	public @Nullable Object[] current() {
		return enumerator.current();
	}

	@Override
	public boolean moveNext() {
		return enumerator.moveNext();
	}

	@Override
	public void reset() {
		enumerator.reset();
	}

	@Override
	public void close() {
		enumerator.close();
	}

}
