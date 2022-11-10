package it.antonio.specializzazionepersonale.dto;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import it.antonio.specializzazionepersonale.model.AnagraficaPersonaleVVFModel;

import static com.mongodb.client.model.Filters.eq;

public class AnagraficaPersonaleVVFDTO {

	static MongoCursor<Document> cursor = null;
	
	//INIT DATABASE
	public static MongoCollection<Document> connectionMongoDB() throws SocketTimeoutException{
				MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
				MongoDatabase database = mongoClient.getDatabase("specializzazionivvf");
				MongoCollection<Document> collection = database.getCollection("anagraficapersonalevvf");
				return collection;
	}
	
	public static long deleteById(String id) throws SocketTimeoutException{
		MongoCollection<Document> collection = connectionMongoDB();

//		System.out.println(id);
        Bson query = eq("_id", new ObjectId(id));
        try {
            DeleteResult result = collection.deleteOne(query);
            return result.getDeletedCount();
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
        }
        return 0;
	}
	
	public static long updateById(String id, Document update) throws SocketTimeoutException{
		MongoCollection<Document> collection = connectionMongoDB();

        Bson query = eq("_id", new ObjectId(id));
        try {
            UpdateResult result = collection.replaceOne(query, update);
            return result.getModifiedCount();
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
        }
        return 0;
	}
	
	//FIND ONE BY NAME WITH PROJECTION OF COLUMNS
	public static List<AnagraficaPersonaleVVFModel> findByName(String nome) throws SocketTimeoutException{
		String jsonString = "";
	        List<AnagraficaPersonaleVVFModel> anagPers = new ArrayList<>();
	        
	        try  {
	        	MongoCollection<Document> collection = connectionMongoDB();

		        /*
		        //FIND
		        Document myDoc = collection.find().first();
		        System.out.println(myDoc.toJson());
		        
		        //FIND
		        Document myDoc2 = collection.find(eq("Nome", "Giuseppe")).first();
		        System.out.println(myDoc2.toJson());*/
		        
		        
		        //FIND ONE BY NAME WITH PROJECTION OF COLUMNS
		        Bson projectionFields = Projections.fields(
	                    Projections.include("qualifica","cognome","nome","ruolo","lista specializzazioni","turno","codiceFiscale","numeroTelefonico","eMail","dataDiInserimento","utenteLoggato")
	                    //,Projections.excludeId()
	                    );
		        
		        		cursor = collection.find(eq("nome", nome))
			                    .projection(projectionFields)
			                    .sort(Sorts.ascending("cognome")).iterator();

		        	 while(cursor.hasNext()) {
	                	jsonString = (cursor.next().toJson());
	                    GsonBuilder gsonBuilder = new GsonBuilder();
	                    Gson gson = gsonBuilder.create();
	                    AnagraficaPersonaleVVFModel myJSONObject = gson.fromJson(jsonString, AnagraficaPersonaleVVFModel.class);
	                    anagPers.add(myJSONObject); 
		        	 }
		        	
			    	/*while (cursor.hasNext()) {
			    		JSONObject jsonObject =  new JSONObject(cursor.next().toJson());
			    		jarray.put(jsonObject);
			    	}*/
		        	/*String app1 = "[" + jsonStringCollection.replaceFirst(",", "")+ "]";

		            Gson g = new Gson();  
			        AnagraficaPersonaleVVFModel jsonAnagrafica = g.fromJson(app, AnagraficaPersonaleVVFModel.class);
			        JSONArray array = new JSONArray(app1); 
			        JSONObject jsn = new JSONObject();
			        List<AnagraficaPersonaleVVFModel> data = new ArrayList<>();
			        for(int i=0; i < array.length(); i++)   
			        {
			        	jsn = array.getJSONObject(i);
				        data.add(new AnagraficaPersonaleVVFModel("1","2","3","4","5","6","7","8","9"));
				        //data.add(jsn.getString("cognome").toString());
			        } */
		        	

		        	 
		            	/*AnagraficaPersonaleVVFModel person = new AnagraficaPersonaleVVFModel("","","","","","","","","");
			            person.setQualifica(jsonObject.getString("qualifica"));
			            person.setCognome(jsonObject.getString("cognome"));
			        	person.setNome(jsonObject.getString("nome"));
			            person.setRuolo(jsonObject.getString("ruolo"));
			            person.setListaSpecializzazioni(jsonObject.getString("lista specializzazioni"));
			            person.setTurno(jsonObject.getString("turno"));
			            person.setCodiceFiscale(jsonObject.getString("codice fiscale"));
			            person.setNumeroTelefonico(jsonObject.getString("numero telefonico"));
			            person.seteMail(jsonObject.getString("e-mail"));
			            persons.add(person);*/
			            
	            }
	        	catch(Exception ex) {
	        		System.out.println("Exception caught in catch block" + ex);
	        	}    
	        	finally {
	                cursor.close();
	            }
	        
	        
	        
	        //String app1 = "[" + jsonStringCollection.replaceFirst(",", "")+ "]";
	        //return data;
	        return anagPers;
	        //return jarray;
	        //return persons;
	    }
	
