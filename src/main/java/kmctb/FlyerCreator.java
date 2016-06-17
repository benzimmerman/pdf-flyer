/*
 * Copyright 2016 Ben Zimmerman and distributed under v3 of the LGPL.
 */
package kmctb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

import javax.swing.JOptionPane;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfWriter;

import kmctb.ui.FlyerInputUi;

/**
 * @author zeroblue76@gmail.com
 */
public class FlyerCreator {

	static final float MARGIN = Utilities.inchesToPoints(0.125f);

	/**
	 * @param args
	 *            unused
	 */
	public static void main(final String[] args) {
		final FlyerInputUi ui = new FlyerInputUi();
		if (JOptionPane.OK_OPTION == ui.show()) {
			try {
				createPdf(ui.getImageFilename(), ui.getPdfFilename(), ui.getFlyerType());
				showSuccess(ui);
			} catch (IOException | DocumentException e) {
				showError(e);
			}
		}
	}

	/**
	 * @param ui
	 */
	static void showSuccess(final FlyerInputUi ui) {
		final String msg = String.format("PDF created:\n%s", ui.getPdfFilename());
		JOptionPane.showMessageDialog(null, msg, "Complete", JOptionPane.INFORMATION_MESSAGE);
	}

	static void showError(final Throwable t) {
		final Object msg = t.getMessage() == null ? t : t.getMessage();
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
		System.err.println(t.getMessage());
		t.printStackTrace();
	}

	static void createPdf(final String imageFilename, final String pdfFilename, final FlyerType type)
			throws MalformedURLException, IOException, DocumentException {

		final Image i = loadImage(imageFilename);
		final Document document = new Document(PageSize.LETTER, MARGIN, MARGIN, MARGIN, MARGIN);

		try (OutputStream out = new FileOutputStream(pdfFilename)) {
			final PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();

			rotateAndScaleImage(i, type);
			placeImages(document, i, type);

			document.close();
			writer.close();
		}
	}

	static void placeImages(final Document document, final Image image, final FlyerType type) throws DocumentException {
		switch (type) {
		case FULL_SHEET:
			placeFull(document, image);
			break;
		case HALF_SHEET:
			placeHalf(document, image);
			break;
		case QUARTER_SHEET:
			placeQuarter(document, image);
			break;
		default:
			throw new IllegalArgumentException("Unrecognized flyer type: " + type);
		}
	}

	/**
	 * @param document
	 * @param image
	 */
	private static void placeFull(final Document document, final Image image) throws DocumentException {
		// single image
		final float x = document.left();
		final float y = document.bottom();
		final Image i = Image.getInstance(image);
		i.setAbsolutePosition(x, y);
		document.add(i);
	}

	/**
	 * @param document
	 * @param image
	 */
	private static void placeHalf(final Document document, final Image image) throws DocumentException {
		// bottom
		final float x = document.left();
		float y = document.bottom();
		Image i = Image.getInstance(image);
		i.setAbsolutePosition(x, y);
		document.add(i);

		// top
		y += image.getScaledHeight() + 2 * MARGIN;
		i = Image.getInstance(image);
		i.setAbsolutePosition(x, y);
		document.add(i);
	}

	private static void placeQuarter(final Document document, final Image image) throws DocumentException {
		// bottom left
		float x = document.left();
		float y = document.bottom();
		Image i = Image.getInstance(image);
		i.setAbsolutePosition(x, y);
		document.add(i);

		// top left
		y += image.getScaledHeight() + 2 * MARGIN;
		i = Image.getInstance(image);
		i.setAbsolutePosition(x, y);
		document.add(i);

		// top right
		x = document.right() - image.getScaledWidth();
		i = Image.getInstance(image);
		i.setAbsolutePosition(x, y);
		document.add(i);

		// bottom right
		y = document.bottom();
		i = Image.getInstance(image);
		i.setAbsolutePosition(x, y);
		document.add(i);
	}

	static boolean rotateAndScaleImage(final Image image, final FlyerType type) {
		boolean rotate = isLandscape(image);
		if (FlyerType.HALF_SHEET.equals(type))
			rotate = !rotate;

		if (rotate) {
			image.setRotationDegrees(90f);
			// System.out.println("Rotated image 90 degrees");
		}

		final float width = Utilities.inchesToPoints(type.getWidth());
		final float height = Utilities.inchesToPoints(type.getHeight());
		image.scaleToFit(width, height);
		// plain width/height = w/h without considering rotation
		// scaled width/height = w/h including rotation

		return rotate;
	}

	static boolean isLandscape(final Image image) {
		return image.getWidth() > image.getHeight();
	}

	static Image loadImage(final String filename) throws BadElementException, MalformedURLException, IOException {
		final Image image = Image.getInstance(filename);
		/*-
		 * System.out.format("Loaded image [%fx%f] - [%fx%f] @ %dx%d %s%n", image.getWidth(), image.getHeight(),
		 * 		image.getPlainWidth(), image.getPlainHeight(), image.getDpiX(), image.getDpiY(), filename);
		 */
		return image;
	}
}
