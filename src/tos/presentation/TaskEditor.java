/*
 * TaskEditor.java
 * 		Called by MainWindow double click a task or create new task
 * 		using flag to determine viewing mode('v') or creating mode('c')
 */
package tos.presentation;

import java.text.SimpleDateFormat;

import acceptanceTests.EventLoop;
import acceptanceTests.Register;
import java.util.ArrayList;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

import tos.business.*;
import tos.objects.*;
@SuppressWarnings("unused")
public class TaskEditor {
	private Display display;
	private Shell shell;

	private Image image;

	private Text titleText;
	private Text tagsText;
	private Text dueDateText;
	private Text detailText;
	private Text commentText;

	private ArrayList<Tag> tagList;

	private Button setTags;
	private Button setdueDate;
	private Button editButton;
	private Button submitButton;
	private Button closeButton;

	private AccessTasks tasks;
	private Task currentTask;
	private Section currentSection;

	private final Calendar dueDate;
	private final Calendar currentDate;
	private SimpleDateFormat date_format;

	private final int WIDTH = 430;
	private final int HEIGHT = 485;

	private char mode;
	
	private int year;
	private int month;
	private int day;
	
	private Text tagText;
	// All of the components below are ONLY used for ATR testing.
	// Warnings are being suppressed for them because they are not used.	
	private Button testSaveTag;
	private Button testCancelTag;
	private Tag testTag;
	
	private Button testSaveDate;

	public TaskEditor() {
		display = Display.getDefault();
		Register.newWindow(this);
		dueDate = Calendar.getInstance();
		currentDate = Calendar.getInstance();
		date_format = new SimpleDateFormat("EEE, dd MMM, yyyy");

		if (tasks == null) {
			tasks = new AccessTasks();
		}
	}

	/*
	 * Create Task Editor Window
	 */
	public Task createWindow(Section section, Task task, char flag) {
		mode = flag;
		currentSection = section;
		if (task == null) {
			currentTask = new Task();
		} else {
			currentTask = task;
		}
		dueDate.setTime(currentTask.getDueDate());
		image = new Image(null, "images/sjgl-02-s.png");
		shell = new Shell(display);
		shell.setImage(image);
		shell.setSize(WIDTH, HEIGHT);
		Rectangle place = display.getBounds();
		int shellX = (place.width - WIDTH) / 2;
		int shellY = (place.height - HEIGHT) / 2;
		shell.setLocation(shellX, shellY);
		shell.setText("Task Editor");

		TabFolder folder = new TabFolder(shell, SWT.NONE);
		folder.setBounds(10, 10, 400, 400);

		createBasic(folder, shell, currentTask, flag);
		shellControl(display, shell);
		return currentTask;
	}

