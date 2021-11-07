package de.paulsikorski.calcitetest.model;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filters files in a directory by file suffix.
 * @author Paul Sikorski
 *
 */
public class SqlOnFileFileFilter implements FilenameFilter {

	public boolean accept(File dir, String name) {
		final String nameSansGz = trim(name, ".gz");
        return nameSansGz.endsWith(".csv")
            || nameSansGz.endsWith(".json");
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
