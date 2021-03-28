package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.ResultPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CredentialTests extends CloudStorageApplicationTests {

	private void createCredential(String url, String username, String password, HomePage homePage) {
		homePage.navToCredentialsTab();
		homePage.addNewCredential();
		homePage.setCredentialUrl(url);
		homePage.setCredentialUsername(username);
		homePage.setCredentialPassword(password);
		homePage.saveCredentialChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToCredentialsTab();
	}


	//Write a test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
	@Test
	public void testCreateCredential() {
		HomePage homePage = signUpAndLogin();
		String url = "https://github.com/";
		String username = "rahaf";
		String password = "rahafo";
		createCredential(url, username, password, homePage);
		homePage.navToCredentialsTab();

		Credential credential = homePage.getFirstCredential();
		Assertions.assertEquals(url, credential.getUrl());
		Assertions.assertEquals(username, credential.getUserName());
		Assertions.assertNotEquals(password, credential.getPassword());
		homePage.deleteCredential();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.logout();
	}



	//Write a test that views an existing set of credentials, verifies that the viewable password is unencrypted,
	// edits the credentials, and verifies that the changes are displayed.
	@Test
	public void testUpdateCredential() {
		HomePage homePage = signUpAndLogin();
		String url = "https://github.com/";
		String username = "rahaf";
		String password = "rahafo";
		createCredential(url, username, password, homePage);
		homePage.navToCredentialsTab();
		Credential credential = homePage.getFirstCredential();
		Assertions.assertEquals(url, credential.getUrl());
		Assertions.assertEquals(username, credential.getUserName());
		Assertions.assertNotEquals(password, credential.getPassword());
		String encryptedPassword = credential.getPassword();

		homePage.editCredential();
		String newUrl = "https://www.netflix.com/";
		String newUsername = "rahafo";
		String newPassword = "r123";
		homePage.setCredentialUrl(newUrl);
		homePage.setCredentialUsername(newUsername);
		homePage.setCredentialPassword(newPassword);
		homePage.saveCredentialChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();

		homePage.navToCredentialsTab();
		Credential updatedCredential = homePage.getFirstCredential();
		Assertions.assertEquals(newUrl, updatedCredential.getUrl());
		Assertions.assertEquals(newUsername, updatedCredential.getUserName());
		String updatedCredentialPassword = updatedCredential.getPassword();
		Assertions.assertNotEquals(newPassword, updatedCredentialPassword);
		Assertions.assertNotEquals(encryptedPassword, updatedCredentialPassword);
		homePage.deleteCredential();
		resultPage.clickOk();
		homePage.logout();
	}


	//Write a test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
	@Test
	public void testDeleteCredential() {
		HomePage homePage = signUpAndLogin();
		createCredential("https://github.com/", "rahaf", "rahafo", homePage);
		createCredential("https://www.netflix.com/", "rahafo", "r123", homePage);
		createCredential("http://www.google.com/", "rahafo", "123", homePage);
		Assertions.assertFalse(homePage.noCredentials(driver));
		homePage.deleteCredential();

		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();

		homePage.navToCredentialsTab();
		homePage.deleteCredential();
		resultPage.clickOk();

		homePage.navToCredentialsTab();
		homePage.deleteCredential();
		resultPage.clickOk();

		homePage.navToCredentialsTab();
		Assertions.assertTrue(homePage.noCredentials(driver));
		homePage.logout();
	}
}
