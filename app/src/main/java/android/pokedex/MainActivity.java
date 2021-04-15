package android.pokedex;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	List<Pokemon> pokemons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);



		RecyclerView recyclerView=findViewById(R.id.recyclerView);


		recyclerView.setLayoutManager(new GridLayoutManager(this,2));
		//new GridLayoutManager(this,2)

		pokemons= new ArrayList<>();
		pokemons.add(new Pokemon());
		pokemons.add(new Pokemon());
		pokemons.add(new Pokemon());


		recyclerView.setAdapter(new RecycleViewAdaptor(pokemons));

	}


	class RecycleViewAdaptor extends RecyclerView.Adapter<RecycleViewAdaptor.PokemonView>{

		public RecycleViewAdaptor(List<Pokemon> pokemons) {
		}

		class PokemonView extends RecyclerView.ViewHolder{


			public PokemonView(@NonNull View itemView) {
				super(itemView);
			}
		}


		@NonNull
		@Override
		public PokemonView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cardview, parent, false);
			PokemonView myViewHolder = new PokemonView(view);

			return myViewHolder;
		}

		@Override
		public void onBindViewHolder(@NonNull PokemonView holder, int position) {

		}


		@Override
		public int getItemCount() {
			return 5;
		}
	}







}