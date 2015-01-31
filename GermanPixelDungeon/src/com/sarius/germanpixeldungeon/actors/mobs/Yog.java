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

import java.util.ArrayList;
import java.util.HashSet;

import com.sarius.germanpixeldungeon.Dungeon;
import com.sarius.germanpixeldungeon.ResultDescriptions;
import com.sarius.germanpixeldungeon.actors.Actor;
import com.sarius.germanpixeldungeon.actors.Char;
import com.sarius.germanpixeldungeon.actors.blobs.Blob;
import com.sarius.germanpixeldungeon.actors.blobs.Fire;
import com.sarius.germanpixeldungeon.actors.blobs.ToxicGas;
import com.sarius.germanpixeldungeon.actors.buffs.Amok;
import com.sarius.germanpixeldungeon.actors.buffs.Buff;
import com.sarius.germanpixeldungeon.actors.buffs.Burning;
import com.sarius.germanpixeldungeon.actors.buffs.Charm;
import com.sarius.germanpixeldungeon.actors.buffs.Ooze;
import com.sarius.germanpixeldungeon.actors.buffs.Poison;
import com.sarius.germanpixeldungeon.actors.buffs.Sleep;
import com.sarius.germanpixeldungeon.actors.buffs.Terror;
import com.sarius.germanpixeldungeon.effects.Pushing;
import com.sarius.germanpixeldungeon.effects.particles.ShadowParticle;
import com.sarius.germanpixeldungeon.items.keys.SkeletonKey;
import com.sarius.germanpixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.sarius.germanpixeldungeon.items.weapon.enchantments.Death;
import com.sarius.germanpixeldungeon.levels.Level;
import com.sarius.germanpixeldungeon.mechanics.Ballistica;
import com.sarius.germanpixeldungeon.scenes.GameScene;
import com.sarius.germanpixeldungeon.sprites.BurningFistSprite;
import com.sarius.germanpixeldungeon.sprites.CharSprite;
import com.sarius.germanpixeldungeon.sprites.LarvaSprite;
import com.sarius.germanpixeldungeon.sprites.RottingFistSprite;
import com.sarius.germanpixeldungeon.sprites.YogSprite;
import com.sarius.germanpixeldungeon.utils.GLog;
import com.sarius.germanpixeldungeon.utils.Utils;
import com.watabou.utils.Random;

public class Yog extends Mob {
	
	{
		name = "Yog-Dzewa";
		spriteClass = YogSprite.class;
		
		HP = HT = 300;
		
		EXP = 50;
		
		state = PASSIVE;
	}
	
	private static final String TXT_DESC =
		"Yog-Dzewa ist ein alter Gott, eine machtvolle Kreatur aus dem Reich des Chaos. Ein Jahrhundert zuvor, " + 
		"haben die alten Zwerge knapp den Krieg gegen seine Daemonenarmee gewonnen, aber sie waren nicht in der Lage, " +
		"den Gott selbst zu toeten. Stattdessen haben sie ihn in den Hallen unter ihrer Stadt eingeschlossen, " +
		"im Glauben, dass er zu sehr geschwaecht ist, sich jemals wieder zu erheben.";	
	
	private static int fistsCount = 0;
	
	public Yog() {
		super();
	}
	
	public void spawnFists() {
		RottingFist fist1 = new RottingFist();
		BurningFist fist2 = new BurningFist();
		
		do {
			fist1.pos = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
			fist2.pos = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
		} while (!Level.passable[fist1.pos] || !Level.passable[fist2.pos] || fist1.pos == fist2.pos);
		
		GameScene.add( fist1 );
		GameScene.add( fist2 );
	}
	
	@Override
	public void damage( int dmg, Object src ) {
		
		if (fistsCount > 0) {
			
			for (Mob mob : Dungeon.level.mobs) {
				if (mob instanceof BurningFist || mob instanceof RottingFist) {
					mob.beckon( pos );
				}
			}
			
			dmg >>= fistsCount;
		}
		
		super.damage( dmg, src );
	}
	
