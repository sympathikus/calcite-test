package de.paulsikorski.calcitetest.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.calcite.DataContext;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.linq4j.Linq4j;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.Schema.TableType;
import org.apache.calcite.schema.Statistic;
import org.apache.calcite.schema.Table;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.util.Pair;
import org.apache.calcite.util.Source;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;  
import org.apache.poi.xssf.usermodel.XSSFSheet;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  

public class XlsxTable implements Table, ScannableTable {
	
	private final Source source;
	private @Nullable RelDataType rowType;
	private @Nullable List<Object> dataList;
	
	public XlsxTable(Source source){
		super();
		this.source = source;
	};

	@Override
	public RelDataType getRowType(RelDataTypeFactory typeFactory) {
		if (rowType == null) {
			this.rowType = XlsxEnumerator.deduceRowType(typeFactory, source).getRelDataType();
		}
		return rowType;
	}
	
	@Override
	public Enumerable<@Nullable Object[]> scan(DataContext root) {
		return (Enumerable<@Nullable Object[]>) Linq4j.enumerator(getDataList(root.getTypeFactory()));
	}
	
	public List<Object> getDataList(RelDataTypeFactory typeFactory) {
	    if (dataList == null) {
	      dataList = JsonEnumerator.deduceRowType(typeFactory, source).getDataList();
	    }
	    return dataList;
	  }

	@Override
	public Statistic getStatistic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TableType getJdbcTableType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRolledUp(String column) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rolledUpColumnValidInsideAgg(String column, SqlCall call, @Nullable SqlNode parent,
			@Nullable CalciteConnectionConfig config) {
		// TODO Auto-generated method stub
		return false;
	}
}
