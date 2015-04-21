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
import com.sarius.germanpixeldungeon.actors.buffs.Bleeding;
import com.sarius.germanpixeldungeon.actors.buffs.Buff;
import com.sarius.germanpixeldungeon.actors.buffs.Cripple;
import com.sarius.germanpixeldungeon.actors.buffs.Poison;
import com.sarius.germanpixeldungeon.actors.buffs.Weakness;
import com.sarius.germanpixeldungeon.actors.hero.Hero;
import com.sarius.germanpixeldungeon.effects.Speck;
import com.sarius.germanpixeldungeon.utils.GLog;

public class PotionOfHealing extends Potion {

	{
		name = "Heiltrank";
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();
		heal( Dungeon.hero );
		GLog.p( "Deine wunden heilten komplett." );
	}
	
	public static void heal( Hero hero ) {
		
		hero.HP = hero.HT;
		Buff.detach( hero, Poison.class );
		Buff.detach( hero, Cripple.class );
		Buff.detach( hero, Weakness.class );
		Buff.detach( hero, Bleeding.class );
		
		hero.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 4 );
	}
	
	@Override
	public String desc() {
		return
			"Ein Elexir, das dich augenblicklich komplett heilt und Vergiftungen entfernt.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
