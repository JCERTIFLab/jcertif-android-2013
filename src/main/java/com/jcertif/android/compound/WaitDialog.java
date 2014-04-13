/*
 * Copyright 2013 JCertifLab.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jcertif.android.compound;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

import com.jcertif.android.R;

public class WaitDialog extends Dialog {

    public static WaitDialog show(Context context, CharSequence title,
            CharSequence message) {
        return show(context, title, message, false);
    }

    public static WaitDialog show(Context context, CharSequence title,
            CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static WaitDialog show(Context context, CharSequence title,
            CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static WaitDialog show(Context context, CharSequence title,
            CharSequence message, boolean indeterminate,
            boolean cancelable, OnCancelListener cancelListener) {
        WaitDialog dialog = new WaitDialog(context);
        dialog.setTitle(title);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        /* The next line will add the ProgressBar to the dialog. */
        dialog.addContentView(new ProgressBar(context), new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        dialog.show();

        return dialog;
    }

    public WaitDialog(Context context) {
        super(context, R.style.WaitDialog);
    }
}
