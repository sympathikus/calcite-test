package de.paulsikorski.calcitetest;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;

import org.apache.calcite.schema.Table;
import org.apache.calcite.util.Source;
import org.apache.calcite.util.Sources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;

public class SchemaTableMapEntryBuilder implements Function<File, Optional<Entry<String, Table>>>{

	private static final Logger LOG = LogManager.getLogger(SchemaTableMapEntryBuilder.class);
	private final Source directory;
	
	public SchemaTableMapEntryBuilder(Source directory) {
		super();
		this.directory = directory;
	}


	/**
	 * Transforms a single file to a {@link Table} object and returns an Optional
	 * containing this table in an {@link Map.Entry}. If there is no implementation for this file suffix,
	 * an empty {@link Optional} is returned.
	 */
	@Override
	public Optional<Entry<String, Table>> apply(File file) {
		LOG.debug("Attempting to build table for file {}", file);
		final Source fileSource = Sources.of(file);
		final String tableName = fileSource.trim(".gz").relative(directory).path();
		if(tableName.endsWith(".json")) {
			final Table table = new JsonTable(fileSource);
			LOG.info("Build table {} from json file", tableName);
			return Optional.of(Maps.immutableEntry(tableName, table));
		} else if(tableName.endsWith(".csv")) {
			LOG.info("Csv adapter not implemented yet");
		} 
		LOG.info("No suitable adapter for file {} found", tableName);
		return Optional.empty();
	}

}
