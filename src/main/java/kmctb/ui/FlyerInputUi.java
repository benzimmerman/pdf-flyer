/*
 * Copyright 2016 Ben Zimmerman and distributed under v3 of the LGPL.
 */
package kmctb.ui;

import java.awt.Insets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import kmctb.FlyerType;

/**
 * @author zeroblue76@gmail.com
 */
public class FlyerInputUi {

	final JPanel p = new JPanel(new SpringLayout());
	final JTextField imageField = new JTextField(10);
	final JTextField pdfField = new JTextField(10);
	final JButton imageButton = new JButton("Choose");
	final JButton pdfButton = new JButton("Choose");
	final JComboBox<FlyerType> typeCombo = new JComboBox<>(FlyerType.values());

	public FlyerInputUi() {
		_build();
	}

	public int show() {
		int response = -1;
		boolean valid = false;
		do {
			response = JOptionPane.showConfirmDialog(null, this.p, "KMCTB PDF Flyer Creator",
					JOptionPane.OK_CANCEL_OPTION);
			if (JOptionPane.OK_OPTION == response) {
				// validate chosen files
				final Path image = Paths.get(this.imageField.getText());
				final Path pdf = Paths.get(this.pdfField.getText());

				// image file must exist
				// pdf file exists or its parent is a directory

				String error = validateImage(image);
				if (error == null)
					error = validatePdf(pdf);

				// TODO can/should validate image dimensions vs. FlyerType?

				if (error == null)
					valid = true;
				else
					JOptionPane.showMessageDialog(this.p, error, "File Error", JOptionPane.ERROR_MESSAGE);

			} else
				valid = true;
		} while (!valid);

		return response;
	}

	/**
	 * @param pdf
	 * @return
	 */
	private String validatePdf(final Path pdf) {
		String error = null;
		if (Files.exists(pdf)) {
			if (!Files.isWritable(pdf)) {
				error = "Cannot access file\n" + pdf.toAbsolutePath().toString();
			}
		} else {
			if (!Files.isWritable(pdf.getParent())) {
				error = "Cannot write to directory\n" + pdf.getParent().toAbsolutePath().toString();
			}
		}
		return error;
	}

	/**
	 * @param image
	 * @return
	 */
	private String validateImage(final Path image) {
		if (Files.isReadable(image))
			return null;
		else
			return "Cannot access file\n" + image.toAbsolutePath().toString();
	}

	public String getImageFilename() {
		return this.imageField.getText();
	}

	public String getPdfFilename() {
		return this.pdfField.getText();
	}

	public FlyerType getFlyerType() {
		return this.typeCombo.getItemAt(this.typeCombo.getSelectedIndex());
	}

	private void _build() {
		final FilePicker picker = new FilePicker(this.imageField, this.pdfField);
		this.p.add(new JLabel("Flyer Image:"));
		this.p.add(this.imageField);
		this.imageButton.setActionCommand("image");
		this.imageButton.addActionListener(picker);
		this.imageButton.setMargin(new Insets(3, 3, 3, 3));
		this.p.add(this.imageButton);

		this.p.add(new JLabel("Output file:"));
		this.p.add(this.pdfField);
		this.pdfButton.setActionCommand("pdf");
		this.pdfButton.addActionListener(picker);
		this.pdfButton.setMargin(new Insets(3, 3, 3, 3));
		this.p.add(this.pdfButton);

		this.p.add(new JLabel("Flyer type:"));
		this.p.add(this.typeCombo);
		this.p.add(new JLabel(""));

		this.p.add(new JLabel(""));
		this.p.add(new JLabel(""));
		this.p.add(new JLabel(""));

		SpringUtilities.makeCompactGrid(this.p, 4, 3, 3, 3, 3, 3);
	}
}