	//FIND ONE
	public static List<AnagraficaPersonaleVVFModel> findAll() throws SocketTimeoutException{
		String jsonString = "";
	        List<AnagraficaPersonaleVVFModel> anagPers = new ArrayList<>();
	        
	        try  {
	        	MongoCollection<Document> collection = connectionMongoDB();
	
		        	cursor = collection.find().sort(Sorts.ascending("cognome")).iterator();
	        		Projections.computed("$toUpper", "$nome");
	        		Projections.computed("$toUpper", "$cognome");
	        		
		        	 while(cursor.hasNext()) {
		        		Document current = cursor.next();
	                	jsonString = current.toJson();
	                    GsonBuilder gsonBuilder = new GsonBuilder();
	                    Gson gson = gsonBuilder.create();
	                    AnagraficaPersonaleVVFModel myJSONObject = gson.fromJson(jsonString, AnagraficaPersonaleVVFModel.class);
	                    myJSONObject.set_id(current.getObjectId("_id"));
	                    anagPers.add(myJSONObject); 
		        	 }
	            }
	        	catch(Exception ex) {
	        		System.out.println("Exception caught in catch block" + ex);
	        	}    
	        	finally {
	                cursor.close();
	            }
	        return anagPers;
	    }
	
	
	//INSERT ONE
	public static Boolean insertPerson(AnagraficaPersonaleVVFModel anagraficaModel) throws SocketTimeoutException{
		Boolean esito = false;
		 try  {
			 /*CUSTOM EXCEPTION
			  * if (anagraficaModel.getCognome().equals("a")) {
			        throw new CustomException("Incorrect cognome : " + anagraficaModel.getCognome(), null );
			    }*/
			 
	        	MongoCollection<Document> collection = connectionMongoDB();
				 //INSERT DOCUMENT
	            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	            //Date date = new Date();  
	            String dataStr = formatter.format(new Date());
	            	        	
	        	anagraficaModel.setDataDiInserimento(dataStr);
	        	anagraficaModel.setUtenteLoggato("Antonio"); //Prendere dalla parte di login utente
	        	
		        Document doc = new Document(
		        				"qualifica", anagraficaModel.getQualifica())
		                .append("cognome", anagraficaModel.getCognome())
		                .append("nome", anagraficaModel.getNome())
		                .append("ruolo", anagraficaModel.getRuolo())
		                .append("turno", anagraficaModel.getTurno())
		                .append("codiceFiscale", anagraficaModel.getCodiceFiscale())
		                .append("numeroTelefonico", anagraficaModel.getNumeroTelefonico())
		                .append("eMail", anagraficaModel.geteMail())
		                .append("dataDiInserimento", anagraficaModel.getDataDiInserimento())
		                .append("utenteLoggato", anagraficaModel.getUtenteLoggato());
		        collection.insertOne(doc);
		       
		        //Document document = new Document();
		        //document.put("user", Document.parse(new ObjectMapper().writeValueAsString(anagraficaModel)));
		        
		        esito = true;
		 }
		 catch(Exception ex)
		 		{System.out.println("Exception caught in catch block" + ex);
		 		esito = false;
		 		}
//		 finally 
//		 		{cursor.close();}
		 
		return esito;
	}
	
	//DELETE ONE
	public static Boolean removePerson(AnagraficaPersonaleVVFModel anagraficaModel) throws SocketTimeoutException{
		Boolean esito = false;

		try {
        	    MongoCollection<Document> collection = connectionMongoDB();
        	    String idPersona = anagraficaModel.get_id().toString(); 
			    collection.deleteOne(new Document("_id", new ObjectId(idPersona)));
			    esito = true;
            } catch (MongoException me) {
            	esito = false;
                System.err.println("Impossibile cancellare il documento: " + me);
            }
//			 finally 
//		 		{cursor.close();}
		
		return esito;
	}
	
}