package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.ResultPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoteTests extends CloudStorageApplicationTests {

	private void createNote(String noteTitle, String noteDescription, HomePage homePage) {
		homePage.navToNotesTab();
		homePage.addNewNote();
		homePage.setNoteTitle(noteTitle);
		homePage.setNoteDescription(noteDescription);
		homePage.saveNoteChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToNotesTab();
	}

	private void deleteNote(HomePage homePage) {
		homePage.deleteNote();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
	}



	//Write a test that creates a note, and verifies it is displayed.
	@Test
	public void testCreateAndDisplayNote() {
		String noteTitle = "TEST Note";
		String noteDescription = "Note Description";
		HomePage homePage = signUpAndLogin();
		createNote(noteTitle, noteDescription, homePage);
		homePage.navToNotesTab();
		homePage = new HomePage(driver);
		Note note = homePage.getFirstNote();
		Assertions.assertEquals(noteTitle, note.getNoteTitle());
		Assertions.assertEquals(noteDescription, note.getNoteDescription());
		deleteNote(homePage);
		homePage.logout();
	}


	//Write a test that edits an existing note and verifies that the changes are displayed.
	@Test
	public void testUpdateNote() {
		String noteTitle = "TEST Note";
		String noteDescription = "Note Description";
		HomePage homePage = signUpAndLogin();
		createNote(noteTitle, noteDescription, homePage);
		homePage.navToNotesTab();
		homePage = new HomePage(driver);
		homePage.editNote();
		String updatedNoteTitle = "updated Note";
		homePage.updateNoteTitle(updatedNoteTitle);
		String updatedNoteDescription = "updated Note Description";
		homePage.updateNoteDescription(updatedNoteDescription);
		homePage.saveNoteChanges();
		ResultPage resultPage = new ResultPage(driver);
		resultPage.clickOk();
		homePage.navToNotesTab();
		Note note = homePage.getFirstNote();
		Assertions.assertEquals(updatedNoteTitle, note.getNoteTitle());
		Assertions.assertEquals(updatedNoteDescription, note.getNoteDescription());
		deleteNote(homePage);
		homePage.logout();
	}

	//Write a test that deletes a note and verifies that the note is no longer displayed.
	@Test
	public void testDeleteNote() {
		String noteTitle = "TEST Note";
		String noteDescription = "Note Description";
		HomePage homePage = signUpAndLogin();
		createNote(noteTitle, noteDescription, homePage);
		homePage.navToNotesTab();
		homePage = new HomePage(driver);
		Assertions.assertFalse(homePage.noNotes(driver));
		deleteNote(homePage);
		Assertions.assertTrue(homePage.noNotes(driver));
	}
}
