package de.paulsikorski.calcitetest.model;

import java.util.List;

import org.apache.calcite.DataContext;
import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.Schema.TableType;
import org.apache.calcite.schema.Statistic;
import org.apache.calcite.schema.Statistics;
import org.apache.calcite.schema.Table;
import org.apache.calcite.sql.SqlCall;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.util.Source;
import org.checkerframework.checker.nullness.qual.Nullable;

public class JsonTable implements Table, ScannableTable{

	private final Source source;
	private @Nullable RelDataType rowType;
	protected @Nullable List<Object> dataList;

	
	public JsonTable(Source source) {
		super();
		this.source = source;
	}

	@Override
	public RelDataType getRowType(RelDataTypeFactory typeFactory) {
		if(this.rowType == null) {
			this.rowType = JsonEnumerator.deduceRowType(typeFactory, source).getRelDataType();
		}
		return rowType;
	}

	@Override
	public Enumerable<@Nullable Object[]> scan(DataContext root) {
		return new AbstractEnumerable<@Nullable Object[]>() {
		@Override public Enumerator<@Nullable Object[]> enumerator() {
			JavaTypeFactory typeFactory = root.getTypeFactory();
			return new JsonEnumerator(getDataList(typeFactory));
		}
		};
	}
	
	public List<Object> getDataList(RelDataTypeFactory typeFactory) {
	    if (dataList == null) {
	      dataList = JsonEnumerator.deduceRowType(typeFactory, source).getDataList();
	    }
	    return dataList;
	  }
	
	@Override
	public Statistic getStatistic() {
		return Statistics.UNKNOWN;
	}

	@Override
	public TableType getJdbcTableType() {
		return TableType.TABLE;
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
