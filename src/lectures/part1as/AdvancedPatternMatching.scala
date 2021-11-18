package lectures.part1as

import scala.annotation.tailrec

object AdvancedPatternMatching extends App{
  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"The only element is $head")
    case head :: tail => println(s"The head is $head, the tail is $tail")
    case _ => "something else"
  }

  /*
  - constants
  - wildcards
  - case classes
  - tuples
  - some special magic like above
   */

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(p: Person): Option[(String, Int)] =
      if (p.age >= 21) Some(p.name, p.age)
      else None

    def unapply(age: Int): Option[String] =
      if (age < 21) Some("Minor")
      else Some("Major")
  }

  val bob = new Person("bob", 25)

  val greeting = bob match {
    case Person(n, a) => s"Hi my name is $n and I'm $a years old"
    case _ => "Not a person"
  }
  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => status
    case _ => ""
  }

  /*
  Exercise
   */
  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }
  object singleDigit {
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }
  val n: Int = 45
  val mathProperty = n match {
    case singleDigit() => "Single digit"
    case even() => "An even number"
    case _ => "no property"
  }

  // infix patterns
  case class Or[A, B](a: A, b: B) // Either

  val either = Or(2, "2")
  val humanDescription = either match {
    case num Or str => s"$num is written as $str"
  }
  println(humanDescription)

  // decomposing sequences
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "Starting with 1 and 2"
    case _ => "something else"
  }

  println(decomposed)

  // custom return types for unapply
  // isEmpty -> boolean
  // get -> something

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(p: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty: Boolean = false
      def get: String = p.name
    }
  }

  println(bob match {
    case PersonWrapper(n) => s"This persons name is $n"
    case _ => "An alien"
  })


}
