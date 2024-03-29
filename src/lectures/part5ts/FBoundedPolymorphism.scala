package lectures.part5ts

object FBoundedPolymorphism extends App{

//  trait Animal {
//    def breed: List[Animal]
//  }
//
//  class Cat extends Animal {
//    override def breed: List[Animal] = ??? // List[Cat]
//  }
//
//  class Dog extends Animal {
//    override def breed: List[Animal] = ??? // List[Dog]
//  }

  // solution 1 - naive
//  trait Animal {
//    def breed: List[Animal]
//  }
//
//  class Cat extends Animal {
//    override def breed: List[Cat] = ??? // List[Cat]
//  }
//
//  class Dog extends Animal {
//    override def breed: List[Cat] = ??? // List[Dog]
//  }

  // solution 2
//  trait Animal[A <: Animal[A]] { // recursive type: f-bounded polymorphism
//    def breed: List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Animal[Cat]] = List[Cat]() // List[Cat]
//  }
//
//  class Dog extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = List[Dog]() // List[Dog]
//  }
//
//  trait Entity[E <: Entity[E]] // ORM
//  class Person extends Comparable[Person] { // FBP
//    override def compareTo(o: Person): Int = 1
//  }
//
//  class Crocodile extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = List[Dog]() // this is wrong
//  }

  // solution 3 - FBP + self types
//  trait Animal[A <: Animal[A]] { self: A => // recursive type: f-bounded polymorphism
//    def breed: List[Animal[A]]
//  }
//
//  class Cat extends Animal[Cat] {
//    override def breed: List[Animal[Cat]] = List[Cat]() // List[Cat]
//  }
//
//  class Dog extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = List[Dog]() // List[Dog]
//  }
//
//  trait Entity[E <: Entity[E]] // ORM
//  class Person extends Comparable[Person] { // FBP
//    override def compareTo(o: Person): Int = 1
//  }

//  class Crocodile extends Animal[Dog] {
//    override def breed: List[Animal[Dog]] = List[Dog]() // this is wrong
//  } // the compiler will not allow this, our constraint is met!

//  trait Fish extends Animal[Fish]
//  class Shark extends Fish {
//    override def breed: List[Animal[Fish]] = ???
//  } // there's a problem here look at the return type

  // solution 4 - type classes!

//  trait Animal
//  trait CanBreed[A] {
//    def breed(a: A): List[A]
//  }
//
//  class Dog extends Animal
//  object Dog {
//    implicit object DogsCanBreed extends CanBreed[Dog] {
//      override def breed(a: Dog): List[Dog] = List[Dog]()
//    }
//  }
//
//  implicit class CanBreedOps[A](val animal: A) {
//    def breed(implicit canBreed: CanBreed[A]): List[A] = canBreed.breed(animal)
//  }
//  val dog = new Dog();
//  dog.breed // List[Dog]
//  /*
//    new CanBreedOps[Dog](dog).breed(Dogs.DogsCanBreed)
//    implicit value passed to breed: Dog.DogsCanBreed
//   */
//
//  class Cat extends Animal
//  object Cat {
//    implicit object CatsCanBreed extends CanBreed[Dog] {
//      override def breed(a: Dog): List[Dog] = List[Dog]()
//    }
//  }
//
//  val cat = new Cat
//  cat.breed

  // solution 5

  trait Animal[A] { // pure type classes
    def breed(a: A): List[A]
  }
  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      override def breed(a: Dog): List[Dog] = List()
    }
  }

  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalInstance: Animal[A]): List[A] =
      animalInstance.breed(animal)
  }

  val dog = new Dog
  dog.breed
}
