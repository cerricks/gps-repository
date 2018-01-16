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
 * A {@code Sector} is a geographical region defined by 4 coordinates.
 *
 * @author Clifford Errickson
 * @since 1.0
 */
public class Sector extends Region {

  private final String id;

  /**
   * Creates a {@code Sector} with given coordinates. It is not necessary to
   * provide coordinates in any particular order.
   *
   * @param id the id of this {@code Sector}.
   * @param c1 the first coordinates.
   * @param c2 the second coordinates.
   * @param c3 the third coordinates.
   * @param c4 the fourth coordinates.
   */
  public Sector(String id, Coordinates c1, Coordinates c2, Coordinates c3, Coordinates c4) {
    super(c1, c2, c3, c4);
    this.id = id;
  }

  /**
   * Retrieve the id of this {@code Sector}.
   *
   * @return the id of this {@code Sector}.
   */
  public String getId() {
    return id;
  }

}
