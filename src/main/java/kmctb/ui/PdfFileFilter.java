/*
 * Copyright 2016 Ben Zimmerman and distributed under v3 of the LGPL.
 */
package kmctb.ui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author zeroblue76@gmail.com
 */
public class PdfFileFilter extends FileFilter {

	@Override
	public boolean accept(final File f) {
		return f.getName().toLowerCase().endsWith(".pdf");
	}

	@Override
	public String getDescription() {
		return "PDF files *.pdf";
	}
}
