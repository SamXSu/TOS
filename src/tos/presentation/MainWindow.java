package tos.presentation;

import java.text.SimpleDateFormat;
import acceptanceTests.Register;
import acceptanceTests.EventLoop;
import java.util.ArrayList;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;

import tos.business.*;
import tos.objects.*;
import org.eclipse.swt.dnd.*;
@SuppressWarnings("unused") // suppresses all unused components extracted out for ATR Testing
public class MainWindow {
	final int BOARD_INDEX = 0;
	private Display display;
	private Shell shell;
	private Image image;
	private AccessBoards accessBoards;
	private ArrayList<Section> sections;
	private ArrayList<Board> boards;
	private AccessSections accessSections;
	private AccessTasks accessTasks;
	private Button[][][] buttons;
	private Group[][] taskGroups;
	private String[] sectionNames;
	private String[][] taskNames;
	private String[][] taskDetails;
	private ArrayList<Tag> tagList;
	private ArrayList<Tag> filterRule;
	private Section section;
	private final int WIDTH = 1200;
	private final int HEIGHT = 675;
	
	// All of the components below are ONLY used for ATR testing.
	// Warnings are being suppressed for them because they are not used.
	private Button testDelete;
	private Button testConfirmDelete;
	
	private String testString;
	private	Label testLabel;
	
	private Menu testFileMenu;
	private MenuItem testNewTask;
	private MenuItem sectionEDT;
	
	private Menu testFilterMenu;
	private MenuItem testFilterItem;
	private MenuItem testFilterTag;
	private Button testTagSelection;
	private Button testTagSelectionSave;
	private String testTagString;
	private Label testTagLabel;

	public MainWindow() {
		accessBoards = new AccessBoards();
		this.boards = accessBoards.getAllBoards();
		accessSections = new AccessSections();
		accessTasks = new AccessTasks();

		this.display = Display.getDefault();
		Register.newWindow(this);
		createWindow(boards, 0);
		runWindow();
	}

	private void runWindow() {
		if (EventLoop.isEnabled()) {
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.dispose();
		}
	}

	private void refresh() {
		for (Control kid : shell.getChildren()) {
			kid.dispose();
		}
		viewBoards(shell);
	}

	private void initTasks() {
		ArrayList<Task> tasks;
		int temp = 0;
		for (int i=0; i < sections.size(); i++) {
			tasks = accessTasks.getTasksInSection(sections.get(i)
					.getSectionID());
			if (tasks.size() > temp)
				temp = tasks.size();
		}
		buttons = new Button[sections.size()][temp][4];
		taskGroups = new Group[sections.size()][temp];
		sectionNames = new String[sections.size()];
		taskNames = new String[sections.size()][temp];
		taskDetails = new String[sections.size()][temp];
	}

	private void initButton() {
		for (int i=0; i < buttons.length; i++) {
			for (int j=0; j < buttons[i].length; j++) {
				if (buttons[i][j][0] != null) {
					buttons[i][j][0].setVisible(false);
				}
			}
		}
	}

