package com.example.aator.auth.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aator.auth.R;
import com.example.aator.auth.Utils.WebUtil;
import com.example.aator.auth.view.MySwipeRefresh;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

/**
 * 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　 ████━████     ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　 　 ┗━━━┓
 * 　　　　　　　　　┃ 神兽保佑　　 ┣┓
 * 　　　　　　　　　┃ 代码无BUG   ┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 * Created by dutingjue on 2016/11/24.
 */

public class PlatoFormFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private CombinedChart combinedChart;
    private Boolean canRefresh = false;
    private ArrayList<BarEntry> barValues = new ArrayList<>();
    private ArrayList<Entry> yValues = new ArrayList<>();
    private ArrayList<String> xValues = new ArrayList<>();
    private ArrayList<Float> yLine = new ArrayList<>();
    private float sum = 0, sum2 = 0;
    private WebThread webThread;
    private MySwipeRefresh mSwipeLayout;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showChart(combinedChart);
                    mSwipeLayout.setRefreshing(false);
                    break;

            }
        }
    };

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        webThread = new WebThread();
        webThread.start();
        View view = inflater.inflate(R.layout.plato_form, container, false);
        combinedChart = (CombinedChart) view.findViewById(R.id.plato);
        mSwipeLayout = (MySwipeRefresh) view.findViewById(R.id.swipe);
        mSwipeLayout.setColorSchemeColors(Color.BLUE);
        mSwipeLayout.setDistanceToTriggerSync(100);
        mSwipeLayout.setOnRefreshListener(this);
        return view;
    }


    //设置坐标轴
    private void showChart(CombinedChart combinedChart) {

        CombinedData combinedData = new CombinedData(xValues);
        combinedData.setData(getBarData(barValues));
        combinedData.setData(getLineData());
        combinedChart.setData(combinedData);
        combinedChart.setDescription("");
        combinedChart.setBorderWidth(2f);
        combinedChart.setBorderColor(Color.RED);
        combinedChart.animateXY(2000, 2000);
        combinedChart.getViewPortHandler().setMaximumScaleX(2);
        combinedChart.setScaleYEnabled(false);
        combinedChart.getViewPortHandler().setMaximumScaleY(2);
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        YAxis yAxisLeft = combinedChart.getAxisLeft();
        yAxisLeft.setDrawGridLines(false);
        YAxis yAxisRight = combinedChart.getAxisRight();
        yAxisRight.setValueFormatter(new PercentFormatter());
        combinedChart.invalidate();

    }

    //设置line
    private LineData getLineData() {

        LineDataSet lineDataSet = new LineDataSet(yValues, "Fail事件百分比" /*显示在比例图上*/);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.parseColor("#cc3333"));// 显示颜色
        lineDataSet.setCircleColor(Color.parseColor("#cc3333"));// 圆形的颜色
        lineDataSet.setFillColor(Color.parseColor("#cc3333"));
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setValueFormatter(new PercentFormatter());
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        return lineData;
    }

    //设置bar
    private BarData getBarData(ArrayList<BarEntry> barValues) {
        BarDataSet barDataSet = new BarDataSet(barValues, "Fail事件");
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barDataSet.setColor(Color.parseColor("#3366ff"));
        barDataSet.setHighlightEnabled(false);
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        return barData;
    }

    @Override
    public void onRefresh() {
        Log.i("refresh", "aaa");

        webThread = new WebThread();
        webThread.start();


    }

    //获取数据
    private class WebThread extends Thread {
        @Override
        public void run() {
            xValues = new ArrayList<>();
            barValues = new ArrayList<>();
            yLine = new ArrayList<>();
            yValues = new ArrayList<>();
            sum = 0;
            sum2 = 0;
            SoapObject soapObject = new SoapObject(WebUtil.SERVICE_NS, WebUtil.FAILDATA);
            SoapObject response = WebUtil.getWebData(soapObject);
            SoapObject detail1 = (SoapObject) response.getProperty(0);
            SoapObject detail2 = (SoapObject) detail1.getProperty(2);
            SoapObject detail3 = (SoapObject) detail2.getProperty(0);
            for (int i = detail3.getPropertyCount() - 1; i >= 0; i--) {
                SoapObject item = (SoapObject) detail3.getProperty(i);
                String x_1 = item.getProperty("testitem").toString();
                String x = x_1.split("-")[0];
                xValues.add(x);
                int num = Integer.parseInt(item.getProperty("COUNT").toString());
                barValues.add(new BarEntry(num, detail3.getPropertyCount() - i - 1));
                yLine.add((float) num);
                sum += num;
            }
            for (int i = 0; i < yLine.size(); i++) {
                sum2 += yLine.get(i);
                Entry lineY = new Entry((sum2 / sum * 100), i);
                yValues.add(lineY);
            }

            mHandler.sendEmptyMessage(0);
        }
    }


}
