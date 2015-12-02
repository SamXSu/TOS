package tos.presentation;

import java.util.ArrayList;

import acceptanceTests.EventLoop;
import acceptanceTests.Register;
import java.util.UUID;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

import tos.business.AccessBoards;
import tos.business.AccessSections;
import tos.objects.Board;
import tos.objects.Section;

public class SectionManager {
	private Display display;
	private Shell shell;
	private ArrayList<Board> boards;
	private ArrayList<Section> sections;
	private AccessBoards accessBoards;
	private AccessSections accessSections;
	private Tree tree;
	private TreeItem treeItem[][];
	private Text sectionName;
	private Button makeNew;
	private Button change;
	private Button delete;
	private Button cancel;
	private TreeItem sourceItem = null;
	private TreeItem selectedItem = null;
	private int newFlag;
	private int changeFlag;
	private int deleteFlag;
	private final int WIDTH = 550;
	private final int HEIGHT = 400;
	private final int BUTTONWIDTH = (WIDTH - 60) / 4;
	
	
	@SuppressWarnings("unused")
	// All of the components below are ONLY used for ATR testing.
	// Warnings are being suppressed for them because they are not used.
	private TreeItem testTreeItem;
	
	public SectionManager() {
		display = Display.getDefault();
		Register.newWindow(this);
	}

