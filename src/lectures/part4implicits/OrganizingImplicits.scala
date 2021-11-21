package lectures.part4implicits

object OrganizingImplicits extends App {

  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)

  println(List(1, 4, 5, 3, 2).sorted)
  // scala.predef

  /*
   Implicits (used as implicit parameters)
    - val/var
    - objects
    - accessor methods - defs with no parenthesis
   */

  // Exercise
  case class Person(name: String, age: Int)

  val personList =
    List(Person("Karan", 28), Person("Xyz", 25), Person("Abc", 30))

  /*
  implicit scope
    - normal scope = LOCAL SCOPE
    - imported scope
    - companions of all types involved in the method signature
   */

  /*
  best practices

    #1 when defining an implicit val, if there is a single possible value for it
       and you can edit the code for the type, then define the implicit in the
       companion

    #2 if there are many possible values for it, but a single good one, and you can
       edit the the code for the type, then define the good implicit in the companion
   */

  object AlphabeticNameOrdering{
    implicit val alphabeticOrdering: Ordering[Person] =
      Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  object AgeOrdering{
    implicit val ageOrdering: Ordering[Person] =
      Ordering.fromLessThan((a, b) => a.age < b.age)
  }

  import AlphabeticNameOrdering._
  println(personList.sorted)

  /*
  Exercise: Add orderings

  - total price = most used (50%) - nUnits * unitPrice
  - by unit count = 25%
  - by unit price = 25%
   */
  case class Purchase(nUnit: Int, unitPrice: Double)

  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] =
      Ordering.fromLessThan((a, b) => (a.nUnit * a.unitPrice) < (b.nUnit * b.unitPrice))
  }

  object UnitCountOrdering {
    implicit val countOrdering: Ordering[Purchase] =
      Ordering.fromLessThan((a, b) => a.nUnit < b.nUnit)
  }

  object UnitPriceOrdering {
    implicit val priceOrdering: Ordering[Purchase] =
      Ordering.fromLessThan((a, b) => a.unitPrice < b.unitPrice)
  }
}
