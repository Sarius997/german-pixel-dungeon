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
package com.sarius.germanpixeldungeon.items.bags;

import com.sarius.germanpixeldungeon.items.Item;
import com.sarius.germanpixeldungeon.items.scrolls.Scroll;
import com.sarius.germanpixeldungeon.sprites.ItemSpriteSheet;

public class ScrollHolder extends Bag {

	{
		name = "Rollentasche";
		image = ItemSpriteSheet.HOLDER;
		
		size = 12;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof Scroll;
	}
	
	@Override
	public int price() {
		return 50;
	}
	
	@Override
	public String info() {
		return
			"Du kannst eine beliebige Anzahl an Schriftrollen in dieser rohrfoermigen Tasche aufbewahren. " +
			"Sie spart Platz in deinem Inventar und schuetzt die Rollen vor Feuer.";
	}
}
