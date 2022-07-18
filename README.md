# LibraryRESTfulAPI_spring

- [X] Fix the connection between BookInfo to BooksOrderr with @ManyToMany.
- [X] Connect User to order.(to every order should be a user- only one user to one order).
- [X] Fix the problem that in BookOrderr there is only number of order but not the list of books.When we run it postman or swagger-ui we can see only the number of order=9 but there are 2 books in db.(we have 2 variables - numberOfOrder and list of books, but actually we can see only the numberOfOrder,maybe its related to the ManyToMany connection).
- [X] BookInfoController: @DeleteMapping("/book/{id}")-->when we tried delete book that not exist the program failed , @GetMapping("/book/bytitle")-->findByTitle , 
- [X] BookOrderrController: @PostMapping("/order/add")-->add order , @PostMapping("/books") -->add book to order, @GetMapping("/order/{numberOfOrder}/info")-->return links.
- [X] UserController: getUserByFirstName, @GetMapping("/users/fullname")-->getuserByFullName 
- [X] Add methods to the 3 repositories.(Requirement number 2 in the requirements file).
- [ ] Add computational tasks with queries and segmentations(Requirement number 11 in the requirements file).
- [ ] Fix status code 200 in all controllers.
- [ ] Desgin class diagram.
