package de.paulsikorski.calcitetest;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.calcite.schema.Table;
import org.apache.calcite.util.Source;
import org.apache.calcite.util.Sources;
import org.apache.calcite.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/** 
 * Supplies a mapping from table name to actual {@link Table} object based on files in the given directory.
 * @author Paul Sikorski
 *
 */
public class SchemaTableMapBuilder implements Supplier<Map<String, Table>>{

	private static final Logger LOG = LogManager.getLogger(SchemaTableMapBuilder.class);
	private final File baseDirectory;
	
	public SchemaTableMapBuilder(File baseDirectory) {
		super();
		this.baseDirectory = baseDirectory;
	}

	/**
	 * Scans the directory for files and transforms them into tables, which are put into the resulting Map.
	 * If no files are found in the directory, or the directory is null, an empty map is returned.
	 */
	@Override
	public Map<String, Table> get() {
		LOG.info("Building tables from files in base directory {}", baseDirectory.getPath());
		final Source baseSource = Sources.of(baseDirectory);
	    File[] files = baseDirectory.listFiles((dir, name) -> {
	        final String nameSansGz = trim(name, ".gz");
	        return nameSansGz.endsWith(".csv")
	            || nameSansGz.endsWith(".json");
	    });
	    if (files == null) {
	        System.out.println("directory " + baseDirectory + " not found");
	        return ImmutableMap.<String, Table>of();
	    }
	      // Build a map from table name to table; each file becomes a table.
	    final ImmutableMap.Builder<String, Table> builder = ImmutableMap.builder();
	    Stream.of(files).forEach(file -> {
	    	Source source = Sources.of(file);
		    Source sourceSansGz = source.trim(".gz");
		    final Source sourceSansJson = sourceSansGz.trimOrNull(".json");
			if (sourceSansJson != null) {
				final Optional<Entry<String, Table>> entryOpt = getEntry(source, sourceSansJson.relative(baseSource).path());
				if(entryOpt.isPresent()) {
					LOG.info("Creation of table for file {} successful", source.path());
					builder.put(entryOpt.get());
				} else {
					LOG.warn("Unable to add table for file {}", source.path());
				}
			}
			final Source sourceSansCsv = sourceSansGz.trimOrNull(".csv");
			if (sourceSansCsv != null) {
				LOG.warn("CSV-Tables not implemented yet");
			}
	      });
	    return builder.build();
	}

	/**
	 * Transforms a single file, given by parameter source, to a {@link Table} object and returns an Optional
	 * containing this table in an {@link Map.Entry}. If the file is null or no implementation exists for this file type,
	 * an empty {@link Optional} is returned.
	 * @param source
	 * @param tableName
	 * @return
	 */
	private Optional<Entry<String, Table>> getEntry(Source source, String tableName){
		final Source sourceSansGz = source.trim(".gz");
	    final Source sourceSansJson = sourceSansGz.trimOrNull(".json");
	    if (sourceSansJson != null) {
	      final Table table = new JsonTable(source);
	      return Optional.of(Maps.immutableEntry(Util.first(tableName, sourceSansJson.path()), table));
	    }
	    LOG.warn("Only json files are implemented");
	    final Source sourceSansCsv = sourceSansGz.trimOrNull(".csv");
	    if (sourceSansCsv != null) {
	    	LOG.warn("Csv not implemented");
	    }
	    return Optional.empty();
	}
	
	private String trim(String s, String suffix) {
	    String trimmed = trimOrNull(s, suffix);
	    return trimmed != null ? trimmed : s;
	}
	
	  /**
   * Looks for a suffix on a string and returns
   * either the string with the suffix removed
   * or null.
   */
	private String trimOrNull(String s, String suffix) {
	    return s.endsWith(suffix) ? s.substring(0, s.length() - suffix.length()) : null;
	}
}
