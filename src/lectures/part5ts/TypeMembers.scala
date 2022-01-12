package lectures.part5ts

object TypeMembers extends App{

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }

  val ac = new AnimalCollection
  //  val dog: ac.AnimalType = ???
  //  val cat: ac.BoundedAnimal = new Cat // this will not compile

  val pup: ac.SuperBoundedAnimal = new Dog // compiler allows this
  // but any other supertype of dog does not work
  val cat: ac.AnimalC = new Cat // type aliases are fine

  type CatAlias = Cat
  val anotherCar: CatAlias = new Cat

  // alternative to generics
  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int
    override def add(element: Int): MyList = new NonEmptyList(element)
  }

  //.type
  type CatsType = cat.type
  val newCat: CatsType = cat
  // cannot instantiate, can only do association

  /*
    Exercise - enforce a type to be applicable to SOME TYPES only
   */
  // LOCKED
  trait MList {
    type A
    def head(): A
    def tail(): MList
  }

  trait ApplicableToNumbers {
    type A <: Number
  }

  // We don't want this to compile
  //  class CustomList(head: String, tail: CustomList) extends MList with ApplicableToNumbers {
  //      type A = String
  //      def head(): String = head
  //      def tail(): MList = tail
  //  }

  // we want this to compile
  class IntList(head: Integer, tail: IntList) extends MList with ApplicableToNumbers {
    type A = Integer
    def head(): Integer = head
    def tail(): MList = tail
  }

  // Number
  // type members and type member constraints with bounds

}
