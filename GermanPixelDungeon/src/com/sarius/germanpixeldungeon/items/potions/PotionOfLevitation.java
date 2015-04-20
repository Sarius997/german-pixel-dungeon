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

import com.sarius.germanpixeldungeon.actors.buffs.Buff;
import com.sarius.germanpixeldungeon.actors.buffs.Levitation;
import com.sarius.germanpixeldungeon.actors.hero.Hero;
import com.sarius.germanpixeldungeon.utils.GLog;

public class PotionOfLevitation extends Potion {

	{
		name = "Schwerelosigkeitstrank";
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();
		Buff.affect( hero, Levitation.class, Levitation.DURATION );
		GLog.i( "Du schwebst in der Lust!" );
	}
	
	@Override
	public String desc() {
		return
			"Das Trinken dieser seltsamen Fluessigkeit wird dich in der Luft schweben lassen, " +
			"faehig, muehelos über Fallen zu schweben. Flammen und Gase in der Luft " +
			"koennen damit aber nicht umgangen werden.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 35 * quantity : super.price();
	}
}
