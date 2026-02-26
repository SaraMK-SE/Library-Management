package dto;

public class CreateBookDTO {

  private String title;
  private String author;
  private String isbn;

  //Constructors
  public CreateBookDTO(){
  }
  public CreateBookDTO(String title, String author , String isbn){
    setTitle(title);
    setAuthor(author);
    setIsbn(isbn);
  }

  //Setters
  public void setTitle(String title) {
    this.title = title;
  }
  public void setAuthor(String author) {
    this.author = author;
  }
  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  //Getters
  public String getTitle() {
    return title;
  }
  public String getAuthor() {
    return author;
  }
  public String getIsbn() {
    return isbn;
  }
}
