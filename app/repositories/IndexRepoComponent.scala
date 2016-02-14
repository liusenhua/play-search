package repositories

import dto.Account

/**
 * Created by andy on 2/14/16.
 */
trait IndexRepoComponent {
  val indexerRepo: IndexerRepo

  trait IndexerRepo {
    def indexAccount(account: Account): RepoResponse[String]
  }
}
