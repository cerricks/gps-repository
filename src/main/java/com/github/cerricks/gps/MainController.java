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
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The controller for the application's main page.
 *
 * @author Clifford Errickson
 * @since 1.0
 */
public class MainController implements Initializable {

  private static final Logger logger = LogManager.getLogger(MainController.class);

  @FXML
  private MenuItem exitMenuItem;

  @FXML
  private Button btnProcess;

  @FXML
  private Button btnCancelProcess;

  @FXML
  private Button btnReset;

  @FXML
  private TextArea outputTextArea;

  @FXML
  private ProgressBar progressBar;

  @Override
  public void initialize(final URL url, final ResourceBundle rb) {
    if (logger.isDebugEnabled()) {
      logger.debug("Initializing MainController");
    }

    exitMenuItem.setOnAction((ActionEvent t) -> {
      Platform.exit();
    });
  }

  /**
   * Prompt user for file and start task to process.
   */
  @FXML
  protected void process() {
    // display dialog to select file name and path
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open File");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

    File file = fileChooser.showOpenDialog(null);

    if (file == null) {
      return;
    }

    if (logger.isDebugEnabled()) {
      logger.debug("Selected file = " + file.getAbsolutePath());
    }

    GpsValidatorService gpsValidatorService = new GpsValidatorService();
    gpsValidatorService.setFile(file);
    gpsValidatorService.setTextArea(outputTextArea);

    gpsValidatorService.setOnRunning(event -> {
      if (logger.isDebugEnabled()) {
        logger.debug("Processing task: {0}", event.getSource().getTitle());
      }

      setStateProcessing();
    });

    gpsValidatorService.setOnSucceeded(event -> {
      if (logger.isDebugEnabled()) {
        logger.debug("Finished processing task: {0}", event.getSource().getTitle());
      }

      setStateNotProcessing();
    });

    gpsValidatorService.setOnCancelled(event -> {
      if (logger.isDebugEnabled()) {
        logger.debug("Cancelled processing task: {0}", event.getSource().getTitle());
      }

      setStateNotProcessing();
    });

    gpsValidatorService.setOnFailed(event -> {
      if (logger.isDebugEnabled()) {
        logger.error("Failed to process task", event.getSource().getException());
      }

      setStateNotProcessing();
    });

    btnCancelProcess.setOnAction((ActionEvent t) -> {
      gpsValidatorService.cancel();
    });

    progressBar.progressProperty().bind(gpsValidatorService.progressProperty());

    // start the background service
    gpsValidatorService.start();
  }

  /**
   * Reset the screen.
   */
  @FXML
  public void reset() {
    outputTextArea.clear();
  }

  /**
   * Set the state of elements to "Processing".
   */
  private void setStateProcessing() {
    btnProcess.setDisable(true);
    btnProcess.setText("Processing...");
    btnCancelProcess.setDisable(false);
    btnReset.setDisable(true);
    progressBar.setVisible(true);
  }

  /**
   * Set the state of elements to "Not Processing".
   */
  private void setStateNotProcessing() {
    btnProcess.setDisable(false);
    btnProcess.setText("Open File");
    btnCancelProcess.setDisable(true);
    btnReset.setDisable(false);
    progressBar.setVisible(false);
  }

  public void showAboutDialog() {
    final Properties properties = new Properties();

    try {
      properties.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
    } catch (IOException ex) {
      logger.error("Failed to load peroperties", ex);
    }

    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("About");
    alert.setHeaderText(null);

    StringBuilder content = new StringBuilder();
    content.append(properties.getProperty("app.name")).append(System.lineSeparator());
    content.append("Version ").append(properties.getProperty("app.version"));

    alert.setContentText(content.toString());

    alert.showAndWait();
  }

}
