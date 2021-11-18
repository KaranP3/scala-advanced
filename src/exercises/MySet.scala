package exercises

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {
  def apply(elem: A): Boolean = contains(elem)

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A] // union

  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(f: A => Boolean): MySet[A]
  def forEach(f: A => Unit): Unit

  /*
  Exercise
  1. removing an element
  2. difference
  3. intersection
   */
  def -(elem: A): MySet[A]
  def --(anotherSet: MySet[A]): MySet[A] // difference
  def &(anotherSet: MySet[A]): MySet[A] // intersection

  // Exercise: implement unary_!, which is the negation of a set
  def unary_! : MySet[A]
}

class EmptySet[A] extends MySet[A] {
  override def contains(elem: A): Boolean = false
  override def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)
  override def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  override def map[B](f: A => B): MySet[B] = new EmptySet[B]
  override def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  override def filter(f: A => Boolean): MySet[A] = this
  override def forEach(f: A => Unit): Unit = ()

  // part 2
  override def -(elem: A): MySet[A] = this
  override def --(anotherSet: MySet[A]): MySet[A] = this
  override def &(anotherSet: MySet[A]): MySet[A] = this

  // part 3
  override def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
}

// All elements of type A which satisfy a property
// { x in A | property(x) }
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {
  def contains(elem: A): Boolean = property(elem)
  // adding an element
  def +(elem: A): MySet[A] = new PropertyBasedSet[A](x => property(x) || x == elem)
  // concatenating another set
  def ++(anotherSet: MySet[A]): MySet[A] = new PropertyBasedSet[A](x => property(x) || anotherSet(x))

  // all integers => (_ % 3) => [0, 1, 2]

  def filter(predicate: A => Boolean): MySet[A] =
    new PropertyBasedSet[A](x => property(x) && predicate(x))

  def -(elem: A): MySet[A] = filter(x => x != elem)
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))

  def map[B](f: A => B): MySet[B] = politelyFail
  def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail
  def forEach(f: A => Unit): Unit = politelyFail

  def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole")
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  override def contains(elem: A): Boolean =
    elem == head || tail.contains(elem)

  override def +(elem: A): MySet[A] = {
    if (this contains elem) this
    else new NonEmptySet(elem, this)
  }

  /*
    [1, 2, 3] ++ [4, 5] =
    [2, 3] ++ [4, 5] + 1 =
    [3] ++ [4, 5] + 1 + 2 =
    [] ++ [4, 5] + 1 + 2 + 3 =
    [4, 5] + 1 + 2 + 3 =
    [4, 5, 1, 2, 3]
   */
  override def ++(anotherSet: MySet[A]): MySet[A] =
    tail ++ anotherSet + head

  /*
   f = (a) => a + 1
   [1, 2, 3]

   [1, 2] + f(3)
   [1] + f(2) + f(3)
   [] + f(1) + f(2) + f(3)
   [4, 3, 2]
   */
  override def map[B](f: A => B): MySet[B] = (tail map f) + f(head)
  override def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head)
  override def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }
  override def forEach(f: A => Unit): Unit = {
    f(head)
    tail forEach f
  }

  // part 2
  override def -(elem: A): MySet[A] =
    if (head == elem) tail
    else tail - elem + head
  override def --(anotherSet: MySet[A]): MySet[A] = filter(x => !anotherSet(x))
  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // intersecting = filtering!

  // new operator
  override def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
}

object MySet {
  /*
  val s = MySet(1, 2, 3) = buildSet(Seq(1, 2, 3), [])
  = buildSet(Seq(2, 3), [1])
  = buildSet(Seq(3), [1, 2])
  = buildSet(Seq(), [1, 2, 3])
  = [1, 2, 3]
   */
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] = {
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)
    }
    buildSet(values, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val s = MySet(1, 2, 3, 4)
  s +  5 ++ MySet(-5, -2) + 3 flatMap(x => MySet(x, 10 * x)) filter(_ % 2 != 0) forEach println

  val negative = !s // s.unary_! = all the naturals not equal to 1, 2, 3, or 4
  println(negative(2))
  println(negative(5))

  val negativeEven = negative.filter(_ % 2 == 0)
  println(negativeEven(5))

  val negativeEven5 = negativeEven + 5 // all the even numbers > 4 + 5
  println(negativeEven5(5))
}