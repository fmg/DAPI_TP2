package Indexer;

import com.sun.xml.internal.ws.model.FieldSignature;
import java.io.IOException;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Fernando Graças
 */
public class Movie {

    private final static int NUMBER_SEARCHABLE_FIELDS = 23;
    
    
    public final static String MOVIE_TITLE = "title";
    public final static String MOVIE_ID = "id";
    public final static String MOVIE_YEAR = "year";
    public final static String MOVIE_GENRE = "genre";
    public final static String MOVIE_MPAA_RATING = "mpaa_rating";
    public final static String MOVIE_RUNTIME = "runtime";
    public final static String MOVIE_CRITICS_CONSENSUS = "critics_consensus";
    
    public final static String MOVIE_RATING_CRITICS_RATING = "critics_rating";
    public final static String MOVIE_RATING_CRITICS_SCORE = "critics_score";
    public final static String MOVIE_RATING_AUDIENCE_RATING = "audience_rating";
    public final static String MOVIE_RATING_AUDIENCE_SCORE = "audience_score";
    
    public final static String MOVIE_SYNOPSIS = "synopsis";
    public final static String ACTOR_ID = "actor_id";
    public final static String ACTOR_NAME = "actor_name";
    public final static String MOVIE_CHARACTER = "character";
    public final static String DIRECTOR_NAME = "director_name";
    public final static String STUDIO_NAME = "studio_name";
    
    public final static String REVIEW_CRITIC_NAME = "critic_name";
    public final static String REVIEW_DATE = "review_date";
    public final static String REVIEW_ORIGINAL_SCORE = "review_original_score";
    public final static String REVIEW_FRESHNESS = "review_freshness";
    public final static String REVIEW_PUBLICATION = "publication_name";
    public final static String REVIEW_QUOTE = "review_quote";


    
    public final static boolean debug = true;
    

