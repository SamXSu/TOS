package tos.presentation;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import acceptanceTests.EventLoop;
import acceptanceTests.Register;

import tos.business.AccessBoards;
import tos.business.AccessTasks;
import tos.objects.Tag;

@SuppressWarnings("unused") // suppresses all unused components extracted out for ATR Testing
public class FilterByTags {
	private Display display;
	private Image image;
	private Shell shell;
	private ArrayList<Tag> tagList;
	private ArrayList<Tag> filterRule;
	int i;
	int size = 0;
	private AccessBoards accessBoards;
	private AccessTasks accessTasks;
	
	private final int WIDTH = 550;
	private final int HEIGHT = 400;
	
	// All of the components below are ONLY used for ATR testing.
	// Warnings are being suppressed for them because they are not used.
	private Button testTagSelection;
	private Button testTagSelectionSave;

	
	public FilterByTags(AccessTasks accessTasks) {
		display = Display.getDefault();
		Register.newWindow(this);
		
		accessBoards = new AccessBoards();
		this.accessTasks = accessTasks;
	}
	
	public void makeWindow() {
		image = new Image(null, "images/sjgl-02-s.png");
		shell = new Shell(display);
		shell.setImage(image);
		shell.setSize(WIDTH, HEIGHT);
		Rectangle tagListPlace = display.getBounds();
		int tagListShellX = (tagListPlace.width - 200) / 2;
		int tagListShellY = (tagListPlace.height - 200) / 2;
		shell.setLocation(tagListShellX, tagListShellY);
		shell.setImage(image);
		shell.setLayout(new RowLayout());
		shell.setText("Filter");
	
		tagList = accessBoards.getTagList();
		if (tagList != null)
			size = tagList.size();
		final Button checkBox[] = new Button[size];
		final Tag rule[] = new Tag[size];
		for(i=0; i < size; i++){
				rule[i] = tagList.get(i);
		}
		
	
		Composite tagListWindow = new Composite(shell,
				SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = 2;
		tagListWindow.setLayout(gridLayout);
	
		if (tagList.size() == 0) {
			Text tagText = new Text(tagListWindow, SWT.BORDER);
			tagText.setEditable(false);
			tagText.setSize(50,20);
			tagText.setText("There are no tags, add a tag first.");
		} else {
			for (i = 0; i < size; i++) {
				checkBox[i] = new Button(tagListWindow, SWT.CHECK);
				checkBox[i].setText(rule[i].toString());
				if(i == 0)
					testTagSelection = checkBox[i];
			}
		}
		
		Button saveButton = new Button(shell, SWT.NONE);
		saveButton.setText("Okay");
		saveButton.setSize(40,20);
		saveButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				filterRule = new ArrayList<Tag>();
				for (int i=0; i < size; i++) {
					if(checkBox[i].getSelection())
						filterRule.add(rule[i]);
				}
				if (filterRule.isEmpty()) {
					accessTasks.setFilterRule(null);
				} else {
					accessTasks.setFilterRule(filterRule);
				}
				shell.dispose();
			}
		});
		testTagSelectionSave = saveButton;
		
		Button closeButton = new Button(shell, SWT.NONE);
		closeButton.setText("Close");
		closeButton.setSize(40,20);
		closeButton.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				filterRule = new ArrayList<Tag>();
				shell.dispose();
			}
		});
	
		shell.pack();
		shell.open();
		shellControl(display, shell);
	
	}
	
	private void shellControl(Display display, Shell shell) {
		if (EventLoop.isEnabled()) {
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			shell.dispose();	
		}
	}
	
}
