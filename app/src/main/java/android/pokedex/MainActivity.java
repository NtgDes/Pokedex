package android.pokedex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

	List<Pokemon> Pokemons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);



		RecyclerView recyclerView=findViewById(R.id.recyclerView);


		recyclerView.setLayoutManager(new GridLayoutManager(this,2));


		new ApiData(pokemons -> {
			if(pokemons==null)return;
			Pokemons =pokemons;
			recyclerView.setAdapter(new RecycleViewAdaptor(Pokemons));

		});




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


				txtID =itemView.findViewById(R.id.txtID);
				txtName=itemView.findViewById(R.id.txtName);
				imgProfile =itemView.findViewById(R.id.imgProfile);

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
			holder.txtName.setText(pokemons.get(position).Name);
			holder.txtID.setText("#"+pokemons.get(position).ID);


			if (pokemons.get(position).Sprite==null)
				new ApiData(pokemons.get(position).SpriteURL, sprite -> {
					if(sprite==null)
						return;
					pokemons.get(position).Sprite=sprite;

					holder.imgProfile.setImageBitmap(pokemons.get(position).Sprite);

				});



		}


		@Override
		public int getItemCount() {
			return pokemons.size();
		}
	}







}