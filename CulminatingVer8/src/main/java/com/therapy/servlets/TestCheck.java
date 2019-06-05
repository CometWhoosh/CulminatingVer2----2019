package com.therapy.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@WebServlet("/testCheck")
public class TestCheck extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		//Connect
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
		
		
		MongoDatabase database = mongoClient.getDatabase("testDB");
		MongoCollection<Document> collection = database.getCollection("testCollection");
		
		FindIterable<Document> findIterable = collection.find(Filters.eq("Hello!"));
		
		MongoCursor<Document> cursor = findIterable.iterator();
		
		Document document = cursor.next();
		
		cursor.close();
		
		String output = document.getString("_id");
		
		response.setContentType("text/html");
		// Allocate a output writer to write the response message into the network socket
		PrintWriter out = response.getWriter();
	 
		// Write the response message, in an HTML page
		out.println("<html>");
		out.println("<head><title>Hello, World</title></head>");
		out.println("<body>");
		out.println("<p>output: " + output + "</p>");
		out.println("</body></html>");
	    out.close();  // Always close the output writer
		
		
		//close
		mongoClient.close();
		
	}
	
}
