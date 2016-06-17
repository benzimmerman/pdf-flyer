/*
 * Copyright 2016 Ben Zimmerman and distributed under v3 of the LGPL.
 */
package kmctb.ui;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * @author zeroblue76@gmail.com
 *
 */
public class ImageFileFilter extends FileFilter {
	private static final List<String> IMAGE_EXTENSIONS = Arrays
			.asList(new String[] { ".png", ".jpg", ".jpeg", ".gif", ".bmp", ".tif", ".tiff" });

	@Override
	public boolean accept(final File f) {
		final String n = f.getName().toLowerCase();
		return IMAGE_EXTENSIONS.stream().anyMatch((e) -> n.endsWith(e));
	}

	@Override
	public String getDescription() {
		return "Image files " + IMAGE_EXTENSIONS;
	}
}
