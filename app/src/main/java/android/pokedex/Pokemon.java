package android.pokedex;

import android.graphics.Bitmap;

public class Pokemon {
	int ID;
	String Name;
	String SpriteURL;

	Bitmap Sprite;

	int Height,Weight;
	String Species,Type;
	int TypeCount;

	//stats
	int HP, Attack,Defense,
			specialAttack,SpecialDefense,
			Speed;
}