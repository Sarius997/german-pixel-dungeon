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
import com.sarius.germanpixeldungeon.actors.Actor;
import com.sarius.germanpixeldungeon.actors.blobs.Blob;
import com.sarius.germanpixeldungeon.actors.blobs.ToxicGas;
import com.sarius.germanpixeldungeon.scenes.GameScene;

public class PotionOfToxicGas extends Potion {

	{
		name = "Trank des giftigen Gases";
	}

	@Override
	protected void shatter(int cell) {

		setKnown();

		splash(cell);
		Sample.INSTANCE.play(Assets.SND_SHATTER);

		ToxicGas gas = Blob.seed(cell, 1000, ToxicGas.class);
		Actor.add(gas);
		GameScene.add(gas);
	}

	@Override
	public String desc() {
		return "Wenn diese Flasche entkorkt oder erschuettert wird, wird ihr "
				+ "Inhalt zu einer giftigen gruenen Gaswolke explodieren. "
				+ "Du solltest diesen Trank lieber auf entfernte Feide schleudern, "
				+ "als sie von Hand zu entkorken.";
	}

	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}
}