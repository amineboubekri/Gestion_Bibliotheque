package module;
import static org.junit.Assert.*;
import java.sql.Date;
import org.junit.Before;
import org.junit.Test;

class EmpruntTest {

    private Livre livre;
    private Membre membre;
    private Date dateEmprunt;
    private Date dateRetourTheo;

    @Before
    void setup() {
        // Create actual Livre and Membre instances
        livre = new Livre("Book Title", "Author Name", 2024, "Fiction", 10);
        membre = new Membre(1, "Doe", "John", "password123", 30, "123 Main St");
        dateEmprunt = Date.valueOf("2024-01-01");
        dateRetourTheo = Date.valueOf("2024-02-01");
    }

    @Test
    void testConstructorAndIDGeneration() {
        Emprunt emprunt1 = new Emprunt(livre, membre, dateEmprunt, dateRetourTheo);

        int firstID = emprunt1.getIdE();

        Emprunt emprunt2 = new Emprunt(livre, membre, dateEmprunt, dateRetourTheo);
        int secondID = emprunt2.getIdE();

        assertTrue(secondID > firstID, "The ID should be incremented correctly.");
    }

    @Test
    void testGettersAndSetters() {
        Emprunt emprunt = new Emprunt(livre, membre, dateEmprunt, dateRetourTheo);

        // Test getters
        assertEquals(livre, emprunt.getLivreEmprunte());
        assertEquals(membre, emprunt.getEmprunteur());
        assertEquals(dateEmprunt, emprunt.getDateEmprunt());
        assertEquals(dateRetourTheo, emprunt.getDateRetourTheo());
        assertFalse(emprunt.isReturned(), "The default 'returned' status should be false.");

        // Test setters
        Livre newLivre = new Livre("New Book", "New Author", 2025, "Non-Fiction", 5);
        Membre newMembre = new Membre(2, "Smith", "Jane", "newpassword", 25, "456 Another St");
        Date newDateEmprunt = Date.valueOf("2024-03-01");
        Date newDateRetourTheo = Date.valueOf("2024-04-01");

        emprunt.setLivreEmprunte(newLivre);
        emprunt.setEmprunteur(newMembre);
        emprunt.setDateEmprunt(newDateEmprunt);
        emprunt.setDateRetourTheo(newDateRetourTheo);
        emprunt.setReturned(true);

        // Test updated values
        assertEquals(newLivre, emprunt.getLivreEmprunte());
        assertEquals(newMembre, emprunt.getEmprunteur());
        assertEquals(newDateEmprunt, emprunt.getDateEmprunt());
        assertEquals(newDateRetourTheo, emprunt.getDateRetourTheo());
        assertTrue(emprunt.isReturned(), "The 'returned' status should be true.");
    }

    @Test
    void testToString() {
        Emprunt emprunt = new Emprunt(livre, membre, dateEmprunt, dateRetourTheo);

        // Test the string representation (toString)
        String expectedString = "1,2,2,2024-01-01,2024-02-01,false";
        assertEquals(expectedString, emprunt.toString(), "The toString method should return the correct string representation.");
    }
}

