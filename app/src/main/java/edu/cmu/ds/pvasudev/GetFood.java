package edu.cmu.ds.pvasudev;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author: Pooja Vasudevan
 * Last Modified: Apr 5, 2020
 *
 * This class provides capabilities to search for relevant nutritional facts based on user's entered search string.  The method "search" is the entry to the class.
 * Network operations cannot be done from the UI thread, therefore this class makes use of an AsyncTask inner class that will do the network
 * operations in a separate worker thread.  The UI updates are done with onPostExecution()  by calling textReady() method.
 *
 **/
public class GetFood {
	FoodSearch ip = null;
	/**
	 * search is the public GetFood method.  Its arguments are the search term and FoodSearch object.  This provides a callback
	 * path such that the textReady method in that object is called when the picture is available from the search.
	 * @param searchTerm a search input string from User and FoodSearch object
	 * @param ip FoodSearch object
	 * @void
	 */
	public void search(String searchTerm, FoodSearch ip) {
		this.ip = ip;
		new AsyncFlickrSearch().execute(searchTerm);
	}

	/*
	 * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
	 * doInBackground is run in the helper thread.
	 * onPostExecute is run in the UI thread, allowing for safe UI updates.
	 */
    private class AsyncFlickrSearch extends AsyncTask<String, Void, String> {
        protected String doInBackground(String ... searchTerm) {
            return search(searchTerm[0]);
        }

        protected void onPostExecute(String result) {
			try {
				ip.textReady(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		/**
		 * This methods takes the user search string and hits the heroku service URL through a GET request, which makes a call to
		 * the API, and return a string formatted JSON object of the relevant nutrient facts.
		 * @return string JSON Object of nutrient facts
		 */
        private String search(String searchTerm) {

        	String finalResult="";
			try {


				URL url = new URL("https://strawberry-pudding-72358.herokuapp.com/getFood?searchWord="+searchTerm);
				//URL url = new URL("https://api.edamam.com/api/food-database/parser?ingr="+foodItem+"&app_id=a6ec1a32&app_key=adbce3f99105b170f0a3cabcb2bb3848");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}


				String inline="";

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				while ((output = br.readLine()) != null) {
					inline+=output;
				}


				JSONObject obj = new JSONObject(inline);
				if(obj.length()!=0){
					finalResult=obj.toString();
				}


				conn.disconnect();



			} catch (MalformedURLException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return finalResult;
		}


    }
}