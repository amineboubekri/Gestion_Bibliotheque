package view;
import controller.*;
import module.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class BiblioView extends JFrame {

    JTabbedPane mainTabbedPane = new JTabbedPane();

    // user section

    JButton userDetailsButton = new JButton("Details");
    JButton userAddButton = new JButton("Add");
    JButton userModifyButton = new JButton("Modify");
    JButton userDeleteButton = new JButton("Delete");
    JButton userSaveButton = new JButton("Save");
    DefaultTableModel userTableModel = new DefaultTableModel(
            new String[]{"ID", "Last Name", "First Name", "Password", "Age", "Adress"}, 0);
    JTable userTable = new JTable(userTableModel);
    JLabel userSearchLabel = new JLabel("Search : ");
    JTextField userSearchField = new JTextField(20);

    // book section
    JButton bookDetailsButton = new JButton("Details");
    JButton bookAddButton = new JButton("Add");
    JButton bookModifyButton = new JButton("Modify");
    JButton bookDeleteButton = new JButton("Delete");
    JButton bookEmpruntButton = new JButton("Borrow");
    JButton bookSaveButton = new JButton("Save");
    DefaultTableModel bookTableModel = new DefaultTableModel(
            new String[]{"ID", "Title", "Author", "Year", "Genre", "Copies"}, 0);
    JTable bookTable = new JTable(bookTableModel);

    JLabel bookSearchLabel = new JLabel("Rechercher : ");
    JTextField bookSearchField = new JTextField(20);

    // Emprunt section
    JLabel empruntAddNomLabel = new JLabel("⌕ User");
    JTextField empruntAddUserNomField = new JTextField(20);
    JLabel empruntAddBookLabel = new JLabel("⌕ Book");
    JTextField empruntAddBookNomField = new JTextField(20);
    JButton empruntAddButton = new JButton("New Loan ");

    JLabel empruntDetailsId = new JLabel("Loan Id : ");
    JLabel empruntDetailsNomUser = new JLabel("Member Id : ");
    JLabel empruntDetailsNomBook = new JLabel("Book Name : ");
    JLabel empruntDetailsDateEmprunt = new JLabel("Loan Date : ");
    JLabel empruntDetailsDateRetourTheorique = new JLabel("Supposed Return Date : ");
    JButton empruntRetourButton = new JButton("Return");

    DefaultTableModel empruntTableModel = new DefaultTableModel(
            new String[]{"ID", "Book Id", "Member Id", "Loan Date", "Supposed Return Date", "Actual Return Date"}, 0);
    JTable empruntTable = new JTable(empruntTableModel);
    JLabel empruntSearchLabel = new JLabel("Rechercher : ");
    JTextField empruntSearchField = new JTextField(20);
    JButton empruntSaveButton = new JButton("Save");
    DefaultTableModel returnTableModel = new DefaultTableModel(
            new String[]{"ID", "Loan Id " ,"Book Id", "Member Id", "Return Date"}, 0);
    JTable retourTable = new JTable(returnTableModel);
    // statistics section

    public BiblioView() {
        MembreController.readMemberFile();
        LivreController.readLivreFile();
        EmpruntController.readEmpruntFile();
        RetourController.readRetourFile();
        addComponentsUser();
        addComponentsBooks();
        addComponentsEmprunt();
        addComponentsStatistics();
        add(mainTabbedPane);
        pack();
        setTitle("Bibliothèque MAMI");
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        bookAddButton.addActionListener(e -> AdminController.ajouterLivre(this));
        bookModifyButton.addActionListener(e -> AdminController.modifierLivre(this, bookTable.getSelectedRow()));
        bookDeleteButton.addActionListener(e -> AdminController.supprimerLivre(this, bookTable.getSelectedRow()));
        bookEmpruntButton.addActionListener(e -> AdminController.borrowBook(this, bookTable.getSelectedRow()));

        userAddButton.addActionListener(e -> AdminController.ajouterMembre(this));
        userModifyButton.addActionListener(e -> AdminController.modifierMembre(this, userTable.getSelectedRow()));
        userDeleteButton.addActionListener(e -> AdminController.supprimerMembre(this, userTable.getSelectedRow()));
        empruntRetourButton.addActionListener(e->AdminController.returnBook(this,empruntTable.getSelectedRow()));
        empruntAddButton.addActionListener(e -> AdminController.ajouterEmprunt(this));
        empruntTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                empruntDetailsId.setText("ID Emprunt :  " + (empruntTableModel.getValueAt(empruntTable.getSelectedRow(), 0)));
                empruntDetailsNomUser.setText("nom propriétaire :  " + MembreController.findMember((int) (empruntTableModel.getValueAt(empruntTable.getSelectedRow(), 2))).getFullName());
                empruntDetailsNomBook.setText("nom livre :  " + LivreController.findBook((int) (empruntTableModel.getValueAt(empruntTable.getSelectedRow(), 1))).getTitre());
                empruntDetailsDateEmprunt.setText("date d'emprunt :  " + empruntTableModel.getValueAt(empruntTable.getSelectedRow(), 3));
                empruntDetailsDateRetourTheorique.setText("date de retour théorique :  " + empruntTableModel.getValueAt(empruntTable.getSelectedRow(), 4));
            }
        });
        empruntTable.getSelectedRow();

        bookSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String typed = bookSearchField.getText() + e.getKeyChar();
                typed = typed.trim();
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(bookTableModel);
                bookTable.setRowSorter(sorter);
                if (typed.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + typed));
                }
            }
        });
        userSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String typed = userSearchField.getText() + e.getKeyChar();
                typed = typed.trim();
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(userTableModel);
                userTable.setRowSorter(sorter);
                if (typed.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + typed));
                }
            }
        });
        empruntSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String typed = userSearchField.getText() + e.getKeyChar();
                typed = typed.trim();
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(empruntTableModel);
                empruntTable.setRowSorter(sorter);
                if (typed.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + typed));
                }
            }
        });
    }


    public DefaultTableModel getEmpruntTableModel() {
        return empruntTableModel;
    }

    public void setEmpruntTableModel(DefaultTableModel empruntTableModel) {
        this.empruntTableModel = empruntTableModel;
    }

    public JTable getEmpruntTable() {
        return empruntTable;
    }

    public void setEmpruntTable(JTable empruntTable) {
        this.empruntTable = empruntTable;
    }

    public void addComponentsUser() {
        for (Membre m : MembreController.membersList) {
            userTableModel.addRow(new Object[]{
                    m.getUid(),
                    m.getLastName(),
                    m.getFirstName(),
                    m.getPassword(),
                    m.getAge(),
                    m.getAdresse()
            });
        }
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel for user buttons
        JPanel userP1 = new JPanel();
        userP1.setLayout(new GridLayout(4, 1, 10, 10));
        userP1.add(userDetailsButton);
        userP1.add(userAddButton);
        userP1.add(userModifyButton);
        userP1.add(userDeleteButton);

        // Panel for search field at the top
        JPanel userP2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userP2.add(userSearchLabel);
        userP2.add(userSearchField);
        JPanel userP3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userP3.add(userSaveButton);
        JPanel userP4 = new JPanel();
        userP4.add(userP2);
        userP4.add(userP3);

        // User list setup
        userTable.setModel(userTableModel);
        JScrollPane userListScrollPane = new JScrollPane(userTable);

        // Add to the main panel
        mainPanel.add(userP4, BorderLayout.NORTH);
        mainPanel.add(userListScrollPane, BorderLayout.CENTER);
        mainPanel.add(userP1, BorderLayout.EAST);
        mainTabbedPane.addTab("Utilisateurs", mainPanel);
    }

    public void addComponentsBooks() {
        for (Livre l : LivreController.livreslist) {
            bookTableModel.addRow(new Object[]{
                    l.getidBook(),
                    l.getTitre(),
                    l.getAuteur(),
                    l.getAnneepub(),
                    l.getGenre(),
                    l.getNbCopies()
            });
        }
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel bookP1 = new JPanel();
        bookP1.setLayout(new GridLayout(5, 1, 10, 10));
        bookP1.add(bookDetailsButton);
        bookP1.add(bookAddButton);
        bookP1.add(bookModifyButton);
        bookP1.add(bookDeleteButton);
        bookP1.add(bookEmpruntButton);

        JPanel bookP2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bookP2.add(bookSearchLabel);
        bookP2.add(bookSearchField);
        JPanel bookP3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bookP3.add(bookSaveButton);
        JPanel bookP4 = new JPanel();
        bookP4.add(bookP2);
        bookP4.add(bookP3);

        bookTable.setModel(bookTableModel);
        JScrollPane bookListScrollPane = new JScrollPane(bookTable);

        mainPanel.add(bookP4, BorderLayout.NORTH);
        mainPanel.add(bookListScrollPane, BorderLayout.CENTER);
        mainPanel.add(bookP1, BorderLayout.EAST);

        mainTabbedPane.addTab("Livres", mainPanel);

    }

    public DefaultTableModel getUserTableModel() {
        return userTableModel;
    }

    public void setUserTableModel(DefaultTableModel userTableModel) {
        this.userTableModel = userTableModel;
    }

    public JTable getUserTable() {
        return userTable;
    }

    public void setUserTable(JTable userTable) {
        this.userTable = userTable;
    }

    public DefaultTableModel getBookTableModel() {
        return bookTableModel;
    }

    public void setBookTableModel(DefaultTableModel bookTableModel) {
        this.bookTableModel = bookTableModel;
    }

    public JTable getBookTable() {
        return bookTable;
    }

    public void setBookTable(JTable bookTable) {
        this.bookTable = bookTable;
    }

    public void addComponentsEmprunt() {
        for (Emprunt em : EmpruntController.empruntList) {
            empruntTableModel.addRow(new Object[]{
                    em.getIdE(),
                    em.getEmprunteur().getUid(),
                    em.getLivreEmprunte().getidBook(),
                    em.getDateEmprunt(),
                    em.getDateRetourTheo(),
                    em.getDateRetourReel()
            });
        }
        for(Retour re : RetourController.retourList){
            returnTableModel.addRow(new Object[]{
                    re.getIdRetour(),
                    re.getEmpruntretournee().getIdE(),
                    re.getLivreretourne().getidBook(),
                    re.getDateRetour()
            });
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel empruntP1 = new JPanel(new GridLayout(3, 1, 10, 10));
        JPanel empruntsubP1 = new JPanel();
        empruntsubP1.add(empruntAddNomLabel);
        empruntsubP1.add(empruntAddUserNomField);
        JPanel empruntsubP2 = new JPanel();
        empruntsubP2.add(empruntAddBookLabel);
        empruntsubP2.add(empruntAddBookNomField);
        empruntP1.add(empruntsubP1);
        empruntP1.add(empruntsubP2);
        empruntP1.add(empruntAddButton);

        JPanel empruntP2 = new JPanel(new GridLayout(6, 1, 10, 10));
        empruntP2.add(empruntDetailsId);
        empruntP2.add(empruntDetailsNomUser);
        empruntP2.add(empruntDetailsNomBook);
        empruntP2.add(empruntDetailsDateEmprunt);
        empruntP2.add(empruntDetailsDateRetourTheorique);
        empruntP2.add(empruntRetourButton);

        JPanel empruntP3 = new JPanel(new GridLayout(2, 1, 10, 10));
        empruntP3.add(empruntP1);
        empruntP3.add(empruntP2);

        JPanel empruntP4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        empruntP4.add(empruntSearchLabel);
        empruntP4.add(empruntSearchField);

        JPanel empruntP5 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        empruntP5.add(empruntSaveButton);
        JPanel empruntP6 = new JPanel();
        empruntP6.add(empruntP4);
        empruntP6.add(empruntP5);


        JScrollPane empruntListScrollPane = new JScrollPane(empruntTable);
        JScrollPane retourTableScrollPane=new JScrollPane(retourTable);

        mainPanel.add(empruntP6, BorderLayout.NORTH);
        JPanel tablesPanel =new JPanel(new GridLayout(2,1));
        tablesPanel.add(empruntListScrollPane);
        tablesPanel.add(retourTableScrollPane);
        mainPanel.add(tablesPanel, BorderLayout.CENTER);
        mainPanel.add(empruntP3, BorderLayout.EAST);

        mainTabbedPane.addTab("Emprunts", mainPanel);
    }

    private void addComponentsStatistics() {
        // Create table models for statistics
        DefaultTableModel mostBorrowedBooksModel = new DefaultTableModel(
                new String[]{"Titre", "Count"}, 0);
        DefaultTableModel mostBorrowedCategoryModel = new DefaultTableModel(
                new String[]{"Genre", "Count"}, 0);
        DefaultTableModel mostActiveUsersModel = new DefaultTableModel(
                new String[]{"Utilisateur", "Count"}, 0);

        Map<String, Integer> borrowCounts = new HashMap<>();
        for (Emprunt em : EmpruntController.empruntList) {
            String bookTitle = em.getLivreEmprunte().getTitre();
            borrowCounts.put(bookTitle, borrowCounts.getOrDefault(bookTitle, 0) + 1);
        }
        borrowCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10) // Limit to top 10 books
                .forEach(entry -> mostBorrowedBooksModel.addRow(new Object[]{entry.getKey(), entry.getValue()}));

        Map<String, Integer> userActivityCounts = new HashMap<>();
        for (Emprunt em : EmpruntController.empruntList) {
            String userName = em.getEmprunteur().getLastName() + " " + em.getEmprunteur().getFirstName();
            userActivityCounts.put(userName, userActivityCounts.getOrDefault(userName, 0) + 1);
        }
        userActivityCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10) // Limit to top 10 users
                .forEach(entry -> mostActiveUsersModel.addRow(new Object[]{entry.getKey(), entry.getValue()}));

        Map<String, Integer> genreCount = new HashMap<>();
        for (Emprunt em : EmpruntController.empruntList) {
            String bookGenre = em.getLivreEmprunte().getGenre();
            borrowCounts.put(bookGenre, borrowCounts.getOrDefault(bookGenre, 0) + 1);
        }
        borrowCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10) // Limit to top 10 categories
                .forEach(entry -> mostBorrowedCategoryModel.addRow(new Object[]{entry.getKey(), entry.getValue()}));
        JTable mostBorrowedBooksTable = new JTable(mostBorrowedBooksModel);
        JTable mostBorrowedCategoryTable = new JTable(mostBorrowedCategoryModel);
        JTable mostActiveUsersTable = new JTable(mostActiveUsersModel);

        // Create panels for each table
        JPanel booksPanel = new JPanel(new BorderLayout());
        booksPanel.add(new JLabel("Livres plus empruntes"), BorderLayout.NORTH);
        booksPanel.add(new JScrollPane(mostBorrowedBooksTable), BorderLayout.CENTER);

        JPanel genrePanel = new JPanel(new BorderLayout());
        genrePanel.add(new JLabel("Genres plus empruntes"), BorderLayout.NORTH);
        genrePanel.add(new JScrollPane(mostBorrowedCategoryTable), BorderLayout.CENTER);

        JPanel usersPanel = new JPanel(new BorderLayout());
        usersPanel.add(new JLabel("Utilisateurs actifs"), BorderLayout.NORTH);
        usersPanel.add(new JScrollPane(mostActiveUsersTable), BorderLayout.CENTER);

        // Combine both panels into one main panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        statsPanel.add(booksPanel);
        statsPanel.add(usersPanel);
        statsPanel.add(genrePanel);

        // Add the panel to the tabbed pane
        mainTabbedPane.addTab("Statistics", statsPanel);
    }
}