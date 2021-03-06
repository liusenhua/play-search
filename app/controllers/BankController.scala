package controllers

import com.wordnik.swagger.annotations._
import javax.ws.rs.{PathParam, QueryParam}

import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import services.bank.{BankServiceComponent}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import dto._

/**
 * Created by andy on 2/8/16.
 */
@Api(value = "/bank", description = "Bank operations")
class BankController(comp: BankServiceComponent) extends Controller with JsonFormats {
  private val bankService = comp.bankService

  @ApiOperation(value = "Get account", response = classOf[Account], httpMethod = "GET")
  def getAccount(@ApiParam(value = "accountId", required = true) @PathParam("id") accountId: String) = Action.async {
    request =>
      Logger.debug(s"BankController.getAccount, accountId=$accountId")

      bankService.getAccount(accountId) map {
        case DTO(d) => Ok(Json.toJson(d))
        case ErrorDTO(c, m) =>
          Logger.error(s"BankController.getAccount, InternalServerError ($c): $m")
          InternalServerError(m)
      }
  }

  @ApiOperation(value = "Search accounts", response = classOf[List[Account]], httpMethod = "GET")
  def searchAccount() = Action.async {
    request =>
      Logger.debug(s"BankController.searchAccount")

      bankService.searchAccount() map {
        case DTO(d) => Ok(Json.toJson(d))
        case ErrorDTO(c, m) =>
          Logger.error(s"BankController.searchAccount, InternalServerError ($c): $m")
          InternalServerError(m)
      }
  }

  @ApiOperation(value = "Index account", response = classOf[String], httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Account", required = true, dataType = "Account", paramType = "body")
  ))
  def indexAccount() = Action.async {
    request =>
      parseRequestJson(request, json => {
        val account = json.as[Account]
        bankService.indexAccount(account) map {
          case DTO(d) => Ok(Json.toJson(d))
          case ErrorDTO(c, m) =>
            Logger.error(s"BankController.indexAccount, InternalServerError ($c): $m")
            InternalServerError(m)
        }
      })
  }
}
