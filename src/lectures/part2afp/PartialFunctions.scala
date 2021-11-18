package lectures.part2afp

object PartialFunctions extends App{
  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] = Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 52
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 52
    case 3 => 999
  }
  // {1, 2, 5} => Int

  // shorthand
  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 52
    case 3 => 999
  } // partial function value

  println(aPartialFunction(2))
  //  println(aPartialFunction(10))

  // PF utilities
  println(aPartialFunction.isDefinedAt(10))

  // lift PF to total
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2))
  println(lifted(8))

  // orElse
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }
  println(pfChain(2))
  println(pfChain(45))

  // pfs extend normal functions/total functions
  val aTotalFunction: Int => Int = {
    case 1 => 49
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  /*
  Note: Partial functions can have only one parameter type
   */

  /*
  Exercises
  1. Construct a partial function instance yourself
  2. implement a small, dumb chatbot as a partial function
   */

  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(x: Int): Int = x match {
      case 1 => 1234
      case 2 => 5678
      case 3 => 91011112
    }

    override def isDefinedAt(x: Int): Boolean =
      x == 1 || x == 2 || x == 3
  }

  val chatBot: PartialFunction[String, String] = {
    case "hello" => "Greetings"
    case "bye" => "Goodbye"
    case "hold on" => "I am waiting"
    case _ => "I cannot understand what you're saying"
  }

  println("Say hello, goodbye, or hold on")
//  scala.io.Source.stdin.getLines().foreach(line => println("chatbot says: " + chatBot(line)))
  scala.io.Source.stdin.getLines().map(chatBot).foreach(println)
}
