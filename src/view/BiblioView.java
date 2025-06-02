package view;
import controller.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import module.*;

/**
 * Represents the main user interface for the library management system.
 * This view provides separate tabs for managing users, books, and loans (emprunts),
 * and integrates with the corresponding controllers for each entity.
 */
public class BiblioView extends JFrame {

    // Theme colors
    private final Color PRIMARY_COLOR = new Color(51, 102, 153);    // Medium blue
    private final Color SECONDARY_COLOR = new Color(245, 245, 245); // Light gray
    private final Color ACCENT_COLOR = new Color(255, 153, 51);     // Orange
    private final Color TEXT_COLOR = new Color(51, 51, 51);         // Dark gray
    private final Color BACKGROUND_COLOR = new Color(255, 255, 255); // White
    private final Color BUTTON_COLOR = new Color(41, 128, 185);     // Blue
    private final Color BUTTON_TEXT_COLOR = new Color(255, 255, 255); // White
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private final int PADDING = 10;

    /**
     * Main tabbed pane containing the "Users", "Books", and "Emprunts" sections.
     */
    JTabbedPane mainTabbedPane = new JTabbedPane();

    // user section
    JButton userAddButton = new JButton("Add");
    JButton userModifyButton = new JButton("Modify");
    JButton userDeleteButton = new JButton("Delete");
    DefaultTableModel userTableModel = new DefaultTableModel(
            new String[]{"Member Id", "Last Name", "First Name","Password", "Age", "Adress","Penalty End"}, 0
    );
    JTable userTable = new JTable(userTableModel);
    JLabel userSearchLabel = new JLabel("Search : ");
    JTextField userSearchField = new JTextField(20);

    // book section
    JButton bookAddButton = new JButton("Add");
    JButton bookModifyButton = new JButton("Modify");
    JButton bookDeleteButton = new JButton("Delete");
    JButton bookEmpruntButton = new JButton("Borrow");
    DefaultTableModel bookTableModel = new DefaultTableModel(
            new String[]{"Book Id", "Title", "Author", "Year", "Genre", "Copies"}, 0);
    JTable bookTable = new JTable(bookTableModel);
    JLabel bookSearchLabel = new JLabel("Search : ");
    JTextField bookSearchField = new JTextField(20);

    // Emprunt section
    JLabel     empruntAddNomLabel = new JLabel("User Id");
    JTextField empruntAddUserNomField = new JTextField(20);
    JLabel     empruntAddBookLabel = new JLabel("Book Id");
    JTextField empruntAddBookNomField = new JTextField(20);
    JButton    empruntAddButton = new JButton("New Loan");
    JButton empruntRetourButton = new JButton("return");
    DefaultTableModel empruntTableModel = new DefaultTableModel(
            new String[]{"Loan Id", "Book Title", "Member Name","Loan Date", "Supposed Return Date"}, 0);
    JTable empruntTable = new JTable(empruntTableModel);
    JLabel empruntSearchLabel = new JLabel("Search : ");
    JTextField empruntSearchField = new JTextField(20);
    DefaultTableModel returnTableModel = new DefaultTableModel(
            new String[]{"return Id","Loan Id","Book Title", "Member Name","Actual Return Date"}, 0);
    JTable returnTable = new JTable(returnTableModel);

    // statistics section
    DefaultTableModel mostBorrowedBooksModel = new DefaultTableModel(
            new String[]{"Title", "Count"}, 0);
    DefaultTableModel mostBorrowedCategoryModel = new DefaultTableModel(
            new String[]{"Genre", "Count"}, 0);
    DefaultTableModel mostActiveUsersModel = new DefaultTableModel(
            new String[]{"Member", "Count"}, 0);

    Map<String, Integer> borrowCounts = new HashMap<>();


