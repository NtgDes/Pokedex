package android.pokedex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiData {
	public static final String
			APIUrl = "https://pokeapi.co/api/v2/",
			SpriteURL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";

	callBackPokemonList callBackResponseList;
	callBackPokemonBitmap callBackResponseBitmap;
	callBackPokemonData callBackResponseData;
	public ApiData(callBackPokemonList response) {
		callBackResponseList =response;
		new GetList().execute();
	}
	public ApiData(String spriteURL,callBackPokemonBitmap response) {
		callBackResponseBitmap =response;
		new GetImage(spriteURL).execute();
	}
	public ApiData(String Name,int ID,callBackPokemonData response) {
		callBackResponseData =response;
		new GetData(Name==null?""+ID:Name).execute();
	}

	private class GetList extends AsyncTask<Void, String, String> {
		@Override
		protected String doInBackground(Void... voids) {

			try {
				return ApIResponse(APIUrl+"pokemon/?limit=2000");
			} catch (Exception e) {
				e.printStackTrace();
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

						//--
						pokemon.SpriteURL=SpriteURL+pokemon.ID+".png";

						//--

						pokemons.add(pokemon);
					}
					callBackResponseList.List(pokemons);
				}
			} catch (JSONException e) {
				e.printStackTrace();
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

	private class GetData extends AsyncTask<Void, String, String> {
		String nameID;
		public GetData(String name) {
			nameID=name;
		}

		@Override
		protected String doInBackground(Void... voids) {

			try {
				return ApIResponse(APIUrl+"pokemon/"+nameID);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String response) {
			if (response==null) return;
			try {
				Pokemon pokemon= new Pokemon();
				JSONObject JsonPokemon=new JSONObject(response);

				pokemon.ID= JsonPokemon.getInt("id");
				pokemon.Name=JsonPokemon.getString("name");

				pokemon.Height=JsonPokemon.getInt("height");
				pokemon.Weight=JsonPokemon.getInt("weight");

				pokemon.Species=JsonPokemon.getJSONObject("species").getString("name");

				pokemon.TypeCount=JsonPokemon.getJSONArray("types").length();
				pokemon.Type=JsonPokemon.getJSONArray("types").getJSONObject(0).getJSONObject("type").getString("name");

				pokemon.HP=JsonPokemon.getJSONArray("stats").getJSONObject(0).getInt("base_stat");
				pokemon.Attack=JsonPokemon.getJSONArray("stats").getJSONObject(1).getInt("base_stat");
				pokemon.Defense=JsonPokemon.getJSONArray("stats").getJSONObject(2).getInt("base_stat");
				pokemon.specialAttack=JsonPokemon.getJSONArray("stats").getJSONObject(3).getInt("base_stat");
				pokemon.SpecialDefense=JsonPokemon.getJSONArray("stats").getJSONObject(4).getInt("base_stat");
				pokemon.Speed=JsonPokemon.getJSONArray("stats").getJSONObject(5).getInt("base_stat");
				callBackResponseData.Profile(pokemon);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}
	public interface callBackPokemonData {void Profile(Pokemon pokemon);}

	private String ApIResponse(String Url) throws IOException {
		HttpURLConnection urlConnection = (HttpURLConnection) new URL(Url).openConnection();

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
	}
}
