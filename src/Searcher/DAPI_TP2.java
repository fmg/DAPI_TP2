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
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
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

        DocIndexer.indexMovieFile("movies2.xml");
        
        Searcher sh = new Searcher();
        sh.initSearch();
        
        
        
        //Query q = QueryBuilder.buildRawQuery(sh.getAnalyzer(), "Great", Movie.MOVIE_TITLE);
        
        BooleanQuery q = new BooleanQuery();
        QueryBuilder.addTermToBooleanQuery(q, Movie.MOVIE_TITLE, BooleanClause.Occur.SHOULD, new String[]{"Great"});
        QueryBuilder.addTermToBooleanQuery(q, Movie.STUDIO_NAME, BooleanClause.Occur.MUST_NOT, new String[]{"Summit"});
        
        //PhraseQuery q = new PhraseQuery();
        //QueryBuilder.addTermsToPhraseQuery(q, Movie.MOVIE_TITLE, new String[]{"Powerful"});
        //QueryBuilder.addTermsToPhraseQuery(q, Movie.MOVIE_SYNOPSIS, new String[]{"Disney"});
        
        
        System.out.println("Query: " + q.toString());
        Document[] docs = sh.performQuery(q);
        
        System.out.println("\nQUERY RESULTS");
        
        for(int i=0;i<docs.length;i++) {
            //int docId = hits[i].doc;
            //org.apache.lucene.document.Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + docs[i].get("title") /*+ " -> Score: " + hits[i].score*/);
        }
      
        sh.finishSearchSession();
        
    }
    
}
