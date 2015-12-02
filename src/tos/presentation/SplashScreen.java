package tos.presentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SplashScreen {
	private Display display;
	private Shell shell;
	private Image image;
	private Image imageIcon;
	private ImageData imData;
	private Rectangle rect;
	private GC gc;

	private final int SLEEP_TIME = 3000;

	public SplashScreen() {
		imageIcon = new Image(null, "images/sjgl-02-s.png");
		display = Display.getDefault();
		shell = new Shell(display, SWT.NO_TRIM);
		shell.setImage(imageIcon);

		image = new Image(display, "images/sjgl-02-c.png");
		imData = image.getImageData();
		shell.setSize(imData.width, imData.height);

		rect = display.getBounds();
		int shellX = (rect.width - imData.width) / 2;
		int shellY = (rect.height - imData.height) / 2;
		shell.setLocation(shellX, shellY);

		shell.open();
		gc = new GC(shell);
		gc.drawImage(image, 0, 0);

		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {

		}
		image.dispose();
		shell.dispose();
		display.dispose();
	}
}
