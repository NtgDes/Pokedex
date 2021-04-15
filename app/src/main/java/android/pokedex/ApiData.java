package android.pokedex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiData {
	public static final String APIUrl = "https://pokeapi.co/api/v2/";
	callBackPokemonList callBackResponseList;
	callBackPokemonBitmap callBackResponseBitmap;
	public ApiData(callBackPokemonList response) {
		callBackResponseList =response;
		new GetData().execute();
	}
	public ApiData(String spriteURL,callBackPokemonBitmap response) {
		callBackResponseBitmap =response;
		new GetImage(spriteURL).execute();
	}


	private class GetData extends AsyncTask<Void, String, String> {


		@Override
		protected String doInBackground(Void... voids) {

			try {
				HttpURLConnection urlConnection = (HttpURLConnection) new URL(APIUrl+"pokemon/?limit=2000").openConnection();


				urlConnection.setRequestMethod("GET");

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
				JSONArray JsonList=new JSONObject(response).getJSONArray("results");
				if(JsonList.length()>0) {
					List<Pokemon> pokemons= new ArrayList<>();
					for (int i = 0; i < JsonList.length(); i++) {
						Pokemon pokemon=new Pokemon();

						pokemon.ID=i+1;
						pokemon.Name=JsonList.getJSONObject(i).getString("name");
						pokemon.DataUrl=JsonList.getJSONObject(i).getString("url");

						//--todo resolve pokeApi get image
						pokemon.SpriteURL="http://floatzel.net/pokemon/black-white/sprites/images/shiny/"+pokemon.ID+".png";

						//--

						pokemons.add(pokemon);
					}
					callBackResponseList.List(pokemons);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return;
			}

		}
	}
	public interface callBackPokemonList {void  List(List<Pokemon> pokemons);}

	private class GetImage extends  AsyncTask<Void,String, Bitmap>{
		String spriteURL;
		public GetImage(String SpriteURL) {
			spriteURL=SpriteURL;
		}

		@Override
		protected Bitmap doInBackground(Void... voids) {
			try {
				return BitmapFactory.decodeStream(new URL(spriteURL).openStream());
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			callBackResponseBitmap.bitmap(bitmap);
		}
	}
	public interface callBackPokemonBitmap {void  bitmap(Bitmap sprite);}

}