	@Override
	public int defenseProc( Char enemy, int damage ) {

		ArrayList<Integer> spawnPoints = new ArrayList<Integer>();
		
		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
			int p = pos + Level.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Level.passable[p] || Level.avoid[p])) {
				spawnPoints.add( p );
			}
		}
		
		if (spawnPoints.size() > 0) {
			Larva larva = new Larva();
			larva.pos = Random.element( spawnPoints );
			
			GameScene.add( larva );
			Actor.addDelayed( new Pushing( larva, pos, larva.pos ), -1 );
		}

		return super.defenseProc(enemy, damage);
	}
	
	@Override
	public void beckon( int cell ) {
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void die( Object cause ) {

		for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
			if (mob instanceof BurningFist || mob instanceof RottingFist) {
				mob.die( cause );
			}
		}
		
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();
		super.die( cause );
		
		yell( "..." );
	}
	
	@Override
	public void notice() {
		super.notice();
		yell( "Hoffentlich ist es eine Illusion..." );
	}
	
	@Override
	public String description() {
		return TXT_DESC;
			
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		
		IMMUNITIES.add( Death.class );
		IMMUNITIES.add( Terror.class );
		IMMUNITIES.add( Amok.class );
		IMMUNITIES.add( Charm.class );
		IMMUNITIES.add( Sleep.class );
		IMMUNITIES.add( Burning.class );
		IMMUNITIES.add( ToxicGas.class );
		IMMUNITIES.add( ScrollOfPsionicBlast.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
	
	public static class RottingFist extends Mob {
	
		private static final int REGENERATION	= 4;
		
		{
			name = "Verwesende Faust";
			spriteClass = RottingFistSprite.class;
			
			HP = HT = 300;
			defenseSkill = 25;
			
			EXP = 0;
			
			state = WANDERING;
		}
		
		public RottingFist() {
			super();
			fistsCount++;
		}
		
		@Override
		public void die( Object cause ) {
			super.die( cause );
			fistsCount--;
		}
		
		@Override
		public int attackSkill( Char target ) {
			return 36;
		}
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 24, 36 );
		}
		
		@Override
		public int dr() {
			return 15;
		}
		
		@Override
		public int attackProc( Char enemy, int damage ) {
			if (Random.Int( 3 ) == 0) {
				Buff.affect( enemy, Ooze.class );
				enemy.sprite.burst( 0xFF000000, 5 );
			}
			
			return damage;
		}
		
		@Override
		public boolean act() {
			
			if (Level.water[pos] && HP < HT) {
				sprite.emitter().burst( ShadowParticle.UP, 2 );
				HP += REGENERATION;
			}
			
			return super.act();
		}
		
		@Override
		public String description() {
			return TXT_DESC;
				
		}
		
		private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
		static {
			RESISTANCES.add( ToxicGas.class );
			RESISTANCES.add( Death.class );
			RESISTANCES.add( ScrollOfPsionicBlast.class );
		}
		
		@Override
		public HashSet<Class<?>> resistances() {
			return RESISTANCES;
		}
		
		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
		static {
			IMMUNITIES.add( Amok.class );
			IMMUNITIES.add( Sleep.class );
			IMMUNITIES.add( Terror.class );
			IMMUNITIES.add( Poison.class );
		}
		
		@Override
		public HashSet<Class<?>> immunities() {
			return IMMUNITIES;
		}
	}
	
	public static class BurningFist extends Mob {
		
		{
			name = "Brennende Faust";
			spriteClass = BurningFistSprite.class;
			
			HP = HT = 200;
			defenseSkill = 25;
			
			EXP = 0;
			
			state = WANDERING;
		}
		
		public BurningFist() {
			super();
			fistsCount++;
		}
		
		@Override
		public void die( Object cause ) {
			super.die( cause );
			fistsCount--;
		}
		
		@Override
		public int attackSkill( Char target ) {
			return 36;
		}
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 20, 32 );
		}
		
		@Override
		public int dr() {
			return 15;
		}
		
		@Override
		protected boolean canAttack( Char enemy ) {
			return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
		}
		
		@Override
		public boolean attack( Char enemy ) {
			
			if (!Level.adjacent( pos, enemy.pos )) {
				spend( attackDelay() );
				
				if (hit( this, enemy, true )) {
					
					int dmg =  damageRoll();
					enemy.damage( dmg, this );
					
					enemy.sprite.bloodBurstA( sprite.center(), dmg );
					enemy.sprite.flash();
					
					if (!enemy.isAlive() && enemy == Dungeon.hero) {
						Dungeon.fail( Utils.format( ResultDescriptions.BOSS, name, Dungeon.depth ) );
						GLog.n( TXT_KILL, name );
					}
					return true;
					
				} else {
					
					enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
					return false;
				}
			} else {
				return super.attack( enemy );
			}
		}
		
		@Override
		public boolean act() {
			
			for (int i=0; i < Level.NEIGHBOURS9.length; i++) {
				GameScene.add( Blob.seed( pos + Level.NEIGHBOURS9[i], 2, Fire.class ) );
			}
			
			return super.act();
		}
		
		@Override
		public String description() {
			return TXT_DESC;
				
		}
		
		private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
		static {
			RESISTANCES.add( ToxicGas.class );
			RESISTANCES.add( Death.class );
			RESISTANCES.add( ScrollOfPsionicBlast.class );
		}
		
		@Override
		public HashSet<Class<?>> resistances() {
			return RESISTANCES;
		}
		
		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
		static {
			IMMUNITIES.add( Amok.class );
			IMMUNITIES.add( Sleep.class );
			IMMUNITIES.add( Terror.class );
			IMMUNITIES.add( Burning.class );
		}
		
		@Override
		public HashSet<Class<?>> immunities() {
			return IMMUNITIES;
		}
	}
	
	public static class Larva extends Mob {
		
		{
			name = "god's larva";
			spriteClass = LarvaSprite.class;
			
			HP = HT = 25;
			defenseSkill = 20;
			
			EXP = 0;
			
			state = HUNTING;
		}
		
		@Override
		public int attackSkill( Char target ) {
			return 30;
		}
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 15, 20 );
		}
		
		@Override
		public int dr() {
			return 8;
		}
		
		@Override
		public String description() {
			return TXT_DESC;
				
		}
	}
}
