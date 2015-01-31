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

import com.watabou.utils.Bundle;

public enum HeroSubClass {

	NONE( null, null ),
	
	GLADIATOR( "gladiator", 
		"Ein erfolgreicher Treffer mit einer Nahkampfwaffe ermoeglicht dem _Gladiator_ eine Kombo zu starten, " +
		"in welcher jeder naechste erfolgreiche Treffer mehr Schaden verursacht." ),	
	BERSERKER( "berserker", 
		"Wenn er schwer verwundet ist, erlangt der _Berserker_ einen Zustand der Raserei, " +
		"welcher seinen Schaden erheblich steigert." ),
	
	WARLOCK( "warlock", 
		"Der  _Hexenmeister_ isst, nachdem er den Feind getoetet hat dessen Seele. " +
		"Dadurch wird er geheilt und sein Hunger gestillt." ),
	BATTLEMAGE( "battlemage", 
		"Wenn er mit einem als Waffe ausgeruestetem Stab kaempft, verursacht der _Kampfmagier_ Bonusschaden abhaengig " +
		"von der Zahl der Aufladungen. Jeder erfolgreiche Treffer laedt eine Ladung des Stabes auf." ),
	
	ASSASSIN( "assassin", 
		"Wenn er einen Ueberraschungsangriff ausuebt, verursacht der _Assassine_ bei seinem Ziel Bonusschaden." ),
	FREERUNNER( "freerunner", 
		"Der _Waldlaeufer_ kann sich doppelt so schnell bewegen wie die meisten Monster. Wenn er " +
		"laeuft, ist der Waldlaeufer viel schwerer zu treffen. Dazu muss er unbelastet sein und darf nicht hungern." ),
		
	SNIPER( "sniper", 
		"_Scharfschuetzinnen_ koennen Schwachstellen in der feindlichen Ruestung entdecken " +
		"und diese ignorieren, wenn sie eine Geschosswaffe benutzen." ),
	WARDEN( "warden", 
		"Dadurch, dass _Waechterinnen_ eine starke Verbindung mit den Kraeften der Natur haben, koennen sie gleichzeitig Tautropfen und " +
		"Samen von Pflanzen erhalten. Ausserdem bekommen sie durch dar Zertrampeln von hohem Grass einen temporaeren Ruestungseffekt." );
	
	private String title;
	private String desc;
	
	private HeroSubClass( String title, String desc ) {
		this.title = title;
		this.desc = desc;
	}
	
	public String title() {
		return title;
	}
	
	public String desc() {
		return desc;
	}
	
	private static final String SUBCLASS	= "subClass";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( SUBCLASS, toString() );
	}
	
	public static HeroSubClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( SUBCLASS );
		try {
			return valueOf( value );
		} catch (Exception e) {
			return NONE;
		}
	}
	
}
