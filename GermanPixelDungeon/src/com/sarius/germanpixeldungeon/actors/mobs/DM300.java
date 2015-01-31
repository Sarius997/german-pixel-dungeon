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

import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.sarius.germanpixeldungeon.Assets;
import com.sarius.germanpixeldungeon.Badges;
import com.sarius.germanpixeldungeon.Dungeon;
import com.sarius.germanpixeldungeon.actors.Actor;
import com.sarius.germanpixeldungeon.actors.Char;
import com.sarius.germanpixeldungeon.actors.blobs.Blob;
import com.sarius.germanpixeldungeon.actors.blobs.ToxicGas;
import com.sarius.germanpixeldungeon.actors.buffs.Buff;
import com.sarius.germanpixeldungeon.actors.buffs.Paralysis;
import com.sarius.germanpixeldungeon.effects.CellEmitter;
import com.sarius.germanpixeldungeon.effects.Speck;
import com.sarius.germanpixeldungeon.effects.particles.ElmoParticle;
import com.sarius.germanpixeldungeon.items.keys.SkeletonKey;
import com.sarius.germanpixeldungeon.items.rings.RingOfThorns;
import com.sarius.germanpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.sarius.germanpixeldungeon.items.weapon.enchantments.Death;
import com.sarius.germanpixeldungeon.levels.Level;
import com.sarius.germanpixeldungeon.levels.Terrain;
import com.sarius.germanpixeldungeon.scenes.GameScene;
import com.sarius.germanpixeldungeon.sprites.DM300Sprite;
import com.sarius.germanpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class DM300 extends Mob {
	
	{
		name = "DM-300";
		spriteClass = DM300Sprite.class;
		
		HP = HT = 200;
		EXP = 30;
		defenseSkill = 18;
		
		loot = new RingOfThorns().random();
		lootChance = 0.333f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 18, 24 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 28;
	}
	
	@Override
	public int dr() {
		return 10;
	}
	
	@Override
	public boolean act() {

		GameScene.add( Blob.seed( pos, 30, ToxicGas.class ) );
		
		return super.act();
	}
	
	@Override
	public void move( int step ) {
		super.move( step );

		if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP && HP < HT) {
			
			HP += Random.Int( 1, HT - HP );
			sprite.emitter().burst( ElmoParticle.FACTORY, 5 );
			
			if (Dungeon.visible[step] && Dungeon.hero.isAlive()) {
				GLog.n( "DM-300 repairs itself!" );
			}
		}

		int[] cells = {
			step-1, step+1, step-Level.WIDTH, step+Level.WIDTH, 
			step-1-Level.WIDTH, 
			step-1+Level.WIDTH, 
			step+1-Level.WIDTH, 
			step+1+Level.WIDTH
		};
		int cell = cells[Random.Int( cells.length )];
		
		if (Dungeon.visible[cell]) {
			CellEmitter.get( cell ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );
			Camera.main.shake( 3, 0.7f );
			Sample.INSTANCE.play( Assets.SND_ROCKS );

			if (Level.water[cell]) {
				GameScene.ripple( cell );
			} else if (Dungeon.level.map[cell] == Terrain.EMPTY) {
				Level.set( cell, Terrain.EMPTY_DECO );
				GameScene.updateMap( cell );
			}
		}

		Char ch = Actor.findChar( cell );
		if (ch != null && ch != this) {
			Buff.prolong( ch, Paralysis.class, 2 );
		}
	}
	
	@Override
	public void die( Object cause ) {
		
		super.die( cause );
		
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();
		
		Badges.validateBossSlain();
		
		yell( "Mission fehlgeschlagen. Abschalten." );
	}
	
	@Override
	public void notice() {
		super.notice();
		yell( "Unautorisiertes Personal entdeckt." );
	}
	
	@Override
	public String description() {
		return
			"Diese Maschine wurde vor Jahrhunderten von den Zwergen erschaffen. Spaeter haben die Zwerge angefangen Maschinen durch " +
			"Golems, Elementare und sogar Daemonen zu ersaetzen. Letztendlich brachte das ihrer Zivilisation den Verfall. Der DM-300 und aehnliche " +
			"Maschinen wurden normalerweise fuer Bau und Bergbau, und in einigen Faellen, zum Verteidigen der Stadt eingesetzt.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( Death.class );
		RESISTANCES.add( ScrollOfPsionicBlast.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( ToxicGas.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
