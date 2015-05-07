import org.apache.spark.streaming.dstream.DStream

/**
 * Accumulate Data instances into a Long
 */
object DataLong extends App {

  def run(data: DStream[(Int, Data)]) = {

    def updateFunction(newValues: Seq[Data], current: Option[Long]): Option[Long] =
      current match {
        case None      => newValues.headOption.map { d => d.index }
        case Some(sum) => newValues.headOption.map { d => sum + d.index }
      }

    data.updateStateByKey(updateFunction _)
  }

  Base(run)

}