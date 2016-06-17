/*
 * Copyright 2016 Ben Zimmerman and distributed under v3 of the LGPL.
 */
package kmctb.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

/**
 * @author zeroblue76@gmail.com
 */
public class FilePicker implements ActionListener {
	private static final FileFilter imageFilter = new ImageFileFilter();
	private static final FileFilter pdfFilter = new PdfFileFilter();

	private final JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
	private final JTextField imageField;
	private final JTextField pdfField;

	public FilePicker(final JTextField imageField, final JTextField pdfField) {
		this.imageField = imageField;
		this.pdfField = pdfField;
		this.jfc.setMultiSelectionEnabled(false);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if ("image".equals(e.getActionCommand())) {
			openImage();
		} else {
			savePdf();
		}
	}

	void prepareFileChooser(final String text, final String title, final FileFilter filter) {
		this.jfc.setCurrentDirectory(new File(text));
		this.jfc.setDialogTitle(title);
		this.jfc.setFileFilter(filter);
	}

	void openImage() {
		prepareFileChooser(this.imageField.getText(), "Choose an image", imageFilter);

		if (this.jfc.showOpenDialog(this.imageField.getRootPane()) == JFileChooser.APPROVE_OPTION) {
			this.imageField.setText(this.jfc.getSelectedFile().getAbsolutePath());
		}
	}

	void savePdf() {
		prepareFileChooser(this.pdfField.getText(), "Save PDF as", pdfFilter);

		boolean ok = true;
		do {
			ok = true;
			if (this.jfc.showSaveDialog(this.pdfField.getRootPane()) == JFileChooser.APPROVE_OPTION) {
				final File selected = this.jfc.getSelectedFile();
				if (selected.exists()) {
					final String msg = String.format("File %s exists. Overwrite?", selected.getName());
					ok = JOptionPane.showConfirmDialog(this.jfc, msg, "File Exists",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

				}
				if (ok)
					this.pdfField.setText(this.jfc.getSelectedFile().getAbsolutePath());
			}
		} while (!ok);
	}
}
