/*
 * Copyright (c) 2015 Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.mazarineblue.pictures.compounders.comperator;

import org.mazarineblue.pictures.PixelUtil;
import org.mazarineblue.pictures.compounders.PixelComperator;

/**
 * A {@code IgnoreAlphaComperator} is an {@code PixelComperator} that indicates
 * that pixels are equal when the components alpha, red, green and blue are
 * equal.
 *
 * @author Alex de Kruijff <alex.de.kruijff@MazarineBlue.org>
 */
public class FullPixelComperator
        implements PixelComperator {

    @Override
    public boolean isPixelEqual(int leftPixel, int rightPixel) {
        return PixelUtil.getAlpha(leftPixel) == PixelUtil.getAlpha(rightPixel)
                && PixelUtil.getRed(leftPixel) == PixelUtil.getRed(rightPixel)
                && PixelUtil.getGreen(leftPixel) == PixelUtil.getGreen(rightPixel)
                && PixelUtil.getBlue(leftPixel) == PixelUtil.getBlue(rightPixel);
    }
}
