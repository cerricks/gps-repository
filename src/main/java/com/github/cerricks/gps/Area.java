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

/**
 * An {@code Area} is a rectangular geographical region (without orientation)
 * defined by 2 diagonal coordinates.
 *
 * @author Clifford Errickson
 * @since 1.0
 */
public class Area extends Region {

  private final String id;

  /**
   * Creates an {@code Area} given 2 diagonal coordinates of a rectangle.
   *
   * @param id the id of this {@code Area}.
   * @param c1 the first diagonal coordinate of the rectangular area.
   * @param c2 the second diagonal coordinate of the rectangular area.
   */
  public Area(String id, Coordinates c1, Coordinates c2) {
    super(c1, c2, new Coordinates(c1.getLatitude(), c2.getLongitude()), new Coordinates(c2.getLatitude(), c1.getLongitude()));
    this.id = id;
  }

  /**
   * Retrieve the id of this {@code Area}.
   *
   * @return the id of this {@code Area}.
   */
  public String getId() {
    return id;
  }

}
