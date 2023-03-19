package org.example;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import org.example.model.GroupMessage;
import org.example.model.GroupSession;

public class BatchGroupChatMerge {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> lineDataSource = env.readTextFile("input/groupChat.txt");

        // 转换成二元祖 <群号,群消息>
        FlatMapOperator<String, Tuple2<String, GroupMessage>> flatMapOperator = lineDataSource.flatMap((String line, Collector<Tuple2<String, GroupMessage>> collector) -> {
            GroupMessage groupMessage = JSON.parseObject(line, GroupMessage.class);
            collector.collect(Tuple2.of(groupMessage.getGroupId(), groupMessage));
        }).returns(Types.TUPLE(Types.STRING, Types.GENERIC(GroupMessage.class)));

        // 按照群号分组
        UnsortedGrouping<Tuple2<String, GroupMessage>> tuple2UnsortedGrouping = flatMapOperator.groupBy(0);

//        // 按照群号分组聚合 聚合操作？ 怎么记住上一次的聚合结果
//        tuple2UnsortedGrouping.combineGroup((Tuple2<String, GroupMessage> groupMessage, Collector<GroupSession> collector) -> {
//            GroupSession groupSession = new GroupSession();
//            groupSession.getSummarys().add(groupMessage.f1.getSummary());
//            groupSession.getUserUids().add(groupMessage.f1.getUserUid());
//            collector.collect(groupSession);
////            collector.collect(Tuple2.of(groupMessage.getField(0), groupSession));
//        });
////        sum.print();
//
//        flatMapOperator.print();

    }
}