package org.chapt01;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.example.model.GroupMessage;

import java.time.Duration;

public class Watermark {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setAutoWatermarkInterval(200);

        DataStreamSource<String> dataStreamSource = env.readTextFile("input/word.txt");

        // 有序流的 watermark
        dataStreamSource.assignTimestampsAndWatermarks(WatermarkStrategy
                .<String>forMonotonousTimestamps()
                .withTimestampAssigner(new SerializableTimestampAssigner<String>() {

                    @Override
                    public long extractTimestamp(String data, long l) {
                        // 提取数据里面的事件时间
                        return 0;
                    }
                }));

        // 乱序流的 watermark
        dataStreamSource.assignTimestampsAndWatermarks(WatermarkStrategy.<String>forBoundedOutOfOrderness(Duration.ofSeconds(2))
                .withTimestampAssigner(new SerializableTimestampAssigner<String>() {
                    @Override
                    public long extractTimestamp(String s, long l) {
                        return 0;
                    }
                }));

        env.execute();
    }

}
