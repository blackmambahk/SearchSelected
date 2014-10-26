/**
 * Copyright 2014 robert.john.edgar@outlook.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * A very simple WebStorm plugin to search the Mozilla Developer Network or Google using the currently selected text
 * The search is invoked by the shortcut alt-m or alt-g
 * Inspired by the GoogleIt plugin
 * Compatible with WebStorm 9/8
 */
package com.xpc.plugins;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.util.TextRange;

import java.awt.*;
import java.net.*;

/**
 * Created by Robert Edgar on 10/24/2014.
 * Search the Mozilla Developer Network or Google using the currently selected text
 * Invoked by the shortcut ctrl+shift+M (MDN) or ctrl+shift+G (Google)
 */
public class SearchSelected extends AnAction  {

    public void actionPerformed(AnActionEvent e){
        SearchHandler(e.getActionManager().getId(this), e.getData(PlatformDataKeys.EDITOR));
    }

    @Override
    public void update(AnActionEvent e){
        SearchHandler(e.getActionManager().getId(this), e.getData(PlatformDataKeys.EDITOR));
    }

    /**
     * static nested class - no access to instance specific data
     */
    private static void SearchHandler(String actionId, Editor editor) {
        Document document = editor.getDocument();

        if (document.isWritable()) {
            SelectionModel selectionModel = editor.getSelectionModel();

            // get the range of the selected characters
            TextRange charsRange = new TextRange(selectionModel.getSelectionStart(), selectionModel.getSelectionEnd());

            // get the string to search
            String searchText = document.getText().substring(charsRange.getStartOffset(), charsRange.getEndOffset());

            if( searchText.length() > 0 ) {
                try {
                    String url;
                    if(actionId.equals("SearchSelectedG")) {
                        url = "http://google.com/search?q=";
                    }else{
                        url = "http://developer.mozilla.org/en-US/search?q=";
                    }
                    openURI(new URL(url + URLEncoder.encode(searchText.replaceAll("\n", ""), "UTF-8")).toURI());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Open the url in the default browser
     */
    private static void openURI(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

