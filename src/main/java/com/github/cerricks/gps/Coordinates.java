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
 * Represents geographical latitude and longitude coordinates as decimal
 * degrees.
 *
 * @author Clifford Errickson
 * @since 1.0
 */
public class Coordinates {

  private final double latitude;
  private final double longitude;

  /**
   * Create {@code Coordinates} with given latitude and longitude decimal
   * degrees.
   *
   * @param latitude the degrees of latitude.
   * @param longitude the degrees of longitude.
   */
  public Coordinates(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public String toString() {
    return new StringBuilder().append("(").append(latitude).append(", ").append(longitude).append(")").toString();
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 59 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(this.latitude) >>> 32));
    hash = 59 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(this.longitude) >>> 32));

    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    final Coordinates other = (Coordinates) obj;

    if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude)) {
      return false;
    }

    if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude)) {
      return false;
    }

    return true;
  }

  /**
   * Get the degrees of latitude.
   *
   * @return the degrees of latitude.
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * Get the degrees of longitude.
   *
   * @return the degrees of longitude.
   */
  public double getLongitude() {
    return longitude;
  }

}
