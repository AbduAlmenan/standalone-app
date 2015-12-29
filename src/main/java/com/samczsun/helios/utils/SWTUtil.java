/*
 * Copyright 2015 Sam Sun <me@samczsun.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.samczsun.helios.utils;

import com.samczsun.helios.Constants;
import com.samczsun.helios.Helios;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import java.util.concurrent.atomic.AtomicBoolean;

public class SWTUtil {
    public static boolean promptForYesNo(String question) {
        return promptForYesNo(Constants.REPO_NAME + "- Question", question);
    }

    public static boolean promptForYesNo(final String title, final String question) {
        final Display display = Display.getDefault();
        final AtomicBoolean result = new AtomicBoolean(false);
        display.syncExec(() -> {
            Shell shell = new Shell(display, SWT.ON_TOP);

            MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
            messageBox.setMessage(question);
            messageBox.setText(title);

            shell.setSize(0, 0);
            shell.setVisible(true);
            shell.forceFocus();
            shell.forceActive();

            result.set(messageBox.open() == SWT.YES);

            shell.dispose();
        });
        return result.get();
    }

    public static void showMessage(String message) {
        showMessage(Constants.REPO_NAME + " - Message", message, false);
    }

    public static void showMessage(String message, boolean wait) {
        showMessage(Constants.REPO_NAME + " - Message", message, wait);
    }

    public static void showMessage(final String title, final String message, boolean wait) {
        final Display display = Display.getDefault();
        Runnable todo = () -> {
            Shell shell = new Shell(display, SWT.ON_TOP);
            shell.setLayout(new FillLayout());
            MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
            messageBox.setMessage(message);
            messageBox.setText(title);
            shell.setSize(0, 0);
            shell.setVisible(true);
            messageBox.open();
            shell.close();
            shell.dispose();
        };
        if (wait) {
            display.syncExec(todo);
        } else {
            display.asyncExec(todo);
        }
    }

    public static void center(Shell shell) {
        org.eclipse.swt.graphics.Point mainLocation = Helios.getGui().getShell().getLocation();
        org.eclipse.swt.graphics.Point size = Helios.getGui().getShell().getSize();
        Rectangle bounds = new Rectangle(mainLocation.x, mainLocation.y, size.x, size.y);
        Rectangle rect = shell.getBounds();
        int x = bounds.x + (bounds.width - rect.width) / 2;
        int y = bounds.y + (bounds.height - rect.height) / 2;
        shell.setLocation(x, y);
    }
}
