package cn.ieclipse.pde.unicodetool.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import cn.ieclipse.pde.unicodetool.views.UnicodeView;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class Ascii2NativeHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public Ascii2NativeHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		ISelection sel = window.getSelectionService().getSelection();
		if (sel instanceof ITextSelection) {
			try {
				UnicodeView view = (UnicodeView) window.getActivePage()
						.showView(UnicodeView.ID);
				view.getReverseAction().setChecked(true);
			} catch (PartInitException e) {
				//
			}

		}
		return null;
	}
}
