package com.fourbao.bookbao.backend.service;

import com.fourbao.bookbao.backend.common.exception.BaseException;
import com.fourbao.bookbao.backend.common.response.BaseResponseStatus;
import com.fourbao.bookbao.backend.dto.request.EnrollBookRequest;
import com.fourbao.bookbao.backend.dto.request.UpdateBookRequest;
import com.fourbao.bookbao.backend.dto.response.BookDetailResponse;
import com.fourbao.bookbao.backend.dto.response.SearchBookResponse;
import com.fourbao.bookbao.backend.entity.Book;
import com.fourbao.bookbao.backend.entity.User;
import com.fourbao.bookbao.backend.repository.BookRepository;
import com.fourbao.bookbao.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;


    public void saveBook(HttpServletRequest request, EnrollBookRequest enrollBookRequest) throws BaseException {
        User user = userService.getUser(request);

        Book book = Book.builder()
                .title(enrollBookRequest.getName())
                .author(enrollBookRequest.getAuthor())
                .publisher(enrollBookRequest.getPublisher())
                .price(enrollBookRequest.getPrice())
                .contactEmail(enrollBookRequest.getEmail())
                .dealWay(enrollBookRequest.getDealWay())
                .dealPlace(enrollBookRequest.getPlace())
                .image(enrollBookRequest.getThumbnail())
                .state(enrollBookRequest.getState())
                .askFor(enrollBookRequest.getAskFor())
                .saleState(Book.SaleState.NOT_SOLD)
                .build();

        user.addBook(book);

        try {
            userRepository.save(user);
            bookRepository.save(book);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }


    public List<SearchBookResponse> searchBooks(HttpServletRequest request, String search) throws BaseException {
        User user = userService.getUser(request);

        List<Book> books = bookRepository.findByKeyword(search);
        List<SearchBookResponse> searchBookResponseList = books.stream()
                .map(SearchBookResponse::entityToSearchBookResponse)
                .collect(Collectors.toList());

        if (searchBookResponseList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return searchBookResponseList;
    }


    // 도서 정보 수정 기능
    public void updateBookInfo(HttpServletRequest request, UpdateBookRequest updateBookRequest) throws BaseException {
        User user = userService.getUser(request);

        Book book = bookRepository.findById(updateBookRequest.getId())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_BOOK));

        book.setTitle(updateBookRequest.getName());
        book.setAuthor(updateBookRequest.getAuthor());
        book.setPublisher(updateBookRequest.getPublisher());
        book.setPrice(updateBookRequest.getPrice());
        book.setContactEmail(updateBookRequest.getEmail());
        book.setDealWay(updateBookRequest.getDealWay());
        book.setDealPlace(updateBookRequest.getPlace());
        book.setState(updateBookRequest.getState());
        book.setAskFor(updateBookRequest.getAskFor());

        try {
            bookRepository.save(book);
        } catch (BaseException e) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }


    public BookDetailResponse getBookDetail(HttpServletRequest request, Long bookId) throws BaseException {
        User user = userService.getUser(request);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_BOOK));

        BookDetailResponse bookDetailResponse = BookDetailResponse.builder()
                .name(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .price(book.getPrice())
                .askFor(book.getAskFor())
                .email(book.getContactEmail())
                .thumbnail(book.getImage())
                .dealWay(book.getDealWay())
                .dealPlace(book.getDealPlace())
                .state(book.getState())
                .build();

        return bookDetailResponse;
    }


    public void deleteBook(HttpServletRequest request, Long id) throws BaseException {
        User user = userService.getUser(request);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NON_EXIST_BOOK));

        if (!book.getUser().getId().equals(user.getId())) {
            throw new BaseException(BaseResponseStatus.NON_EXIST_USER);     // 사용자의 도서가 아닌 경우의 예외 처리 필요
        }

        try {
            user.removeBook(book);
            bookRepository.delete(book);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR);
        }
    }
}