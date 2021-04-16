package android.pokedex;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	RecyclerView recyclerView;
	List<Pokemon> Pokemons;
	EditText txtSearch;
	ImageButton btnClear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		recyclerView=findViewById(R.id.recyclerView);
		recyclerView.setLayoutManager(new GridLayoutManager(this,2));

		Button btnSearch =findViewById(R.id.btnSearch); //Used to search for pokemon 'Name'
		btnClear=findViewById(R.id.btnClear);
		txtSearch=findViewById(R.id.txtSearch);

		//connect to the PokeAPI site and GET a list of pokemons,
		new ApiData(pokemons -> {
			if(pokemons==null)return; //make sure the pokemons has data to display
			Pokemons =pokemons;
			recyclerView.setAdapter(new RecycleViewAdaptor(Pokemons));//display List

		});

		//search for search-text from pokemons name
		btnSearch.setOnClickListener(view -> {
			if(Pokemons ==null)return; //make sure the pokemons has data to search
			List<Pokemon> Search= new ArrayList<>();

			// loop through the data only adding pokemon that match (partial match also) entered text
			for (int i = 0; i < Pokemons.size() ; i++) {
				if (Pokemons.get(i).Name.toLowerCase().contains(txtSearch.getText().toString().trim().toLowerCase()))//lower case remove case sensitivity
					Search.add(Pokemons.get(i));
			}

			recyclerView.setAdapter(new RecycleViewAdaptor(Search));//display result
		});

		//clear search text
		btnClear.setOnClickListener(view -> {
			txtSearch.setText("");
			ResetSearch();
		});

		/*search for search-text from pokemons name that start with
		  eg press 'A' to search all that start with 'A'*/
		txtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				//hide or show clear button depending if search text is available
				if(txtSearch.getText().toString().trim().isEmpty()){
					ResetSearch();// reset search results
				}else{
					btnClear.setVisibility(View.VISIBLE);
					searchTimer+=2;//add a delay in case user is still typing
					SearchDelay();
				}
			}
		});

	}
	//reset all to the state before search
	void ResetSearch() {
		recyclerView.setAdapter(new RecycleViewAdaptor(Pokemons));
		btnClear.setVisibility(View.INVISIBLE);
		searchTimer=-1;
	}
	int searchTimer=-1;//set delay to nothing
	void SearchDelay(){

		new Handler().postDelayed(() -> {
			if (searchTimer == 0) { // search for pokemons when =0
				searchTimer = -1;//stop future search attempts
				if (Pokemons == null) return;
				List<Pokemon> Search = new ArrayList<>();
				//search for pokemons that start with search text
				for (int i = 0; i < Pokemons.size(); i++) {
					if (Pokemons.get(i).Name.toLowerCase().startsWith(txtSearch.getText().toString().trim().toLowerCase()))
						Search.add(Pokemons.get(i));
				}
				recyclerView.setAdapter(new RecycleViewAdaptor(Search));//display result
			} else if (searchTimer > 0) {
				searchTimer--;//reduce search wait time;
				SearchDelay();
			}
		}, 1000);//Search delay time
	}

	class RecycleViewAdaptor extends RecyclerView.Adapter<RecycleViewAdaptor.PokemonView>{
		List<Pokemon> pokemons;
		public RecycleViewAdaptor(List<Pokemon> Pokemons) {
			pokemons=Pokemons;
		}

		class PokemonView extends RecyclerView.ViewHolder{
			CardView PokemonCard;
			TextView txtID, txtName;
			ImageView imgProfile;

			public PokemonView(@NonNull View itemView) {
				super(itemView);
				PokemonCard=itemView.findViewById(R.id.cvPokemon);
				txtID =itemView.findViewById(R.id.txtID);
				txtName=itemView.findViewById(R.id.txtName);
				imgProfile =itemView.findViewById(R.id.imgProfile);
			}
		}
		@NonNull
		@Override
		public PokemonView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cardview, parent, false);

			return new PokemonView(view);
		}

		@Override
		public void onBindViewHolder(@NonNull PokemonView holder, int position) {
			//set Card-view to show given pokemon data
			holder.txtName.setText(pokemons.get(position).Name);
			holder.txtID.setText("#"+pokemons.get(position).ID);

			if (pokemons.get(position).Sprite==null){//if no image was loaded it send request for image from site

				//connect to the site and GET pokemon sprite
				new ApiData(pokemons.get(position).SpriteURL, sprite -> {
					if(sprite==null)
						return;
					pokemons.get(position).Sprite=sprite;//store sprite to be use if needed later (Sprite cache)

					holder.imgProfile.setImageBitmap(pokemons.get(position).Sprite);//displays sprite

				});
				holder.imgProfile.setImageResource(R.drawable.pokeball);//displays sprite place holder
			}else
				holder.imgProfile.setImageBitmap(pokemons.get(position).Sprite);//displays cached sprite

			holder.PokemonCard.setOnClickListener(view -> {

				// setup for detailed view
				DetailedView PokemonStats = new DetailedView();
				PokemonStats.PokemonData=pokemons.get(position);
				PokemonStats.show(getSupportFragmentManager(),null);

				if(pokemons.get(position).Type==null)//if no detailed data of pokemon was loaded it send request for data from PokeAPI site
					new ApiData(pokemons.get(position).Name, 0, pokemon -> {
						pokemon.Sprite=pokemons.get(position).Sprite;//preserve sprite data
						pokemon.SpriteURL=pokemons.get(position).SpriteURL;//? May no longer be necessary to preserve since bitmap available in var sprite
						pokemons.set(position,pokemon);

						PokemonStats.RefreshData(pokemons.get(position));//update information displayed
					});
			});
		}
		@Override
		public int getItemCount() {
			return pokemons.size();
		}
	}
}