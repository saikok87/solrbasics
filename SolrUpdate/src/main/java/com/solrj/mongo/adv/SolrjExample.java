package com.solrj.mongo.adv;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class SolrjExample {
	
	String solrURL = "http://localhost:8983/solr/high2collection";
	
	public static void main(String[] args) {
		new SolrjExample().execute();
	}

	private void execute() {
		System.out.println("Starting off " + this.getClass().toString());
		SolrDao<Users> solrDao = new SolrDao<Users> (solrURL);
		
		readMongoAndIndexSolr(solrDao); // read mongo data and add it to solr Index
		//readHighLightedPage(solrDao); // read solr searched result in highlighted format + pagination
		 
	    //addDocuments (solrDao);
        readDocuments (solrDao); // to read useres in sorted order of id
        
        // addUserss (solrDao);
        // readUserss (solrDao); 
		
	}

	

	private void readMongoAndIndexSolr(SolrDao<Users> solrDao) {
		try {
			MongoClient client = new MongoClient("localhost",27017);
			
			DB db = client.getDB("testdb");
			
			DBCollection table = db.getCollection("user");
			
			BasicDBObject searchQuery = new BasicDBObject();
			//searchQuery.put("name", "sai");
			
			DBCursor cursor = table.find();
			
			Collection<Users> users = new ArrayList<Users>();
			
			while(cursor.hasNext()) {
				BasicDBObject record = (BasicDBObject) cursor.next(); 
				System.out.println("Mongo record: " + record.toString());
				
				users.add(new Users(record.get("_id").toString(), record.get("name").toString(), record.get("age").toString(), 
						record.get("createdDate").toString()));
				solrDao.put(users);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void readUserss(SolrDao<Users> solrDao) {
		QueryResponse rsp = solrDao.readAll();
		List<Users> beans = rsp.getBeans(Users.class);
		for(Users bean: beans){
			System.out.println("Read Users " + bean.getId() + ", name = " + bean.getName());
		}
		
	}

	/*private void addUserss(SolrDao<Users> solrDao) {
		Collection<Users> users = new ArrayList<Users> (3);
		users.add(new Users("1", "Users 1"));
		users.add(new Users("2", "Users 2"));
		users.add(new Users("3", "Users 3"));
		solrDao.put(Userss);
	}*/

	private void readDocuments(SolrDao<Users> solrDao) {
		SolrDocumentList docs = solrDao.readAllDocs();
		Iterator<SolrDocument> itr = docs.iterator();
		int count=10;
		
		while(itr.hasNext() && count-- >0){
			SolrDocument resultDoc = itr.next();
			
			String content = (String) resultDoc.getFieldValue("content");
			String id = (String) resultDoc.getFieldValue("id"); //unique field
			System.out.println ("Read " + resultDoc + " with id = " + id + " and content = " + content);
		}
		
	}
	
	private void readHighLightedPage(SolrDao<Users> solrDao) {
		try {
			List<Users> usersList = solrDao.testHighlightAndPagination(1,14); // passing pageNum=1 and no. of rows/page=14
			for(Users user : usersList) {
				System.out.println("id: " + user.getId());
				System.out.println("name: " + user.getName());
				System.out.println("age: " + user.getAge());
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private void addDocuments(SolrDao<Users> solrDao) {
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		for(int i=0; i<1000; i++)
			docs.add(getRandomSolrDoc(i));
		
		solrDao.putDoc(docs);
	}

	private SolrInputDocument getRandomSolrDoc(int count) {
		SolrInputDocument doc =  new SolrInputDocument();
		doc.addField("id", "id"+count);
		doc.addField("name", "doc"+count);
		
		return doc;
	}
	
	
	

}
