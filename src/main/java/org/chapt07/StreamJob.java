package org.chapt07;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.chapt07.source.ClickSource;
import org.example.model.GroupMessage;

import java.time.Duration;

public class StreamJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        SingleOutputStreamOperator<GroupMessage> stream = env.addSource(new ClickSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<GroupMessage>forBoundedOutOfOrderness(Duration.ZERO)
                        .withTimestampAssigner((SerializableTimestampAssigner<GroupMessage>) (groupMessage, l) -> groupMessage.getTimestamp()));

        SingleOutputStreamOperator<Object> processedStream = stream.process(new ProcessFunction<GroupMessage, Object>() {
            @Override
            public void processElement(GroupMessage groupMessage, ProcessFunction<GroupMessage, Object>.Context context, Collector<Object> collector) throws Exception {
                collector.collect(groupMessage);
                System.out.println(context.timerService().currentProcessingTime());
                System.out.println(context.timerService().currentWatermark());
                System.out.println(getRuntimeContext().getIndexOfThisSubtask());
            }
        });


        env.execute();

    }
}