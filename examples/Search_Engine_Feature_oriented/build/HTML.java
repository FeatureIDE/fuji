package HTML;



import java.io.*;
import java.util.regex.*;

import javax.swing.text.html.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;


import org.apache.lucene.demo.html.HTMLParser;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;




/**
 * Verarbeitet HTML-Dateien.
 * 
 * Dieser ContentHandler verarbeitet HTML-Dateien. Dabei werden Ã¼berflÃ¼ssige
 * Tags entfernt (script, style, link, meta, Kommentare) und der Dateiinhalt
 * (ohne sonstige Tags) ausgelesen.<br>
 * AuÃerdem wird der Dateititel aus dem title-Tag Ã¼bernommen.
 * 
 * @author Mr. Pink
 */
public class HTML extends ContentHandler {
	// Ein paar Patterns, um Ã¼berflÃ¼ssige HTML-Elemente zu entfernen
	
	private static Pattern scriptPattern  = Pattern.compile("<script.*?</script>", Pattern.DOTALL);
	private static Pattern stylePattern   = Pattern.compile("<style.*?</style>", Pattern.DOTALL);
	private static Pattern commentPattern = Pattern.compile("<!--.*?-->", Pattern.DOTALL);
	private static Pattern metaPattern    = Pattern.compile("<meta.*?/\\s*>", Pattern.DOTALL);
	private static Pattern linkPattern    = Pattern.compile("<link.*?/\\s*>", Pattern.DOTALL);
//	private static Pattern titlePattern   = Pattern.compile("<title>(.*?)</title>", Pattern.DOTALL);
	
	/**
	 * Ermittelt die von diesem CH gesetzten indexierten Felder
	 * 
	 * @return  die Namen der Felder
	 */
	public String[] getIndexedFields() {
		return new String[] {"content", "title"};
	}

	/**
	 * Fragt ab, ob die Klasse fÃ¼r die Ã¼bergebene Datei zustÃ¤ndig ist.
	 * 
	 * @param  filename  der Dateiname ohne Pfad
	 * @return           true, falls ja, sonst false
	 */
	public boolean handles(String filename) {
		return filename.endsWith(".html") || filename.endsWith(".phtml") || filename.endsWith(".htm");
	}
	
	/**
	 * Wurde das Dokument mit diesem ContentHandler erstellt?
	 * 
	 * @param  doc  das Dokument
	 * @return      true, falls ja (type = html), sonst false
	 */
	public static boolean isOwnerOfDocument(Document doc) {
		return doc.getField("type").stringValue().equals("html");
	}

	/**
	 * Indexieren einer HTML Datei.
	 * 
	 * That's where the magic happens. Hier wird eine HTML-Datei eingelesen und
	 * schlussendlich dem Index hinzugefÃ¼gt. Dabei werden Ã¼berflÃ¼ssige Tags entfernt
	 * und nur der textuelle Inhalt der Datei indexiert.
	 * 
	 * @param filename  Pfad der zu indexierenden Datei
	 * @param writer    der zu verwendende IndexWriter
	 * @return          true wenn die Datei fehlerfrei indexiert werden konnte, sonst false
	 */
	public boolean index(String filename, IndexWriter writer) {
		try {
			String content = HTML.getFileContents(filename);

			// Jetzt verarbeiten wir das HTML. Wir entfernen Tags
			// ebenso wie den HTML-Kopf.

			content = scriptPattern.matcher(content).replaceAll("");  // JavaScript
			content = stylePattern.matcher(content).replaceAll("");   // CSS
			content = commentPattern.matcher(content).replaceAll(""); // Kommentare
			content = metaPattern.matcher(content).replaceAll("");    // Links im Dateikopf
			content = linkPattern.matcher(content).replaceAll("");    // Metaangaben
			String plainText = HTML.extractText(content).trim();

			// Wir fÃ¼gen dem Dokument die von uns zur VerfÃ¼gung
			// gestellten Felder hinzu.

			Document doc = new Document();

			doc.add(new Field("content", plainText, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
			doc.add(new Field("type", "html", Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.YES));

//			Matcher matcher = titlePattern.matcher(content);
//
//			if (matcher.find()) {
//				doc.add(new Field("title", matcher.group(1), Field.Store.YES, Field.Index.ANALYZED));
//			}
			
			// Titel ermitteln
			
			HTMLParser parser = new HTMLParser(new StringReader(content));
			String     title  = "";
			
			try {
				title = parser.getTitle();
				doc.add(new Field("title", title, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
			} catch (InterruptedException e1) {
				doc.add(new Field("title", "Unbenanntes Dokument", Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
				// pass... (kann ja sein, dass kein Titel gesetzt ist)
			}
			
			// Standardfelder

			ContentHandler.addDefaultFields(doc, filename);

			// Dokument dem Index hinzufÃ¼gen

			writer.addDocument(doc);

			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	/**
	 * Gibt den Text in einem HTML Dokument zurÃ¼ck.
	 * 
	 * Dabei werden nicht nur die Tags via RegEx entfernt, sondern es wird der
	 * in Swing enthaltene HTML-Parser mit der Aufgabe betraut.
	 * 
	 * @see   <a href="http://www.exampledepot.com/egs/javax.swing.text.html/GetText.html">www.exampledepot.com</a>
	 * @param fileContent  der Inhalt einer HTML-Datei
	 */
	public static String extractText(String fileContent) {
		final StringBuffer buf = new StringBuffer(1000);
	    
        try {
            HTMLDocument doc = new HTMLDocument() {
				public HTMLEditorKit.ParserCallback getReader(int pos) {
                    return new HTMLEditorKit.ParserCallback() {
                        public void handleText(char[] data, int pos) {
                            buf.append(data);
                            buf.append(' ');
                        }
                    };
                }
            };
    
            EditorKit kit = new HTMLEditorKit();
            kit.read(new StringReader(fileContent), doc, 0);
        }
        catch (IOException e) { }
        catch (BadLocationException e) { }
    
        return buf.toString();
	}
}