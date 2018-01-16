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

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test for {@link Region}.
 *
 * @author Clifford Errickson
 * @since 1.0
 */
public class RegionTest {

  /**
   * Test of getCoordinates method, of class Region.
   */
  @Test
  public void testGetCoordinates() {
    Region r1 = new Region(
            new Coordinates(38.866694, -77.128092),
            new Coordinates(38.866600, -77.128099),
            new Coordinates(38.866629, -77.126662),
            new Coordinates(38.866724, -77.126666)
    );

    List<Coordinates> coordinates = r1.getCoordinates();

    assertNotNull(coordinates);
    assertFalse(coordinates.isEmpty());
    assertEquals(4, coordinates.size());
    assertEquals(new Coordinates(38.866600, -77.128099), coordinates.get(0));
    assertEquals(new Coordinates(38.866724, -77.126666), coordinates.get(1));
    assertEquals(new Coordinates(38.866629, -77.126662), coordinates.get(2));
    assertEquals(new Coordinates(38.866694, -77.128092), coordinates.get(3));
  }

  /**
   * Test of getCoordinates method, of class Region.
   */
  @Test
  public void testGetCoordinates_orthogonalOrientation() {
    Region rectangle = new Region(new Coordinates(2, 3), new Coordinates(0, 0), new Coordinates(2, 0), new Coordinates(0, 3));

    List<Coordinates> coordinates = rectangle.getCoordinates();

    assertNotNull(coordinates);
    assertFalse(coordinates.isEmpty());
    assertEquals(4, coordinates.size());
    assertEquals(new Coordinates(0, 0), coordinates.get(0));
    assertEquals(new Coordinates(2, 0), coordinates.get(1));
    assertEquals(new Coordinates(2, 3), coordinates.get(2));
    assertEquals(new Coordinates(0, 3), coordinates.get(3));
  }

  /**
   * Test of getCoordinates method, of class Region.
   */
  @Test
  public void testGetCoordinates_nonOrthogonalOrientation() {
    Region rectangle = new Region(new Coordinates(2, 0), new Coordinates(1, 1), new Coordinates(4, 2), new Coordinates(3, 3));

    List<Coordinates> coordinates = rectangle.getCoordinates();

    assertNotNull(coordinates);
    assertFalse(coordinates.isEmpty());
    assertEquals(4, coordinates.size());
    assertEquals(new Coordinates(2, 0), coordinates.get(0));
    assertEquals(new Coordinates(4, 2), coordinates.get(1));
    assertEquals(new Coordinates(3, 3), coordinates.get(2));
    assertEquals(new Coordinates(1, 1), coordinates.get(3));
  }

  @Test
  public void testIntersects_none() {
    Region r1 = new Region(new Coordinates(38.73733, -77.18872), new Coordinates(38.73739, -77.18843), new Coordinates(38.7364, -77.18851), new Coordinates(38.73633, -77.18879));
    Region r2 = new Region(new Coordinates(38.73611, -77.18859), new Coordinates(38.73615, -77.18828), new Coordinates(38.73502, -77.18832), new Coordinates(38.73506, -77.18867));

    assertFalse(r1.intersects(r2) && r2.intersects(r1));
  }

  @Test
  public void testIntersects_partiallyContained() {
    Region r1 = new Region(new Coordinates(38.73733, -77.18872), new Coordinates(38.73739, -77.18843), new Coordinates(38.7364, -77.18851), new Coordinates(38.73633, -77.18879));
    Region r2 = new Region(new Coordinates(38.73706, -77.18855), new Coordinates(38.73737, -77.18671), new Coordinates(38.73712, -77.18656), new Coordinates(38.7368, -77.18833));

    assertTrue(r1.intersects(r2) && r2.intersects(r1));
  }

  @Test
  public void testIntersects_fullyContained() {
    Region r1 = new Region(new Coordinates(38.73474, -77.1892), new Coordinates(38.7376, -77.1892), new Coordinates(38.7376, -77.18611), new Coordinates(38.73474, -77.18611));
    Region r2 = new Region(new Coordinates(38.7365, -77.18755), new Coordinates(38.73647, -77.18614), new Coordinates(38.73633, -77.18614), new Coordinates(38.73637, -77.18755));

    assertFalse(r1.intersects(r2) && r2.intersects(r1));
  }

  @Test
  public void testOverlaps_none() {
    Region r1 = new Region(new Coordinates(38.866694, -77.128092), new Coordinates(38.866600, -77.128099), new Coordinates(38.866629, -77.126662), new Coordinates(38.866724, -77.126666));
    Region r2 = new Region(new Coordinates(38.866065, -77.127876), new Coordinates(38.865238, -77.127959), new Coordinates(38.865241, -77.127819), new Coordinates(38.866077, -77.127735));

    assertFalse(r1.overlaps(r2) && r2.overlaps(r1));
  }

  @Test
  public void testOverlaps_partiallyContained() {
    Region r1 = new Region(new Coordinates(38.73733, -77.18872), new Coordinates(38.73739, -77.18843), new Coordinates(38.7364, -77.18851), new Coordinates(38.73633, -77.18879));
    Region r2 = new Region(new Coordinates(38.73706, -77.18855), new Coordinates(38.73737, -77.18671), new Coordinates(38.73712, -77.18656), new Coordinates(38.7368, -77.18833));

    assertTrue(r1.overlaps(r2) && r2.overlaps(r1));
  }

  @Test
  public void testOverlaps_fullyContained() {
    Region r1 = new Region(new Coordinates(38.73474, -77.1892), new Coordinates(38.7376, -77.1892), new Coordinates(38.7376, -77.18611), new Coordinates(38.73474, -77.18611));
    Region r2 = new Region(new Coordinates(38.7365, -77.18755), new Coordinates(38.73647, -77.18614), new Coordinates(38.73633, -77.18614), new Coordinates(38.73637, -77.18755));

    assertTrue(r1.overlaps(r2) && r2.overlaps(r1));
  }

}
