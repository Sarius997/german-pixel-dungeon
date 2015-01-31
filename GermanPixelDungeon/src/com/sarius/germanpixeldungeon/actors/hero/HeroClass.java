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
package com.sarius.germanpixeldungeon.actors.hero;

import com.sarius.germanpixeldungeon.Assets;
import com.sarius.germanpixeldungeon.Badges;
import com.sarius.germanpixeldungeon.Dungeon;
import com.sarius.germanpixeldungeon.items.TomeOfMastery;
import com.sarius.germanpixeldungeon.items.armor.ClothArmor;
import com.sarius.germanpixeldungeon.items.food.Food;
import com.sarius.germanpixeldungeon.items.potions.PotionOfStrength;
import com.sarius.germanpixeldungeon.items.rings.RingOfShadows;
import com.sarius.germanpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.sarius.germanpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.sarius.germanpixeldungeon.items.wands.WandOfMagicMissile;
import com.sarius.germanpixeldungeon.items.weapon.melee.Dagger;
import com.sarius.germanpixeldungeon.items.weapon.melee.Knuckles;
import com.sarius.germanpixeldungeon.items.weapon.melee.ShortSword;
import com.sarius.germanpixeldungeon.items.weapon.missiles.Boomerang;
import com.sarius.germanpixeldungeon.items.weapon.missiles.Dart;
import com.watabou.utils.Bundle;

public enum HeroClass {

	WARRIOR( "warrior" ), MAGE( "mage" ), ROGUE( "rogue" ), HUNTRESS( "huntress" );
	
	private String title;
	
	private HeroClass( String title ) {
		this.title = title;
	}
	
	public static final String[] WAR_PERKS = {
		"Krieger starten mit 11 Staerkepunkten.",
		"Krieger starten mit einem einzigartigem Kurzschwert. Dieses Schwert kann spaeter \"wiederverwendet\" werden um eine andere Nahkampfwaffe zu verbessern.",
		"Krieger sind nicht so geuebt im Umgang mit Geschosswaffen.",
		"Jedes gegessene Lebensmittel stellt etwas Leben wieder her.",
		"Staerketraenke sind von Anfang an identifiziert.",
	};
	
	public static final String[] MAG_PERKS = {
		"Magier starten mit einem einzigartigem Stab der magischen Geschosse. Dieser Stab kann spaeter \"entzaubert\" werden um einen anderen Stab zu verbessern.",
		"Magier laden Staebe schneller wieder auf.",
		"Jedes gegessene Lebensmittel laedt alle Staebe im Inventar um eine Ladung auf.",
		"Magier koennen Staebe als Nahkampfwaffen ausruesten.",
		"Identifikationsrollen sind von Anfang an identifiziert."
	};
	
	public static final String[] ROG_PERKS = {
		"Schurken starten mit einem Ring der Schatten+1.",
		"Schurken identifizieren den Ringtyp durch ausruesten des Ringes.",
		"Schurken sind geuebt im Umgang mit leichter Ruestung, sie weichen damit besser aus.",
		"Schurken sind geuebt im Erkennen von verstaekten Tueren und Fallen.",
		"Schurken halten laenger ohne Essen durch.",
		"Magische Karten sind von Anfang an identifiziert."
	};
	
	public static final String[] HUN_PERKS = {
		"Jaegerinnen starten mit 15 Lebenspunkten.",
		"Jaegerinnen starten mit einem einzigartigem verbesserbaren Boomerang.",
		"Jaegerinnen sind geuebt im Umgang mit Geschosswaffen und verursachen damit Bonusschaden.",
		"Jaegerinnen heilen mehr Lebenspunkte durch Tautropfen.",
		"Jaegerinnen spueren Monster in der Naehe, auch wenn sie hinter einem Hinderniss versteckt sind."
	};
	
	public void initHero( Hero hero ) {
		
		hero.heroClass = this;
		
		initCommon( hero );
		
		switch (this) {
		case WARRIOR:
			initWarrior( hero );
			break;
			
		case MAGE:
			initMage( hero );
			break;
			
		case ROGUE:
			initRogue( hero );
			break;
			
		case HUNTRESS:
			initHuntress( hero );
			break;
		}
		
		if (Badges.isUnlocked( masteryBadge() )) {
			new TomeOfMastery().collect();
		}
		
		hero.updateAwareness();
	}
	
	private static void initCommon( Hero hero ) {
		(hero.belongings.armor = new ClothArmor()).identify();
		new Food().identify().collect();
	}
	
	public Badges.Badge masteryBadge() {
		switch (this) {
		case WARRIOR:
			return Badges.Badge.MASTERY_WARRIOR;
		case MAGE:
			return Badges.Badge.MASTERY_MAGE;
		case ROGUE:
			return Badges.Badge.MASTERY_ROGUE;
		case HUNTRESS:
			return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}
	
	private static void initWarrior( Hero hero ) {
		hero.STR = hero.STR + 1;
		(hero.belongings.weapon = new ShortSword()).identify();
		new Dart( 8 ).identify().collect();
		
		Dungeon.quickslot = Dart.class;
		
		new PotionOfStrength().setKnown();
	}
	
	private static void initMage( Hero hero ) {	
		(hero.belongings.weapon = new Knuckles()).identify();
		
		WandOfMagicMissile wand = new WandOfMagicMissile();
		wand.identify().collect();
		
		Dungeon.quickslot = wand;
		
		new ScrollOfIdentify().setKnown();
	}
	
	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();
		(hero.belongings.ring1 = new RingOfShadows()).upgrade().identify();
		new Dart( 8 ).identify().collect();
		
		hero.belongings.ring1.activate( hero );
		
		Dungeon.quickslot = Dart.class;
		
		new ScrollOfMagicMapping().setKnown();
	}
	
	private static void initHuntress( Hero hero ) {
		
		hero.HP = (hero.HT -= 5);
		
		(hero.belongings.weapon = new Dagger()).identify();
		Boomerang boomerang = new Boomerang();
		boomerang.identify().collect();
		
		Dungeon.quickslot = boomerang;
	}
	
	public String title() {
		return title;
	}
	
	public String spritesheet() {
		
		switch (this) {
		case WARRIOR:
			return Assets.WARRIOR;
		case MAGE:
			return Assets.MAGE;
		case ROGUE:
			return Assets.ROGUE;
		case HUNTRESS:
			return Assets.HUNTRESS;
		}
		
		return null;
	}
	
	public String[] perks() {
		
		switch (this) {
		case WARRIOR:
			return WAR_PERKS;
		case MAGE:
			return MAG_PERKS;
		case ROGUE:
			return ROG_PERKS;
		case HUNTRESS:
			return HUN_PERKS;
		}
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : ROGUE;
	}
}
