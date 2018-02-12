package com.jcg.rca.main;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import swing2swt.layout.BorderLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.jface.viewers.TableViewer;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ColumnPixelData;

public class DataTable {

	protected Shell shell;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table table;
	
	private static int[] pixelArray;
	private static int imageWidth;
	private static int imageHeight;
	
	public void setPixelArray(int[] pixelArray) {
		DataTable.pixelArray = pixelArray;
	}
	
	public void setImageWidth(int imageWidth) {
		DataTable.imageWidth = imageWidth;
	}
	
	public void setImageHeight(int imageHeight) {
		DataTable.imageHeight = imageHeight;
	}
	
	public int[] getPixelArray() {
		return pixelArray;
	}
	
	public int getImageWidth() {
		return imageWidth;
	}
	
	public int getImageHeight() {
		return imageHeight;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DataTable window = new DataTable();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		populateTable(pixelArray, imageWidth, imageHeight);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("Data Table");
		shell.setLayout(new BorderLayout(0, 0));
		
		ScrolledForm scrldfrmData = formToolkit.createScrolledForm(shell);
		scrldfrmData.setLayoutData(BorderLayout.CENTER);
		formToolkit.paintBordersFor(scrldfrmData);
		scrldfrmData.setText("Data");
		scrldfrmData.getBody().setLayout(new FormLayout());
		
		Composite composite = new Composite(scrldfrmData.getBody(), SWT.NONE);
		composite.setBounds(70, 36, 64, 64);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, -10);
		fd_composite.right = new FormAttachment(0, 424);
		fd_composite.top = new FormAttachment(0, 10);
		fd_composite.left = new FormAttachment(0, 10);
		composite.setLayoutData(fd_composite);
		TableColumnLayout tcl_composite = new TableColumnLayout();
		composite.setLayout(tcl_composite);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		
		TableViewer tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		formToolkit.paintBordersFor(table);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnX = tableViewerColumn.getColumn();
		tcl_composite.setColumnData(tblclmnX, new ColumnPixelData(78, true, true));
		tblclmnX.setText("X");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnY = tableViewerColumn_1.getColumn();
		tcl_composite.setColumnData(tblclmnY, new ColumnPixelData(96, true, true));
		tblclmnY.setText("Y");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnValue = tableViewerColumn_2.getColumn();
		tcl_composite.setColumnData(tblclmnValue, new ColumnPixelData(213, true, true));
		tblclmnValue.setText("Value");
		
	}
	
	public void populateTable(int[] pixelArray, int imageWidth, int imageHeight) {
		//add pixel data to the Data Table and the corresponding (x, y) coords
		int count = 0;
		
		for(int y = 0; y < imageHeight; y++) {
			for(int x = 0; x < imageWidth; x++) {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(new String[] {Integer.toString(x), Integer.toString(y), Integer.toString(pixelArray[count])});
				count++;
			}
		}
	}
}
