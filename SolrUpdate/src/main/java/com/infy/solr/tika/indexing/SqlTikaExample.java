package com.infy.solr.tika.indexing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.request.JavaBinUpdateRequestCodec.StreamingUpdateHandler;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/* Example class showing the skeleton of using Tika and
Sql on the client to index documents from
both structured documents and a SQL database.

NOTE: The SQL example and the Tika example are entirely orthogonal.
Both are included here to make a
more interesting example, but you can omit either of them.

*/

public class SqlTikaExample {
	private ConcurrentUpdateSolrServer _server;
	private long _start = System.currentTimeMillis();
	private AutoDetectParser _autoParser;
	private int _totalTika = 0;
	private int _totalSql = 0;
	
	private Collection _docs = new ArrayList();
	public static File folder = new File("D:\\expert track\\enterprise search\\testdocs");
	static File file;

	public static void main(String args[]) {
		try {
			SqlTikaExample idxer = new SqlTikaExample("http://localhost:8983/solr/finetikacollection");
			idxer.doTikaDocuments(folder);
			//idxer.doSqlDocuments();
			idxer.endIndexing();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public SqlTikaExample(String url) {
		// Create a multi-threaded communications channel to the Solr server.
	      // Could be CommonsHttpSolrServer as well.
	      // In recent Solr versions, this is ConcurrentUpdateSolrClient
		_server = new ConcurrentUpdateSolrServer(url, 10, 4);
		_server.setSoTimeout(1000); //socket read timeout
		_server.setConnectionTimeout(1000); 
		_server.setParser(new XMLResponseParser());
		
		// One of the ways Tika can be used to attempt to parse arbitrary files.
		_autoParser =  new AutoDetectParser();
		
	}
	
	// Just a convenient place to wrap things up.
	private void endIndexing() {
		
			try {
				if(_docs.size() > 0) { // Are there any documents left over?
				UpdateResponse resp = _server.add(_docs, 300000); // Commit within 5 minutes
				System.out.println("updateresponse code: " + resp.getStatus() + "updateresponse: " +
                        resp.toString());
				}
				_server.commit(); // Only needs to be done at the end,
								// commitWithin should do the rest.
                				// Could even be omitted
								// assuming commitWithin was specified.
				long endTime = System.currentTimeMillis();
				System.out.println(("Total Time Taken: " + (endTime - _start) +
									" milliseconds to index " + _totalSql +
									" SQL rows and " + _totalTika + " documents"));
				} catch (SolrServerException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	}

	private void doTikaDocuments(File root) throws IOException {
		
		// Simple loop for recursively indexing all the files
	    // in the root directory passed in.
		for(File fileEntry : root.listFiles()) {
			if(fileEntry.isDirectory()) {
				doTikaDocuments(fileEntry);
				continue;
			} else {
				file = fileEntry;
				// Get ready to parse the file.
				ContentHandler textHandler = new BodyContentHandler();
				Metadata metadata = new Metadata();
				ParseContext context = new ParseContext();
				// Tim Allison noted the following, thanks Tim!
			      // If you want Tika to parse embedded files (attachments within your .doc or any other embedded 
			      // files), you need to send in the autodetectparser in the parsecontext:
			      // context.set(Parser.class, autoParser);
				
				try {
					
					InputStream input = new FileInputStream(file);
					// Try parsing the file. Note we haven't checked at all to
			        // see whether this file is a good candidate.
					_autoParser.parse(input, textHandler, metadata, context);
					
				} catch (Exception e) {
			          // Needs better logging of what went wrong in order to
			          // track down "bad" documents.
			        try {
						System.out.println(String.format("File %s failed", file.getCanonicalPath()));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			        e.printStackTrace();
			        
			     }
				// Just to show how much meta-data and what form it's in.
				dumpMetaData(file.getCanonicalPath(), metadata);
				
				// Index just a couple of the meta-data fields.
				SolrInputDocument doc = new SolrInputDocument();
				
				doc.addField("id", file.getCanonicalPath());
				
				// Crude way to get known meta-data fields.
			      // Also possible to write a simple loop to examine all the
			      // metadata returned and selectively index it and/or
			      // just get a list of them.
			      // One can also use the LucidWorks field mapping to
			      // accomplish much the same thing.
				String parsedby = metadata.get("X-Parsed-By").toString();
				//metadata.CONTENT_TYPE.get
				System.out.println("parseby: " + parsedby);
				
				if(parsedby != null) {
					doc.addField("parsedby", parsedby);
				}
				
				doc.addField("textfeed", textHandler.toString());
				
				_docs.add(doc);
				++_totalTika;
				
				// Completely arbitrary, just batch up more than one document
			      // for throughput!
				if(_docs.size() >= 1000) {
					//commit within 5 minutes
					try {
						UpdateResponse resp = _server.add(_docs, 300000);
						
						if(resp.getStatus() != 0) {
							System.out.println("Some horrible error has occurred, status is: " +
					                  resp.getStatus());
						} else {
							System.out.println("updateresponse code: " + resp.getStatus() + "updateresponse: " +
						                        resp.toString());
						}
						_docs.clear();
					} catch (SolrServerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
	
		
	}

	private void dumpMetaData(String fileName, Metadata metadata) {
		System.out.println("Dumping metadata for file: " + fileName);
		for(String name : metadata.names()) {
			System.out.println(name + ":" + metadata.get(name));
		}
		System.out.println("nn");
	}
	

}
