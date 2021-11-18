package lectures.part2afp

object CurriesPAF extends App {

  // curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3) // Int => Int = 3 + y
  println(add3(5))

  // METHOD!
  def curriedAdder(x: Int)(y: Int): Int = x + y

  // lifting = ETA-EXPANSION
  val add4: Int => Int = curriedAdder(4)
  println(add4(10))

  // functions != methods (JVM limitation)
  def inc(x: Int): Int = x + 1
  List(1, 2, 3).map(x => inc(x)) // ETA-expansion

  // Partial function applications
  val add5 = curriedAdder(5) _ // Int => Int

  // exercise
  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleAddMethod(x: Int, y: Int): Int = x + y
  def curriedAddMethod(x: Int)(y: Int): Int = x + y

  // add7: Int => Int = y => 7 + y
  // as many different implementations of add7 as you can
  val add7 = curriedAddMethod(7) _
  val add7_2 = (x: Int) => simpleAddFunction(x, 7)
  val add7_3 = simpleAddFunction.curried(7)

  val add7_4 = simpleAddMethod(7, _ : Int) // alternative syntax for turning methods into function values
  val add7_5 = simpleAddFunction(7, _ : Int) // this also works

  // underscores are powerful
  def concatenator(a: String, b: String, c: String) = a + b + c
  val insertName = concatenator("Hello, i'm ", _: String, ", how are you?")
  println(insertName("Karan"))

  val fillInTheBlanks = concatenator("Hello, ", _: String, _:String ) // (x, y) => concatenator

  // Exercises
  /*
  Process a list of numbers and return their string representations with different formats
  Use the %4.2f, %8.6f and %14.2f with a curried formatter function
   */
  println("%4.2f".format(Math.PI))

  val curriedFormatter = (formatter: String) => (x: Double) => formatter.format(x)
  val formatter = curriedFormatter("%4.2f")
  println(List(Math.PI, 5.4232, 5.6486).map(formatter))

  /*
    Difference between
    - functions vs methods
    - parameters: by name vs. 0-lambda
   */
  def byName(n: Int): Int = n + 1
  def byFunction(f: () => Int): Int = f() + 1

  def method: Int = 42
  def parenthesis(): Int = 42

  /*
  calling byName and byFunction
  - Int
  - method
  - parenMethod
  - lambda
  - PAF
   */
}
