import org.apache.solr.client.solrj.SolrQuery;
//import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class httpSolrJ {

	private static final String urlString = "http://localhost:8983/solr/tweetcollection";
	private HttpSolrServer solrServer;

	public httpSolrJ() {
		if (solrServer == null) {
			solrServer = new HttpSolrServer(urlString);
		}
	}

	public void deleteByQuery(String queryString) {
		try {
			solrServer.deleteByQuery(queryString);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addDocumentTest() {

		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "2");
		doc.addField("nameAuto", "Second Solr 4.0 CookBook");
		
		
		try {
			solrServer.add(doc);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public QueryResponse getRueryResponse(String queryString) {
		SolrQuery query = new SolrQuery();
		query.setQuery(queryString);
		QueryResponse queryResponse = null;
		try {
			queryResponse = solrServer.query(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queryResponse;
	}
	
	//autocomplete on product names
	private QueryResponse getAutoCompleteResponse(String queryString) {
		SolrQuery query =  new SolrQuery();
		query.setQuery(queryString);
		query.set("op", "AND");
		query.setRows(0);
		query.setFacet(true);
		query.addFacetField("nameAuto_show");
		query.setFacetLimit(5);
		//query.setFacetMinCount(1); //creating issues so commented
		
		QueryResponse qResp = null;
		
		try {
			qResp = solrServer.query(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return qResp;
	}
	
	//autocomplete on category of products
	private QueryResponse getAutoCompleteCategoryResponse(String queryString) {
		SolrQuery query = new SolrQuery();
		query.setQuery(queryString);
		query.setRows(0);
		query.setFacet(true);
		query.addFacetField("category");
		query.setFacetLimit(5);
		query.setFacetPrefix("boo");
		
		QueryResponse qResp = null;
		
		try {
			qResp = solrServer.query(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return qResp;
	}
	
	//using diff. query parsers in single query
	private QueryResponse getQueryParserResponse(String queryString) {
		SolrQuery query =  new SolrQuery();
		query.setQuery(queryString);
		query.set("defType","edismax");
		query.addFilterQuery("{!term f=category}Books And Tutorials");
		
		QueryResponse qResp = null;
		
		try {
			qResp = solrServer.query(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return qResp;
	}
	
	//get realtime response for just indexed docs
	private QueryResponse getRealTimeResponse(String queryStr) {
		SolrQuery query = new SolrQuery();
		query.setQuery(queryStr);
		query.setRequestHandler("/select");
		
		QueryResponse resp = null;
		
		try {
			resp = solrServer.query(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resp;
	}
	
	// to get the docs with all query words at top of result set
	private QueryResponse getQueryWordsAtTopResultSetTimeResponse(String queryStr) {
		
		System.out.println("inputval: " + queryStr);
		SolrQuery query =  new SolrQuery();
		query.setRequestHandler("/better");
		//query.setFields("mainQuery", queryStr.trim());
		query.setParam("mainQuery", queryStr.trim());
		
		System.out.println("query: " + query.toString());
		
		QueryResponse resp = null;
		
		try {
			resp = solrServer.query(query);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resp;
	}
	
	public static void main(String[] args) {
		// Go to http://wiki.apache.org/solr/Solrj to look up various other
		// SolrJ APIs
		httpSolrJ solrJ = new httpSolrJ();
		
		BufferedReader br = null;
		String input = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			
			
				System.out.println("Enter Search Input: ");
				input = br.readLine();
				
				System.out.println("Input: " + input);
				System.out.println("-----------------\n");
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		
		//solrJ.addDocumentTest();
		
		//solrJ.deleteByQuery("id:7890");
		//solrJ.deleteByQuery("*:*");
		
		QueryResponse response = solrJ.getRueryResponse("*:*");
		
		//auto complete functionality     <<< real-life
		//QueryResponse response = solrJ.getAutoCompleteResponse("nameAuto_autocomplete:sol"); //success
		//QueryResponse response = solrJ.getAutoCompleteCategoryResponse("*:*"); //success
		//QueryResponse response = solrJ.getQueryParserResponse("*:*"); //unsuccessful
		//QueryResponse response = solrJ.getRealTimeResponse("id:1");
		//QueryResponse response = solrJ.getQueryWordsAtTopResultSetTimeResponse(input); // to get the docs with all query words at top of result set
		
		//QueryResponse response = solrJ.getRueryResponse("nameAuto:sol");
		
		System.out.println("SolrJ 61 response =  " + response);
		
	}

	

}

