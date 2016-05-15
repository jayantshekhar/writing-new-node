package fire.examples.workflow.sessionize;

import fire.context.JobContext;
import fire.context.JobContextImpl;
import fire.nodes.logs.NodeApacheFileAccessLog;
import fire.nodes.util.NodePrintFirstNRows;
import fire.util.spark.CreateSparkContext;
import fire.workflowengine.ConsoleWorkflowContext;
import fire.workflowengine.Workflow;
import fire.workflowengine.WorkflowContext;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Created by jayantshekhar
 */
public class WorkflowSessionize {

    //--------------------------------------------------------------------------------------

    public static void main(String[] args) {

        // create spark context
        JavaSparkContext ctx = CreateSparkContext.create(args);
        // create workflow context
        WorkflowContext workflowContext = new ConsoleWorkflowContext();
        // create job context
        JobContext jobContext = new JobContextImpl(ctx, workflowContext);

        execute(jobContext);

        // stop the context
        ctx.stop();
    }

    //--------------------------------------------------------------------------------------

    private static void execute(JobContext jobContext) {

        Workflow wf = new Workflow();

        // pdf node
        NodeApacheFileAccessLog log = new NodeApacheFileAccessLog(1, "apache log node", "data/apacheaccesslog_new.log");
        wf.addNode(log);

        // print first 3 rows node
        NodePrintFirstNRows nodePrintFirstNRows = new NodePrintFirstNRows(2, "print first 3 rows", 50);
        wf.addLink(log, nodePrintFirstNRows);

        wf.execute(jobContext);
    }

}