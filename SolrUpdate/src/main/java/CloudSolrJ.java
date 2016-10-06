import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;

public class CloudSolrJ {
	public static void main(String args[]) throws SolrServerException,
			IOException {
		CloudSolrServer server = new CloudSolrServer("localhost:9983");
		server.setDefaultCollection("querywordstop1collection");
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "1");
		doc.addField("rName", "Solr and all the others");
		doc.addField("rDescription", "This is about Solr");
		server.add(doc);
		server.commit();
	}
}
