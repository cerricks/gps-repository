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

import java.io.File;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

/**
 * A {@link Service} for validating GPS coordinates in a file.
 *
 * @author Clifford Errickson
 * @since 1.0
 */
public class GpsValidatorService extends Service<Void> {

  private File file;
  private TextArea textArea;

  public GpsValidatorService() {
  }

  /**
   * Sets the file to be processed by this service.
   *
   * @param file the file to be processed by this service.
   */
  public void setFile(File file) {
    this.file = file;
  }

  /**
   * Sets the text area to output results.
   *
   * @param textArea the text area to output results.
   */
  public void setTextArea(TextArea textArea) {
    this.textArea = textArea;
  }

  @Override
  protected Task<Void> createTask() {
    return new GpsValidatorTask(file, textArea);
  }

}
