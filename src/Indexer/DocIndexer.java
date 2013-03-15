/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Indexer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Fernando Graças
 */
public class DocIndexer {
    
    final static String INDEX_PATH = "index";

    public static int indexMovieFile(String movieXMLFile){
        
        File fXmlFile = new File(movieXMLFile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder;
        org.w3c.dom.Document doc;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
        } catch (SAXException ex) {
            
            System.out.println("Error parsing XML FILE");
            Logger.getLogger(DocIndexer.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
            
        } catch (ParserConfigurationException ex) {
            
            System.out.println("Error parsing XML FILE");
            Logger.getLogger(DocIndexer.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }catch (IOException ex) {
            
            System.out.println("Error parsing XML FILE");
            Logger.getLogger(DocIndexer.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } 
	
        //optional, but recommended
	doc.getDocumentElement().normalize();
        
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        
        NodeList nList = doc.getElementsByTagName("movie");
 
	System.out.println("Number of films:" +  nList.getLength());
        
        
        System.out.println("Indexing to directory '" + INDEX_PATH);

        Directory dir;
        try {
            dir = FSDirectory.open(new File(INDEX_PATH));
            
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);

            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);


            IndexWriter writer = new IndexWriter(dir, iwc);

            for(int i = 0; i < nList.getLength(); i++){
                Element eElement = (Element)nList.item(i);

                Movie.ParseAndIndexMovie(eElement, writer);

            }

            writer.close();
            
        } catch (IOException ex) {
            System.out.println("Error indexing XML FILE");
            Logger.getLogger(DocIndexer.class.getName()).log(Level.SEVERE, null, ex);
            
            return -2;
        }
        
        
        return 0;
    }
    
    
    
}
