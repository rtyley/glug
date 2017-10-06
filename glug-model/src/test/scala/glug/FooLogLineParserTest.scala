package glug

import glug.model.ThreadedSystem
import org.scalatest._

import scala.collection.mutable.Stack

class FooLogLineParserTest extends FlatSpec with Matchers {

  "A line parser" should "understand a sample line" in {
    val sample = "2017-10-04 09:08:00,037 [application-akka.actor.default-dispatcher-745] INFO  lib.DashboardData$ - completed lib.DashboardData$::topPagesQueryNonContentAgg in 121 ms elapsed, 116 ms elasticsearch"
    val boo = new FooLogLineParser(new ThreadedSystem()).elasticSearchCompletedLine.parse(sample)
    println(boo.get.value)
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = new Stack[Int]
    a[NoSuchElementException] should be thrownBy {
      emptyStack.pop()
    }
  }
}