	/*
	 * Create Task Editor Basic Tab Folder
	 */
	private void createBasic(TabFolder folder, final Shell shell, Task task,
			char flag) {
		final Composite basicComp = new Composite(folder, SWT.NONE);
		TabItem basicTab = new TabItem(folder, SWT.NONE);
		basicTab.setText("Basic");
		basicTab.setControl(basicComp);

		// Title Label
		final Label titleLabel = new Label(basicComp, SWT.NONE);
		titleLabel.setText("Title:");
		titleLabel.setSize(60, 20);
		titleLabel.setLocation(5, 15);

		// Tags Label
		final Label tagsLabel = new Label(basicComp, SWT.NONE);
		tagsLabel.setText("Tags:");
		tagsLabel.setSize(60, 20);
		tagsLabel.setLocation(5, 40);

		// Due Date Label
		final Label dueDateLabel = new Label(basicComp, SWT.NONE);
		dueDateLabel.setText("Due Date:");
		dueDateLabel.setSize(60, 20);
		dueDateLabel.setLocation(5, 65);

		// Detail Label
		final Label detailLabel = new Label(basicComp, SWT.NONE);
		detailLabel.setText("Detail:");
		detailLabel.setSize(60, 20);
		detailLabel.setLocation(5, 90);

		// Comment Label
		final Label commentLabel = new Label(basicComp, SWT.NONE);
		commentLabel.setText("Comment:");
		commentLabel.setSize(60, 20);
		commentLabel.setLocation(5, 265);

		titleText = new Text(basicComp, SWT.BORDER);
		titleText.setBounds(80, 15, 300, 20);
		titleText.setTextLimit(30);
		if (flag == 'v') {
			titleText.setEnabled(false);
			if (task.getTitle() != null) {
				titleText.setText(task.getTitle());
			} else {
				throw new NullPointerException("Null task");
			}
		} else if (flag == 'c') {
			titleText.setText("Enter Title (30 chars max)");
		} else {
			throw new IllegalArgumentException("No matched mode");
		}

		tagsText = new Text(basicComp, SWT.BORDER);
		tagsText.setEditable(false);
		tagsText.setBounds(80, 40, 190, 20);
		tagList = new ArrayList<Tag>();

		setTags = new Button(basicComp, SWT.NONE);
		setTags.setBounds(280, 40, 100, 20);
		setTags.setText("Set Tags");
		setTags.addSelectionListener(new SetTagsButtonListener());
		if (flag == 'v') {
			setTags.setEnabled(false);
			tagList = tasks.getTags(currentTask.getTaskID());
			if (tagList != null) {
				if (!tagList.isEmpty())
					tagsText.setText(tagList.toString());
				else
					tagsText.setText("No tags.");
			} else {
				tagsText.setText("No tags.");
			}
		} else if (flag != 'v' && flag != 'c') {
			throw new IllegalArgumentException("No matched mode");
		}

		dueDateText = new Text(basicComp, SWT.BORDER);
		dueDateText.setEditable(false);
		dueDateText.setBounds(80, 65, 190, 20);

		setdueDate = new Button(basicComp, SWT.NONE);
		setdueDate.setBounds(280, 65, 100, 20);
		setdueDate.setText("Set Due Date");
		setdueDate.addSelectionListener(new SetDDButtonListener());
		if (flag == 'v') {
			setdueDate.setEnabled(false);
			if (task.getDueDate() != null) {
				dueDateText.setText(date_format.format(task.getDueDate()));
			} else {
				throw new NullPointerException("Null task");
			}
		} else if (flag != 'v' && flag != 'c') {
			throw new IllegalArgumentException("No matched mode");
		}

		detailText = new Text(basicComp, SWT.BORDER | SWT.V_SCROLL);
		detailText.setBounds(80, 90, 300, 170);
		detailText.setTextLimit(1000);
		if (flag == 'v') {
			detailText.setEnabled(false);
			if (task.getDetail() != null) {
				detailText.setText(task.getDetail());
			} else {
				throw new NullPointerException("Null task");
			}
		} else if (flag == 'c') {
			detailText.setText("Enter Detail (1000 chars max)");
		} else {
			throw new IllegalArgumentException("No matched mode");
		}

		commentText = new Text(basicComp, SWT.BORDER | SWT.V_SCROLL);
		commentText.setBounds(80, 265, 300, 105);
		commentText.setTextLimit(800);
		if (flag == 'v') {
			commentText.setEnabled(false);
			if (task.getComment() != null) {
				commentText.setText(task.getComment());
			} else {
				throw new NullPointerException("Null task");
			}
		} else if (flag == 'c') {
			commentText.setText("Enter Comment (800 chars max)");
		} else {
			throw new IllegalArgumentException("No matched mode");
		}

		submitButton = new Button(shell, SWT.NONE);
		submitButton.setBounds(260, 420, 60, 20);
		submitButton.setText("Save");
		submitButton.addSelectionListener(new SubmitButtonListener());

		if (flag == 'v') {
			submitButton.setEnabled(false);
			editButton = new Button(shell, SWT.NONE);
			editButton.setBounds(180, 420, 60, 20);
			editButton.setText("Edit");
			editButton.addSelectionListener(new EditButtonListener());
		}

		closeButton = new Button(shell, SWT.NONE);
		closeButton.setBounds(340, 420, 60, 20);
		closeButton.setText("Cancel");
		closeButton.addSelectionListener(new CancelButtonListener());

		shell.open();
	}

