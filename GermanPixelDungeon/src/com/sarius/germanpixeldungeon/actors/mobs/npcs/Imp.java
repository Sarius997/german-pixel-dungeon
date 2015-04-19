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
package com.sarius.germanpixeldungeon.actors.mobs.npcs;

import com.sarius.germanpixeldungeon.Dungeon;
import com.sarius.germanpixeldungeon.Journal;
import com.sarius.germanpixeldungeon.actors.Actor;
import com.sarius.germanpixeldungeon.actors.Char;
import com.sarius.germanpixeldungeon.actors.buffs.Buff;
import com.sarius.germanpixeldungeon.actors.mobs.Golem;
import com.sarius.germanpixeldungeon.actors.mobs.Mob;
import com.sarius.germanpixeldungeon.actors.mobs.Monk;
import com.sarius.germanpixeldungeon.items.Generator;
import com.sarius.germanpixeldungeon.items.quest.DwarfToken;
import com.sarius.germanpixeldungeon.items.rings.Ring;
import com.sarius.germanpixeldungeon.levels.CityLevel;
import com.sarius.germanpixeldungeon.levels.Room;
import com.sarius.germanpixeldungeon.scenes.GameScene;
import com.sarius.germanpixeldungeon.sprites.ImpSprite;
import com.sarius.germanpixeldungeon.utils.Utils;
import com.sarius.germanpixeldungeon.windows.WndImp;
import com.sarius.germanpixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Imp extends NPC {

	{
		name = "ehrgeiziger Kobold";
		spriteClass = ImpSprite.class;
	}
	
	private static final String TXT_GOLEMS1	=
		"Bist du ein Abenteurer? Ich liebe Abenteurer! Man kann sich immer auf sie verlassen " +
		"wenn etwas getoetet werden soll. Hab ich recht? Fuer eine Belohnung natürlich ;)\n" +
		"In meinem Fall sind es _Golems_ die getoetet werden sollen. Siehst du, ich will hier ein " +
		"kleines Geschaeft aufbauen, aber diese dumme Golems sind schlecht fuers Geschaeft! " +
		"Es ist sehr schwer, mit lebendigen Granitbrocken zu verhandeln, verdammt sollen sie sein! " +
		"Also bitte toete... sagen wir mal _6 von ihnen_ und die Belohnung ist dein.";
	
	private static final String TXT_MONKS1	=
		"Bist du ein Abenteurer? Ich liebe Abenteurer! Man kann sich immer auf sie verlassen " +
		"wenn etwas getoetet werden soll. Hab ich recht? Fuer eine Belohnung natürlich ;)\n" +
		"In meinem Fall sind es _Zwergenmoenche_ die getoetet werden sollen. Siehst du, ich will hier ein " +
		"kleines Geschaeft aufbauen, aber diese verrueckten kaufen sich nichts und wuerden mir " +
		"meine Kunden verschaeuchen. " +
		"Also bitte toete... sagen wir mal _8 von ihnen_ und die Belohnung ist dein.";
	
	private static final String TXT_GOLEMS2	=
		"Wie verlaeuft deine Golemjagd?";	
	
	private static final String TXT_MONKS2	=
		"Oh, du lebst noch! Ich wusste, dass dein kung fu staerker ist ;) " +
		"Vergiss aber nicht die Tokens dieser Zwergenmoenche zu nehmen.";	
	
	private static final String TXT_CYA	= "Bis spaeter, %s!";
	private static final String TXT_HEY	= "Psst, %s!";
	
	private boolean seenBefore = false;
	
	@Override
	protected boolean act() {
		
		if (!Quest.given && Dungeon.visible[pos]) {
			if (!seenBefore) {
				yell( Utils.format( TXT_HEY, Dungeon.hero.className() ) );
			}
			seenBefore = true;
		} else {
			seenBefore = false;
		}
		
		throwItem();
		
		return super.act();
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public String defenseVerb() {
		return "evaded";
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		if (Quest.given) {
			
			DwarfToken tokens = Dungeon.hero.belongings.getItem( DwarfToken.class );
			if (tokens != null && (tokens.quantity() >= 8 || (!Quest.alternative && tokens.quantity() >= 6))) {
				GameScene.show( new WndImp( this, tokens ) );
			} else {
				tell( Quest.alternative ? TXT_MONKS2 : TXT_GOLEMS2, Dungeon.hero.className() );
			}
			
		} else {
			tell( Quest.alternative ? TXT_MONKS1 : TXT_GOLEMS1 );
			Quest.given = true;
			Quest.completed = false;
			
			Journal.add( Journal.Feature.IMP );
		}
	}
	
	private void tell( String format, Object...args ) {
		GameScene.show( 
			new WndQuest( this, Utils.format( format, args ) ) );
	}
	
	public void flee() {
		
		yell( Utils.format( TXT_CYA, Dungeon.hero.className() ) );
		
		destroy();
		sprite.die();
	}
	
	@Override
	public String description() {
		return 
			"Kobolde sind niedrigere Daemonen. Sie sind nicht wegen ihrer Staerke oder ihrem Magietalent beachtenswert, " +
			"sondern weil sie sehr intelligent und gesellig sind. Viele Kobolde bevorzugen es unter Menschen zu leben.";
	}
	
	public static class Quest {
		
		private static boolean alternative;
		
		private static boolean spawned;
		private static boolean given;
		private static boolean completed;
		
		public static Ring reward;
		
		public static void reset() {
			spawned = false;

			reward = null;
		}
		
		private static final String NODE		= "demon";
		
		private static final String ALTERNATIVE	= "alternative";
		private static final String SPAWNED		= "spawned";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String REWARD		= "reward";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( ALTERNATIVE, alternative );
				
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( REWARD, reward );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				alternative	= node.getBoolean( ALTERNATIVE );
				
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				reward = (Ring)node.get( REWARD );
			}
		}
		
		public static void spawn( CityLevel level, Room room ) {
			if (!spawned && Dungeon.depth > 16 && Random.Int( 20 - Dungeon.depth ) == 0) {
				
				Imp npc = new Imp();
				do {
					npc.pos = level.randomRespawnCell();
				} while (npc.pos == -1 || level.heaps.get( npc.pos ) != null);
				level.mobs.add( npc );
				Actor.occupyCell( npc );
				
				spawned = true;	
				alternative = Random.Int( 2 ) == 0;
				
				given = false;
				
				do {
					reward = (Ring)Generator.random( Generator.Category.RING );
				} while (reward.cursed);
				reward.upgrade( 2 );
				reward.cursed = true;
			}
		}
		
		public static void process( Mob mob ) {
			if (spawned && given && !completed) {
				if ((alternative && mob instanceof Monk) ||
					(!alternative && mob instanceof Golem)) {
					
					Dungeon.level.drop( new DwarfToken(), mob.pos ).sprite.drop();
				}
			}
		}
		
		public static void complete() {
			reward = null;
			completed = true;
			
			Journal.remove( Journal.Feature.IMP );
		}
		
		public static boolean isCompleted() {
			return completed;
		}
	}
}
