package eu.larkc.csparql.core.general_test;

import java.text.ParseException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.Syntax;

import eu.larkc.csparql.cep.api.RdfStream;
import eu.larkc.csparql.core.engine.CsparqlEngine;
import eu.larkc.csparql.core.engine.CsparqlEngineImpl;
import eu.larkc.csparql.core.engine.CsparqlQueryResultProxy;

public class Static_data_management {

	public static void main(String[] args) {

		CsparqlEngine engine = new CsparqlEngineImpl();
		engine.initialize();

		RdfStream stream = new Static_Knowledge_Test_Streamer("http://myexample.org/stream");

		String query = "REGISTER QUERY test AS " +
				"PREFIX ex:<http://streamreasoning.org#> " +
				"SELECT * " +
				"FROM STREAM <http://myexample.org/stream> [RANGE 5s STEP 5s] "	+ 
				"FROM <http://127.0.0.1/~baldo/StaticKnowledgeTest.rdf> " +
				"WHERE { " +
				"?w1 ex:isIn ?r1 . " +
				"?w2 ex:isIn ?r2 . " +
				"?r1 ex:contiguous ?r2 . " +
				"FILTER(?w1 != ?w2 && ?r1 != ?r2) " +
				"}";
		
//		String query = "REGISTER QUERY test AS " +
//				"PREFIX ex:<http://streamreasoning.org#> " +
//				"SELECT ?s ?p ?o " +
//				"FROM STREAM <http://myexample.org/stream> [RANGE 5s STEP 5s] "	+ 
//				"WHERE { " +
//				"?s ?p ?o . " +
//				"}";
		
		engine.registerStream(stream);
		CsparqlQueryResultProxy c1 = null;

		try {
			c1 = engine.registerQuery(query);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (c1 != null) {
			c1.addObserver(new ConsoleFormatter());
		}

		new Thread((Runnable) stream).start();
		
//		String query = "SELECT ?s ?p ?o WHERE { ?s ?p ?o }";
//		
//		Query q = QueryFactory.create(query, Syntax.syntaxSPARQL_11);
//		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ds/query", q);
//		
//		ResultSet rs = qexec.execSelect();
//		
//		System.out.println(ResultSetFormatter.asText(rs));

	}

}