	private void createTask(Composite composite, int secNum, int taskNum) {
		Task task;
		String data;
		task = accessTasks.getTask(0, secNum, taskNum);
		data = "";
		data += secNum + "," + taskNum;

		taskGroups[secNum][taskNum] = new Group(composite, SWT.BORDER);
		String taskName=task.getTitle();
		if(taskName.length()>=35){
			taskName = taskName.substring(0, 32) +"...";
		}
		taskNames[secNum][taskNum] = taskName;
		taskGroups[secNum][taskNum].setText(taskName);
		
		taskGroups[secNum][taskNum].setBounds(0, 100 * taskNum, 300, 100);

		taskGroups[secNum][taskNum]
				.addMouseTrackListener(new MouseTrackListener() {
					public void mouseEnter(MouseEvent e) {
						String[] temp;
						temp = ((String) (((Group) e.getSource()).getData()))
								.split(",");
						int i = Integer.parseInt(temp[0]);
						int j = Integer.parseInt(temp[1]);
						initButton();
						buttons[i][j][0].setVisible(true);
						buttons[i][j][1].setVisible(false);
					}

					public void mouseExit(MouseEvent e) {
						String[] temp;
						temp = ((String) (((Group) e.getSource()).getData()))
								.split(",");
						int i = Integer.parseInt(temp[0]);
						int j = Integer.parseInt(temp[1]);
						buttons[i][j][0].setVisible(false);
						buttons[i][j][1].setVisible(false);
					}

					public void mouseHover(MouseEvent e) {
					}
				});

		buttons[secNum][taskNum][0] = new Button(taskGroups[secNum][taskNum],
				SWT.NONE);
		buttons[secNum][taskNum][0].setBounds(210, 60, 80, 30);
		buttons[secNum][taskNum][0].setText("Delete");
		buttons[secNum][taskNum][0]
				.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						String[] temp;
						temp = ((String) (((Button) e.getSource()).getData()))
								.split(",");
						int i = Integer.parseInt(temp[0]);
						int j = Integer.parseInt(temp[1]);
						buttons[i][j][0].setVisible(false);
						buttons[i][j][1].setVisible(true);
					}
				});
		buttons[secNum][taskNum][0]
				.addMouseTrackListener(new MouseTrackListener() {
					public void mouseEnter(MouseEvent e) {
						String[] temp;
						temp = ((String) (((Button) e.getSource()).getData()))
								.split(",");
						int i = Integer.parseInt(temp[0]);
						int j = Integer.parseInt(temp[1]);
						buttons[i][j][0].setVisible(true);
						buttons[i][j][1].setVisible(false);
					}

					public void mouseExit(MouseEvent e) {
					}

					public void mouseHover(MouseEvent e) {
					}
				});

		buttons[secNum][taskNum][1] = new Button(taskGroups[secNum][taskNum],
				SWT.NONE);
		buttons[secNum][taskNum][1].setBounds(210, 60, 80, 30);
		buttons[secNum][taskNum][1].setText("Confirm");
		buttons[secNum][taskNum][1]
				.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						String[] temp;
						temp = ((String) (((Button) e.getSource()).getData()))
								.split(",");
						int i = Integer.parseInt(temp[0]);
						int j = Integer.parseInt(temp[1]);
						Task task = accessTasks.getTask(0, i, j);
						ArrayList<Tag> tags = accessTasks.getTags(task.getTaskID());
						accessTasks.deleteTask(task.getTaskID(),tags);
						refresh();
					}
				});
		
		buttons[secNum][taskNum][2] = new Button(taskGroups[secNum][taskNum],
				SWT.NONE);
		buttons[secNum][taskNum][2].setBounds(10, 20, 30, 30);
		buttons[secNum][taskNum][2].setText("^");
		buttons[secNum][taskNum][2]
				.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						String[] temp;
						temp = ((String) (((Button) e.getSource()).getData()))
								.split(",");
						int i = Integer.parseInt(temp[0]);
						int j = Integer.parseInt(temp[1]);
						Task task = accessTasks.getTask(0, i, j);
						accessTasks.swapTaskPriority(task, -1);
						refresh();
					}
				});
		
		buttons[secNum][taskNum][3] = new Button(taskGroups[secNum][taskNum],
				SWT.NONE);
		buttons[secNum][taskNum][3].setBounds(10, 60, 30, 30);
		buttons[secNum][taskNum][3].setText("v");
		buttons[secNum][taskNum][3]
				.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						String[] temp;
						temp = ((String) (((Button) e.getSource()).getData()))
								.split(",");
						int i = Integer.parseInt(temp[0]);
						int j = Integer.parseInt(temp[1]);
						Task task = accessTasks.getTask(0, i, j);
						accessTasks.swapTaskPriority(task, 1);
						refresh();
					}
				});

		buttons[secNum][taskNum][0].setVisible(false);
		buttons[secNum][taskNum][1].setVisible(false);

		taskGroups[secNum][taskNum].setData(data);
		buttons[secNum][taskNum][0].setData(data);
		buttons[secNum][taskNum][1].setData(data);
		buttons[secNum][taskNum][2].setData(data);
		buttons[secNum][taskNum][3].setData(data);

		Label taskDetail = new Label(taskGroups[secNum][taskNum], SWT.WRAP);
		DragSource source = new DragSource(taskDetail, DND.DROP_MOVE);
		source.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		source.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent e) {
			}

			public void dragFinished(DragSourceEvent e) {
			}

			public void dragSetData(DragSourceEvent e) {
				e.data = (String) ((Label) ((DragSource) e.getSource())
						.getControl()).getData();
			}
		});
		taskDetail.setBounds(45, 20, 240, 80);
		taskDetail.setData(data);
		String detail;
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM, yyyy");
		detail = "";
		detail += sdf.format(task.getDueDate()) + "\n";
		if (task.getDetail().length() > 28) {
			detail += task.getDetail().substring(0, 26);
			detail += "...";
		} else {
			detail += task.getDetail();
		}
		taskDetail.setText(detail);
		taskDetails[secNum][taskNum] = detail;
		taskDetail.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				shell.setEnabled(false);
				Label taskDetail = (Label) e.getSource();
				String str = (String) taskDetail.getData();
				String[] temp = str.split(",");
				// show the task editor
				TaskEditor editor = new TaskEditor();
				editor.createWindow(section, accessTasks.getTask(0,
						Integer.parseInt(temp[0]), Integer.parseInt(temp[1])),
						'v');
				shell.setEnabled(true);
				shell.setFocus();
				// refresh the main window
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						refresh();
					}
				});
			}
		});
		
		taskDetail.addMouseTrackListener(new MouseTrackListener() {
			public void mouseEnter(MouseEvent e) {
				String[] temp;
				temp = ((String) (((Label) e.getSource()).getData()))
						.split(",");
				int i = Integer.parseInt(temp[0]);
				int j = Integer.parseInt(temp[1]);
				initButton();
				buttons[i][j][0].setVisible(true);
				buttons[i][j][1].setVisible(false);
			}

			public void mouseExit(MouseEvent e) {
			}

			public void mouseHover(MouseEvent e) {
			}
		});

		if(secNum == 0 && (taskNum + 1) == buttons[0].length) {
			testDelete = buttons[secNum][taskNum][0];
			testConfirmDelete = buttons[secNum][taskNum][1];
			testString = taskGroups[secNum][taskNum].getText();
			testLabel = new Label(shell,SWT.NONE);
			testLabel.setText(testString);
		}
		
		if(secNum == 1 && (taskNum) == 0) {
			testTagString = taskGroups[secNum][taskNum].getText();
			testTagLabel = new Label(shell,SWT.NONE);
			testTagLabel.setText(testTagString);
		}
	}

	private void createMenu(final Shell shell) {
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		{ // File menu drop down
			MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
			fileItem.setText("File");
			Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
			testFileMenu = fileMenu;
			fileItem.setMenu(fileMenu);
			{ // file menu item 'New'

				MenuItem createNewTask = new MenuItem(fileMenu, SWT.PUSH);
				createNewTask.setText("New Task\tCtrl+Shift+T");
				createNewTask.setAccelerator(SWT.CTRL + SWT.SHIFT + 'T');
				createNewTask.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						shell.setEnabled(false);
						TaskEditor editor = new TaskEditor();
						editor.createWindow(sections.get(0), null, 'c');
						shell.setEnabled(true);
						shell.setFocus();
						refresh();
					}
				});
				testNewTask = createNewTask;
				
				sectionEDT = new MenuItem(fileMenu, SWT.PUSH);
				sectionEDT.setText("Section Editor\tCtrl+Shift+S");
				sectionEDT.setAccelerator(SWT.CTRL + SWT.SHIFT + 'S');
				sectionEDT.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						shell.setEnabled(false);
						SectionManager sectionManager = new SectionManager();
						sectionManager.makeWindow();
						shell.setEnabled(true);
						shell.setFocus();
						refresh();
					}
				});
				
				MenuItem taskT = new MenuItem(fileMenu, SWT.PUSH);
				taskT.setText("Task Running Time Tracker");
				taskT.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						shell.setEnabled(false);
						TaskTracker tc = new TaskTracker();
						tc.makeWindow();
						shell.setEnabled(true);
						shell.setFocus();
						refresh();
					}
				}); 

				// File --> Exit
				MenuItem exit = new MenuItem(fileMenu, SWT.CASCADE);
				exit.setText("Exit");
				exit.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						shell.close();
					}
				});
			}
		}

		MenuItem filterItem = new MenuItem(menu, SWT.CASCADE);
		testFilterItem = filterItem;
		filterItem.setText("Filter");
		Menu filterMenu = new Menu(shell, SWT.DROP_DOWN);
		testFilterMenu = filterMenu;
		filterItem.setMenu(filterMenu);
		{
			MenuItem filterTag = new MenuItem(filterMenu, SWT.CASCADE);
			testFilterTag = filterTag;
			filterTag.setText("Filter by Tags\tCtrl+Shift+F");
			filterTag.setAccelerator(SWT.CTRL + SWT.SHIFT + 'F');
			filterTag.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					shell.setEnabled(false);
					FilterByTags fbt = new FilterByTags(accessTasks);
					fbt.makeWindow();
					shell.setEnabled(true);
					shell.setFocus();
					refresh();
				}
			});
		}

		MenuItem helpItem = new MenuItem(menu, SWT.CASCADE);
		helpItem.setText("Help");
		helpItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// -------help
				MessageBox msgBox = new MessageBox(shell,
						"Help", "1.  File->New->Task to create new tasks.\n"
								+ "2.  Double click to view an existing task.\n"
								+ "3.  To edit an existing task, double click " +
									"and modify it, then click save.\n"
								+ "4.  To delete a task move mouse on task click" +
									" twice to confirm deletion.\n"
								+ "5.  To move a task between sections, drag a task," +
									"\n    drop it into destination section.\n"
								+ "6.  To add a tag to a task, click 'Set Tags' " +
									"button to input a tag, then save. \n    Don't" +
										" save when don't have input.\n"
								+ "7.  To filter by specific tag by select in " +
									"Filter by Tags.\n"
								+ "8.  To show all tasks without filter, " +
									"select nothing in Filter by Tags.\n"
								+ "9.  To remove a tag from a task, click" +
									" 'Set Tags' button, then click 'Remove',\n   " +
									" select tag you want to delete," +
									" then click save.\n"
								+ "10. To create new section by using section manager.\n"
								+ "11. To rearrange section, drag and drop" +
									" it in section manager.\n"
								+ "12. To trace a task by using task tracer.\n",
						"Okay");
				msgBox.open();
			}
		});

	}

	private void createWindow(final ArrayList<Board> boards, int boardNum) {
		image = new Image(null, "images/sjgl-02-s.png");
		shell = new Shell(display);
		shell.setImage(image);
		String windowName = "TOS";
		shell.setText(windowName);
		shell.setSize(WIDTH, HEIGHT);

		Rectangle place = display.getBounds();
		int shellX = (place.width - WIDTH) / 2;
		int shellY = (place.height - HEIGHT) / 2;
		shell.setLocation(shellX, shellY);
		createMenu(shell);
		viewBoards(shell);
		shell.open();
	}

	private void viewBoards(Shell shell) {
		sections = accessSections
				.getSectionsOnBoard(boards.get(0).getBoardID());

		initTasks();

		ScrolledComposite boardSC = new ScrolledComposite(shell, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL);
		// set size of ScrolledComposite
		boardSC.setBounds(0, 0, 1185, 605);
		Composite boardC = new Composite(boardSC, SWT.NONE);
		// set size in ScrolledComposite
		boardC.setSize(390 * sections.size() + 20, 580);
		boardSC.setContent(boardC);

		for (int i = 0; i < sections.size(); i++) {

			section = (Section) sections.get(i);
			ArrayList<Task> tasks = accessTasks.getTasksInSection(sections.get(
					i).getSectionID());
			Group sectionFrame = new Group(boardC, SWT.NONE);
			String str = (BOARD_INDEX + ":" + i + ":" + sections.get(i)
					.getSectionID());
			sectionFrame.setData(str);

			DropTarget dt = new DropTarget(sectionFrame, DND.DROP_MOVE);
			dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
			dt.addDropListener(new DropTargetListener() {
				
				public void dragEnter(DropTargetEvent e) {
				}

				public void dragLeave(DropTargetEvent e) {
				}
				
				public void dragOperationChanged(DropTargetEvent e) {
				}

				public void dragOver(DropTargetEvent e) {
				}

				public void drop(DropTargetEvent e) {
					final int EXPECTED_TOKEN_SIZE = 3;
					// we need to validate the String
					String str = (String) (((DropTarget) e.getSource())
							.getControl()).getData();
					String callerTokens[] = str.split(":");
					String eTokens[] = ((String) e.data).split(",");
					if (callerTokens.length == EXPECTED_TOKEN_SIZE) {
						try {
							int callerSecIndex = Integer
									.parseInt(callerTokens[1]);
							int eSecIndex = Integer.parseInt(eTokens[0]);
							if (callerSecIndex != eSecIndex) {
								int eTaskIndex = Integer.parseInt(eTokens[1]);
								int callerBoardID = Integer
										.parseInt(callerTokens[0]);
								UUID callerSecUUID = UUID
										.fromString(callerTokens[2]);
								accessTasks.setTaskSection(callerBoardID,
										eSecIndex, eTaskIndex, callerSecUUID);
								// we need to drop an inject an update() 
								// or MS native widget crash on this refresh()
								Display.getDefault().asyncExec(new Runnable() {
									public void run() {
										display.update();
										refresh();
									}
								});
							}
						}
						
						catch (NumberFormatException error) {
						
						}
					}
				}

				public void dropAccept(DropTargetEvent e) {
					
				}
			});

			
			sectionFrame.setText(section.getTitle());
			sectionNames[i] = section.getTitle();
			sectionFrame.setBounds(390 * i + 20, 0, 350, 580);
			ScrolledComposite sectionSC = new ScrolledComposite(sectionFrame,
					SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			// set size of ScrolledComposite
			sectionSC.setBounds(10, 20, 330, 550);
			Composite sectionC = new Composite(sectionSC, SWT.NONE);
			// set size in ScrolledComposite
			sectionC.setSize(300, 100 * tasks.size());
			sectionSC.setContent(sectionC);

			for (int j = 0; j < tasks.size(); j++) {
				createTask(sectionC, i, j);
			}
		}
	}

	public void testDoubleClick(){
		TaskEditor editor = new TaskEditor();
		editor.createWindow(section, accessTasks.getTask(0,
				0, 3),'v');
		refresh();
	}
}
