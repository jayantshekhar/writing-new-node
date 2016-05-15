package fire.examples.workflow.ml;

import fire.context.JobContext;
import fire.context.JobContextImpl;
import fire.nodes.dataset.NodeDatasetStructured;
import fire.nodes.util.NodePrintFirstNRows;
import fire.nodes.ml.NodeStopWordRemover;
import fire.util.spark.CreateSparkContext;
import fire.workflowengine.ConsoleWorkflowContext;
import fire.workflowengine.DatasetType;
import fire.workflowengine.Workflow;
import fire.workflowengine.WorkflowContext;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Created by nikhilshekhar
 */
public class WorkflowStopWordRemover {

    //--------------------------------------------------------------------------------------

    public static void main(String[] args) {

        // create spark context
        JavaSparkContext ctx = CreateSparkContext.create(args);
        // create workflow context
        WorkflowContext workflowContext = new ConsoleWorkflowContext();
        // create job context
        JobContext jobContext = new JobContextImpl(ctx, workflowContext);

        stopWordRemover(jobContext);

        // stop the context
        ctx.stop();
    }


    //--------------------------------------------------------------------------------------

    // one hot encoder workflow
    private static void stopWordRemover(JobContext jobContext) {

        Workflow wf = new Workflow();

        // csv1 node
        NodeDatasetStructured csv1 = new NodeDatasetStructured(1, "csv1 node", "data/spam.csv", DatasetType.CSV, ",",
                "c1 c2 c3", "double string double",
                "numeric text numeric");

        wf.addNode(csv1);

        // one hot encoder node
        NodeStopWordRemover stopWordRemover = new NodeStopWordRemover(2, "Stop word remover node");
        stopWordRemover.inputCol = "c2";
        stopWordRemover.outputCol = "filtered";
        wf.addLink(csv1, stopWordRemover);

        // print first 3 rows node
        NodePrintFirstNRows nodePrintFirstNRows = new NodePrintFirstNRows(4, "print first 3 rows", 3);
        wf.addLink(stopWordRemover, nodePrintFirstNRows);

        // execute the workflow
        wf.execute(jobContext);

    }


}