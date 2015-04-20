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

import com.sarius.germanpixeldungeon.Dungeon;
import com.sarius.germanpixeldungeon.actors.buffs.Buff;
import com.sarius.germanpixeldungeon.actors.buffs.MindVision;
import com.sarius.germanpixeldungeon.actors.hero.Hero;
import com.sarius.germanpixeldungeon.utils.GLog;

public class PotionOfMindVision extends Potion {

	{
		name = "Trank der Geistessicht";
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();
		Buff.affect( hero, MindVision.class, MindVision.DURATION );
		Dungeon.observe();
		
		if (Dungeon.level.mobs.size() > 0) {
			GLog.i( "Irgendwie kannst du die Anwesenheit der Geister anderer Kreaturen spueren!" );
		} else {
			GLog.i( "Irgendwie weisst du, dass du im Moment allein auf dieser Ebene bist." );
		}
	}
	
	@Override
	public String desc() {
		return
			"Wenn du dies trinkst, wird dein Geist auf die psychische Signatur entfernter Kreaturen " +
			"abgestimmt, was es dir erlaubt, biologische Wesen durch Waende zu spueren. " +
			"Ausserdem erlaubt es dir dieser Trank auch, durch nahegelegene Waende und Tueren zu sehen.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 35 * quantity : super.price();
	}
}
