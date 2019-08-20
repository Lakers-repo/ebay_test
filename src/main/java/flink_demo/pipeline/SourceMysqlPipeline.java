package flink_demo.pipeline;

import flink_demo.source.SourceFromMySQL;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class SourceMysqlPipeline {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        streamExecutionEnvironment.addSource(new SourceFromMySQL()).print();

        streamExecutionEnvironment.execute("Flink add data source from MySQL");
    }
}
