/*
Title: Scala Mini Project
Jarvis Developer: Preet Khasakia
 */
import org.apache.spark.sql.{Row, SparkSession}

object scala_object{
  def main(args: Array[String]): Unit = {
    //Function to check CLI arguments if null
    checkCLI()

    //This command is for resolving permission issues. Please uncomment if necessary.
    System.setProperty("hadoop.home.dir", "C:\\winutils")

    //Creating a spark session
    val spark = SparkSession
      .builder()
      .appName("Scala app for hive tables")
      .master("local")
      .config("spark.master", "local")
      .enableHiveSupport()
      .getOrCreate()

    import spark.sql

    //Variables for CLI arguments <source_table backup_table>
    val srcTable = args(0)
    val backupTable = args(1)


    sql(s"DROP TABLE $srcTable")

    //SQl command to create the source table using the first CLI argument
    val createTableSRC =
      s"""CREATE TABLE IF NOT EXISTS $srcTable(
         |src_foo STRING,
         |src_bar BIGINT,
         |src_baz FLOAT)
         |PARTITIONED BY (src_part STRING)""".stripMargin
    sql(createTableSRC)

    //Dynamic partitioning
    sql("set hive.exec.dynamic.partition.mode=nonstrict")

    //SQL insert commands to insert data into the source table
    sql(s"INSERT INTO $srcTable VALUES('Apple', 100, 100.10, 'partition1')")
    sql(s"INSERT INTO $srcTable VALUES('Pencil', 200, 200.20, 'partition2')")
    sql(s"INSERT INTO $srcTable VALUES('Phone', 300, 300.30, 'partition3')")

    sql(s"DROP TABLE $backupTable")

    //SQL command to create the backup table
    val createTableBackup =
      s"""CREATE TABLE IF NOT EXISTS $backupTable(
         |backup_foo STRING,
         |backup_bar BIGINT,
         |backup_baz FLOAT)
         |PARTITIONED BY (backup_part STRING)""".stripMargin
    sql(createTableBackup)

    //Dynamic partitioning
    sql("set hive.exec.dynamic.partition.mode=nonstrict")

    //Inserting data into the backup table
    sql(s"INSERT INTO $backupTable VALUES('Apple', 100, 100.10, 'partition1')")
    sql(s"INSERT INTO $backupTable VALUES('Pencil', 200, 200.20, 'partition2')")
    sql(s"INSERT INTO $backupTable VALUES('Phone', 300, 300.30, 'partition3')")

    /*Pseudocode for overwriting backup table
     If src partition changes{
        then
            overwrite the data in backup table from source table.
    */
    sql(s"INSERT OVERWRITE TABLE $backupTable FROM $srcTable SELECT *")

    //Left join command to show the user both tables
    sql(s"SELECT * FROM $srcTable LEFT JOIN $backupTable ON $srcTable.src_part = $backupTable.backup_part ORDER BY $srcTable.src_bar").show()



  }
  //Function to check CLI arguments
  def checkCLI(){
    val args = ""
    if(args.length == 0){
      println("No arguments BUDDY!")
    }
  }

}