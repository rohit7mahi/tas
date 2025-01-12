import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        // Set up ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ROHIT\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Navigate to the website
            driver.get("https://www.saucedemo.com");
            logger.info("Navigated to Sauce Demo website");
            takeScreenshot(driver, "Step1_HomePage");

            // Verify and print page title
            String pageTitle = driver.getTitle();
            logger.info("Page Title: " + pageTitle);
            System.out.println("Page Title: " + pageTitle);

            // Log in
            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");
            driver.findElement(By.id("login-button")).click();
            logger.info("Logged in successfully");
            takeScreenshot(driver, "Step2_AfterLogin");

            // Add the third product to the cart
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//button[text()='Add to cart'])[3]"))).click();
            logger.info("Added third product to the cart");
            takeScreenshot(driver, "Step3_ProductAdded");

            // Click the cart icon
            driver.findElement(By.id("shopping_cart_container")).click();
            logger.info("Opened cart");
            takeScreenshot(driver, "Step4_CartView");

            // Verify product is in the cart
            WebElement cartItem = driver.findElement(By.className("inventory_item_name"));
            if (cartItem.isDisplayed()) {
                logger.info("Product is in the cart: " + cartItem.getText());
            }

            // Proceed to checkout
            driver.findElement(By.id("checkout")).click();
            logger.info("Clicked Checkout");
            takeScreenshot(driver, "Step5_Checkout");

            // Fill out checkout form
            driver.findElement(By.id("first-name")).sendKeys("Test");
            driver.findElement(By.id("last-name")).sendKeys("User");
            driver.findElement(By.id("postal-code")).sendKeys("12345");
            driver.findElement(By.id("continue")).click();
            logger.info("Filled checkout form");
            takeScreenshot(driver, "Step6_CheckoutForm");

            // Complete the order
            driver.findElement(By.id("finish")).click();
            logger.info("Clicked Finish");
            takeScreenshot(driver, "Step7_OrderCompleted");

            // Verify success message
            WebElement successMessage = driver.findElement(By.className("complete-header"));
            if (successMessage.getText().equals("THANK YOU FOR YOUR ORDER")) {
                logger.info("Order completed successfully");
            }

        } catch (Exception e) {
            logger.severe("Error during automation: " + e.getMessage());
        } finally {
            driver.quit();
            logger.info("Browser closed");
        }
    }

    // Utility method to take screenshots
    public static void takeScreenshot(WebDriver driver, String fileName) throws IOException {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File("screenshots/" + fileName + ".png");
        Files.copy(srcFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        logger.info("Screenshot saved: " + destFile.getPath());
    }
}
