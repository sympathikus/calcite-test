package de.paulsikorski.calcitetest.model;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.calcite.schema.Table;
import org.apache.calcite.util.Sources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;

/** 
 * Supplies a mapping from table name to actual {@link Table} object based on files in the given directory.
 * @author Paul Sikorski
 *
 */
public class SchemaTableMapBuilder implements Supplier<Map<String, Table>>{

	private static final Logger LOG = LogManager.getLogger(SchemaTableMapBuilder.class);
	private final File directory;
	
	public SchemaTableMapBuilder(File baseDirectory) {
		super();
		this.directory = baseDirectory;
	}

	/**
	 * Scans the directory for files and transforms them into tables, which are put into the resulting Map.
	 * If no files are found in the directory, or the directory is null, an empty map is returned.
	 */
	@Override
	public Map<String, Table> get() {
		LOG.info("Building tables from files in base directory {}", directory.getPath());
		
		// List suitable files (specified by SqlOnFileFileFilter) in given directory
	    File[] files = directory.listFiles(new SqlOnFileFileFilter());
	    if (files == null) {
	        System.out.println("directory " + directory + " not found");
	        return ImmutableMap.<String, Table>of();
	    }
	    LOG.info("Found {} files in directory {}", files.length, directory.getPath());
	    
	      // Build a map from table name to table; each file becomes a table.
	    final ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();
	    Stream.of(files).map(new SchemaTableMapEntryBuilder(Sources.of(directory)))
	    .filter(Optional::isPresent)
	    .map(Optional::get)
	    .forEach(builder::put);
	    LOG.info("Builder Map {}", builder.build().keySet());
	    return builder.build();
	}

}
