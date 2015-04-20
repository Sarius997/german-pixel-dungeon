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

import com.sarius.germanpixeldungeon.Badges;
import com.sarius.germanpixeldungeon.actors.hero.Hero;
import com.sarius.germanpixeldungeon.sprites.CharSprite;
import com.sarius.germanpixeldungeon.utils.GLog;

public class PotionOfStrength extends Potion {

	{
		name = "Staerketrank";
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();
		
		hero.STR++;
		hero.sprite.showStatus( CharSprite.POSITIVE, "+1 str" );
		GLog.p( "Neu entdeckte Staerke flutet durch deinen Koerper." );
		
		Badges.validateStrengthAttained();
	}
	
	@Override
	public String desc() {
		return
			"Diese maechtige Fluessigkeit wird deine Muskeln durchstroemen und " +
			"deine Staerke permanent um einen Punkt erhoehen.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 100 * quantity : super.price();
	}
}
