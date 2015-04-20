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
import com.sarius.germanpixeldungeon.actors.blobs.Blob;
import com.sarius.germanpixeldungeon.actors.blobs.ParalyticGas;
import com.sarius.germanpixeldungeon.scenes.GameScene;

public class PotionOfParalyticGas extends Potion {

	{
		name = "Trank des Laehmungsgases";
	}
	
	@Override
	protected void shatter( int cell ) {
		
		setKnown();
		
		splash( cell );
		Sample.INSTANCE.play( Assets.SND_SHATTER );
		
		GameScene.add( Blob.seed( cell, 1000, ParalyticGas.class ) );
	}
	
	@Override
	public String desc() {
		return
			"Unter der Einwirkung von Luft wird die Fluessigkeit in dieser Flasche zu einem betaeubenden gelbem " +
			"Nebel verdampfen. Jeder, der etwas von der Wolke einatmet wird augenblicklich gelaehmt und ist " +
			"auch kurze Zeit nachdem sich die Wolke zerstreut hat noch unfaehig sich zu bewegen. Dieser " +
			"Gegenstand kann auf entfernte Feinde geworfen werden, um sie im Effekt des Gases zu fangen.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}
}
