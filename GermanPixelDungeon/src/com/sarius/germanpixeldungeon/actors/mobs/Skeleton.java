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

import java.util.HashSet;

import com.watabou.noosa.audio.Sample;
import com.sarius.germanpixeldungeon.Assets;
import com.sarius.germanpixeldungeon.Dungeon;
import com.sarius.germanpixeldungeon.ResultDescriptions;
import com.sarius.germanpixeldungeon.actors.Char;
import com.sarius.germanpixeldungeon.items.Generator;
import com.sarius.germanpixeldungeon.items.Item;
import com.sarius.germanpixeldungeon.items.weapon.enchantments.Death;
import com.sarius.germanpixeldungeon.levels.Level;
import com.sarius.germanpixeldungeon.sprites.SkeletonSprite;
import com.sarius.germanpixeldungeon.utils.GLog;
import com.sarius.germanpixeldungeon.utils.Utils;
import com.watabou.utils.Random;

public class Skeleton extends Mob {

	private static final String TXT_HERO_KILLED = "Du wurdest durch die Explosion der Knochen getoetet...";
	
	{
		name = "Skelett";
		spriteClass = SkeletonSprite.class;
		
		HP = HT = 25;
		defenseSkill = 9;
		
		EXP = 5;
		maxLvl = 10;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 3, 8 );
	}
	
	@Override
	public void die( Object cause ) {
		
		super.die( cause );
		
		boolean heroKilled = false;
		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
			Char ch = findChar( pos + Level.NEIGHBOURS8[i] );
			if (ch != null && ch.isAlive()) {
				int damage = Math.max( 0,  damageRoll() - Random.IntRange( 0, ch.dr() / 2 ) );
				ch.damage( damage, this );
				if (ch == Dungeon.hero && !ch.isAlive()) {
					heroKilled = true;
				}
			}
		}
		
		if (Dungeon.visible[pos]) {
			Sample.INSTANCE.play( Assets.SND_BONES );
		}
		
		if (heroKilled) {
			Dungeon.fail( Utils.format( ResultDescriptions.MOB, Utils.indefinite( name ), Dungeon.depth ) );
			GLog.n( TXT_HERO_KILLED );
		}
	}
	
	@Override
	protected void dropLoot() {
		if (Random.Int( 5 ) == 0) {
			Item loot = Generator.random( Generator.Category.WEAPON );
			for (int i=0; i < 2; i++) {
				Item l = Generator.random( Generator.Category.WEAPON );
				if (l.level < loot.level) {
					loot = l;
				}
			}
			Dungeon.level.drop( loot, pos ).sprite.drop();
		}
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 12;
	}
	
	@Override
	public int dr() {
		return 5;
	}
	
	@Override
	public String defenseVerb() {
		return "blocked";
	}
	
	@Override
	public String description() {
		return
			"Skelette bestehen aus Knochen ungluecklicher Abenteurer und Bewohner des Verlieses, " +
			"sie werden von Ausstrahlungen dunkler Magie von tieferen Ebenen angetrieben. " +
			"Wenn sie stark genug geschaedigt wurden, loesen sie sich in einer Konchenexplosion auf.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Death.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
