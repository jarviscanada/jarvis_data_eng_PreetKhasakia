import org.apache.spark.sql.Row;
import org.finra.hiveqlunit.resources.ResourceFolderResource;
import org.finra.hiveqlunit.resources.TextLiteralResource;
import org.finra.hiveqlunit.rules.SetUpHql;
import org.finra.hiveqlunit.rules.TestDataLoader;
import org.finra.hiveqlunit.rules.TestHiveServer;
import org.finra.hiveqlunit.script.MultiExpressionScript;
import org.junit.Assert;

public class srcTableTest {
    //Builds the HiveContext instance
    public static TestHiveServer hiveServer = new TestHiveServer();

    //Provides methods for loading data into a Hive Table
    public static TestDataLoader loader = new TestDataLoader(hiveServer);

    //Takes in the hive script and runs it on the Hive server
    public static SetUpHql prepSrc = new SetUpHql(hiveServer, new MultiExpressionScript(
            new TextLiteralResource("CREATE TABLE IF NOT EXISTS src (foo STRING," +
                    "bar BIGINT, baz FLOAT) PARTITIONED BY (part STRING) ROW FORMAT DELIMITED" +
                    "FIELDS TERMINATED BY ','")
    ));

    public void test(){

        //Using data to test the output
        loader.loadDataIntoTable("src", new ResourceFolderResource("C:/Users/preet/src_table.txt"));
        Row[] results = (Row[]) hiveServer.getHiveContext().sql("SELECT foo FROM src").collect();

        Assert.assertEquals(1, results.length);
        Assert.assertEquals(results[0].get(0), "bob, 500");
        System.out.println("HELLO");
    }
}

