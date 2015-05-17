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
package com.sarius.germanpixeldungeon.items;

import com.watabou.noosa.audio.Sample;
import com.sarius.germanpixeldungeon.Assets;
import com.sarius.germanpixeldungeon.Dungeon;
import com.sarius.germanpixeldungeon.actors.hero.Hero;
import com.sarius.germanpixeldungeon.effects.particles.ShadowParticle;
import com.sarius.germanpixeldungeon.utils.GLog;

public abstract class EquipableItem extends Item {

	private static final String TXT_UNEQUIP_CURSED	= "Du kannst den/die/das verfluchte/n %s nicht ablegen!";
	
//	public static final String AC_EQUIP		= "AUSRUESTEN";
	public static final String AC_EQUIP		= "ANLEGEN";
	public static final String AC_UNEQUIP	= "ABLEGEN";
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_EQUIP )) {
			doEquip( hero );
		} else if (action.equals( AC_UNEQUIP )) {
			doUnequip( hero, true );
		} else {
			super.execute( hero, action );
		}
	}
	
	@Override
	public void doDrop( Hero hero ) {
		if (!isEquipped( hero ) || doUnequip( hero, false, false )) {
			super.doDrop( hero );
		}
	}
	
	@Override
	public void cast( final Hero user, int dst ) {

		if (isEquipped( user )) {
			if (quantity == 1 && !this.doUnequip( user, false, false )) {
				return;
			}
		}
		
		super.cast( user, dst );
	}
	
	protected static void equipCursed( Hero hero ) {
		hero.sprite.emitter().burst( ShadowParticle.CURSE, 6 );
		Sample.INSTANCE.play( Assets.SND_CURSED );
	}
	
	protected float time2equip( Hero hero ) {
		return 1;
	}
	
	public abstract boolean doEquip( Hero hero );
	
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		
		if (cursed) {
			GLog.w( TXT_UNEQUIP_CURSED, name() );
			return false;
		}
		
		if (single) {
			hero.spendAndNext( time2equip( hero ) );
		} else {
			hero.spend( time2equip( hero ) );
		}
		
		if (collect && !collect( hero.belongings.backpack )) {
			Dungeon.level.drop( this, hero.pos );
		}
				
		return true;
	}
	
	public boolean doUnequip( Hero hero, boolean collect ) {
		return doUnequip( hero, collect, true );
	}
}