import java.util.Stack;
import java.util.Scanner;

class Book {
    private String isbn;
    private String title;
    private String author;
    
    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }
    
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    
    @Override
    public String toString() {
        return "ISBN: " + isbn + ", Title: \"" + title + "\", Author: " + author;
    }
}

class BorrowingRecord {
    private Book book;
    private String borrowerId;
    private String borrowDate;
    private String returnDate;
    
    public BorrowingRecord(Book book, String borrowerId, String borrowDate, String returnDate) {
        this.book = book;
        this.borrowerId = borrowerId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }
    
    public Book getBook() { return book; }
    public String getBorrowerId() { return borrowerId; }
    public String getBorrowDate() { return borrowDate; }
    public String getReturnDate() { return returnDate; }
    
    @Override
    public String toString() {
        return "Borrower: " + borrowerId + 
               " | Book: " + book.getTitle() + 
               " | Borrowed: " + borrowDate + 
               " | Returned: " + returnDate;
    }
}

class LibrarySystem {
    private Stack<BorrowingRecord> borrowingHistory;
    private Stack<Book> returnedBooksStack;
    private Stack<Book> availableBooks;
    
    public LibrarySystem() {
        borrowingHistory = new Stack<>();
        returnedBooksStack = new Stack<>();
        availableBooks = new Stack<>();
        initializeBooks();
    }
    
    private void initializeBooks() {
        availableBooks.push(new Book("978-0451524935", "1984", "George Orwell"));
        availableBooks.push(new Book("978-0141439518", "Pride and Prejudice", "Jane Austen"));
        availableBooks.push(new Book("978-0061120084", "To Kill a Mockingbird", "Harper Lee"));
        availableBooks.push(new Book("978-0743273565", "The Great Gatsby", "F. Scott Fitzgerald"));
    }
    
    public void borrowBook(String borrowerId, String borrowDate) {
        if (availableBooks.isEmpty()) {
            System.out.println("No books available for borrowing!");
            return;
        }
        
        Book bookToBorrow = availableBooks.pop();
        BorrowingRecord record = new BorrowingRecord(bookToBorrow, borrowerId, borrowDate, "Not returned yet");
        borrowingHistory.push(record);
        
        System.out.println("Book borrowed successfully!");
        System.out.println("Borrowed: " + bookToBorrow.getTitle());
    }
    
    public void returnBook(String returnDate) {
        if (borrowingHistory.isEmpty()) {
            System.out.println("No books to return!");
            return;
        }
        
        BorrowingRecord lastRecord = borrowingHistory.peek();
        
        if (lastRecord.getReturnDate().equals("Not returned yet")) {
            BorrowingRecord completedRecord = new BorrowingRecord(
                lastRecord.getBook(), 
                lastRecord.getBorrowerId(), 
                lastRecord.getBorrowDate(), 
                returnDate
            );
            
            borrowingHistory.pop();
            borrowingHistory.push(completedRecord);
            returnedBooksStack.push(completedRecord.getBook());
            
            System.out.println("Book returned successfully!");
            System.out.println("Returned: " + completedRecord.getBook().getTitle());
        } else {
            System.out.println("This book was already returned!");
        }
    }
    
    public void undoLastBorrow() {
        if (borrowingHistory.isEmpty()) {
            System.out.println("No borrowing records to undo!");
            return;
        }
        
        BorrowingRecord lastRecord = borrowingHistory.peek();
        
        if (lastRecord.getReturnDate().equals("Not returned yet")) {
            Book book = lastRecord.getBook();
            availableBooks.push(book);
            borrowingHistory.pop();
            
            System.out.println("Last borrow operation undone!");
            System.out.println("Book '" + book.getTitle() + "' is available again.");
        } else {
            System.out.println("Cannot undo a returned book transaction!");
        }
    }
    
    public void showLastReturnedBook() {
        if (returnedBooksStack.isEmpty()) {
            System.out.println("No books have been returned yet!");
            return;
        }
        
        Book lastReturned = returnedBooksStack.peek();
        System.out.println("Last returned book:");
        System.out.println(lastReturned);
    }
    
    public void displayBorrowingHistory() {
        if (borrowingHistory.isEmpty()) {
            System.out.println("No borrowing history available!");
            return;
        }
        
        System.out.println("\n=== BORROWING HISTORY (Most Recent First) ===");
        Stack<BorrowingRecord> tempStack = new Stack<>();
        
        for (BorrowingRecord record : borrowingHistory) {
            tempStack.push(record);
        }
        
        int count = 1;
        while (!tempStack.isEmpty()) {
            System.out.println(count + ". " + tempStack.pop());
            count++;
        }
    }
    
    public void displayAvailableBooks() {
        if (availableBooks.isEmpty()) {
            System.out.println("No books available in the library!");
            return;
        }
        
        System.out.println("\n=== AVAILABLE BOOKS ===");
        for (Book book : availableBooks) {
            System.out.println(book);
        }
    }
}

public class LibraryStackApplication {
    public static void main(String[] args) {
        LibrarySystem library = new LibrarySystem();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== LIBRARY MANAGEMENT SYSTEM ===");
            System.out.println("1. Borrow a Book");
            System.out.println("2. Return a Book");
            System.out.println("3. Undo Last Borrow");
            System.out.println("4. Show Last Returned Book");
            System.out.println("5. Display Borrowing History");
            System.out.println("6. Display Available Books");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    System.out.print("Enter Borrower ID: ");
                    String borrowerId = scanner.nextLine();
                    System.out.print("Enter Borrow Date (YYYY-MM-DD): ");
                    String borrowDate = scanner.nextLine();
                    library.borrowBook(borrowerId, borrowDate);
                    break;
                    
                case 2:
                    System.out.print("Enter Return Date (YYYY-MM-DD): ");
                    String returnDate = scanner.nextLine();
                    library.returnBook(returnDate);
                    break;
                    
                case 3:
                    library.undoLastBorrow();
                    break;
                    
                case 4:
                    library.showLastReturnedBook();
                    break;
                    
                case 5:
                    library.displayBorrowingHistory();
                    break;
                    
                case 6:
                    library.displayAvailableBooks();
                    break;
                    
                case 7:
                    System.out.println("Exiting Library System. Goodbye!");
                    scanner.close();
                    return;
                    
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}