    /**
     * Constructs the main library management view.
     * Initializes components, populates data from controllers, and sets up action listeners.
     */
    public BiblioView(){
        MembreController.readMemberFile();
        LivreController.readLivreFile();
        EmpruntController.readEmpruntFile();
        RetourController.readRetourFile();
        
        // Apply theme to components
        applyTheme();
        
        addComponentsUser();
        addComponentsBooks();
        addComponentsEmprunt();
        addComponentsStatistics();
        
        // Create a main panel with padding
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.add(mainTabbedPane, BorderLayout.CENTER);
        
        // Add a header panel
        JPanel headerPanel = createHeaderPanel();
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        
        add(contentPanel);
        pack();
        setTitle("Library Management System");
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        // Add button listeners
        bookAddButton.addActionListener(e-> AdminController.ajouterLivre(this));
        bookModifyButton.addActionListener(e->AdminController.modifierLivre(this,bookTable.getSelectedRow()));
        bookDeleteButton.addActionListener(e->AdminController.supprimerLivre(this,bookTable.getSelectedRow()));
        bookEmpruntButton.addActionListener(e->AdminController.borrowBook(this,bookTable.getSelectedRow()));

        userAddButton.addActionListener(e->AdminController.ajouterMembre(this));
        userModifyButton.addActionListener(e->AdminController.modifierMembre(this,userTable.getSelectedRow()));
        userDeleteButton.addActionListener(e->AdminController.supprimerMembre(this,userTable.getSelectedRow()));

        empruntAddButton.addActionListener(e->AdminController.ajouterEmprunt(this));
        empruntRetourButton.addActionListener(e->AdminController.returnBook(this,empruntTable.getSelectedRow()));
        
        // Search functionality
        bookSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String typed = bookSearchField.getText()+e.getKeyChar();
                typed = typed.trim();
                TableRowSorter<TableModel> sorter=new TableRowSorter<>(bookTableModel);
                bookTable.setRowSorter(sorter);
                if(typed.isEmpty()){
                    sorter.setRowFilter(null);
                }else{
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)"+typed));
                }
            }
        });
        userSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String typed = userSearchField.getText()+e.getKeyChar();
                typed = typed.trim();
                TableRowSorter<TableModel> sorter=new TableRowSorter<>(userTableModel);
                userTable.setRowSorter(sorter);
                if(typed.isEmpty()){
                    sorter.setRowFilter(null);
                }else{
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)"+typed));
                }
            }
        });
        empruntSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String emprunttyped = empruntSearchField.getText()+e.getKeyChar();
                emprunttyped = emprunttyped.trim();
                TableRowSorter<TableModel> sorter=new TableRowSorter<>(empruntTableModel);
                empruntTable.setRowSorter(sorter);
                if(emprunttyped.isEmpty()){
                    sorter.setRowFilter(null);
                }else{
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)"+emprunttyped));
                }
            }
        });
    }
    
    /**
     * Creates the header panel with the library title
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        return headerPanel;
    }
    
    /**
     * Applies the theme to all components
     */
    private void applyTheme() {
        // Style buttons
        styleButton(userAddButton);
        styleButton(userModifyButton);
        styleButton(userDeleteButton);
        styleButton(bookAddButton);
        styleButton(bookModifyButton);
        styleButton(bookDeleteButton);
        styleButton(bookEmpruntButton);
        styleButton(empruntAddButton);
        styleButton(empruntRetourButton);
        
        // Style tabbed pane
        mainTabbedPane.setFont(HEADER_FONT);
        mainTabbedPane.setBackground(BACKGROUND_COLOR);
        mainTabbedPane.setForeground(TEXT_COLOR);
        
        // Style tables
        styleTable(userTable);
        styleTable(bookTable);
        styleTable(empruntTable);
        styleTable(returnTable);
        
        // Style text fields
        styleTextField(userSearchField);
        styleTextField(bookSearchField);
        styleTextField(empruntSearchField);
        styleTextField(empruntAddUserNomField);
        styleTextField(empruntAddBookNomField);
        
        // Style labels
        userSearchLabel.setFont(REGULAR_FONT);
        bookSearchLabel.setFont(REGULAR_FONT);
        empruntSearchLabel.setFont(REGULAR_FONT);
        empruntAddNomLabel.setFont(REGULAR_FONT);
        empruntAddBookLabel.setFont(REGULAR_FONT);
    }
    
    /**
     * Applies consistent styling to a button
     */
    private void styleButton(JButton button) {
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_COLOR.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });
    }
    
    /**
     * Applies consistent styling to a table
     */
    private void styleTable(JTable table) {
        table.setFont(REGULAR_FONT);
        table.setRowHeight(25);
        table.setIntercellSpacing(new Dimension(10, 5));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(PRIMARY_COLOR.brighter());
        table.setSelectionForeground(Color.WHITE);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setBorder(null);
    }
    
    /**
     * Applies consistent styling to a text field
     */
    private void styleTextField(JTextField textField) {
        textField.setFont(REGULAR_FONT);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
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

    /**
     * Adds components for the "Users" section of the library system.
     * Populates user data and sets up the user tab layout.
     */
    public void addComponentsUser() {
        // Populate user data
        for (Membre m : MembreController.membersList) {
            if(!m.isPenalized())
                userTableModel.addRow(new Object[]{
                        m.getUid(),
                        m.getLastName(),
                        m.getFirstName(),
                        m.getPassword(),
                        m.getAge(),
                        m.getAdresse()
                });
            else
                userTableModel.addRow(new Object[]{
                        m.getUid(),
                        m.getLastName(),
                        m.getFirstName(),
                        m.getPassword(),
                        m.getAge(),
                        m.getAdresse(),
                        m.getFinPenalite()
                });
        }
        
        // Create the main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(PADDING, PADDING));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        // Create a panel for the button actions with styled title
        JPanel actionPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        actionPanel.setBackground(BACKGROUND_COLOR);
        actionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            "Actions",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            HEADER_FONT,
            PRIMARY_COLOR
        ));
        
        // Add buttons to the action panel with some padding
        JPanel buttonPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel1.setBackground(BACKGROUND_COLOR);
        buttonPanel1.add(userAddButton);
        actionPanel.add(buttonPanel1);
        
        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel2.setBackground(BACKGROUND_COLOR);
        buttonPanel2.add(userModifyButton);
        actionPanel.add(buttonPanel2);
        
        JPanel buttonPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel3.setBackground(BACKGROUND_COLOR);
        buttonPanel3.add(userDeleteButton);
        actionPanel.add(buttonPanel3);

        // Create a panel for the search field
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(new EmptyBorder(0, 0, PADDING, 0));
        
        // Add a descriptive label for the search field
        JLabel searchTitle = new JLabel("Find Members:");
        searchTitle.setFont(HEADER_FONT);
        searchTitle.setForeground(PRIMARY_COLOR);
        searchPanel.add(searchTitle);
        searchPanel.add(userSearchLabel);
        searchPanel.add(userSearchField);

        // Create a scroll pane for the user table with custom border
        JScrollPane userListScrollPane = new JScrollPane(userTable);
        userListScrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR));
        
        // Add components to the main panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(userListScrollPane, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.EAST);
        
        // Add the main panel to the tabbed pane
        mainTabbedPane.addTab("Members", mainPanel);
    }

    /**
     * Adds components for the "Books" section of the library system.
     * Populates book data and sets up the book tab layout.
     */
    public void addComponentsBooks(){
        // Populate book data
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
        
        // Create the main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(PADDING, PADDING));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        // Create a panel for the button actions with styled title
        JPanel actionPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        actionPanel.setBackground(BACKGROUND_COLOR);
        actionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            "Book Actions",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            HEADER_FONT,
            PRIMARY_COLOR
        ));
        
        // Add buttons to the action panel
        JPanel buttonPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel1.setBackground(BACKGROUND_COLOR);
        buttonPanel1.add(bookAddButton);
        actionPanel.add(buttonPanel1);
        
        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel2.setBackground(BACKGROUND_COLOR);
        buttonPanel2.add(bookModifyButton);
        actionPanel.add(buttonPanel2);
        
        JPanel buttonPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel3.setBackground(BACKGROUND_COLOR);
        buttonPanel3.add(bookDeleteButton);
        actionPanel.add(buttonPanel3);
        
        JPanel buttonPanel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel4.setBackground(BACKGROUND_COLOR);
        buttonPanel4.add(bookEmpruntButton);
        actionPanel.add(buttonPanel4);

        // Create a panel for the search field
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(new EmptyBorder(0, 0, PADDING, 0));
        
        // Add a descriptive label for the search field
        JLabel searchTitle = new JLabel("Find Books:");
        searchTitle.setFont(HEADER_FONT);
        searchTitle.setForeground(PRIMARY_COLOR);
        searchPanel.add(searchTitle);
        searchPanel.add(bookSearchLabel);
        searchPanel.add(bookSearchField);

        // Create a scroll pane for the book table with custom border
        JScrollPane bookListScrollPane = new JScrollPane(bookTable);
        bookListScrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR));
        
        // Add components to the main panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(bookListScrollPane, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.EAST);
        
        // Add the main panel to the tabbed pane
        mainTabbedPane.addTab("Books", mainPanel);
    }

    /**
     * Adds components for the "Emprunts" section of the library system.
     * Populates loan data and sets up the loan tab layout.
     */
    public void addComponentsEmprunt(){
        // Populate loan data
        for (Emprunt em : EmpruntController.empruntList) {
            if(!em.isReturned()) {
                empruntTableModel.addRow(new Object[]{
                        em.getIdE(),
                        em.getLivreEmprunte().getTitre(),
                        em.getEmprunteur().getFullName(),
                        em.getDateEmprunt(),
                        em.getDateRetourTheo()
                });
            }
        }
        // Populate returns data
        for(Retour re: RetourController.retourList){
            returnTableModel.addRow(new Object[]{
                    re.getIdRetour(),
                    re.getEmpruntretournee().getIdE(),
                    re.getLivreretourne().getTitre(),
                    re.getMembreemprunteur().getFullName(),
                    re.getDateRetour()
            });
        }

        // Create the main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(PADDING, PADDING));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        // Create a panel for loan actions
        JPanel loanActionsPanel = new JPanel(new BorderLayout());
        loanActionsPanel.setBackground(BACKGROUND_COLOR);
        loanActionsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            "Loan Actions",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            HEADER_FONT,
            PRIMARY_COLOR
        ));
        
        // Panel for adding new loans
        JPanel newLoanPanel = new JPanel(new GridLayout(3, 1, 5, 10));
        newLoanPanel.setBackground(BACKGROUND_COLOR);
        newLoanPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // User ID field
        JPanel userIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userIdPanel.setBackground(BACKGROUND_COLOR);
        empruntAddNomLabel.setFont(REGULAR_FONT);
        userIdPanel.add(empruntAddNomLabel);
        userIdPanel.add(empruntAddUserNomField);
        newLoanPanel.add(userIdPanel);
        
        // Book ID field
        JPanel bookIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bookIdPanel.setBackground(BACKGROUND_COLOR);
        empruntAddBookLabel.setFont(REGULAR_FONT);
        bookIdPanel.add(empruntAddBookLabel);
        bookIdPanel.add(empruntAddBookNomField);
        newLoanPanel.add(bookIdPanel);
        
        // Add loan button
        JPanel addLoanButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addLoanButtonPanel.setBackground(BACKGROUND_COLOR);
        addLoanButtonPanel.add(empruntAddButton);
        newLoanPanel.add(addLoanButtonPanel);
        
        // Return book panel
        JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        returnPanel.setBackground(BACKGROUND_COLOR);
        returnPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        returnPanel.add(empruntRetourButton);
        
        // Combine loan actions
        loanActionsPanel.add(newLoanPanel, BorderLayout.NORTH);
        loanActionsPanel.add(returnPanel, BorderLayout.CENTER);

        // Create a panel for the search field
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(new EmptyBorder(0, 0, PADDING, 0));
        
        // Add a descriptive label for the search field
        JLabel searchTitle = new JLabel("Find Loans:");
        searchTitle.setFont(HEADER_FONT);
        searchTitle.setForeground(PRIMARY_COLOR);
        searchPanel.add(searchTitle);
        searchPanel.add(empruntSearchLabel);
        searchPanel.add(empruntSearchField);

        // Create tables panel with tabs
        JTabbedPane loansTabbedPane = new JTabbedPane();
        loansTabbedPane.setFont(REGULAR_FONT);
        
        // Create scroll panes for the tables
        JScrollPane empruntListScrollPane = new JScrollPane(empruntTable);
        empruntListScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane returnTableScrollPane = new JScrollPane(returnTable);
        returnTableScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Add tables to tabbed pane
        loansTabbedPane.addTab("Current Loans", empruntListScrollPane);
        loansTabbedPane.addTab("Returned Books", returnTableScrollPane);
        
        // Add components to the main panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(loansTabbedPane, BorderLayout.CENTER);
        mainPanel.add(loanActionsPanel, BorderLayout.EAST);
        
        // Add the main panel to the tabbed pane
        mainTabbedPane.addTab("Loans", mainPanel);
    }
    private void addComponentsStatistics() {
// Create table models for statistics

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
            genreCount.put(bookGenre, genreCount.getOrDefault(bookGenre, 0) + 1); // Use genreCount here
        }
        genreCount.entrySet().stream() // Use genreCount instead of borrowCounts
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10) // Limit to top 10 categories
                .forEach(entry -> mostBorrowedCategoryModel.addRow(new Object[]{entry.getKey(), entry.getValue()}));

        JTable mostBorrowedBooksTable = new JTable(mostBorrowedBooksModel);
        JTable mostBorrowedCategoryTable = new JTable(mostBorrowedCategoryModel);
        JTable mostActiveUsersTable = new JTable(mostActiveUsersModel);

        // Create panels for each table
        JPanel booksPanel = new JPanel(new BorderLayout());
        booksPanel.add(new JLabel("Most Loaned Books"), BorderLayout.NORTH);
        booksPanel.add(new JScrollPane(mostBorrowedBooksTable), BorderLayout.CENTER);

        JPanel genrePanel = new JPanel(new BorderLayout());
        genrePanel.add(new JLabel("Most Loaned Genres"), BorderLayout.NORTH);
        genrePanel.add(new JScrollPane(mostBorrowedCategoryTable), BorderLayout.CENTER);

        JPanel usersPanel = new JPanel(new BorderLayout());
        usersPanel.add(new JLabel("Most Active Users"), BorderLayout.NORTH);
        usersPanel.add(new JScrollPane(mostActiveUsersTable), BorderLayout.CENTER);

        // Combine both panels into one main panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        statsPanel.add(booksPanel);
        statsPanel.add(usersPanel);
        statsPanel.add(genrePanel);

        // Add the panel to the tabbed pane
        mainTabbedPane.addTab("Statistics", statsPanel);
    }

    public void updateStatistics() {
        // Clear the table
        mostBorrowedBooksModel.setRowCount(0);
        mostBorrowedCategoryModel.setRowCount(0);
        mostActiveUsersModel.setRowCount(0);

        Map<String, Integer> bookBorrowCounts = new HashMap<>();
        Map<String, Integer> userBorrowCounts = new HashMap<>();
        Map<String, Integer> genreBorrowCounts = new HashMap<>();

        for (Emprunt em : EmpruntController.empruntList) {

            String bookTitle = em.getLivreEmprunte().getTitre();
            bookBorrowCounts.put(bookTitle, bookBorrowCounts.getOrDefault(bookTitle, 0) + 1);


            String userName = em.getEmprunteur().getLastName() + " " + em.getEmprunteur().getFirstName();
            userBorrowCounts.put(userName, userBorrowCounts.getOrDefault(userName, 0) + 1);


            String genre = em.getLivreEmprunte().getGenre();
            genreBorrowCounts.put(genre, genreBorrowCounts.getOrDefault(genre, 0) + 1);
        }


        bookBorrowCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10)
                .forEach(entry -> mostBorrowedBooksModel.addRow(new Object[]{entry.getKey(), entry.getValue()}));

        userBorrowCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10)
                .forEach(entry -> mostActiveUsersModel.addRow(new Object[]{entry.getKey(), entry.getValue()}));

        genreBorrowCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10)
                .forEach(entry -> mostBorrowedCategoryModel.addRow(new Object[]{entry.getKey(), entry.getValue()}));
    }


    public DefaultTableModel getUserTableModel() {
        return userTableModel;
    }

    public JTable getUserTable() {
        return userTable;
    }

    public DefaultTableModel getBookTableModel() {
        return bookTableModel;
    }

    public JTable getBookTable() {
        return bookTable;
    }


    public DefaultTableModel getReturnTableModel() {
        return returnTableModel;
    }

    public JTable getReturnTable() {
        return returnTable;
    }
    public JTextField getEmpruntAddBookNomField() {
        return empruntAddBookNomField;
    }

    public JTextField getEmpruntAddUserNomField() {
        return empruntAddUserNomField;
    }

    public DefaultTableModel getMostActiveUsersModel() {
        return mostActiveUsersModel;
    }

    public DefaultTableModel getMostBorrowedCategoryModel() {
        return mostBorrowedCategoryModel;
    }

    public DefaultTableModel getMostBorrowedBooksModel() {
        return mostBorrowedBooksModel;
    }
}
