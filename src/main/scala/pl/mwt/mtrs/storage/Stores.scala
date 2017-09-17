package pl.mwt.mtrs.storage

import com.typesafe.config.Config
import io.getquill.{PostgresAsyncContext, SnakeCase}

import scala.concurrent.ExecutionContext

class Stores(cfg: Config) {

  private val dbCtx = new PostgresAsyncContext[SnakeCase](cfg)

  def movieStore(implicit ex: ExecutionContext) = new DbMovieStore(dbCtx)
}
