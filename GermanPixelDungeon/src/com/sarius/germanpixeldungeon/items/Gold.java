/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.sarius.germanpixeldungeon.items;

import java.util.ArrayList;

import com.watabou.noosa.audio.Sample;
import com.sarius.germanpixeldungeon.Assets;
import com.sarius.germanpixeldungeon.Badges;
import com.sarius.germanpixeldungeon.Dungeon;
import com.sarius.germanpixeldungeon.Statistics;
import com.sarius.germanpixeldungeon.actors.hero.Hero;
import com.sarius.germanpixeldungeon.scenes.GameScene;
import com.sarius.germanpixeldungeon.sprites.CharSprite;
import com.sarius.germanpixeldungeon.sprites.ItemSpriteSheet;
import com.sarius.germanpixeldungeon.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Gold extends Item {

	private static final String TXT_COLLECT	= "Sammle Goldmuenzen um sie spaeter in Laeden auszugeben.";
//	private static final String TXT_INFO	= "Ein Haufen mit %d Goldmuenzen. " + TXT_COLLECT;
	private static final String TXT_INFO	= "Du hast %d Goldmuenzen. " + TXT_COLLECT;
//	private static final String TXT_INFO_1	= "Eine Goldmuenze. " + TXT_COLLECT;
	private static final String TXT_INFO_1	= "Du hast eine Goldmuenze. " + TXT_COLLECT;
	private static final String TXT_VALUE	= "%+d";
	
	{
		name = "Gold";
		image = ItemSpriteSheet.GOLD;
		stackable = true;
	}
	
	public Gold() {
		this( 1 );
	}
	
	public Gold( int value ) {
		this.quantity = value;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		return new ArrayList<String>();
	}
	
	@Override
	public boolean doPickUp( Hero hero ) {
		
		Dungeon.gold += quantity;
		Statistics.goldCollected += quantity;
		Badges.validateGoldCollected();
		
		GameScene.pickUp( this );
		hero.sprite.showStatus( CharSprite.NEUTRAL, TXT_VALUE, quantity );
		hero.spendAndNext( TIME_TO_PICK_UP );
		
		Sample.INSTANCE.play( Assets.SND_GOLD, 1, 1, Random.Float( 0.9f, 1.1f ) );
		
		return true;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String info() {
		switch (quantity) {
		case 0:
			return TXT_COLLECT;
		case 1:
			return TXT_INFO_1;
		default:
			return Utils.format( TXT_INFO, quantity );
		}
	}
	
	@Override
	public Item random() {
		quantity = Random.Int( 20 + Dungeon.depth * 10, 40 + Dungeon.depth * 20 );
		return this;
	}
	
	private static final String VALUE	= "value";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( VALUE, quantity );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		quantity = bundle.getInt( VALUE );
	}
}
