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
import org.junit.Test;

/**
 * Unit test for {@link Area}.
 *
 * @author Clifford Errickson
 * @since 1.0
 */
public class AreaTest {

  /**
   * Test creating a rectangular area with diagonal coordinates.
   */
  @Test
  public void testConstructor() {
    Area area = new Area("Area 1", new Coordinates(5, 1), new Coordinates(1, 4));

    List<Coordinates> coordinates = area.getCoordinates();

    assertNotNull(coordinates);
    assertFalse(coordinates.isEmpty());
    assertEquals(4, coordinates.size());
    assertEquals(new Coordinates(1, 1), coordinates.get(0));
    assertEquals(new Coordinates(5, 1), coordinates.get(1));
    assertEquals(new Coordinates(5, 4), coordinates.get(2));
    assertEquals(new Coordinates(1, 4), coordinates.get(3));
  }

  /**
   * Test creating a rectangular area with diagonal coordinates (including
   * negative degrees).
   */
  @Test
  public void testConstructor_nagativeDegrees() {
    Area area = new Area("Area 2", new Coordinates(5, -1), new Coordinates(-1, 4));

    List<Coordinates> coordinates = area.getCoordinates();

    assertNotNull(coordinates);
    assertFalse(coordinates.isEmpty());
    assertEquals(4, coordinates.size());
    assertEquals(new Coordinates(-1, -1), coordinates.get(0));
    assertEquals(new Coordinates(5, -1), coordinates.get(1));
    assertEquals(new Coordinates(5, 4), coordinates.get(2));
    assertEquals(new Coordinates(-1, 4), coordinates.get(3));
  }

}
