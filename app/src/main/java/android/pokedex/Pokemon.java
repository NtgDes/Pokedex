package android.pokedex;

import android.graphics.Bitmap;

public class Pokemon {
	int ID;
	String Name;
	String SpriteURL;

	Bitmap Sprite;

	int Height,Weight;
	String Species,Type;
	int TypeCount;//show the number 'Type' it belong to

	//pokemon stats
	int HP, Attack,Defense,
			specialAttack,SpecialDefense,
			Speed;
}