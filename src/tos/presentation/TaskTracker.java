package tos.presentation;

import java.text.SimpleDateFormat;

import acceptanceTests.EventLoop;
import acceptanceTests.Register;
import java.util.ArrayList;
import java.util.UUID;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import tos.business.AccessBoards;
import tos.business.AccessSections;
import tos.business.AccessTasks;
import tos.objects.Board;
import tos.objects.Section;
import tos.objects.Task;

public class TaskTracker {
	
	private Display display;
	private Shell shell;

	private AccessBoards accessBoards;
	private AccessSections accessSections;
	private AccessTasks accessTasks;
	private ArrayList<Board> boards;
	private ArrayList<Section> sections;
	private ArrayList<Task> tasks;
	
	private Table table;
	private String[] titles;
	private int countR;
	private int countS;
	private SimpleDateFormat format;
	private Label label;
	private ArrayList<TableItem> tableItems;
	
	private final int WIDTH = 800;
	private final int HEIGHT = 450;
		
	public TaskTracker(){
		display = Display.getDefault();
		Register.newWindow(this);
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		accessBoards = new AccessBoards();
		accessSections = new AccessSections();
		accessTasks  = new AccessTasks();
	}
	
	public int makeWindow(){
		Image image = new Image(null, "images/sjgl-02-s.png");
		shell = new Shell(display);
		shell.setImage(image);
		shell.setSize(WIDTH, HEIGHT);
		Rectangle place = display.getBounds();
		int shellX = (place.width - WIDTH) / 2;
		int shellY = (place.height - HEIGHT) / 2;
		shell.setLocation(shellX, shellY);
		shell.setText("Task Running Time Tracker");
		
		table = new Table (shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(10, 10, 760, 350);
		titles = new String[5];
		titles[0] = "Section Name";
		titles[1] = "Task Name";
		titles[2] = "State";
		titles[3] = "Starting Time";
		titles[4] = "Total Running Time";
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText(titles[i]);
		}
		
		readInfo();
		
		label = new Label(shell,SWT.BORDER);
		label.setBounds(10, 370, 400, 27);
		label.setText("Running tasks: "+countR+" , Stopped Tasks: "+countS+".");
		Button switchState = new Button(shell,SWT.NONE);
		switchState.setText("Switch State");
		switchState.setBounds(450,370,100,27);
		switchState.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem[] ti = table.getSelection();
				if (ti.length > 0){
					Section section = accessSections.getSection((UUID)ti[0].getData());
					if (section != null) {
						tasks = accessTasks.getTasksInSection((UUID)ti[0].getData());
						for (int i=0; i<tasks.size() ;i++) {
							Task task = tasks.get(i);
							task.switchState();
							accessTasks.updateTask(task);
						}
						refresh();
					} else {
						Task task = accessTasks.getTask((UUID)ti[0].getData());
						task.switchState();
						accessTasks.updateTask(task);
						refresh();
					}
				} else {
					MessageBox msgBox = new MessageBox(shell,
							"Error", "Please select a task first.",
							"Okay");
					msgBox.open();
				}
			}
		});
		Button refresh = new Button(shell,SWT.NONE);
		refresh.setText("Refresh");
		refresh.setBounds(560,370,100,27);
		refresh.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
		
		Button close = new Button(shell,SWT.NONE);
		close.setText("Close");
		close.setBounds(670,370,100,27);
		close.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				shell.dispose();
			}
		});
		
		shell.open();
		shellControl(display, shell);
		return 0;
	}
	
	private void readInfo(){
		countR = 0;
		countS = 0;
		boards = accessBoards.getAllBoards();
		tableItems = new ArrayList<TableItem>();
		for (int i=0; i < boards.size(); i++) {
			sections=accessSections.getSectionsOnBoard(boards.get(i).getBoardID());
			for(int j=0; j<sections.size(); j++){
				int sectionIndex = tableItems.size();
				TableItem sectionItem = new TableItem(table, SWT.NONE);
				Section section = sections.get(j);
				sectionItem.setText(0,section.getTitle());
				sectionItem.setText(1,"");
				sectionItem.setText(2,"");
				sectionItem.setText(3,"");
				sectionItem.setData(section.getSectionID());
				int secCountR=0;
				int secCountS=0;
				tasks = accessTasks.getTasksInSection(sections.get(j).getSectionID());
				for(int k=0; k < tasks.size(); k++){
					Task task = tasks.get(k);
					TableItem item = new TableItem(table, SWT.NONE);
					item.setText(0, "");
					item.setText(1, task.getTitle());
					if (task.isRunning()) {
						secCountR++;
						item.setText(2, "In Progress");
						item.setText(3, format.format(task.getStartTime()));
					} else {
						secCountS++;
						item.setText(2, "Stopped");
						item.setText(3, "N/A");
					}
					String time = "Run time:";
					long l = task.getRuntime();
					int sign = 0;
					if (l>=86400000) {
						time += " " + l/86400000 + " Day ";
						l = l % 86400000;
						sign = 1;
					}
					if ((l>=3600000)||(sign == 1)) {
						time += l/3600000 + " Hour ";
						l=l%3600000;
						sign = 2;
					}
					if ((l>=60000)||(sign == 2)) {
						time += l/60000+" Min ";
						l = l%60000;
					}
					time += l/1000 +" Sec ";
					
					item.setText(4,time);
					item.setData(task.getTaskID());
					tableItems.add(item);
				}
				countR += secCountR;
				countS += secCountS;
				sectionItem.setText(4,"Running: "+secCountR+" , Stopped: "+secCountS);
				tableItems.add(sectionIndex, sectionItem);
			}
		}
		for (int i=0; i < titles.length; i++) {
			table.getColumn (i).pack ();
		}
	}

	private void refresh(){
		table.removeAll();
		readInfo();
		label.setText("Running tasks: "+countR+" , Stopped Tasks: "+countS+".");
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

