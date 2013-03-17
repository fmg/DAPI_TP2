/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Searcher;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;

/**
 *
 * @author Desktop
 */
public class QueryBuilder {
    
    
    public static void addTermsToPhraseQuery(PhraseQuery phQuery, String field, String[] terms){
        
        for(int i = 0; i< terms.length; i++){
            phQuery.add(new Term(field, terms[i].toLowerCase()));
        }
    }
    
    
    public static Query buildRawQuery(Analyzer analyzer, String qstr, String field) throws ParseException{
              
            Query q = new QueryParser(Version.LUCENE_40, field, analyzer).parse(qstr);
            return q;
    }
    
    
    public static void addTermToBooleanQuery(BooleanQuery query, String field, BooleanClause.Occur clause, String[] words){
        
        for(int i = 0; i< words.length; i++){
            TermQuery tq = new TermQuery(new Term(field, words[i].toLowerCase()));
            query.add(tq,clause);
        }
    }
    
    public static void addRangeTermToBooleanQuery(BooleanQuery query, String field, BooleanClause.Occur clause, int begin, int end){
        
        NumericRangeQuery nrq = NumericRangeQuery.newIntRange(field, begin, end, true, true);
        query.add(nrq, clause); 
    }
    
    public static void addWildCardTermToBooleanQuery(BooleanQuery query, String field, BooleanClause.Occur clause, String wildcard){
        
        WildcardQuery wcq = new WildcardQuery(new Term(field,wildcard.toLowerCase()));
        query.add(wcq, clause);      
    }
    
}
