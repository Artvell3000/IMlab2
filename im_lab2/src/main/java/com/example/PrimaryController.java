package com.example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import com.example.Model.Cord;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class PrimaryController {
    Timeline timeline = null;
    final int nSeries = 2;
    final double delay = 50;
    ModelArray models = null;

    private class ModelArray{
        private int n;
        private Model[] models;

        ModelArray(int n, double... prices){
            this.n = n;
            models = new Model[n];
            int i = 0;
            for(double p : prices){
                models[i] = new Model(p);
                i++;
            }
        }

        public void update(double... prices){
            int i = 0;
            for(double p : prices){
                models[i].setPrice(p);
                i++;
            }
        }

        public Cord getNextState(int i){
            return models[i].getNextState();
        }

        public Cord getNowState(int i){
            return models[i].getNowState();
        }
    }

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    @FXML
    private TextField edtFirst;

    @FXML
    private TextField edtSecond;

    @FXML
    private LineChart<Number, Number> chart;

    @FXML
    XYChart.Series<Number, Number> series1 = null;

    @FXML
    XYChart.Series<Number, Number> series2 = null;

    ArrayList<XYChart.Series<Number, Number>> series = new ArrayList<XYChart.Series<Number, Number>>();

    @FXML
    private void onStart(ActionEvent event){
        if(series1 == null){
            models = new ModelArray(nSeries, Double.parseDouble(edtFirst.getText()),Double.parseDouble(edtSecond.getText()));

            for(int i=0;i<nSeries;i++){
                series1 = new XYChart.Series<>();
                chart.getData().add(series1);
                Cord p1 = models.getNowState(i);
                series1.getData().add(new XYChart.Data<>(p1.x, p1.y));
                series.add(series1);
            }

            chart.getStylesheets().add(App.class.getResource("chartCSS.css").toExternalForm());
        }


        if(timeline == null){
            timeline = new Timeline();
            timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(delay), e -> {
                    for(int i=0;i<nSeries;i++){
                        Cord p1 = models.getNextState(i);
                        XYChart.Data data1 = new XYChart.Data<>(p1.x, p1.y);

                        series.get(i).getData().add(data1);

                        Tooltip tooltip1 = new Tooltip();
                        Tooltip.install(data1.getNode(), tooltip1);

                        data1.getNode().setOnMouseEntered(eu -> {
                            tooltip1.setText("(" + data1.getXValue() + ", " + data1.getYValue() + ")");
                            tooltip1.show(data1.getNode(), eu.getScreenX(), eu.getScreenY());
                        });

                        data1.getNode().setOnMouseExited(eu -> {
                            tooltip1.hide();
                        });
                    }
                })
            );
        }
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        btnStart.setVisible(false);
        btnStop.setVisible(true);
    }

    @FXML
    private void onStop(ActionEvent event){

        timeline.stop();

        btnStart.setVisible(true);
        btnStop.setVisible(false);
    }

    @FXML
    private void onUpdate(ActionEvent event){

        models.update(Double.parseDouble(edtFirst.getText()), Double.parseDouble(edtSecond.getText()));
        System.out.println("update");
    }
}
