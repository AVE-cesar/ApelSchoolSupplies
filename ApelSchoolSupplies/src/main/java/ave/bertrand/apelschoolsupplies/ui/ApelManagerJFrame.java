package ave.bertrand.apelschoolsupplies.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.table.TableRowSorter;

import org.springframework.stereotype.Component;

import ave.bertrand.apelschoolsupplies.Application;
import ave.bertrand.apelschoolsupplies.data.DataModel;
import ave.bertrand.apelschoolsupplies.ui.action.AbstractMenuAction;
import ave.bertrand.apelschoolsupplies.ui.action.CloseListener;
import ave.bertrand.apelschoolsupplies.ui.action.MenuActionType;
import ave.bertrand.apelschoolsupplies.ui.helper.EntryHelper;

@Component
public class ApelManagerJFrame extends JFrame {
	private static final long serialVersionUID = -4114209356464342368L;

	public static final int MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

	public static final String PROGRAM_NAME = "Apel Manager";
	public static final String PROGRAM_VERSION = "1.0.7";

	/**
	 * Popup menu sur la table.
	 */
	private JPopupMenu popup = null;

	private JMenuBar menuBar;
	private JMenu fileMenu = null;
	private JMenu editMenu = null;
	private boolean editMenuActivated = false;
	private AbstractMenuAction editAction = null;
	private AbstractMenuAction duplicateAction = null;
	private AbstractMenuAction deleteAction = null;
	private AbstractMenuAction copyUrlAction = null;
	private AbstractMenuAction copyUserAction = null;
	private AbstractMenuAction copyPasswordAction = null;

	private JToolBar toolBar = null;
	private final JScrollPane scrollPane;

	private JTable table;
	private TableRowSorter<DataModel> sorter;

	/**
	 * Zone pour filter les entrées du tableau.
	 */
	private JTextField filterText;

	private final DataModel model = DataModel.getInstance();
	private final StatusPanel statusPanel;
	private volatile boolean processing = false;

