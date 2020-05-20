package util;

public class BookTM {

    String isbn;
    String title;
    String author;
    String edition;

    public BookTM() {
    }

    public BookTM(String isbn, String title, String author, String edition) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.edition = edition;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getEdition() {
        return edition;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", edition='" + edition + '\'' +
                '}';
    }


}
