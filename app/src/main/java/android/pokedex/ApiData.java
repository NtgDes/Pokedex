package android.pokedex;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiData {
	public static final String APIUrl = "https://pokeapi.co/api/v2/";

	public void getList(){
		new GetData().execute();



	}



	static private class GetData extends AsyncTask<Void, String, String> {


		@Override
		protected String doInBackground(Void... voids) {

			try {
				HttpURLConnection urlConnection = (HttpURLConnection) new URL(APIUrl).openConnection();

				//publishProgress("Connecting", "10");
				//urlConnection.setDoOutput(true);urlConnection.setDoInput(true);
				urlConnection.setRequestMethod("GET");
				//urlConnection.setConnectTimeout(20000);
				//urlConnection.setReadTimeout(15000);

				//OutputStream OptS = urlConnection.getOutputStream();


				if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					urlConnection.disconnect();
					return null;
				}

				BufferedReader bufferedReaderIN = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				StringBuilder response = new StringBuilder();
				String line;

				while ((line = bufferedReaderIN.readLine()) != null) {
					response.append(line);
				}
				bufferedReaderIN.close();
				urlConnection.disconnect();

				return response.toString();

			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String response) {
			if (response==null) return;


			try {
				JSONObject List = new JSONObject(response);










			} catch (JSONException e) {
				return;
			}
		}
	}
}