	public int makeWindow() {
		Image image = new Image(null, "images/sjgl-02-s.png");
		shell = new Shell(display);
		shell.setImage(image);
		shell.setSize(WIDTH, HEIGHT);
		Rectangle place = display.getBounds();
		int shellX = (place.width - WIDTH) / 2;
		int shellY = (place.height - HEIGHT) / 2;
		shell.setLocation(shellX, shellY);
		shell.setText("Section Manager");

		tree = new Tree(shell, 0);
		tree.setBounds(10, 10, WIDTH - 40, HEIGHT - 140);

		sectionName = new Text(shell, SWT.BORDER);
		sectionName.setText("");
		sectionName.setEditable(false);
		sectionName.setBounds(10, HEIGHT - 120, WIDTH - 40, 30);
		makeNew = new Button(shell, 0);
		makeNew.setText("New");
		makeNew.setBounds(10, HEIGHT - 80, BUTTONWIDTH, 30);
		makeNew.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (newFlag == 0) {
					selectedItem = tree.getSelection()[0];
					if (accessSections.getSection((UUID) selectedItem.getData()) != null) {
						newFlag = 1;
						changeFlag = 0;
						deleteFlag = 0;
						makeNew.setText("Confirm");
						change.setText("Rename");
						delete.setText("Delete");
						cancel.setText("Close");
						sectionName.setText("input the new section name here.");
						sectionName.setEditable(true);
					}

					else {
						MessageBox msgBox = new MessageBox(shell,
								"Error", "You cannot create a new board.",
								"Okay");
						msgBox.open();
					}

				}

				else {
					if (sectionName.getText().length() <= 60) {
						newFlag = 0;
						Section newSection = new Section(sectionName.getText());
						UUID sID = (UUID) selectedItem.getData();
						UUID bID = (accessSections.getSection(sID))
								.getBoardID();
						Board board = accessBoards.getBoard(bID);
						UUID id = accessSections.createSection(newSection, board);
						newSection = accessSections.getSection(id);
						TreeItem ti = new TreeItem(
								selectedItem.getParentItem(), 0);
						ti.setText(sectionName.getText());
						makeNew.setText("New");
						sectionName.setEditable(false);
						change.setText("Rename");
						ti.setData(newSection.getSectionID());
						sectionName.setText("");
					}

					else {
						MessageBox msgBox = new MessageBox(shell,
								"Error", "Section name should be less than 60 characters.",
								"Okay");
						msgBox.open();
					}
				}
			}
		});

		change = new Button(shell, 0);
		change.setText("Rename");
		change.setBounds(BUTTONWIDTH + 20, HEIGHT - 80, BUTTONWIDTH, 30);
		change.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (changeFlag == 0) {
					selectedItem = tree.getSelection()[0];
					if (accessSections.getSection((UUID) selectedItem.getData()) != null) {
						newFlag = 0;
						changeFlag = 1;
						deleteFlag = 0;
						makeNew.setText("New");
						change.setText("Confirm");
						delete.setText("Delete");
						cancel.setText("Close");
						sectionName.setText(selectedItem.getText());
						sectionName.setEditable(true);
					}

					else {
						MessageBox msgBox = new MessageBox(shell,
								"Error", "You cannot change a board name.",
								"Okay");
						msgBox.open();
					}
				}

				else {
					if (sectionName.getText().length() <= 60) {
						changeFlag = 0;
						Section section = accessSections
								.getSection((UUID) selectedItem.getData());
						section.setTitle(sectionName.getText());
						accessSections.updateSection(section);
						selectedItem.setText(sectionName.getText());
						sectionName.setText("");
						sectionName.setEditable(false);
						change.setText("Rename");
					}

					else {
						MessageBox msgBox = new MessageBox(shell,
								"Error", "Section name should be less than 60 characters.",
								"Okay");
						msgBox.open();
					}
				}
			}
		});

		delete = new Button(shell, 0);
		delete.setText("Delete");
		delete.setBounds(BUTTONWIDTH * 2 + 30, HEIGHT - 80, BUTTONWIDTH, 30);
		delete.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (deleteFlag == 0) {
					selectedItem = tree.getSelection()[0];
					if (accessSections.getSection((UUID) selectedItem.getData()) != null) {
						newFlag = 0;
						changeFlag = 0;
						deleteFlag = 1;
						makeNew.setText("New");
						change.setText("Rename");
						delete.setText("Confirm");
						cancel.setText("Close");
						sectionName.setEditable(false);
					} else {
						MessageBox msgBox = new MessageBox(shell,
								"Error", "You cannot delete a new board.",
								"Okay");
						msgBox.open();
					}
				}

				else {
					boolean result = accessSections.deleteSection((UUID) selectedItem.getData());
					if (!result) {
						MessageBox msgBox = new MessageBox(shell,
								"Error", "Can't delete Work In-Progress section.",
								"Okay");
						msgBox.open();
					}
					deleteFlag = 0;
					delete.setText("Delete");
					if (result)
						selectedItem.dispose();
				}
			}
		});
		cancel = new Button(shell, 0);
		cancel.setText("Close");
		cancel.setBounds(BUTTONWIDTH * 3 + 40, HEIGHT - 80, BUTTONWIDTH, 30);
		cancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				shell.setVisible(false);
				shell.dispose();
			}
		});

		newFlag = 0;
		changeFlag = 0;
		deleteFlag = 0;
		printData();

		// drag and drop
		DragSource dragSource = new DragSource(tree, DND.DROP_MOVE
				| DND.DROP_COPY);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceListener() {

			public void dragStart(DragSourceEvent event) {
				if (tree.getSelectionCount() == 0)
					event.doit = false;
			}

			public void dragSetData(DragSourceEvent event) {
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					event.data = tree.getSelection()[0].getData().toString();
					sourceItem = tree.getSelection()[0];
				}
			}

			public void dragFinished(DragSourceEvent event) {
			}
		});

		DropTarget dropTarget = new DropTarget(tree, DND.DROP_MOVE
				| DND.DROP_DEFAULT | DND.DROP_COPY);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetListener() {

			public void dragEnter(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT)
					event.detail = DND.DROP_COPY;
			}

			public void dragOperationChanged(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT)
					event.detail = DND.DROP_COPY;
			}

			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SELECT;
			}

			public void drop(DropTargetEvent event) {
				if (event.item == null) {
					return;
				}
				TreeItem eventItem = (TreeItem) event.item;
				if (TextTransfer.getInstance().isSupportedType(
						event.currentDataType)) {
					UUID uid = UUID.fromString((String) event.data);
					// board reorder banned
					if (accessSections.getSection(uid) == null) {
						return;
					}

					else {
						String temp1 = eventItem.getText();
						eventItem.setText(sourceItem.getText());
						sourceItem.setText(temp1);
						UUID temp2 = (UUID) eventItem.getData();
						eventItem.setData((UUID) sourceItem.getData());
						sourceItem.setData(temp2);
						UUID temp3 = (UUID) eventItem.getData();
						accessSections.switchSections(temp2, temp3);
					}
				}
			}

			public void dragLeave(DropTargetEvent event) {
			}

			public void dropAccept(DropTargetEvent event) {
			}
		});

		shell.open();
		shellControl(display, shell);
		return 0;
	}

	private void printData() {
		accessBoards = new AccessBoards();
		this.boards = accessBoards.getAllBoards();
		accessSections = new AccessSections();
		// set array length
		int max = 0;
		for (int i=0; i < boards.size(); i++) {
			Board board = boards.get(i);
			sections = accessSections.getSectionsOnBoard(board.getBoardID());
			if (sections.size() > max)
				max = sections.size();
		}
		max++;
		treeItem = new TreeItem[boards.size()][max];
		// printing data
		for (int i=0; i < boards.size(); i++) {
			Board board = boards.get(i);
			treeItem[i][0] = new TreeItem(tree, 0);
			treeItem[i][0].setText(board.getName());
			treeItem[i][0].setData(board.getBoardID());
			sections = accessSections.getSectionsOnBoard(board.getBoardID());
			for (int j = 0; j < sections.size(); j++) {
				treeItem[i][j + 1] = new TreeItem(treeItem[i][0], 0);
				Section section = sections.get(j);
				treeItem[i][j + 1].setText(section.getTitle());
				treeItem[i][j + 1].setData(section.getSectionID());
				
				if (j == sections.size()-1) {
					testTreeItem = treeItem[i][j+1];
				}
			}
			treeItem[i][0].setExpanded(true);
		}
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
