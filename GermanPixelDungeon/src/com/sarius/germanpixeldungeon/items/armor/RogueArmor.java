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

import com.watabou.noosa.audio.Sample;
import com.sarius.germanpixeldungeon.Assets;
import com.sarius.germanpixeldungeon.Dungeon;
import com.sarius.germanpixeldungeon.actors.Actor;
import com.sarius.germanpixeldungeon.actors.buffs.Blindness;
import com.sarius.germanpixeldungeon.actors.buffs.Buff;
import com.sarius.germanpixeldungeon.actors.hero.Hero;
import com.sarius.germanpixeldungeon.actors.hero.HeroClass;
import com.sarius.germanpixeldungeon.actors.mobs.Mob;
import com.sarius.germanpixeldungeon.effects.CellEmitter;
import com.sarius.germanpixeldungeon.effects.Speck;
import com.sarius.germanpixeldungeon.items.wands.WandOfBlink;
import com.sarius.germanpixeldungeon.levels.Level;
import com.sarius.germanpixeldungeon.scenes.CellSelector;
import com.sarius.germanpixeldungeon.scenes.GameScene;
import com.sarius.germanpixeldungeon.sprites.ItemSpriteSheet;
import com.sarius.germanpixeldungeon.utils.GLog;

public class RogueArmor extends ClassArmor {
	
	private static final String TXT_FOV 		= "Du kannst nur an eine leere Stelle in deinem Sichtfeld springen";
	private static final String TXT_NOT_ROGUE	= "Nur Schurken koennen diese Ruestung benutzen!";
	
	private static final String AC_SPECIAL = "RAUCHBOMBE"; 
	
	{
		name = "Schurkentracht";
		image = ItemSpriteSheet.ARMOR_ROGUE;
	}
	
	@Override
	public String special() {
		return AC_SPECIAL;
	}
	
	@Override
	public void doSpecial() {			
		GameScene.selectCell( teleporter );
	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		if (hero.heroClass == HeroClass.ROGUE) {
			return super.doEquip( hero );
		} else {
			GLog.w( TXT_NOT_ROGUE );
			return false;
		}
	}
	
	@Override
	public String desc() {
		return 
			"Wenn er diese dunkle Tracht traegt, kann ein Schurke einen Trick vollfuehren, der \"Rauchbombe\" genannt wird " +
			"(obwohl kein richtiger Sprengstoff verwendet wird): er blendet Feinde die ihn sehen koennen und springt zur Seite.";
	}
	
	protected static CellSelector.Listener teleporter = new  CellSelector.Listener() {
		
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {

				if (!Level.fieldOfView[target] || 
					!(Level.passable[target] || Level.avoid[target]) || 
					Actor.findChar( target ) != null) {
					
					GLog.w( TXT_FOV );
					return;
				}
				
				curUser.HP -= (curUser.HP / 3);
				
				for (Mob mob : Dungeon.level.mobs) {
					if (Level.fieldOfView[mob.pos]) {
						Buff.prolong( mob, Blindness.class, 2 );
						mob.state = mob.WANDERING;
						mob.sprite.emitter().burst( Speck.factory( Speck.LIGHT ), 4 );
					}
				}
				
				WandOfBlink.appear( curUser, target );
				CellEmitter.get( target ).burst( Speck.factory( Speck.WOOL ), 10 );
				Sample.INSTANCE.play( Assets.SND_PUFF );
				Dungeon.level.press( target, curUser );
				Dungeon.observe();
				
				curUser.spendAndNext( Actor.TICK );
			}
		}
		
		@Override
		public String prompt() {
			return "Waehle eine Stelle an die du springen willst";
		}
	};
}