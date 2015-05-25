
import org.apache.spark.streaming._
import org.apache.spark._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.StreamingContext._
import java.util.Arrays
import scala.util.Try
import org.apache.spark.streaming.dstream.DStream

/**
 * Trivial test program that performs combineByKey using only
 * standard Scala classes.
 *
 * Does not use print but rather explicitly forces each RDD
 * Specify max rate
 *
 */
object TrivialCombineByKey extends App {

  val conf = new SparkConf()
    //.setMaster("local[*]")
    .setAppName("Example")
    .set("spark.executor.memory", "1g")
    .set("spark.cleaner.ttl", "300")
    .set("spark.streaming.receiver.maxRate","400000")
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

  val streamingContext = new StreamingContext(conf, Seconds(2))

  streamingContext.checkpoint("/tmp/spark")

  val dStream1 = streamingContext.socketTextStream("localhost",
    12345, StorageLevel.MEMORY_AND_DISK_SER)
    
     val dStream2 = streamingContext.socketTextStream("localhost",
    12345, StorageLevel.MEMORY_AND_DISK_SER)
    
    val dStream = dStream1.union(dStream2)
    
    type Datum = (Int,String)

  val string:DStream[(Int,Datum)] = dStream.map { s =>
    val p = s.split(" ")
    val id = p(0).toInt
    (id, (id, p(1)))
  }

  

  val updated = string.combineByKey[Datum](v=>v, 
      (a:Datum,v:Datum) => v,
      (a:Datum,v:Datum)=> v,
      new HashPartitioner(4),
      true)

  updated.foreachRDD(rdd => {
      rdd.foreach {
      record => 
      }
      }
     )


 //  updated.saveAsTextFiles("data")
  streamingContext.start()
  streamingContext.awaitTermination()

}
