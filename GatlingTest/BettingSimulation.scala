package gatlingTest

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class BettingSimulation extends Simulation {

	//This class was generated as part of a recording

	val httpProtocol = http
		.baseURL("http://sports.williamhill.com")
		.inferHtmlResources(BlackList(""".*\.css, .*\.js , .*\.ico"""), WhiteList())
		.acceptHeader("image/webp,image/*,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate, sdch")
		.acceptLanguageHeader("en-US,en;q=0.8")
		.doNotTrackHeader("1")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")

	val headers_0 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate",
		"Content-Type" -> "application/x-www-form-urlencoded; charset=UTF-8",
		"Origin" -> "http://sports.williamhill.com")

    val uri1 = "http://secdn.williamhill.com/core/ob/static/cust/images/en/blank.gif"
    val uri2 = "http://metrics.williamhill.com/b/ss/whg-intl-prod-v3,whgintltraf-prod/1/JS-1.5.1"
    val uri3 = "http://sports.williamhill.com/slp/en-gb"

	//Put test setups into the following singletons
	object Visit {
		val visit = exec(http("Go to betting page")
			.post("/slp/en-gb")
			.headers(headers_0)
			.formParam("action", "GoRemoveLeg")
			.formParam("bet_no", "0")
			.formParam("blockbuster_id", "-1")
			.formParam("switch_tab", "1")
			.formParam("csrf_token", "122616cc02f4a081f35d"))
			.pause(4)
	}

	object AddTeam {
		val addTeam = exec(http("Add team")
			.post("/slp/en-gb")
			.headers(headers_0)
			.formParam("action", "GoAddLeg")
			.formParam("leg_sort", "--")
			.formParam("price_type", "L")
			.formParam("lp_num", "50")
			.formParam("lp_den", "1")
			.formParam("hcap_value", "0")
			.formParam("bir_index", "")
			.formParam("ew_places", "")
			.formParam("ew_fac_num", "")
			.formParam("ew_fac_den", "")
			.formParam("ev_oc_id", "1542636701")
			.formParam("combi_sel", "Y")
			.formParam("blockbuster_id", "-1")
			.formParam("switch_tab", "1")
			.formParam("aff_id", "850")
			.formParam("csrf_token", "86ff8d0b1826366e7bab")
			.resources(http("Bet Added")
				.get(uri2 + "/s29593420049506?AQB=1&ndh=1&pf=1&t=26%2F3%2F2017%2021%3A40%3A29%203%20-60&mid=02402740728773446981827477181421495053&aamlh=6&ce=UTF-8&ns=williamhill&pageName=sports%3Abetting%3Aen-gb&g=http%3A%2F%2Fsports.williamhill.com%2Fbetting%2Fen-gb&pe=lnk_o&pev2=bet%20added%20to%20betslip&pid=sports%3Abetting%3Aen-gb&pidt=1&oid=50%2F1&oidt=3&ot=SUBMIT&events=scOpen%2CscAdd&products=%3B1542636701&v36=sports%3Abetting%3Aen-gb&v44=In%20Live&v70=Crystal%20Palace%20v%20Tottenham%7CMatch%20Betting%20Live%7CEnglish%20Premier%20League%7C26%20Apr%202017&v113=Crystal%20Palace%7C51.00&s=1280x800&c=24&j=1.6&v=N&k=Y&bw=1280&bh=569&AQE=1"),
				http("Bet added to payslip")
					.get(uri1 + "?nosubsite=EN&nourl=www.willhill.com/EN/SB/Betslip/AddToSlip&r_betst=d_&f_bet_lang=EN")))
			.pause(4)
	}

	object PlaceBet {
		val placeBet = exec(http("Place Bet")
			.get(uri2 + "/s23402688113242?AQB=1&ndh=1&pf=1&t=26%2F3%2F2017%2021%3A40%3A33%203%20-60&mid=02402740728773446981827477181421495053&aamlh=6&ce=UTF-8&ns=williamhill&pageName=sports%3Abetting%3Aen-gb&g=http%3A%2F%2Fsports.williamhill.com%2Fbetting%2Fen-gb&pe=lnk_o&pev2=betslip%3A%20submit%20betslip&events=scCheckout&products=%3B1542636701%3B%3B%3B%3BeVar38%3Dsingle%20%3A%3A%20s-only&v106=1&s=1280x800&c=24&j=1.6&v=N&k=Y&bw=1280&bh=569&AQE=1")
			.resources(http("Must be logged in - oops")
				.get(uri2 + "/s25270891875539?AQB=1&ndh=1&pf=1&t=26%2F3%2F2017%2021%3A40%3A33%203%20-60&mid=02402740728773446981827477181421495053&aamlh=6&ce=UTF-8&ns=williamhill&pageName=sports%3Abetting%3Aen-gb&g=http%3A%2F%2Fsports.williamhill.com%2Fbetting%2Fen-gb&pe=lnk_o&pev2=SLIP_API_ERR_USER_NOT_LOGGED_IN&events=event10&c75=SLIP_API_ERR_USER_NOT_LOGGED_IN&v103=SLIP_API_ERR_USER_NOT_LOGGED_IN&v106=1&s=1280x800&c=24&j=1.6&v=N&k=Y&bw=1280&bh=569&AQE=1")))
	}

  val users = scenario("Users").exec(Visit.visit, AddTeam.addTeam, PlaceBet.placeBet)

	//Scenario runs through the possible scenarios
	val scn = scenario("BettingSimulation")
		.exec(Visit.visit, AddTeam.addTeam, PlaceBet.placeBet)

	//Ramp up users
	setUp(users.inject(rampUsers(10) over (10 seconds)))
		.protocols(httpProtocol)
}