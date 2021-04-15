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
	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

		AlertDialog.Builder builder=new AlertDialog.Builder(getContext());


		View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_detailed,null);


		((ImageView) view.findViewById(R.id.imgProfile)).setImageBitmap(PokemonData.Sprite);

		((TextView) view.findViewById(R.id.txtName)).setText(PokemonData.Name);
		((TextView) view.findViewById(R.id.txtID)).setText("#"+PokemonData.ID);


		builder.setView(view);

		AlertDialog alertDialog= builder.create();



		alertDialog.show();



		return alertDialog;



	}
}