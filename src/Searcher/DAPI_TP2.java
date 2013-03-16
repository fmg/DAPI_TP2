/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Searcher;

import Indexer.DocIndexer;
import Indexer.Movie;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.xml.sax.SAXException;

/**
 *
 * @author Fernando Graças
 */
public class DAPI_TP2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ParseException {
        
        /*
        // TODO code application logic here
        
        String indexPath = "index";

        File fXmlFile = new File("movies2.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
 
	//optional, but recommended
	doc.getDocumentElement().normalize();
        
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        
        NodeList nList = doc.getElementsByTagName("movie");
 
	System.out.println("Number of films:" +  nList.getLength());
        
        
        System.out.println("Indexing to directory '" + indexPath + "'...");

        Directory dir = FSDirectory.open(new File(indexPath));
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);
      
        iwc.setOpenMode(OpenMode.CREATE);
        
        // Optional: for better indexing performance, if you
        // are indexing many documents, increase the RAM
        // buffer.  But if you do this, increase the max heap
        // size to the JVM (eg add -Xmx512m or -Xmx1g):
        //
        // iwc.setRAMBufferSizeMB(256.0);

        IndexWriter writer = new IndexWriter(dir, iwc);
        
        
        for(int i = 0; i < nList.getLength(); i++){
            Element eElement = (Element)nList.item(i);
            
            Movie.ParseAndIndexMovie(eElement, writer);
            
        }
        
        writer.close();
        
        
        System.out.println("\n\n\n----------------------------- PERORMING QUERY ------------------------\n\n");
        
        String querystr = "Disney";
        
        Query q = new QueryParser(Version.LUCENE_40, Movie.STUDIO_NAME, analyzer).parse(querystr);

        

        // 3. search
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
    
        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            org.apache.lucene.document.Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("title") + " -> Score: " + hits[i].score);
        }

        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
        * */
        
        DocIndexer.indexMovieFile("movies2.xml");
        
        Searcher sh = new Searcher();
        sh.initSearch();
        
        Document[] docs = sh.performQuerie(Movie.MOVIE_TITLE, "Giant");
        for(int i=0;i<docs.length;i++) {
            //int docId = hits[i].doc;
            //org.apache.lucene.document.Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + docs[i].get("title") /*+ " -> Score: " + hits[i].score*/);
        }
        
        sh.finishSearchSession();
        
    }
}
