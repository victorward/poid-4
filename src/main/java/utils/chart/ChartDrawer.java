package utils.chart;

import org.apache.commons.math3.complex.Complex;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class ChartDrawer extends JFrame {

    public static void drawChart(JFreeChart chart){
        ChartDrawer cd = new ChartDrawer();
        ChartPanel cp = new ChartPanel(chart);
        cd.setContentPane(cp);
        cd.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cd.pack();
        cd.setVisible(true);
    }

    public static JFreeChart drawChart(Double[] values, String name){
        final XYSeries phase = new XYSeries("D");

        final XYSeriesCollection dataset = new XYSeriesCollection();

        for (int i = 0; i < values.length; i++){
            phase.add(i, values[i]);
        }

        dataset.addSeries(phase);
        JFreeChart chart = ChartFactory.createXYLineChart(name, "index", "Distance",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        ChartDrawer.drawChart(chart);
        return chart;
    }

    public static JFreeChart drawChart(Integer[] values, String name){
        final XYSeries phase = new XYSeries("D");

        final XYSeriesCollection dataset = new XYSeriesCollection();

        for (int i = 0; i < values.length; i++){
            phase.add(i, values[i]);
        }

        dataset.addSeries(phase);
        JFreeChart chart = ChartFactory.createXYLineChart(name, "index", "Distance",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        //ChartDrawer.drawChart(chart);
        return chart;
    }

    public static void drawChart(Complex[] values, String name){
        final XYSeries phase = new XYSeries("D");

        final XYSeriesCollection dataset = new XYSeriesCollection();

        for (int i = 0; i < values.length; i++){
            phase.add(i, values[i].abs());
        }

        dataset.addSeries(phase);
        JFreeChart chart = ChartFactory.createXYLineChart(name, "index", "Distance",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        ChartDrawer.drawChart(chart);
    }

    public static void drawFromChart(Complex[] values, String name){
        final XYSeries phase = new XYSeries("D");

        final XYSeriesCollection dataset = new XYSeriesCollection();

        for (int i = 0; i < values.length; i++){
            phase.add(i, values[i].getReal());
        }

        dataset.addSeries(phase);
        JFreeChart chart = ChartFactory.createXYLineChart(name, "index", "Distance",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        ChartDrawer.drawChart(chart);
    }
}
