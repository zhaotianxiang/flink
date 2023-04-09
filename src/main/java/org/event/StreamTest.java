package org.event;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.event.model.Event;

public class StreamTest {
    public static void main(String[] args) throws Exception {
        // nc -lk 40999
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 1.从文件中读取
        DataStreamSource<String> dataStreamSource1 = env.readTextFile("input/click.txt");

        // 2.从集合中读取, 用于测试项目中
        DataStreamSource<String> dataStreamSource = env.fromCollection(Lists.newArrayList("a,b,c"));

        // 3. 从元素读取数据
        DataStreamSource<Event> eventDataStreamSource = env.fromElements(new Event("1", "2", 1000L));

        // 4. 从socket文本流中读取数据
        // 4. 从socket文本流中读取数据
        eventDataStreamSource.print();

        env.execute();

    }
}