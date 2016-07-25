package cn.ieclipse.pde.unicodetool.popup.actions;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import cn.ieclipse.pde.unicodetool.UnicodeUtil;

public class Native2AsciiAction implements IObjectActionDelegate {

    private Shell shell;

    /**
     * Constructor for Action1.
     */
    public Native2AsciiAction() {
        super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        shell = targetPart.getSite().getShell();
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction action) {
        ISelection sel = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getSelectionService().getSelection();
        if (sel instanceof IStructuredSelection) {
            try {
                toUnicode(((IStructuredSelection) sel).toArray());
            } catch (Exception e) {
                ErrorDialog.openError(shell, "转换失败", e.getMessage(), null);
            }
        }
    }

    private void toUnicode(Object[] array) throws CoreException, IOException {
        for (Object obj : array) {
            if (obj instanceof IFile) {
                final IFile src = (IFile) obj;
                InputDialog dialog = new InputDialog(shell, "输入文件名称",
                        "请输入{0}转换后的文件名", getInitFileName(src.getName()),
                        new IInputValidator() {
                            public String isValid(String newText) {
                                if (src.getParent().getFullPath().append(
                                        newText).toFile().exists()) {
                                    return "文件已经存在";
                                }
                                return null;
                            }
                        });
                dialog.open();
                IPath path = src.getParent().getFullPath().append(
                        dialog.getValue());
                IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(
                        path);
                file.create(new ByteArrayInputStream(new byte[0]), true, null);
                InputStream is = src.getContents();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                String str = br.readLine();
                while (str != null) {
                    String unicode = UnicodeUtil.encode(str)
                            + System.getProperty("line.separator");
                    file.appendContents(new ByteArrayInputStream(unicode
                            .getBytes()), true, true, null);
                    str = br.readLine();
                }
            }
        }
    }

    private String getInitFileName(String name) {
        int pos = name.lastIndexOf('.');
        String suffix = "_" + Locale.getDefault();
        if (pos == -1) {
            return name + suffix;
        } else {
            return new StringBuilder(name).insert(pos, suffix).toString();
        }
    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    public static void main(String[] args) {
        System.out.println(new Native2AsciiAction().getInitFileName("abc.txt"));
    }
}