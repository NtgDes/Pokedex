package android.pokedex;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DetailedView extends DialogFragment {
	Pokemon PokemonData;
	ImageView imgProfile;
	TextView txtName,txtID,
			txtHeight,txtWeight,
			txtType,txtSpecies,txtHP,
			txtDefense,txtSpeed;
	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

		AlertDialog.Builder builder=new AlertDialog.Builder(getContext());


		View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_detailed,null);

		imgProfile=view.findViewById(R.id.imgProfile);

		txtName=view.findViewById(R.id.txtName);
		txtID=view.findViewById(R.id.txtID);
		txtHeight=view.findViewById(R.id.txtHeight);
		txtWeight=view.findViewById(R.id.txtWeight);
		txtType=view.findViewById(R.id.txtType);
		txtSpecies=view.findViewById(R.id.txtSpecies);

		txtHP=view.findViewById(R.id.txtHP);

		txtDefense=view.findViewById(R.id.txtDefense);
		txtSpeed=view.findViewById(R.id.txtSpeed);
		txtHP=view.findViewById(R.id.txtHP);

		txtDefense=view.findViewById(R.id.txtDefense);
		txtSpeed=view.findViewById(R.id.txtSpeed);



		setData();

		builder.setView(view);

		AlertDialog alertDialog= builder.create();



		alertDialog.show();



		return alertDialog;



	}

	public void RefreshData(Pokemon pokemonData){
		PokemonData=pokemonData;
		setData();
	}
	private void setData(){
		imgProfile.setImageBitmap(PokemonData.Sprite);
		txtName.setText(PokemonData.Name);
		txtID.setText("#"+PokemonData.ID);

		if(PokemonData.Type==null)return;

		txtHeight.setText((PokemonData.Height/10)+"m");
		txtWeight.setText((PokemonData.Weight/10)+"kg");
		txtType.setText(PokemonData.Type+(PokemonData.TypeCount>1?" +"+(PokemonData.TypeCount-1):""));
		txtSpecies.setText(PokemonData.Species);

		txtHP.setText(""+PokemonData.HP);

		txtDefense.setText(""+PokemonData.Defense);
		txtSpeed.setText(""+PokemonData.Speed);


	}
}