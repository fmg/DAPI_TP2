/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Searcher;

import Indexer.DocIndexer;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author Fernando Graças
 */
public class Searcher {
    
    final static int NUMBER_HITS = 20;
    
    private String userQuery;
    private Analyzer analyzer;
    private IndexSearcher searcher;
    private IndexReader reader;

    public Searcher() {
        analyzer = new StandardAnalyzer(Version.LUCENE_40);
    }
    
    public int initSearch(){
        
        try {
            reader = DirectoryReader.open(FSDirectory.open(new File(DocIndexer.INDEX_PATH)));
            searcher = new IndexSearcher(reader);
        
        } catch (IOException ex) {
            System.out.println("COULD NOT START SEARCH");
            Logger.getLogger(Searcher.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
        
        return 0;
    }
    
    public ScoreDoc[] performQuery(Query q){
        
        TopScoreDocCollector collector = TopScoreDocCollector.create(NUMBER_HITS, true);
        try {
            searcher.search(q, collector);
            
            return collector.topDocs().scoreDocs;
/*
            Document[] documents = new Document[hits.length];
            
            for(int i=0;i<hits.length;++i) {
                int docId = hits[i].doc;
                documents[i] = searcher.doc(docId);
            }
            
            return documents;
   */         
        } catch (IOException ex) {
            Logger.getLogger(Searcher.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }    
        
    }
    
    
    public int finishSearchSession(){
        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(Searcher.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
        return 0;
    }
    
    public Analyzer getAnalyzer(){
        return analyzer;
    }
    
    
    public IndexSearcher getIndexSearcher(){
        return searcher;
    }
    
}
