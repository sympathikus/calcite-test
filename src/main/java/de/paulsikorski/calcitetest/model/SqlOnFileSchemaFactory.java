package de.paulsikorski.calcitetest.model;

import java.io.File;
import java.util.Map;

import org.apache.calcite.model.ModelHandler;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SqlOnFileSchemaFactory implements SchemaFactory {

	private static final Logger LOG = LogManager.getLogger(SqlOnFileSchemaFactory.class);
	private static final SqlOnFileSchemaFactory INSTANCE = new SqlOnFileSchemaFactory();
	
	public SqlOnFileSchemaFactory() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Schema create(SchemaPlus parentSchema, String name, Map<String, Object> operand) {
		LOG.info("Constructing Schema {}", name);
		final File baseDirectory = (File) operand.get(ModelHandler.ExtraOperand.BASE_DIRECTORY);
		final String directory = (String) operand.get("directory");
	    File directoryFile = null;
	    if (directory != null) {
	      directoryFile = new File(directory);
	    }
	    if (baseDirectory != null) {
	      if (directoryFile == null) {
	        directoryFile = baseDirectory;
	      } else if (!directoryFile.isAbsolute()) {
	        directoryFile = new File(baseDirectory, directory);
	      }
	    }
	    final Map<String, Table> tableMap = new SchemaTableMapBuilder(directoryFile).get();
	    if(tableMap.isEmpty()) {
	    	LOG.error("Unable to build any tables. Please check calcite model file or configuration");
	    }
	    return new SqlOnFileSchema(parentSchema, name, tableMap);
	}

	
}