	private void changeCurrent(Task currentTask) {
		this.currentTask = currentTask;
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

	class SetTagsButtonListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			shell.setEnabled(false);
			final Display tagDisplay = Display.getDefault();
			final Shell tagShell = new Shell(tagDisplay);
			Rectangle tagPlace = tagDisplay.getBounds();
			int tagShellX = (tagPlace.width - 200) / 2;
			int dateShellY = (tagPlace.height - 200) / 2;
			tagShell.setLocation(tagShellX, dateShellY);
			tagShell.setImage(image);
			tagShell.setLayout(new RowLayout());
			tagShell.setText("Tags");

			Composite tagWindow = new Composite(tagShell, SWT.NONE);
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 3;
			GridData gridData = new GridData();
			gridData.horizontalSpan =3 ;
			gridData.minimumWidth = 100;
			tagWindow.setLayout(gridLayout);
			tagText = new Text(tagWindow, SWT.BORDER);
			tagText.setLayoutData(gridData);
			tagText.setTextLimit(10);

			Button saveButton = new Button(tagWindow, SWT.NONE);
			saveButton.setText("Save");
			saveButton.setSize(40,20);
			saveButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					String str = tagText.getText();
					if (!str.equals("")) {
						Tag tag = new Tag(tagText.getText(), currentTask
								.getTaskID());
						if (tag != null && tagList == null) {
							tagList = new ArrayList<Tag>();
							tagList.add(tag);
							tagsText.setText(tagList.toString());
						} else if (tag != null && tagList != null) {
							int size = tagList.size();
							boolean isContain = false;
							for(int i = 0; i < size && !isContain; i++){
								if(tag.getTag().equals(tagList.get(i).getTag()))
									isContain = true;
							}
							if(!isContain)
								tagList.add(tag);
							else{
								MessageBox msgBox = new MessageBox(shell,
										"Error", "Can't add a tag that already exists.",
										"Okay");
								msgBox.open();
							}
							tagsText.setText(tagList.toString());
							testTag = tagList.get(0);
						}
					} else {
						MessageBox errorBox = new MessageBox(tagShell,"ERROR","Don't have input for tag.\nPlease enter again.","Ok");
						errorBox.open();
					}
					tagShell.dispose();
					shell.setEnabled(true);
					shell.setFocus();
				}
			});
			
			testSaveTag = saveButton;
			
			if (mode == 'v') {
				Button removeButton = new Button(tagWindow, SWT.NONE);
				removeButton.setText("Remove");
				removeButton.setSize(40, 20);
				int size = tasks.getTags(currentTask.getTaskID()).size();
				if(size == 0)
					removeButton.setEnabled(false);
				removeButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						int i;
						int size = 0;
						tagList = tasks.getTags(currentTask.getTaskID());
						if (tagList != null)
							size = tagList.size();
						final int tempSize = size;
						final Button checkBox[] = new Button[size];
						final Tag rule[] = new Tag[size];
						for(i = 0; i < size; i++){
								rule[i] = tagList.get(i);
						}

						shell.setEnabled(false);
						tagShell.setEnabled(false);
						final Display tagListDisplay = Display.getDefault();
						final Shell tagListShell = new Shell(tagListDisplay);
						Rectangle tagListPlace = tagListDisplay.getBounds();
						int tagListShellX = (tagListPlace.width - 200) / 2;
						int tagListShellY = (tagListPlace.height - 200) / 2;
						tagListShell.setLocation(tagListShellX, tagListShellY);
						tagListShell.setImage(image);
						tagListShell.setLayout(new RowLayout());
						tagListShell.setText("Filter");

						Composite tagListWindow = new Composite(tagListShell,
								SWT.NONE);
						GridLayout gridLayout = new GridLayout();
						gridLayout.numColumns = 2;
						gridLayout.horizontalSpacing = 2;
						tagListWindow.setLayout(gridLayout);
						
						if(size != 0){
							for (i = 0; i < size; i++) {
								checkBox[i] = new Button(tagListWindow,
										SWT.CHECK);
								checkBox[i].setText(rule[i].toString());
							}
							
							Button saveButton = new Button(tagListShell, SWT.NONE);
							saveButton.setText("SAVE");
							saveButton.setSize(40,20);
							saveButton.addSelectionListener(new SelectionAdapter(){
								public void widgetSelected(SelectionEvent e){
									tagList = new ArrayList<Tag>();
									for(int i=0; i < tempSize; i++){
										if(!checkBox[i].getSelection())
											tagList.add(rule[i]);
									}
									if (tagList == null || tagList.size() == 0)
										tagsText.setText("No tags.");
									else
										tagsText.setText(tagList.toString());
									tagListShell.dispose();
								}
							});
							
							Button closeButton = new Button(tagListShell, SWT.NONE);
							closeButton.setText("CLOSE");
							closeButton.setSize(40,20);
							closeButton.addSelectionListener(new SelectionAdapter(){
								public void widgetSelected(SelectionEvent e){
									tagListShell.dispose();
								}
							});
							tagListShell.pack();
							tagListShell.open();
							shellControl(tagListDisplay, tagListShell);
							tagShell.dispose();
						}else{
							MessageBox mb = new MessageBox(tagShell,"Error","No tag.\nPlease add a tag first.","Okey");
							mb.open();
							tagShell.dispose();
						}

						shell.setEnabled(true);
						shell.setFocus();
					}
				});
			}
			Button tagCancelButton = new Button(tagWindow, SWT.NONE);
			tagCancelButton.setSize(20,20);
			tagCancelButton.setText("Cancel");
			tagCancelButton.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e){
					tagShell.dispose();
					shell.setEnabled(true);
					shell.setFocus();
				}
			});
			testCancelTag = tagCancelButton;
			
			tagShell.pack();
			tagShell.open();
			shellControl(tagDisplay, tagShell);
			shell.setEnabled(true);
			shell.setFocus();
		}
	}

	class SetDDButtonListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			shell.setEnabled(false);
			final Display dateDisplay = Display.getDefault();
			final Shell dateShell = new Shell(dateDisplay);
			Rectangle datePlace = dateDisplay.getBounds();
			int dateShellX = (datePlace.width - 200) / 2;
			int dateShellY = (datePlace.height - 200) / 2;
			dateShell.setLocation(dateShellX, dateShellY);
			dateShell.setImage(image);
			dateShell.setLayout(new RowLayout());
			dateShell.setText("Due Date");

			Composite dateWindow = new Composite(dateShell, SWT.NONE);
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			dateWindow.setLayout(gridLayout);
			final DateTime calendar = new DateTime(dateWindow, SWT.CALENDAR);
			GridData gridData = new GridData();
			gridData.horizontalSpan=2;
			calendar.setLayoutData(gridData);
			calendar.setDate(dueDate.get(Calendar.YEAR), dueDate.get(Calendar.MONTH), dueDate.get(Calendar.DAY_OF_MONTH));
			calendar.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					year = calendar.getYear();
					month = calendar.getMonth();
					day = calendar.getDay();
				}
			});
			
			Button saveButton = new Button(dateWindow, SWT.NONE);
			saveButton.setText("SAVE");
			saveButton.setSize(40,20);
			saveButton.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e){
					dueDate.set(year, month, day);
					if(dueDate.compareTo(currentDate) < 0){
						dueDate.setTime(currentTask.getDueDate());
						MessageBox msgBox = new MessageBox(shell,
								"Error", "Due Date can't be before than today.\n" +
								"Please select again.", "Okay");
						msgBox.open();
					}
					dueDateText.setText(date_format.format(dueDate.getTime()));
					dateShell.dispose();
				}
			});
			testSaveDate = saveButton;
			
			Button displayCancelButton = new Button(dateWindow, SWT.NONE);
			displayCancelButton.setSize(20,20);
			displayCancelButton.setText("Cancel");
			displayCancelButton.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e){
				dateShell.dispose();
				shell.setEnabled(true);
				shell.setFocus();
				}
			});
			
			dateShell.pack();
			dateShell.open();
			shellControl(dateDisplay, dateShell);
			shell.setEnabled(true);
			shell.setFocus();
		}
	}

	class EditButtonListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			editButton.setEnabled(false);
			titleText.setEnabled(true);
			setdueDate.setEnabled(true);
			detailText.setEnabled(true);
			commentText.setEnabled(true);
			submitButton.setEnabled(true);
			setTags.setEnabled(true);
		}
	}

	class SubmitButtonListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			String title = titleText.getText();
			String detail = detailText.getText();
			String comment = commentText.getText();
			Calendar duedate = dueDate;
			ArrayList<Tag> tags = tagList;

			currentTask.setTitle(title);
			currentTask.setDueDate(duedate);
			currentTask.setDetail(detail);
			currentTask.setComment(comment);
			changeCurrent(currentTask);

			if (mode == 'c') {
				tasks.createTask(currentTask, currentSection, tags);
			} else if (mode == 'v') {
				tasks.updateTask(currentTask, tags);
			} else {
				throw new IllegalArgumentException(
						"Creation/Edit Flag not set within task editor");
			}
			shell.dispose();
		}
	}

	class CancelButtonListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			shell.setVisible(false);
			shell.dispose();
		}
	}
}
