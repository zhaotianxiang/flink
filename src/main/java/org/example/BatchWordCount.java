package org.example;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class BatchWordCount {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> lineDataSource = env.readTextFile("input/word.txt");

        // 转换成二元祖
        FlatMapOperator<String, Tuple2<String, Long>> flatMapOperator = lineDataSource.flatMap((String line, Collector<Tuple2<String, Long>> collector) -> {
            String[] words = line.split(" ");
            for (String word : words) {
                collector.collect(Tuple2.of(word, 1L));
            }
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));

        // 按照word分组
        UnsortedGrouping<Tuple2<String, Long>> tuple2UnsortedGrouping = flatMapOperator.groupBy(0);

        // 分组聚合
        AggregateOperator<Tuple2<String, Long>> sum = tuple2UnsortedGrouping.sum(1);
        sum.print();

    }
}