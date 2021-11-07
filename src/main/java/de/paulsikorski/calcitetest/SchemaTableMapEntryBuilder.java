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
		return Optional.ofNullable(fileSource)
		.map(fS -> fS.trim(".gz"))
		.map(trimmedFileSource -> trimmedFileSource.relative(directory))
		.map(relativeSource -> relativeSource.path())
		.map(tableName -> {
			final Table table;
			if(tableName.endsWith(".json")) {
				//TODO maybe do the file processing here so that it doesn't have to be executed multiple times
				// don't know if this is an actual issue 
				table = new JsonTable(fileSource);
				LOG.info("Build table {} from json file", tableName);
			} else if(tableName.endsWith(".csv")) {
				table = new CsvTable(fileSource);
				LOG.info("Build table {} from csv file", tableName);
			} else {
				LOG.info("No suitable adapter for file {} found", tableName);
				return null;
			}
			return Maps.immutableEntry(tableName, table);
		});
	}
	
}
