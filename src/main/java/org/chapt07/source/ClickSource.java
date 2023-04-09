package org.chapt07.source;

import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.example.model.GroupMessage;

import java.util.Date;

public class ClickSource extends RichSourceFunction<GroupMessage> {
    private boolean isRunning = true;

    @Override
    public void run(SourceContext sourceContext) throws Exception {
        int i = 0;
        while (isRunning) {
            Thread.sleep(1000);
            GroupMessage message = new GroupMessage();
            message.setTimestamp(new Date().getTime());
            message.setGroupId(String.valueOf(i++));
            message.setSummary("你又是个什么鬼？");

            sourceContext.collect(message);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
