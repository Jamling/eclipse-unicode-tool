package cn.ieclipse.pde.unicodetool.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import cn.ieclipse.pde.unicodetool.Activator;
import cn.ieclipse.pde.unicodetool.views.UnicodeView;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class Native2AsciiHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public Native2AsciiHandler() {
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
				view.getReverseAction().setChecked(false);
				ITextSelection tSel = (ITextSelection) sel;
				IHandlerService handlerService = null;
//				handlerService = (IHandlerService) PlatformUI
//						.getWorkbench().getActiveWorkbenchWindow().getService(
//								IHandlerService.class);
				AbstractTextEditor editor = (AbstractTextEditor) PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor();
				IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
				System.out.println(doc);
				//doc.
				
			} catch (PartInitException e) {
				//
			}

		}
		return null;
	}
}
