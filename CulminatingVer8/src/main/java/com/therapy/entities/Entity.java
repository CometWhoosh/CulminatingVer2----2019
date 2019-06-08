package com.therapy.entities;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public abstract class Entity {

	protected ObjectId id;
	protected MongoCollection<Document> collection;
	protected MongoDatabase database;
	
	/**
	 * Creates a new <code>Entity</code>.
	 */
	public Entity(MongoDatabase database) {
		this.database = database;
	}
	
	/**
	 * Creates a new <code>Entity</code> belonging to the passed
	 * database with the passed id.
	 * 
	 * @param id       the id of this <code>Entity</code>.
	 * @param database the database this <code>Entity</code> belongs to.
	 */
	public Entity(ObjectId id, MongoDatabase database) {
		this.database = database;
		this.id = id;
	}
	
	/**
	 * 
	 * @return the id of the <code>Entity</code>.
	 */
	protected ObjectId getId() {
		return id;
	}
	
	/**
	 * 
	 * @return a <code>Document</code> representing this <code>Entity</code>
	 */
	public Document getDocument() {
    	return collection.find(eq(id)).first();
    }
	
	public ObjectId getUniqueId() {
		
		boolean isDuplicate = false;
		ObjectId id = null;
		
		do {
			
			isDuplicate = false;
			id = ObjectId.get();
			
			/*
			 * Try to insert the chat into the collection. If the id 
			 * already belongs to another chat in the collection, then
			 * make a new id and try again.
			 */
			try {
				collection.insertOne(new Document("_id", id));
			} catch(MongoWriteException e) {
				if(e.getCode() == 11000) {
					isDuplicate = true;
				} else {
					throw e;
				}
			}
			
		} while(isDuplicate);
		
		return id;
		
	}
	
}
