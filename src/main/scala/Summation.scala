import org.apache.spark.streaming.dstream.DStream

/**
 * Accumulate Data instances into a Summation
 */
object DataSummation extends App {

  def run(data: DStream[(Int, Data)]) = {

    def updateFunction(newValues: Seq[Data], current: Option[Summation]): Option[Summation] = {
      def process(sum: Long) = newValues.headOption.map { d => Summation(d.id, sum + d.index) }
      current match {
        case None                    => process(0)
        case Some(Summation(_, sum)) => process(sum)
      }
    }

    data.updateStateByKey(updateFunction _)
  }

  Base(run)
}