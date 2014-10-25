SearchMDN
========

Intellij idea plugin

Search the Mozilla Developer Network or Google using the currently selected text in the editor.

Invoked by shortcut alt-m or alt-g



/**
 * Created by Robert Edgar on 10/24/2014.
 * Search on Mozilla Developer Network or Google using the currently selected text
 * Invoked by the shortcut ctrl+shift+M
 */
public class SearchMDNSelected extends EditorAction  {

    public SearchMDNSelected(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    public SearchMDNSelected() {
        this(new SearchHandler());
    }

    /**
     * static nested class - no access to instance specific data
     */
    private static class SearchHandler extends EditorWriteActionHandler {
        private SearchHandler() { }

        @Override
        public void executeWriteAction(Editor editor, Caret caret, DataContext dataContext) {
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
                        if(editor.myCommandProcessor.myCurrentCommand.myName=='Search Google') {
                            url = "http://google.com/search?q=";
                        }else{
                            url = "http://developer.mozilla.org/en-US/search?q=" + URLEncoder.encode(searchText.replaceAll("\n", ""), "UTF-8");
                        }
                        SearchHandler.openURI(new URL(url).toURI());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * Open the url in the default browser
         * @param uri
         */
        public static void openURI(URI uri) {
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
}

