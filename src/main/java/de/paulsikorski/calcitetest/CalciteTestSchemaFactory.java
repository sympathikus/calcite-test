package de.paulsikorski.calcitetest;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.calcite.model.ModelHandler;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaFactory;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.schema.Table;
import org.apache.calcite.util.Source;
import org.apache.calcite.util.Sources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;

public class CalciteTestSchemaFactory implements SchemaFactory {

	private static final Logger LOG = LogManager.getLogger(CalciteTestSchemaFactory.class);
	private static final CalciteTestSchemaFactory INSTANCE = new CalciteTestSchemaFactory();
	
	public CalciteTestSchemaFactory() {
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
	    return new CalciteTestSchema(parentSchema, name, new SchemaTableMapBuilder(directoryFile).get());
	}

	
}
