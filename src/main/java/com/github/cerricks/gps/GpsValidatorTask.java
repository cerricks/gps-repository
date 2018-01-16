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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A {@link Task} for validating GPS coordinates from a file.
 *
 * @author Clifford Errickson
 * @since 1.0
 */
public class GpsValidatorTask extends Task<Void> {

  private static final Logger logger = LogManager.getLogger(GpsValidatorTask.class);

  private static final String MESSAGE_INVALID_AREA_COORDINATES = "Error: Invalid Area Coordinates";
  private static final String MESSAGE_INVALID_SECTORS = "Error: A sector is outside the area or overlaps with another sector";
  private static final String MESSAGE_VALID_AREA_COORDINATES = "Succes: Area Coordinates Valid";
  private static final String MESSAGE_VALID_SECTORS = "Success: All sectors within area and clear of overlap";

  private final File file;
  private final TextArea textArea;
  private final int totalLines;

  /**
   * Creates a GpsValidatorTask.
   *
   * @param file the file to process.
   * @param textArea the field to write output to.
   */
  public GpsValidatorTask(File file, TextArea textArea) {
    this.file = file;
    this.textArea = textArea;
    this.totalLines = countLines(file);
  }

  @Override
  protected Void call() throws Exception {
    writeMessage("Processing file: " + file.getAbsolutePath() + System.lineSeparator());

    String areaId = null;
    Area area = null;
    List<Sector> sectors = new ArrayList();

    try (FileReader fileReader = new FileReader(file)) {
      CSVParser parser = CSVParser.parse(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

      for (CSVRecord record : parser) {
        if (areaId == null || !areaId.equals(record.get("AreaID"))) {
          if (area != null) {
            validateSectors(area, sectors);

            // reset for next area
            area = null;
            sectors.clear();
          }

          writeMessage(System.lineSeparator());

          areaId = record.get("AreaID");

          writeMessage("Area ID = " + areaId + System.lineSeparator());

          Coordinates c1 = new Coordinates(Double.valueOf(record.get("ALat1")), Double.valueOf(record.get("ALon1")));
          Coordinates c2 = new Coordinates(Double.valueOf(record.get("ALat2")), Double.valueOf(record.get("ALon2")));

          // check that area coordinates are valid
          if (c1.getLatitude() == c2.getLatitude()
                  || c1.getLongitude() == c2.getLongitude()) {
            writeMessage(MESSAGE_INVALID_AREA_COORDINATES + System.lineSeparator());
          } else {
            writeMessage(MESSAGE_VALID_AREA_COORDINATES + System.lineSeparator());

            area = new Area(areaId, c1, c2);
          }
        }

        // process sector
        sectors.add(new Sector(
                record.get("SectorID"),
                new Coordinates(Double.valueOf(record.get("c1")), Double.valueOf(record.get("d1"))),
                new Coordinates(Double.valueOf(record.get("c2")), Double.valueOf(record.get("d2"))),
                new Coordinates(Double.valueOf(record.get("c3")), Double.valueOf(record.get("d3"))),
                new Coordinates(Double.valueOf(record.get("c4")), Double.valueOf(record.get("d4")))
        ));

        updateProgress(parser.getCurrentLineNumber(), totalLines);
      }

      validateSectors(area, sectors);

      writeMessage(System.lineSeparator() + "Finished processing file.");

      // update progress to 100% complete
      updateProgress(1, 1);
    } catch (IOException ex) {
      logger.error("Failed to process file", ex);

      writeMessage("Oops. Something went wrong. Check log for details.");
    }

    return null;
  }

  /**
   * Validates that the given sectors are contained within an area and that no
   * sectors overlap one another.
   *
   * @param area the area to check.
   * @param sectors the sectors to check.
   */
  private void validateSectors(Area area, List<Sector> sectors) {
    // check that all sectors are fully contained within the area - STOP if not
    for (Sector sector : sectors) {
      if (!area.contains(sector)) {
        if (logger.isDebugEnabled()) {
          logger.debug("Area [" + area.getId() + "] does not fully contain Sector [" + sector.getId() + "]");
        }

        writeMessage(MESSAGE_INVALID_SECTORS + System.lineSeparator());

        return;
      }
    }

    // check if any of the sectors overlaps another
    Sector s1, s2;
    for (int i = 0; i < sectors.size(); i++) {
      s1 = sectors.get(i);

      for (int j = i + 1; j < sectors.size(); j++) {
        s2 = sectors.get(j);

        if (s1.overlaps(s2)) {
          if (logger.isDebugEnabled()) {
            logger.debug("Sector [" + s1.getId() + "] overlaps Sector [" + s2.getId() + "] in Area [" + area.getId() + "]");
          }

          writeMessage(MESSAGE_INVALID_SECTORS + System.lineSeparator());

          return;
        }
      }
    }

    writeMessage(MESSAGE_VALID_SECTORS + System.lineSeparator());
  }

  /**
   * Writes a message to the output text area field.
   *
   * @param message the message to write.
   */
  private void writeMessage(String message) {
    if (Platform.isFxApplicationThread()) {
      textArea.appendText(message);
    } else {
      Platform.runLater(() -> textArea.appendText(message));
    }
  }

  /**
   * Count the number of lines in the given file.
   *
   * @param file the file.
   * @return the number of lines in the given file.
   */
  private int countLines(File file) {
    int lineCount = 0;

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      while (reader.readLine() != null) {
        lineCount++;
      }
    } catch (IOException ex) {
      logger.error("Failed to count lines in file", ex);
    }

    return lineCount;
  }

}
