package de.paulsikorski.calcitetest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.calcite.DataContext;
import org.apache.calcite.adapter.file.CsvEnumerator;
import org.apache.calcite.adapter.file.CsvFieldType;
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
import org.apache.calcite.util.ImmutableIntList;
import org.apache.calcite.util.Source;
import org.checkerframework.checker.nullness.qual.Nullable;

public class CsvTable implements Table, ScannableTable {
	/** Creates a CsvScannableTable. */
	protected final Source source;
	private RelDataType rowType;
	private List<CsvFieldType> fieldTypes;
	
	public CsvTable(Source source) {
		this.source = source;
	}

	@Override
	public RelDataType getRowType(RelDataTypeFactory typeFactory) {
	   if (rowType == null) {
	      rowType = CsvEnumerator.deduceRowType((JavaTypeFactory) typeFactory, source,
	          null, false);
	   }
	   return rowType;
	}

	@Override
	public Enumerable<@Nullable Object[]> scan(DataContext root) {
		JavaTypeFactory typeFactory = root.getTypeFactory();
		final List<CsvFieldType> fieldTypes = getFieldTypes(typeFactory);
		final List<Integer> fields = ImmutableIntList.identity(fieldTypes.size());
		final AtomicBoolean cancelFlag = DataContext.Variable.CANCEL_FLAG.get(root);
		return new AbstractEnumerable<@Nullable Object[]>() {
			@Override public Enumerator<@Nullable Object[]> enumerator() {
				return new CsvEnumerator<>(source, cancelFlag, false, null,
						CsvEnumerator.arrayConverter(fieldTypes, fields, false));
			}
		};
	}
	
	public List<CsvFieldType> getFieldTypes(RelDataTypeFactory typeFactory) {
		if (fieldTypes == null) {
			fieldTypes = new ArrayList<>();
		    CsvEnumerator.deduceRowType((JavaTypeFactory) typeFactory, source, fieldTypes, false);
		}
		return fieldTypes;
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
	
	@Override public String toString() {
		return "CsvTable";
	}
}
