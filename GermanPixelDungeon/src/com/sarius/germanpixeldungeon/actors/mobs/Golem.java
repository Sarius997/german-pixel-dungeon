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

import com.sarius.germanpixeldungeon.actors.Char;
import com.sarius.germanpixeldungeon.actors.buffs.Amok;
import com.sarius.germanpixeldungeon.actors.buffs.Sleep;
import com.sarius.germanpixeldungeon.actors.buffs.Terror;
import com.sarius.germanpixeldungeon.actors.mobs.npcs.Imp;
import com.sarius.germanpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.sarius.germanpixeldungeon.sprites.GolemSprite;
import com.watabou.utils.Random;

public class Golem extends Mob {
	
	{
		name = "Golem";
		spriteClass = GolemSprite.class;
		
		HP = HT = 85;
		defenseSkill = 18;
		
		EXP = 12;
		maxLvl = 22;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 20, 40 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 28;
	}
	
	@Override
	protected float attackDelay() {
		return 1.5f;
	}
	
	@Override
	public int dr() {
		return 12;
	}
	
	@Override
	public String defenseVerb() {
		return "blocked";
	}
	
	@Override
	public void die( Object cause ) {
		Imp.Quest.process( this );
		
		super.die( cause );
	}
	
	@Override
	public String description() {
		return
			"Die Zwerge haben versucht ihr Wissen ueber Maschinen mit ihrer neu gefundenen Macht Daemonen zu Binden zu kombinieren. " +
			"Sie benutzen Erdgeister als \"Seelen\" fuer die mechanischen Koerper der Golems, von denen geglaubt wurde, " +
			"man koennte sie am leichtesten kontrollieren. Ungeachtet dessen, konnte der kleinste Fehler im Ritual einen Ausbruch verursachen.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( ScrollOfPsionicBlast.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Amok.class );
		IMMUNITIES.add( Terror.class );
		IMMUNITIES.add( Sleep.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
