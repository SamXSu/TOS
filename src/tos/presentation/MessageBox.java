package tos.presentation;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

import acceptanceTests.EventLoop;
import acceptanceTests.Register;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;

public class MessageBox extends Dialog {

	protected Object result;
	protected Shell shell;
	protected Display display;
	private Text contentLabel;
	private Button okButton;
	
	private String messageText;
	private String okButtonText;

	public MessageBox(Shell parent, String title, String message, String okButton) {
		super(parent, SWT.OK | SWT.APPLICATION_MODAL);
		setText(title);
		this.messageText = message;
		this.okButtonText = okButton;
		display = getParent().getDisplay();
		Register.newWindow(this);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		if (EventLoop.isEnabled())
		{
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(400, 170);
		shell.setText(getText());
		Rectangle mbPlace = display.getBounds();
		int mbShellX = (mbPlace.width - 400) / 2;
		int mbShellY = (mbPlace.height - 170) / 2;
		
		if (this.messageText.length() > 400){
			contentLabel = new Text(shell, SWT.NONE | SWT.V_SCROLL);
			shell.setSize(480, 270);
			contentLabel.setBounds(10, 10, 460, 250);
		} else {
			contentLabel = new Text(shell, SWT.NONE);
			contentLabel.setBounds(10, 10, 380, 100);
		}
		contentLabel.setText(messageText);
		contentLabel.setEditable(false);
		
		okButton = new Button(shell, SWT.NONE);
		okButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent arg0) {
				result = SWT.OK;
				shell.dispose();
			}
		});
		if (this.messageText.length() > 400){
			shell.setSize(640, 330);
			contentLabel.setBounds(10, 10, 620, 250);
			okButton.setBounds(570, 260, 50, 30);
			mbShellX = (mbPlace.width - 640) / 2;
			mbShellY = (mbPlace.height - 330) / 2;
		} else {
			contentLabel.setBounds(10, 10, 380, 70);
			okButton.setBounds(330, 100, 50, 30);
		}
		shell.setLocation(mbShellX, mbShellY);
		okButton.setSelection(true);
		
		okButton.setText(okButtonText);
	}

}
