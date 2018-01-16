/*
 * Copyright 2018 Clifford Errickson.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cerricks.gps;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a region bounded by 4 geographical coordinates.
 *
 * @author Clifford Errickson
 * @since 1.0
 */
public class Region {

  private final List<Coordinates> coordinates;

  /**
   * Creates a {@code Region} defined by 4 geographical coordinates. It is not
   * necessary to provide coordinates in any particular order. They will be
   * ordered along the path of the perimeter of this {@code Region} internally.
   *
   * @param c1 the first coordinates.
   * @param c2 the second coordinates.
   * @param c3 the third coordinates.
   * @param c4 the fourth coordinates.
   */
  public Region(Coordinates c1, Coordinates c2, Coordinates c3, Coordinates c4) {
    this.coordinates = Collections.unmodifiableList(orderCoordinates(c1, c2, c3, c4));
  }

  /**
   * Orders coordinates in order along the path of the perimeter of the
   * {@code Region}.
   *
   * <p>
   * Coordinates will be ordered as follows:
   *
   * <ol>
   * <li>Minimum Longitude (with minimum Latitude - in case of orthogonal
   * orientation)</li>
   * <li>Maximum Latitude (with minimum Longitude - in case of orthogonal
   * orientation)</li>
   * <li>Maximum Longitude (with maximum Latitude - in case of orthogonal
   * orientation)</li>
   * <li>Minimum Latitude (with maximum Longitude - in case of orthogonal
   * orientation)</li>
   * </ol>
   *
   * @param c1 the first coordinates.
   * @param c2 the second coordinates.
   * @param c3 the third coordinates.
   * @param c4 the forth coordinates.
   * @return a list of {@code Coordinates} ordered along the perimeter of this
   * {@code Region}.
   */
  private List<Coordinates> orderCoordinates(Coordinates c1, Coordinates c2, Coordinates c3, Coordinates c4) {
    List<Coordinates> tmp = new ArrayList(Arrays.asList(c1, c2, c3, c4));

    Coordinates a = tmp.stream().min(Comparator.comparing(Coordinates::getLongitude).thenComparing(Comparator.comparing(Coordinates::getLatitude))).get();
    tmp.remove(a);

    Coordinates b = tmp.stream().max(Comparator.comparing(Coordinates::getLatitude).thenComparing(Comparator.comparing(Coordinates::getLongitude).reversed())).get();
    tmp.remove(b);

    Coordinates c = tmp.stream().max(Comparator.comparing(Coordinates::getLongitude).thenComparing(Comparator.comparing(Coordinates::getLatitude))).get();
    tmp.remove(c);

    Coordinates d = tmp.get(0);

    return Arrays.asList(a, b, c, d);
  }

  /**
   * Retrieve the {@code Coordinates} making up this {@code Region}.
   *
   * @return the {@code Coordinates} making up this {@code Region}.
   */
  public List<Coordinates> getCoordinates() {
    return coordinates;
  }

  /**
   * Retrieve the sides making up this {@code Region}.
   *
   * @return the sides making up this {@code Region}.
   */
  private Set<Line2D> getSides() {
    Set<Line2D> sides = new HashSet<>();

    for (int i = 0; i < coordinates.size(); i++) {
      sides.add(new Line2D.Double(
              coordinates.get(i).getLongitude(),
              coordinates.get(i).getLatitude(),
              coordinates.get((i + 1) % coordinates.size()).getLongitude(),
              coordinates.get((i + 1) % coordinates.size()).getLatitude()
      ));
    }

    return sides;
  }

  /**
   * Indicates if this {@code Region} fully contains the given {@code Region}.
   *
   * <p>
   * A region is fully contained within another region if all of its coordinates
   * fall within its boundary.
   *
   * @param region the {@code Region} to check.
   * @return {@code true} if this {@code Region} fully contains the given
   * {@code Region}., {@code false} otherwise.
   */
  public boolean contains(Region region) {
    return region.getCoordinates().stream().allMatch((c) -> (this.contains(c)));
  }

  /**
   * Indicates if this {@code Region} fully contains ALL of the given
   * {@code Rectangles}.
   *
   * <p>
   * A region is fully contained within another region if all of its coordinates
   * fall within its boundary.
   *
   * @param regions the regions to check.
   * @return {@code true} if this {@code Region} fully contains ALL of the given
   * {@code Rectangles}, {@code false} otherwise.
   */
  public boolean containsAll(List<? extends Region> regions) {
    return regions.stream().allMatch((region) -> (this.contains(region)));
  }

  /**
   * Indicates if the given {@code Coordinates} is contained within this
   * {@code Region}.
   *
   * @param c the {@code Coordinates} to check.
   * @return {@code true} if the given {@code Coordinates} is contained within
   * this {@code Region}, {@code false} otherwise.
   */
  public boolean contains(Coordinates c) {
    int hits = 0;

    double lastx = coordinates.get(coordinates.size() - 1).getLongitude();
    double lasty = coordinates.get(coordinates.size() - 1).getLatitude();
    double curx, cury;

    for (int i = 0; i < coordinates.size(); lastx = curx, lasty = cury, i++) {
      curx = coordinates.get(i).getLongitude();
      cury = coordinates.get(i).getLatitude();

      if (cury == lasty) {
        continue;
      }

      double leftx;

      if (curx < lastx) {
        if (c.getLongitude() >= lastx) {
          continue;
        }

        leftx = curx;
      } else {
        if (c.getLongitude() >= curx) {
          continue;
        }

        leftx = lastx;
      }

      double test1, test2;

      if (cury < lasty) {
        if (c.getLatitude() < cury || c.getLatitude() >= lasty) {
          continue;
        }

        if (c.getLongitude() < leftx) {
          hits++;
          continue;
        }

        test1 = c.getLongitude() - curx;
        test2 = c.getLatitude() - cury;
      } else {
        if (c.getLatitude() < lasty || c.getLatitude() >= cury) {
          continue;
        }

        if (c.getLongitude() < leftx) {
          hits++;

          continue;
        }

        test1 = c.getLongitude() - lastx;
        test2 = c.getLatitude() - lasty;
      }

      if (test1 < (test2 / (lasty - cury) * (lastx - curx))) {
        hits++;
      }
    }

    return ((hits & 1) != 0);
  }

  /**
   * Indicates if this {@code Region} intersects the given {@code Region} at any
   * point.
   *
   * <p>
   * A region intersects another if any of their perimeter lines intersect.
   *
   * @param region another {@code Region}.
   * @return {@code true} if this {@code Region} intersects the given
   * {@code Region} at any point, {@code false} otherwise.
   */
  public boolean intersects(Region region) {
    return this.getSides().stream().anyMatch((side) -> (region.getSides().stream().anyMatch((otherSide) -> (side.intersectsLine(otherSide)))));
  }

  /**
   * Indicates if this {@code Region} overlaps the given {@code Region}.
   *
   * <p>
   * A region overlaps another if either of the following a true:
   *
   * <ol>
   * <li>Any coordinates are within the boundary of another region.</li>
   * <li>Any lines making up the perimeter intersect the perimeter of another
   * region.</li>
   * </ol>
   *
   * @param region the given {@code Region}.
   * @return {@code true} if this {@code Region} overlaps the given
   * {@code Region}, {@code false} otherwise.
   */
  public boolean overlaps(Region region) {
    // check if either region contains at least one coordinate from the other region
    if (region.getCoordinates().stream().anyMatch((c) -> (this.contains(c)))
            || this.getCoordinates().stream().anyMatch((c) -> (region.contains(c)))) {
      return true;
    }

    // check if the regions intersect at any point
    return this.intersects(region);
  }

}
