package glug.model

import java.util.Collections.emptyMap

import glug.model.time.{LogInstant, LogInterval}
import org.scalatest.{FlatSpec, Matchers}

class ThreadedSystemTest extends FlatSpec with Matchers {

  "ThreadedSystem" should "not error when reporting interval covered by empty threads" in {
    val threadedSystem = new ThreadedSystem
    threadedSystem.getOrCreateThread("Thread without sigint")
    threadedSystem.getIntervalCoveredByAllThreads
  }

  it should "understand that threads are different dammit" in {
    val threadedSystem = new ThreadedSystem
    threadedSystem.getOrCreateThread("Timeout guard")
    threadedSystem.getOrCreateThread("timerFactory")
    threadedSystem.getNumThreads shouldBe 2
  }

  it should "handle getting total interval even if some threads have no interval data" in {
    val threadedSystem = new ThreadedSystem
    val interval = LogInterval(LogInstant(3000L), LogInstant(7000L))
    threadedSystem.getOrCreateThread("A1")
    threadedSystem.getOrCreateThread("B2").add(new SignificantInterval(emptyMap(), interval))
    threadedSystem.getOrCreateThread("C3")
    threadedSystem.getIntervalCoveredByAllThreads shouldBe interval
  }

}