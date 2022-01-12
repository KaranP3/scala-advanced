package lectures.part5ts

object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance?
  // "inheritance" - type substitution of generics

  class Cage[T]
  // should the Cage[Cat] also "inherit" from Cage[Animal]

  // yes - covariance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariance
  class ICage[T]
  //  val iCage: ICage[Animal] = new ICage[Cat] - this will not work

  // hell no - opposite = contravariance
  class XCage[-T]
  val xCage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](val animal: T) // invariant

  // covariant positions
  class CovariantCage[+T](val animal: T) // COVARIANT POSITION

  //  class ContravariantCage[-T](val animal : T) - doesn't compile
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

  //  class CovariantVariableCage[+T](var animal: T) // types of vars are in CONTRAVARIANT position
  /*
  val cCage: CCage[Animal] = new CCage[Cat](new Cat)
  cCage.animal = new Crocodile
   */
  //  class ContravariantVairableCage[-T](var animal: T) // also in COVARIANT position
  /*
   val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
  */
  // the only acceptable type for a variable field is invariant!
  class InvariantVariableCage[T](var animal: T)

  //  trait AnotherCovariantCage[+T] {
  //    def addAnimal(animal: T) // CONTRAVARIANT position
  //  } - this does not compile either
  /*
    val cCage: CCage[Animal] = new CCage[Dog]
    cCage.add(new Cat)
   */

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T): Boolean = true
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  //  acc.addAnimal(new Dog) - this would not compile
  acc.addAnimal(new Cat)
  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = moreAnimals.add(new Dog)

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION

  // return types
  class PetShop[-T] {
    // def get(isItAPuppy: Boolean): T // METHOD RETURN TYPES ARE IN COVARIANT POSITION
    /*
      val catShop = new PetShop[Animal] {
        def get(isItAPuppy: Boolean): Animal = new Cat
      }
      val dogShop: PetShop[Dog] = catShop
      dogShop.get(true) // EVIL CAT!
     */
    def get[S <: T](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]
  //  val evilCat = shop.get(true, new Cat)
  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

  /*
    Big rule
    - method arguments are in contravariant position
    - return types are in covariant position
   */
}
