package org.chapt07;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.chapt07.source.ClickSource;
import org.example.model.GroupMessage;

import java.time.Duration;

public class EventTimeJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        SingleOutputStreamOperator<GroupMessage> stream = env.addSource(new ClickSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<GroupMessage>forBoundedOutOfOrderness(Duration.ZERO)
                .withTimestampAssigner((SerializableTimestampAssigner<GroupMessage>) (groupMessage, l) -> groupMessage.getTimestamp()));;
        stream.keyBy(o->o.getGroupId()).process(new KeyedProcessFunction<String, GroupMessage, Object>() {
            @Override
            public void processElement(GroupMessage groupMessage, KeyedProcessFunction<String, GroupMessage, Object>.Context context, Collector<Object> collector) throws Exception {
                System.out.println(context.getCurrentKey()+",数据到达时间:"+context.timerService().currentProcessingTime()+" watermark: "+context.timerService().currentWatermark());
                // 过一段时间打印一次日志
                Long current = context.timerService().currentWatermark();
                collector.collect("数据到达时间:"+current);

                // 10 s later timer
                context.timerService().registerEventTimeTimer(current+10*1000);
            }

            // 按照⌚事件时间顺序执行，目的？
            @Override
            public void onTimer(long timestamp, KeyedProcessFunction<String, GroupMessage, Object>.OnTimerContext ctx, Collector<Object> out) throws Exception {
                super.onTimer(timestamp, ctx, out);
                System.out.println(ctx.getCurrentKey()+":10秒后定时器触发执行，时间："+ ctx.timerService().currentProcessingTime()+"watermark:"+ctx.timerService().currentWatermark());
            }
        });

        env.execute();

    }
}