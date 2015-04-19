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

import java.util.ArrayList;

import com.watabou.noosa.audio.Sample;
import com.sarius.germanpixeldungeon.Assets;
import com.sarius.germanpixeldungeon.actors.hero.Hero;
import com.sarius.germanpixeldungeon.effects.particles.PurpleParticle;
import com.sarius.germanpixeldungeon.items.armor.Armor;
import com.sarius.germanpixeldungeon.scenes.GameScene;
import com.sarius.germanpixeldungeon.sprites.ItemSpriteSheet;
import com.sarius.germanpixeldungeon.utils.GLog;
import com.sarius.germanpixeldungeon.windows.WndBag;

public class Stylus extends Item {
	
	private static final String TXT_SELECT_ARMOR	= "Waehle eine Ruestung zum beschriften aus";
	private static final String TXT_INSCRIBED		= "Du hast die %s auf deine %s geschrieben";
	
	private static final float TIME_TO_INSCRIBE = 2;
	
	private static final String AC_INSCRIBE = "BESCHRIFTEN";
	
	{
		name = "arkaner Stylus";
		image = ItemSpriteSheet.STYLUS;
		
		stackable = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_INSCRIBE );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action == AC_INSCRIBE) {

			curUser = hero;
			GameScene.selectItem( itemSelector, WndBag.Mode.ARMOR, TXT_SELECT_ARMOR );
			
		} else {
			
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	private void inscribe( Armor armor ) {
		
		detach( curUser.belongings.backpack );
		
		Class<? extends Armor.Glyph> oldGlyphClass = armor.glyph != null ? armor.glyph.getClass() : null;
		Armor.Glyph glyph = Armor.Glyph.random();
		while (glyph.getClass() == oldGlyphClass) {
			glyph = Armor.Glyph.random();
		}
		
		GLog.w( TXT_INSCRIBED, glyph.name(), armor.name() );
		
		armor.inscribe( glyph );
		
		curUser.sprite.operate( curUser.pos );
		curUser.sprite.centerEmitter().start( PurpleParticle.BURST, 0.05f, 10 );
		Sample.INSTANCE.play( Assets.SND_BURNING );
		
		curUser.spend( TIME_TO_INSCRIBE );
		curUser.busy();
	}
	
	@Override
	public int price() {
		return 50 * quantity;
	}
	
	@Override
	public String info() {
		return
			"Dieser arkane Stylusist aus einem dunklen, sehr hartem Stein. Wenn du ihn benutzt, kannst du " +
			"eine magische Glyphe auf deine Ruestung schreiben, aber du hast keine Moeglichkeit die Glyphe auszusuchen, " +
			"der Stylus wird das fuer dich entscheiden.";
	}
	
	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				Stylus.this.inscribe( (Armor)item );
			}
		}
	};
}
