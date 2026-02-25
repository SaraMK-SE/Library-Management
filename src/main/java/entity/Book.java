package entity;

import java.time.LocalDateTime;

public class Book {

  private Integer id;
  private String title;
  private String author;
  private String isbn;
  private Boolean isAvailable;
  private LocalDateTime createdAt;

  public Book(){
  }

  public Book(Integer id , String title , String author, String isbn, Boolean isAvailable , LocalDateTime createdAt){
    setId(id);
    setTitle(title);
    setAuthor(author);
    setIsbn(isbn);
    setIsAvailable(isAvailable);
    setCreatedAt(createdAt);
  }

  //getters
  public Integer getId() {
    return id;
  }
  public String getTitle() {
    return title;
  }
  public String getAuthor() {
    return author;
  }
  public String getIsbn() {
    return isbn;
  }
  public Boolean isAvailable() {
    return isAvailable;
  }
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  //setters
  public void setId(Integer id){
    this.id = id;
  }
  public void setTitle(String title){
    this.title = title;
  }
  public void setAuthor(String author){
    this.author = author;
  }
  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }
  public void setIsAvailable(Boolean is_available) {
    this.isAvailable = is_available;
  }
  public void setCreatedAt(LocalDateTime created_at) {
    this.createdAt = created_at;
  }
}
