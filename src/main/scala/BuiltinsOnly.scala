import org.apache.spark.streaming.dstream.DStream


/**
 * Use only standard Scala classes
 */
object BuiltinsOnly  extends App{

  def run(data: DStream[(Int, Data)]) = {
    // convert Data to simple Scala representation
    val string = data.map { p => (p._1, (p._1, p._2.toString)) }

    def updateFunction(newValues: Seq[(Int, String)], current: Option[(Int, String)]): Option[(Int, String)] =
      newValues.headOption

    string.updateStateByKey(updateFunction _)
  }
  
    Base(run)
}