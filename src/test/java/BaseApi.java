import io.restassured.RestAssured;
import org.junit.Before;

public class BaseApi {
    @Before
    public void setUpConfigure() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
}
