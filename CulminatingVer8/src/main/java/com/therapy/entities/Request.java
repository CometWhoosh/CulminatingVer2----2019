package com.therapy.entities;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * This class represents a request that a patient would send to a therapist in 
 * order to be accepted as one of their patients. It has fields for the patient
 * and therapist that are associated with the request, the status of the request
 * (if it is accepted or not), and the summary written by the sender 
 * (the patient) describing their situation.
 *
 * @author Yousef Bulbulia
 * 
 */
public class Request extends Entity{
	
	Patient patient;
	Therapist therapist;
	boolean isAccepted;
	String summary;
	
	
	public Request(Patient patient, Therapist therapist, ObjectId id, MongoDatabase database) {
	    	
	    	super(id, database);
	    	
	    	this.patient = patient;
	    	this.therapist = therapist;
	    	
	    	MongoCollection<Document> collection = database.getCollection("requests");
	    	Document doc = collection.find(eq(id)).first();
	    	
	    	isAccepted = doc.getBoolean("is_accepted", false);
	    	
	        summary = doc.getString("summary");
    	
    }
	
	
    
    /**
     * 
     * @return the patient associated with the request.
     */
    public Patient getPatient() {
        return patient;
    }
    
    /**
     * 
     * @return the therapist associated with the request.
     */
    public Therapist getTherapist() {
        return therapist;
    }
    
    /**
     * 
     * @return the summary associated with the request.
     */
    public String getSummary() {
        return summary;
    }
    
    /**
     * 
     * @return a boolean value which, if <code>true</code>, indicates that the 
     *  request is accepted.
     */
    public boolean isAccepted() {
        return isAccepted;
    }
    
    public void setID(ObjectId id) {
    	this.id = id;
    }
    
    /**
     * 
     * @param patient the patient to be associated with the request.
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    /**
     * 
     * @param therapist the therapist to be associated with the request.
     */
    public void setTherapist(Therapist therapist) {
        this.therapist = therapist;
    }
    
    /**
     * 
     * @param summary the summary to be associated with the request.
     */
    public void setSummary(String summary) {
       this.summary = summary;
    }
    
    /**
     * Accepts the request. Should only be used by a <code>Therapist</code> 
     * object.
     */
    void accept() throws IllegalStateException {
        isAccepted = true;
    }
    
    public void insertIntoCollection() throws MongoWriteException, MongoWriteConcernException, MongoException {
    	
    	MongoCollection<Document> collection = database.getCollection("requests");
    	
    	Document doc = new Document("_id", id)
    			.append("patient_id", patient.getId())
    			.append("therapist_id", therapist.getId())
    			.append("is_accepted", isAccepted)
    			.append("summary", summary);
    	
    	collection.insertOne(doc);
    	
    }
    
    public void updateCollection() throws MongoWriteException, MongoWriteConcernException, MongoException {
    	
    	MongoCollection<Document> collection = database.getCollection("requests");
    	
    	Document doc = new Document("_id", id)
    			.append("patient_id", patient.getId())
    			.append("therapist_id", therapist.getId())
    			.append("is_accepted", isAccepted)
    			.append("summary", summary);
    	
    	collection.findOneAndUpdate(eq(id), doc);
    	
    }
    
    
	

}
