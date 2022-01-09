package lectures.part4implicits

import scala.annotation.tailrec
import scala.language.implicitConversions

object PimpMyLibrary extends App {

  // 2.isPrime
  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)

    def times(f: () => Unit): Unit = {
      @tailrec
      def timesHelper(n: Int): Unit = {
        if (n <= 0) ()
        else {
          f()
          timesHelper(n - 1)
        }
      }

      timesHelper(value)
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] =
        if (n <= 0) List()
        else concatenate(n - 1) ++ list

      concatenate(value)
    }
  }

//  implicit class RicherInt(richInt: RichInt) {
//    def isOdd: Boolean = richInt.value % 2 != 0
//  }

  println(42.isEven) // new RichInt(42).isEven

  // type enrichment = pimping

  // compiler does not do multiple implicit searches
//  42.isOdd

  /*
  Enrich the String class
    - asInt
    - encrypt
      John -> Lqjp

   Keep enriching the Int class
    - times(function)
      3.times(() => ....)
    - *
      3 * List(1, 2) => List(1, 2, 1, 2, 1, 2)
   */

  implicit class RichString(val s: String) extends AnyVal {
    def asInt: Int = Integer.valueOf(s)
    def encrypt(cypherDistance: Int): String =
      s.map(c => (c + cypherDistance).asInstanceOf[Char])
  }

  println("3".asInt + 4)
  println("John".encrypt(2))
  3.times(() => println("Hello"))
  println(4 * List(1, 2))

  // "3" / 4
  implicit def stringToInt(str: String): Int = Integer.valueOf(str)
  println("6" / 2) // stringToInt("6") / 2

  // equivalent implicit class RichAltInt(value: Int)
  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  // danger zone
  implicit def IntToBoolean(i: Int): Boolean = i == 1

  /*
  if (n) do something
  else do something else
   */

  val aConditionValue = if (3) "ok" else "something is wrong"
  println(aConditionValue)

  /*
  tips:
    - keep type enrichment to implicit classes and type classes
    - avoid implicit defs as much as possible
    - package implicits clearly, bring into scope only what you need
    - IF you need conversions, make them specific
   */
}
