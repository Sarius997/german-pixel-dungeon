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
package com.sarius.germanpixeldungeon.items.armor;

import java.util.HashMap;

import com.sarius.germanpixeldungeon.Dungeon;
import com.sarius.germanpixeldungeon.actors.hero.Hero;
import com.sarius.germanpixeldungeon.actors.hero.HeroClass;
import com.sarius.germanpixeldungeon.actors.mobs.Mob;
import com.sarius.germanpixeldungeon.items.Item;
import com.sarius.germanpixeldungeon.items.weapon.missiles.Shuriken;
import com.sarius.germanpixeldungeon.levels.Level;
import com.sarius.germanpixeldungeon.sprites.ItemSpriteSheet;
import com.sarius.germanpixeldungeon.sprites.MissileSprite;
import com.sarius.germanpixeldungeon.utils.GLog;
import com.watabou.utils.Callback;

public class HuntressArmor extends ClassArmor {
	
	private static final String TXT_NO_ENEMIES 		= "Keine Feinde in sicht";
	private static final String TXT_NOT_HUNTRESS	= "Nur Jaegerinnen koennen diese Ruestung benutzen!";
	
	private static final String AC_SPECIAL = "SPEKTRALKLINGEN"; 
	
	{
		name = "Jaegerinnenmantel";
		image = ItemSpriteSheet.ARMOR_HUNTRESS;
	}
	
	private HashMap<Callback, Mob> targets = new HashMap<Callback, Mob>();
	
	@Override
	public String special() {
		return AC_SPECIAL;
	}
	
	@Override
	public void doSpecial() {
		
		Item proto = new Shuriken();
		
		for (Mob mob : Dungeon.level.mobs) {
			if (Level.fieldOfView[mob.pos]) {
				
				Callback callback = new Callback() {	
					@Override
					public void call() {
						curUser.attack( targets.get( this ) );
						targets.remove( this );
						if (targets.isEmpty()) {
							curUser.spendAndNext( curUser.attackDelay() );
						}
					}
				};
				
				((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
					reset( curUser.pos, mob.pos, proto, callback );
				
				targets.put( callback, mob );
			}
		}
		
		if (targets.size() == 0) {
			GLog.w( TXT_NO_ENEMIES );
			return;
		}
		
		curUser.HP -= (curUser.HP / 3);
		
		curUser.sprite.zap( curUser.pos );
		curUser.busy();
	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.heroClass == HeroClass.HUNTRESS) {
			return super.doEquip( hero );
		} else {
			GLog.w( TXT_NOT_HUNTRESS );
			return false;
		}
	}
	
	@Override
	public String desc() {
		return
			"Eine Jaegerin in solch einem Mantel kann einen Sturm aus Spektralklingen erzeugen. Jede von diesen Klingen " +
			"wird auf einen einzelnen Feind in dem Sichtfeld der Jaegerin zielen und Schaden abhaengig von " +
			"ihrer aktuell ausgeruesteten Nahkampfwaffe.";
	}
}