	/**
	 * Création de la fenêtre principale en passant les paramètres de la ligne de
	 * commmande.
	 * 
	 * @param args
	 */
	public ApelManagerJFrame(String[] args) {
		initTheme();

		try {
			setIconImage(MessageDialog.getIcon("lock").getImage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		fillToolbar();

		this.menuBar = new JMenuBar();

		fillFileMenu();
		this.menuBar.add(this.fileMenu);

		fillEditMenu();
		this.menuBar.add(this.editMenu);

		fillPopup();

		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addMouseListener(new MyJTableMouseListener());
		table.setAutoCreateRowSorter(true);
		table.setFillsViewportHeight(true);
		table.getTableHeader().setToolTipText("Click to sort; Shift-Click to sort in reverse order");
		// tri sur la première colonne par défaut
		sorter = new TableRowSorter<DataModel>(model);
		table.setRowSorter(sorter);
		table.getRowSorter().toggleSortOrder(0);
		// sets the popup menu for the table
		table.setComponentPopupMenu(popup);
		table.getColumnModel().getColumn(DataModel.STUDENT_COLUMN).setCellRenderer(new TitleCellRenderer());

		this.scrollPane = new JScrollPane(this.table);

		MenuActionType.bindAllActions(this.table);

		// Create a separate form for filterText and statusText
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Specify a word to match:"), BorderLayout.WEST);

		filterText = new JTextField();
		// Whenever filterText changes, invoke newFilter.
		filterText.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				newFilter();
			}

			public void insertUpdate(DocumentEvent e) {
				newFilter();
			}

			public void removeUpdate(DocumentEvent e) {
				newFilter();
			}
		});
		panel.add(filterText, BorderLayout.CENTER);
		// focus sur la zone de filtre au démarrage
		this.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				filterText.requestFocus();
			}
		});

		this.statusPanel = new StatusPanel();
		panel.add(this.statusPanel, BorderLayout.SOUTH);

		refreshAll();

		getContentPane().add(this.toolBar, BorderLayout.NORTH);
		getContentPane().add(this.scrollPane, BorderLayout.CENTER);
		getContentPane().add(panel, BorderLayout.SOUTH);

		setJMenuBar(this.menuBar);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		int width = 1000;
		// largeur de la fenêtre
		if (args != null && args.length >= 2) {
			width = Integer.parseInt(args[1]);
		}
		int height = 1000;
		// hauteur de la fenêtre
		if (args != null && args.length >= 3) {
			height = Integer.parseInt(args[2]);
		}
		setSize(width, height);
		setMinimumSize(new Dimension(width, height));
		addWindowListener(new CloseListener());
		setLocationRelativeTo(null);
		setVisible(true);

		// set focus to the list for easier keyboard navigation
		// this.table.requestFocusInWindow();
	}

	private void initTheme() {
		MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme() {
			private final ColorUIResource primary1 = new ColorUIResource(0x4d6781);
			private final ColorUIResource primary2 = new ColorUIResource(0x7a96b0);
			private final ColorUIResource primary3 = new ColorUIResource(0xc8d4e2);
			private final ColorUIResource secondary1 = new ColorUIResource(0x000000);
			private final ColorUIResource secondary2 = new ColorUIResource(0xaaaaaa);
			private final ColorUIResource secondary3 = new ColorUIResource(0xdfdfdf);

			@Override
			protected ColorUIResource getPrimary1() {
				return this.primary1;
			}

			@Override
			protected ColorUIResource getPrimary2() {
				return this.primary2;
			}

			@Override
			protected ColorUIResource getPrimary3() {
				return this.primary3;
			}

			@Override
			protected ColorUIResource getSecondary1() {
				return this.secondary1;
			}

			@Override
			protected ColorUIResource getSecondary2() {
				return this.secondary2;
			}

			@Override
			protected ColorUIResource getSecondary3() {
				return this.secondary3;
			}
		});

		try {
			UIManager.put("swing.boldMetal", Boolean.FALSE);
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fillPopup() {
		this.popup = new JPopupMenu();
		this.popup.add(MenuActionType.VALIDATE_ENTRY.getAction());
		this.popup.add(MenuActionType.REPLYTO_ENTRY.getAction());

		this.popup.addPopupMenuListener(new MyPopupMenuListener());
	}

	private void fillEditMenu() {
		this.editMenu = new JMenu("Edit");
		this.editMenu.setMnemonic(KeyEvent.VK_E);
		this.editMenu.add(MenuActionType.VALIDATE_ENTRY.getAction());

		this.editAction = MenuActionType.REPLYTO_ENTRY.getAction();
		this.editMenu.add(editAction);

		this.editMenu.addSeparator();

		this.toggleEditMenu(false);
		this.editMenuActivated = false;
	}

	public void toggleEditMenu(boolean enable) {
		if (this.editMenuActivated && enable) {
			return;
		}
		if (!this.editMenuActivated && !enable) {
			return;
		}

		if (this.editAction != null) {
			this.editAction.setEnabled(enable);
		}
		if (this.duplicateAction != null) {
			this.duplicateAction.setEnabled(enable);
		}
		if (this.deleteAction != null) {
			this.deleteAction.setEnabled(enable);
		}
		if (this.copyUrlAction != null) {
			this.copyUrlAction.setEnabled(enable);
		}
		if (this.copyUserAction != null) {
			this.copyUserAction.setEnabled(enable);
		}
		if (this.copyPasswordAction != null) {
			this.copyPasswordAction.setEnabled(enable);
		}
		this.editMenuActivated = enable;
	}

	private void fillFileMenu() {
		this.fileMenu = new JMenu("File");
		this.fileMenu.setMnemonic(KeyEvent.VK_F);

		this.fileMenu.add(MenuActionType.IMPORT_EMAILS.getAction());
		this.fileMenu.add(MenuActionType.LOAD_DB.getAction());
		this.fileMenu.add(MenuActionType.EXPORT_XLSX.getAction());
		this.fileMenu.add(MenuActionType.REGISTRATIONSHEET_XLSX.getAction());

		this.fileMenu.addSeparator();
		this.fileMenu.add(MenuActionType.EXIT.getAction());
	}

	private void fillToolbar() {
		this.toolBar = new JToolBar();
		this.toolBar.setFloatable(false);

		this.toolBar.add(MenuActionType.IMPORT_EMAILS.getAction());
		this.toolBar.add(MenuActionType.LOAD_DB.getAction());
		this.toolBar.addSeparator();
		this.toolBar.add(MenuActionType.EXPORT_XLSX.getAction());
		this.toolBar.add(MenuActionType.REGISTRATIONSHEET_XLSX.getAction());
		this.toolBar.addSeparator();
		this.toolBar.add(MenuActionType.VALIDATE_ENTRY.getAction());
		this.toolBar.add(MenuActionType.REPLYTO_ENTRY.getAction());
		this.toolBar.addSeparator();
		this.toolBar.add(MenuActionType.EXIT.getAction());
	}

	public static ApelManagerJFrame getInstance() {
		return getInstance(null);
	}

	public static ApelManagerJFrame getInstance(String[] args) {
		return Application.getSpringContext().getBean(ApelManagerJFrame.class);
	}

	public JTable getEntryTable() {
		return this.table;
	}

	public JPopupMenu getPopupMenu() {
		return this.popup;
	}

	/**
	 * Gets the data model of this frame.
	 *
	 * @return data model
	 */
	public DataModel getModel() {
		return this.model;
	}

	/**
	 * Refresh frame title based on data model.
	 */
	public void refreshFrameTitle() {
		setTitle((getModel().isModified() ? "*" : "") + " - " + PROGRAM_NAME);
	}

	/**
	 * Refresh the entry titles based on data model.
	 *
	 * @param selectTitle
	 *            title to select, or {@code null} if nothing to select
	 */
	public void refreshEntryTitleList(String selectTitle) {

		this.getModel().fireTableDataChanged();

		if (selectTitle != null) {
			// FIXMEthis.entryTitleList.setSelectedValue(selectTitle, true);
		}
		updateStatusText("Entries count: " + this.getModel().getRowCount(), true);
	}

	public void updateStatusText(String message, boolean permanent) {

		if (permanent) {
			this.statusPanel.setText(message);
		} else {
			String oldText = this.statusPanel.getText();
			this.statusPanel.setText(message);
			// FIXME la suite ne marche pas
			/*
			 * try { Thread.sleep(1000*1); } catch (InterruptedException e) { }
			 * this.statusPanel.setText(oldText);
			 */
		}
	}

	/**
	 * Refresh frame title and entry list.
	 */
	public void refreshAll() {
		refreshFrameTitle();
		refreshEntryTitleList(null);
	}

	/**
	 * Exits the application.
	 */
	public void exitFrame() {
		// Clear clipboard on exit
		EntryHelper.copyEntryField(this, null);

		if (this.processing) {
			return;
		}

		System.exit(0);
	}

	public JPopupMenu getPopup() {
		return this.popup;
	}

	/**
	 * Sets the processing state of this frame.
	 *
	 * @param processing
	 *            processing state
	 */
	public void setProcessing(boolean processing) {
		this.processing = processing;
		for (MenuActionType actionType : MenuActionType.values()) {
			// actionType.getAction().setEnabled(!processing);
		}

		this.table.setEnabled(!processing);
		this.statusPanel.setProcessing(processing);
	}

	/**
	 * Gets the processing state of this frame.
	 *
	 * @return processing state
	 */
	public boolean isProcessing() {
		return this.processing;
	}

	/**
	 * Update the row filter regular expression from the expression in the text box.
	 */
	private void newFilter() {
		RowFilter<DataModel, Object> rf = null;
		// If current expression doesn't parse, don't update.
		try {
			// le préfixe permet d'être insensible à la casse
			rf = RowFilter.regexFilter("(?i)" + filterText.getText(), 0);
		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		sorter.setRowFilter(rf);
	}
}
