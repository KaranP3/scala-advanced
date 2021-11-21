package lectures.part4implicits

object ImplicitsIntro extends App{

  val pair = "Karan" -> "555"
  val intPair = 1 -> 2

  case class Person(name: String) {
    def greet: String = s"Hi my name is $name"
  }

  implicit def fromStringToPerson(name: String): Person = Person(name)

  println("Karan".greet())

//  class A {
//    def greet: Int = 2
//  }
//  implicit def fromStringToA(str:String): A = new A // will not compile

  // implicit parameters
  def increment(x: Int)(implicit amount: Int): Int = x + amount
  implicit val defaultAmount = 10

  println(increment(2))
  // not default args


}
