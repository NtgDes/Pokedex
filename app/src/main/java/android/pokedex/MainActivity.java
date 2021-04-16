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

		Button btnSearch =findViewById(R.id.btnSearch);
		btnClear=findViewById(R.id.btnClear);
		txtSearch=findViewById(R.id.txtSearch);

		new ApiData(pokemons -> {
			if(pokemons==null)return;
			Pokemons =pokemons;
			recyclerView.setAdapter(new RecycleViewAdaptor(Pokemons));

		});

		btnSearch.setOnClickListener(view -> {
			if(Pokemons ==null)return;
			List<Pokemon> Search= new ArrayList<>();

			for (int i = 0; i < Pokemons.size() ; i++) {
				if (Pokemons.get(i).Name.contains(txtSearch.getText().toString().trim()))
					Search.add(Pokemons.get(i));
			}

			recyclerView.setAdapter(new RecycleViewAdaptor(Search));
		});
		btnClear.setOnClickListener(view -> {
			txtSearch.setText("");
			ResetSearch();
		});

		txtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				if(txtSearch.getText().toString().trim().isEmpty()){
					ResetSearch();
				}else{
					btnClear.setVisibility(View.VISIBLE);
					searchTimer+=2;
					SearchDelay();
				}
			}
		});

	}
	void ResetSearch() {
		recyclerView.setAdapter(new RecycleViewAdaptor(Pokemons));
		btnClear.setVisibility(View.INVISIBLE);
		searchTimer=-1;
	}
	int searchTimer=-1;
	void SearchDelay(){

		new Handler().postDelayed(() -> {
			if (searchTimer == 0) {
				searchTimer = -1;
				if (Pokemons == null) return;
				List<Pokemon> Search = new ArrayList<>();

				for (int i = 0; i < Pokemons.size(); i++) {
					if (Pokemons.get(i).Name.startsWith(txtSearch.getText().toString().trim()))
						Search.add(Pokemons.get(i));
				}
				recyclerView.setAdapter(new RecycleViewAdaptor(Search));
			} else if (searchTimer > 0) {
				searchTimer--;
				SearchDelay();
			}
		}, 1000);
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
			holder.txtName.setText(pokemons.get(position).Name);
			holder.txtID.setText("#"+pokemons.get(position).ID);

			if (pokemons.get(position).Sprite==null){
				new ApiData(pokemons.get(position).SpriteURL, sprite -> {
					if(sprite==null)
						return;
					pokemons.get(position).Sprite=sprite;

					holder.imgProfile.setImageBitmap(pokemons.get(position).Sprite);

				});
				holder.imgProfile.setImageResource(R.drawable.pokeball);
			}else
				holder.imgProfile.setImageBitmap(pokemons.get(position).Sprite);

			holder.PokemonCard.setOnClickListener(view -> {

				DetailedView PokemonStats = new DetailedView();
				PokemonStats.PokemonData=pokemons.get(position);
				PokemonStats.show(getSupportFragmentManager(),null);

				if(pokemons.get(position).Type==null)
					new ApiData(pokemons.get(position).Name, 0, pokemon -> {
						pokemon.Sprite=pokemons.get(position).Sprite;
						pokemon.SpriteURL=pokemons.get(position).SpriteURL;//? May no longer be necessary to preserve since bitmap available in var sprite
						pokemons.set(position,pokemon);

						PokemonStats.RefreshData(pokemons.get(position));
					});
			});
		}
		@Override
		public int getItemCount() {
			return pokemons.size();
		}
	}
}