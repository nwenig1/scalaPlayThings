import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import org.scalatestplus.play.OneBrowserPerSuite
import org.scalatestplus.play.HtmlUnitFactory


class task5FullTestSpec extends PlaySpec with GuiceOneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory{
    "Task 5 message board" must{
        "login and access functions" in {
            go to s"http://localhost:$port/task5"
            pageTitle mustBe "Task 5 login"
            find(cssSelector("h2")).isEmpty mustBe false 
            //click on "loginUsername" 
        }
    }
}
