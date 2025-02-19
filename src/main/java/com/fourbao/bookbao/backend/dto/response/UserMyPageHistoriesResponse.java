package com.fourbao.bookbao.backend.dto.response;

import com.fourbao.bookbao.backend.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserMyPageHistoriesResponse {
    private long id;
    private String name;
    private String author;
    private String publisher;
    private int price;
    private String thumbnail;

    public static UserMyPageHistoriesResponse entityToUserMyPageHistoriesResponse(Book book) {
        return new UserMyPageHistoriesResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPrice(),
                book.getImage()
        );
    }
}
