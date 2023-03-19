package org.example;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class BoundedStreamWordCount {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> dataStreamSource = env.readTextFile("input/word.txt");
        // 转换成二元祖
        SingleOutputStreamOperator<Tuple2<String, Long>> dataStream = dataStreamSource.flatMap((String line, Collector<Tuple2<String, Long>> collector) -> {
            String[] words = line.split(" ");
            for (String word : words) {
                collector.collect(Tuple2.of(word, 1L));
            }
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));

        // 按照word分组
        KeyedStream<Tuple2<String, Long>, String> tuple2StringKeyedStream = dataStream.keyBy(d -> d.f0);

        // 分组聚合
        SingleOutputStreamOperator<Tuple2<String, Long>> sum = tuple2StringKeyedStream.sum(1);
        sum.print();
        env.execute();

    }
}