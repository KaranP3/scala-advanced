package lectures.part3concurrency

import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

// important for futures
import scala.concurrent.ExecutionContext.Implicits.global

object FuturesPromises extends App{

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

//  val aFuture: Future[Int] = Future {
//    calculateMeaningOfLife // calculates meaning of life on another thread
//  } // (global) which is passed by the compiler
//
//  println(aFuture.value) // Option[Try[Int]]

//  println("waiting on the future")
//  aFuture.onComplete {
//    case Success(value) => println(s"the meaning of life is $value")
//    case Failure(exception) => println(s"I have failed with $exception")
//  } // SOME thread

//  Thread.sleep(3000)

  // mini social network

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile): Unit =
      println(s"${this.name} poking ${anotherProfile.name}")
  }

  object SocialNetwork {
    // "database"
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "dummy"
    )
    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )

    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(400)
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }
  }

  // client: mark to poke bill

  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
  //  mark.onComplete{
  //    case Success(markProfile) =>
  //      val bill = SocialNetwork.fetchBestFriend(markProfile)
  //      bill.onComplete{
  //        case Success(billProfile) => markProfile.poke(billProfile)
  //        case Failure(e) => e.printStackTrace()
  //      }
  //    case Failure(e) => e.printStackTrace()
  //  }
  //
  //  Thread.sleep(1000)

  // functional composition of futures
  // map, flatMap, filter
  val nameOnTheWall: Future[String] = mark.map(profile => profile.name)

  val marksBestFriend: Future[Profile] =
    mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))

  val zucksBestFriendRestricted: Future[Profile] =
    marksBestFriend.filter(p => p.name.startsWith("Z"))

  // for comprehensions
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark poke bill

  Thread.sleep(1000)

  // fallbacks
  val aProfileNoMatterWhat =
    SocialNetwork.fetchProfile("unknown id").recover{
      case _: Throwable => Profile("fb.id.0-dummy", "Forever alone")
    }

  val aFetchedProfileNoMatterWhat =
    SocialNetwork.fetchProfile("unknown id").recoverWith{
      case _: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
    }

  val fallbackResult =
    SocialNetwork.fetchProfile("unknown id")
      .fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))


}