    public static void ParseAndIndexMovie(Element movie, IndexWriter writer) throws IOException {

        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();

        //id
        long id = Long.parseLong(movie.getElementsByTagName("id").item(0).getTextContent());
        if (debug) {
            System.out.println("ID film :" + id);
        }
        doc.add(new LongField(MOVIE_ID, id, Field.Store.NO));

        //title
        String title = movie.getElementsByTagName("title").item(0).getTextContent();
        System.out.println("Indexing film :" + title);
        doc.add(new TextField(MOVIE_TITLE, title, Field.Store.YES));

        //year
        int year = Integer.parseInt(movie.getElementsByTagName("year").item(0).getTextContent());
        if (debug) {
            System.out.println("year :" + year);
        }
        doc.add(new IntField(MOVIE_YEAR, year, Field.Store.YES));

        //genres
        NodeList genres = ((Element) movie.getElementsByTagName("genres").item(0)).getElementsByTagName("genre");
        for (int i = 0; i < genres.getLength(); i++) {
            String genre = genres.item(i).getTextContent();
            doc.add(new TextField(MOVIE_GENRE, genre, Field.Store.YES));
        }

        //mpaa_rating
        doc.add(new StringField(MOVIE_MPAA_RATING, movie.getElementsByTagName("mpaa_rating").item(0).getTextContent(), Field.Store.YES));

        //runtime
        String runtime = movie.getElementsByTagName("runtime").item(0).getTextContent();
        if (!runtime.equals("-")) {
            doc.add(new IntField(MOVIE_RUNTIME, Integer.parseInt(runtime), Field.Store.YES));
        }
        
        
        //critics_consensus
        doc.add(new TextField(MOVIE_CRITICS_CONSENSUS, movie.getElementsByTagName("critics_consensus").item(0).getTextContent(), Field.Store.NO));


        //ratings
        Element ratings = ((Element) movie.getElementsByTagName("ratings").item(0));
        String critics_rating = ratings.getElementsByTagName("critics_rating").item(0).getTextContent();
        int critics_score = Integer.parseInt(ratings.getElementsByTagName("critics_score").item(0).getTextContent());
        String audience_rating = ratings.getElementsByTagName("audience_rating").item(0).getTextContent();
        int audience_score = Integer.parseInt(ratings.getElementsByTagName("audience_score").item(0).getTextContent());
        doc.add(new StringField(MOVIE_RATING_CRITICS_RATING, critics_rating, Field.Store.YES));
        doc.add(new IntField(MOVIE_RATING_CRITICS_SCORE, critics_score, Field.Store.YES));
        doc.add(new StringField(MOVIE_RATING_AUDIENCE_RATING, audience_rating, Field.Store.YES));
        doc.add(new IntField(MOVIE_RATING_AUDIENCE_SCORE, critics_score, Field.Store.YES));

        //synopsis
        doc.add(new TextField(MOVIE_SYNOPSIS, movie.getElementsByTagName("synopsis").item(0).getTextContent(), Field.Store.NO));

        //abridged_cast
        NodeList cast = ((Element) movie.getElementsByTagName("abridged_cast").item(0)).getElementsByTagName("cast");
        for (int i = 0; i < cast.getLength(); i++) {
            Element actor = (Element) cast.item(i);
            int actor_id = Integer.parseInt(actor.getElementsByTagName("id").item(0).getTextContent());
            String actor_name = actor.getElementsByTagName("name").item(0).getTextContent();

            doc.add(new IntField(ACTOR_ID, actor_id, Field.Store.NO));
            doc.add(new TextField(ACTOR_NAME, actor_name, Field.Store.YES));


            NodeList characters = ((Element) actor.getElementsByTagName("characters").item(0)).getElementsByTagName("character");
            for (int k = 0; k < characters.getLength(); k++) {

                String movie_character = characters.item(k).getTextContent();
                doc.add(new TextField(MOVIE_CHARACTER, movie_character, Field.Store.YES));


            }

        }


        //abridged_directors
        NodeList directors = ((Element) movie.getElementsByTagName("abridged_directors").item(0)).getElementsByTagName("director");
        for (int i = 0; i < directors.getLength(); i++) {

            String movie_director = directors.item(i).getTextContent();
            if (!movie_director.equals("-")) {
                doc.add(new TextField(DIRECTOR_NAME, movie_director, Field.Store.YES));
            }
        }


        //studio
        doc.add(new TextField(STUDIO_NAME, movie.getElementsByTagName("studio").item(0).getTextContent(), Field.Store.YES));


        //reviews
        NodeList reviews = ((Element) movie.getElementsByTagName("reviews").item(0)).getElementsByTagName("review");
        for (int i = 0; i < reviews.getLength(); i++) {

            Element review = (Element) reviews.item(i);
            //critic
            String rev_critic = review.getElementsByTagName("critic").item(0).getTextContent();
            String rev_date = review.getElementsByTagName("date").item(0).getTextContent();
            String rev_original_score = review.getElementsByTagName("original_score").item(0).getTextContent();
            String rev_freshness = review.getElementsByTagName("freshness").item(0).getTextContent();
            String rev_publication = review.getElementsByTagName("publication").item(0).getTextContent();
            String rev_quote = review.getElementsByTagName("quote").item(0).getTextContent();

            doc.add(new TextField(REVIEW_CRITIC_NAME, rev_critic, Field.Store.YES));
            doc.add(new StringField(REVIEW_DATE, rev_date, Field.Store.NO));
            if (!rev_original_score.equals("-")) {
                doc.add(new StringField(REVIEW_ORIGINAL_SCORE, rev_original_score, Field.Store.NO));
            }
            doc.add(new TextField(REVIEW_FRESHNESS, rev_freshness, Field.Store.YES));
            doc.add(new TextField(REVIEW_PUBLICATION, rev_publication, Field.Store.YES));
            doc.add(new TextField(REVIEW_QUOTE, rev_quote, Field.Store.NO));

        }



        writer.addDocument(doc);

    }

    public static String[] getShearchableFields() {
        
        String[] fields = new String[NUMBER_SEARCHABLE_FIELDS];

        int i = 0;
        fields[i] = MOVIE_TITLE;
        fields[++i] = MOVIE_ID;
        fields[++i] = MOVIE_YEAR;
        fields[++i] = MOVIE_GENRE;
        fields[++i] = MOVIE_MPAA_RATING;
        fields[++i] = MOVIE_RUNTIME;
        fields[++i] = MOVIE_CRITICS_CONSENSUS;
        
        fields[++i] = MOVIE_RATING_CRITICS_RATING;
        fields[++i] = MOVIE_RATING_CRITICS_SCORE;
        fields[++i] = MOVIE_RATING_AUDIENCE_RATING;
        fields[++i] = MOVIE_RATING_AUDIENCE_SCORE;
        
        fields[++i] = MOVIE_SYNOPSIS;
        fields[++i] = ACTOR_ID;
        fields[++i] = ACTOR_NAME;
        fields[++i] = MOVIE_CHARACTER;
        fields[++i] = DIRECTOR_NAME;
        fields[++i] = STUDIO_NAME;
        
        fields[++i] = REVIEW_CRITIC_NAME;
        fields[++i] = REVIEW_DATE;
        fields[++i] = REVIEW_ORIGINAL_SCORE;
        fields[++i] = REVIEW_FRESHNESS;
        fields[++i] = REVIEW_PUBLICATION;
        fields[++i] = REVIEW_QUOTE;


        return fields;

    }
}
