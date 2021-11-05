package de.paulsikorski.calcitetest;

import java.util.Map;

import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;

public class CalciteTestSchema extends AbstractSchema {

	private final Map<String, Table> tables;
	
	public CalciteTestSchema(SchemaPlus parentSchema, String name, Map<String, Table> tables) {
		this.tables = tables;
	}

	 @Override
	protected Map<String, Table> getTableMap() {
		return this.tables;
	}
	 
}
