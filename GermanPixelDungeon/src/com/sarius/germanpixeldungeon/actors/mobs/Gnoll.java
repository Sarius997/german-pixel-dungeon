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
package com.sarius.germanpixeldungeon.actors.mobs;

import com.sarius.germanpixeldungeon.actors.Char;
import com.sarius.germanpixeldungeon.actors.mobs.npcs.Ghost;
import com.sarius.germanpixeldungeon.items.Gold;
import com.sarius.germanpixeldungeon.sprites.GnollSprite;
import com.watabou.utils.Random;

public class Gnoll extends Mob {
	
	{
		name = "Gnollkundschafter";
		spriteClass = GnollSprite.class;
		
		HP = HT = 12;
		defenseSkill = 4;
		
		EXP = 2;
		maxLvl = 8;
		
		loot = Gold.class;
		lootChance = 0.5f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 5 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 11;
	}
	
	@Override
	public int dr() {
		return 2;
	}
	
	@Override
	public void die( Object cause ) {
		Ghost.Quest.process( pos );
		super.die( cause );
	}
	
	@Override
	public String description() {
		return
			"Gnolle sind hyaenen- und menschenartige Wesen. Sie hausen in den Abwassersystemen und Verliesen, wagen es aber von Zeit zu Zeit die Oberflaeche zu pluendern. " +
			"Gnollkundschafter sind normale Mitglieder ihrer Meute, sie sind nicht so stark wie die Wuestlinge und nicht so intelligent wie die Schamanen.";
	}
}
