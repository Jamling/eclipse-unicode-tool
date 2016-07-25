package cn.ieclipse.pde.unicodetool.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import cn.ieclipse.pde.unicodetool.UnicodeUtil;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class UnicodeView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "cn.ieclipse.pde.unicodetool.views.UnicodeView";
	private Action reverseAction;

	// private Action action2;
	// private Action doubleClickAction;

	public Action getReverseAction() {
		return reverseAction;
	}

	Text leftText;
	Text rightText;
	private ISelectionListener listener = new ISelectionListener() {

		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (part != UnicodeView.this) {
				showSelection(selection);
			}
		}
	};

	/**
	 * The constructor.
	 */
	public UnicodeView() {
	}

	private void showSelection(ISelection selection) {
		//setContentDescription(selection.getClass().toString());
		if (selection instanceof ITextSelection) {
			String text = ((ITextSelection) selection).getText();
			if (reverseAction.isChecked()) {
				leftText.setText(text);
				rightText.setText(UnicodeUtil.decode(text));
			} else {
				leftText.setText(text);
				rightText.setText(UnicodeUtil.encode(text));
			}
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		
		getSite().getWorkbenchWindow().getSelectionService()
				.addSelectionListener(listener);
		Composite comp = new Composite(parent, SWT.FILL | SWT.BORDER);
		comp.setLayout(new FillLayout(SWT.HORIZONTAL));
		leftText = new Text(comp, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		leftText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (reverseAction.isChecked()) {
					rightText.setText(UnicodeUtil.decode(leftText.getText()));
				} else {
					rightText.setText(UnicodeUtil.encode(leftText.getText()));
				}
			}
		});
		rightText = new Text(comp, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL
				| SWT.WRAP);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				UnicodeView.this.fillContextMenu(manager);
			}
		});
		// Menu menu = menuMgr.createContextMenu(viewer.getControl());
		// viewer.getControl().setMenu(menu);
		// getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(reverseAction);
		manager.add(new Separator());
		// manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(reverseAction);
		// manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(reverseAction);
		// manager.add(action2);
	}

	private void makeActions() {
		reverseAction = new Action("Switch", Action.AS_CHECK_BOX) {
			public void run() {
				showSelection(getSite().getWorkbenchWindow()
						.getSelectionService().getSelection());
			}
		};
		reverseAction.setToolTipText("Reverse native/ascii");
		reverseAction.setImageDescriptor(PlatformUI.getWorkbench()
				.getSharedImages().getImageDescriptor(
						ISharedImages.IMG_ELCL_SYNCED));
		reverseAction.addPropertyChangeListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				if(reverseAction.isChecked()){
					rightText.setText(UnicodeUtil.encode(leftText.getText()));
				}else{
					rightText.setText(UnicodeUtil.decode(leftText.getText()));
				}
			}
		});
	}

	private void hookDoubleClickAction() {

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {

	}

	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService()
				.removeSelectionListener(listener);
		super.dispose();
	}
}