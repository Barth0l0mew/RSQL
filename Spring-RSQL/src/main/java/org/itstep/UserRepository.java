package org.itstep;

import org.itstep.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Integer>, JpaSpecificationExecutor<Users> {


//            @Query("SELECT u FROM Users u WHERE u.id = ?1")
//            Users findActiveUserById(Integer id);
//
//            @Query(value = "SELECT u FROM Users u WHERE u.name  = :name")
//                    Users findActiveUserByFirstName(@Param("name") String name);
//
//            @Query(value = "SELECT * FROM Users WHERE LAST_NAME = :name", nativeQuery = true)
//            Users findActiveUserByLastName(@Param("name") String firstName);

}