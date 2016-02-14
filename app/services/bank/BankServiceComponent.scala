package services.bank

import dto.Account
import services._

trait BankServiceComponent {
  val bankService: BankService

  trait BankService {
    def getAccount(accountId: String): ServiceResponse[Account]

    def getAllAccounts(): ServiceResponse[List[Account]]

    def indexAccount(account: Account): ServiceResponse[String]
  }
}
