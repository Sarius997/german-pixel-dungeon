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
package com.sarius.germanpixeldungeon.items.potions;

import com.watabou.noosa.audio.Sample;
import com.sarius.germanpixeldungeon.Assets;
import com.sarius.germanpixeldungeon.Dungeon;
import com.sarius.germanpixeldungeon.actors.blobs.Blob;
import com.sarius.germanpixeldungeon.actors.blobs.ParalyticGas;
import com.sarius.germanpixeldungeon.actors.blobs.ToxicGas;
import com.sarius.germanpixeldungeon.actors.buffs.Buff;
import com.sarius.germanpixeldungeon.actors.buffs.GasesImmunity;
import com.sarius.germanpixeldungeon.actors.hero.Hero;
import com.sarius.germanpixeldungeon.effects.CellEmitter;
import com.sarius.germanpixeldungeon.effects.Speck;
import com.sarius.germanpixeldungeon.levels.Level;
import com.sarius.germanpixeldungeon.utils.BArray;
import com.sarius.germanpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;

public class PotionOfPurity extends Potion {

	private static final String TXT_FRESHNESS	= "Du bemerkt eine unnatuerliche Frische in der Luft.";
	private static final String TXT_NO_SMELL	= "Du hast aufgehoert Gerueche wahrzunehmen!";
	
	private static final int DISTANCE	= 2;
	
	{
		name = "Trank der Reinigung";
	}
	
	@Override
	protected void shatter( int cell ) {
		
		PathFinder.buildDistanceMap( cell, BArray.not( Level.losBlocking, null ), DISTANCE );
		
		boolean procd = false;
		
		Blob[] blobs = {
			Dungeon.level.blobs.get( ToxicGas.class ), 
			Dungeon.level.blobs.get( ParalyticGas.class )
		};
		
		for (int j=0; j < blobs.length; j++) {
			
			Blob blob = blobs[j];
			if (blob == null) {
				continue;
			}
			
			for (int i=0; i < Level.LENGTH; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE) {
					
					int value = blob.cur[i]; 
					if (value > 0) {
						
						blob.cur[i] = 0;
						blob.volume -= value;
						procd = true;
						
						CellEmitter.get( i ).burst( Speck.factory( Speck.DISCOVER ), 1 );
					}

				}
			}
		}
		
		boolean heroAffected = PathFinder.distance[Dungeon.hero.pos] < Integer.MAX_VALUE;
		
		if (procd) {
			
			splash( cell );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
			
			setKnown();
			
			if (heroAffected) {
				GLog.p( TXT_FRESHNESS );
			}
			
		} else {
			
			super.shatter( cell );
			
			if (heroAffected) {
				GLog.i( TXT_FRESHNESS );
				setKnown();
			}
			
		}
	}
	
	@Override
	protected void apply( Hero hero ) {
		GLog.w( TXT_NO_SMELL );
		Buff.prolong( hero, GasesImmunity.class, GasesImmunity.DURATION );
		setKnown();
	}
	
	@Override
	public String desc() {
		return 
			"Dieses Gegengift wird sofort alle gefaehrlichen Gase in ihrem Wirkungsbereich neutralisieren. " +
			"Wenn man es trinkt gewaehrt es temporaere immunitaet gegen solche Gase.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 50 * quantity : super.price();
	}
}
