package org.itstep.controller;


import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.apache.catalina.User;
import org.itstep.UserRepository;
import org.itstep.model.Users;
import org.itstep.rsql.CustomRsqlVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {
    /*

    http://localhost:8080/user/filter?search=id==2 только 2
    http://localhost:8080/user/filter?search=id==2,id==3 - ',' -или
    http://localhost:8080/user/filter?search=id=in=(2,3,4,5)
    http://localhost:8080/user/filter?search=name==*ви*
        http://localhost:8080/users?size=10&page=0&sort=surname,asc&search=name==Петр;surname==Агеев
        http://localhost:8080/users?size=10&page=0&sort=surname,asc&search=name!=Иван;surname==Иванов
        http://localhost:8080/users?size=10&page=0&sort=surname,asc&search=name<Антон
        http://localhost:8080/users?size=10&page=0&sort=surname,asc&search=name==В*;surname==И*
        http://localhost:8080/users?size=10&page=0&sort=surname,asc&search=name==*ви*
        http://localhost:8080/users?size=10&page=0&sort=surname,asc&search=name=in=(Юрий,Яков)
         */
    @Autowired
    UserRepository repository;

    @GetMapping("/")
    Iterable<Users> all() {
        return repository.findAll();
    }

    @GetMapping("/filter")
    public List<Users> searchFilter (@RequestParam (value = "search") String search){
        Node rootNode = new RSQLParser().parse(search);
        Specification<Users> spec = rootNode.accept(new CustomRsqlVisitor<Users>());
        return repository.findAll(spec);
    }
    //http://localhost:8080/user/filter2?search=id==2
    //http://localhost:8080/user/filter2
    @RequestMapping(method = RequestMethod.GET, value = "/filter2")
    @ResponseBody
    public List<Users> findByRsql(@RequestParam(value = "search", required = false) String search) {
        if (search != null) {
            Node rootNode = new RSQLParser().parse(search);
            Specification<Users> spec = rootNode.accept(new CustomRsqlVisitor<Users>());
            return repository.findAll(spec);
        } else return repository.findAll();
    }

    //    http://localhost:8080/user/users/filter?page=1&size=10&search=id>100
    @GetMapping(value = "/users/filter", params = {"page", "size", "search"})
    public Page<Users> paginationFilterUsers(@RequestParam("page") int page,
                                            @RequestParam("size") int size,
                                            @RequestParam(value = "search", required = false) String search) {
        Pageable pageable = PageRequest.of(page, size);
        if (search != null) {
            Node rootNode = new RSQLParser().parse(search);
            Specification<Users> spec = rootNode.accept(new CustomRsqlVisitor<Users>());
            return repository.findAll(spec, pageable);
        } else return repository.findAll(pageable);
    }

    //     http://localhost:8080/users?page=10&size=10

    @GetMapping(value = "/users", params = {"page", "size"})
    public Page<Users> paginationUsers(@RequestParam("page") int page,
                                      @RequestParam("size") int size) throws IOException {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }


    //http://localhost:8080/user/users/filter?page=1&size=10&search=id>100&sort=surname
    //desc asc
    @GetMapping(value = "/users/filter", params = {"page", "size", "search", "sort"})
    public Page<Users> paginationFilterUsers(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "sort", required = false) String sort) {

        Sort sortOrder = Sort.unsorted();
        if (sort != null) {
            String[] sortParams = sort.split(",");
            for (String param : sortParams) {
                String[] sortInfo = param.split(":");
                        String property = sortInfo[0];
                Sort.Direction direction = sortInfo.length > 1 && sortInfo[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                sortOrder = sortOrder.and(
                        Sort.by
                                (direction, property));
            }
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        if (search != null) {
            Node rootNode = new RSQLParser().parse(search);
            Specification<Users> spec = rootNode.accept(new CustomRsqlVisitor<Users>());
            return repository.findAll(spec, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }
//    @GetMapping("/search")
//    List<Users> search(@RequestParam(value = "search") String search) {
//        Node rootNode = new RSQLParser().parse(search);
//        Specification<Users> spec = rootNode.accept(new CustomRsqlVisitor<>());
//        return repository.findAll(spec);
//    }
//
//    @GetMapping("/{id}")
//    Optional<Users> one(@PathVariable Integer id) {
//        return repository.findById(id);
//    }

//    @PostMapping("/")
//    Users create(@RequestBody Users user) {
//        return repository.save(user);
//    }
//
//    @PutMapping("/{id}")
//    Users update(@RequestBody Users user, @PathVariable Integer id) {
//        return repository.save(user); //check if exists first ...
//    }
//
//    @DeleteMapping("/{id}")
//    void delete(@PathVariable Integer id) {
//        repository.deleteById(id);
//    }

}