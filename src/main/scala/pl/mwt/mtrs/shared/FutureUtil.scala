package pl.mwt.mtrs.shared

import scala.concurrent.Future
import scala.language.implicitConversions

object FutureUtil {

  implicit def immediateSuccess[A](a: A): Future[A] =
    Future successful a
}
